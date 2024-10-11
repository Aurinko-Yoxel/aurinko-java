package com.yoxel.aurinko;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpIOExceptionHandler;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.util.ExponentialBackOff;
import com.yoxel.aurinko.apis.CreateSupport;
import com.yoxel.aurinko.apis.DeleteSupport;
import com.yoxel.aurinko.apis.EntityPageApi;
import com.yoxel.aurinko.apis.HttpApi;
import com.yoxel.aurinko.apis.ListSupport_OffsetBased;
import com.yoxel.aurinko.apis.ListSupport_TokenBased;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.apis.ReadSupport;
import com.yoxel.aurinko.apis.SyncSupport;
import com.yoxel.aurinko.apis.UpdateSupport;
import com.yoxel.aurinko.bean.*;
import com.yoxel.aurinko.bean.sub.MeetingResponse;
import com.yoxel.aurinko.dto.AurAccountDto;
import com.yoxel.commons.xstream.XStream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yoxel.aurinko.apis.QueryParams.qp;

public class AurinkoService implements AutoCloseable {

  public static final HttpExecuteInterceptor EMPTY_INTERCEPTOR = request -> {
  };

  private static final String DEFAULT_BASE_URL = "https://aurinko.yoxel.com";

  private final String baseUrl;

  private final HttpTransport httpTransport;

  public static final JsonObjectParser JSON_PARSER =
      new JsonObjectParser(Utils.getDefaultJsonFactory());

  private final HttpRequestInitializer requestInitializer;

  private final Map<String, String> headers;

  private final HttpIOExceptionHandler httpIOExceptionHandler = (request, supportsRetry) -> {
    if (supportsRetry) {
//      log.warn("Handling IOException for {} {}", request.getRequestMethod(),
//               request.getUrl().toString());
      request.setReadTimeout(request.getReadTimeout() + 60000);
    }

    return supportsRetry;
  };

  public enum BodyType {html, text, none}

  private AurinkoService(String baseUrl, Map<String, String> headers, HttpRequestInitializer requestInitializer) {
//    HttpClientBuilder
//        httpClientBuilder =
//        ApacheHttpTransport.newDefaultHttpClientBuilder().setMaxConnPerRoute(10);

    this.baseUrl = baseUrl;
    this.headers = headers;
    this.httpTransport = new ApacheHttpTransport();
    this.requestInitializer = requestInitializer;
  }

  public void close() throws IOException {
    httpTransport.shutdown();
  }

  public static AurinkoService createWithAppAuth(
      String baseUrl,
      String clientId,
      String clientSecret
  ) {
    return create(baseUrl, new BasicAuthentication(clientId, clientSecret));
  }

  public static AurinkoService createWithAppAuth(
      String clientId,
      String clientSecret,
      long accountId
  ) {
    return createWithAppAuth(DEFAULT_BASE_URL, clientId, clientSecret, accountId);
  }

  public static AurinkoService createWithAppAuth(
      String baseUrl,
      String clientId,
      String clientSecret,
      long accountId
  ) {
    return create(
        baseUrl,
        new BasicAuthentication(clientId, clientSecret),
        Map.of("X-Aurinko-Account-Id", String.valueOf(accountId))
    );
  }

  public static AurinkoService createWithAppAuth(String clientId, String clientSecret) {
    return createWithAppAuth(DEFAULT_BASE_URL, clientId, clientSecret);
  }

  public static AurinkoService createWithAccountAuth(String baseUrl, String accessToken) {
    return create(baseUrl, new BearerAuthorization(accessToken));
  }

  public static AurinkoService createWithAccountAuth(String accessToken) {
    return createWithAccountAuth(DEFAULT_BASE_URL, accessToken);
  }

  public static AurinkoService create(HttpExecuteInterceptor httpInterceptor) {
    return create(DEFAULT_BASE_URL, httpInterceptor);
  }

  public static AurinkoService create(String baseUrl) {
    return create(baseUrl, EMPTY_INTERCEPTOR);
  }

  public static AurinkoService create() {
    return create(DEFAULT_BASE_URL, EMPTY_INTERCEPTOR);
  }

  public static AurinkoService create(String baseUrl, HttpExecuteInterceptor httpInterceptor) {
    return create(baseUrl, httpInterceptor, Map.of());
  }

  public static AurinkoService create(
      String baseUrl,
      HttpExecuteInterceptor httpInterceptor,
      Map<String, String> headers
  ) {
    return new AurinkoService(
        baseUrl,
        headers,
        new BackoffInterceptorWrapper(
            httpInterceptor,
            new ExponentialBackOff.Builder()
                .setMultiplier(2.0)
                .build(),
            BackoffInterceptorWrapper.ON_RATE_LIMITING
        ));
  }

  private HttpRequest createRequest(String method, String path) throws IOException {
    HttpRequest
        httpRequest =
        httpTransport.createRequestFactory(requestInitializer) // Utils.getDefaultTransport()
            .buildRequest(method, new GenericUrl((path.startsWith("https://") ? "" : baseUrl + "/v1") + path), null)
            .setParser(JSON_PARSER).setIOExceptionHandler(httpIOExceptionHandler)
            .setNumberOfRetries(5).setConnectTimeout(60 * 1000).setReadTimeout(35 * 1000);

    httpRequest.getHeaders().setUserAgent("Yoxel Sync (Aurinko)/1.0");
    headers.forEach((k, v) -> httpRequest.getHeaders().set(k, v));
//        if ("PATCH".equalsIgnoreCase(method))
//            httpRequest.getHeaders().set("X-HTTP-Method-Override", method);

    return httpRequest;
  }

  public Api api = new Api();
  public Direct direct = new Direct();
  public Subscriptions subscriptions = new Subscriptions();
  public Auth auth = new Auth();
  public Accounts accounts = new Accounts();
  public Users users = new Users();
  public Calendars calendars = new Calendars();
  public TaskLists taskLists = new TaskLists();
  public Emails emails = new Emails();
  public Contacts contacts = new Contacts();

  /**
   * Create a client for /dynamic API, using specific api_configuration ID.
   */
  public <Entity extends AurLiveIdEntity, Page extends AurTokenPage<Entity>> Dynamic<Entity, Page>
  dynamic(AurinkoClass<Entity, Page> clazz, Integer apiConfId) {
    return new Dynamic<>(clazz, apiConfId);
  }

  /**
   * Create a client for /dynamic API, using default api configuration, configured on app-level.
   */
  public <Entity extends AurLiveIdEntity, Page extends AurTokenPage<Entity>> Dynamic<Entity, Page>
  dynamic(AurinkoClass<Entity, Page> clazz) {
    return new Dynamic<>(clazz, null);
  }

  abstract class HttpApiSupport implements HttpApi {

    String basePath() {
      return "";
    }

    @Override
    public HttpRequest httpRequestPrepare(String method, String path, QueryParams queryParams)
        throws IOException {
      return createRequest(method, basePath() + path + queryParams.toUrlString());
    }
  }

  /**
   * Base class for API with CRUD and list operations.
   */
  @RequiredArgsConstructor
  public abstract class FullEntitySupport<Entity, Id, Page, SaveResult>
      extends HttpApiSupport
      implements
      CreateSupport<Entity, Id, SaveResult>,
      ReadSupport<Entity, Id>,
      UpdateSupport<Entity, Id, SaveResult>,
      DeleteSupport<Id>,
      EntityPageApi<Id, Page> {

    private final String entityPath;
    private final Class<Entity> eClass;
    private final Class<Page> pClass;
    private final Class<SaveResult> sClass;


    @Override
    public String entityPath() {
      return entityPath;
    }

    @Override
    public Class<Entity> entityClass() {
      return eClass;
    }

    @Override
    public Class<Page> entityPageClass() {
      return pClass;
    }

    @Override
    public Class<SaveResult> entitySaveResultClass() {
      return sClass;
    }
  }

  /**
   * Base class for APIs with offset-based pagination.
   */
  public abstract class EntitySupport_OffsetBased<Entity, Id, Page extends AurOffsetPage<Entity>, SaveResult>
      extends FullEntitySupport<Entity, Id, Page, SaveResult>
      implements ListSupport_OffsetBased<Entity, Id, Page> {

    EntitySupport_OffsetBased(String entityPath, Class<Entity> eClass, Class<Page> pClass, Class<SaveResult> sClass) {
      super(entityPath, eClass, pClass, sClass);
    }
  }

  /**
   * Base class for APIs with token-based pagination.
   */
  public abstract class EntitySupport_TokenBased<Entity, Id, Page extends AurTokenPage<Entity>, SaveResult>
      extends FullEntitySupport<Entity, Id, Page, SaveResult>
      implements ListSupport_TokenBased<Entity, Id, Page> {

    EntitySupport_TokenBased(String entityPath, Class<Entity> eClass, Class<Page> pClass, Class<SaveResult> sClass) {
      super(entityPath, eClass, pClass, sClass);
    }
  }


  /**
   * Auth API
   */
  public class Auth extends HttpApiSupport {

    public AurAccountToken getToken(String code) throws IOException {
      return httpGet("/auth/token/" + code).parseAs(AurAccountToken.class);
    }
  }

  public class Api extends HttpApiSupport {

  }

  public class Direct extends HttpApiSupport {

    @Override
    String basePath() {
      return "/direct";
    }
  }

  /**
   * Accounts API
   */
  @NoArgsConstructor(access = AccessLevel.PACKAGE)
  public class Accounts extends HttpApiSupport {

    public AurAccount getMe() throws IOException {
      return httpGet("/account").parseAs(AurAccount.class);
    }

    public void deleteMe() throws IOException {
      httpDelete("/account").parseAs(GenericJson.class);
    }

    public AurOAuthClientRegsPage getOAuthClientRegs() throws IOException {
      return httpGet("/am/oauth_regs").parseAs(AurOAuthClientRegsPage.class);
    }

    public AurAccountToken upsertPersonal(AurAccountDto acc) throws IOException {
      return httpPost(
          "/am/accounts",
          QueryParams.of("recycleKeys", "email,clientOrgId"),
          new JsonHttpContent(Utils.getDefaultJsonFactory(), acc)
      ).parseAs(AurAccountToken.class);
    }

    public AurAccountToken upsertService(AurAccountDto svcAcc) throws IOException {
      return httpPost(
          "/am/svc_accounts",
          QueryParams.of("recycleKeys", "clientOrgId"),
          new JsonHttpContent(Utils.getDefaultJsonFactory(), svcAcc)
      ).parseAs(AurAccountToken.class);
    }

    public AurAccountToken upsertGoogleService(AurAccountDto svcAcc) throws IOException {
      return httpPost(
          "/am/svc_accounts",
          QueryParams.of("recycleKeys", "userId,clientOrgId"),
          new JsonHttpContent(Utils.getDefaultJsonFactory(), svcAcc)
      ).parseAs(AurAccountToken.class);
    }

    public AurAccountToken upsertManaged(AurAccountDto acc, long svcAccountId)
        throws IOException {
      return httpPost(
          "/am/svc_accounts/" + svcAccountId + "/accounts",
          QueryParams.of("recycleKeys", "email"),
          new JsonHttpContent(Utils.getDefaultJsonFactory(), acc)
      ).parseAs(AurAccountToken.class);
    }
  }

  public class Users {

    public UserAccounts accounts(String userId) {
      return new UserAccounts(userId);
    }
  }

  @RequiredArgsConstructor
  public class UserAccounts extends HttpApiSupport
      implements ListSupport_OffsetBased<AurAccount, Long, AurAccount.Page> {

    private final String userId;

    @Override
    public Class<AurAccount.Page> entityPageClass() {
      return AurAccount.Page.class;
    }

    @Override
    public String entityPath() {
      return "/users/" + userId + "/accounts";
    }
  }

  /**
   * TaskList API: /tasklists
   */
  public class TaskLists extends EntitySupport_TokenBased<AurTasklist, String, AurTasklistsPage, AurTasklist> {

    TaskLists() {
      super("/tasklists", AurTasklist.class, AurTasklistsPage.class, AurTasklist.class);
    }

    @Override
    public String normalizeId(String id) {
      return id == null ? "default" : id;
    }

    public TasklistEntries tasklistEntries(String tasklistId) {
      return new TasklistEntries(normalizeId(tasklistId));
    }
  }

  /**
   * Events API: /calendars/:id/events
   */
  public class TasklistEntries
      extends EntitySupport_TokenBased<AurTask, String, AurTasksPage, AurTaskSaveResult>
      implements SyncSupport<AurTask, AurTasksPage> {

    private final String tasklistId;

    TasklistEntries(String tasklistId) {
      this(tasklistId, "");
    }

    private TasklistEntries(String tasklistId, String postfix) {
      super("/tasklists/" + tasklistId + "/tasks" + postfix,
            AurTask.class, AurTasksPage.class, AurTaskSaveResult.class);
      this.tasklistId = tasklistId;
    }

    @Override
    public String syncRootPath() {
      return "/tasklists/" + tasklistId;
    }

    public XStream<AurTask, IOException> streamTasks()
        throws IOException {

      return new AurinkoService.TasklistEntries(tasklistId, "").streamPaged();
    }
  }

  /**
   * Calendars API: /calendars
   */
  public class Calendars extends EntitySupport_TokenBased<AurCalendar, String, AurCalendarsPage, AurCalendar> {

    Calendars() {
      super("/calendars", AurCalendar.class, AurCalendarsPage.class, AurCalendar.class);
    }

    @Override
    public String normalizeId(String id) {
      return id == null ? "primary" : id;
    }

    public CalendarEvents calendarEvents(String calendarId) {
      return new CalendarEvents(normalizeId(calendarId));
    }
  }

  /**
   * Events API: /calendars/:id/events
   */
  public class CalendarEvents
      extends EntitySupport_TokenBased<AurEvent, String, AurEventsPage, AurEventSaveResult>
      implements SyncSupport<AurEvent, AurEventsPage> {

    private final String calendarId;

    CalendarEvents(String calendarId) {
      this(calendarId, "");
    }

    private CalendarEvents(String calendarId, String postfix) {
      super("/calendars/" + URLEncoder.encode(calendarId, StandardCharsets.UTF_8) + "/events" + postfix,
            AurEvent.class, AurEventsPage.class, AurEventSaveResult.class);
      this.calendarId = calendarId;
    }

    @Override
    public String syncRootPath() {
      return "/calendars/" + URLEncoder.encode(calendarId, StandardCharsets.UTF_8);
    }

    public XStream<AurEvent, IOException> streamRange(DateTime timeMin, DateTime timeMax)
        throws IOException {

      return new CalendarEvents(calendarId, "/range").streamPaged(
          QueryParams.of(
              qp("timeMin", timeMin.toDateTimeISO()),
              qp("timeMax", timeMax.toDateTimeISO())
          )
      );
    }

    public XStream<AurEvent, IOException> streamFindEvents(List<String> iCalUIds)
        throws IOException {
      // trick to be able to call /find endpoint
      return new CalendarEvents(calendarId, "/find").streamPaged(
          QueryParams.of(
              iCalUIds.stream()
                  .map(iCalUId -> qp("iCalUId", iCalUId))
                  .collect(Collectors.toList())
          )
      );
    }

    public CalendarSeriesOccurrences occurrences(String masterId) {
      return new CalendarSeriesOccurrences(calendarId, masterId);
    }

    public void updateMeetingResponse(String eventId, MeetingResponse response) throws IOException {
      updateMeetingResponse(eventId, response, true);
    }

    public void updateMeetingResponse(String eventId, MeetingResponse response, boolean notifyAttendees)
        throws IOException {
      httpPut(
          entityPath() + "/" + normalizeId(eventId) + "/response",
          QueryParams.of("notifyAttendees", notifyAttendees),
          new JsonHttpContent(Utils.getDefaultJsonFactory(), response)
      ).ignore();
    }
  }

  /**
   * Occurrences API: /calendars/:calendarId/events/:masterId/occurrences
   */
  @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
  public class CalendarSeriesOccurrences extends HttpApiSupport
      implements ListSupport_TokenBased<AurEvent, String, AurEventsPage>,
      ReadSupport<AurEvent, String> {

    private final String calendarId;
    private final String masterId;

    @Override
    public Class<AurEvent> entityClass() {
      return AurEvent.class;
    }

    @Override
    public Class<AurEventsPage> entityPageClass() {
      return AurEventsPage.class;
    }

    @Override
    public String entityPath() {
      return "/calendars/" + calendarId + "/events/" + masterId + "/occurrences";
    }
  }

  /**
   * Email API: /email/messages
   */
  public class Emails extends EntitySupport_TokenBased<AurEmail, String, AurEmailsPage, AurEmail>
      implements SyncSupport<AurEmail, AurEmailsPage> {

    Emails() {
      super("/email/messages", AurEmail.class, AurEmailsPage.class, AurEmail.class);
    }

    public AurContent getAttachment(String msgId, String attachmentId) throws IOException {
      return httpGet("/email/messages/" + msgId + "/attachments/" + attachmentId)
          .parseAs(AurContent.class);
    }

    @Override
    public String syncRootPath() {
      return "/email";
    }

    public EmailConvo conversation(String id) {
      return new EmailConvo(id);
    }

    public EmailTracking tracking() {
      return new EmailTracking();
    }
  }

  /**
   * Email conversations API: /email/conversations/:id
   */
  @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
  public class EmailConvo extends HttpApiSupport implements ListSupport_TokenBased<AurEmail, String, AurEmailsPage> {

    private final String convoId;

    @Override
    public String entityPath() {
      return "/email/conversations/" + convoId;
    }

    @Override
    public Class<AurEmailsPage> entityPageClass() {
      return AurEmailsPage.class;
    }
  }

  public class EmailTracking extends HttpApiSupport
      implements ListSupport_OffsetBased<AurTracking, Long, AurTracking.Page>,
      ReadSupport<AurTracking, Long> {

    @Override
    public Class<AurTracking> entityClass() {
      return AurTracking.class;
    }

    @Override
    public Class<AurTracking.Page> entityPageClass() {
      return AurTracking.Page.class;
    }

    @Override
    public String entityPath() {
      return "/email/tracking";
    }

    public EmailTrackingEvents events() {
      return new EmailTrackingEvents();
    }
  }

  public class EmailTrackingEvents extends HttpApiSupport
      implements ListSupport_OffsetBased<AurTrackingEvent, Long, AurTrackingEvent.Page> {

    @Override
    public Class<AurTrackingEvent.Page> entityPageClass() {
      return AurTrackingEvent.Page.class;
    }

    @Override
    public String entityPath() {
      return "/email/tracking/events";
    }
  }

  /**
   * Contact API: /contacts
   */
  public class Contacts
      extends EntitySupport_TokenBased<AurContact, String, AurContactsPage, AurContactSaveResult>
      implements SyncSupport<AurContact, AurContactsPage> {

    Contacts() {
      super("/contacts", AurContact.class, AurContactsPage.class, AurContactSaveResult.class);
    }

    @Override
    public String syncRootPath() {
      return "/contacts";
    }
  }

  public class Dynamic<Entity extends AurLiveIdEntity, Page extends AurTokenPage<Entity>>
      extends EntitySupport_TokenBased<Entity, String, Page, Entity> {

    Dynamic(AurinkoClass<Entity, Page> aurClass, Integer apiConfId) {
      super(
          "/dynamic/" + (apiConfId == null ? "default" : apiConfId) +
          "/objects/" + aurClass.name, aurClass.entityClass, aurClass.pageClass, aurClass.entityClass);
    }
  }

  public class Subscriptions extends EntitySupport_OffsetBased<AurSubscription, Long, AurSubscriptionsPage, AurSubscription> {

    public Subscriptions() {
      super(
          "/subscriptions",
          AurSubscription.class,
          AurSubscriptionsPage.class,
          AurSubscription.class
      );
    }
  }
}

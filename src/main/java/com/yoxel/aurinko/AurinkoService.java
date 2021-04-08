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
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.util.ExponentialBackOff;

import com.yoxel.aurinko.apis.CrudAndListSupport;
import com.yoxel.aurinko.apis.HttpApi;
import com.yoxel.aurinko.apis.ListSupport;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.apis.ReadSupport;
import com.yoxel.aurinko.apis.SyncSupport;
import com.yoxel.aurinko.bean.AurAccount;
import com.yoxel.aurinko.bean.AurAccountToken;
import com.yoxel.aurinko.bean.AurCalendar;
import com.yoxel.aurinko.bean.AurCalendarsPage;
import com.yoxel.aurinko.bean.AurContact;
import com.yoxel.aurinko.bean.AurContactSaveResult;
import com.yoxel.aurinko.bean.AurContactsPage;
import com.yoxel.aurinko.bean.AurContent;
import com.yoxel.aurinko.bean.AurEmail;
import com.yoxel.aurinko.bean.AurEmailsPage;
import com.yoxel.aurinko.bean.AurEvent;
import com.yoxel.aurinko.bean.AurEventSaveResult;
import com.yoxel.aurinko.bean.AurEventsPage;
import com.yoxel.aurinko.bean.AurLiveIdEntity;
import com.yoxel.aurinko.bean.AurOAuthClientRegsPage;
import com.yoxel.aurinko.bean.AurQueryResult;
import com.yoxel.aurinko.dto.AurAccountDto;
import com.yoxel.commons.xstream.XStream;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import static com.yoxel.aurinko.apis.QueryParams.qp;

public class AurinkoService {

  private static final String BASE_URL = "https://api.aurinko.io/v1";

  private final HttpTransport httpTransport;

  private static final JsonObjectParser JSON_PARSER =
      new JsonObjectParser(Utils.getDefaultJsonFactory());

  private final HttpRequestInitializer requestInitializer;

  private final HttpIOExceptionHandler httpIOExceptionHandler = (request, supportsRetry) -> {
    if (supportsRetry) {
//      log.warn("Handling IOException for {} {}", request.getRequestMethod(),
//               request.getUrl().toString());
      request.setReadTimeout(request.getReadTimeout() + 60000);
    }

    return supportsRetry;
  };

  public enum BodyType {html, text}

  private AurinkoService(HttpRequestInitializer requestInitializer) {
    this.httpTransport = new ApacheHttpTransport();
    this.requestInitializer = requestInitializer;
  }

  public static AurinkoService createWithAppAuth(String clientId, String clientSecret) {
    return createService(new BasicAuthentication(clientId, clientSecret));
  }

  public static AurinkoService createWithAccountAuth(String accessToken) {
    return createService(new BearerAuthorization(accessToken));
  }

  private static AurinkoService createService(HttpExecuteInterceptor httpInterceptor) {
    return new AurinkoService(new BackoffInterceptorWrapper(
        httpInterceptor,
        new ExponentialBackOff(),
        BackoffInterceptorWrapper.ON_RATE_LIMITING
    ));
  }

  private HttpRequest createRequest(String method, String path) throws IOException {
    HttpRequest httpRequest = httpTransport.createRequestFactory(requestInitializer) // Utils.getDefaultTransport()
        .buildRequest(method, new GenericUrl(BASE_URL + path), null)
        .setParser(JSON_PARSER).setIOExceptionHandler(httpIOExceptionHandler).setNumberOfRetries(3)
        .setConnectTimeout(120 * 1000).setReadTimeout(180 * 1000);

    httpRequest.getHeaders().setUserAgent("Aurinko.io/1.0");
//        if ("PATCH".equalsIgnoreCase(method))
//            httpRequest.getHeaders().set("X-HTTP-Method-Override", method);

    return httpRequest;
  }


  public Accounts accounts = new Accounts();
  public Calendars calendars = new Calendars();
  public Emails emails = new Emails();
  public Contacts contacts = new Contacts();

  abstract class HttpApiSupport implements HttpApi {

    @Override
    public HttpRequest httpRequestPrepare(String method, String path, QueryParams queryParams) throws IOException {
      return createRequest(method, path + queryParams.toUrlString());
    }
  }

  @RequiredArgsConstructor
  abstract class BasicEntitySupport<
      Entity extends AurLiveIdEntity,
      Page extends AurQueryResult<Entity>,
      SaveResult
      > extends HttpApiSupport implements CrudAndListSupport<Entity, Page, SaveResult> {

    private final String root;
    private final String ePath;
    private final Class<Entity> eClass;
    private final Class<Page> pClass;
    private final Class<SaveResult> sClass;


    @Override
    public String entityApiRoot() {
      return root;
    }

    @Override
    public String entityPath() {
      return ePath;
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
   * Accounts API
   */
  public class Accounts extends HttpApiSupport {

    public AurAccount getMe() throws IOException {
      return httpGet("/account").parseAs(AurAccount.class);
    }

    public AurOAuthClientRegsPage getOAuthClientRegs() throws IOException {
      return httpGet("/am/oauth_regs").parseAs(AurOAuthClientRegsPage.class);
    }

    public AurAccountToken upsertPersonal(AurAccountDto acc) throws IOException {
      return httpPost("/am/accounts", new JsonHttpContent(Utils.getDefaultJsonFactory(), acc))
          .parseAs(AurAccountToken.class);
    }

    public AurAccountToken upsertService(AurAccountDto svcAcc) throws IOException {
      return httpPost("/am/svc_accounts", new JsonHttpContent(Utils.getDefaultJsonFactory(), svcAcc))
          .parseAs(AurAccountToken.class);
    }

    public AurAccountToken upsertManaged(AurAccountDto acc, long svcAccountId)
        throws IOException {
      return httpPost("/am/svc_accounts/" + svcAccountId + "/accounts",
                      new JsonHttpContent(Utils.getDefaultJsonFactory(), acc))
          .parseAs(AurAccountToken.class);
    }
  }

  /**
   * Calendars API: /calendars
   */
  public class Calendars extends BasicEntitySupport<AurCalendar, AurCalendarsPage, AurCalendar> {

    public Calendars() {
      super("/calendars", "", AurCalendar.class, AurCalendarsPage.class, AurCalendar.class);
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
  public class CalendarEvents extends BasicEntitySupport<AurEvent, AurEventsPage, AurEventSaveResult>
      implements SyncSupport<AurEvent, AurEventsPage> {

    private final String calendarId;

    public CalendarEvents(String calendarId) {
      this(calendarId, false);
    }

    private CalendarEvents(String calendarId, boolean find) {
      super("/calendars/" + calendarId,
            "/events" + (find ? "/find" : ""),
            AurEvent.class, AurEventsPage.class, AurEventSaveResult.class);
      this.calendarId = calendarId;
    }


    public XStream<AurEvent, IOException> streamFindEvents(List<String> iCalUIds)
        throws IOException {
      // trick to be able to call /find endpoint
      return new CalendarEvents(calendarId, true).streamPaged(
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
  }

  /**
   * Occurrences API: /calendars/:calendarId/events/:masterId/occurrences
   */
  @RequiredArgsConstructor
  public class CalendarSeriesOccurrences extends HttpApiSupport
      implements ListSupport<AurEvent, AurEventsPage>,
                 ReadSupport<AurEvent> {

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
    public String entityApiRoot() {
      return "/calendars/" + calendarId + "/events/" + masterId;
    }

    @Override
    public String entityPath() {
      return "/occurrences";
    }
  }

  /**
   * Email API: /email/messages
   */
  public class Emails extends BasicEntitySupport<AurEmail, AurEmailsPage, AurEmail>
      implements SyncSupport<AurEmail, AurEmailsPage> {

    public Emails() {
      super("/email", "/messages", AurEmail.class, AurEmailsPage.class, AurEmail.class);
    }

    public AurContent getAttachment(String msgId, String attachmentId) throws IOException {
      return httpGet("/email/messages/" + msgId + "/attachments/" + attachmentId)
          .parseAs(AurContent.class);
    }

    public EmailConvo conversation(String id) {
      return new EmailConvo(id);
    }
  }

  /**
   * Email conversations API: /email/conversations/:id
   */
  @RequiredArgsConstructor
  public class EmailConvo extends HttpApiSupport implements ListSupport<AurEmail, AurEmailsPage> {

    private final String convoId;

    @Override
    public String entityApiRoot() {
      return "/email/conversations/" + convoId;
    }

    @Override
    public String entityPath() {
      return "";
    }

    @Override
    public Class<AurEmailsPage> entityPageClass() {
      return AurEmailsPage.class;
    }
  }

  /**
   * Contact API: /contacts
   */
  public class Contacts extends BasicEntitySupport<AurContact, AurContactsPage, AurContactSaveResult>
      implements SyncSupport<AurContact, AurContactsPage> {

    public Contacts() {
      super("/contacts", "", AurContact.class, AurContactsPage.class, AurContactSaveResult.class);
    }
  }
}

package com.yoxel.aurinko;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.*;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonObjectParser;
import com.yoxel.aurinko.bean.*;
import com.yoxel.aurinko.dto.AurAccountDto;
import com.yoxel.commons.xstream.IOXStream;
import com.yoxel.commons.xstream.XStream;
import org.joda.time.DateTime;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class AurinkoService {

    private static final String BASE_URL = "https://api.aurinko.io/v1";

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

    private AurinkoService(HttpRequestInitializer requestInitializer) {
        this.requestInitializer = requestInitializer;
    }

    public static AurinkoService createWithAppAuth(String clientId, String clientSecret) {
        return new AurinkoService(new BasicAuthentication(clientId, clientSecret));
    }

    public static AurinkoService createWithAccountAuth(String accessToken) {
        return new AurinkoService(new BearerAuthorization(accessToken));
    }

    private HttpRequest createRequest(String method, String path) throws IOException {
        return Utils.getDefaultTransport()
                .createRequestFactory(requestInitializer)
                .buildRequest(method, new GenericUrl(BASE_URL + path), null)
                .setParser(JSON_PARSER).setIOExceptionHandler(httpIOExceptionHandler).setNumberOfRetries(3)
                .setConnectTimeout(120 * 1000).setReadTimeout(180 * 1000);

        //httpRequest.getHeaders().setUserAgent(SForceAuthorizationCodeFlow.USER_AGENT);
    }

    public AurOAuthClientRegsPage getOAuthClientRegs() throws IOException {
        return createRequest("GET", "/oauth_regs").execute().parseAs(AurOAuthClientRegsPage.class);
    }

    public AurAccount getAccount() throws IOException {
        return createRequest("GET", "/account")
                .execute().parseAs(AurAccount.class);
    }

    public AurAccountToken upsertAccountByEmail(AurAccountDto acc, String clientOrgId)
            throws IOException {
        return createRequest("POST", "/accounts?clientOrgId=" + clientOrgId)
                .setContent(new JsonHttpContent(Utils.getDefaultJsonFactory(), acc))
                .execute().parseAs(AurAccountToken.class);
    }

    public AurAccountToken upsertSvcAccountByType(AurAccountDto svcAcc, String clientOrgId)
            throws IOException {
        return createRequest("POST", "/svc_accounts" + "?clientOrgId=" + clientOrgId)
                .setContent(new JsonHttpContent(Utils.getDefaultJsonFactory(), svcAcc))
                .execute().parseAs(AurAccountToken.class);
    }

    public AurAccountToken upsertManagedAccountByEmail(AurAccountDto acc, String svcToken, String clientOrgId)
            throws IOException {
        return createRequest("POST",
                "/svc_accounts/" + svcToken + "/accounts?clientOrgId=" + clientOrgId)
                .setContent(new JsonHttpContent(Utils.getDefaultJsonFactory(), acc))
                .execute().parseAs(AurAccountToken.class);
    }

    public AurCalendarsPage getCalendars(String nextPageToken) throws IOException {
        return createRequest("GET", "/calendars" + tokenParams(null, nextPageToken)).execute().parseAs(AurCalendarsPage.class);
    }

    public AurCalendar getCalendar(String id) throws IOException {
        return createRequest("GET", "/calendars/" + id).execute().parseAs(AurCalendar.class);
    }

    public XStream<AurCalendar, IOException> streamCalendars(Consumer<? super AurCalendarsPage> onPage) throws IOException {

        if (onPage == null) {
            onPage = v -> {
            };
        }

        AurCalendarsPage firstPage = getCalendars(null);

        // query pages, until we get a page with done=true | totalSize=0
        return IOXStream.iterateUntil(
                firstPage,
                qr -> getCalendars(qr.getNextPageToken()),
                qr -> qr.getLength() == 0 || qr.getNextPageToken() == null
        )
                .filter(qr -> qr.getRecords() != null) // this can happen
                .peek(onPage) // execute action on each page
                .map(AurCalendarsPage::getRecords)
                .flatMap(IOXStream::of);
    }

    public AurSyncStatus startCalendarSync(String calendarId, DateTime timeMin, DateTime timeMax) throws IOException {
        return createRequest("POST", "/calendars/" + (calendarId == null ? "primary" : calendarId)
                + "/sync?timeMin=" + timeMin.toDateTimeISO() + "&timeMax=" + timeMax.toDateTimeISO()).execute().parseAs(AurSyncStatus.class);
    }

    private String tokenParams(String deltaToken, String nextPageToken) {

        if (nextPageToken != null) {
            return "?nextPageToken=" + nextPageToken;
        }

        if (deltaToken != null) {
            return "?deltaToken=" + deltaToken;
        }

        return "";
    }

    public AurEventsPage calSyncUpdated(String calendarId, String deltaToken, String nextPageToken) throws IOException {
        return createRequest("GET", "/calendars/" + (calendarId == null ? "primary" : calendarId) + "/sync/updated" + tokenParams(deltaToken, nextPageToken))
                .execute().parseAs(AurEventsPage.class);
    }

    public AurEventsPage calSyncDeleted(String calendarId, String deltaToken, String nextPageToken) throws IOException {
        return createRequest("GET", "/calendars/" + (calendarId == null ? "primary" : calendarId) + "/sync/deleted" + tokenParams(deltaToken, nextPageToken))
                .execute().parseAs(AurEventsPage.class);
    }

    public AurEvent getCalendarEvent(String calendarId, String eventId) throws IOException {
        return createRequest("GET", "/calendars/" + (calendarId == null ? "primary" : calendarId) + "/events/" + eventId)
                .execute().parseAs(AurEvent.class);
    }

    public XStream<AurEvent, IOException> streamDeletedEvents(String calendarId, String deltaToken, Consumer<? super AurEventsPage> onPage, Predicate<? super AurEventsPage> stopWhen) throws IOException {
        return streamCalendarSync(true, calendarId, deltaToken, onPage, stopWhen);
    }

    public XStream<AurEvent, IOException> streamUpdatedEvents(String calendarId, String deltaToken, Consumer<? super AurEventsPage> onPage, Predicate<? super AurEventsPage> stopWhen) throws IOException {
        return streamCalendarSync(false, calendarId, deltaToken, onPage, stopWhen);
    }

    private XStream<AurEvent, IOException>
    streamCalendarSync(boolean deleted, String calendarId, String deltaToken, Consumer<? super AurEventsPage> onPage, Predicate<? super AurEventsPage> stopWhen) throws IOException {

        if (onPage == null) {
            onPage = v -> {
            };
        }

        if (stopWhen == null) {
            stopWhen = v -> false;
        }

        AurEventsPage firstPage = deleted ? calSyncDeleted(calendarId, deltaToken, null) : calSyncUpdated(calendarId, deltaToken, null);

        // query pages, until we get a page with done=true | totalSize=0
        Predicate<? super AurEventsPage> finalStopWhen = stopWhen;
        return IOXStream.iterateUntil(
                firstPage,
                qr -> deleted ? calSyncDeleted(calendarId, deltaToken, qr.getNextPageToken()) : calSyncUpdated(calendarId, deltaToken, qr.getNextPageToken()),
                qr -> qr.getNextPageToken() == null || finalStopWhen.test(qr)
        )
                .filter(qr -> qr.getRecords() != null) // this can happen
                .peek(onPage) // execute action on each page
                .map(AurEventsPage::getRecords)
                .flatMap(IOXStream::of);
    }

    public AurEmail getEmailMessage(String id, String bodyType) throws IOException {
        return createRequest("GET", "/email/messages/" + id + (bodyType == null ? "" : "?bodyType=" + bodyType)).execute().parseAs(AurEmail.class);
    }

    public AurEmailsPage getEmailMessages(String query, String bodyType, String pageToken) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (query != null)
            sb.append("q=" + URLEncoder.encode(query, "utf8"));

        if (bodyType != null) {
            if (sb.length() > 0)
                sb.append("&");
            sb.append("bodyType=" + bodyType);
        }

        if (pageToken != null) {
            if (sb.length() > 0)
                sb.append("&");
            sb.append("pageToken=" + pageToken);
        }
        return createRequest("GET", "/email/messages" + (sb.length() > 0 ? "?" + sb.toString() : ""))
                .execute().parseAs(AurEmailsPage.class);
    }

    private XStream<AurEmail, IOException> streamEmailQuery(String query, String bodyType) throws IOException {

        AurEmailsPage firstPage = getEmailMessages(query, bodyType, null);

        // query pages, until we get a page with done=true | totalSize=0
        return IOXStream.iterateUntil(
                firstPage,
                qr -> getEmailMessages(query, bodyType, qr.getNextPageToken()),
                qr -> qr.getNextPageToken() == null
        )
                .filter(qr -> qr.getRecords() != null) // this can happen
                .map(AurEmailsPage::getRecords)
                .flatMap(IOXStream::of);
    }

    public AurSyncStatus startMailSync(Integer days) throws IOException {
        return createRequest("POST", "/email/sync" + (days == null ? "" : "?daysWithin" + days)).execute().parseAs(AurSyncStatus.class);
    }

    public AurEmailsPage mailSyncUpdated(String deltaToken, String nextPageToken) throws IOException {
        return createRequest("GET", "/email/sync/updated" + tokenParams(deltaToken, nextPageToken))
                .execute().parseAs(AurEmailsPage.class);
    }

    public AurEmailsPage mailSyncDeleted(String deltaToken, String nextPageToken) throws IOException {
        return createRequest("GET", "/email/sync/deleted" + tokenParams(deltaToken, nextPageToken))
                .execute().parseAs(AurEmailsPage.class);
    }

    public XStream<AurEmail, IOException> streamDeletedEmails(String deltaToken, Consumer<? super AurEmailsPage> onPage, Predicate<? super AurEmailsPage> stopWhen) throws IOException {
        return streamEmailSync(true, deltaToken, onPage, stopWhen);
    }

    public XStream<AurEmail, IOException> streamUpdatedEmails(String deltaToken, Consumer<? super AurEmailsPage> onPage, Predicate<? super AurEmailsPage> stopWhen) throws IOException {
        return streamEmailSync(false, deltaToken, onPage, stopWhen);
    }

    private XStream<AurEmail, IOException> streamEmailSync(boolean deleted, String deltaToken, Consumer<? super AurEmailsPage> onPage, Predicate<? super AurEmailsPage> stopWhen) throws IOException {

        if (onPage == null) {
            onPage = v -> {
            };
        }

        if (stopWhen == null) {
            stopWhen = v -> false;
        }

        AurEmailsPage firstPage = deleted ? mailSyncDeleted(deltaToken, null) : mailSyncUpdated(deltaToken, null);

        // query pages, until we get a page with done=true | totalSize=0
        Predicate<? super AurEmailsPage> finalStopWhen = stopWhen;
        return IOXStream.iterateUntil(
                firstPage,
                qr -> deleted ? mailSyncDeleted(deltaToken, qr.getNextPageToken()) : mailSyncUpdated(deltaToken, qr.getNextPageToken()),
                qr -> qr.getNextPageToken() == null || finalStopWhen.test(qr)
        )
                .filter(qr -> qr.getRecords() != null) // this can happen
                .peek(onPage) // execute action on each page
                .map(AurEmailsPage::getRecords)
                .flatMap(IOXStream::of);
    }
}

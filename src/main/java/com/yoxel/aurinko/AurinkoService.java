package com.yoxel.aurinko;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.*;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
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

    private static final HttpTransport HTTP_TRANSPORT = new ApacheHttpTransport();

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
        this.requestInitializer = requestInitializer;
    }

    public static AurinkoService createWithAppAuth(String clientId, String clientSecret) {
        return new AurinkoService(new BasicAuthentication(clientId, clientSecret));
    }

    public static AurinkoService createWithAccountAuth(String accessToken) {
        return new AurinkoService(new BearerAuthorization(accessToken));
    }

    public static boolean isNotFound404(IOException e) {
        if (HttpResponseException.class.isInstance(e)) {
            if (((HttpResponseException) e).getStatusCode() == 404) {
                return true;
            }
        }

        return false;
    }

    public static boolean isGone410(IOException e) {
        if (HttpResponseException.class.isInstance(e)) {
            if (((HttpResponseException) e).getStatusCode() == 410) {
                return true;
            }
        }

        return false;
    }

    private HttpRequest createRequest(String method, String path) throws IOException {
        HttpRequest httpRequest = HTTP_TRANSPORT.createRequestFactory(requestInitializer) // Utils.getDefaultTransport()
                .buildRequest(method, new GenericUrl(BASE_URL + path), null)
                .setParser(JSON_PARSER).setIOExceptionHandler(httpIOExceptionHandler).setNumberOfRetries(3)
                .setConnectTimeout(120 * 1000).setReadTimeout(180 * 1000);

        httpRequest.getHeaders().setUserAgent("Aurinko.io/1.0");
//        if ("PATCH".equalsIgnoreCase(method))
//            httpRequest.getHeaders().set("X-HTTP-Method-Override", method);

        return httpRequest;
    }

    public AurAccount getAccount() throws IOException {
        return createRequest("GET", "/account").execute().parseAs(AurAccount.class);
    }

    public AurOAuthClientRegsPage getOAuthClientRegs() throws IOException {
        return createRequest("GET", "/am/oauth_regs").execute().parseAs(AurOAuthClientRegsPage.class);
    }

    public AurAccountToken upsertUserAccount(AurAccountDto acc) throws IOException {
        return createRequest("PATCH", "/am/accounts")
                .setContent(new JsonHttpContent(Utils.getDefaultJsonFactory(), acc))
                .execute().parseAs(AurAccountToken.class);
    }

    public AurAccountToken upsertServiceAccount(AurAccountDto svcAcc) throws IOException {
        return createRequest("PATCH", "/am/svc_accounts")
                .setContent(new JsonHttpContent(Utils.getDefaultJsonFactory(), svcAcc))
                .execute().parseAs(AurAccountToken.class);
    }

    public AurAccountToken upsertManagedAccount(AurAccountDto acc, long svcAccountId)
            throws IOException {
        return createRequest("PATCH", "/am/svc_accounts/" + svcAccountId + "/accounts")
                .setContent(new JsonHttpContent(Utils.getDefaultJsonFactory(), acc))
                .execute().parseAs(AurAccountToken.class);
    }

    public AurCalendar getCalendar(String id) throws IOException {
        return createRequest("GET", "/calendars/" + id).execute().parseAs(AurCalendar.class);
    }

    public AurCalendar updateCalendar(String id, String name) throws IOException {
        final AurCalendar cal = new AurCalendar();
        cal.setName(name);
        return createRequest("PATCH", "/calendars/" + id)
                .setContent(new JsonHttpContent(Utils.getDefaultJsonFactory(), cal))
                .execute().parseAs(AurCalendar.class);
    }

    public AurCalendar createCalendar(String name, String color) throws IOException {
        final AurCalendar cal = new AurCalendar();
        cal.setName(name);
        cal.setColor(color);
        return createRequest("POST", "/calendars")
                .setContent(new JsonHttpContent(Utils.getDefaultJsonFactory(), cal))
                .execute().parseAs(AurCalendar.class);
    }

    public AurCalendarsPage getCalendars(String pageToken) throws IOException {
        return createRequest("GET", "/calendars" + tokenParams(null, pageToken)).execute().parseAs(AurCalendarsPage.class);
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

    private String tokenParams(String deltaToken, String pageToken) {

        if (pageToken != null) {
            return "?pageToken=" + pageToken;
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

    public AurEvent getCalendarOccurrence(String calendarId, String masterId, String origStart) throws IOException {
        return createRequest("GET", "/calendars/" + (calendarId == null ? "primary" : calendarId) + "/events/" + masterId + "/occurrences/" + origStart)
                .execute().parseAs(AurEvent.class);
    }

    public AurEventsPage getCalendarSeries(String calendarId, String masterId, String pageToken) throws IOException {
        return createRequest("GET", "/calendars/" + (calendarId == null ? "primary" : calendarId) + "/events/" + masterId + "/series" + tokenParams(null, pageToken))
                .execute().parseAs(AurEventsPage.class);
    }

    public AurEventSaveResult updateCalendarEvent(String calendarId, String eventId, AurEvent event, boolean notifyAttendees) throws IOException {
        return createRequest("PATCH", "/calendars/" + calendarId + "/events/" + eventId + (notifyAttendees ? "?notifyAttendees=true" : ""))
                .setContent(new JsonHttpContent(Utils.getDefaultJsonFactory(), event))
                .execute().parseAs(AurEventSaveResult.class);
    }

    public AurEventSaveResult createCalendarEvent(String calendarId, AurEvent event, boolean notifyAttendees) throws IOException {
        return createRequest("POST", "/calendars/" + calendarId + "/events" + (notifyAttendees ? "?notifyAttendees=true" : ""))
                .setContent(new JsonHttpContent(Utils.getDefaultJsonFactory(), event))
                .execute().parseAs(AurEventSaveResult.class);
    }

    public void deleteCalendarEvent(String calendarId, String eventId, boolean notifyAttendees) throws IOException {
        createRequest("DELETE", "/calendars/" + calendarId + "/events/" + eventId + (notifyAttendees ? "?notifyAttendees=true" : "")).execute();
    }

    public XStream<AurEvent, IOException> streamDeletedEvents(String calendarId, String pageOrDelta, Consumer<? super AurEventsPage> onPage, Predicate<? super AurEventsPage> stopWhen) throws IOException {
        return streamCalendarSync(true, calendarId, pageOrDelta, onPage, stopWhen);
    }

    public XStream<AurEvent, IOException> streamUpdatedEvents(String calendarId, String pageOrDelta, Consumer<? super AurEventsPage> onPage, Predicate<? super AurEventsPage> stopWhen) throws IOException {
        return streamCalendarSync(false, calendarId, pageOrDelta, onPage, stopWhen);
    }

    private XStream<AurEvent, IOException>
    streamCalendarSync(boolean deleted, String calendarId, String pageOrDelta, Consumer<? super AurEventsPage> onPage, Predicate<? super AurEventsPage> stopWhen) throws IOException {

        if (onPage == null) {
            onPage = v -> {
            };
        }

        if (stopWhen == null) {
            stopWhen = v -> false;
        }

        final String deltaToken, pageToken;
        if (pageOrDelta.startsWith("page:")) {
            deltaToken = null;
            pageToken = pageOrDelta.substring(5);
        } else {
            deltaToken = pageOrDelta;
            pageToken = null;
        }

        AurEventsPage firstPage = deleted ? calSyncDeleted(calendarId, deltaToken, pageToken) : calSyncUpdated(calendarId, deltaToken, pageToken);

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

    public AurEmail getEmailMessage(String id, BodyType bodyType, boolean loadInlines) throws IOException {
        final StringBuilder sb = new StringBuilder();

        if (bodyType != null) {
            if (sb.length() > 0)
                sb.append("&");
            sb.append("bodyType=" + bodyType.name());
        }

        if (loadInlines) {
            if (sb.length() > 0)
                sb.append("&");
            sb.append("loadInlines=true");
        }

        return createRequest("GET", "/email/messages/" + id + (sb.length() > 0 ? "?" + sb.toString() : "")).execute().parseAs(AurEmail.class);
    }

    public AurEmailsPage getEmailThread(String id, BodyType bodyType, String pageToken) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (bodyType != null) {
            if (sb.length() > 0)
                sb.append("&");
            sb.append("bodyType=" + bodyType.name());
        }

        if (pageToken != null) {
            if (sb.length() > 0)
                sb.append("&");
            sb.append("pageToken=" + pageToken);
        }

        return createRequest("GET", "/email/conversations/" + id + (sb.length() > 0 ? "?" + sb.toString() : "")).execute().parseAs(AurEmailsPage.class);
    }

    public XStream<AurEmail, IOException> streamEmailThread(String threadId, BodyType bodyType) throws IOException {

        AurEmailsPage firstPage = getEmailThread(threadId, bodyType, null);

        // query pages, until we get a page with done=true | totalSize=0
        return IOXStream.iterateUntil(
                firstPage,
                qr -> getEmailThread(threadId, bodyType, qr.getNextPageToken()),
                qr -> qr.getNextPageToken() == null
        )
                .filter(qr -> qr.getRecords() != null) // this can happen
                .map(AurEmailsPage::getRecords)
                .flatMap(IOXStream::of);
    }

    public AurEmailsPage getEmailMessages(String query, BodyType bodyType, String pageToken) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (query != null)
            sb.append("q=" + URLEncoder.encode(query, "utf8"));

        if (bodyType != null) {
            if (sb.length() > 0)
                sb.append("&");
            sb.append("bodyType=" + bodyType.name());
        }

        if (pageToken != null) {
            if (sb.length() > 0)
                sb.append("&");
            sb.append("pageToken=" + pageToken);
        }
        return createRequest("GET", "/email/messages" + (sb.length() > 0 ? "?" + sb.toString() : ""))
                .execute().parseAs(AurEmailsPage.class);
    }

    public XStream<AurEmail, IOException> streamEmailQuery(String query, BodyType bodyType) throws IOException {

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

    public AurSyncStatus startMailSync(Integer days, BodyType bodyType) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (days != null)
            sb.append("daysWithin=" + days);

        if (bodyType != null) {
            if (sb.length() > 0)
                sb.append("&");
            sb.append("bodyType=" + bodyType.name());
        }

        return createRequest("POST", "/email/sync" + (sb.length() > 0 ? "?" + sb.toString() : "")).execute().parseAs(AurSyncStatus.class);
    }

    public AurEmailsPage mailSyncUpdated(String deltaToken, String nextPageToken) throws IOException {
        return createRequest("GET", "/email/sync/updated" + tokenParams(deltaToken, nextPageToken))
                .execute().parseAs(AurEmailsPage.class);
    }

    public AurEmailsPage mailSyncDeleted(String deltaToken, String nextPageToken) throws IOException {
        return createRequest("GET", "/email/sync/deleted" + tokenParams(deltaToken, nextPageToken))
                .execute().parseAs(AurEmailsPage.class);
    }

    public XStream<AurEmail, IOException> streamDeletedEmails(String pageOrDelta, Consumer<? super AurEmailsPage> onPage, Predicate<? super AurEmailsPage> stopWhen) throws IOException {
        return streamEmailSync(true, pageOrDelta, onPage, stopWhen);
    }

    public XStream<AurEmail, IOException> streamUpdatedEmails(String pageOrDelta, Consumer<? super AurEmailsPage> onPage, Predicate<? super AurEmailsPage> stopWhen) throws IOException {
        return streamEmailSync(false, pageOrDelta, onPage, stopWhen);
    }

    private XStream<AurEmail, IOException> streamEmailSync(boolean deleted, String pageOrDelta, Consumer<? super AurEmailsPage> onPage, Predicate<? super AurEmailsPage> stopWhen) throws IOException {

        if (onPage == null) {
            onPage = v -> {
            };
        }

        if (stopWhen == null) {
            stopWhen = v -> false;
        }

        final String deltaToken, pageToken;
        if (pageOrDelta.startsWith("page:")) {
            deltaToken = null;
            pageToken = pageOrDelta.substring(5);
        } else {
            deltaToken = pageOrDelta;
            pageToken = null;
        }

        AurEmailsPage firstPage = deleted ? mailSyncDeleted(deltaToken, pageToken) : mailSyncUpdated(deltaToken, pageToken);

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

    public AurContent getEmailAttachment(String msgId, String attachmentId) throws IOException {
        return createRequest("GET", "/email/messages/" + msgId + "/attachments/" + attachmentId).execute().parseAs(AurContent.class);
    }
}

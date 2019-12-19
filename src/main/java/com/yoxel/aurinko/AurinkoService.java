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
import java.util.function.Consumer;

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

    public AurAccount getAccount() throws IOException {
        return createRequest("GET", "/account")
                .execute().parseAs(AurAccount.class);
    }

    public AurAccountToken upsertSvcAccount(AurAccountDto svcAcc, String clientOrgId, String svcToken)
            throws IOException {
        return createRequest("POST", "/svc_accounts" + "?clientOrgId=" + clientOrgId + (svcToken == null ? "" : "&svcToken=" + svcToken))
                .setContent(new JsonHttpContent(Utils.getDefaultJsonFactory(), svcAcc))
                .execute().parseAs(AurAccountToken.class);
    }

    public AurAccountToken upsertDaemonFlowAccount(AurAccountDto acc, String svcToken, String clientOrgId)
            throws IOException {
        return createRequest("POST",
                "/svc_accounts/" + svcToken + "/accounts?clientOrgId=" + clientOrgId)
                .setContent(new JsonHttpContent(Utils.getDefaultJsonFactory(), acc))
                .execute().parseAs(AurAccountToken.class);
    }

    public AurAccountToken upsertAccountByEmail(AurAccountDto acc, String clientOrgId)
            throws IOException {
        return createRequest("POST", "/accounts?clientOrgId=" + clientOrgId)
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

    public AurSyncStatus startMailSync(DateTime timeMin) throws IOException {
        return createRequest("POST", "/mailbox/sync?timeMin=" + timeMin.toDateTimeISO()).execute().parseAs(AurSyncStatus.class);
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
        return createRequest("GET", "/calendars/"+ (calendarId == null ? "primary" : calendarId)+"/sync/updated" + tokenParams(deltaToken, nextPageToken))
                .execute().parseAs(AurEventsPage.class);
    }

    public AurEventsPage calSyncDeleted(String calendarId, String deltaToken, String nextPageToken) throws IOException {
        return createRequest("GET", "/calendars/"+ (calendarId == null ? "primary" : calendarId)+"/sync/deleted" + tokenParams(deltaToken, nextPageToken))
                .execute().parseAs(AurEventsPage.class);
    }

    public AurEmailsPage mailSync(String deltaToken, String nextPageToken) throws IOException {
        return createRequest("GET", "/mailbox/sync/updated" + tokenParams(deltaToken, nextPageToken))
                .execute().parseAs(AurEmailsPage.class);
    }

    public AurEmailsPage mailDeleted(String deltaToken, String nextPageToken) throws IOException {
        return createRequest("GET", "/mailbox/sync/deleted" + tokenParams(deltaToken, nextPageToken))
                .execute().parseAs(AurEmailsPage.class);
    }

    public XStream<AurEvent, IOException> streamDeletedEvents(String calendarId, String deltaToken, Consumer<? super AurEventsPage> onPage) throws IOException {
        return streamCalendarSync(true, calendarId, deltaToken, onPage);
    }

    public XStream<AurEvent, IOException> streamUpdatedEvents(String calendarId, String deltaToken, Consumer<? super AurEventsPage> onPage) throws IOException {
        return streamCalendarSync(false, calendarId, deltaToken, onPage);
    }

    private XStream<AurEvent, IOException>
    streamCalendarSync(boolean deleted, String calendarId, String deltaToken, Consumer<? super AurEventsPage> onPage) throws IOException {

        if (onPage == null) {
            onPage = v -> {
            };
        }

        AurEventsPage firstPage = deleted ? calSyncDeleted(calendarId, deltaToken, null) : calSyncUpdated(calendarId, deltaToken, null);

        // query pages, until we get a page with done=true | totalSize=0
        return IOXStream.iterateUntil(
                firstPage,
                qr -> deleted ? calSyncDeleted(calendarId, deltaToken, qr.getNextPageToken()) : calSyncUpdated(calendarId, deltaToken, qr.getNextPageToken()),
                qr -> qr.getLength() == 0 || qr.getNextPageToken() == null
        )
                .filter(qr -> qr.getRecords() != null) // this can happen
                .peek(onPage) // execute action on each page
                .map(AurEventsPage::getRecords)
                .flatMap(IOXStream::of);
    }
}

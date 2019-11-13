package com.yoxel.aurinko;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.*;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonObjectParser;
import com.yoxel.aurinko.dto.AurAccountDto;
import com.yoxel.aurinko.dto.AurEmailsPage;
import com.yoxel.aurinko.dto.AurEventsPage;
import com.yoxel.aurinko.dto.AurTokenDto;

import java.io.IOException;

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

    public AurAccountDto getAccount() throws IOException {
        return createRequest("GET", "/account")
                .execute().parseAs(AurAccountDto.class);
    }

    public AurTokenDto upsertSvcAccount(AurAccountDto svcAcc, String clientOrgId, String svcToken)
            throws IOException {
        return createRequest("POST", "/svc_accounts" + "?clientOrgId=" + clientOrgId + (svcToken == null ? "" : "&svcToken=" + svcToken))
                .setContent(new JsonHttpContent(Utils.getDefaultJsonFactory(), svcAcc))
                .execute().parseAs(AurTokenDto.class);
    }

    public AurTokenDto upsertDaemonFlowAccount(AurAccountDto acc, String svcToken, String clientOrgId)
            throws IOException {
        return createRequest("POST",
                "/svc_accounts/" + svcToken + "/accounts?clientOrgId=" + clientOrgId)
                .setContent(new JsonHttpContent(Utils.getDefaultJsonFactory(), acc))
                .execute().parseAs(AurTokenDto.class);
    }

    public AurTokenDto upsertAccountByEmail(AurAccountDto acc, String clientOrgId)
            throws IOException {
        return createRequest("POST", "/accounts?clientOrgId=" + clientOrgId)
                .setContent(new JsonHttpContent(Utils.getDefaultJsonFactory(), acc))
                .execute().parseAs(AurTokenDto.class);
    }

    private String tokenParams(String deltaToken, String nextPageToken) {
        StringBuilder sb = new StringBuilder();
        if (deltaToken != null) {
            sb.append("deltaToken=" + deltaToken);
        }

        if (nextPageToken != null) {
            if (sb.length() > 0)
                sb.append("&");

            sb.append("nextPageToken=" + nextPageToken);
        }

        return sb.length() > 0 ? "?" + sb.toString() : "";
    }

    public AurEventsPage calendarSync(String deltaToken, String nextPageToken) throws IOException {
        return createRequest("GET", "/calendar/sync" + tokenParams(deltaToken, nextPageToken))
                .execute().parseAs(AurEventsPage.class);
    }

    public AurEventsPage calendarDeleted(String deltaToken, String nextPageToken) throws IOException {
        return createRequest("GET", "/calendar/syncDeleted" + tokenParams(deltaToken, nextPageToken))
                .execute().parseAs(AurEventsPage.class);
    }

    public AurEmailsPage mailSync(String deltaToken, String nextPageToken) throws IOException {
        return createRequest("GET", "/mailbox/sync" + tokenParams(deltaToken, nextPageToken))
                .execute().parseAs(AurEmailsPage.class);
    }

    public AurEmailsPage mailDeleted(String deltaToken, String nextPageToken) throws IOException {
        return createRequest("GET", "/mailbox/syncDeleted" + tokenParams(deltaToken, nextPageToken))
                .execute().parseAs(AurEmailsPage.class);
    }
}

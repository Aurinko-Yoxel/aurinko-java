package com.yoxel.aurinko;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.testing.http.MockHttpTransport;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AurinkoHttpExceptionTest {

    private static HttpRequest request(String method, String url) throws IOException {
        return new MockHttpTransport().createRequestFactory().buildRequest(method, new GenericUrl(url), null);
    }

    private static HttpResponseException httpError(int status, String statusMessage, String content) {
        return new HttpResponseException.Builder(status, statusMessage, new HttpHeaders())
                .setContent(content)
                .setMessage(status + " " + statusMessage + (content == null ? "" : "\n" + content))
                .build();
    }

    private static String firstLine(Throwable t) {
        return t.getMessage().split("\\R", 2)[0];
    }

    @Test
    void aurinkoEnvelope403() throws IOException {
        String body = """
                {
                  "code": "forbidden",
                  "message": "Forbidden",
                  "requestId": "8e107126-c44a-4943-8a39-73384cb4dafe",
                  "originalError": {
                    "error": {
                      "code": "ErrorAccessDenied",
                      "message": "Access is denied. Check credentials and try again."
                    }
                  }
                }
                """;

        AurinkoHttpException e = AurinkoHttpException.of(
                request("GET", "https://aurinko.yoxel.com/v1/calendars/17/events?email=user%40acme.com"),
                httpError(403, "Forbidden", body));

        assertEquals("403 Forbidden on GET /v1/calendars/17/events"
                     + " - Access is denied. Check credentials and try again."
                     + " [forbidden, request 8e107126-c44a-4943-8a39-73384cb4dafe]",
                     firstLine(e));

        assertEquals("GET", e.getRequestMethod());
        assertEquals("/v1/calendars/17/events", e.getRequestPath());
        assertEquals("forbidden", e.getErrorCode());
        assertEquals("8e107126-c44a-4943-8a39-73384cb4dafe", e.getRequestId());

        assertFalse(e.getMessage().contains("email="));
        assertTrue(HttpErrors.isForbidden403(e));
        assertEquals("Forbidden", e.getStatusMessage());
        assertTrue(e.getContent().contains("ErrorAccessDenied"));
        assertNull(e.getCause());
    }

    @Test
    void events404() throws IOException {
        AurinkoHttpException e = AurinkoHttpException.of(
                request("GET", "https://aurinko.yoxel.com/v1/calendars/17/events/9"),
                httpError(404, "Not Found",
                          "{\"code\":\"not_found\",\"message\":\"Event not found\",\"requestId\":\"req-1\"}"));

        assertEquals("404 Not Found on GET /v1/calendars/17/events/9"
                     + " - Event not found [not_found, request req-1]",
                     firstLine(e));

        assertTrue(HttpErrors.isNotFound404(e));
    }

    @Test
    void graphTranscriptsDisabled403() throws IOException {
        String body = """
                {"error":{
                  "code": "Forbidden",
                  "message": "Graph API access to transcripts is disabled for this tenant.",
                  "innerError": {
                    "code": "GraphAccessToTranscriptsDisabled",
                    "message": "Graph API access to transcripts is disabled for this tenant.",
                    "date": "2026-07-30T19:27:31",
                    "request-id": "9f730c78-5cad-462c-b0c7-063f99264847"
                  }}}
                """;

        AurinkoHttpException e = AurinkoHttpException.of(
                request("GET", "https://aurinko.yoxel.com/v1/direct/me/onlineMeetings/AAA/transcripts"),
                httpError(403, "Forbidden", body));

        assertEquals("403 Forbidden on GET /v1/direct/me/onlineMeetings/AAA/transcripts"
                     + " - Graph API access to transcripts is disabled for this tenant."
                     + " [GraphAccessToTranscriptsDisabled]",
                     firstLine(e));

        assertEquals("GraphAccessToTranscriptsDisabled", e.getErrorCode());
    }
}

package com.yoxel.aurinko.api;

import com.google.api.client.http.HttpIOExceptionHandler;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;
import java.util.Map;

public interface FakeHttpImpl {

    default MockLowLevelHttpResponse successJsonResponse(String body) {
        return new MockLowLevelHttpResponse()
                .setStatusCode(200)
                .setContentType("application/json")
                .setContent(body);
    }

    default MockLowLevelHttpResponse emptySuccess() {
        return new MockLowLevelHttpResponse()
                .setStatusCode(200);
    }

    default HttpImpl buildFakeHttp(MockLowLevelHttpResponse response) {
        MockHttpTransport transport = new MockHttpTransport.Builder()
                .setLowLevelHttpResponse(response)
                .build();
        return createHttpImpl(transport);
    }

    default MockHttpTransport buildFakeTransport(MockLowLevelHttpResponse mockResponse) {
        return new MockHttpTransport.Builder()
                .setLowLevelHttpResponse(mockResponse)
                .build();
    }

    default HttpImpl buildFakeHttp(MockHttpTransport transport) {
        return createHttpImpl(transport);
    }

    private HttpImpl createHttpImpl(HttpTransport transport) {
        HttpRequestInitializer initializer = request -> {
        };
        JsonObjectParser parser = new JsonObjectParser(new GsonFactory());
        HttpIOExceptionHandler exceptionHandler = (request, response) -> false;
        Map<String, String> customHeaders = Map.of("X-Custom-Header", "CustomValue");

        return new HttpImpl(
                transport,
                initializer,
                "https://api.aurinko.io",
                parser,
                exceptionHandler,
                customHeaders,
                "Test Agent"
        );
    }

    default GenericJson asJson(String body) throws IOException {
        try (final var parser = new GsonFactory()
                     .createJsonParser(body)) {
            return parser.parse(GenericJson.class);
        }
    }
}

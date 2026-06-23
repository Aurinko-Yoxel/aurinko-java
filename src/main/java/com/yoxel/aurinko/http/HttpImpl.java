package com.yoxel.aurinko.http;

import com.google.api.client.http.*;
import com.google.api.client.json.JsonObjectParser;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class HttpImpl {

    private final HttpTransport httpTransport;
    private final HttpRequestInitializer requestInitializer;
    private final String baseUrl;
    private final JsonObjectParser JSON_PARSER;
    private final HttpIOExceptionHandler httpIOExceptionHandler;
    private final Map<String, String> headers;
    private final String userAgent;

    public HttpRequest createRequest(String method, String path) throws IOException {
        HttpRequest
                httpRequest =
                httpTransport.createRequestFactory(requestInitializer) // Utils.getDefaultTransport()
                        .buildRequest(method, new GenericUrl((path.startsWith("https://") ? "" : baseUrl + "/v1") + path), null)
                        .setParser(JSON_PARSER).setIOExceptionHandler(httpIOExceptionHandler)
                        .setNumberOfRetries(5).setConnectTimeout(60 * 1000).setReadTimeout(35 * 1000);

        httpRequest.getHeaders().setUserAgent(userAgent);
        headers.forEach((k, v) -> httpRequest.getHeaders().set(k, v));
//        if ("PATCH".equalsIgnoreCase(method))
//            httpRequest.getHeaders().set("X-HTTP-Method-Override", method);

        return httpRequest;
    }
}

package com.yoxel.aurinko.http;

import com.google.api.client.http.HttpRequest;
import com.yoxel.aurinko.apis.HttpApi;
import com.yoxel.aurinko.apis.QueryParams;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public abstract class HttpApiSupport implements HttpApi {

    private final HttpImpl httpImpl;

    protected String basePath() {
        return "";
    }

    @Override
    public HttpRequest httpRequestPrepare(String method,
                                          String path,
                                          QueryParams queryParams,
                                          Map<String, String> headers)
            throws IOException {
        return httpImpl.createRequest(
                method,
                basePath() + path + queryParams.toUrlString(),
                headers);
    }
}

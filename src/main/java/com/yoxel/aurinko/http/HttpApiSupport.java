package com.yoxel.aurinko.http;

import com.google.api.client.http.HttpRequest;
import com.yoxel.aurinko.apis.HttpApi;
import com.yoxel.aurinko.apis.QueryParams;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class HttpApiSupport implements HttpApi {

    private final HttpImpl httpImpl;

    protected String basePath() {
        return "";
    }

    @Override
    public HttpRequest httpRequestPrepare(String method, String path, QueryParams queryParams)
            throws IOException {
        return httpImpl.createRequest(method, basePath() + path + queryParams.toUrlString());
    }
}

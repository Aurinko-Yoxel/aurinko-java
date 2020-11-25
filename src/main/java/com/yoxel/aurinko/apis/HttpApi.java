package com.yoxel.aurinko.apis;

import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;

import java.io.IOException;

/**
 * Support for http methods
 */
public interface HttpApi {

  HttpRequest httpRequestPrepare(String method, String path, QueryParams queryParams) throws IOException;

  default HttpRequest httpGetPrepare(String path, QueryParams queryParams) throws IOException {
    return httpRequestPrepare("GET", path, queryParams);
  }

  default HttpRequest httpPostPrepare(String path, QueryParams queryParams) throws IOException {
    return httpRequestPrepare("POST", path, queryParams);
  }

  default HttpRequest httpPostPrepare(String path, QueryParams queryParams, HttpContent content) throws IOException {
    return httpRequestPrepare("POST", path, queryParams).setContent(content);
  }

  default HttpRequest httpPatchPrepare(String path, QueryParams queryParams, HttpContent content) throws IOException {
    return httpRequestPrepare("PATCH", path, queryParams).setContent(content);
  }

  default HttpRequest httpDeletePrepare(String path, QueryParams queryParams) throws IOException {
    return httpRequestPrepare("DELETE", path, queryParams);
  }

  default HttpResponse httpGet(String path, QueryParams queryParams) throws IOException {
    return httpGetPrepare(path, queryParams).execute();
  }

  default HttpResponse httpGet(String path) throws IOException {
    return httpGet(path, QueryParams.EMPTY);
  }

  default HttpResponse httpPost(String path, QueryParams queryParams) throws IOException {
    return httpPostPrepare(path, queryParams).execute();
  }

  default HttpResponse httpPost(String path) throws IOException {
    return httpPost(path, QueryParams.EMPTY);
  }

  default HttpResponse httpPost(String path, QueryParams queryParams, HttpContent content) throws IOException {
    return httpPostPrepare(path, queryParams, content).execute();
  }

  default HttpResponse httpPost(String path, HttpContent content) throws IOException {
    return httpPost(path, QueryParams.EMPTY, content);
  }

  default HttpResponse httpPatch(String path, QueryParams queryParams, HttpContent content) throws IOException {
    return httpPatchPrepare(path, queryParams, content).execute();
  }

  default HttpResponse httpPatch(String path, HttpContent content) throws IOException {
    return httpPatch(path, QueryParams.EMPTY, content);
  }

  default HttpResponse httpDelete(String path, QueryParams queryParams) throws IOException {
    return httpDeletePrepare(path, queryParams).execute();
  }

  default HttpResponse httpDelete(String path) throws IOException {
    return httpDelete(path, QueryParams.EMPTY);
  }

}

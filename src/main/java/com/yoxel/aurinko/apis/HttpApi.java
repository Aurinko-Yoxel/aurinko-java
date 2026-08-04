package com.yoxel.aurinko.apis;

import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;

import com.yoxel.aurinko.AurinkoHttpException;

import java.io.IOException;

/**
 * Support for http methods
 */
public interface HttpApi {

  HttpRequest httpRequestPrepare(String method, String path, QueryParams queryParams) throws IOException;

  default HttpResponse httpExecute(HttpRequest request) throws IOException {
    try {
      return request.execute();
    } catch (HttpResponseException e) {
      throw AurinkoHttpException.of(request, e);
    }
  }

  default HttpRequest httpGetPrepare(String path, QueryParams queryParams) throws IOException {
    return httpRequestPrepare("GET", path, queryParams);
  }

  default HttpRequest httpPostPrepare(String path, QueryParams queryParams) throws IOException {
    return httpRequestPrepare("POST", path, queryParams);
  }

  default HttpRequest httpPostPrepare(String path, QueryParams queryParams, HttpContent content) throws IOException {
    return httpRequestPrepare("POST", path, queryParams).setContent(content);
  }

  default HttpRequest httpPutPrepare(String path, QueryParams queryParams) throws IOException {
    return httpRequestPrepare("PUT", path, queryParams);
  }

  default HttpRequest httpPutPrepare(String path, QueryParams queryParams, HttpContent content) throws IOException {
    return httpRequestPrepare("PUT", path, queryParams).setContent(content);
  }

  default HttpRequest httpPatchPrepare(String path, QueryParams queryParams, HttpContent content) throws IOException {
    return httpRequestPrepare("PATCH", path, queryParams).setContent(content);
  }

  default HttpRequest httpDeletePrepare(String path, QueryParams queryParams) throws IOException {
    return httpRequestPrepare("DELETE", path, queryParams);
  }

  default HttpResponse httpGet(String path, QueryParams queryParams) throws IOException {
    return httpExecute(httpGetPrepare(path, queryParams));
  }

  default HttpResponse httpGet(String path) throws IOException {
    return httpGet(path, QueryParams.EMPTY);
  }

  default HttpResponse httpPost(String path, QueryParams queryParams) throws IOException {
    return httpExecute(httpPostPrepare(path, queryParams));
  }

  default HttpResponse httpPost(String path) throws IOException {
    return httpPost(path, QueryParams.EMPTY);
  }

  default HttpResponse httpPost(String path, QueryParams queryParams, HttpContent content) throws IOException {
    return httpExecute(httpPostPrepare(path, queryParams, content));
  }

  default HttpResponse httpPost(String path, HttpContent content) throws IOException {
    return httpPost(path, QueryParams.EMPTY, content);
  }

  default HttpResponse httpPut(String path, QueryParams queryParams) throws IOException {
    return httpExecute(httpPutPrepare(path, queryParams));
  }

  default HttpResponse httpPut(String path) throws IOException {
    return httpPut(path, QueryParams.EMPTY);
  }

  default HttpResponse httpPut(String path, QueryParams queryParams, HttpContent content) throws IOException {
    return httpExecute(httpPutPrepare(path, queryParams, content));
  }

  default HttpResponse httpPut(String path, HttpContent content) throws IOException {
    return httpPut(path, QueryParams.EMPTY, content);
  }

  default HttpResponse httpPatch(String path, QueryParams queryParams, HttpContent content) throws IOException {
    return httpExecute(httpPatchPrepare(path, queryParams, content));
  }

  default HttpResponse httpPatch(String path, HttpContent content) throws IOException {
    return httpPatch(path, QueryParams.EMPTY, content);
  }

  default HttpResponse httpDelete(String path, QueryParams queryParams) throws IOException {
    return httpExecute(httpDeletePrepare(path, queryParams));
  }

  default HttpResponse httpDelete(String path) throws IOException {
    return httpDelete(path, QueryParams.EMPTY);
  }

}

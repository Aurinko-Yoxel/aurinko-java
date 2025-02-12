package com.yoxel.aurinko;

import com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandler;
import com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandler.BackOffRequired;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.util.BackOff;

import java.io.IOException;

/**
 *
 */
public class BackoffInterceptorWrapper implements HttpRequestInitializer {

  public BackoffInterceptorWrapper(HttpExecuteInterceptor underlying, BackOff backOff,
                                   BackOffRequired backOffRequired) {
    this.underlying = underlying;
    this.handler = new HttpBackOffUnsuccessfulResponseHandler(backOff).setBackOffRequired(backOffRequired);
  }

  private final HttpExecuteInterceptor underlying;
  private final HttpBackOffUnsuccessfulResponseHandler handler;

  @Override
  public void initialize(HttpRequest request) throws IOException {
    handler.getBackOff().reset();

    request.setInterceptor(underlying);
    request.setUnsuccessfulResponseHandler(handler);
  }

  public static final BackOffRequired ON_RATE_LIMITING =
      (resp) -> resp.getStatusCode() == 429 || resp.getStatusCode() == 408 || resp.getStatusCode() == 503;
}

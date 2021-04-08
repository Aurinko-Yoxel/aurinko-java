package com.yoxel.aurinko;

import com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandler;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.util.BackOff;

import java.io.IOException;

/**
 *
 */
public class BackoffInterceptorWrapper implements HttpRequestInitializer {

  public BackoffInterceptorWrapper(HttpExecuteInterceptor underlying, BackOff backOff) {
    this.underlying = underlying;
    this.handler = new HttpBackOffUnsuccessfulResponseHandler(backOff);
  }

  private final HttpExecuteInterceptor underlying;
  private final HttpBackOffUnsuccessfulResponseHandler handler;

  @Override
  public void initialize(HttpRequest request) throws IOException {
    request.setInterceptor(underlying);
    request.setUnsuccessfulResponseHandler(handler);
  }
}

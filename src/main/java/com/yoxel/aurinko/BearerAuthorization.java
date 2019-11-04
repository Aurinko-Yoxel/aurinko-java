package com.yoxel.aurinko;

import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.util.Preconditions;

import java.io.IOException;

public class BearerAuthorization implements
                               HttpExecuteInterceptor,
                               HttpRequestInitializer {

  private final String accessToken;

  public BearerAuthorization(String accessToken) {
    this.accessToken = Preconditions.checkNotNull(accessToken);
  }

  public void initialize(HttpRequest request) throws IOException {
    request.setInterceptor(this);
  }

  public void intercept(HttpRequest request) throws IOException {
    request.getHeaders().setAuthorization("Bearer " + accessToken);
  }

}

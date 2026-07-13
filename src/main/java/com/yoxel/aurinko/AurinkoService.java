package com.yoxel.aurinko;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.*;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.util.ExponentialBackOff;
import com.yoxel.aurinko.api.*;
import com.yoxel.aurinko.bean.AurLiveIdEntity;
import com.yoxel.aurinko.bean.AurTokenPage;
import com.yoxel.aurinko.http.HttpImpl;

import java.io.IOException;
import java.util.Map;

public class AurinkoService implements AutoCloseable {

  public static final HttpExecuteInterceptor EMPTY_INTERCEPTOR = request -> {
  };

  private static final String DEFAULT_BASE_URL = "https://aurinko.yoxel.com";
  private static final String DEFAULT_USER_AGENT = "Aurinko-Client-Library/1.0";

  private final HttpTransport httpTransport;

  public static final JsonObjectParser JSON_PARSER =
      new JsonObjectParser(Utils.getDefaultJsonFactory());

  private final HttpIOExceptionHandler httpIOExceptionHandler = (request, supportsRetry) -> {
    if (supportsRetry) {
      request.setReadTimeout(request.getReadTimeout() + 60000);
    }

    return supportsRetry;
  };

  private final HttpImpl httpImpl;

  public enum BodyType {html, text, none}

  public final Api api;
  public final Direct direct;
  public final Subscriptions subscriptions;
  public final Auth auth;
  public final Accounts accounts;
  public final Users users;
  public final Calendars calendars;
  public final TaskLists taskLists;
  public final Emails emails;
  public final Contacts contacts;
  public final Drives drives;
  public final Followups followups;

  private AurinkoService(String baseUrl, Map<String, String> headers, HttpRequestInitializer requestInitializer, String userAgent) {
    this.httpTransport = new ApacheHttpTransport();
    this.httpImpl = new HttpImpl(httpTransport, requestInitializer, baseUrl, JSON_PARSER, httpIOExceptionHandler, headers, userAgent);
    this.api = new Api(httpImpl);
    this.direct = new Direct(httpImpl);
    this.subscriptions = new Subscriptions(httpImpl);
    this.auth = new Auth(httpImpl);
    this.accounts = new Accounts(httpImpl);
    this.users = new Users(httpImpl);
    this.calendars = new Calendars(httpImpl);
    this.taskLists = new TaskLists(httpImpl);
    this.emails = new Emails(httpImpl);
    this.contacts = new Contacts(httpImpl);
    this.drives = new Drives(httpImpl);
    this.followups = new Followups(httpImpl);
  }

  public void close() throws IOException {
    httpTransport.shutdown();
  }

  public static AurinkoService createWithAppAuth(
      String baseUrl,
      String clientId,
      String clientSecret
  ) {
    return create(baseUrl, new BasicAuthentication(clientId, clientSecret), DEFAULT_USER_AGENT);
  }

  public static AurinkoService createWithAppAuth(
      String clientId,
      String clientSecret,
      long accountId
  ) {
    return createWithAppAuth(DEFAULT_BASE_URL, clientId, clientSecret, accountId);
  }

  public static AurinkoService createWithAppAuth(
      String baseUrl,
      String clientId,
      String clientSecret,
      long accountId
  ) {
    return create(
            baseUrl,
            new BasicAuthentication(clientId, clientSecret),
            Map.of("X-Aurinko-Account-Id", String.valueOf(accountId)),
            DEFAULT_USER_AGENT
    );
  }

  public static AurinkoService createWithAppAuth(String clientId, String clientSecret) {
    return createWithAppAuth(DEFAULT_BASE_URL, clientId, clientSecret);
  }

  public static AurinkoService createWithAccountAuth(String baseUrl, String accessToken) {
    return create(baseUrl, new BearerAuthorization(accessToken), DEFAULT_USER_AGENT);
  }

  public static AurinkoService createWithAccountAuth(String accessToken) {
    return createWithAccountAuth(DEFAULT_BASE_URL, accessToken);
  }

  public static AurinkoService create(HttpExecuteInterceptor httpInterceptor) {
    return create(DEFAULT_BASE_URL, httpInterceptor, DEFAULT_USER_AGENT);
  }

  public static AurinkoService create(String baseUrl) {
    return create(baseUrl, EMPTY_INTERCEPTOR, DEFAULT_USER_AGENT);
  }

  public static AurinkoService create() {
    return create(DEFAULT_BASE_URL, EMPTY_INTERCEPTOR, DEFAULT_USER_AGENT);
  }

  public static AurinkoService create(String baseUrl, HttpExecuteInterceptor httpInterceptor, String userAgent) {
    return create(baseUrl, httpInterceptor, Map.of(), userAgent);
  }

  public static AurinkoService create(
      String baseUrl,
      HttpExecuteInterceptor httpInterceptor,
      Map<String, String> headers,
      String userAgent
  ) {
    return new AurinkoService(
            baseUrl,
            headers,
            new BackoffInterceptorWrapper(
                    httpInterceptor,
                    new ExponentialBackOff.Builder()
                            .setMultiplier(2.0)
                            .build(),
                    BackoffInterceptorWrapper.ON_RATE_LIMITING
            ),
            userAgent
    );
  }

  /**
   * Create a client for /dynamic API, using specific api_configuration ID.
   */
  public <Entity extends AurLiveIdEntity, Page extends AurTokenPage<Entity>> Dynamic<Entity, Page>
  dynamic(AurinkoClass<Entity, Page> clazz, Integer apiConfId) {
    return new Dynamic<>(clazz, apiConfId, httpImpl);
  }

  /**
   * Create a client for /dynamic API, using default api configuration, configured on app-level.
   */
  public <Entity extends AurLiveIdEntity, Page extends AurTokenPage<Entity>> Dynamic<Entity, Page>
  dynamic(AurinkoClass<Entity, Page> clazz) {
    return new Dynamic<>(clazz, null, httpImpl);
  }
}

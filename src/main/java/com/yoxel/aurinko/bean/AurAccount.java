package com.yoxel.aurinko.bean;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurAccount {

  @Key
  private long id;

  @Key
  private long parentId;

  @Key
  private String serviceType;

  @Key
  private String serviceProvider;

  @Key
  private boolean active;

  @Key
  private String tokenStatus;

  @Key
  private String tokenError;

  @Key
  private String type;

  @Key
  private boolean daemon;

  @Key
  private String loginString;

  @Key
  private String email;

  @Key
  private String email2;

  @Key
  private String mailboxAddress;

  @Key
  private String name;

  @Key
  private String name2;

  @Key
  private String serverUrl;

  @Key
  private String serverUrl2;

  @Key
  private String clientOrgId;

  @Key
  private String authOrgId;

  @Key
  private String authUserId;

  @Key
  private String timezone;

  @Key
  private DateTime tokenIssuedAt;

  @Key
  private DateTime tokenLastActivity;

  @Key
  private String[] authScopes;

  @Key
  private String[] authNativeScopes;

  @Key
  private DateTime authObtainedAt;

  @Key
  private DateTime authExpiresAt;

  @Key
  private String userId;

  @Key
  private Boolean copyToSent;

  @Key
  private Boolean trustServer;

  @Key
  private DateTime createdAt;

  @Key
  private DateTime updatedAt;

  @Key
  private String providerAccessToken;

  public static class Page extends AurOffsetPage<AurAccount> {
  }
}

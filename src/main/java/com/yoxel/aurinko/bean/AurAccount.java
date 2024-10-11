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
  private boolean active;

  @Key
  private String tokenStatus;

  @Key
  private boolean daemon;

  @Key
  private String loginString;

  @Key
  private String email;

  @Key
  private String name;

  @Key
  private String clientOrgId;

  @Key
  private String serverUrl;

  @Key
  private String authOrgId;

  @Key
  private String authUserId;

  @Key
  private DateTime tokenIssuedAt;

  @Key
  private String[] authScopes;

  @Key
  private DateTime authObtainedAt;

  @Key
  private DateTime authExpiresAt;

  public static class Page extends AurOffsetPage<AurAccount> {
  }
}

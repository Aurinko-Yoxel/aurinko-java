package com.yoxel.aurinko.dto;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

import lombok.Data;

@Data
public class AurAccountDto {

  @Key
  private Long id;

  @Key
  private Long parentId;

  @Key
  private String serviceType;

  @Key
  private Boolean daemon;

  @Key
  private String email;

  @Key
  private String name;

  @Key
  private String[] authScopes;

  @Key
  private String authOrgId;

  @Key
  private String authUserId;

  @Key
  private String clientOrgId;

  @Key
  private String serverUrl;

  @Key
  private String serverInfo;

  @Key
  private String loginString;

  @Key
  private String oauthClientId;

  @Key
  private String authString1;

  @Key
  private String authString2;

  @Key
  private DateTime authObtainedAt;

  @Key
  private DateTime authExpiresAt;

  @Key
  boolean active;

  @Key
  private String timezone;
}

package com.yoxel.rest2.aurinko;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

import com.yoxel.oauth.gmail.ServiceAccountUtil;

import java.util.ArrayList;
import java.util.List;

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
  private String status;

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

}

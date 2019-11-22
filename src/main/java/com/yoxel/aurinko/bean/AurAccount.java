package com.yoxel.aurinko.bean;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurAccount {

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

}

package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;

import lombok.Data;

@Data
public class AurAccountToken {

  @Key
  private long accountId;

  @Key
  private String accessToken;

  @Key
  private String userId;

  @Key
  private String userSession;

  private String accServiceType;

}

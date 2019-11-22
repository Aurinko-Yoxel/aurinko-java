package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;

import lombok.Data;

@Data
public class AurAccountToken {

  @Key
  private String accessToken;
}

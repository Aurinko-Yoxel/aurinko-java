package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;

import lombok.Data;

@Data
public class AurToken {

  @Key
  private String accessToken;
}

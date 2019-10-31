package com.yoxel.rest2.aurinko;

import com.google.api.client.util.Key;

import lombok.Data;

@Data
public class AurTokenDto {

  @Key
  private String accessToken;
}

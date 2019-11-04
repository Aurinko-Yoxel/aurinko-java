package com.yoxel.aurinko.dto;

import com.google.api.client.util.Key;

import lombok.Data;

@Data
public class AurTokenDto {

  @Key
  private String accessToken;
}

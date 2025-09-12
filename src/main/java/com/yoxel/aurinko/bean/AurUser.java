package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AurUser extends AurLiveIdEntity {

  @Key
  String email;

  @Key
  String fullName;

  @Key
  String orgId;

  @Key
  String username;

  @Key
  String timezone;

}

package com.yoxel.aurinko.bean;

import static org.apache.commons.lang3.BooleanUtils.isTrue;

import com.google.api.client.util.Key;

public class AurDrive extends AurLiveIdEntity {

  public AurDrive(String title) {
    this.name = title;
  }

  public AurDrive() {
  }

  @Key
  String name;

  @Key
  String driveType;

  @Key
  String description;

  @Key
  String webUrl;

  // more fields exist

}

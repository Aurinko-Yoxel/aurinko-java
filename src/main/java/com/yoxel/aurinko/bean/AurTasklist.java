package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;

import lombok.Data;

import static org.apache.commons.lang3.BooleanUtils.isTrue;

@Data
public class AurTasklist extends AurLiveIdEntity {

  public AurTasklist(String title) {
    this.title = title;
  }

  public AurTasklist() {
  }

  @Key
  String title;

  @Key
  Boolean owner;

  @Key
  Boolean shared;

  @Key
  Boolean defaultList;

  @Key
  Boolean emailsList;

  public boolean isDefault() {
    return isTrue(defaultList);
  }

}

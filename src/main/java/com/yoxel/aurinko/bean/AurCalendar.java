package com.yoxel.aurinko.bean;


import com.google.api.client.util.Key;

import lombok.Data;

import static org.apache.commons.lang3.BooleanUtils.isTrue;

@Data
public class AurCalendar extends AurLiveIdEntity {

  public AurCalendar(String name) {
    this.name = name;
  }

  public AurCalendar() {
  }

  @Key
  String name;

  @Key
  String color;

  @Key
  String description;

  @Key
  String accessRole;

  @Key
  Boolean primary;

  @Key
  String groupName;

  public boolean isPrimary() {
    return isTrue(primary);
  }
}

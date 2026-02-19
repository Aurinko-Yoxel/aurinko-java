package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;

public class AurDriveItem extends AurLiveIdEntity {

  @Key // ("folderId")
  private String driveId;

  @Key
  private String name;

  @Key
  private String description;

  @Key
  private long size;

  @Key
  private String webUrl;

  // more fields exist

}

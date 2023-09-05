package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import lombok.Data;

@Data
public class TaskLink extends GenericJson {

  @Key
  private String source;

  @Key
  private String description;

  @Key
  private String link;

}

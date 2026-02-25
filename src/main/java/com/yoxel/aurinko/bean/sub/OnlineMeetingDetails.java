package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import lombok.Data;

/**
 *
 */
@Data
public class OnlineMeetingDetails extends GenericJson {

  @Key
  private String id;

  @Key
  private String url;

  @Key
  private String infoUrl;
}

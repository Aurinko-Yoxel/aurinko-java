package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AurSubscription extends GenericJson {

  public AurSubscription(String resource, String notificationUrl, String detailLevel) {
    this.id = 0;
    this.resource = resource;
    this.notificationUrl = notificationUrl;
    this.detailLevel = detailLevel;
  }

  public AurSubscription(String resource, String notificationUrl) {
    this(resource, notificationUrl, null);
  }

  @Key
  private long id;

  @Key
  private String resource;

  @Key
  private String notificationUrl;

  @Key
  private String detailLevel;
}

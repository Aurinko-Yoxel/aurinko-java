package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurTrackingEvent {
  @Key
  private String type;
  @Key
  private long id;
  @Key
  private com.google.api.client.util.DateTime createdAt;
  @Key
  private String eventType;
  @Key
  private String location;
  @Key
  private String userAgent;
  @Key
  private String remoteAddr;
  @Key
  private long trackingId;
  @Key
  private String trackingThreadId;
  @Key
  private String trackingMessageId;
  @Key
  private String trackingInternetMessageId;
  @Key
  private String context;
  @Key
  private String trackingCode;

  public static class Page extends AurOffsetPage<AurTrackingEvent> {
  }
}

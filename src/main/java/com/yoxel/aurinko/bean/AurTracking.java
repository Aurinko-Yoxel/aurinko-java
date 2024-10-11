package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class AurTracking {

  @Key
  private long id;
  @Key
  private String messageId;
  @Key
  private String threadId;
  @Key
  private com.google.api.client.util.DateTime sendDate;
  @Key
  private com.google.api.client.util.DateTime lastActivityTime;
  @Key
  private boolean trackReplies;
  @Key
  private boolean trackOpens;
  @Key
  private boolean trackLinks;
  @Key
  private String trackingCode;
  @Key
  private String context;
  @Key
  private String location;
  @Key
  private String userAgent;
  @Key
  private String remoteAddr;
  @Key
  private boolean ignoreOpenClicks;
  @Key
  private boolean hasBounced;

  public static class Page extends AurOffsetPage<AurTracking> {
  }
}

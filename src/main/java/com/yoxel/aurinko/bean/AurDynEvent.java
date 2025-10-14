package com.yoxel.aurinko.bean;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AurDynEvent extends AurLiveIdEntity implements AurNativePropertiesSupport {

  private String calendarId;

  @Key
  private Boolean deleted;

  @Key
  private String createdBy;

  @Key
  private String subject;

  @Key
  private String description;

  @Key
  private String location;

  @Key
  private DateTime startTime;

  @Key
  private DateTime endTime;

  @Key("private")
  private boolean privateEvent;

  @Key
  private boolean allDay;

  @Key("__nativeProperties")
  private Map<String, Object> nativeProperties;

}

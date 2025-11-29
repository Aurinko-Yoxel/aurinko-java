package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

import com.yoxel.aurinko.bean.sub.EmailAddress;
import com.yoxel.aurinko.bean.sub.EventParticipant;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper = true)
public class AurDynEvent extends AurLiveIdEntity implements AurNativePropertiesSupport {

  @Getter
  @Setter
  public static class Attendee {

    @Key
    private String userId;

    @Key
    private String emailAddress;

    @Key
    private String type;

    @Key
    private String status;
  }

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
  private String type;

  @Key
  private DateTime startTime;

  @Key
  private DateTime endTime;

  @Key("private")
  private boolean privateEvent;

  @Key
  private boolean allDay;

  @Key
  private String organizerEmail;

  @Key
  private String organizerName;

  @Key
  private List<Attendee> attendees;

  @Key("__nativeProperties")
  private Map<String, Object> nativeProperties;

}

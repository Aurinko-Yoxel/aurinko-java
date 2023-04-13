package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;

import com.yoxel.aurinko.bean.sub.*;

import org.apache.commons.collections4.CollectionUtils;

import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class AurEvent extends AurLiveIdEntity {

  @Key // ("folderId")
  private String calendarId;

  @Key
  private String subject;

  @Key
  private String description;

  @Key
  private String htmlLink;

  @Key
  private String location;

  @Key
  private String type;

  @Key
  private EventDateTime start;

  @Key
  private EventDateTime end;

  @Key
  private Organizer organizer;

  @Key
  private MeetingInfo meetingInfo;

  @Key
  private String recurrenceType;

  @Key
  private Recurrence recurrence;

  @Key
  private OccurrenceInfo occurrenceInfo;

  @Key
  private String iCalUId;

  @Key
  private String globalId;

  @Key
  private String showAs;

  @Key
  private String sensitivity;

  @Key
  private List<String> categories;

  @Key
  private Map<String, String> nativeProperties;

  public boolean isMeeting() {
    return meetingInfo != null && CollectionUtils.isNotEmpty(meetingInfo.getAttendees());
  }
}

package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class MeetingInfo extends GenericJson {

  @Key
  private boolean canceled;

  @Key
  private List<Attendee> attendees;

  @Key
  private String response;

  @Key
  private Set<String> attendeePermissions;

  @Key
  private OnlineMeetingDetails onlineMeetingDetails;
}

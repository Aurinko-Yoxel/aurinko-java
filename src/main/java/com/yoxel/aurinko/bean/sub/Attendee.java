package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import lombok.Data;

@Data
public final class Attendee extends GenericJson {

  @Key
  private String id;

  @Key
  private EmailAddress emailAddress;

  @Key
  private String type;

  @Key
  private String response;

  @Key
  private String comment;
}

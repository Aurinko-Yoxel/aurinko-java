package com.yoxel.aurinko.bean;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class AurApplication extends GenericJson {

  @Key
  private long id;
  @Key
  private String name;
  @Key
  private boolean syncAvailable;
  @Key
  private boolean internalServicesEnabled;
  @Key
  private DateTime createdAt;
  @Key
  private String applicationType;
  @Key
  private String outlookManifestId;
  @Key
  private List<String> capabilities;
  @Key
  private boolean trial;
  @Key
  private String signingSecret;
}

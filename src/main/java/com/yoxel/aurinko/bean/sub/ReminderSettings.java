package com.yoxel.aurinko.bean.sub;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ReminderSettings extends GenericJson {

  @Key
  private Boolean useDefault;

  @Key
  private List<ReminderOverride> overrides;
}

package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AurEventSaveResult extends AurSaveResult<AurEvent> {

  @Key
  private String iCalUId;

  @Key
  private String globalId;
}

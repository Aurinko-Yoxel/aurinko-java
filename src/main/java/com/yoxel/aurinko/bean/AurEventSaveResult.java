package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;

import com.yoxel.aurinko.bean.sub.OnlineMeetingDetails;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AurEventSaveResult extends AurSaveResult<AurEvent> {

  @Key
  private String iCalUId;

  @Key
  private String globalId;

  @Key
  private String onlineMeetingProvider;

  @Key
  private OnlineMeetingDetails onlineMeetingDetails;

}

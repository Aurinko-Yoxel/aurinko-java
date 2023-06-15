package com.yoxel.aurinko.bean.sub;

import com.google.api.client.util.Key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 *
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class MeetingResponse {

  @Key
  private String response;

  @Key
  private String comment;
}

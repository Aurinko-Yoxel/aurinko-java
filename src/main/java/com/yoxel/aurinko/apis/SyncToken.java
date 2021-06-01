package com.yoxel.aurinko.apis;

import lombok.Data;

/**
 *
 */
@Data
public class SyncToken {

  private final String value;
  private final SyncTokenType type;

  public static SyncToken delta(String value) {
    return new SyncToken(value, SyncTokenType.DELTA);
  }

  public static SyncToken page(String value) {
    return new SyncToken(value, SyncTokenType.PAGE);
  }

}

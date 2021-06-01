package com.yoxel.aurinko.apis;

import lombok.Data;

/**
 *
 */
@Data
public class SyncToken {

  private final String value;
  private final SyncTokenType type;
}

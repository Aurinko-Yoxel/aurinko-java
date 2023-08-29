package com.yoxel.aurinko.bean.sub;

import com.google.api.client.util.Key;
import lombok.Data;

/**
 *
 */
@Data
public class ContactPhoto {
  @Key
  private final String id;
  @Key
  private final String name;
  @Key
  private final String mimeType;
  @Key
  private final String content;
}

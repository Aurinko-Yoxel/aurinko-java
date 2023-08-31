package com.yoxel.aurinko.bean.sub;

import com.google.api.client.util.Key;
import lombok.Data;

/**
 *
 */
@Data
public class ContactPhoto {
  @Key
  private String id;
  @Key
  private String name;
  @Key
  private String mimeType;
  @Key
  private String content;
}

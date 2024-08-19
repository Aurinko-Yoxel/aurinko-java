package com.yoxel.aurinko.bean;

import com.google.api.client.http.HttpResponseException;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.AurinkoService;
import lombok.Data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Data
public class AurHttpError {

  @Key
  private String code;

  @Key
  private String message;

  @Key
  private String requestId;

  public static AurHttpError fromException(HttpResponseException ex) {
    if (ex.getContent() == null) {
      return null;
    }
    try {
      return AurinkoService.JSON_PARSER.parseAndClose(
          new ByteArrayInputStream(ex.getContent().getBytes(StandardCharsets.UTF_8)),
          StandardCharsets.UTF_8,
          AurHttpError.class
      );
    } catch (IOException e) {
      return null;
    }
  }
}

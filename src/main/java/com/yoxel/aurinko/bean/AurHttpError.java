package com.yoxel.aurinko.bean;

import com.google.api.client.http.HttpResponseException;
import com.google.api.client.util.Key;
import com.yoxel.aurinko.AurinkoService;
import lombok.Data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Data
public class AurHttpError {

  private static final int MAX_NESTING = 4;

  @Key
  private String code;

  @Key
  private String message;

  @Key
  private String requestId;

  @Key
  private Map<String, Object> originalError;

  @Key
  private Map<String, Object> error;

  private static final List<String> NESTED_KEYS = List.of("originalError", "error", "innerError");

  public String detailMessage() {
    final String nested = deepest("message");
    return nested != null ? nested : message;
  }

  public String detailCode() {
    return code != null && !code.isBlank() ? code : deepest("code");
  }

  private String deepest(String key) {
    String found = null;

    Object node = originalError != null ? originalError : error;
    for (int depth = 0; depth <= MAX_NESTING && node instanceof Map<?, ?> map; depth++) {
      if (map.get(key) instanceof String value && !value.isBlank()) {
        found = value;
      }

      node = firstNested(map);
    }

    return found;
  }

  private static Object firstNested(Map<?, ?> map) {
    for (String key : NESTED_KEYS) {
      if (map.get(key) instanceof Map<?, ?> child) {
        return child;
      }
    }

    return null;
  }

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
    } catch (IOException | RuntimeException e) {
      return null;
    }
  }
}

package com.yoxel.aurinko.bean;

import com.yoxel.aurinko.apis.SyncToken;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

/**
 *
 */
@Data
public class SyncTokensPair {

  public static SyncTokensPair EMPTY = new SyncTokensPair(null, null, 0);

  private final String deltaToken;
  private final String pageToken;
  private final int attempts;

  public boolean hasNextToken() {
    return deltaToken != null || pageToken != null;
  }

  public SyncToken getNextToken() {
    if (pageToken != null) {
      return SyncToken.page(pageToken);
    }

    if (deltaToken != null) {
      return SyncToken.delta(deltaToken);
    }

    return null;
  }

  public SyncTokensPair withPageToken(String pageToken) {
    return new SyncTokensPair(this.deltaToken, pageToken, attempts);
  }

  // when resetting page token, we increment attempts count
  public SyncTokensPair resetPageToken() {
    return new SyncTokensPair(this.deltaToken, null, attempts + 1);
  }

  public static SyncTokensPair delta(String deltaToken) {
    return new SyncTokensPair(deltaToken, null, 0);
  }

  public static SyncTokensPair page(String pageToken) {
    return new SyncTokensPair(null, pageToken, 0);
  }

  public static SyncTokensPair parse(String raw) {

    if (StringUtils.isBlank(raw)) {
      return SyncTokensPair.EMPTY;
    }

    String pageToken = null;
    String deltaToken = null;
    int attempts = 0;
    if (raw.startsWith(";")) { // new format

      for (String part : StringUtils.split(raw, ";")) {
        if (part.startsWith("delta:")) {
          deltaToken = part.substring(6);
        } else if (part.startsWith("page:")) {
          pageToken = part.substring(5);
        } else if (part.startsWith("attempts:")) {
          attempts = Integer.parseInt((part.substring(9)));
        } else {
          throw new IllegalStateException("Unknown part: " + part);
        }
      }
    } else {

      int p = raw.indexOf(":page:");

      if (p >= 0) {
        pageToken = raw.substring(p + 6);
        deltaToken = raw.substring(0, p);
      } else if (raw.startsWith("page:")) {
        pageToken = raw.substring(5);
      } else {
        deltaToken = raw;
      }
    }

    return new SyncTokensPair(deltaToken, pageToken, attempts);
  }

  public String toDbString() {
    if (deltaToken == null && pageToken == null) {
      return null;
    }

    final List<String> parts = new ArrayList<>();
    if (deltaToken != null) {
      parts.add("delta:" + deltaToken);
    }

    if (pageToken != null) {
      parts.add("page:" + pageToken);
    }

    parts.add("attempts:" + attempts);

    // starting the string with ";" to distinguish old and new formats
    return parts.stream().collect(Collectors.joining(";", ";", ""));
  }
}

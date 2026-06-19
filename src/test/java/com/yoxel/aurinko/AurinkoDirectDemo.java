package com.yoxel.aurinko;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

import com.yoxel.aurinko.apis.QueryParams;

import java.io.IOException;
import java.util.List;

import lombok.Data;

/**
 *
 */
public class AurinkoDirectDemo {

  public static void main(String[] args) throws IOException {

    try (final var svc = AurinkoService.createWithAccountAuth(
        "http://localhost:9000",
        "access_token"
    )) {

      final var soqlQuery = "SELECT Id from Opportunity";

      final var response1 = svc.direct()
          .httpGet("/services/data/v53.0/query", QueryParams.of("q", soqlQuery))
          .parseAs(SForcePage.class);
      System.out.println(response1);

      // or:

      final var response2 = svc.direct()
          .httpGetPrepare("/services/data/v53.0/query", QueryParams.of("q", soqlQuery))
          .execute()
          .parseAs(SForcePage.class);
      System.out.println(response2);
    }
  }

  @Data
  public static class SForcePage {

    @Key
    private int totalSize;

    @Key
    private boolean done;

    @Key
    private List<GenericJson> records;
  }
}

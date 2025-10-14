package com.yoxel.aurinko;

import com.google.api.client.util.DateTime;

import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.bean.AurLiveIdEntity;

import java.io.IOException;
import java.util.Comparator;

/**
 *
 */
public class AurinkoDynamicDemo {

  public static void main(String[] args) throws IOException, InterruptedException {

    try (final var svc = AurinkoService.createWithAccountAuth(
        "http://localhost:9000",
        "WxKEMe8gTbSxKslz68dWj7UZGkjIuDmIJV5ime9TgNc"
    )) {

      var xx=svc.dynamic(AurinkoClass.EVENT).entityFunctionPath("/xx");

      // list
      final var compApi = svc.dynamic(AurinkoClass.COMPANY);
      final var allCompanies = compApi.streamPaged().toList();
      System.out.println("ALL " + allCompanies);

      // get by id
      final var compId = allCompanies.get(0).getId();
      final var oneCompany = compApi.read(compId);
      System.out.println("ONE " + oneCompany);

      // update
      final var now = System.currentTimeMillis();
      oneCompany.setAboutUs("Bla-bla new " + now);
      final var updResult = compApi.update(compId, oneCompany);
      System.out.println("UPDATED " + updResult); // will be empty for hubspot

      // create new
      oneCompany.setId(null);
      oneCompany.setName("New name");
      oneCompany.setDescription("Test create");
      final var createResult = compApi.create(oneCompany);
      System.out.println("CREATED " + createResult);

      // delete - not implemented in /dynamic API.
      // compApi.delete(oneCompany.getId());
      // System.out.println("DELETED");

      final var maxUpdateTime = compApi.streamPaged()
          .map(AurLiveIdEntity::getLastModifiedTime)
          .toList().stream()
          .max(Comparator.comparing(DateTime::getValue))
          .get();

      // list all updated since:
      final var updatedSince =
          compApi.streamPaged(QueryParams.of("since", maxUpdateTime.toStringRfc3339())).toList();
      System.out.println("UPDATED SINCE (should be empty): " + updatedSince);

      Thread.sleep(5000);

      // one more update
      final var now2 = System.currentTimeMillis();
      oneCompany.setAboutUs("Bla-bla new " + now2);
      final var updResult2 = compApi.update(compId, oneCompany);
      System.out.println("UPDATED2 " + updResult2); // will be empty for hubspot

      // list all updated since, again. now it should return 1 element:
      final var updatedSince2 =
          compApi.streamPaged(QueryParams.of("since", maxUpdateTime.toStringRfc3339())).toList();
      System.out.println("UPDATED SINCE (should contain one record): " + updatedSince2);


    }
  }
}

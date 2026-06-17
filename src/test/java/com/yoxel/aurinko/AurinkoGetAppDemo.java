package com.yoxel.aurinko;

import com.yoxel.aurinko.bean.AurApplication;

import java.io.IOException;

/**
 *
 */
public class AurinkoGetAppDemo {

  public static void main(String[] args) throws IOException {

    try (final var svc = AurinkoService.createWithAppAuth(
        "http://localhost:9000",
        "client_id",
        "client_secret"
    )) {
      final var app = svc.api.httpGet("/am/application").parseAs(AurApplication.class);

      System.out.println(app);
    }
  }
}

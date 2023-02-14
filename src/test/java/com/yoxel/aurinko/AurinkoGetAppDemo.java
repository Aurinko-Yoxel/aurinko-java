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
        "2f9597c387e8c2283eb9c311b903bd2c",
        "J5A5nt2iyMpCBPDzqlitkJRmD_nwBTuu1fACa6tF7PsYEEWSIDe_LSn7lXAWanxLzeuywgIFe8N_-q14CxSNTA"
    )) {
      final var app = svc.api.httpGet("/am/application").parseAs(AurApplication.class);

      System.out.println(app);
    }
  }
}

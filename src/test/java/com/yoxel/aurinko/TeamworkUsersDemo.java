package com.yoxel.aurinko;

import com.yoxel.aurinko.extuser.impl.TeamworkUserInfoProvider;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

/**
 *
 */
public class TeamworkUsersDemo {

  public static void main(String[] args) throws IOException {

    try (final var svc = AurinkoService.createWithAccountAuth("VsSi-4wLdhXj1ubNRHRK0UByrqa2RUyKHZI2Lj7cItQ")) {
      System.out.println(
          TeamworkUserInfoProvider.FACTORY
              .createProvider(svc, "36020", "94550")
              .loadUsers(Date.from(Instant.parse("2023-05-23T12:53:54Z")))
              .toList()
      );
    }
  }
}

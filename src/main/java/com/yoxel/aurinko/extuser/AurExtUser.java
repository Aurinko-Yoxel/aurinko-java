package com.yoxel.aurinko.extuser;

import java.sql.Timestamp;

import lombok.Value;

/**
 *
 */
@Value
public class AurExtUser {

  String xId;
  String email;
  String fullName;
  String orgId;
  String username;
  String timezone;
  Timestamp updatedAt;
}

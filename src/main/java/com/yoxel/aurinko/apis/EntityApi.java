package com.yoxel.aurinko.apis;

/**
 *
 */
public interface EntityApi {

  String entityRoot();

  default String normalizeId(String id) {
    return id;
  }
}

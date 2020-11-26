package com.yoxel.aurinko.apis;

/**
 *
 */
public interface EntityApi {

  String entityApiRoot(); // for instance, /calendars/primary

  String entityPath(); // for instance /events

  default String entityFullPath() {
    return entityApiRoot() + entityPath();
  }

  default String normalizeId(String id) {
    return id;
  }
}

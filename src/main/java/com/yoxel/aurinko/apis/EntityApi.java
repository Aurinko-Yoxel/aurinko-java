package com.yoxel.aurinko.apis;

/**
 *
 */
public interface EntityApi<Id> {

  String entityPath();

  default Id normalizeId(Id id) {
    return id;
  }
}

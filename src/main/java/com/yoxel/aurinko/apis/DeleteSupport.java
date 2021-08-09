package com.yoxel.aurinko.apis;

import java.io.IOException;

/**
 *
 */
public interface DeleteSupport extends EntityApi, HttpApi {

  default void delete(String id, QueryParams params) throws IOException {
    httpDelete(entityFullPath() + "/" + normalizeId(id), params).ignore();
  }

  default void delete(String id) throws IOException {
    httpDelete(id, QueryParams.EMPTY).ignore();
  }

}

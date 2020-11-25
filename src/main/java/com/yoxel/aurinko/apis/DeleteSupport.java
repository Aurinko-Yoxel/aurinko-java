package com.yoxel.aurinko.apis;

import java.io.IOException;

/**
 *
 */
public interface DeleteSupport extends EntityApi, HttpApi {

  default void delete(String id) throws IOException {
    httpDelete(entityRoot() + "/" + id).ignore();
  }

}

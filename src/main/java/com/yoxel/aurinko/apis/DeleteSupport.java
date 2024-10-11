package com.yoxel.aurinko.apis;

import java.io.IOException;

/**
 *
 */
public interface DeleteSupport<Id> extends EntityApi<Id>, HttpApi {

  default void delete(Id id, QueryParams params) throws IOException {
    httpDelete(entityPath() + "/" + normalizeId(id), params).ignore();
  }

  default void delete(Id id) throws IOException {
    delete(id, QueryParams.EMPTY);
  }

}

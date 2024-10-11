package com.yoxel.aurinko.apis;

import com.google.api.client.http.HttpResponseException;

import java.io.IOException;

/**
 *
 */
public interface ReadSupport<Entity, Id>
    extends EntityReadApi<Entity, Id>, HttpApi {

  default Entity read(Id id) throws IOException {
    return read(id, QueryParams.EMPTY);
  }

  default Entity read(Id id, QueryParams params) throws IOException {

    return httpGet(entityPath() + "/" + normalizeId(id), params)
        .parseAs(entityClass());
  }

  default Entity readOpt(Id id, QueryParams params) throws IOException {
    try {
      return read(id, params);
    } catch (HttpResponseException e) {
      if (e.getStatusCode() == 404) {
        return null;
      } else {
        throw e;
      }
    }
  }

  default Entity readOpt(Id id) throws IOException {
    return readOpt(id, QueryParams.EMPTY);
  }

}

package com.yoxel.aurinko.apis;

import com.yoxel.aurinko.bean.AurLiveIdEntity;

import java.io.IOException;

/**
 *
 */
public interface ReadSupport<Entity extends AurLiveIdEntity>
    extends EntityReadApi<Entity>, HttpApi {

  default Entity read(String id) throws IOException {
    return read(id, QueryParams.EMPTY);
  }

  default Entity read(String id, QueryParams params) throws IOException {

    return httpGet(entityFullPath() + "/" + normalizeId(id), params)
        .parseAs(entityClass());
  }

}

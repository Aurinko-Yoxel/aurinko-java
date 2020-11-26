package com.yoxel.aurinko.apis;

import com.yoxel.aurinko.bean.AurIdEntity;

import java.io.IOException;

/**
 *
 */
public interface ReadSupport<Entity extends AurIdEntity>
    extends EntityReadApi<Entity>, HttpApi {

  default Entity read(String id) throws IOException {
    return read(id, QueryParams.EMPTY);
  }

  default Entity read(String id, QueryParams params) throws IOException {

    return httpGet(entityFullPath() + "/" + normalizeId(id), params)
        .parseAs(entityClass());
  }

}

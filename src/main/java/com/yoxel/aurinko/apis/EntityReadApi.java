package com.yoxel.aurinko.apis;

import com.yoxel.aurinko.bean.AurLiveIdEntity;

/**
 *
 */
public interface EntityReadApi<Entity extends AurLiveIdEntity> extends EntityApi {

  Class<Entity> entityClass();
}

package com.yoxel.aurinko.apis;

import com.yoxel.aurinko.bean.AurIdEntity;

/**
 *
 */
public interface EntityReadApi<Entity extends AurIdEntity> extends EntityApi {

  Class<Entity> entityClass();
}

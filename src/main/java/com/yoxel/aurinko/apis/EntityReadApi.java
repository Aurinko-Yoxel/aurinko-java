package com.yoxel.aurinko.apis;

import com.yoxel.aurinko.bean.AurLiveIdEntity;

/**
 *
 */
public interface EntityReadApi<Entity, Id> extends EntityApi<Id> {

  Class<Entity> entityClass();
}

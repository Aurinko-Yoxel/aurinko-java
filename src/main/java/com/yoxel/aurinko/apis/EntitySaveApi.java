package com.yoxel.aurinko.apis;

import com.yoxel.aurinko.bean.AurLiveIdEntity;

/**
 *
 */
public interface EntitySaveApi<Entity extends AurLiveIdEntity, SaveResult> extends EntityApi {

  Class<SaveResult> entitySaveResultClass();
}

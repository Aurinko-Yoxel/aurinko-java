package com.yoxel.aurinko.apis;

import com.yoxel.aurinko.bean.AurIdEntity;

/**
 *
 */
public interface EntitySaveApi<Entity extends AurIdEntity, SaveResult> extends EntityApi {

  Class<SaveResult> entitySaveResultClass();
}

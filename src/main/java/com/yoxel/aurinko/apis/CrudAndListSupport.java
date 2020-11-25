package com.yoxel.aurinko.apis;

import com.yoxel.aurinko.bean.AurIdEntity;
import com.yoxel.aurinko.bean.AurQueryResult;

/**
 *
 */
public interface CrudAndListSupport<
    Entity extends AurIdEntity,
    Page extends AurQueryResult<Entity>,
    SaveResult
    > extends CreateSupport<Entity, SaveResult>,
              ReadSupport<Entity>,
              UpdateSupport<Entity, SaveResult>,
              DeleteSupport,
              ListSupport<Entity, Page> {


}

package com.yoxel.aurinko.apis;

import com.yoxel.aurinko.bean.AurIdEntity;
import com.yoxel.aurinko.bean.AurLiveIdEntity;
import com.yoxel.aurinko.bean.AurQueryResult;

/**
 *
 */
public interface EntityListApi<Entity extends AurIdEntity, Page extends AurQueryResult<Entity>>
    extends EntityApi {

  Class<Page> entityPageClass();
}

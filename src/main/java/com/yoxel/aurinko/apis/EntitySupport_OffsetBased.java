package com.yoxel.aurinko.apis;

import com.yoxel.aurinko.bean.AurOffsetPage;
import com.yoxel.aurinko.http.HttpImpl;

/**
 * Base class for APIs with offset-based pagination.
 */
public abstract class EntitySupport_OffsetBased<Entity, Id, Page extends AurOffsetPage<Entity>, SaveResult>
        extends FullEntitySupport<Entity, Id, Page, SaveResult>
        implements ListSupport_OffsetBased<Entity, Id, Page> {

    public EntitySupport_OffsetBased(String entityPath, Class<Entity> eClass, Class<Page> pClass, Class<SaveResult> sClass, HttpImpl httpImpl) {
        super(entityPath, eClass, pClass, sClass, httpImpl);
    }
}

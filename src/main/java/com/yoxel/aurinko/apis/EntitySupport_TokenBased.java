package com.yoxel.aurinko.apis;

import com.yoxel.aurinko.bean.AurTokenPage;
import com.yoxel.aurinko.http.HttpImpl;

/**
 * Base class for APIs with token-based pagination.
 */
public abstract class EntitySupport_TokenBased<Entity, Id, Page extends AurTokenPage<Entity>, SaveResult>
        extends FullEntitySupport<Entity, Id, Page, SaveResult>
        implements ListSupport_TokenBased<Entity, Id, Page> {

    public EntitySupport_TokenBased(String entityPath, Class<Entity> eClass, Class<Page> pClass, Class<SaveResult> sClass, HttpImpl httpImpl) {
        super(entityPath, eClass, pClass, sClass, httpImpl);
    }
}

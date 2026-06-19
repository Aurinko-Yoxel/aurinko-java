package com.yoxel.aurinko.apis;


import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

/**
 * Base class for API with CRUD and list operations.
 */
public abstract class FullEntitySupport<Entity, Id, Page, SaveResult>
        extends HttpApiSupport implements
        CreateSupport<Entity, Id, SaveResult>,
        ReadSupport<Entity, Id>,
        UpdateSupport<Entity, Id, SaveResult>,
        DeleteSupport<Id>,
        EntityPageApi<Id, Page> {

    private final String entityPath;
    private final Class<Entity> eClass;
    private final Class<Page> pClass;
    private final Class<SaveResult> sClass;

    public FullEntitySupport(String entityPath,
                      Class<Entity> eClass,
                      Class<Page> pClass,
                      Class<SaveResult> sClass,
                      HttpImpl httpImpl) {
        super(httpImpl);
        this.entityPath = entityPath;
        this.eClass = eClass;
        this.pClass = pClass;
        this.sClass = sClass;
    }


    @Override
    public String entityPath() {
        return entityPath.replace("{pathFunction}", "");
    }

    @Override
    public Class<Entity> entityClass() {
        return eClass;
    }

    @Override
    public Class<Page> entityPageClass() {
        return pClass;
    }

    @Override
    public Class<SaveResult> entitySaveResultClass() {
        return sClass;
    }
}

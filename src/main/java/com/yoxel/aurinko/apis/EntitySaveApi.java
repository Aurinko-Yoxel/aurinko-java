package com.yoxel.aurinko.apis;

/**
 *
 */
public interface EntitySaveApi<Id, SaveResult> extends EntityApi<Id> {

  Class<SaveResult> entitySaveResultClass();
}

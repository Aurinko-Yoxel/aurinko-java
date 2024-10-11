package com.yoxel.aurinko.apis;

/**
 *
 */
public interface EntityPageApi<Id, Page> extends EntityApi<Id> {

  Class<Page> entityPageClass();
}

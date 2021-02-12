package com.yoxel.aurinko.apis;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.json.JsonHttpContent;

import com.yoxel.aurinko.bean.AurLiveIdEntity;

import java.io.IOException;

/**
 *
 */
public interface CreateSupport<Entity extends AurLiveIdEntity, SaveResult>
    extends EntitySaveApi<Entity, SaveResult>, HttpApi {

  default SaveResult create(Entity entity) throws IOException {
    return create(entity, QueryParams.EMPTY);
  }
  default SaveResult create(Entity entity, QueryParams params) throws IOException {

    return httpPost(entityFullPath(), params, new JsonHttpContent(Utils.getDefaultJsonFactory(), entity))
        .parseAs(entitySaveResultClass());
  }

}

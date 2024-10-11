package com.yoxel.aurinko.apis;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.json.JsonHttpContent;

import java.io.IOException;

/**
 *
 */
public interface CreateSupport<Entity, Id, SaveResult>
    extends EntitySaveApi<Id, SaveResult>, HttpApi {

  default SaveResult create(Entity entity) throws IOException {
    return create(entity, QueryParams.EMPTY);
  }
  default SaveResult create(Entity entity, QueryParams params) throws IOException {

    return httpPost(entityPath(), params, new JsonHttpContent(Utils.getDefaultJsonFactory(), entity))
        .parseAs(entitySaveResultClass());
  }

}

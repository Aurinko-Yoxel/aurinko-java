package com.yoxel.aurinko.apis;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.json.JsonHttpContent;

import com.yoxel.aurinko.bean.AurIdEntity;
import com.yoxel.aurinko.bean.AurSaveResult;

import java.io.IOException;

/**
 *
 */
public interface CreateSupport<Entity extends AurIdEntity, SaveResult>
    extends EntitySaveApi<Entity, SaveResult>, HttpApi {

  default SaveResult create(Entity entity) throws IOException {

    return httpPost(entityRoot(), new JsonHttpContent(Utils.getDefaultJsonFactory(), entity))
        .parseAs(entitySaveResultClass());
  }

}

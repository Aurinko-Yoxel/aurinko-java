package com.yoxel.aurinko.apis;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.json.JsonHttpContent;

import com.yoxel.aurinko.bean.AurIdEntity;

import java.io.IOException;

/**
 *
 */
public interface UpdateSupport<Entity extends AurIdEntity, SaveResult>
    extends EntitySaveApi<Entity, SaveResult>, HttpApi {

  default SaveResult update(String id, Entity entity) throws IOException {
    return update(id, entity, QueryParams.EMPTY, null);
  }

  default SaveResult update(String id, Entity entity, String etag) throws IOException {
    return update(id, entity, QueryParams.EMPTY, etag);
  }

  default SaveResult update(String id, Entity entity, QueryParams params) throws IOException {
    return update(id, entity, params, null);
  }

  default SaveResult update(String id, Entity entity, QueryParams params, String etag) throws IOException {
    final HttpRequest request = httpPatchPrepare(
        entityFullPath() + "/" + normalizeId(id),
        params,
        new JsonHttpContent(Utils.getDefaultJsonFactory(), entity)
    );

    if (etag != null) {
      request.getHeaders().setIfMatch(etag);
    }

    return request.execute().parseAs(entitySaveResultClass());
  }
}

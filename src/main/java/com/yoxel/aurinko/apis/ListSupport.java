package com.yoxel.aurinko.apis;

import com.yoxel.aurinko.bean.AurIdEntity;
import com.yoxel.aurinko.bean.AurQueryResult;
import com.yoxel.commons.xstream.IOXStream;
import com.yoxel.commons.xstream.XStream;

import java.io.IOException;
import java.util.function.Consumer;

/**
 *
 */
public interface ListSupport<Entity extends AurIdEntity, Page extends AurQueryResult<Entity>>
    extends EntityListApi<Entity, Page>, EntityApi, HttpApi {

  default Page loadPage() throws IOException {
    return loadPage(QueryParams.EMPTY, null);
  }

  default Page loadPage(QueryParams query) throws IOException {

    return loadPage(query, null);
  }

  default Page loadPage(QueryParams query, String pageToken) throws IOException {

    return httpGet(
        entityRoot(),
        query.add("pageToken", pageToken)
    ).parseAs(entityPageClass());
  }

  default XStream<Entity, IOException> streamPaged() throws IOException {
    return streamPaged(QueryParams.EMPTY, null);
  }

  default XStream<Entity, IOException> streamPaged(QueryParams queryParams) throws IOException {
    return streamPaged(queryParams, null);
  }

  default XStream<Entity, IOException> streamPaged(Consumer<? super Page> onPage) throws IOException {
    return streamPaged(QueryParams.EMPTY, onPage);
  }

  default XStream<Entity, IOException> streamPaged(QueryParams queryParams, Consumer<? super Page> onPage)
      throws IOException {

    if (onPage == null) {
      onPage = v -> {
      };
    }

    // query pages, until we get a page with done=true | totalSize=0
    return IOXStream
        .iterateUntil(
            loadPage(queryParams, null),
            qr -> loadPage(queryParams, qr.getNextPageToken()),
            qr -> qr.getNextPageToken() == null
        )
        .peek(onPage)
        .map(Page::getRecords)
        .flatMap(IOXStream::of);
  }
}

package com.yoxel.aurinko.apis;

import com.yoxel.aurinko.bean.AurTokenPage;
import com.yoxel.commons.xstream.IOXStream;
import com.yoxel.commons.xstream.XStream;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Support for list API endpoints with token-based pagination.
 */
public interface ListSupport_TokenBased<Entity, Id, Page extends AurTokenPage<Entity>>
    extends EntityPageApi<Id, Page>, HttpApi {

  default Page loadPage() throws IOException {
    return loadPage(QueryParams.EMPTY, null);
  }

  default Page loadPage(QueryParams query) throws IOException {

    return loadPage(query, null);
  }

  default Page loadPage(QueryParams query, String pageToken) throws IOException {

    return httpGet(
        entityPath(),
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

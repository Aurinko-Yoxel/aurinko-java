package com.yoxel.aurinko.apis;

import com.yoxel.aurinko.bean.AurOffsetPage;
import com.yoxel.commons.xstream.IOXStream;
import com.yoxel.commons.xstream.XStream;

import java.io.IOException;

import static com.yoxel.aurinko.apis.QueryParams.qp;

/**
 * Support for list API endpoints with offset-based pagination.
 */
public interface ListSupport_OffsetBased<Entity, Id, Page extends AurOffsetPage<Entity>>
 extends HttpApi, EntityPageApi<Id, Page> {

  int defaultPageSize = 50;

  default Page loadPage(int limit, int offset, QueryParams query) throws IOException {
    return httpGet(
        entityPath(),
        query.addAll(qp("limit", limit), qp("offset", offset))
    ).parseAs(entityPageClass());
  }

  default XStream<Entity, IOException> streamPaged(int pageSize, QueryParams queryParams) throws IOException {

    return IOXStream
        .iterateUntil(
            loadPage(pageSize, 0, queryParams),
            pg -> loadPage(pg.getOffset() + pageSize, pageSize, queryParams),
            AurOffsetPage::isDone
        )
        .flatMap(pg -> IOXStream.of(pg.getRecords()));
  }

  default XStream<Entity, IOException> streamPaged(QueryParams queryParams) throws IOException {
    return streamPaged(defaultPageSize, queryParams);
  }

  default XStream<Entity, IOException> streamPaged() throws IOException {
    return streamPaged(defaultPageSize, QueryParams.EMPTY);
  }
}


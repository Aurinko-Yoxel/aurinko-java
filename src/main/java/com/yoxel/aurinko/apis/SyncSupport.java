package com.yoxel.aurinko.apis;

import com.yoxel.aurinko.bean.AurLiveIdEntity;
import com.yoxel.aurinko.bean.AurQueryResult;
import com.yoxel.aurinko.bean.AurSyncStatus;
import com.yoxel.commons.xstream.IOXStream;
import com.yoxel.commons.xstream.XStream;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Predicate;

import lombok.RequiredArgsConstructor;

import static com.yoxel.aurinko.apis.QueryParams.qp;

/**
 *
 */
public interface SyncSupport<Entity extends AurLiveIdEntity, Page extends AurQueryResult<Entity>>
    extends EntityListApi<Entity, Page>, HttpApi {

  @RequiredArgsConstructor
  enum SyncScope {
    UPDATED("updated"), DELETED("deleted");

    private final String path;
  }

  default AurSyncStatus syncStart() throws IOException {
    return syncStart(QueryParams.EMPTY);
  }

  default AurSyncStatus syncStart(QueryParams params) throws IOException {
    return httpPost(entityApiRoot() + "/sync", params).parseAs(AurSyncStatus.class);
  }

  default Page syncPage(
      SyncScope scope,
      String deltaToken,
      String pageToken,
      QueryParams queryParams
  ) throws IOException {

    return httpGet(
        entityApiRoot() + "/sync/" + scope.path,
        queryParams.addAll(
            qp("deltaToken", deltaToken),
            qp("pageToken", pageToken)
        )
    ).parseAs(entityPageClass());
  }

  default XStream<Entity, IOException> streamSync(
      SyncScope scope,
      SyncToken token,
      QueryParams queryParams,
      Consumer<? super Page> onPage,
      Predicate<? super Page> stopWhen
  ) throws IOException {

    if (onPage == null) {
      onPage = v -> {
      };
    }

    String deltaToken = null, pageToken = null;
    switch (token.getType()) {
      case DELTA:
        deltaToken = token.getValue();
        break;
      case PAGE:
        pageToken = token.getValue();
        break;
    }

    // query pages, until we get a page with done=true | totalSize=0
    return IOXStream
        .iterateUntil(
            syncPage(scope, deltaToken, pageToken, queryParams),
            qr -> syncPage(scope, null, qr.getNextPageToken(), queryParams),
            qr -> qr.getNextPageToken() == null || (stopWhen != null && stopWhen.test(qr))
        )
        .peek(onPage)
        .map(Page::getRecords)
        .flatMap(IOXStream::of);
  }
}

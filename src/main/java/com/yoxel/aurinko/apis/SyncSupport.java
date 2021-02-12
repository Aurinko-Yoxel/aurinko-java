package com.yoxel.aurinko.apis;

import com.yoxel.aurinko.bean.AurIdEntity;
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

  default Page syncPage(SyncScope scope, String deltaToken, String pageToken) throws IOException {

    return httpGet(
        entityApiRoot() + "/sync/" + scope.path,
        QueryParams.of(
            qp("deltaToken", deltaToken),
            qp("pageToken", pageToken)
        )
    ).parseAs(entityPageClass());
  }

  default XStream<Entity, IOException> streamSync(
      SyncScope scope,
      String pageOrDelta,
      Consumer<? super Page> onPage,
      Predicate<? super Page> stopWhen
  ) throws IOException {

    if (onPage == null) {
      onPage = v -> {
      };
    }

    final String deltaToken, pageToken;
    if (pageOrDelta.startsWith("page:")) {
      deltaToken = null;
      pageToken = pageOrDelta.substring(5);
    } else {
      deltaToken = pageOrDelta;
      pageToken = null;
    }

    // query pages, until we get a page with done=true | totalSize=0
    return IOXStream
        .iterateUntil(
            syncPage(scope, deltaToken, pageToken),
            qr -> syncPage(scope, null, qr.getNextPageToken()),
            qr -> qr.getNextPageToken() == null || (stopWhen != null && stopWhen.test(qr))
        )
        .peek(onPage)
        .map(Page::getRecords)
        .flatMap(IOXStream::of);
  }
}

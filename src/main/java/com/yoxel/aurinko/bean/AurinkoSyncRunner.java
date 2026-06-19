package com.yoxel.aurinko.bean;

import com.yoxel.aurinko.HttpErrors;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.apis.SyncSupport;
import com.yoxel.aurinko.apis.SyncSupport.SyncScope;
import com.yoxel.commons.xstream.functions.XConsumer;
import com.yoxel.commons.xstream.functions.XPredicate;

import java.io.IOException;
import java.util.function.Predicate;

import lombok.Getter;

/**
 *
 */
public class AurinkoSyncRunner<Entity extends AurLiveIdEntity, Page extends AurTokenPage<Entity>> {

  private final SyncSupport<Entity, Page> syncApi;
  private final SyncScope syncScope;
  private final SyncTokensStack tokenStack;

  private Predicate<Page> stopWhen;
  private XPredicate<Entity, IOException> filter = e -> true;

  private QueryParams queryParams = QueryParams.EMPTY;
  private boolean isSynced = false;

  public AurinkoSyncRunner<Entity, Page> withStopCondition(Predicate<Page> sw) {
    this.stopWhen = sw;
    return this;
  }

  public AurinkoSyncRunner<Entity, Page> withFilter(XPredicate<Entity, IOException> p) {
    this.filter = p;
    return this;
  }

  public AurinkoSyncRunner<Entity, Page> withQueryParams(QueryParams queryParams) {
    this.queryParams = queryParams;
    return this;
  }

  public AurinkoSyncRunner(SyncSupport<Entity, Page> syncApi, SyncScope syncScope, SyncTokensPair tokens) {
    this.syncApi = syncApi;
    this.syncScope = syncScope;
    this.tokenStack = new SyncTokensStack(tokens); // this stack can actually be limited to the size of 2.
  }

  public SyncTokensPair getNext() {
    if (!isSynced) {
      throw new IllegalStateException("Not synced yet");
    }

    final SyncTokensPair current = tokenStack.getCurrent();
    return current == null ? SyncTokensPair.EMPTY : current;
  }

  public void forEach(XConsumer<Entity, IOException> consumer) throws AurSyncException {

    if (isSynced) {
      throw new IllegalStateException("Already synced");
    }

    boolean isError = false;
    boolean badToken = false;

    try {
      syncApi
          .streamSync(
              syncScope, tokenStack.getCurrent().getNextToken(), queryParams,
              qr -> {
                if (qr.getNextPageToken() != null) {
                  tokenStack.pushPageToken(qr.getNextPageToken());
                } else {
                  tokenStack.pushDeltaToken(qr.getNextDeltaToken());
                }
              },
              stopWhen
          )
          .filter(filter)
          .forEach(consumer);

    } catch (IOException e) {

      isError = true;
      if ((HttpErrors.isBadRequest400(e) &&
           (e.getMessage().contains("token.notValid") || e.getMessage().contains("Page token is invalid")))
          || HttpErrors.isGone410(e)
      ) {
        badToken = true;
      }

      throw new AurSyncException(badToken, tokenStack.getCurrent(), e);
    } finally {

      isSynced = true;

      if (isError) {
        if (badToken) {
          tokenStack.resetPageOrClear();
        } else {
          tokenStack.removeLatest();
        }
      }
    }
  }

  public static class AurSyncException extends Exception {

    @Getter
    private final boolean badToken;
    @Getter
    private final SyncTokensPair tokensUsed;

    public AurSyncException(boolean badToken, SyncTokensPair tokensUsed, IOException cause) {
      super(cause);
      this.badToken = badToken;
      this.tokensUsed = tokensUsed;
    }

    public IOException getIOCause() {
      return ((IOException) getCause());
    }
  }
}

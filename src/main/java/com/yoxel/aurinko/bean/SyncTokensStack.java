package com.yoxel.aurinko.bean;

import java.util.LinkedList;

/**
 * Mutable stack of sync tokens.
 */
public class SyncTokensStack {

  private final LinkedList<SyncTokensPair> history = new LinkedList<>();

  public SyncTokensStack(SyncTokensPair tokens) {
    history.addFirst(tokens);
  }

  public SyncTokensPair getCurrent() {
    return history.isEmpty() ? null : history.getFirst();
  }

  public void pushPageToken(String value) {
    final SyncTokensPair current = getCurrent();
    history.addFirst(current == null ? SyncTokensPair.page(value) : current.withPageToken(value));
  }

  public void pushDeltaToken(String value) {
    history.addFirst(SyncTokensPair.delta(value));
  }

  /**
   *
   */
  public void resetPageOrClear() {
    final SyncTokensPair current = getCurrent();
    if (current.getPageToken() != null && current.getAttempts() < 1) {
      history.addFirst(current.resetPageToken());
    } else {
      history.clear();
    }
  }

  public void removeLatest() {
    if (!history.isEmpty()) {
      history.removeFirst();
    }
  }
}

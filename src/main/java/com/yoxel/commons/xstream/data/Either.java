package com.yoxel.commons.xstream.data;

import com.yoxel.commons.xstream.XOption;

import java.util.NoSuchElementException;

/**
 * Disjoint union of types L and R.
 * Should we rename it to E for brevity?
 */
public interface Either<L, R> {

  @SuppressWarnings("unchecked")
  static <L, R> Either<L, R> left(L value) {
    return (Either<L, R>) new Left<L>(value);
  }

  @SuppressWarnings("unchecked")
  static <L, R> Either<L, R> right(R value) {
    return (Either<L, R>) new Right<>(value);
  }

  default boolean isLeft() {
    return this instanceof Left;
  }

  default boolean isRight() {
    return this instanceof Right;
  }

  default L getLeft() {
    throw new NoSuchElementException();
  }

  default R getRight() {
    throw new NoSuchElementException();
  }

  default XOption<L> left() {
    return isLeft() ? XOption.some(getLeft()) : XOption.none();
  }

  default XOption<R> right() {
    return isRight() ? XOption.some(getRight()) : XOption.none();
  }
}

@lombok.RequiredArgsConstructor
class Left<L> implements Either<L, Void> {

  private final L value;

  @Override
  public L getLeft() {
    return value;
  }
}

@lombok.RequiredArgsConstructor
class Right<R> implements Either<Void, R> {

  private final R value;

  @Override
  public R getRight() {
    return value;
  }
}

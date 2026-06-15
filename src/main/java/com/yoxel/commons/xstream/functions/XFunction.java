package com.yoxel.commons.xstream.functions;

import java.util.Objects;

/**
 * @author leopold
 * @since 31/08/18
 */
@FunctionalInterface
public interface XFunction<F, T, X extends Exception> {

  T apply(F from) throws X;

  default <V> XFunction<F, V, X> andThen(XFunction<? super T, ? extends V, X> after) {
    Objects.requireNonNull(after);
    return (F f) -> after.apply(apply(f));
  }
}

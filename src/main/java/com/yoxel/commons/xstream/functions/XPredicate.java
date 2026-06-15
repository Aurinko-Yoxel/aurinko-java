package com.yoxel.commons.xstream.functions;

import java.util.function.Predicate;

/**
 * @author leopold
 * @since 31/08/18
 */
@FunctionalInterface
public interface XPredicate<T, X extends Exception> {

  boolean test(T t) throws X;

  default XPredicate<T, X> negate() {
    return t -> !test(t);
  }
}

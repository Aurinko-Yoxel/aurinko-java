package com.yoxel.commons.xstream.functions;

/**
 * @author leopold
 * @since 31/08/18
 */
@FunctionalInterface
public interface XConsumer<V, X extends Exception> {

  void accept(V v) throws X;
}

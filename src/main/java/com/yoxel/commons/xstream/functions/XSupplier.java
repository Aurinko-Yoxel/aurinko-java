package com.yoxel.commons.xstream.functions;

/**
 * @author leopold
 * @since 31/08/18
 */
@FunctionalInterface
public interface XSupplier<V, X extends Exception> {

  V get() throws X;
}

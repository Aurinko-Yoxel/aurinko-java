package com.yoxel.commons.xstream.functions;

/**
 * @author leopold
 * @since 31/08/18
 */
@FunctionalInterface
public interface XRunnable<X extends Exception> {

  void run() throws X;
}

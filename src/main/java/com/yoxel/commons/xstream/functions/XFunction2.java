package com.yoxel.commons.xstream.functions;

/**
 * @author leopold
 * @since 31/08/18
 */
public interface XFunction2<F1, F2, T, X extends Exception> {

  T apply(F1 from1, F2 from2) throws X;

}

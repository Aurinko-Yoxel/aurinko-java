package com.yoxel.commons.xstream;

import com.yoxel.commons.xstream.functions.XSupplier;
import com.yoxel.commons.xstream.functions.XUnaryOperator;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

/**
 * Factory methods for creating XStream<T, IOException>.
 *
 * @author leopold
 * @since 31/08/18
 */
public final class IOXStream {

  private IOXStream() {}

  @SafeVarargs
  public static <T> XStream<T, IOException> of(T... values) {
    return XStream.of(values);
  }

  public static <T> XStream<T, IOException> ofAll(Collection<T> collection) {
    return XStream.ofAll(collection);
  }

  public static <T> XStream<T, IOException>
  iterateUntil(T seed, XUnaryOperator<T, IOException> generate, Predicate<T> stopWhen) {
    return XStream.iterateUntil(seed, generate, stopWhen);
  }

  public static <T> XStream<T, IOException>
  iterateWhile(T seed, XUnaryOperator<T, IOException> generate, Predicate<T> generateWhile) {
    return XStream.iterateWhile(seed, generate, generateWhile);
  }

  public static <T> XStream.XStreamBuilder<T, IOException> startWith(T t) {
    return XStream.startWith(t);
  }

  public static <T> XStream<T, IOException> empty() {
    return XStream.empty();
  }

  public <T> XStream<T, IOException> fromIterator(Iterator<T> iterator) {
    return XStream.fromIterator(iterator);
  }

  @SafeVarargs
  public static <T> XStream<T, IOException> lazy(XSupplier<T, IOException>... suppliers) {
    return XStream.lazy(suppliers);
  }


  @SafeVarargs
  public static <T> XStream<XSupplier<T, IOException>, IOException> suppliers(
      XSupplier<T, IOException>... suppliers) {
    return XStream.suppliers(suppliers);
  }
}

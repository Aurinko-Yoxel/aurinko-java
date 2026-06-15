package com.yoxel.commons.xstream;

import com.yoxel.commons.xstream.functions.XSupplier;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Factory methods for creating XStream<T, Z> (the one that doesn't throw checked exceptions).
 *
 * @author leopold
 * @since 31/08/18
 */
public final class ZStream {

  private ZStream() {}

  @SafeVarargs
  public static <T> XStream<T, Z> of(T... values) {
    return XStream.of(values);
  }

  public static <T> XStream<T, Z> ofAll(Collection<T> collection) {
    return XStream.ofAll(collection);
  }

  public static <T> XStream<T, Z>
  iterateUntil(T seed, UnaryOperator<T> generate, Predicate<T> stopWhen) {
    return XStream.iterateUntil(seed, generate::apply, stopWhen);
  }

  public static <T> XStream<T, Z>
  iterateWhile(T seed, UnaryOperator<T> generate, Predicate<T> generateWhile) {
    return XStream.iterateWhile(seed, generate::apply, generateWhile);
  }

  public static <T> XStream.XStreamBuilder<T, Z> startWith(T t) {
    return XStream.startWith(t);
  }

  public static <T> XStream<T, Z> empty() {
    return XStream.empty();
  }

  public static <T> XStream<T, Z> fromIterator(Iterator<T> iterator) {
    return XStream.fromIterator(iterator);
  }

  @SafeVarargs
  public static <T> XStream<T, Z> lazy(XSupplier<T, Z>... suppliers) {
    return XStream.lazy(suppliers);
  }

  @SafeVarargs
  public static <T> XStream<Supplier<T>, Z> suppliers(Supplier<T>... suppliers) {
    return XStream.of(suppliers);
  }
}

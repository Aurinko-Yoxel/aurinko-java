package com.yoxel.commons.xstream;

import com.yoxel.commons.xstream.functions.XConsumer;
import com.yoxel.commons.xstream.functions.XFunction;
import com.yoxel.commons.xstream.functions.XPredicate;
import com.yoxel.commons.xstream.functions.XSupplier;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author leopold
 * @since 5/10/18
 */
final class Unsafe {

  public Unsafe() {
  }

  @SuppressWarnings("unchecked")
  static <T> Stream<T> stream(XStream<T, ?> xStream) {
    return ((XStreamImpl<T, ?>) xStream).internal;
  }

  static <F, T> Function<F, T> unsafeF(XFunction<F, T, ?> f) {
    return from -> {
      try {
        return f.apply(from);
      } catch (Exception e) {
        throw sneakyThrow(e);
      }
    };
  }

  static <T> Consumer<T> unsafeC(XConsumer<T, ?> c) {
    return t -> {
      try {
        c.accept(t);
      } catch (Exception e) {
        throw sneakyThrow(e);
      }
    };
  }

  static <T> Predicate<T> unsafeP(XPredicate<T, ?> p) {
    return t -> {
      try {
        return p.test(t);
      } catch (Exception e) {
        throw sneakyThrow(e);
      }
    };
  }

  @SuppressWarnings("unchecked")
  static <T extends Throwable> RuntimeException sneakyThrow(Throwable t) throws T {
    throw (T) t;
  }

  static <T extends Exception, V> V sneakyThrows(XSupplier<V, T> sup) {
    try {
      return sup.get();
    } catch (Throwable t) {
      throw sneakyThrow(t);
    }
  }

}

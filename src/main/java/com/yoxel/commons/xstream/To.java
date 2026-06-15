package com.yoxel.commons.xstream;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Some predefined collectors.
 *
 * @author leopold
 * @since 6/09/18
 */
public final class To {

  private To() {
  }

  /**
   * Self-explanatory
   */
  public static Collector<String, ?, Set<String>>
  caseInsensitiveSet() {
    return Collectors.toCollection(() -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER));
  }

  /**
   * Group values using {@code keyMapper}, ignoring duplicate keys.
   */
  public static <T, K> Collector<T, ?, Map<K, T>>
  mergingMap(Function<? super T, ? extends K> keyMapper) {
    return Collectors.toMap(keyMapper, Function.identity(), (t1, t2) -> t1);
  }
}

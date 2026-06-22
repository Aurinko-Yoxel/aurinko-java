package com.yoxel.commons.xstream;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author leopold
 * @since 22/01/18
 */
public class XStreamTest {

  @Test
  public void testBuilder() {
    final List<Integer> integers = ZStream
        .startWith(3) // 3
        .nextValue(i -> i * 3)  // 9
        .nextValue(i -> i - 2) // 7
        .nextValues(i -> asList(i + 3, i + 6)) // 10, 13
        .nextValues(i -> asList(i + 1, i + 11)) // 14, 24
        .nextValue(i -> i * 10) // 240
        .generate()
        .toList();

    assertEquals(asList(3, 9, 7, 10, 13, 14, 24, 240), integers);
  }

  @Test
  public void testBuilderLaziness() {
    final AtomicBoolean reached = new AtomicBoolean(false);

    ZStream
        .startWith(3)
        .nextValue(i -> {
          reached.set(true);
          return i * 2;
        })
        .generate()
        .filter(i -> i == 3)
        .head();

    assertFalse(reached.get());
  }

  @Test
  public void testBuilderLaziness2() {
    final AtomicBoolean reached = new AtomicBoolean(false);

    ZStream
        .startWith(3)
        .nextValue(i -> i * 3)
        .nextValues(i -> {
          reached.set(true);
          return asList(i, i * 2);
        })
        .generate()
        .filter(i -> i == 9)
        .head();

    assertFalse(reached.get());
  }

  @Test
  public void testFlatMapOpt() {
    final List<Integer> integers = ZStream
        .of(1, 2, 3, 4, 5, 6)
        .flatMapOpt(i -> XOption.some(i).filter(j -> j % 2 == 0))
        .toList();

    assertEquals(asList(2, 4, 6), integers);
  }

  @Test
  public void testDistinctBy() {
    @RequiredArgsConstructor
    class Wrapper {

      final int value;
    }

    final Wrapper w11 = new Wrapper(1);
    final Wrapper w12 = new Wrapper(1);
    final Wrapper w13 = new Wrapper(1);

    final List<Wrapper> result = ZStream
        .of(w11, new Wrapper(2), w12, new Wrapper(3), w13, new Wrapper(4))
        .distinctBy(w -> w.value)
        .toList();

    assertThat(result)
            .extracting(w -> w.value)
            .containsExactly(1, 2, 3, 4);

    assertThat(result.getFirst())
            .isSameAs(w11);
  }

  @Test
  public void testFlatMapOptLaziness() {
    final AtomicBoolean reached5 = new AtomicBoolean(false);
    final AtomicBoolean reached6 = new AtomicBoolean(false);
    ZStream.lazy(
        () -> 1,
        () -> 2,
        () -> 3,
        () -> 4,
        () -> {
          reached5.set(true);
          return 5;
        },
        () -> {
          reached6.set(true);
          return 6;
        }
    )
        .flatMapOpt(i -> XOption.some(i).filter(j -> j % 2 == 0))
        .filter(i -> i == 4)
        .head();

    assertFalse(reached6.get());
    assertFalse(reached5.get());

  }
}

package com.yoxel.commons.xstream;

import org.junit.Test;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author leopold
 * @since 6/09/17
 */
public class StreamsTest {

  @Test
  public void iterateWhileEmpty() {
    assertArrayEquals(
        Stream.empty().toArray(),
        Streams.iterateWhile(1, i -> i + 1, i -> i < 0).toArray()
    );
  }

  @Test
  public void iterateWhileSingle() {
    assertArrayEquals(
        Stream.of(1).toArray(),
        Streams.iterateWhile(1, i -> i + 1, i -> i <= 1).toArray()
    );
  }

  @Test
  public void iterateWhile() {
    assertArrayEquals(
        IntStream.range(1, 20).boxed().toArray(),
        Streams.iterateWhile(1, i -> i + 1, i -> i < 20).toArray()
    );
  }

  @Test
  public void iterateUntilSingle() {
    assertArrayEquals(
        Stream.of(1).toArray(),
        Streams.iterateUntil(1, i -> i + 1, i -> i > 0).toArray()
    );
  }

  @Test
  public void iterateUntil() {
    assertArrayEquals(
        IntStream.range(1, 21).boxed().toArray(),
        Streams.iterateUntil(1, i -> i + 1, i -> i == 20).toArray()
    );
  }

}
package com.yoxel.commons.xstream;

import com.yoxel.commons.xstream.functions.XConsumer;
import com.yoxel.commons.xstream.functions.XFunction;
import com.yoxel.commons.xstream.functions.XPredicate;
import com.yoxel.commons.xstream.functions.XSupplier;
import com.yoxel.commons.xstream.functions.XUnaryOperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.yoxel.commons.xstream.Unsafe.unsafeC;
import static com.yoxel.commons.xstream.Unsafe.unsafeF;
import static com.yoxel.commons.xstream.Unsafe.unsafeP;

/**
 * Wrapper over {@link java.util.stream.Stream} that deals with checked exceptions.
 * Its combinator methods (map, flatMap, filter, etc) accept functions that can throw an exception.
 * Its terminal methods (foreach, toList, head etc) throw exceptions themselves.
 *
 * Mostly, its methods are just wrappers over {@link Stream}, but there are some
 * additional methods that don't have counterparts in standard stream.
 *
 * Attention! If you add terminal methods to this interface, don't forget to specify
 * {@code throws X} in the signature. The implementation is using "sneaky-throw" technic to pass
 * X-functions (XFunction, XPredicate, etc.) to the underlying java.lang.Stream, so the compiler
 * will not force you to declare throwing checked exceptions.
 *
 * @author leopold
 * @since 31/08/18
 */
public interface XStream<T, X extends Exception> {

  /**
   * This method allows creating XStreams parametrized with any {@code X extends Exception},
   * but without providing explicit type parameter for {@code T}.
   *
   * Compare two following ways of creating XStream:
   *
   * <pre>
   *   {@code
   *     MyArbitraryClassWithLongName a, b, c = ...;
   *
   *     // Need to specify both type parameters:
   *     XStream.<MyArbitraryClassWithLongName, IOException>of(a, b, c);
   *
   *     // Here we specify only one type parameter,
   *     // the second one will be inferred by the compiler:
   *     XStream.<IOException>create().of(a, b, c);
   *   }
   * </pre>
   *
   * Both ways are completely equivalent, so it's up to the user of the class which one to choose.
   */
  @SuppressWarnings("unchecked")
  static <X extends Exception> XStreamStarter<X> create() {
    return XStreamStarter.INSTANCE;
  }

  /**
   * @see Stream#of(Object[])
   */
  @SafeVarargs
  static <T, X extends Exception> XStream<T, X> of(T... values) {
    return new XStreamImpl<>(Stream.of(values));
  }

  /**
   * @see Collection#stream()
   */
  static <T, X extends Exception> XStream<T, X> ofAll(Collection<T> collection) {
    return new XStreamImpl<>(collection.stream());
  }

  /**
   * @see Stream#empty()
   */
  static <T, X extends Exception> XStream<T, X> empty() {
    return new XStreamImpl<>(Stream.empty());
  }

  static <T, X extends Exception> XStream<T, X> fromIterator(Iterator<T> iterator) {
    return new XStreamImpl<>(Streams.fromIterator(iterator));
  }

  /**
   * @see Stream#concat(Stream, Stream)
   */
  static <T, X extends Exception> XStream<T, X> concat(XStream<? extends T, ? extends X> fst,
                                                       XStream<? extends T, ? extends X> snd) {
    return new XStreamImpl<>(Stream.concat(Unsafe.stream(fst), Unsafe.stream(snd)));
  }

  /**
   * Create an XStreamBuilder with {@code t} as the first element. This method should be used
   * for creating a stream, each element (or group of elements) of which has a dependency on
   * the previous value.
   */
  static <T, X extends Exception> XStreamBuilder<T, X> startWith(T t) {
    return new XStreamBuilder<>(t);
  }

  /**
   * Produce a Stream of values, given a bunch of suppliers.
   */
  @SafeVarargs
  static <T, X extends Exception> XStream<T, X> lazy(XSupplier<T, X>... suppliers) {
    return new XStreamImpl<XSupplier<T, X>, X>(Stream.of(suppliers)).map(XSupplier::get);
  }

  /**
   * Produce a Stream of values, given a bunch of suppliers.
   */
  @SafeVarargs
  static <T, X extends Exception> XStream<XSupplier<T, X>, X>
  suppliers(XSupplier<T, X>... suppliers) {
    return of(suppliers);
  }

  /**
   * Create a stream from a seed and a generator function that will be used to produce
   * new elements until it returns a value satisfying {@code stopWhen} predicate.
   * This last value WILL BE included in the resulting stream.
   */
  static <T, X extends Exception> XStream<T, X> iterateUntil(T seed, XUnaryOperator<T, X> generate,
                                                             Predicate<T> stopWhen) {
    return new XStreamImpl<>(
        Streams.iterateUntil(seed, unsafeF(generate), stopWhen)
    );
  }

  /**
   * Create a stream from a seed and a generator function that will be used to produce
   * new elements while it returns values satisfying {@code generateWhile} predicate.
   * The first value that does not satisfy the predicate WILL NOT BE included.
   */
  static <T, X extends Exception> XStream<T, X> iterateWhile(T seed, XUnaryOperator<T, X> generate,
                                                             Predicate<T> generateWhile) {
    return new XStreamImpl<>(
        Streams.iterateWhile(seed, unsafeF(generate), generateWhile)
    );
  }

  /**
   * Concat the stream with a value, that is not yet computed
   */
  default XStream<T, X> then(XSupplier<? extends T, ? extends X> supplier) {
    return concat(this, lazy(supplier));
  }

  default XStream<T, X> appendIf(boolean condition, T value) {
    return appendIfLazy(condition, () -> value);
  }

  default XStream<T, X> appendIfLazy(boolean condition,
                                     XSupplier<? extends T, ? extends X> supplier) {
    return condition ? then(supplier) : this;
  }
  /**
   * Concat the stream with another stream
   */
  default XStream<T, X> contactWith(XStream<? extends T, ? extends X> another) {
    return concat(this, another);
  }

  /**
   * @see Stream#forEach(Consumer)
   */
  void forEach(XConsumer<? super T, ? extends X> consumer) throws X;

  /**
   * @see Stream#map(Function)
   */
  <R> XStream<R, X> map(XFunction<? super T, ? extends R, X> f);

  /**
   * @see Stream#flatMap(Function)
   */
  <R> XStream<R, X> flatMap(XFunction<? super T, XStream<? extends R, X>, X> f);

  /**
   * @see Stream#filter(Predicate)
   */
  XStream<T, X> filter(XPredicate<? super T, X> predicate);

  /**
   * @see Stream#peek(Consumer)
   */
  XStream<T, X> peek(Consumer<? super T> cons);

  /**
   * @see Stream#findFirst()
   */
  XOption<T> head() throws X;

  /**
   * More intuitive shortcut for filter(...).head()
   */
  default XOption<T> firstWhere(XPredicate<? super T, X> predicate) throws X {
    return filter(predicate).head();
  }

  /**
   * @see Collectors#toList()
   */
  List<T> toList() throws X;

  /**
   * @see Collectors#toSet()
   */
  Set<T> toSet() throws X;


  /**
   * @see Collectors#toMap(Function, Function)
   */
  <K, U> Map<K, U> toMap(Function<? super T, ? extends K> keyMapper,
                         Function<? super T, ? extends U> valueMapper) throws X;

  /**
   * @see Collectors#toMap(Function, Function, BinaryOperator)
   */
  <K, U> Map<K, U> toMap(Function<? super T, ? extends K> keyMapper,
                         Function<? super T, ? extends U> valueMapper,
                         BinaryOperator<U> mergeFunction) throws X;

  /**
   * @see Collectors#partitioningBy(Predicate)
   */
  Map<Boolean, List<T>> partition(Predicate<? super T> predicate) throws X;

  /**
   * @see Collectors#partitioningBy(Predicate, Collector)
   */
  <D> Map<Boolean, D> partition(Predicate<? super T> predicate,
                                Collector<? super T, ?, D> downstream) throws X;

  /**
   * @see Stream#collect(Collector)
   */
  <R, A> R to(Collector<? super T, A, R> collector) throws X;

  /**
   * @see Collectors#toCollection(Supplier)
   */
  <C extends Collection<T>> C toCollection(Supplier<C> supplier) throws X;

  /**
   * @see Stream#allMatch(Predicate)
   */
  boolean forall(Predicate<T> test) throws X;

  /**
   * @see Stream#anyMatch(Predicate)
   */
  boolean exists(Predicate<T> test) throws X;

  /**
   * @see Collectors#groupingBy(Function)
   */
  <K> Map<K, List<T>> groupBy(Function<T, K> keyExtractor) throws X;

  /**
   * @see Collectors#joining(CharSequence)
   */
  String mkString(CharSequence delimiter) throws X;

  /**
   * @see Collectors#joining(CharSequence, CharSequence, CharSequence)
   */
  String mkString(CharSequence delimiter, CharSequence prefix, CharSequence suffix) throws X;

  /**
   * @see Stream#reduce(BinaryOperator)
   */
  XOption<T> reduce(BinaryOperator<T> accumulator) throws X;

  /**
   * A version of flatMap that takes a function from T to Option, rather than from T to Stream.
   */
  default <R> XStream<R, X> flatMapOpt(XFunction<? super T, XOption<? extends R>, ? extends X> f) {
    return flatMap(t -> f.apply(t).toXStream());
  }

  default XStream<T, X> filterNot(XPredicate<? super T, X> predicate) {
    return filter(predicate.negate());
  }

  /**
   * Create a stream that consists of distinct elements (according to {@code keyFunction}).
   *
   * Repeating elements are filtered out only if {@code keyFunction} returns a non-null value for
   * them. If {@code keyFunction} returns null for some of the elements of the stream, they all
   * will be preserved in the resulting stream.
   */
  default <K> XStream<T, X> distinctBy(Function<T, K> keyFunction) {
    final Set<K> seenKeys = new HashSet<>();

    return filter(t -> {
      final K key = keyFunction.apply(t);
      return key == null || seenKeys.add(key);
    });
  }

  /**
   * A class for constructing a stream, each value (or group of values) of which depends on
   * a previous value. See `XStreamTest` for examples.
   *
   * @see XStream#startWith(Object)
   */
  class XStreamBuilder<T, X extends Exception> {

    private final T seed;
    private final List<XFunction<T, ? extends Collection<T>, ? extends X>>
        transformingFunctions = new ArrayList<>();

    XStreamBuilder(T seed) {
      this.seed = seed;
      nextValue(t -> t);
    }

    public XStreamBuilder<T, X> nextValues(XFunction<T, ? extends Collection<T>, ? extends X> f) {
      transformingFunctions.add(f);
      return this;
    }

    public XStreamBuilder<T, X> nextValue(XUnaryOperator<T, ? extends X> op) {
      return nextValues(op.andThen(Collections::singletonList));
    }

    public XStream<T, X> generate() {
      return new XStreamImpl<>(
          Streams.expandAll(
              seed,
              transformingFunctions.stream()
                  .map(Unsafe::unsafeF)
                  .collect(Collectors.toList())
          )
      );
    }

  }

  /**
   * Helper class helps creating a XStream, parametrized with any {@code X} but without explicit
   * type parameter for {@code T}.
   *
   * @see XStream#create()
   */
  class XStreamStarter<X extends Exception> {

    private XStreamStarter() {
    }

    private static final XStreamStarter INSTANCE = new XStreamStarter();

    @SafeVarargs
    public final <T> XStream<T, X> of(T... values) {
      return XStream.of(values);
    }

    public <T> XStream<T, X>
    ofAll(Collection<T> collection) {
      return XStream.ofAll(collection);
    }

    public <T> XStream<T, X>
    iterateUntil(T seed, XUnaryOperator<T, X> generate, Predicate<T> stopWhen) {
      return XStream.iterateUntil(seed, generate, stopWhen);
    }

    public <T> XStream<T, X>
    iterateWhile(T seed, XUnaryOperator<T, X> generate, Predicate<T> generateWhile) {
      return XStream.iterateWhile(seed, generate, generateWhile);
    }

    public <T> XStreamBuilder<T, X> startWith(T t) {
      return XStream.startWith(t);
    }

    public <T> XStream<T, X> empty() {
      return XStream.empty();
    }

    public <T> XStream<T, X> fromIterator(Iterator<T> iterator) {
      return XStream.fromIterator(iterator);
    }

    @SafeVarargs
    public final <T> XStream<T, X> lazy(XSupplier<T, X>... suppliers) {
      return XStream.lazy(suppliers);
    }
  }
}

class XStreamImpl<T, X extends Exception> implements XStream<T, X> {

  final Stream<T> internal;

  XStreamImpl(Stream<T> internal) {
    this.internal = internal;
  }

  @Override
  public void forEach(XConsumer<? super T, ? extends X> consumer) throws X {
    internal.forEach(unsafeC(consumer));
  }

  @Override
  public <R> XStream<R, X> map(XFunction<? super T, ? extends R, X> f) {
    return new XStreamImpl<>(internal.map(unsafeF(f)));
  }

  @Override
  public <R> XStream<R, X> flatMap(XFunction<? super T, XStream<? extends R, X>, X> f) {
    return new XStreamImpl<>(internal.flatMap(unsafeF(f).andThen(Unsafe::stream)));
  }

  @Override
  public XStream<T, X> filter(XPredicate<? super T, X> predicate) {
    return new XStreamImpl<>(internal.filter(unsafeP(predicate)));
  }

  @Override
  public XStream<T, X> peek(Consumer<? super T> cons) {
    return new XStreamImpl<>(internal.peek(cons));
  }

  @Override
  public XOption<T> head() throws X {
    return internal.findFirst()
        .map(XOption::some)
        .orElse(XOption.none());
  }

  @Override
  public List<T> toList() throws X {
    return internal.collect(Collectors.toList());
  }

  @Override
  public Set<T> toSet() throws X {
    return internal.collect(Collectors.toSet());
  }

  @Override
  public <K, U> Map<K, U> toMap(Function<? super T, ? extends K> keyMapper,
                                Function<? super T, ? extends U> valueMapper) {
    return internal.collect(Collectors.toMap(keyMapper, valueMapper));
  }

  @Override
  public <K, U> Map<K, U> toMap(Function<? super T, ? extends K> keyMapper,
                                Function<? super T, ? extends U> valueMapper,
                                BinaryOperator<U> mergeFunction) throws X {
    return internal.collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction));
  }

  @Override
  public Map<Boolean, List<T>> partition(Predicate<? super T> predicate) {
    return internal.collect(Collectors.partitioningBy(predicate));
  }

  @Override
  public <D> Map<Boolean, D> partition(Predicate<? super T> predicate,
                                       Collector<? super T, ?, D> downstream) {
    return internal.collect(Collectors.partitioningBy(predicate, downstream));
  }

  @Override
  public <R, A> R to(Collector<? super T, A, R> collector) {
    return internal.collect(collector);
  }

  @Override
  public <C extends Collection<T>> C toCollection(Supplier<C> supplier) throws X {
    return internal.collect(Collectors.toCollection(supplier));
  }

  @Override
  public boolean forall(Predicate<T> test) throws X {
    return internal.allMatch(test);
  }

  @Override
  public boolean exists(Predicate<T> test) throws X {
    return internal.anyMatch(test);
  }

  @Override
  public <K> Map<K, List<T>> groupBy(Function<T, K> keyExtractor) {
    return internal.collect(Collectors.groupingBy(keyExtractor));
  }

  @Override
  public String mkString(CharSequence delimiter) {
    return internal
        .map(Object::toString)
        .collect(Collectors.joining(delimiter));
  }

  @Override
  public String mkString(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
    return internal
        .map(Object::toString)
        .collect(Collectors.joining(delimiter, prefix, suffix));
  }

  @Override
  public XOption<T> reduce(BinaryOperator<T> accumulator) {
    return XOption.ofOptional(internal.reduce(accumulator));
  }
}


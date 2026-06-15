package com.yoxel.commons.xstream;

import com.yoxel.commons.xstream.data.P;
import com.yoxel.commons.xstream.functions.XConsumer;
import com.yoxel.commons.xstream.functions.XFunction;
import com.yoxel.commons.xstream.functions.XRunnable;
import com.yoxel.commons.xstream.functions.XSupplier;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author leopold
 * @since 23/08/18
 */
public abstract class XOption<T> {

  XOption() {
  }

  /**
   * Given two options, this function returns an option containing product of their values.
   * Result is empty if any of two options is empty.
   */
  public static <T, U> XOption<P<T, U>> both(XOption<T> to, XOption<U> uo) {
    // kind of ugly:
    return to.<P<T, U>, Z>flatMap(t -> uo.map(u -> P.of(t, u)));
  }

  public abstract XOption<T> filter(Predicate<T> test);

  public abstract <U, X extends Exception> XOption<U> map(XFunction<? super T, ? extends U, X> f)
      throws X;

  public abstract <U, X extends Exception> XOption<U> flatMap(XFunction<? super T, XOption<U>, X> f)
      throws X;

  public abstract <X extends Exception> void forEach(XConsumer<T, X> consumer) throws X;

  public abstract boolean isDefined();

  public abstract T get() throws NoSuchElementException;

  public XOption<T> filterNot(Predicate<T> test) {
    return filter(test.negate());
  }

  public T getOrNull() {
    return isDefined() ? get() : null;
  }

  public T getOrElse(T alt) {
    return isDefined() ? get() : alt;
  }

  public <X extends Exception> T getOrElseGet(Supplier<T> alt) {
    return isDefined() ? get() : alt.get();
  }

  public <X extends Throwable> T getOrThrow(Supplier<X> x) throws X {
    if (isDefined()) {
      return get();
    } else {
      throw x.get();
    }
  }

  public boolean forall(Predicate<T> predicate) {
    return !isDefined() || predicate.test(get());
  }

  public boolean exists(Predicate<T> predicate) {
    return isDefined() && predicate.test(get());
  }

  public boolean contains(T value) {
    return exists(v -> Objects.equals(v, value));
  }

  @SuppressWarnings("unchecked")
  public <X extends Exception> XOption<T> orElse(XSupplier<XOption<? extends T>, X> alt) throws X {
    return isDefined() ? this : (XOption<T>) alt.get();
  }

  public boolean isEmpty() {
    return !isDefined();
  }

  public <U, X extends Exception>
  XStream<U, X> flatMapStream(XFunction<? super T, XStream<U, X>, X> f) throws X {
    return map(f).getOrElse(XStream.empty());
  }

  public XStream<T, Z> toZStream() {
    return toXStream();
  }

  public <X extends Exception> XStream<T, X> toXStream() {
    return isDefined() ? XStream.of(get()) : XStream.empty();
  }

  public <X extends Exception> XOption<T> peek(XConsumer<T, X> action) throws X {
    if (isDefined()) {
      action.accept(get());
    }
    return this;
  }

  public <X extends Exception> XOption<T> onEmpty(XRunnable<X> action) throws X {
    if (isEmpty()) {
      action.run();
    }
    return this;
  }

  @SuppressWarnings("unchecked")
  public static <T> XOption<T> none() {
    return (XNone<T>) XNone.INSTANCE;
  }

  public static <T> XOption<T> some(T t) {
    return new XSome<>(t);
  }

  public static <T> XOption<T> of(T t) {
    return t == null ? none() : new XSome<>(t);
  }

  public static <T> XOption<T> ofOptional(Optional<T> optional) {
    return optional
        .map(XOption::some)
        .orElse(none());
  }
}

class XNone<T> extends XOption<T> {

  static final XNone<Object> INSTANCE = new XNone<>();

  @Override
  public XOption<T> filter(Predicate<T> test) {
    return this;
  }

  @Override
  public <U, X extends Exception> XOption<U> map(XFunction<? super T, ? extends U, X> f) {
    return XOption.none();
  }

  @Override
  public <U, X extends Exception> XOption<U> flatMap(XFunction<? super T, XOption<U>, X> f) {
    return XOption.none();
  }

  @Override
  public boolean isDefined() {
    return false;
  }

  @Override
  public T get() {
    throw new NoSuchElementException();
  }

  @Override
  public <X extends Exception> void forEach(XConsumer<T, X> consumer) {
    //
  }

  @Override
  public String toString() {
    return "None";
  }
}

@lombok.RequiredArgsConstructor
class XSome<T> extends XOption<T> {

  private final T value;

  @Override
  public XOption<T> filter(Predicate<T> test) {
    return test.test(value) ? this : XOption.none();
  }

  @Override
  public <U, X extends Exception> XOption<U> map(XFunction<? super T, ? extends U, X> f)
      throws X {
    return new XSome<>(f.apply(value));
  }

  @Override
  public <U, X extends Exception> XOption<U> flatMap(XFunction<? super T, XOption<U>, X> f)
      throws X {
    return f.apply(value);
  }

  @Override
  public boolean isDefined() {
    return true;
  }

  @Override
  public T get() {
    return value;
  }

  @Override
  public <X extends Exception> void forEach(XConsumer<T, X> consumer) throws X {
    consumer.accept(value);
  }

  @Override
  public String toString() {
    return "Some(" + value + ")";
  }
}

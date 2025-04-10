package javolution.lang;

public interface ValueType<T> extends Immutable<T> {
  T value();
}


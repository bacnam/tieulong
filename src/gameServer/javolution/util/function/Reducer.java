package javolution.util.function;

import java.util.Collection;

public interface Reducer<E> extends Consumer<Collection<E>>, Supplier<E> {}


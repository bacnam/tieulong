package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.io.Serializable;

@Beta
public abstract class CacheLoader<K, V>
{
public static <K, V> CacheLoader<K, V> from(Function<K, V> function) {
return new FunctionToCacheLoader<K, V>(function);
}

private static final class FunctionToCacheLoader<K, V> extends CacheLoader<K, V> implements Serializable {
private final Function<K, V> computingFunction;
private static final long serialVersionUID = 0L;

public FunctionToCacheLoader(Function<K, V> computingFunction) {
this.computingFunction = (Function<K, V>)Preconditions.checkNotNull(computingFunction);
}

public V load(K key) {
return (V)this.computingFunction.apply(key);
}
}

public static <V> CacheLoader<Object, V> from(Supplier<V> supplier) {
return new SupplierToCacheLoader<V>(supplier);
}

public abstract V load(K paramK) throws Exception;

private static final class SupplierToCacheLoader<V> extends CacheLoader<Object, V> implements Serializable { private final Supplier<V> computingSupplier;

public SupplierToCacheLoader(Supplier<V> computingSupplier) {
this.computingSupplier = (Supplier<V>)Preconditions.checkNotNull(computingSupplier);
}
private static final long serialVersionUID = 0L;

public V load(Object key) {
return (V)this.computingSupplier.get();
} }

}


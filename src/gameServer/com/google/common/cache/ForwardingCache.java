package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingObject;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import javax.annotation.Nullable;

@Beta
public abstract class ForwardingCache<K, V>
extends ForwardingObject
implements Cache<K, V>
{
@Nullable
public V get(@Nullable K key) throws ExecutionException {
return delegate().get(key);
}

@Nullable
public V getUnchecked(@Nullable K key) {
return delegate().getUnchecked(key);
}

@Deprecated
@Nullable
public V apply(@Nullable K key) {
return delegate().apply(key);
}

public void invalidate(@Nullable Object key) {
delegate().invalidate(key);
}

public void invalidateAll() {
delegate().invalidateAll();
}

public long size() {
return delegate().size();
}

public CacheStats stats() {
return delegate().stats();
}

public ConcurrentMap<K, V> asMap() {
return delegate().asMap();
}

public void cleanUp() {
delegate().cleanUp();
}

protected abstract Cache<K, V> delegate();

@Beta
public static abstract class SimpleForwardingCache<K, V>
extends ForwardingCache<K, V>
{
private final Cache<K, V> delegate;

protected SimpleForwardingCache(Cache<K, V> delegate) {
this.delegate = (Cache<K, V>)Preconditions.checkNotNull(delegate);
}

protected final Cache<K, V> delegate() {
return this.delegate;
}
}
}


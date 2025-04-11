package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

@Beta
public interface Cache<K, V> extends Function<K, V> {
    V get(K paramK) throws ExecutionException;

    V getUnchecked(K paramK);

    V apply(K paramK);

    void invalidate(Object paramObject);

    void invalidateAll();

    long size();

    CacheStats stats();

    ConcurrentMap<K, V> asMap();

    void cleanUp();
}


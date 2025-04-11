package com.google.common.cache;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;

import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

class ComputingCache<K, V>
        extends AbstractCache<K, V>
        implements Serializable {
    private static final long serialVersionUID = 1L;
    final CustomConcurrentHashMap<K, V> map;

    ComputingCache(CacheBuilder<? super K, ? super V> builder, Supplier<? extends AbstractCache.StatsCounter> statsCounterSupplier, CacheLoader<? super K, V> loader) {
        this.map = new CustomConcurrentHashMap<K, V>(builder, statsCounterSupplier, loader);
    }

    public V get(K key) throws ExecutionException {
        return this.map.getOrCompute(key);
    }

    public void invalidate(Object key) {
        Preconditions.checkNotNull(key);
        this.map.remove(key);
    }

    public void invalidateAll() {
        this.map.clear();
    }

    public long size() {
        return this.map.longSize();
    }

    public ConcurrentMap<K, V> asMap() {
        return this.map;
    }

    public CacheStats stats() {
        AbstractCache.SimpleStatsCounter aggregator = new AbstractCache.SimpleStatsCounter();
        for (CustomConcurrentHashMap.Segment<K, V> segment : this.map.segments) {
            aggregator.incrementBy(segment.statsCounter);
        }
        return aggregator.snapshot();
    }

    public void cleanUp() {
        this.map.cleanUp();
    }

    Object writeReplace() {
        return this.map.cacheSerializationProxy();
    }
}


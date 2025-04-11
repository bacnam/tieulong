package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.util.concurrent.UncheckedExecutionException;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

@Beta
public abstract class AbstractCache<K, V>
        implements Cache<K, V> {
    public V getUnchecked(K key) {
        try {
            return get(key);
        } catch (ExecutionException e) {
            throw new UncheckedExecutionException(e.getCause());
        }
    }

    public final V apply(K key) {
        return getUnchecked(key);
    }

    public void cleanUp() {
    }

    public long size() {
        throw new UnsupportedOperationException();
    }

    public void invalidate(Object key) {
        throw new UnsupportedOperationException();
    }

    public void invalidateAll() {
        throw new UnsupportedOperationException();
    }

    public CacheStats stats() {
        throw new UnsupportedOperationException();
    }

    public ConcurrentMap<K, V> asMap() {
        throw new UnsupportedOperationException();
    }

    @Beta
    public static interface StatsCounter {
        void recordHit();

        void recordLoadSuccess(long param1Long);

        void recordLoadException(long param1Long);

        void recordConcurrentMiss();

        void recordEviction();

        CacheStats snapshot();
    }

    @Beta
    public static class SimpleStatsCounter
            implements StatsCounter {
        private final AtomicLong hitCount = new AtomicLong();
        private final AtomicLong missCount = new AtomicLong();
        private final AtomicLong loadSuccessCount = new AtomicLong();
        private final AtomicLong loadExceptionCount = new AtomicLong();
        private final AtomicLong totalLoadTime = new AtomicLong();
        private final AtomicLong evictionCount = new AtomicLong();

        public void recordHit() {
            this.hitCount.incrementAndGet();
        }

        public void recordLoadSuccess(long loadTime) {
            this.missCount.incrementAndGet();
            this.loadSuccessCount.incrementAndGet();
            this.totalLoadTime.addAndGet(loadTime);
        }

        public void recordLoadException(long loadTime) {
            this.missCount.incrementAndGet();
            this.loadExceptionCount.incrementAndGet();
            this.totalLoadTime.addAndGet(loadTime);
        }

        public void recordConcurrentMiss() {
            this.missCount.incrementAndGet();
        }

        public void recordEviction() {
            this.evictionCount.incrementAndGet();
        }

        public CacheStats snapshot() {
            return new CacheStats(this.hitCount.get(), this.missCount.get(), this.loadSuccessCount.get(), this.loadExceptionCount.get(), this.totalLoadTime.get(), this.evictionCount.get());
        }

        public void incrementBy(AbstractCache.StatsCounter other) {
            CacheStats otherStats = other.snapshot();
            this.hitCount.addAndGet(otherStats.hitCount());
            this.missCount.addAndGet(otherStats.missCount());
            this.loadSuccessCount.addAndGet(otherStats.loadSuccessCount());
            this.loadExceptionCount.addAndGet(otherStats.loadExceptionCount());
            this.totalLoadTime.addAndGet(otherStats.totalLoadTime());
            this.evictionCount.addAndGet(otherStats.evictionCount());
        }
    }
}


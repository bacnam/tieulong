package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.base.*;
import com.google.common.collect.ForwardingConcurrentMap;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.UncheckedExecutionException;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Beta
public final class CacheBuilder<K, V> {
    static final CacheStats EMPTY_STATS = new CacheStats(0L, 0L, 0L, 0L, 0L, 0L);
    static final Supplier<? extends AbstractCache.StatsCounter> DEFAULT_STATS_COUNTER = Suppliers.ofInstance(new AbstractCache.StatsCounter() {
        public void recordHit() {
        }

        public void recordLoadSuccess(long loadTime) {
        }

        public void recordLoadException(long loadTime) {
        }

        public void recordConcurrentMiss() {
        }

        public void recordEviction() {
        }

        public CacheStats snapshot() {
            return CacheBuilder.EMPTY_STATS;
        }
    });
    static final Supplier<AbstractCache.SimpleStatsCounter> CACHE_STATS_COUNTER = new Supplier<AbstractCache.SimpleStatsCounter>() {
        public AbstractCache.SimpleStatsCounter get() {
            return new AbstractCache.SimpleStatsCounter();
        }
    };
    static final int UNSET_INT = -1;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
    private static final int DEFAULT_EXPIRATION_NANOS = 0;
    int initialCapacity = -1;
    int concurrencyLevel = -1;
    int maximumSize = -1;
    CustomConcurrentHashMap.Strength keyStrength;
    CustomConcurrentHashMap.Strength valueStrength;
    long expireAfterWriteNanos = -1L;
    long expireAfterAccessNanos = -1L;
    RemovalCause nullRemovalCause;
    Equivalence<Object> keyEquivalence;
    Equivalence<Object> valueEquivalence;
    RemovalListener<? super K, ? super V> removalListener;
    Ticker ticker;

    public static CacheBuilder<Object, Object> newBuilder() {
        return new CacheBuilder<Object, Object>();
    }

    private boolean useNullCache() {
        return (this.nullRemovalCause == null);
    }

    CacheBuilder<K, V> keyEquivalence(Equivalence<Object> equivalence) {
        Preconditions.checkState((this.keyEquivalence == null), "key equivalence was already set to %s", new Object[]{this.keyEquivalence});
        this.keyEquivalence = (Equivalence<Object>) Preconditions.checkNotNull(equivalence);
        return this;
    }

    Equivalence<Object> getKeyEquivalence() {
        return (Equivalence<Object>) Objects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence());
    }

    CacheBuilder<K, V> valueEquivalence(Equivalence<Object> equivalence) {
        Preconditions.checkState((this.valueEquivalence == null), "value equivalence was already set to %s", new Object[]{this.valueEquivalence});

        this.valueEquivalence = (Equivalence<Object>) Preconditions.checkNotNull(equivalence);
        return this;
    }

    Equivalence<Object> getValueEquivalence() {
        return (Equivalence<Object>) Objects.firstNonNull(this.valueEquivalence, getValueStrength().defaultEquivalence());
    }

    public CacheBuilder<K, V> initialCapacity(int initialCapacity) {
        Preconditions.checkState((this.initialCapacity == -1), "initial capacity was already set to %s", new Object[]{Integer.valueOf(this.initialCapacity)});

        Preconditions.checkArgument((initialCapacity >= 0));
        this.initialCapacity = initialCapacity;
        return this;
    }

    int getInitialCapacity() {
        return (this.initialCapacity == -1) ? 16 : this.initialCapacity;
    }

    public CacheBuilder<K, V> concurrencyLevel(int concurrencyLevel) {
        Preconditions.checkState((this.concurrencyLevel == -1), "concurrency level was already set to %s", new Object[]{Integer.valueOf(this.concurrencyLevel)});

        Preconditions.checkArgument((concurrencyLevel > 0));
        this.concurrencyLevel = concurrencyLevel;
        return this;
    }

    int getConcurrencyLevel() {
        return (this.concurrencyLevel == -1) ? 4 : this.concurrencyLevel;
    }

    public CacheBuilder<K, V> maximumSize(int size) {
        Preconditions.checkState((this.maximumSize == -1), "maximum size was already set to %s", new Object[]{Integer.valueOf(this.maximumSize)});

        Preconditions.checkArgument((size >= 0), "maximum size must not be negative");
        this.maximumSize = size;
        if (this.maximumSize == 0) {
            this.nullRemovalCause = RemovalCause.SIZE;
        }
        return this;
    }

    CacheBuilder<K, V> strongKeys() {
        return setKeyStrength(CustomConcurrentHashMap.Strength.STRONG);
    }

    public CacheBuilder<K, V> weakKeys() {
        return setKeyStrength(CustomConcurrentHashMap.Strength.WEAK);
    }

    CustomConcurrentHashMap.Strength getKeyStrength() {
        return (CustomConcurrentHashMap.Strength) Objects.firstNonNull(this.keyStrength, CustomConcurrentHashMap.Strength.STRONG);
    }

    CacheBuilder<K, V> setKeyStrength(CustomConcurrentHashMap.Strength strength) {
        Preconditions.checkState((this.keyStrength == null), "Key strength was already set to %s", new Object[]{this.keyStrength});
        this.keyStrength = (CustomConcurrentHashMap.Strength) Preconditions.checkNotNull(strength);
        return this;
    }

    CacheBuilder<K, V> strongValues() {
        return setValueStrength(CustomConcurrentHashMap.Strength.STRONG);
    }

    public CacheBuilder<K, V> weakValues() {
        return setValueStrength(CustomConcurrentHashMap.Strength.WEAK);
    }

    public CacheBuilder<K, V> softValues() {
        return setValueStrength(CustomConcurrentHashMap.Strength.SOFT);
    }

    CustomConcurrentHashMap.Strength getValueStrength() {
        return (CustomConcurrentHashMap.Strength) Objects.firstNonNull(this.valueStrength, CustomConcurrentHashMap.Strength.STRONG);
    }

    CacheBuilder<K, V> setValueStrength(CustomConcurrentHashMap.Strength strength) {
        Preconditions.checkState((this.valueStrength == null), "Value strength was already set to %s", new Object[]{this.valueStrength});
        this.valueStrength = (CustomConcurrentHashMap.Strength) Preconditions.checkNotNull(strength);
        return this;
    }

    public CacheBuilder<K, V> expireAfterWrite(long duration, TimeUnit unit) {
        checkExpiration(duration, unit);
        this.expireAfterWriteNanos = unit.toNanos(duration);
        if (duration == 0L && this.nullRemovalCause == null) {
            this.nullRemovalCause = RemovalCause.EXPIRED;
        }
        return this;
    }

    private void checkExpiration(long duration, TimeUnit unit) {
        Preconditions.checkState((this.expireAfterWriteNanos == -1L), "expireAfterWrite was already set to %s ns", new Object[]{Long.valueOf(this.expireAfterWriteNanos)});

        Preconditions.checkState((this.expireAfterAccessNanos == -1L), "expireAfterAccess was already set to %s ns", new Object[]{Long.valueOf(this.expireAfterAccessNanos)});

        Preconditions.checkArgument((duration >= 0L), "duration cannot be negative: %s %s", new Object[]{Long.valueOf(duration), unit});
    }

    long getExpireAfterWriteNanos() {
        return (this.expireAfterWriteNanos == -1L) ? 0L : this.expireAfterWriteNanos;
    }

    public CacheBuilder<K, V> expireAfterAccess(long duration, TimeUnit unit) {
        checkExpiration(duration, unit);
        this.expireAfterAccessNanos = unit.toNanos(duration);
        if (duration == 0L && this.nullRemovalCause == null) {
            this.nullRemovalCause = RemovalCause.EXPIRED;
        }
        return this;
    }

    long getExpireAfterAccessNanos() {
        return (this.expireAfterAccessNanos == -1L) ? 0L : this.expireAfterAccessNanos;
    }

    public CacheBuilder<K, V> ticker(Ticker ticker) {
        Preconditions.checkState((this.ticker == null));
        this.ticker = (Ticker) Preconditions.checkNotNull(ticker);
        return this;
    }

    Ticker getTicker() {
        return (Ticker) Objects.firstNonNull(this.ticker, Ticker.systemTicker());
    }

    @CheckReturnValue
    public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> removalListener(RemovalListener<? super K1, ? super V1> listener) {
        Preconditions.checkState((this.removalListener == null));

        CacheBuilder<K1, V1> me = this;
        me.removalListener = (RemovalListener<? super K, ? super V>) Preconditions.checkNotNull(listener);
        return me;
    }

    <K1 extends K, V1 extends V> RemovalListener<K1, V1> getRemovalListener() {
        return (RemovalListener<K1, V1>) Objects.firstNonNull(this.removalListener, NullListener.INSTANCE);
    }

    public <K1 extends K, V1 extends V> Cache<K1, V1> build(CacheLoader<? super K1, V1> loader) {
        return useNullCache() ? new ComputingCache<K1, V1>(this, (Supplier) CACHE_STATS_COUNTER, loader) : new NullCache<K1, V1>(this, (Supplier) CACHE_STATS_COUNTER, loader);
    }

    public String toString() {
        Objects.ToStringHelper s = Objects.toStringHelper(this);
        if (this.initialCapacity != -1) {
            s.add("initialCapacity", Integer.valueOf(this.initialCapacity));
        }
        if (this.concurrencyLevel != -1) {
            s.add("concurrencyLevel", Integer.valueOf(this.concurrencyLevel));
        }
        if (this.maximumSize != -1) {
            s.add("maximumSize", Integer.valueOf(this.maximumSize));
        }
        if (this.expireAfterWriteNanos != -1L) {
            s.add("expireAfterWrite", this.expireAfterWriteNanos + "ns");
        }
        if (this.expireAfterAccessNanos != -1L) {
            s.add("expireAfterAccess", this.expireAfterAccessNanos + "ns");
        }
        if (this.keyStrength != null) {
            s.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
        }
        if (this.valueStrength != null) {
            s.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
        }
        if (this.keyEquivalence != null) {
            s.addValue("keyEquivalence");
        }
        if (this.valueEquivalence != null) {
            s.addValue("valueEquivalence");
        }
        if (this.removalListener != null) {
            s.addValue("removalListener");
        }
        return s.toString();
    }

    enum NullListener implements RemovalListener<Object, Object> {
        INSTANCE;

        public void onRemoval(RemovalNotification<Object, Object> notification) {
        }
    }

    static class NullConcurrentMap<K, V>
            extends AbstractMap<K, V>
            implements ConcurrentMap<K, V>, Serializable {
        private static final long serialVersionUID = 0L;
        private final RemovalListener<K, V> removalListener;
        private final RemovalCause removalCause;

        NullConcurrentMap(CacheBuilder<? super K, ? super V> builder) {
            this.removalListener = builder.getRemovalListener();
            this.removalCause = builder.nullRemovalCause;
        }

        public boolean containsKey(@Nullable Object key) {
            return false;
        }

        public boolean containsValue(@Nullable Object value) {
            return false;
        }

        public V get(@Nullable Object key) {
            return null;
        }

        void notifyRemoval(K key, V value) {
            RemovalNotification<K, V> notification = new RemovalNotification<K, V>(key, value, this.removalCause);

            this.removalListener.onRemoval(notification);
        }

        public V put(K key, V value) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(value);
            notifyRemoval(key, value);
            return null;
        }

        public V putIfAbsent(K key, V value) {
            return put(key, value);
        }

        public V remove(@Nullable Object key) {
            return null;
        }

        public boolean remove(@Nullable Object key, @Nullable Object value) {
            return false;
        }

        public V replace(K key, V value) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(value);
            return null;
        }

        public boolean replace(K key, @Nullable V oldValue, V newValue) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(newValue);
            return false;
        }

        public Set<Map.Entry<K, V>> entrySet() {
            return Collections.emptySet();
        }
    }

    static final class NullComputingConcurrentMap<K, V>
            extends NullConcurrentMap<K, V> {
        private static final long serialVersionUID = 0L;

        final CacheLoader<? super K, ? extends V> loader;

        NullComputingConcurrentMap(CacheBuilder<? super K, ? super V> builder, CacheLoader<? super K, ? extends V> loader) {
            super(builder);
            this.loader = (CacheLoader<? super K, ? extends V>) Preconditions.checkNotNull(loader);
        }

        public V get(Object k) {
            K key = (K) k;
            V value = compute(key);
            Preconditions.checkNotNull(value, this.loader + " returned null for key " + key + ".");
            notifyRemoval(key, value);
            return value;
        }

        private V compute(K key) {
            Preconditions.checkNotNull(key);
            try {
                return this.loader.load(key);
            } catch (Exception e) {
                throw new UncheckedExecutionException(e);
            } catch (Error e) {
                throw new ExecutionError(e);
            }
        }
    }

    static final class NullCache<K, V>
            extends AbstractCache<K, V> {
        final CacheBuilder.NullConcurrentMap<K, V> map;
        final CacheLoader<? super K, V> loader;
        final AbstractCache.StatsCounter statsCounter;
        ConcurrentMap<K, V> asMap;

        NullCache(CacheBuilder<? super K, ? super V> builder, Supplier<? extends AbstractCache.StatsCounter> statsCounterSupplier, CacheLoader<? super K, V> loader) {
            this.map = new CacheBuilder.NullConcurrentMap<K, V>(builder);
            this.statsCounter = (AbstractCache.StatsCounter) statsCounterSupplier.get();
            this.loader = (CacheLoader<? super K, V>) Preconditions.checkNotNull(loader);
        }

        public V get(K key) throws ExecutionException {
            V value = compute(key);
            this.map.notifyRemoval(key, value);
            return value;
        }

        private V compute(K key) throws ExecutionException {
            Preconditions.checkNotNull(key);
            long start = System.nanoTime();
            V value = null;
            try {
                value = this.loader.load(key);
            } catch (RuntimeException e) {
                throw new UncheckedExecutionException(e);
            } catch (Exception e) {
                throw new ExecutionException(e);
            } catch (Error e) {
                throw new ExecutionError(e);
            } finally {
                long elapsed = System.nanoTime() - start;
                if (value == null) {
                    this.statsCounter.recordLoadException(elapsed);
                } else {
                    this.statsCounter.recordLoadSuccess(elapsed);
                }
                this.statsCounter.recordEviction();
            }
            if (value == null) {
                throw new NullPointerException();
            }
            return value;
        }

        public long size() {
            return 0L;
        }

        public void invalidate(Object key) {
        }

        public void invalidateAll() {
        }

        public CacheStats stats() {
            return this.statsCounter.snapshot();
        }

        public ConcurrentMap<K, V> asMap() {
            ConcurrentMap<K, V> am = this.asMap;
            return (am != null) ? am : (this.asMap = (ConcurrentMap<K, V>) new CacheBuilder.CacheAsMap<K, V>(this.map));
        }
    }

    static final class CacheAsMap<K, V> extends ForwardingConcurrentMap<K, V> {
        private final ConcurrentMap<K, V> delegate;

        CacheAsMap(ConcurrentMap<K, V> delegate) {
            this.delegate = delegate;
        }

        protected ConcurrentMap<K, V> delegate() {
            return this.delegate;
        }

        public V put(K key, V value) {
            throw new UnsupportedOperationException();
        }

        public void putAll(Map<? extends K, ? extends V> map) {
            throw new UnsupportedOperationException();
        }

        public V putIfAbsent(K key, V value) {
            throw new UnsupportedOperationException();
        }

        public V replace(K key, V value) {
            throw new UnsupportedOperationException();
        }

        public boolean replace(K key, V oldValue, V newValue) {
            throw new UnsupportedOperationException();
        }
    }
}


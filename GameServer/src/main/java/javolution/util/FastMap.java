package javolution.util;

import javolution.lang.Immutable;
import javolution.lang.Parallelizable;
import javolution.lang.Realtime;
import javolution.text.TextContext;
import javolution.util.function.Consumer;
import javolution.util.function.Equalities;
import javolution.util.function.Equality;
import javolution.util.internal.map.*;
import javolution.util.service.CollectionService;
import javolution.util.service.MapService;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Realtime
public class FastMap<K, V>
        implements Map<K, V>, ConcurrentMap<K, V>, Serializable {
    private static final long serialVersionUID = 1536L;
    private final MapService<K, V> service;

    public FastMap() {
        this(Equalities.STANDARD);
    }

    public FastMap(Equality<? super K> keyEquality) {
        this(keyEquality, Equalities.STANDARD);
    }

    public FastMap(Equality<? super K> keyEquality, Equality<? super V> valueEquality) {
        this.service = (MapService<K, V>) new FastMapImpl(keyEquality, valueEquality);
    }

    protected FastMap(MapService<K, V> service) {
        this.service = service;
    }

    @Parallelizable(mutexFree = true, comment = "Except for write operations, all read operations are mutex-free.")
    public FastMap<K, V> atomic() {
        return new FastMap((MapService<K, V>) new AtomicMapImpl(this.service));
    }

    @Parallelizable(mutexFree = false, comment = "Use multiple-readers/single-writer lock.")
    public FastMap<K, V> shared() {
        return new FastMap((MapService<K, V>) new SharedMapImpl(this.service));
    }

    public FastMap<K, V> parallel() {
        return new FastMap((MapService<K, V>) new ParallelMapImpl(this.service));
    }

    public FastMap<K, V> sequential() {
        return new FastMap((MapService<K, V>) new SequentialMapImpl(this.service));
    }

    public FastMap<K, V> unmodifiable() {
        return new FastMap((MapService<K, V>) new UnmodifiableMapImpl(this.service));
    }

    public FastSet<K> keySet() {
        return new FastSet<K>(this.service.keySet());
    }

    public FastCollection<V> values() {
        return new FastCollection<V>() {
            private static final long serialVersionUID = 1536L;
            private final CollectionService<V> serviceValues = FastMap.this.service.values();

            protected CollectionService<V> service() {
                return this.serviceValues;
            }
        };
    }

    public FastSet<Map.Entry<K, V>> entrySet() {
        return new FastSet<Map.Entry<K, V>>(this.service.entrySet());
    }

    @Realtime(limit = Realtime.Limit.LINEAR)
    public void perform(Consumer<? extends Map<K, V>> action) {
        service().perform(action, service());
    }

    @Realtime(limit = Realtime.Limit.LINEAR)
    public void update(Consumer<? extends Map<K, V>> action) {
        service().update(action, service());
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public int size() {
        return this.service.size();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public boolean isEmpty() {
        return this.service.isEmpty();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public boolean containsKey(Object key) {
        return this.service.containsKey(key);
    }

    @Realtime(limit = Realtime.Limit.LINEAR)
    public boolean containsValue(Object value) {
        return this.service.containsValue(value);
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public V get(Object key) {
        return (V) this.service.get(key);
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public V put(K key, V value) {
        return (V) this.service.put(key, value);
    }

    @Realtime(limit = Realtime.Limit.LINEAR)
    public void putAll(Map<? extends K, ? extends V> map) {
        this.service.putAll(map);
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public V remove(Object key) {
        return (V) this.service.remove(key);
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public void clear() {
        this.service.clear();
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public V putIfAbsent(K key, V value) {
        return (V) this.service.putIfAbsent(key, value);
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public boolean remove(Object key, Object value) {
        return this.service.remove(key, value);
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public boolean replace(K key, V oldValue, V newValue) {
        return this.service.replace(key, oldValue, newValue);
    }

    @Realtime(limit = Realtime.Limit.CONSTANT)
    public V replace(K key, V value) {
        return (V) this.service.replace(key, value);
    }

    public FastMap<K, V> putAll(FastMap<? extends K, ? extends V> that) {
        putAll(that);
        return this;
    }

    public <T extends Map<K, V>> Immutable<T> toImmutable() {
        return new Immutable<T>() {
            final T value = (T) FastMap.this.unmodifiable();

            public T value() {
                return this.value;
            }
        };
    }

    @Realtime(limit = Realtime.Limit.LINEAR)
    public String toString() {
        return TextContext.getFormat(FastCollection.class).format(entrySet());
    }

    protected MapService<K, V> service() {
        return this.service;
    }
}


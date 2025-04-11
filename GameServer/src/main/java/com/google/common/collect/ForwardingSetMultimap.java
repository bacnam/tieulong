package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

@GwtCompatible
public abstract class ForwardingSetMultimap<K, V>
        extends ForwardingMultimap<K, V>
        implements SetMultimap<K, V> {
    public Set<Map.Entry<K, V>> entries() {
        return delegate().entries();
    }

    public Set<V> get(@Nullable K key) {
        return delegate().get(key);
    }

    public Set<V> removeAll(@Nullable Object key) {
        return delegate().removeAll(key);
    }

    public Set<V> replaceValues(K key, Iterable<? extends V> values) {
        return delegate().replaceValues(key, values);
    }

    protected abstract SetMultimap<K, V> delegate();
}


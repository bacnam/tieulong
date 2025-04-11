package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

import java.util.Map;

@GwtCompatible(serializable = true, emulated = true)
class RegularImmutableBiMap<K, V>
        extends ImmutableBiMap<K, V> {
    final transient ImmutableMap<K, V> delegate;
    final transient ImmutableBiMap<V, K> inverse;

    RegularImmutableBiMap(ImmutableMap<K, V> delegate) {
        this.delegate = delegate;

        ImmutableMap.Builder<V, K> builder = ImmutableMap.builder();
        for (Map.Entry<K, V> entry : delegate.entrySet()) {
            builder.put(entry.getValue(), entry.getKey());
        }
        ImmutableMap<V, K> backwardMap = builder.build();
        this.inverse = new RegularImmutableBiMap(backwardMap, this);
    }

    RegularImmutableBiMap(ImmutableMap<K, V> delegate, ImmutableBiMap<V, K> inverse) {
        this.delegate = delegate;
        this.inverse = inverse;
    }

    ImmutableMap<K, V> delegate() {
        return this.delegate;
    }

    public ImmutableBiMap<V, K> inverse() {
        return this.inverse;
    }

    boolean isPartialView() {
        return (this.delegate.isPartialView() || this.inverse.delegate().isPartialView());
    }
}


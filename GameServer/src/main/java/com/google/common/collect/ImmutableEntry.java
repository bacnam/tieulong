package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;
import java.io.Serializable;

@GwtCompatible(serializable = true)
class ImmutableEntry<K, V>
        extends AbstractMapEntry<K, V>
        implements Serializable {
    private static final long serialVersionUID = 0L;
    private final K key;
    private final V value;

    ImmutableEntry(@Nullable K key, @Nullable V value) {
        this.key = key;
        this.value = value;
    }

    @Nullable
    public K getKey() {
        return this.key;
    }

    @Nullable
    public V getValue() {
        return this.value;
    }

    public final V setValue(V value) {
        throw new UnsupportedOperationException();
    }
}


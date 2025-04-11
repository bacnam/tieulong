package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;
import java.util.Map;

@GwtCompatible(serializable = true, emulated = true)
final class EmptyImmutableMap
        extends ImmutableMap<Object, Object> {
    static final EmptyImmutableMap INSTANCE = new EmptyImmutableMap();

    private static final long serialVersionUID = 0L;

    public Object get(@Nullable Object key) {
        return null;
    }

    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return true;
    }

    public boolean containsKey(@Nullable Object key) {
        return false;
    }

    public boolean containsValue(@Nullable Object value) {
        return false;
    }

    public ImmutableSet<Map.Entry<Object, Object>> entrySet() {
        return ImmutableSet.of();
    }

    public ImmutableSet<Object> keySet() {
        return ImmutableSet.of();
    }

    public ImmutableCollection<Object> values() {
        return ImmutableCollection.EMPTY_IMMUTABLE_COLLECTION;
    }

    public boolean equals(@Nullable Object object) {
        if (object instanceof Map) {
            Map<?, ?> that = (Map<?, ?>) object;
            return that.isEmpty();
        }
        return false;
    }

    boolean isPartialView() {
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        return "{}";
    }

    Object readResolve() {
        return INSTANCE;
    }
}


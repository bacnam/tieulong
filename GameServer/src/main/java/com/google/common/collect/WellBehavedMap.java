package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

import java.util.Map;
import java.util.Set;

@GwtCompatible
final class WellBehavedMap<K, V>
        extends ForwardingMap<K, V> {
    private final Map<K, V> delegate;
    private Set<Map.Entry<K, V>> entrySet;

    private WellBehavedMap(Map<K, V> delegate) {
        this.delegate = delegate;
    }

    static <K, V> WellBehavedMap<K, V> wrap(Map<K, V> delegate) {
        return new WellBehavedMap<K, V>(delegate);
    }

    protected Map<K, V> delegate() {
        return this.delegate;
    }

    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> es = this.entrySet;
        if (es != null) {
            return es;
        }
        return this.entrySet = Sets.transform(this.delegate.keySet(), new KeyToEntryConverter<Object, V>(this));
    }

    private static class KeyToEntryConverter<K, V>
            extends Sets.InvertibleFunction<K, Map.Entry<K, V>> {
        final Map<K, V> map;

        KeyToEntryConverter(Map<K, V> map) {
            this.map = map;
        }

        public Map.Entry<K, V> apply(final K key) {
            return new AbstractMapEntry<K, V>() {
                public K getKey() {
                    return (K) key;
                }

                public V getValue() {
                    return (V) WellBehavedMap.KeyToEntryConverter.this.map.get(key);
                }

                public V setValue(V value) {
                    return WellBehavedMap.KeyToEntryConverter.this.map.put(key, value);
                }
            };
        }

        public K invert(Map.Entry<K, V> entry) {
            return entry.getKey();
        }
    }
}


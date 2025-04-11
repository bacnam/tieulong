package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Supplier;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@GwtCompatible
public abstract class ForwardingMap<K, V>
        extends ForwardingObject
        implements Map<K, V> {
    public int size() {
        return delegate().size();
    }

    public boolean isEmpty() {
        return delegate().isEmpty();
    }

    public V remove(Object object) {
        return delegate().remove(object);
    }

    public void clear() {
        delegate().clear();
    }

    public boolean containsKey(Object key) {
        return delegate().containsKey(key);
    }

    public boolean containsValue(Object value) {
        return delegate().containsValue(value);
    }

    public V get(Object key) {
        return delegate().get(key);
    }

    public V put(K key, V value) {
        return delegate().put(key, value);
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        delegate().putAll(map);
    }

    public Set<K> keySet() {
        return delegate().keySet();
    }

    public Collection<V> values() {
        return delegate().values();
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return delegate().entrySet();
    }

    public boolean equals(@Nullable Object object) {
        return (object == this || delegate().equals(object));
    }

    public int hashCode() {
        return delegate().hashCode();
    }

    @Beta
    protected void standardPutAll(Map<? extends K, ? extends V> map) {
        Maps.putAllImpl(this, map);
    }

    @Beta
    protected V standardRemove(@Nullable Object key) {
        Iterator<Map.Entry<K, V>> entryIterator = entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<K, V> entry = entryIterator.next();
            if (Objects.equal(entry.getKey(), key)) {
                V value = entry.getValue();
                entryIterator.remove();
                return value;
            }
        }
        return null;
    }

    @Beta
    protected void standardClear() {
        Iterator<Map.Entry<K, V>> entryIterator = entrySet().iterator();
        while (entryIterator.hasNext()) {
            entryIterator.next();
            entryIterator.remove();
        }
    }

    @Deprecated
    @Beta
    protected Set<K> standardKeySet() {
        return new StandardKeySet();
    }

    @Beta
    protected boolean standardContainsKey(@Nullable Object key) {
        return Maps.containsKeyImpl(this, key);
    }

    @Deprecated
    @Beta
    protected Collection<V> standardValues() {
        return new StandardValues();
    }

    @Beta
    protected boolean standardContainsValue(@Nullable Object value) {
        return Maps.containsValueImpl(this, value);
    }

    @Deprecated
    @Beta
    protected Set<Map.Entry<K, V>> standardEntrySet(final Supplier<Iterator<Map.Entry<K, V>>> entryIteratorSupplier) {
        return new StandardEntrySet() {
            public Iterator<Map.Entry<K, V>> iterator() {
                return (Iterator<Map.Entry<K, V>>) entryIteratorSupplier.get();
            }
        };
    }

    @Beta
    protected boolean standardIsEmpty() {
        return !entrySet().iterator().hasNext();
    }

    @Beta
    protected boolean standardEquals(@Nullable Object object) {
        return Maps.equalsImpl(this, object);
    }

    @Beta
    protected int standardHashCode() {
        return Sets.hashCodeImpl(entrySet());
    }

    @Beta
    protected String standardToString() {
        return Maps.toStringImpl(this);
    }

    protected abstract Map<K, V> delegate();

    @Beta
    protected class StandardKeySet
            extends Maps.KeySet<K, V> {
        Map<K, V> map() {
            return ForwardingMap.this;
        }
    }

    @Beta
    protected class StandardValues
            extends Maps.Values<K, V> {
        Map<K, V> map() {
            return ForwardingMap.this;
        }
    }

    @Beta
    protected abstract class StandardEntrySet
            extends Maps.EntrySet<K, V> {
        Map<K, V> map() {
            return ForwardingMap.this;
        }
    }
}


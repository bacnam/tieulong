package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;
import java.util.*;

@GwtCompatible
public abstract class ForwardingSortedMap<K, V>
        extends ForwardingMap<K, V>
        implements SortedMap<K, V> {
    public Comparator<? super K> comparator() {
        return delegate().comparator();
    }

    public K firstKey() {
        return delegate().firstKey();
    }

    public SortedMap<K, V> headMap(K toKey) {
        return delegate().headMap(toKey);
    }

    public K lastKey() {
        return delegate().lastKey();
    }

    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        return delegate().subMap(fromKey, toKey);
    }

    public SortedMap<K, V> tailMap(K fromKey) {
        return delegate().tailMap(fromKey);
    }

    private int unsafeCompare(Object k1, Object k2) {
        Comparator<? super K> comparator = comparator();
        if (comparator == null) {
            return ((Comparable<Object>) k1).compareTo(k2);
        }
        return comparator.compare((K) k1, (K) k2);
    }

    @Beta
    protected boolean standardContainsKey(@Nullable Object key) {
        try {
            ForwardingSortedMap<K, V> forwardingSortedMap = this;
            Object ceilingKey = forwardingSortedMap.tailMap((K) key).firstKey();
            return (unsafeCompare(ceilingKey, key) == 0);
        } catch (ClassCastException e) {
            return false;
        } catch (NoSuchElementException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Beta
    protected V standardRemove(@Nullable Object key) {
        try {
            ForwardingSortedMap<K, V> forwardingSortedMap = this;
            Iterator<Map.Entry<Object, V>> entryIterator = forwardingSortedMap.tailMap((K) key).entrySet().iterator();

            if (entryIterator.hasNext()) {
                Map.Entry<Object, V> ceilingEntry = entryIterator.next();
                if (unsafeCompare(ceilingEntry.getKey(), key) == 0) {
                    V value = ceilingEntry.getValue();
                    entryIterator.remove();
                    return value;
                }
            }
        } catch (ClassCastException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        }
        return null;
    }

    @Beta
    protected SortedMap<K, V> standardSubMap(K fromKey, K toKey) {
        return tailMap(fromKey).headMap(toKey);
    }

    protected abstract SortedMap<K, V> delegate();
}


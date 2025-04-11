package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

@GwtCompatible(serializable = true, emulated = true)
public class ImmutableSetMultimap<K, V>
        extends ImmutableMultimap<K, V>
        implements SetMultimap<K, V> {
    @GwtIncompatible("not needed in emulated source.")
    private static final long serialVersionUID = 0L;
    private final transient ImmutableSortedSet<V> emptySet;
    private transient ImmutableSet<Map.Entry<K, V>> entries;

    ImmutableSetMultimap(ImmutableMap<K, ImmutableSet<V>> map, int size, @Nullable Comparator<? super V> valueComparator) {
        super((ImmutableMap) map, size);
        this.emptySet = (valueComparator == null) ? null : ImmutableSortedSet.<V>emptySet(valueComparator);
    }

    public static <K, V> ImmutableSetMultimap<K, V> of() {
        return EmptyImmutableSetMultimap.INSTANCE;
    }

    public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1) {
        Builder<K, V> builder = builder();
        builder.put(k1, v1);
        return builder.build();
    }

    public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2) {
        Builder<K, V> builder = builder();
        builder.put(k1, v1);
        builder.put(k2, v2);
        return builder.build();
    }

    public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        Builder<K, V> builder = builder();
        builder.put(k1, v1);
        builder.put(k2, v2);
        builder.put(k3, v3);
        return builder.build();
    }

    public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        Builder<K, V> builder = builder();
        builder.put(k1, v1);
        builder.put(k2, v2);
        builder.put(k3, v3);
        builder.put(k4, v4);
        return builder.build();
    }

    public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        Builder<K, V> builder = builder();
        builder.put(k1, v1);
        builder.put(k2, v2);
        builder.put(k3, v3);
        builder.put(k4, v4);
        builder.put(k5, v5);
        return builder.build();
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder<K, V>();
    }

    public static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
        return copyOf(multimap, (Comparator<? super V>) null);
    }

    private static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap, Comparator<? super V> valueComparator) {
        Preconditions.checkNotNull(multimap);
        if (multimap.isEmpty() && valueComparator == null) {
            return of();
        }

        if (multimap instanceof ImmutableSetMultimap) {

            ImmutableSetMultimap<K, V> kvMultimap = (ImmutableSetMultimap) multimap;

            if (!kvMultimap.isPartialView()) {
                return kvMultimap;
            }
        }

        ImmutableMap.Builder<K, ImmutableSet<V>> builder = ImmutableMap.builder();
        int size = 0;

        for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>) multimap.asMap().entrySet()) {
            K key = entry.getKey();
            Collection<? extends V> values = entry.getValue();
            ImmutableSet<V> set = (valueComparator == null) ? ImmutableSet.<V>copyOf(values) : ImmutableSortedSet.<V>copyOf(valueComparator, values);

            if (!set.isEmpty()) {
                builder.put(key, set);
                size += set.size();
            }
        }

        return new ImmutableSetMultimap<K, V>(builder.build(), size, valueComparator);
    }

    public ImmutableSet<V> get(@Nullable K key) {
        ImmutableSet<V> set = (ImmutableSet<V>) this.map.get(key);
        if (set != null)
            return set;
        if (this.emptySet != null) {
            return this.emptySet;
        }
        return ImmutableSet.of();
    }

    public ImmutableSet<V> removeAll(Object key) {
        throw new UnsupportedOperationException();
    }

    public ImmutableSet<V> replaceValues(K key, Iterable<? extends V> values) {
        throw new UnsupportedOperationException();
    }

    public ImmutableSet<Map.Entry<K, V>> entries() {
        ImmutableSet<Map.Entry<K, V>> result = this.entries;
        return (result == null) ? (this.entries = ImmutableSet.copyOf(super.entries())) : result;
    }

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        Serialization.writeMultimap(this, stream);
    }

    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        ImmutableMap<Object, ImmutableSet<Object>> tmpMap;
        stream.defaultReadObject();
        int keyCount = stream.readInt();
        if (keyCount < 0) {
            throw new InvalidObjectException("Invalid key count " + keyCount);
        }
        ImmutableMap.Builder<Object, ImmutableSet<Object>> builder = ImmutableMap.builder();

        int tmpSize = 0;

        for (int i = 0; i < keyCount; i++) {
            Object key = stream.readObject();
            int valueCount = stream.readInt();
            if (valueCount <= 0) {
                throw new InvalidObjectException("Invalid value count " + valueCount);
            }

            Object[] array = new Object[valueCount];
            for (int j = 0; j < valueCount; j++) {
                array[j] = stream.readObject();
            }
            ImmutableSet<Object> valueSet = ImmutableSet.copyOf(array);
            if (valueSet.size() != array.length) {
                throw new InvalidObjectException("Duplicate key-value pairs exist for key " + key);
            }

            builder.put(key, valueSet);
            tmpSize += valueCount;
        }

        try {
            tmpMap = builder.build();
        } catch (IllegalArgumentException e) {
            throw (InvalidObjectException) (new InvalidObjectException(e.getMessage())).initCause(e);
        }

        ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, tmpMap);
        ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, tmpSize);
    }

    private static class BuilderMultimap<K, V>
            extends AbstractMultimap<K, V> {
        private static final long serialVersionUID = 0L;

        BuilderMultimap() {
            super(new LinkedHashMap<K, Collection<V>>());
        }

        Collection<V> createCollection() {
            return Sets.newLinkedHashSet();
        }
    }

    private static class SortedKeyBuilderMultimap<K, V>
            extends AbstractMultimap<K, V> {
        private static final long serialVersionUID = 0L;

        SortedKeyBuilderMultimap(Comparator<? super K> keyComparator, Multimap<K, V> multimap) {
            super(new TreeMap<K, Collection<V>>(keyComparator));
            putAll(multimap);
        }

        Collection<V> createCollection() {
            return Sets.newLinkedHashSet();
        }
    }

    public static final class Builder<K, V>
            extends ImmutableMultimap.Builder<K, V> {
        public Builder<K, V> put(K key, V value) {
            this.builderMultimap.put((K) Preconditions.checkNotNull(key), (V) Preconditions.checkNotNull(value));
            return this;
        }

        public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
            Collection<V> collection = this.builderMultimap.get((K) Preconditions.checkNotNull(key));
            for (V value : values) {
                collection.add((V) Preconditions.checkNotNull(value));
            }
            return this;
        }

        public Builder<K, V> putAll(K key, V... values) {
            return putAll(key, Arrays.asList(values));
        }

        public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
            for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>) multimap.asMap().entrySet()) {
                putAll(entry.getKey(), entry.getValue());
            }
            return this;
        }

        @Beta
        public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
            this.builderMultimap = new ImmutableSetMultimap.SortedKeyBuilderMultimap<K, V>((Comparator<? super K>) Preconditions.checkNotNull(keyComparator), this.builderMultimap);

            return this;
        }

        @Beta
        public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
            super.orderValuesBy(valueComparator);
            return this;
        }

        public ImmutableSetMultimap<K, V> build() {
            return ImmutableSetMultimap.copyOf(this.builderMultimap, this.valueComparator);
        }
    }
}


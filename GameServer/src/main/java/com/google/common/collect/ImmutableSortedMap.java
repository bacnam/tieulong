package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

@GwtCompatible(serializable = true, emulated = true)
public class ImmutableSortedMap<K, V>
        extends ImmutableSortedMapFauxverideShim<K, V>
        implements SortedMap<K, V> {
    private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();

    private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP = new ImmutableSortedMap(ImmutableList.of(), (Comparator) NATURAL_ORDER);
    private static final long serialVersionUID = 0L;
    final transient ImmutableList<Map.Entry<K, V>> entries;
    private final transient Comparator<? super K> comparator;
    private transient ImmutableSet<Map.Entry<K, V>> entrySet;
    private transient ImmutableSortedSet<K> keySet;
    private transient ImmutableCollection<V> values;

    ImmutableSortedMap(ImmutableList<Map.Entry<K, V>> entries, Comparator<? super K> comparator) {
        this.entries = entries;
        this.comparator = comparator;
    }

    public static <K, V> ImmutableSortedMap<K, V> of() {
        return (ImmutableSortedMap) NATURAL_EMPTY_MAP;
    }

    private static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
        if (NATURAL_ORDER.equals(comparator)) {
            return (ImmutableSortedMap) NATURAL_EMPTY_MAP;
        }
        return new ImmutableSortedMap<K, V>(ImmutableList.of(), comparator);
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1) {
        return new ImmutableSortedMap<K, V>(ImmutableList.of(entryOf(k1, v1)), Ordering.natural());
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2) {
        return (new Builder<K, V>(Ordering.natural())).put(k1, v1).put(k2, v2).build();
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return (new Builder<K, V>(Ordering.natural())).put(k1, v1).put(k2, v2).put(k3, v3).build();
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return (new Builder<K, V>(Ordering.natural())).put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).build();
    }

    public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return (new Builder<K, V>(Ordering.natural())).put(k1, v1).put(k2, v2).put(k3, v3).put(k4, v4).put(k5, v5).build();
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        Ordering<K> naturalOrder = Ordering.natural();
        return copyOfInternal(map, naturalOrder);
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
        return copyOfInternal(map, (Comparator<? super K>) Preconditions.checkNotNull(comparator));
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(SortedMap<K, ? extends V> map) {
        Comparator<Comparable> comparator1;
        Comparator<? super K> comparator = map.comparator();
        if (comparator == null) {

            comparator1 = NATURAL_ORDER;
        }
        return copyOfInternal(map, (Comparator) comparator1);
    }

    private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
        boolean sameComparator = false;
        if (map instanceof SortedMap) {
            SortedMap<?, ?> sortedMap = (SortedMap<?, ?>) map;
            Comparator<?> comparator2 = sortedMap.comparator();
            sameComparator = (comparator2 == null) ? ((comparator == NATURAL_ORDER)) : comparator.equals(comparator2);
        }

        if (sameComparator && map instanceof ImmutableSortedMap) {

            ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap) map;
            if (!kvMap.isPartialView()) {
                return kvMap;
            }
        }

        Map.Entry[] arrayOfEntry = (Map.Entry[]) map.entrySet().toArray((Object[]) new Map.Entry[0]);

        for (int i = 0; i < arrayOfEntry.length; i++) {
            Map.Entry<K, V> entry = arrayOfEntry[i];
            arrayOfEntry[i] = entryOf(entry.getKey(), entry.getValue());
        }

        List<Map.Entry<K, V>> list = Arrays.asList((Map.Entry<K, V>[]) arrayOfEntry);

        if (!sameComparator) {
            sortEntries(list, comparator);
            validateEntries(list, comparator);
        }

        return new ImmutableSortedMap<K, V>(ImmutableList.copyOf(list), comparator);
    }

    private static <K, V> void sortEntries(List<Map.Entry<K, V>> entries, final Comparator<? super K> comparator) {
        Comparator<Map.Entry<K, V>> entryComparator = new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> entry1, Map.Entry<K, V> entry2) {
                return comparator.compare(entry1.getKey(), entry2.getKey());
            }
        };

        Collections.sort(entries, entryComparator);
    }

    private static <K, V> void validateEntries(List<Map.Entry<K, V>> entries, Comparator<? super K> comparator) {
        for (int i = 1; i < entries.size(); i++) {
            if (comparator.compare((K) ((Map.Entry) entries.get(i - 1)).getKey(), (K) ((Map.Entry) entries.get(i)).getKey()) == 0) {
                throw new IllegalArgumentException("Duplicate keys in mappings " + entries.get(i - 1) + " and " + entries.get(i));
            }
        }
    }

    public static <K extends Comparable<K>, V> Builder<K, V> naturalOrder() {
        return new Builder<K, V>(Ordering.natural());
    }

    public static <K, V> Builder<K, V> orderedBy(Comparator<K> comparator) {
        return new Builder<K, V>(comparator);
    }

    public static <K extends Comparable<K>, V> Builder<K, V> reverseOrder() {
        return new Builder<K, V>(Ordering.<Comparable>natural().reverse());
    }

    public int size() {
        return this.entries.size();
    }

    Comparator<Object> unsafeComparator() {
        return (Comparator) this.comparator;
    }

    public V get(@Nullable Object key) {
        int i;
        if (key == null) {
            return null;
        }

        try {
            i = index(key, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.INVERTED_INSERTION_INDEX);
        } catch (ClassCastException e) {
            return null;
        }
        return (i >= 0) ? (V) ((Map.Entry) this.entries.get(i)).getValue() : null;
    }

    public boolean containsValue(@Nullable Object value) {
        if (value == null) {
            return false;
        }
        return Iterators.contains(valueIterator(), value);
    }

    boolean isPartialView() {
        return this.entries.isPartialView();
    }

    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        ImmutableSet<Map.Entry<K, V>> es = this.entrySet;
        return (es == null) ? (this.entrySet = createEntrySet()) : es;
    }

    private ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return isEmpty() ? ImmutableSet.<Map.Entry<K, V>>of() : new EntrySet<K, V>(this);
    }

    public ImmutableSortedSet<K> keySet() {
        ImmutableSortedSet<K> ks = this.keySet;
        return (ks == null) ? (this.keySet = createKeySet()) : ks;
    }

    private ImmutableSortedSet<K> createKeySet() {
        if (isEmpty()) {
            return ImmutableSortedSet.emptySet(this.comparator);
        }

        return new RegularImmutableSortedSet<K>(new TransformedImmutableList<Map.Entry<K, V>, K>(this.entries) {
            K transform(Map.Entry<K, V> entry) {
                return entry.getKey();
            }
        }, this.comparator);
    }

    public ImmutableCollection<V> values() {
        ImmutableCollection<V> v = this.values;
        return (v == null) ? (this.values = new Values<V>(this)) : v;
    }

    UnmodifiableIterator<V> valueIterator() {
        final UnmodifiableIterator<Map.Entry<K, V>> entryIterator = this.entries.iterator();
        return new UnmodifiableIterator<V>() {
            public boolean hasNext() {
                return entryIterator.hasNext();
            }

            public V next() {
                return (V) ((Map.Entry) entryIterator.next()).getValue();
            }
        };
    }

    public Comparator<? super K> comparator() {
        return this.comparator;
    }

    public K firstKey() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return (K) ((Map.Entry) this.entries.get(0)).getKey();
    }

    public K lastKey() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return (K) ((Map.Entry) this.entries.get(size() - 1)).getKey();
    }

    public ImmutableSortedMap<K, V> headMap(K toKey) {
        return headMap(toKey, false);
    }

    ImmutableSortedMap<K, V> headMap(K toKey, boolean inclusive) {
        int index;
        if (inclusive) {
            index = index(toKey, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER) + 1;
        } else {
            index = index(toKey, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
        }
        return createSubmap(0, index);
    }

    public ImmutableSortedMap<K, V> subMap(K fromKey, K toKey) {
        return subMap(fromKey, true, toKey, false);
    }

    ImmutableSortedMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        Preconditions.checkNotNull(fromKey);
        Preconditions.checkNotNull(toKey);
        Preconditions.checkArgument((this.comparator.compare(fromKey, toKey) <= 0));
        return tailMap(fromKey, fromInclusive).headMap(toKey, toInclusive);
    }

    public ImmutableSortedMap<K, V> tailMap(K fromKey) {
        return tailMap(fromKey, true);
    }

    ImmutableSortedMap<K, V> tailMap(K fromKey, boolean inclusive) {
        int index;
        if (inclusive) {
            index = index(fromKey, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
        } else {
            index = index(fromKey, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER) + 1;
        }
        return createSubmap(index, size());
    }

    private ImmutableList<K> keyList() {
        return new TransformedImmutableList<Map.Entry<K, V>, K>(this.entries) {
            K transform(Map.Entry<K, V> entry) {
                return entry.getKey();
            }
        };
    }

    private int index(Object key, SortedLists.KeyPresentBehavior presentBehavior, SortedLists.KeyAbsentBehavior absentBehavior) {
        return SortedLists.binarySearch(keyList(), (K) Preconditions.checkNotNull(key), (Comparator) unsafeComparator(), presentBehavior, absentBehavior);
    }

    private ImmutableSortedMap<K, V> createSubmap(int newFromIndex, int newToIndex) {
        if (newFromIndex < newToIndex) {
            return new ImmutableSortedMap(this.entries.subList(newFromIndex, newToIndex), this.comparator);
        }

        return emptyMap(this.comparator);
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }

    public static class Builder<K, V>
            extends ImmutableMap.Builder<K, V> {
        private final Comparator<? super K> comparator;

        public Builder(Comparator<? super K> comparator) {
            this.comparator = (Comparator<? super K>) Preconditions.checkNotNull(comparator);
        }

        public Builder<K, V> put(K key, V value) {
            this.entries.add(ImmutableMap.entryOf(key, value));
            return this;
        }

        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public ImmutableSortedMap<K, V> build() {
            ImmutableSortedMap.sortEntries(this.entries, this.comparator);
            ImmutableSortedMap.validateEntries(this.entries, this.comparator);
            return new ImmutableSortedMap<K, V>(ImmutableList.copyOf(this.entries), this.comparator);
        }
    }

    private static class EntrySet<K, V>
            extends ImmutableSet<Map.Entry<K, V>> {
        final transient ImmutableSortedMap<K, V> map;

        EntrySet(ImmutableSortedMap<K, V> map) {
            this.map = map;
        }

        boolean isPartialView() {
            return this.map.isPartialView();
        }

        public int size() {
            return this.map.size();
        }

        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return this.map.entries.iterator();
        }

        public boolean contains(Object target) {
            if (target instanceof Map.Entry) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) target;
                V mappedValue = this.map.get(entry.getKey());
                return (mappedValue != null && mappedValue.equals(entry.getValue()));
            }
            return false;
        }

        Object writeReplace() {
            return new ImmutableSortedMap.EntrySetSerializedForm<K, V>(this.map);
        }
    }

    private static class EntrySetSerializedForm<K, V> implements Serializable {
        private static final long serialVersionUID = 0L;
        final ImmutableSortedMap<K, V> map;

        EntrySetSerializedForm(ImmutableSortedMap<K, V> map) {
            this.map = map;
        }

        Object readResolve() {
            return this.map.entrySet();
        }
    }

    private static class Values<V>
            extends ImmutableCollection<V> {
        private final ImmutableSortedMap<?, V> map;

        Values(ImmutableSortedMap<?, V> map) {
            this.map = map;
        }

        public int size() {
            return this.map.size();
        }

        public UnmodifiableIterator<V> iterator() {
            return this.map.valueIterator();
        }

        public boolean contains(Object target) {
            return this.map.containsValue(target);
        }

        boolean isPartialView() {
            return true;
        }

        Object writeReplace() {
            return new ImmutableSortedMap.ValuesSerializedForm<V>(this.map);
        }
    }

    private static class ValuesSerializedForm<V> implements Serializable {
        private static final long serialVersionUID = 0L;
        final ImmutableSortedMap<?, V> map;

        ValuesSerializedForm(ImmutableSortedMap<?, V> map) {
            this.map = map;
        }

        Object readResolve() {
            return this.map.values();
        }
    }

    private static class SerializedForm
            extends ImmutableMap.SerializedForm {
        private static final long serialVersionUID = 0L;
        private final Comparator<Object> comparator;

        SerializedForm(ImmutableSortedMap<?, ?> sortedMap) {
            super(sortedMap);
            this.comparator = (Comparator) sortedMap.comparator();
        }

        Object readResolve() {
            ImmutableSortedMap.Builder<Object, Object> builder = new ImmutableSortedMap.Builder<Object, Object>(this.comparator);
            return createMap(builder);
        }
    }
}


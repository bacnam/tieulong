package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.*;
import com.google.common.base.Objects;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

@GwtCompatible(emulated = true)
public final class Maps {
    static final Joiner.MapJoiner STANDARD_JOINER = Collections2.STANDARD_JOINER.withKeyValueSeparator("=");

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }

    public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) {
        return new HashMap<K, V>(capacity(expectedSize));
    }

    static int capacity(int expectedSize) {
        if (expectedSize < 3) {
            Preconditions.checkArgument((expectedSize >= 0));
            return expectedSize + 1;
        }
        if (expectedSize < 1073741824) {
            return expectedSize + expectedSize / 3;
        }
        return Integer.MAX_VALUE;
    }

    public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> map) {
        return new HashMap<K, V>(map);
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap<K, V>();
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> map) {
        return new LinkedHashMap<K, V>(map);
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
        return (new MapMaker()).makeMap();
    }

    public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() {
        return new TreeMap<K, V>();
    }

    public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> map) {
        return new TreeMap<K, V>(map);
    }

    public static <C, K extends C, V> TreeMap<K, V> newTreeMap(@Nullable Comparator<C> comparator) {
        return new TreeMap<K, V>(comparator);
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> type) {
        return new EnumMap<K, V>((Class<K>) Preconditions.checkNotNull(type));
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Map<K, ? extends V> map) {
        return new EnumMap<K, V>(map);
    }

    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
        return new IdentityHashMap<K, V>();
    }

    public static <K, V> BiMap<K, V> synchronizedBiMap(BiMap<K, V> bimap) {
        return Synchronized.biMap(bimap, null);
    }

    public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right) {
        return difference(left, right, Equivalences.equals());
    }

    @Beta
    public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right, Equivalence<? super V> valueEquivalence) {
        Preconditions.checkNotNull(valueEquivalence);

        Map<K, V> onlyOnLeft = newHashMap();
        Map<K, V> onlyOnRight = new HashMap<K, V>(right);
        Map<K, V> onBoth = newHashMap();
        Map<K, MapDifference.ValueDifference<V>> differences = newHashMap();
        boolean eq = true;

        for (Map.Entry<? extends K, ? extends V> entry : left.entrySet()) {
            K leftKey = entry.getKey();
            V leftValue = entry.getValue();
            if (right.containsKey(leftKey)) {
                V rightValue = onlyOnRight.remove(leftKey);
                if (valueEquivalence.equivalent(leftValue, rightValue)) {
                    onBoth.put(leftKey, leftValue);
                    continue;
                }
                eq = false;
                differences.put(leftKey, ValueDifferenceImpl.create(leftValue, rightValue));

                continue;
            }
            eq = false;
            onlyOnLeft.put(leftKey, leftValue);
        }

        boolean areEqual = (eq && onlyOnRight.isEmpty());
        return mapDifference(areEqual, onlyOnLeft, onlyOnRight, onBoth, differences);
    }

    private static <K, V> MapDifference<K, V> mapDifference(boolean areEqual, Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth, Map<K, MapDifference.ValueDifference<V>> differences) {
        return new MapDifferenceImpl<K, V>(areEqual, Collections.unmodifiableMap(onlyOnLeft), Collections.unmodifiableMap(onlyOnRight), Collections.unmodifiableMap(onBoth), Collections.unmodifiableMap(differences));
    }

    public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterable<V> values, Function<? super V, K> keyFunction) {
        return uniqueIndex(values.iterator(), keyFunction);
    }

    @Deprecated
    @Beta
    public static <K, V, I extends Iterable<V> & Iterator<V>> ImmutableMap<K, V> uniqueIndex(I values, Function<? super V, K> keyFunction) {
        Iterable<V> valuesIterable = (Iterable<V>) Preconditions.checkNotNull(values);
        return uniqueIndex(valuesIterable, keyFunction);
    }

    public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterator<V> values, Function<? super V, K> keyFunction) {
        Preconditions.checkNotNull(keyFunction);
        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
        while (values.hasNext()) {
            V value = values.next();
            builder.put((K) keyFunction.apply(value), value);
        }
        return builder.build();
    }

    @GwtIncompatible("java.util.Properties")
    public static ImmutableMap<String, String> fromProperties(Properties properties) {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

        for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            builder.put(key, properties.getProperty(key));
        }

        return builder.build();
    }

    @GwtCompatible(serializable = true)
    public static <K, V> Map.Entry<K, V> immutableEntry(@Nullable K key, @Nullable V value) {
        return new ImmutableEntry<K, V>(key, value);
    }

    static <K, V> Set<Map.Entry<K, V>> unmodifiableEntrySet(Set<Map.Entry<K, V>> entrySet) {
        return new UnmodifiableEntrySet<K, V>(Collections.unmodifiableSet(entrySet));
    }

    static <K, V> Map.Entry<K, V> unmodifiableEntry(final Map.Entry<K, V> entry) {
        Preconditions.checkNotNull(entry);
        return new AbstractMapEntry<K, V>() {
            public K getKey() {
                return (K) entry.getKey();
            }

            public V getValue() {
                return (V) entry.getValue();
            }
        };
    }

    public static <K, V> BiMap<K, V> unmodifiableBiMap(BiMap<? extends K, ? extends V> bimap) {
        return new UnmodifiableBiMap<K, V>(bimap, null);
    }

    public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> fromMap, final Function<? super V1, V2> function) {
        Preconditions.checkNotNull(function);
        EntryTransformer<K, V1, V2> transformer = new EntryTransformer<K, V1, V2>() {
            public V2 transformEntry(K key, V1 value) {
                return (V2) function.apply(value);
            }
        };
        return transformEntries(fromMap, transformer);
    }

    public static <K, V1, V2> Map<K, V2> transformEntries(Map<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
        return new TransformedEntriesMap<K, V1, V2>(fromMap, transformer);
    }

    public static <K, V> Map<K, V> filterKeys(Map<K, V> unfiltered, final Predicate<? super K> keyPredicate) {
        Preconditions.checkNotNull(keyPredicate);
        Predicate<Map.Entry<K, V>> entryPredicate = new Predicate<Map.Entry<K, V>>() {
            public boolean apply(Map.Entry<K, V> input) {
                return keyPredicate.apply(input.getKey());
            }
        };
        return (unfiltered instanceof AbstractFilteredMap) ? filterFiltered((AbstractFilteredMap<K, V>) unfiltered, entryPredicate) : new FilteredKeyMap<K, V>((Map<K, V>) Preconditions.checkNotNull(unfiltered), keyPredicate, entryPredicate);
    }

    public static <K, V> Map<K, V> filterValues(Map<K, V> unfiltered, final Predicate<? super V> valuePredicate) {
        Preconditions.checkNotNull(valuePredicate);
        Predicate<Map.Entry<K, V>> entryPredicate = new Predicate<Map.Entry<K, V>>() {
            public boolean apply(Map.Entry<K, V> input) {
                return valuePredicate.apply(input.getValue());
            }
        };
        return filterEntries(unfiltered, entryPredicate);
    }

    public static <K, V> Map<K, V> filterEntries(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
        Preconditions.checkNotNull(entryPredicate);
        return (unfiltered instanceof AbstractFilteredMap) ? filterFiltered((AbstractFilteredMap<K, V>) unfiltered, entryPredicate) : new FilteredEntryMap<K, V>((Map<K, V>) Preconditions.checkNotNull(unfiltered), entryPredicate);
    }

    private static <K, V> Map<K, V> filterFiltered(AbstractFilteredMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
        Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.predicate, entryPredicate);

        return new FilteredEntryMap<K, V>(map.unfiltered, predicate);
    }

    static <V> V safeGet(Map<?, V> map, Object key) {
        try {
            return map.get(key);
        } catch (ClassCastException e) {
            return null;
        }
    }

    static boolean safeContainsKey(Map<?, ?> map, Object key) {
        try {
            return map.containsKey(key);
        } catch (ClassCastException e) {
            return false;
        }
    }

    static <K, V> boolean containsEntryImpl(Collection<Map.Entry<K, V>> c, Object o) {
        if (!(o instanceof Map.Entry)) {
            return false;
        }
        return c.contains(unmodifiableEntry((Map.Entry<?, ?>) o));
    }

    static <K, V> boolean removeEntryImpl(Collection<Map.Entry<K, V>> c, Object o) {
        if (!(o instanceof Map.Entry)) {
            return false;
        }
        return c.remove(unmodifiableEntry((Map.Entry<?, ?>) o));
    }

    static boolean equalsImpl(Map<?, ?> map, Object object) {
        if (map == object) {
            return true;
        }
        if (object instanceof Map) {
            Map<?, ?> o = (Map<?, ?>) object;
            return map.entrySet().equals(o.entrySet());
        }
        return false;
    }

    static int hashCodeImpl(Map<?, ?> map) {
        return Sets.hashCodeImpl(map.entrySet());
    }

    static String toStringImpl(Map<?, ?> map) {
        StringBuilder sb = Collections2.newStringBuilderForCollection(map.size()).append('{');

        STANDARD_JOINER.appendTo(sb, map);
        return sb.append('}').toString();
    }

    static <K, V> void putAllImpl(Map<K, V> self, Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            self.put(entry.getKey(), entry.getValue());
        }
    }

    static boolean containsKeyImpl(Map<?, ?> map, @Nullable Object key) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (Objects.equal(entry.getKey(), key)) {
                return true;
            }
        }
        return false;
    }

    static boolean containsValueImpl(Map<?, ?> map, @Nullable Object value) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (Objects.equal(entry.getValue(), value)) {
                return true;
            }
        }
        return false;
    }

    public static interface EntryTransformer<K, V1, V2> {
        V2 transformEntry(@Nullable K param1K, @Nullable V1 param1V1);
    }

    static class MapDifferenceImpl<K, V>
            implements MapDifference<K, V> {
        final boolean areEqual;

        final Map<K, V> onlyOnLeft;

        final Map<K, V> onlyOnRight;

        final Map<K, V> onBoth;
        final Map<K, MapDifference.ValueDifference<V>> differences;

        MapDifferenceImpl(boolean areEqual, Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth, Map<K, MapDifference.ValueDifference<V>> differences) {
            this.areEqual = areEqual;
            this.onlyOnLeft = onlyOnLeft;
            this.onlyOnRight = onlyOnRight;
            this.onBoth = onBoth;
            this.differences = differences;
        }

        public boolean areEqual() {
            return this.areEqual;
        }

        public Map<K, V> entriesOnlyOnLeft() {
            return this.onlyOnLeft;
        }

        public Map<K, V> entriesOnlyOnRight() {
            return this.onlyOnRight;
        }

        public Map<K, V> entriesInCommon() {
            return this.onBoth;
        }

        public Map<K, MapDifference.ValueDifference<V>> entriesDiffering() {
            return this.differences;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object instanceof MapDifference) {
                MapDifference<?, ?> other = (MapDifference<?, ?>) object;
                return (entriesOnlyOnLeft().equals(other.entriesOnlyOnLeft()) && entriesOnlyOnRight().equals(other.entriesOnlyOnRight()) && entriesInCommon().equals(other.entriesInCommon()) && entriesDiffering().equals(other.entriesDiffering()));
            }

            return false;
        }

        public int hashCode() {
            return Objects.hashCode(new Object[]{entriesOnlyOnLeft(), entriesOnlyOnRight(), entriesInCommon(), entriesDiffering()});
        }

        public String toString() {
            if (this.areEqual) {
                return "equal";
            }

            StringBuilder result = new StringBuilder("not equal");
            if (!this.onlyOnLeft.isEmpty()) {
                result.append(": only on left=").append(this.onlyOnLeft);
            }
            if (!this.onlyOnRight.isEmpty()) {
                result.append(": only on right=").append(this.onlyOnRight);
            }
            if (!this.differences.isEmpty()) {
                result.append(": value differences=").append(this.differences);
            }
            return result.toString();
        }
    }

    static class ValueDifferenceImpl<V>
            implements MapDifference.ValueDifference<V> {
        private final V left;
        private final V right;

        private ValueDifferenceImpl(@Nullable V left, @Nullable V right) {
            this.left = left;
            this.right = right;
        }

        static <V> MapDifference.ValueDifference<V> create(@Nullable V left, @Nullable V right) {
            return new ValueDifferenceImpl<V>(left, right);
        }

        public V leftValue() {
            return this.left;
        }

        public V rightValue() {
            return this.right;
        }

        public boolean equals(@Nullable Object object) {
            if (object instanceof MapDifference.ValueDifference) {
                MapDifference.ValueDifference<?> that = (MapDifference.ValueDifference) object;

                return (Objects.equal(this.left, that.leftValue()) && Objects.equal(this.right, that.rightValue()));
            }

            return false;
        }

        public int hashCode() {
            return Objects.hashCode(new Object[]{this.left, this.right});
        }

        public String toString() {
            return "(" + this.left + ", " + this.right + ")";
        }
    }

    static class UnmodifiableEntries<K, V>
            extends ForwardingCollection<Map.Entry<K, V>> {
        private final Collection<Map.Entry<K, V>> entries;

        UnmodifiableEntries(Collection<Map.Entry<K, V>> entries) {
            this.entries = entries;
        }

        protected Collection<Map.Entry<K, V>> delegate() {
            return this.entries;
        }

        public Iterator<Map.Entry<K, V>> iterator() {
            final Iterator<Map.Entry<K, V>> delegate = super.iterator();
            return new ForwardingIterator<Map.Entry<K, V>>() {
                public Map.Entry<K, V> next() {
                    return Maps.unmodifiableEntry(super.next());
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }

                protected Iterator<Map.Entry<K, V>> delegate() {
                    return delegate;
                }
            };
        }

        public boolean add(Map.Entry<K, V> element) {
            throw new UnsupportedOperationException();
        }

        public boolean addAll(Collection<? extends Map.Entry<K, V>> collection) {
            throw new UnsupportedOperationException();
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object object) {
            throw new UnsupportedOperationException();
        }

        public boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] array) {
            return (T[]) standardToArray((Object[]) array);
        }
    }

    static class UnmodifiableEntrySet<K, V>
            extends UnmodifiableEntries<K, V>
            implements Set<Map.Entry<K, V>> {
        UnmodifiableEntrySet(Set<Map.Entry<K, V>> entries) {
            super(entries);
        }

        public boolean equals(@Nullable Object object) {
            return Sets.equalsImpl(this, object);
        }

        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }

    private static class UnmodifiableBiMap<K, V>
            extends ForwardingMap<K, V>
            implements BiMap<K, V>, Serializable {
        private static final long serialVersionUID = 0L;
        final Map<K, V> unmodifiableMap;
        final BiMap<? extends K, ? extends V> delegate;
        transient BiMap<V, K> inverse;
        transient Set<V> values;

        UnmodifiableBiMap(BiMap<? extends K, ? extends V> delegate, @Nullable BiMap<V, K> inverse) {
            this.unmodifiableMap = Collections.unmodifiableMap(delegate);
            this.delegate = delegate;
            this.inverse = inverse;
        }

        protected Map<K, V> delegate() {
            return this.unmodifiableMap;
        }

        public V forcePut(K key, V value) {
            throw new UnsupportedOperationException();
        }

        public BiMap<V, K> inverse() {
            BiMap<V, K> result = this.inverse;
            return (result == null) ? (this.inverse = new UnmodifiableBiMap(this.delegate.inverse(), this)) : result;
        }

        public Set<V> values() {
            Set<V> result = this.values;
            return (result == null) ? (this.values = Collections.unmodifiableSet(this.delegate.values())) : result;
        }
    }

    static class TransformedEntriesMap<K, V1, V2>
            extends AbstractMap<K, V2> {
        final Map<K, V1> fromMap;

        final Maps.EntryTransformer<? super K, ? super V1, V2> transformer;

        Set<Map.Entry<K, V2>> entrySet;

        Collection<V2> values;

        TransformedEntriesMap(Map<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
            this.fromMap = (Map<K, V1>) Preconditions.checkNotNull(fromMap);
            this.transformer = (Maps.EntryTransformer<? super K, ? super V1, V2>) Preconditions.checkNotNull(transformer);
        }

        public int size() {
            return this.fromMap.size();
        }

        public boolean containsKey(Object key) {
            return this.fromMap.containsKey(key);
        }

        public V2 get(Object key) {
            V1 value = this.fromMap.get(key);
            return (value != null || this.fromMap.containsKey(key)) ? this.transformer.transformEntry((K) key, value) : null;
        }

        public V2 remove(Object key) {
            return this.fromMap.containsKey(key) ? this.transformer.transformEntry((K) key, this.fromMap.remove(key)) : null;
        }

        public void clear() {
            this.fromMap.clear();
        }

        public Set<K> keySet() {
            return this.fromMap.keySet();
        }

        public Set<Map.Entry<K, V2>> entrySet() {
            Set<Map.Entry<K, V2>> result = this.entrySet;
            if (result == null) {
                this.entrySet = result = new Maps.EntrySet<K, V2>() {
                    Map<K, V2> map() {
                        return Maps.TransformedEntriesMap.this;
                    }

                    public Iterator<Map.Entry<K, V2>> iterator() {
                        Iterator<Map.Entry<K, V1>> backingIterator = Maps.TransformedEntriesMap.this.fromMap.entrySet().iterator();

                        return Iterators.transform(backingIterator, new Function<Map.Entry<K, V1>, Map.Entry<K, V2>>() {
                            public Map.Entry<K, V2> apply(Map.Entry<K, V1> entry) {
                                return Maps.immutableEntry(entry.getKey(), (V2) Maps.TransformedEntriesMap.this.transformer.transformEntry(entry.getKey(), entry.getValue()));
                            }
                        });
                    }
                };
            }

            return result;
        }

        public Collection<V2> values() {
            Collection<V2> result = this.values;
            if (result == null) {
                return this.values = new Maps.Values<K, V2>() {
                    Map<K, V2> map() {
                        return Maps.TransformedEntriesMap.this;
                    }
                };
            }
            return result;
        }
    }

    private static abstract class AbstractFilteredMap<K, V>
            extends AbstractMap<K, V> {
        final Map<K, V> unfiltered;
        final Predicate<? super Map.Entry<K, V>> predicate;
        Collection<V> values;

        AbstractFilteredMap(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
            this.unfiltered = unfiltered;
            this.predicate = predicate;
        }

        boolean apply(Object key, V value) {
            K k = (K) key;
            return this.predicate.apply(Maps.immutableEntry(k, value));
        }

        public V put(K key, V value) {
            Preconditions.checkArgument(apply(key, value));
            return this.unfiltered.put(key, value);
        }

        public void putAll(Map<? extends K, ? extends V> map) {
            for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
                Preconditions.checkArgument(apply(entry.getKey(), entry.getValue()));
            }
            this.unfiltered.putAll(map);
        }

        public boolean containsKey(Object key) {
            return (this.unfiltered.containsKey(key) && apply(key, this.unfiltered.get(key)));
        }

        public V get(Object key) {
            V value = this.unfiltered.get(key);
            return (value != null && apply(key, value)) ? value : null;
        }

        public boolean isEmpty() {
            return entrySet().isEmpty();
        }

        public V remove(Object key) {
            return containsKey(key) ? this.unfiltered.remove(key) : null;
        }

        public Collection<V> values() {
            Collection<V> result = this.values;
            return (result == null) ? (this.values = new Values()) : result;
        }

        class Values extends AbstractCollection<V> {
            public Iterator<V> iterator() {
                final Iterator<Map.Entry<K, V>> entryIterator = Maps.AbstractFilteredMap.this.entrySet().iterator();
                return new UnmodifiableIterator<V>() {
                    public boolean hasNext() {
                        return entryIterator.hasNext();
                    }

                    public V next() {
                        return (V) ((Map.Entry) entryIterator.next()).getValue();
                    }
                };
            }

            public int size() {
                return Maps.AbstractFilteredMap.this.entrySet().size();
            }

            public void clear() {
                Maps.AbstractFilteredMap.this.entrySet().clear();
            }

            public boolean isEmpty() {
                return Maps.AbstractFilteredMap.this.entrySet().isEmpty();
            }

            public boolean remove(Object o) {
                Iterator<Map.Entry<K, V>> iterator = Maps.AbstractFilteredMap.this.unfiltered.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<K, V> entry = iterator.next();
                    if (Objects.equal(o, entry.getValue()) && Maps.AbstractFilteredMap.this.predicate.apply(entry)) {
                        iterator.remove();
                        return true;
                    }
                }
                return false;
            }

            public boolean removeAll(Collection<?> collection) {
                Preconditions.checkNotNull(collection);
                boolean changed = false;
                Iterator<Map.Entry<K, V>> iterator = Maps.AbstractFilteredMap.this.unfiltered.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<K, V> entry = iterator.next();
                    if (collection.contains(entry.getValue()) && Maps.AbstractFilteredMap.this.predicate.apply(entry)) {
                        iterator.remove();
                        changed = true;
                    }
                }
                return changed;
            }

            public boolean retainAll(Collection<?> collection) {
                Preconditions.checkNotNull(collection);
                boolean changed = false;
                Iterator<Map.Entry<K, V>> iterator = Maps.AbstractFilteredMap.this.unfiltered.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<K, V> entry = iterator.next();
                    if (!collection.contains(entry.getValue()) && Maps.AbstractFilteredMap.this.predicate.apply(entry)) {

                        iterator.remove();
                        changed = true;
                    }
                }
                return changed;
            }

            public Object[] toArray() {
                return Lists.<V>newArrayList(iterator()).toArray();
            }

            public <T> T[] toArray(T[] array) {
                return (T[]) Lists.<V>newArrayList(iterator()).toArray((Object[]) array);
            }
        }
    }

    private static class FilteredKeyMap<K, V> extends AbstractFilteredMap<K, V> {
        Predicate<? super K> keyPredicate;
        Set<Map.Entry<K, V>> entrySet;
        Set<K> keySet;

        FilteredKeyMap(Map<K, V> unfiltered, Predicate<? super K> keyPredicate, Predicate<Map.Entry<K, V>> entryPredicate) {
            super(unfiltered, entryPredicate);
            this.keyPredicate = keyPredicate;
        }

        public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> result = this.entrySet;
            return (result == null) ? (this.entrySet = Sets.filter(this.unfiltered.entrySet(), this.predicate)) : result;
        }

        public Set<K> keySet() {
            Set<K> result = this.keySet;
            return (result == null) ? (this.keySet = Sets.filter(this.unfiltered.keySet(), this.keyPredicate)) : result;
        }

        public boolean containsKey(Object key) {
            return (this.unfiltered.containsKey(key) && this.keyPredicate.apply(key));
        }
    }

    static class FilteredEntryMap<K, V>
            extends AbstractFilteredMap<K, V> {
        final Set<Map.Entry<K, V>> filteredEntrySet;
        Set<Map.Entry<K, V>> entrySet;
        Set<K> keySet;

        FilteredEntryMap(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
            super(unfiltered, entryPredicate);
            this.filteredEntrySet = Sets.filter(unfiltered.entrySet(), this.predicate);
        }

        public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> result = this.entrySet;
            return (result == null) ? (this.entrySet = new EntrySet()) : result;
        }

        public Set<K> keySet() {
            Set<K> result = this.keySet;
            return (result == null) ? (this.keySet = new KeySet()) : result;
        }

        private class EntrySet extends ForwardingSet<Map.Entry<K, V>> {
            private EntrySet() {
            }

            protected Set<Map.Entry<K, V>> delegate() {
                return Maps.FilteredEntryMap.this.filteredEntrySet;
            }

            public Iterator<Map.Entry<K, V>> iterator() {
                final Iterator<Map.Entry<K, V>> iterator = Maps.FilteredEntryMap.this.filteredEntrySet.iterator();
                return new UnmodifiableIterator<Map.Entry<K, V>>() {
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    public Map.Entry<K, V> next() {
                        final Map.Entry<K, V> entry = iterator.next();
                        return new ForwardingMapEntry<K, V>() {
                            protected Map.Entry<K, V> delegate() {
                                return entry;
                            }

                            public V setValue(V value) {
                                Preconditions.checkArgument(Maps.FilteredEntryMap.this.apply(entry.getKey(), value));
                                return super.setValue(value);
                            }
                        };
                    }
                };
            }
        }

        private class KeySet extends AbstractSet<K> {
            private KeySet() {
            }

            public Iterator<K> iterator() {
                final Iterator<Map.Entry<K, V>> iterator = Maps.FilteredEntryMap.this.filteredEntrySet.iterator();
                return new UnmodifiableIterator<K>() {
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    public K next() {
                        return (K) ((Map.Entry) iterator.next()).getKey();
                    }
                };
            }

            public int size() {
                return Maps.FilteredEntryMap.this.filteredEntrySet.size();
            }

            public void clear() {
                Maps.FilteredEntryMap.this.filteredEntrySet.clear();
            }

            public boolean contains(Object o) {
                return Maps.FilteredEntryMap.this.containsKey(o);
            }

            public boolean remove(Object o) {
                if (Maps.FilteredEntryMap.this.containsKey(o)) {
                    Maps.FilteredEntryMap.this.unfiltered.remove(o);
                    return true;
                }
                return false;
            }

            public boolean removeAll(Collection<?> collection) {
                Preconditions.checkNotNull(collection);
                boolean changed = false;
                for (Object obj : collection) {
                    changed |= remove(obj);
                }
                return changed;
            }

            public boolean retainAll(Collection<?> collection) {
                Preconditions.checkNotNull(collection);
                boolean changed = false;
                Iterator<Map.Entry<K, V>> iterator = Maps.FilteredEntryMap.this.unfiltered.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<K, V> entry = iterator.next();
                    if (!collection.contains(entry.getKey()) && Maps.FilteredEntryMap.this.predicate.apply(entry)) {
                        iterator.remove();
                        changed = true;
                    }
                }
                return changed;
            }

            public Object[] toArray() {
                return Lists.<K>newArrayList(iterator()).toArray();
            }

            public <T> T[] toArray(T[] array) {
                return (T[]) Lists.<K>newArrayList(iterator()).toArray((Object[]) array);
            }
        }

    }

    @GwtCompatible
    static abstract class ImprovedAbstractMap<K, V>
            extends AbstractMap<K, V> {
        private Set<Map.Entry<K, V>> entrySet;

        private Set<K> keySet;

        private Collection<V> values;

        protected abstract Set<Map.Entry<K, V>> createEntrySet();

        public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> result = this.entrySet;
            if (result == null) {
                this.entrySet = result = createEntrySet();
            }
            return result;
        }

        public Set<K> keySet() {
            Set<K> result = this.keySet;
            if (result == null) {
                return this.keySet = new Maps.KeySet<K, V>() {
                    Map<K, V> map() {
                        return Maps.ImprovedAbstractMap.this;
                    }
                };
            }
            return result;
        }

        public Collection<V> values() {
            Collection<V> result = this.values;
            if (result == null) {
                return this.values = new Maps.Values<K, V>() {
                    Map<K, V> map() {
                        return Maps.ImprovedAbstractMap.this;
                    }
                };
            }
            return result;
        }

        public boolean isEmpty() {
            return entrySet().isEmpty();
        }
    }

    static abstract class KeySet<K, V> extends AbstractSet<K> {
        abstract Map<K, V> map();

        public Iterator<K> iterator() {
            return Iterators.transform(map().entrySet().iterator(), new Function<Map.Entry<K, V>, K>() {
                public K apply(Map.Entry<K, V> entry) {
                    return entry.getKey();
                }
            });
        }

        public int size() {
            return map().size();
        }

        public boolean isEmpty() {
            return map().isEmpty();
        }

        public boolean contains(Object o) {
            return map().containsKey(o);
        }

        public boolean remove(Object o) {
            if (contains(o)) {
                map().remove(o);
                return true;
            }
            return false;
        }

        public boolean removeAll(Collection<?> c) {
            return super.removeAll((Collection) Preconditions.checkNotNull(c));
        }

        public void clear() {
            map().clear();
        }
    }

    static abstract class Values<K, V> extends AbstractCollection<V> {
        abstract Map<K, V> map();

        public Iterator<V> iterator() {
            return Iterators.transform(map().entrySet().iterator(), new Function<Map.Entry<K, V>, V>() {
                public V apply(Map.Entry<K, V> entry) {
                    return entry.getValue();
                }
            });
        }

        public boolean remove(Object o) {
            try {
                return super.remove(o);
            } catch (UnsupportedOperationException e) {
                for (Map.Entry<K, V> entry : map().entrySet()) {
                    if (Objects.equal(o, entry.getValue())) {
                        map().remove(entry.getKey());
                        return true;
                    }
                }
                return false;
            }
        }

        public boolean removeAll(Collection<?> c) {
            try {
                return super.removeAll((Collection) Preconditions.checkNotNull(c));
            } catch (UnsupportedOperationException e) {
                Set<K> toRemove = Sets.newHashSet();
                for (Map.Entry<K, V> entry : map().entrySet()) {
                    if (c.contains(entry.getValue())) {
                        toRemove.add(entry.getKey());
                    }
                }
                return map().keySet().removeAll(toRemove);
            }
        }

        public boolean retainAll(Collection<?> c) {
            try {
                return super.retainAll((Collection) Preconditions.checkNotNull(c));
            } catch (UnsupportedOperationException e) {
                Set<K> toRetain = Sets.newHashSet();
                for (Map.Entry<K, V> entry : map().entrySet()) {
                    if (c.contains(entry.getValue())) {
                        toRetain.add(entry.getKey());
                    }
                }
                return map().keySet().retainAll(toRetain);
            }
        }

        public int size() {
            return map().size();
        }

        public boolean isEmpty() {
            return map().isEmpty();
        }

        public boolean contains(@Nullable Object o) {
            return map().containsValue(o);
        }

        public void clear() {
            map().clear();
        }
    }

    static abstract class EntrySet<K, V> extends AbstractSet<Map.Entry<K, V>> {
        abstract Map<K, V> map();

        public int size() {
            return map().size();
        }

        public void clear() {
            map().clear();
        }

        public boolean contains(Object o) {
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                Object key = entry.getKey();
                V value = map().get(key);
                return (Objects.equal(value, entry.getValue()) && (value != null || map().containsKey(key)));
            }

            return false;
        }

        public boolean isEmpty() {
            return map().isEmpty();
        }

        public boolean remove(Object o) {
            if (contains(o)) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                return map().keySet().remove(entry.getKey());
            }
            return false;
        }

        public boolean removeAll(Collection<?> c) {
            try {
                return super.removeAll((Collection) Preconditions.checkNotNull(c));
            } catch (UnsupportedOperationException e) {

                boolean changed = true;
                for (Object o : c) {
                    changed |= remove(o);
                }
                return changed;
            }
        }

        public boolean retainAll(Collection<?> c) {
            try {
                return super.retainAll((Collection) Preconditions.checkNotNull(c));
            } catch (UnsupportedOperationException e) {

                Set<Object> keys = Sets.newHashSetWithExpectedSize(c.size());
                for (Object o : c) {
                    if (contains(o)) {
                        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                        keys.add(entry.getKey());
                    }
                }
                return map().keySet().retainAll(keys);
            }
        }
    }
}


package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

@GwtCompatible(emulated = true)
public final class Multimaps {
    public static <K, V> Multimap<K, V> newMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> factory) {
        return new CustomMultimap<K, V>(map, factory);
    }

    public static <K, V> ListMultimap<K, V> newListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> factory) {
        return new CustomListMultimap<K, V>(map, factory);
    }

    public static <K, V> SetMultimap<K, V> newSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> factory) {
        return new CustomSetMultimap<K, V>(map, factory);
    }

    public static <K, V> SortedSetMultimap<K, V> newSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> factory) {
        return new CustomSortedSetMultimap<K, V>(map, factory);
    }

    public static <K, V, M extends Multimap<K, V>> M invertFrom(Multimap<? extends V, ? extends K> source, M dest) {
        Preconditions.checkNotNull(dest);
        for (Map.Entry<? extends V, ? extends K> entry : source.entries()) {
            dest.put(entry.getValue(), entry.getKey());
        }
        return dest;
    }

    public static <K, V> Multimap<K, V> synchronizedMultimap(Multimap<K, V> multimap) {
        return Synchronized.multimap(multimap, null);
    }

    public static <K, V> Multimap<K, V> unmodifiableMultimap(Multimap<K, V> delegate) {
        if (delegate instanceof UnmodifiableMultimap || delegate instanceof ImmutableMultimap) {
            return delegate;
        }
        return new UnmodifiableMultimap<K, V>(delegate);
    }

    @Deprecated
    public static <K, V> Multimap<K, V> unmodifiableMultimap(ImmutableMultimap<K, V> delegate) {
        return (Multimap<K, V>) Preconditions.checkNotNull(delegate);
    }

    public static <K, V> SetMultimap<K, V> synchronizedSetMultimap(SetMultimap<K, V> multimap) {
        return Synchronized.setMultimap(multimap, null);
    }

    public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(SetMultimap<K, V> delegate) {
        if (delegate instanceof UnmodifiableSetMultimap || delegate instanceof ImmutableSetMultimap) {
            return delegate;
        }
        return new UnmodifiableSetMultimap<K, V>(delegate);
    }

    @Deprecated
    public static <K, V> SetMultimap<K, V> unmodifiableSetMultimap(ImmutableSetMultimap<K, V> delegate) {
        return (SetMultimap<K, V>) Preconditions.checkNotNull(delegate);
    }

    public static <K, V> SortedSetMultimap<K, V> synchronizedSortedSetMultimap(SortedSetMultimap<K, V> multimap) {
        return Synchronized.sortedSetMultimap(multimap, null);
    }

    public static <K, V> SortedSetMultimap<K, V> unmodifiableSortedSetMultimap(SortedSetMultimap<K, V> delegate) {
        if (delegate instanceof UnmodifiableSortedSetMultimap) {
            return delegate;
        }
        return new UnmodifiableSortedSetMultimap<K, V>(delegate);
    }

    public static <K, V> ListMultimap<K, V> synchronizedListMultimap(ListMultimap<K, V> multimap) {
        return Synchronized.listMultimap(multimap, null);
    }

    public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ListMultimap<K, V> delegate) {
        if (delegate instanceof UnmodifiableListMultimap || delegate instanceof ImmutableListMultimap) {
            return delegate;
        }
        return new UnmodifiableListMultimap<K, V>(delegate);
    }

    @Deprecated
    public static <K, V> ListMultimap<K, V> unmodifiableListMultimap(ImmutableListMultimap<K, V> delegate) {
        return (ListMultimap<K, V>) Preconditions.checkNotNull(delegate);
    }

    private static <V> Collection<V> unmodifiableValueCollection(Collection<V> collection) {
        if (collection instanceof SortedSet)
            return Collections.unmodifiableSortedSet((SortedSet<V>) collection);
        if (collection instanceof Set)
            return Collections.unmodifiableSet((Set<? extends V>) collection);
        if (collection instanceof List) {
            return Collections.unmodifiableList((List<? extends V>) collection);
        }
        return Collections.unmodifiableCollection(collection);
    }

    private static <K, V> Map.Entry<K, Collection<V>> unmodifiableAsMapEntry(final Map.Entry<K, Collection<V>> entry) {
        Preconditions.checkNotNull(entry);
        return (Map.Entry) new AbstractMapEntry<K, Collection<Collection<V>>>() {
            public K getKey() {
                return (K) entry.getKey();
            }

            public Collection<V> getValue() {
                return Multimaps.unmodifiableValueCollection((Collection) entry.getValue());
            }
        };
    }

    private static <K, V> Collection<Map.Entry<K, V>> unmodifiableEntries(Collection<Map.Entry<K, V>> entries) {
        if (entries instanceof Set) {
            return Maps.unmodifiableEntrySet((Set<Map.Entry<K, V>>) entries);
        }
        return new Maps.UnmodifiableEntries<K, V>(Collections.unmodifiableCollection(entries));
    }

    private static <K, V> Set<Map.Entry<K, Collection<V>>> unmodifiableAsMapEntries(Set<Map.Entry<K, Collection<V>>> asMapEntries) {
        return new UnmodifiableAsMapEntries<K, V>(Collections.unmodifiableSet(asMapEntries));
    }

    public static <K, V> SetMultimap<K, V> forMap(Map<K, V> map) {
        return new MapMultimap<K, V>(map);
    }

    @Beta
    public static <K, V1, V2> Multimap<K, V2> transformValues(Multimap<K, V1> fromMultimap, final Function<? super V1, V2> function) {
        Preconditions.checkNotNull(function);
        Maps.EntryTransformer<K, V1, V2> transformer = new Maps.EntryTransformer<K, V1, V2>() {
            public V2 transformEntry(K key, V1 value) {
                return (V2) function.apply(value);
            }
        };
        return transformEntries(fromMultimap, transformer);
    }

    @Beta
    public static <K, V1, V2> Multimap<K, V2> transformEntries(Multimap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
        return new TransformedEntriesMultimap<K, V1, V2>(fromMap, transformer);
    }

    @Beta
    public static <K, V1, V2> ListMultimap<K, V2> transformValues(ListMultimap<K, V1> fromMultimap, final Function<? super V1, V2> function) {
        Preconditions.checkNotNull(function);
        Maps.EntryTransformer<K, V1, V2> transformer = new Maps.EntryTransformer<K, V1, V2>() {
            public V2 transformEntry(K key, V1 value) {
                return (V2) function.apply(value);
            }
        };
        return transformEntries(fromMultimap, transformer);
    }

    @Beta
    public static <K, V1, V2> ListMultimap<K, V2> transformEntries(ListMultimap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
        return new TransformedEntriesListMultimap<K, V1, V2>(fromMap, transformer);
    }

    public static <K, V> ImmutableListMultimap<K, V> index(Iterable<V> values, Function<? super V, K> keyFunction) {
        return index(values.iterator(), keyFunction);
    }

    @Deprecated
    @Beta
    public static <K, V, I extends Iterable<V> & Iterator<V>> ImmutableListMultimap<K, V> index(I values, Function<? super V, K> keyFunction) {
        Iterable<V> valuesIterable = (Iterable<V>) Preconditions.checkNotNull(values);
        return index(valuesIterable, keyFunction);
    }

    public static <K, V> ImmutableListMultimap<K, V> index(Iterator<V> values, Function<? super V, K> keyFunction) {
        Preconditions.checkNotNull(keyFunction);
        ImmutableListMultimap.Builder<K, V> builder = ImmutableListMultimap.builder();

        while (values.hasNext()) {
            V value = values.next();
            Preconditions.checkNotNull(value, values);
            builder.put((K) keyFunction.apply(value), value);
        }
        return builder.build();
    }

    private static class CustomMultimap<K, V>
            extends AbstractMultimap<K, V> {
        @GwtIncompatible("java serialization not supported")
        private static final long serialVersionUID = 0L;
        transient Supplier<? extends Collection<V>> factory;

        CustomMultimap(Map<K, Collection<V>> map, Supplier<? extends Collection<V>> factory) {
            super(map);
            this.factory = (Supplier<? extends Collection<V>>) Preconditions.checkNotNull(factory);
        }

        protected Collection<V> createCollection() {
            return (Collection<V>) this.factory.get();
        }

        @GwtIncompatible("java.io.ObjectOutputStream")
        private void writeObject(ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeObject(this.factory);
            stream.writeObject(backingMap());
        }

        @GwtIncompatible("java.io.ObjectInputStream")
        private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            this.factory = (Supplier<? extends Collection<V>>) stream.readObject();
            Map<K, Collection<V>> map = (Map<K, Collection<V>>) stream.readObject();
            setMap(map);
        }
    }

    private static class CustomListMultimap<K, V> extends AbstractListMultimap<K, V> {
        @GwtIncompatible("java serialization not supported")
        private static final long serialVersionUID = 0L;
        transient Supplier<? extends List<V>> factory;

        CustomListMultimap(Map<K, Collection<V>> map, Supplier<? extends List<V>> factory) {
            super(map);
            this.factory = (Supplier<? extends List<V>>) Preconditions.checkNotNull(factory);
        }

        protected List<V> createCollection() {
            return (List<V>) this.factory.get();
        }

        @GwtIncompatible("java.io.ObjectOutputStream")
        private void writeObject(ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeObject(this.factory);
            stream.writeObject(backingMap());
        }

        @GwtIncompatible("java.io.ObjectInputStream")
        private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            this.factory = (Supplier<? extends List<V>>) stream.readObject();
            Map<K, Collection<V>> map = (Map<K, Collection<V>>) stream.readObject();
            setMap(map);
        }
    }

    private static class CustomSetMultimap<K, V> extends AbstractSetMultimap<K, V> {
        @GwtIncompatible("not needed in emulated source")
        private static final long serialVersionUID = 0L;
        transient Supplier<? extends Set<V>> factory;

        CustomSetMultimap(Map<K, Collection<V>> map, Supplier<? extends Set<V>> factory) {
            super(map);
            this.factory = (Supplier<? extends Set<V>>) Preconditions.checkNotNull(factory);
        }

        protected Set<V> createCollection() {
            return (Set<V>) this.factory.get();
        }

        @GwtIncompatible("java.io.ObjectOutputStream")
        private void writeObject(ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeObject(this.factory);
            stream.writeObject(backingMap());
        }

        @GwtIncompatible("java.io.ObjectInputStream")
        private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            this.factory = (Supplier<? extends Set<V>>) stream.readObject();
            Map<K, Collection<V>> map = (Map<K, Collection<V>>) stream.readObject();
            setMap(map);
        }
    }

    private static class CustomSortedSetMultimap<K, V> extends AbstractSortedSetMultimap<K, V> {
        @GwtIncompatible("not needed in emulated source")
        private static final long serialVersionUID = 0L;
        transient Supplier<? extends SortedSet<V>> factory;
        transient Comparator<? super V> valueComparator;

        CustomSortedSetMultimap(Map<K, Collection<V>> map, Supplier<? extends SortedSet<V>> factory) {
            super(map);
            this.factory = (Supplier<? extends SortedSet<V>>) Preconditions.checkNotNull(factory);
            this.valueComparator = ((SortedSet<V>) factory.get()).comparator();
        }

        protected SortedSet<V> createCollection() {
            return (SortedSet<V>) this.factory.get();
        }

        public Comparator<? super V> valueComparator() {
            return this.valueComparator;
        }

        @GwtIncompatible("java.io.ObjectOutputStream")
        private void writeObject(ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject();
            stream.writeObject(this.factory);
            stream.writeObject(backingMap());
        }

        @GwtIncompatible("java.io.ObjectInputStream")
        private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject();
            this.factory = (Supplier<? extends SortedSet<V>>) stream.readObject();
            this.valueComparator = ((SortedSet<V>) this.factory.get()).comparator();
            Map<K, Collection<V>> map = (Map<K, Collection<V>>) stream.readObject();
            setMap(map);
        }
    }

    private static class UnmodifiableMultimap<K, V> extends ForwardingMultimap<K, V> implements Serializable {
        private static final long serialVersionUID = 0L;
        final Multimap<K, V> delegate;
        transient Collection<Map.Entry<K, V>> entries;
        transient Multiset<K> keys;
        transient Set<K> keySet;
        transient Collection<V> values;
        transient Map<K, Collection<V>> map;

        UnmodifiableMultimap(Multimap<K, V> delegate) {
            this.delegate = (Multimap<K, V>) Preconditions.checkNotNull(delegate);
        }

        protected Multimap<K, V> delegate() {
            return this.delegate;
        }

        public void clear() {
            throw new UnsupportedOperationException();
        }

        public Map<K, Collection<V>> asMap() {
            Map<K, Collection<V>> result = this.map;
            if (result == null) {
                final Map<K, Collection<V>> unmodifiableMap = Collections.unmodifiableMap(this.delegate.asMap());

                this.map = result = (Map) new ForwardingMap<K, Collection<Collection<Collection<Collection<V>>>>>() {
                    Set<Map.Entry<K, Collection<V>>> entrySet;
                    Collection<Collection<V>> asMapValues;

                    protected Map<K, Collection<V>> delegate() {
                        return unmodifiableMap;
                    }

                    public Set<Map.Entry<K, Collection<V>>> entrySet() {
                        Set<Map.Entry<K, Collection<V>>> result = this.entrySet;
                        return (result == null) ? (this.entrySet = Multimaps.unmodifiableAsMapEntries(unmodifiableMap.entrySet())) : result;
                    }

                    public Collection<V> get(Object key) {
                        Collection<V> collection = (Collection<V>) unmodifiableMap.get(key);
                        return (collection == null) ? null : Multimaps.unmodifiableValueCollection(collection);
                    }

                    public Collection<Collection<V>> values() {
                        Collection<Collection<V>> result = this.asMapValues;
                        return (result == null) ? (this.asMapValues = new Multimaps.UnmodifiableAsMapValues<V>(unmodifiableMap.values())) : result;
                    }

                    public boolean containsValue(Object o) {
                        return values().contains(o);
                    }
                };
            }
            return result;
        }

        public Collection<Map.Entry<K, V>> entries() {
            Collection<Map.Entry<K, V>> result = this.entries;
            if (result == null) {
                this.entries = result = Multimaps.unmodifiableEntries(this.delegate.entries());
            }
            return result;
        }

        public Collection<V> get(K key) {
            return Multimaps.unmodifiableValueCollection(this.delegate.get(key));
        }

        public Multiset<K> keys() {
            Multiset<K> result = this.keys;
            if (result == null) {
                this.keys = result = Multisets.unmodifiableMultiset(this.delegate.keys());
            }
            return result;
        }

        public Set<K> keySet() {
            Set<K> result = this.keySet;
            if (result == null) {
                this.keySet = result = Collections.unmodifiableSet(this.delegate.keySet());
            }
            return result;
        }

        public boolean put(K key, V value) {
            throw new UnsupportedOperationException();
        }

        public boolean putAll(K key, Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }

        public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object key, Object value) {
            throw new UnsupportedOperationException();
        }

        public Collection<V> removeAll(Object key) {
            throw new UnsupportedOperationException();
        }

        public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }

        public Collection<V> values() {
            Collection<V> result = this.values;
            if (result == null) {
                this.values = result = Collections.unmodifiableCollection(this.delegate.values());
            }
            return result;
        }
    }

    private static class UnmodifiableAsMapValues<V>
            extends ForwardingCollection<Collection<V>> {
        final Collection<Collection<V>> delegate;

        UnmodifiableAsMapValues(Collection<Collection<V>> delegate) {
            this.delegate = Collections.unmodifiableCollection(delegate);
        }

        protected Collection<Collection<V>> delegate() {
            return this.delegate;
        }

        public Iterator<Collection<V>> iterator() {
            final Iterator<Collection<V>> iterator = this.delegate.iterator();
            return new Iterator<Collection<V>>() {
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                public Collection<V> next() {
                    return Multimaps.unmodifiableValueCollection(iterator.next());
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] array) {
            return (T[]) standardToArray((Object[]) array);
        }

        public boolean contains(Object o) {
            return standardContains(o);
        }

        public boolean containsAll(Collection<?> c) {
            return standardContainsAll(c);
        }
    }

    private static class UnmodifiableListMultimap<K, V> extends UnmodifiableMultimap<K, V> implements ListMultimap<K, V> {
        private static final long serialVersionUID = 0L;

        UnmodifiableListMultimap(ListMultimap<K, V> delegate) {
            super(delegate);
        }

        public ListMultimap<K, V> delegate() {
            return (ListMultimap<K, V>) super.delegate();
        }

        public List<V> get(K key) {
            return Collections.unmodifiableList(delegate().get(key));
        }

        public List<V> removeAll(Object key) {
            throw new UnsupportedOperationException();
        }

        public List<V> replaceValues(K key, Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }
    }

    private static class UnmodifiableSetMultimap<K, V> extends UnmodifiableMultimap<K, V> implements SetMultimap<K, V> {
        private static final long serialVersionUID = 0L;

        UnmodifiableSetMultimap(SetMultimap<K, V> delegate) {
            super(delegate);
        }

        public SetMultimap<K, V> delegate() {
            return (SetMultimap<K, V>) super.delegate();
        }

        public Set<V> get(K key) {
            return Collections.unmodifiableSet(delegate().get(key));
        }

        public Set<Map.Entry<K, V>> entries() {
            return Maps.unmodifiableEntrySet(delegate().entries());
        }

        public Set<V> removeAll(Object key) {
            throw new UnsupportedOperationException();
        }

        public Set<V> replaceValues(K key, Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }
    }

    private static class UnmodifiableSortedSetMultimap<K, V> extends UnmodifiableSetMultimap<K, V> implements SortedSetMultimap<K, V> {
        private static final long serialVersionUID = 0L;

        UnmodifiableSortedSetMultimap(SortedSetMultimap<K, V> delegate) {
            super(delegate);
        }

        public SortedSetMultimap<K, V> delegate() {
            return (SortedSetMultimap<K, V>) super.delegate();
        }

        public SortedSet<V> get(K key) {
            return Collections.unmodifiableSortedSet(delegate().get(key));
        }

        public SortedSet<V> removeAll(Object key) {
            throw new UnsupportedOperationException();
        }

        public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }

        public Comparator<? super V> valueComparator() {
            return delegate().valueComparator();
        }
    }

    static class UnmodifiableAsMapEntries<K, V>
            extends ForwardingSet<Map.Entry<K, Collection<V>>> {
        private final Set<Map.Entry<K, Collection<V>>> delegate;

        UnmodifiableAsMapEntries(Set<Map.Entry<K, Collection<V>>> delegate) {
            this.delegate = delegate;
        }

        protected Set<Map.Entry<K, Collection<V>>> delegate() {
            return this.delegate;
        }

        public Iterator<Map.Entry<K, Collection<V>>> iterator() {
            final Iterator<Map.Entry<K, Collection<V>>> iterator = this.delegate.iterator();
            return new ForwardingIterator<Map.Entry<K, Collection<V>>>() {
                protected Iterator<Map.Entry<K, Collection<V>>> delegate() {
                    return iterator;
                }

                public Map.Entry<K, Collection<V>> next() {
                    return Multimaps.unmodifiableAsMapEntry(iterator.next());
                }
            };
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] array) {
            return (T[]) standardToArray((Object[]) array);
        }

        public boolean contains(Object o) {
            return Maps.containsEntryImpl(delegate(), o);
        }

        public boolean containsAll(Collection<?> c) {
            return standardContainsAll(c);
        }

        public boolean equals(@Nullable Object object) {
            return standardEquals(object);
        }
    }

    private static class MapMultimap<K, V>
            implements SetMultimap<K, V>, Serializable {
        private static final Joiner.MapJoiner JOINER = Joiner.on("], ").withKeyValueSeparator("=[").useForNull("null");
        private static final long serialVersionUID = 7845222491160860175L;
        final Map<K, V> map;
        transient Map<K, Collection<V>> asMap;

        MapMultimap(Map<K, V> map) {
            this.map = (Map<K, V>) Preconditions.checkNotNull(map);
        }

        public int size() {
            return this.map.size();
        }

        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        public boolean containsKey(Object key) {
            return this.map.containsKey(key);
        }

        public boolean containsValue(Object value) {
            return this.map.containsValue(value);
        }

        public boolean containsEntry(Object key, Object value) {
            return this.map.entrySet().contains(Maps.immutableEntry(key, value));
        }

        public Set<V> get(final K key) {
            return new AbstractSet<V>() {
                public Iterator<V> iterator() {
                    return new Iterator<V>() {
                        int i;

                        public boolean hasNext() {
                            return (this.i == 0 && Multimaps.MapMultimap.this.map.containsKey(key));
                        }

                        public V next() {
                            if (!hasNext()) {
                                throw new NoSuchElementException();
                            }
                            this.i++;
                            return (V) Multimaps.MapMultimap.this.map.get(key);
                        }

                        public void remove() {
                            Preconditions.checkState((this.i == 1));
                            this.i = -1;
                            Multimaps.MapMultimap.this.map.remove(key);
                        }
                    };
                }

                public int size() {
                    return Multimaps.MapMultimap.this.map.containsKey(key) ? 1 : 0;
                }
            };
        }

        public boolean put(K key, V value) {
            throw new UnsupportedOperationException();
        }

        public boolean putAll(K key, Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }

        public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
            throw new UnsupportedOperationException();
        }

        public Set<V> replaceValues(K key, Iterable<? extends V> values) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object key, Object value) {
            return this.map.entrySet().remove(Maps.immutableEntry(key, value));
        }

        public Set<V> removeAll(Object key) {
            Set<V> values = new HashSet<V>(2);
            if (!this.map.containsKey(key)) {
                return values;
            }
            values.add(this.map.remove(key));
            return values;
        }

        public void clear() {
            this.map.clear();
        }

        public Set<K> keySet() {
            return this.map.keySet();
        }

        public Multiset<K> keys() {
            return Multisets.forSet(this.map.keySet());
        }

        public Collection<V> values() {
            return this.map.values();
        }

        public Set<Map.Entry<K, V>> entries() {
            return this.map.entrySet();
        }

        public Map<K, Collection<V>> asMap() {
            Map<K, Collection<V>> result = this.asMap;
            if (result == null) {
                this.asMap = result = new AsMap();
            }
            return result;
        }

        public boolean equals(@Nullable Object object) {
            if (object == this) {
                return true;
            }
            if (object instanceof Multimap) {
                Multimap<?, ?> that = (Multimap<?, ?>) object;
                return (size() == that.size() && asMap().equals(that.asMap()));
            }
            return false;
        }

        public int hashCode() {
            return this.map.hashCode();
        }

        public String toString() {
            if (this.map.isEmpty()) {
                return "{}";
            }
            StringBuilder builder = Collections2.newStringBuilderForCollection(this.map.size()).append('{');

            JOINER.appendTo(builder, this.map);
            return builder.append("]}").toString();
        }

        class AsMapEntries
                extends AbstractSet<Map.Entry<K, Collection<V>>> {
            public int size() {
                return Multimaps.MapMultimap.this.map.size();
            }

            public Iterator<Map.Entry<K, Collection<V>>> iterator() {
                return new Iterator<Map.Entry<K, Collection<V>>>() {
                    final Iterator<K> keys = Multimaps.MapMultimap.this.map.keySet().iterator();

                    public boolean hasNext() {
                        return this.keys.hasNext();
                    }

                    public Map.Entry<K, Collection<V>> next() {
                        final K key = this.keys.next();
                        return (Map.Entry) new AbstractMapEntry<K, Collection<Collection<V>>>() {
                            public K getKey() {
                                return (K) key;
                            }

                            public Collection<V> getValue() {
                                return Multimaps.MapMultimap.this.get(key);
                            }
                        };
                    }

                    public void remove() {
                        this.keys.remove();
                    }
                };
            }

            public boolean contains(Object o) {
                if (!(o instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                if (!(entry.getValue() instanceof Set)) {
                    return false;
                }
                Set<?> set = (Set) entry.getValue();
                return (set.size() == 1 && Multimaps.MapMultimap.this.containsEntry(entry.getKey(), set.iterator().next()));
            }

            public boolean remove(Object o) {
                if (!(o instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                if (!(entry.getValue() instanceof Set)) {
                    return false;
                }
                Set<?> set = (Set) entry.getValue();
                return (set.size() == 1 && Multimaps.MapMultimap.this.map.entrySet().remove(Maps.immutableEntry(entry.getKey(), set.iterator().next())));
            }
        }

        class AsMap
                extends Maps.ImprovedAbstractMap<K, Collection<V>> {
            protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
                return new Multimaps.MapMultimap.AsMapEntries();
            }

            public boolean containsKey(Object key) {
                return Multimaps.MapMultimap.this.map.containsKey(key);
            }

            public Collection<V> get(Object key) {
                Collection<V> collection = Multimaps.MapMultimap.this.get(key);
                return collection.isEmpty() ? null : collection;
            }

            public Collection<V> remove(Object key) {
                Collection<V> collection = Multimaps.MapMultimap.this.removeAll(key);
                return collection.isEmpty() ? null : collection;
            }
        }
    }

    private static class TransformedEntriesMultimap<K, V1, V2> implements Multimap<K, V2> {
        final Multimap<K, V1> fromMultimap;
        final Maps.EntryTransformer<? super K, ? super V1, V2> transformer;
        private transient Map<K, Collection<V2>> asMap;
        private transient Collection<Map.Entry<K, V2>> entries;
        private transient Collection<V2> values;

        TransformedEntriesMultimap(Multimap<K, V1> fromMultimap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
            this.fromMultimap = (Multimap<K, V1>) Preconditions.checkNotNull(fromMultimap);
            this.transformer = (Maps.EntryTransformer<? super K, ? super V1, V2>) Preconditions.checkNotNull(transformer);
        }

        Collection<V2> transform(final K key, Collection<V1> values) {
            return Collections2.transform(values, new Function<V1, V2>() {
                public V2 apply(V1 value) {
                    return Multimaps.TransformedEntriesMultimap.this.transformer.transformEntry(key, value);
                }
            });
        }

        public Map<K, Collection<V2>> asMap() {
            if (this.asMap == null) {
                Map<K, Collection<V2>> aM = Maps.transformEntries(this.fromMultimap.asMap(), (Maps.EntryTransformer) new Maps.EntryTransformer<K, Collection<Collection<V1>>, Collection<Collection<V2>>>() {

                    public Collection<V2> transformEntry(K key, Collection<V1> value) {
                        return Multimaps.TransformedEntriesMultimap.this.transform(key, value);
                    }
                });
                this.asMap = aM;
                return aM;
            }
            return this.asMap;
        }

        public void clear() {
            this.fromMultimap.clear();
        }

        public boolean containsEntry(Object key, Object value) {
            Collection<V2> values = get((K) key);
            return values.contains(value);
        }

        public boolean containsKey(Object key) {
            return this.fromMultimap.containsKey(key);
        }

        public boolean containsValue(Object value) {
            return values().contains(value);
        }

        public Collection<Map.Entry<K, V2>> entries() {
            if (this.entries == null) {
                Collection<Map.Entry<K, V2>> es = new TransformedEntries(this.transformer);
                this.entries = es;
                return es;
            }
            return this.entries;
        }

        public Collection<V2> get(K key) {
            return transform(key, this.fromMultimap.get(key));
        }

        public boolean isEmpty() {
            return this.fromMultimap.isEmpty();
        }

        public Set<K> keySet() {
            return this.fromMultimap.keySet();
        }

        public Multiset<K> keys() {
            return this.fromMultimap.keys();
        }

        public boolean put(K key, V2 value) {
            throw new UnsupportedOperationException();
        }

        public boolean putAll(K key, Iterable<? extends V2> values) {
            throw new UnsupportedOperationException();
        }

        public boolean putAll(Multimap<? extends K, ? extends V2> multimap) {
            throw new UnsupportedOperationException();
        }

        public boolean remove(Object key, Object value) {
            return get((K) key).remove(value);
        }

        public Collection<V2> removeAll(Object key) {
            return transform((K) key, this.fromMultimap.removeAll(key));
        }

        public Collection<V2> replaceValues(K key, Iterable<? extends V2> values) {
            throw new UnsupportedOperationException();
        }

        public int size() {
            return this.fromMultimap.size();
        }

        public Collection<V2> values() {
            if (this.values == null) {
                Collection<V2> vs = Collections2.transform(this.fromMultimap.entries(), new Function<Map.Entry<K, V1>, V2>() {
                    public V2 apply(Map.Entry<K, V1> entry) {
                        return (V2) Multimaps.TransformedEntriesMultimap.this.transformer.transformEntry(entry.getKey(), entry.getValue());
                    }
                });

                this.values = vs;
                return vs;
            }
            return this.values;
        }

        public boolean equals(Object obj) {
            if (obj instanceof Multimap) {
                Multimap<?, ?> other = (Multimap<?, ?>) obj;
                return asMap().equals(other.asMap());
            }
            return false;
        }

        public int hashCode() {
            return asMap().hashCode();
        }

        public String toString() {
            return asMap().toString();
        }

        private class TransformedEntries
                extends Collections2.TransformedCollection<Map.Entry<K, V1>, Map.Entry<K, V2>> {
            TransformedEntries(Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
                super(Multimaps.TransformedEntriesMultimap.this.fromMultimap.entries(), new Function<Map.Entry<K, V1>, Map.Entry<K, V2>>(Multimaps.TransformedEntriesMultimap.this, transformer) {
                    public Map.Entry<K, V2> apply(final Map.Entry<K, V1> entry) {
                        return new AbstractMapEntry<K, V2>() {
                            public K getKey() {
                                return (K) entry.getKey();
                            }

                            public V2 getValue() {
                                return (V2) transformer.transformEntry(entry.getKey(), entry.getValue());
                            }
                        };
                    }
                });
            }

            public boolean contains(Object o) {
                if (o instanceof Map.Entry) {
                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                    return Multimaps.TransformedEntriesMultimap.this.containsEntry(entry.getKey(), entry.getValue());
                }
                return false;
            }

            public boolean remove(Object o) {
                if (o instanceof Map.Entry) {
                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                    Collection<V2> values = Multimaps.TransformedEntriesMultimap.this.get(entry.getKey());
                    return values.remove(entry.getValue());
                }
                return false;
            }
        }
    }

    private static final class TransformedEntriesListMultimap<K, V1, V2>
            extends TransformedEntriesMultimap<K, V1, V2>
            implements ListMultimap<K, V2> {
        TransformedEntriesListMultimap(ListMultimap<K, V1> fromMultimap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
            super(fromMultimap, transformer);
        }

        List<V2> transform(final K key, Collection<V1> values) {
            return Lists.transform((List<V1>) values, new Function<V1, V2>() {
                public V2 apply(V1 value) {
                    return Multimaps.TransformedEntriesListMultimap.this.transformer.transformEntry(key, value);
                }
            });
        }

        public List<V2> get(K key) {
            return transform(key, this.fromMultimap.get(key));
        }

        public List<V2> removeAll(Object key) {
            return transform((K) key, this.fromMultimap.removeAll(key));
        }

        public List<V2> replaceValues(K key, Iterable<? extends V2> values) {
            throw new UnsupportedOperationException();
        }
    }

    static abstract class Keys<K, V> extends AbstractMultiset<K> {
        private Set<Multiset.Entry<K>> entrySet;

        abstract Multimap<K, V> multimap();

        public Set<Multiset.Entry<K>> entrySet() {
            return (this.entrySet == null) ? (this.entrySet = createEntrySet()) : this.entrySet;
        }

        Iterator<Multiset.Entry<K>> entryIterator() {
            final Iterator<Map.Entry<K, Collection<V>>> backingIterator = multimap().asMap().entrySet().iterator();

            return new Iterator<Multiset.Entry<K>>() {
                public boolean hasNext() {
                    return backingIterator.hasNext();
                }

                public Multiset.Entry<K> next() {
                    final Map.Entry<K, Collection<V>> backingEntry = backingIterator.next();

                    return new Multisets.AbstractEntry<K>() {
                        public K getElement() {
                            return (K) backingEntry.getKey();
                        }

                        public int getCount() {
                            return ((Collection) backingEntry.getValue()).size();
                        }
                    };
                }

                public void remove() {
                    backingIterator.remove();
                }
            };
        }

        int distinctElements() {
            return multimap().asMap().size();
        }

        Set<Multiset.Entry<K>> createEntrySet() {
            return new KeysEntrySet();
        }

        public boolean contains(@Nullable Object element) {
            return multimap().containsKey(element);
        }

        public Iterator<K> iterator() {
            return Iterators.transform(multimap().entries().iterator(), new Function<Map.Entry<K, V>, K>() {
                public K apply(Map.Entry<K, V> entry) {
                    return entry.getKey();
                }
            });
        }

        public int count(@Nullable Object element) {
            try {
                if (multimap().containsKey(element)) {
                    Collection<V> values = (Collection<V>) multimap().asMap().get(element);
                    return (values == null) ? 0 : values.size();
                }
                return 0;
            } catch (ClassCastException e) {
                return 0;
            } catch (NullPointerException e) {
                return 0;
            }
        }

        public int remove(@Nullable Object element, int occurrences) {
            Collection<V> values;
            Preconditions.checkArgument((occurrences >= 0));
            if (occurrences == 0) {
                return count(element);
            }

            try {
                values = (Collection<V>) multimap().asMap().get(element);
            } catch (ClassCastException e) {
                return 0;
            } catch (NullPointerException e) {
                return 0;
            }

            if (values == null) {
                return 0;
            }

            int oldCount = values.size();
            if (occurrences >= oldCount) {
                values.clear();
            } else {
                Iterator<V> iterator = values.iterator();
                for (int i = 0; i < occurrences; i++) {
                    iterator.next();
                    iterator.remove();
                }
            }
            return oldCount;
        }

        public void clear() {
            multimap().clear();
        }

        public Set<K> elementSet() {
            return multimap().keySet();
        }

        class KeysEntrySet extends Multisets.EntrySet<K> {
            Multiset<K> multiset() {
                return Multimaps.Keys.this;
            }

            public Iterator<Multiset.Entry<K>> iterator() {
                return Multimaps.Keys.this.entryIterator();
            }

            public int size() {
                return Multimaps.Keys.this.distinctElements();
            }

            public boolean isEmpty() {
                return Multimaps.Keys.this.multimap().isEmpty();
            }

            public boolean contains(@Nullable Object o) {
                if (o instanceof Multiset.Entry) {
                    Multiset.Entry<?> entry = (Multiset.Entry) o;
                    Collection<V> collection = (Collection<V>) Multimaps.Keys.this.multimap().asMap().get(entry.getElement());
                    return (collection != null && collection.size() == entry.getCount());
                }
                return false;
            }

            public boolean remove(@Nullable Object o) {
                if (o instanceof Multiset.Entry) {
                    Multiset.Entry<?> entry = (Multiset.Entry) o;
                    Collection<V> collection = (Collection<V>) Multimaps.Keys.this.multimap().asMap().get(entry.getElement());
                    if (collection != null && collection.size() == entry.getCount()) {
                        collection.clear();
                        return true;
                    }
                }
                return false;
            }
        }
    }

    static abstract class Values<K, V> extends AbstractCollection<V> {
        abstract Multimap<K, V> multimap();

        public Iterator<V> iterator() {
            final Iterator<Map.Entry<K, V>> backingIterator = multimap().entries().iterator();

            return new Iterator<V>() {
                public boolean hasNext() {
                    return backingIterator.hasNext();
                }

                public V next() {
                    return (V) ((Map.Entry) backingIterator.next()).getValue();
                }

                public void remove() {
                    backingIterator.remove();
                }
            };
        }

        public int size() {
            return multimap().size();
        }

        public boolean contains(@Nullable Object o) {
            return multimap().containsValue(o);
        }

        public void clear() {
            multimap().clear();
        }
    }

    static abstract class Entries<K, V>
            extends AbstractCollection<Map.Entry<K, V>> {
        abstract Multimap<K, V> multimap();

        public int size() {
            return multimap().size();
        }

        public boolean contains(@Nullable Object o) {
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                return multimap().containsEntry(entry.getKey(), entry.getValue());
            }
            return false;
        }

        public boolean remove(@Nullable Object o) {
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                return multimap().remove(entry.getKey(), entry.getValue());
            }
            return false;
        }

        public void clear() {
            multimap().clear();
        }
    }

    static abstract class EntrySet<K, V>
            extends Entries<K, V>
            implements Set<Map.Entry<K, V>> {
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }

        public boolean equals(@Nullable Object obj) {
            return Sets.equalsImpl(this, obj);
        }
    }

    static abstract class AsMap<K, V>
            extends Maps.ImprovedAbstractMap<K, Collection<V>> {
        protected Set<Map.Entry<K, Collection<V>>> createEntrySet() {
            return new EntrySet();
        }

        void removeValuesForKey(Object key) {
            multimap().removeAll(key);
        }

        public Collection<V> get(Object key) {
            return containsKey(key) ? multimap().get((K) key) : null;
        }

        public Collection<V> remove(Object key) {
            return containsKey(key) ? multimap().removeAll(key) : null;
        }

        public Set<K> keySet() {
            return multimap().keySet();
        }

        public boolean isEmpty() {
            return multimap().isEmpty();
        }

        public boolean containsKey(Object key) {
            return multimap().containsKey(key);
        }

        public void clear() {
            multimap().clear();
        }

        abstract Multimap<K, V> multimap();

        public abstract int size();

        abstract Iterator<Map.Entry<K, Collection<V>>> entryIterator();

        class EntrySet extends Maps.EntrySet<K, Collection<V>> {
            Map<K, Collection<V>> map() {
                return Multimaps.AsMap.this;
            }

            public Iterator<Map.Entry<K, Collection<V>>> iterator() {
                return Multimaps.AsMap.this.entryIterator();
            }

            public boolean remove(Object o) {
                if (!contains(o)) {
                    return false;
                }
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                Multimaps.AsMap.this.removeValuesForKey(entry.getKey());
                return true;
            }
        }
    }
}


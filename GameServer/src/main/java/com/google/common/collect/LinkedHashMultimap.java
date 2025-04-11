package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

@GwtCompatible(serializable = true, emulated = true)
public final class LinkedHashMultimap<K, V>
        extends AbstractSetMultimap<K, V> {
    private static final int DEFAULT_VALUES_PER_KEY = 8;
    @GwtIncompatible("java serialization not supported")
    private static final long serialVersionUID = 0L;
    @VisibleForTesting
    transient int expectedValuesPerKey = 8;
    transient Collection<Map.Entry<K, V>> linkedEntries;

    private LinkedHashMultimap() {
        super(new LinkedHashMap<K, Collection<V>>());
        this.linkedEntries = Sets.newLinkedHashSet();
    }

    private LinkedHashMultimap(int expectedKeys, int expectedValuesPerKey) {
        super(new LinkedHashMap<K, Collection<V>>(expectedKeys));
        Preconditions.checkArgument((expectedValuesPerKey >= 0));
        this.expectedValuesPerKey = expectedValuesPerKey;
        this.linkedEntries = new LinkedHashSet<Map.Entry<K, V>>((int) Math.min(1073741824L, expectedKeys * expectedValuesPerKey));
    }

    private LinkedHashMultimap(Multimap<? extends K, ? extends V> multimap) {
        super(new LinkedHashMap<K, Collection<V>>(Maps.capacity(multimap.keySet().size())));

        this.linkedEntries = new LinkedHashSet<Map.Entry<K, V>>(Maps.capacity(multimap.size()));

        putAll(multimap);
    }

    public static <K, V> LinkedHashMultimap<K, V> create() {
        return new LinkedHashMultimap<K, V>();
    }

    public static <K, V> LinkedHashMultimap<K, V> create(int expectedKeys, int expectedValuesPerKey) {
        return new LinkedHashMultimap<K, V>(expectedKeys, expectedValuesPerKey);
    }

    public static <K, V> LinkedHashMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
        return new LinkedHashMultimap<K, V>(multimap);
    }

    Set<V> createCollection() {
        return new LinkedHashSet<V>(Maps.capacity(this.expectedValuesPerKey));
    }

    Collection<V> createCollection(@Nullable K key) {
        return new SetDecorator(key, createCollection());
    }

    Iterator<Map.Entry<K, V>> createEntryIterator() {
        final Iterator<Map.Entry<K, V>> delegateIterator = this.linkedEntries.iterator();

        return new Iterator<Map.Entry<K, V>>() {
            Map.Entry<K, V> entry;

            public boolean hasNext() {
                return delegateIterator.hasNext();
            }

            public Map.Entry<K, V> next() {
                this.entry = delegateIterator.next();
                return this.entry;
            }

            public void remove() {
                delegateIterator.remove();
                LinkedHashMultimap.this.remove(this.entry.getKey(), this.entry.getValue());
            }
        };
    }

    public Set<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
        return super.replaceValues(key, values);
    }

    public Set<Map.Entry<K, V>> entries() {
        return super.entries();
    }

    public Collection<V> values() {
        return super.values();
    }

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(this.expectedValuesPerKey);
        Serialization.writeMultimap(this, stream);
        for (Map.Entry<K, V> entry : this.linkedEntries) {
            stream.writeObject(entry.getKey());
            stream.writeObject(entry.getValue());
        }
    }

    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.expectedValuesPerKey = stream.readInt();
        int distinctKeys = Serialization.readCount(stream);
        setMap(new LinkedHashMap<K, Collection<V>>(Maps.capacity(distinctKeys)));
        this.linkedEntries = new LinkedHashSet<Map.Entry<K, V>>(distinctKeys * this.expectedValuesPerKey);

        Serialization.populateMultimap(this, stream, distinctKeys);
        this.linkedEntries.clear();
        for (int i = 0; i < size(); i++) {

            K key = (K) stream.readObject();

            V value = (V) stream.readObject();
            this.linkedEntries.add(Maps.immutableEntry(key, value));
        }
    }

    private class SetDecorator extends ForwardingSet<V> {
        final Set<V> delegate;
        final K key;

        SetDecorator(K key, Set<V> delegate) {
            this.delegate = delegate;
            this.key = key;
        }

        protected Set<V> delegate() {
            return this.delegate;
        }

        <E> Map.Entry<K, E> createEntry(@Nullable E value) {
            return Maps.immutableEntry(this.key, value);
        }

        <E> Collection<Map.Entry<K, E>> createEntries(Collection<E> values) {
            Collection<Map.Entry<K, E>> entries = Lists.newArrayListWithExpectedSize(values.size());

            for (E value : values) {
                entries.add(createEntry(value));
            }
            return entries;
        }

        public boolean add(@Nullable V value) {
            boolean changed = this.delegate.add(value);
            if (changed) {
                LinkedHashMultimap.this.linkedEntries.add(createEntry(value));
            }
            return changed;
        }

        public boolean addAll(Collection<? extends V> values) {
            boolean changed = this.delegate.addAll(values);
            if (changed) {
                LinkedHashMultimap.this.linkedEntries.addAll(createEntries(delegate()));
            }
            return changed;
        }

        public void clear() {
            for (V value : this.delegate) {
                LinkedHashMultimap.this.linkedEntries.remove(createEntry(value));
            }
            this.delegate.clear();
        }

        public Iterator<V> iterator() {
            final Iterator<V> delegateIterator = this.delegate.iterator();
            return new Iterator<V>() {
                V value;

                public boolean hasNext() {
                    return delegateIterator.hasNext();
                }

                public V next() {
                    this.value = delegateIterator.next();
                    return this.value;
                }

                public void remove() {
                    delegateIterator.remove();
                    LinkedHashMultimap.this.linkedEntries.remove(LinkedHashMultimap.SetDecorator.this.createEntry(this.value));
                }
            };
        }

        public boolean remove(@Nullable Object value) {
            boolean changed = this.delegate.remove(value);
            if (changed) {

                LinkedHashMultimap.this.linkedEntries.remove(createEntry(value));
            }
            return changed;
        }

        public boolean removeAll(Collection<?> values) {
            boolean changed = this.delegate.removeAll(values);
            if (changed) {
                LinkedHashMultimap.this.linkedEntries.removeAll(createEntries(values));
            }
            return changed;
        }

        public boolean retainAll(Collection<?> values) {
            boolean changed = false;
            Iterator<V> iterator = this.delegate.iterator();
            while (iterator.hasNext()) {
                V value = iterator.next();
                if (!values.contains(value)) {
                    iterator.remove();
                    LinkedHashMultimap.this.linkedEntries.remove(Maps.immutableEntry(this.key, value));
                    changed = true;
                }
            }
            return changed;
        }
    }
}


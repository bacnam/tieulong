package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

@GwtCompatible(serializable = true, emulated = true)
public final class ArrayListMultimap<K, V>
        extends AbstractListMultimap<K, V> {
    private static final int DEFAULT_VALUES_PER_KEY = 10;
    @GwtIncompatible("Not needed in emulated source.")
    private static final long serialVersionUID = 0L;
    @VisibleForTesting
    transient int expectedValuesPerKey;

    private ArrayListMultimap() {
        super(new HashMap<K, Collection<V>>());
        this.expectedValuesPerKey = 10;
    }

    private ArrayListMultimap(int expectedKeys, int expectedValuesPerKey) {
        super(Maps.newHashMapWithExpectedSize(expectedKeys));
        Preconditions.checkArgument((expectedValuesPerKey >= 0));
        this.expectedValuesPerKey = expectedValuesPerKey;
    }

    private ArrayListMultimap(Multimap<? extends K, ? extends V> multimap) {
        this(multimap.keySet().size(), (multimap instanceof ArrayListMultimap) ? ((ArrayListMultimap) multimap).expectedValuesPerKey : 10);

        putAll(multimap);
    }

    public static <K, V> ArrayListMultimap<K, V> create() {
        return new ArrayListMultimap<K, V>();
    }

    public static <K, V> ArrayListMultimap<K, V> create(int expectedKeys, int expectedValuesPerKey) {
        return new ArrayListMultimap<K, V>(expectedKeys, expectedValuesPerKey);
    }

    public static <K, V> ArrayListMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
        return new ArrayListMultimap<K, V>(multimap);
    }

    List<V> createCollection() {
        return new ArrayList<V>(this.expectedValuesPerKey);
    }

    public void trimToSize() {
        for (Collection<V> collection : backingMap().values()) {
            ArrayList<V> arrayList = (ArrayList<V>) collection;
            arrayList.trimToSize();
        }
    }

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(this.expectedValuesPerKey);
        Serialization.writeMultimap(this, stream);
    }

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.expectedValuesPerKey = stream.readInt();
        int distinctKeys = Serialization.readCount(stream);
        Map<K, Collection<V>> map = Maps.newHashMapWithExpectedSize(distinctKeys);
        setMap(map);
        Serialization.populateMultimap(this, stream, distinctKeys);
    }
}


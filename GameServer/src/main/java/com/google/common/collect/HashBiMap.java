package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class HashBiMap<K, V>
extends AbstractBiMap<K, V>
{
@GwtIncompatible("Not needed in emulated source")
private static final long serialVersionUID = 0L;

public static <K, V> HashBiMap<K, V> create() {
return new HashBiMap<K, V>();
}

public static <K, V> HashBiMap<K, V> create(int expectedSize) {
return new HashBiMap<K, V>(expectedSize);
}

public static <K, V> HashBiMap<K, V> create(Map<? extends K, ? extends V> map) {
HashBiMap<K, V> bimap = create(map.size());
bimap.putAll(map);
return bimap;
}

private HashBiMap() {
super(new HashMap<K, V>(), new HashMap<V, K>());
}

private HashBiMap(int expectedSize) {
super(Maps.newHashMapWithExpectedSize(expectedSize), Maps.newHashMapWithExpectedSize(expectedSize));
}

public V put(@Nullable K key, @Nullable V value) {
return super.put(key, value);
}

public V forcePut(@Nullable K key, @Nullable V value) {
return super.forcePut(key, value);
}

@GwtIncompatible("java.io.ObjectOutputStream")
private void writeObject(ObjectOutputStream stream) throws IOException {
stream.defaultWriteObject();
Serialization.writeMap(this, stream);
}

@GwtIncompatible("java.io.ObjectInputStream")
private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
stream.defaultReadObject();
int size = Serialization.readCount(stream);
setDelegates(Maps.newHashMapWithExpectedSize(size), Maps.newHashMapWithExpectedSize(size));

Serialization.populateMap(this, stream, size);
}
}


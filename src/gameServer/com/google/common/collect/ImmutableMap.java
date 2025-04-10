package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableMap<K, V>
implements Map<K, V>, Serializable
{
public static <K, V> ImmutableMap<K, V> of() {
return EmptyImmutableMap.INSTANCE;
}

public static <K, V> ImmutableMap<K, V> of(K k1, V v1) {
return new SingletonImmutableMap<K, V>((K)Preconditions.checkNotNull(k1), (V)Preconditions.checkNotNull(v1));
}

public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2) {
return new RegularImmutableMap<K, V>((Map.Entry<?, ?>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2) });
}

public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
return new RegularImmutableMap<K, V>((Map.Entry<?, ?>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) });
}

public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
return new RegularImmutableMap<K, V>((Map.Entry<?, ?>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4) });
}

public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
return new RegularImmutableMap<K, V>((Map.Entry<?, ?>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5) });
}

public static <K, V> Builder<K, V> builder() {
return new Builder<K, V>();
}

static <K, V> Map.Entry<K, V> entryOf(K key, V value) {
return Maps.immutableEntry((K)Preconditions.checkNotNull(key, "null key"), (V)Preconditions.checkNotNull(value, "null value"));
}

public static class Builder<K, V>
{
final ArrayList<Map.Entry<K, V>> entries = Lists.newArrayList();

public Builder<K, V> put(K key, V value) {
this.entries.add(ImmutableMap.entryOf(key, value));
return this;
}

public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
this.entries.ensureCapacity(this.entries.size() + map.size());
for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
put(entry.getKey(), entry.getValue());
}
return this;
}

public ImmutableMap<K, V> build() {
return fromEntryList(this.entries);
}

private static <K, V> ImmutableMap<K, V> fromEntryList(List<Map.Entry<K, V>> entries) {
int size = entries.size();
switch (size) {
case 0:
return ImmutableMap.of();
case 1:
return new SingletonImmutableMap<K, V>(Iterables.<Map.Entry<K, V>>getOnlyElement(entries));
} 
Map.Entry[] arrayOfEntry = entries.<Map.Entry>toArray(new Map.Entry[entries.size()]);

return new RegularImmutableMap<K, V>((Map.Entry<?, ?>[])arrayOfEntry);
}
}

public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
if (map instanceof ImmutableMap && !(map instanceof ImmutableSortedMap)) {

ImmutableMap<K, V> kvMap = (ImmutableMap)map;
if (!kvMap.isPartialView()) {
return kvMap;
}
} 

Map.Entry[] arrayOfEntry = (Map.Entry[])map.entrySet().toArray((Object[])new Map.Entry[0]);
switch (arrayOfEntry.length) {
case 0:
return of();
case 1:
return new SingletonImmutableMap<K, V>(entryOf(arrayOfEntry[0].getKey(), arrayOfEntry[0].getValue()));
} 

for (int i = 0; i < arrayOfEntry.length; i++) {
K k = arrayOfEntry[i].getKey();
V v = arrayOfEntry[i].getValue();
arrayOfEntry[i] = entryOf(k, v);
} 
return new RegularImmutableMap<K, V>((Map.Entry<?, ?>[])arrayOfEntry);
}

public final V put(K k, V v) {
throw new UnsupportedOperationException();
}

public final V remove(Object o) {
throw new UnsupportedOperationException();
}

public final void putAll(Map<? extends K, ? extends V> map) {
throw new UnsupportedOperationException();
}

public final void clear() {
throw new UnsupportedOperationException();
}

public boolean isEmpty() {
return (size() == 0);
}

public boolean containsKey(@Nullable Object key) {
return (get(key) != null);
}

public boolean equals(@Nullable Object object) {
if (object == this) {
return true;
}
if (object instanceof Map) {
Map<?, ?> that = (Map<?, ?>)object;
return entrySet().equals(that.entrySet());
} 
return false;
}

public int hashCode() {
return entrySet().hashCode();
}

public String toString() {
return Maps.toStringImpl(this);
}

static class SerializedForm
implements Serializable
{
private final Object[] keys;
private final Object[] values;
private static final long serialVersionUID = 0L;

SerializedForm(ImmutableMap<?, ?> map) {
this.keys = new Object[map.size()];
this.values = new Object[map.size()];
int i = 0;
for (Map.Entry<?, ?> entry : map.entrySet()) {
this.keys[i] = entry.getKey();
this.values[i] = entry.getValue();
i++;
} 
}
Object readResolve() {
ImmutableMap.Builder<Object, Object> builder = new ImmutableMap.Builder<Object, Object>();
return createMap(builder);
}
Object createMap(ImmutableMap.Builder<Object, Object> builder) {
for (int i = 0; i < this.keys.length; i++) {
builder.put(this.keys[i], this.values[i]);
}
return builder.build();
}
}

Object writeReplace() {
return new SerializedForm(this);
}

public abstract boolean containsValue(@Nullable Object paramObject);

public abstract V get(@Nullable Object paramObject);

public abstract ImmutableSet<Map.Entry<K, V>> entrySet();

public abstract ImmutableSet<K> keySet();

public abstract ImmutableCollection<V> values();

abstract boolean isPartialView();
}


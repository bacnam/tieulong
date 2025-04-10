package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public abstract class ImmutableMultimap<K, V>
implements Multimap<K, V>, Serializable
{
final transient ImmutableMap<K, ? extends ImmutableCollection<V>> map;
final transient int size;
private transient ImmutableCollection<Map.Entry<K, V>> entries;
private transient ImmutableMultiset<K> keys;
private transient ImmutableCollection<V> values;
private static final long serialVersionUID = 0L;

public static <K, V> ImmutableMultimap<K, V> of() {
return ImmutableListMultimap.of();
}

public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1) {
return ImmutableListMultimap.of(k1, v1);
}

public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2) {
return ImmutableListMultimap.of(k1, v1, k2, v2);
}

public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3);
}

public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4);
}

public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
}

public static <K, V> Builder<K, V> builder() {
return new Builder<K, V>();
}

private static class BuilderMultimap<K, V>
extends AbstractMultimap<K, V>
{
private static final long serialVersionUID = 0L;

BuilderMultimap() {
super(new LinkedHashMap<K, Collection<V>>());
}
Collection<V> createCollection() {
return Lists.newArrayList();
}
}

private static class SortedKeyBuilderMultimap<K, V>
extends AbstractMultimap<K, V>
{
private static final long serialVersionUID = 0L;

SortedKeyBuilderMultimap(Comparator<? super K> keyComparator, Multimap<K, V> multimap) {
super(new TreeMap<K, Collection<V>>(keyComparator));
putAll(multimap);
}
Collection<V> createCollection() {
return Lists.newArrayList();
}
}

public static class Builder<K, V>
{
Multimap<K, V> builderMultimap = new ImmutableMultimap.BuilderMultimap<K, V>();

Comparator<? super V> valueComparator;

public Builder<K, V> put(K key, V value) {
this.builderMultimap.put((K)Preconditions.checkNotNull(key), (V)Preconditions.checkNotNull(value));
return this;
}

public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
Collection<V> valueList = this.builderMultimap.get((K)Preconditions.checkNotNull(key));
for (V value : values) {
valueList.add((V)Preconditions.checkNotNull(value));
}
return this;
}

public Builder<K, V> putAll(K key, V... values) {
return putAll(key, Arrays.asList(values));
}

public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet()) {
putAll(entry.getKey(), entry.getValue());
}
return this;
}

@Beta
public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
this.builderMultimap = new ImmutableMultimap.SortedKeyBuilderMultimap<K, V>((Comparator<? super K>)Preconditions.checkNotNull(keyComparator), this.builderMultimap);

return this;
}

@Beta
public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
this.valueComparator = (Comparator<? super V>)Preconditions.checkNotNull(valueComparator);
return this;
}

public ImmutableMultimap<K, V> build() {
if (this.valueComparator != null) {
for (Collection<V> values : (Iterable<Collection<V>>)this.builderMultimap.asMap().values()) {
List<V> list = (List<V>)values;
Collections.sort(list, this.valueComparator);
} 
}
return ImmutableMultimap.copyOf(this.builderMultimap);
}
}

public static <K, V> ImmutableMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
if (multimap instanceof ImmutableMultimap) {

ImmutableMultimap<K, V> kvMultimap = (ImmutableMultimap)multimap;

if (!kvMultimap.isPartialView()) {
return kvMultimap;
}
} 
return ImmutableListMultimap.copyOf(multimap);
}

@GwtIncompatible("java serialization is not supported")
static class FieldSettersHolder
{
static final Serialization.FieldSetter<ImmutableMultimap> MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");

static final Serialization.FieldSetter<ImmutableMultimap> SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "size");
}

ImmutableMultimap(ImmutableMap<K, ? extends ImmutableCollection<V>> map, int size) {
this.map = map;
this.size = size;
}

public ImmutableCollection<V> removeAll(Object key) {
throw new UnsupportedOperationException();
}

public ImmutableCollection<V> replaceValues(K key, Iterable<? extends V> values) {
throw new UnsupportedOperationException();
}

public void clear() {
throw new UnsupportedOperationException();
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

boolean isPartialView() {
return this.map.isPartialView();
}

public boolean containsEntry(@Nullable Object key, @Nullable Object value) {
Collection<V> values = this.map.get(key);
return (values != null && values.contains(value));
}

public boolean containsKey(@Nullable Object key) {
return this.map.containsKey(key);
}

public boolean containsValue(@Nullable Object value) {
for (Collection<V> valueCollection : this.map.values()) {
if (valueCollection.contains(value)) {
return true;
}
} 
return false;
}

public boolean isEmpty() {
return (this.size == 0);
}

public int size() {
return this.size;
}

public boolean equals(@Nullable Object object) {
if (object instanceof Multimap) {
Multimap<?, ?> that = (Multimap<?, ?>)object;
return this.map.equals(that.asMap());
} 
return false;
}

public int hashCode() {
return this.map.hashCode();
}

public String toString() {
return this.map.toString();
}

public ImmutableSet<K> keySet() {
return this.map.keySet();
}

public ImmutableMap<K, Collection<V>> asMap() {
return (ImmutableMap)this.map;
}

public ImmutableCollection<Map.Entry<K, V>> entries() {
ImmutableCollection<Map.Entry<K, V>> result = this.entries;
return (result == null) ? (this.entries = new EntryCollection<K, V>(this)) : result;
}

private static class EntryCollection<K, V>
extends ImmutableCollection<Map.Entry<K, V>> {
final ImmutableMultimap<K, V> multimap;
private static final long serialVersionUID = 0L;

EntryCollection(ImmutableMultimap<K, V> multimap) {
this.multimap = multimap;
}

public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
final Iterator<? extends Map.Entry<K, ? extends ImmutableCollection<V>>> mapIterator = this.multimap.map.entrySet().iterator();

return new UnmodifiableIterator<Map.Entry<K, V>>()
{
K key;
Iterator<V> valueIterator;

public boolean hasNext() {
return ((this.key != null && this.valueIterator.hasNext()) || mapIterator.hasNext());
}

public Map.Entry<K, V> next() {
if (this.key == null || !this.valueIterator.hasNext()) {
Map.Entry<K, ? extends ImmutableCollection<V>> entry = mapIterator.next();

this.key = entry.getKey();
this.valueIterator = ((ImmutableCollection<V>)entry.getValue()).iterator();
} 
return Maps.immutableEntry(this.key, this.valueIterator.next());
}
};
}

boolean isPartialView() {
return this.multimap.isPartialView();
}

public int size() {
return this.multimap.size();
}

public boolean contains(Object object) {
if (object instanceof Map.Entry) {
Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
return this.multimap.containsEntry(entry.getKey(), entry.getValue());
} 
return false;
}
}

public ImmutableMultiset<K> keys() {
ImmutableMultiset<K> result = this.keys;
return (result == null) ? (this.keys = createKeys()) : result;
}

private ImmutableMultiset<K> createKeys() {
ImmutableMultiset.Builder<K> builder = ImmutableMultiset.builder();

for (Map.Entry<K, ? extends ImmutableCollection<V>> entry : this.map.entrySet()) {
builder.addCopies(entry.getKey(), ((ImmutableCollection)entry.getValue()).size());
}
return builder.build();
}

public ImmutableCollection<V> values() {
ImmutableCollection<V> result = this.values;
return (result == null) ? (this.values = new Values<V>(this)) : result;
}
public abstract ImmutableCollection<V> get(K paramK);

private static class Values<V> extends ImmutableCollection<V> { final ImmutableMultimap<?, V> multimap;

Values(ImmutableMultimap<?, V> multimap) {
this.multimap = multimap;
}
private static final long serialVersionUID = 0L;
public UnmodifiableIterator<V> iterator() {
final Iterator<? extends Map.Entry<?, V>> entryIterator = this.multimap.entries().iterator();

return new UnmodifiableIterator<V>()
{
public boolean hasNext() {
return entryIterator.hasNext();
}

public V next() {
return (V)((Map.Entry)entryIterator.next()).getValue();
}
};
}

public int size() {
return this.multimap.size();
}

boolean isPartialView() {
return true;
} }

}


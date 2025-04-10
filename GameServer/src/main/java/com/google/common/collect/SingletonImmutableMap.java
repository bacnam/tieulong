package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
final class SingletonImmutableMap<K, V>
extends ImmutableMap<K, V>
{
final transient K singleKey;
final transient V singleValue;
private transient Map.Entry<K, V> entry;
private transient ImmutableSet<Map.Entry<K, V>> entrySet;
private transient ImmutableSet<K> keySet;
private transient ImmutableCollection<V> values;

SingletonImmutableMap(K singleKey, V singleValue) {
this.singleKey = singleKey;
this.singleValue = singleValue;
}

SingletonImmutableMap(Map.Entry<K, V> entry) {
this.entry = entry;
this.singleKey = entry.getKey();
this.singleValue = entry.getValue();
}

private Map.Entry<K, V> entry() {
Map.Entry<K, V> e = this.entry;
return (e == null) ? (this.entry = Maps.<K, V>immutableEntry(this.singleKey, this.singleValue)) : e;
}

public V get(@Nullable Object key) {
return this.singleKey.equals(key) ? this.singleValue : null;
}

public int size() {
return 1;
}

public boolean isEmpty() {
return false;
}

public boolean containsKey(@Nullable Object key) {
return this.singleKey.equals(key);
}

public boolean containsValue(@Nullable Object value) {
return this.singleValue.equals(value);
}

boolean isPartialView() {
return false;
}

public ImmutableSet<Map.Entry<K, V>> entrySet() {
ImmutableSet<Map.Entry<K, V>> es = this.entrySet;
return (es == null) ? (this.entrySet = ImmutableSet.of(entry())) : es;
}

public ImmutableSet<K> keySet() {
ImmutableSet<K> ks = this.keySet;
return (ks == null) ? (this.keySet = ImmutableSet.of(this.singleKey)) : ks;
}

public ImmutableCollection<V> values() {
ImmutableCollection<V> v = this.values;
return (v == null) ? (this.values = new Values<V>(this.singleValue)) : v;
}

private static class Values<V>
extends ImmutableCollection<V> {
final V singleValue;

Values(V singleValue) {
this.singleValue = singleValue;
}

public boolean contains(Object object) {
return this.singleValue.equals(object);
}

public boolean isEmpty() {
return false;
}

public int size() {
return 1;
}

public UnmodifiableIterator<V> iterator() {
return Iterators.singletonIterator(this.singleValue);
}

boolean isPartialView() {
return true;
}
}

public boolean equals(@Nullable Object object) {
if (object == this) {
return true;
}
if (object instanceof Map) {
Map<?, ?> that = (Map<?, ?>)object;
if (that.size() != 1) {
return false;
}
Map.Entry<?, ?> entry = that.entrySet().iterator().next();
return (this.singleKey.equals(entry.getKey()) && this.singleValue.equals(entry.getValue()));
} 

return false;
}

public int hashCode() {
return this.singleKey.hashCode() ^ this.singleValue.hashCode();
}

public String toString() {
return '{' + this.singleKey.toString() + '=' + this.singleValue.toString() + '}';
}
}


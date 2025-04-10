package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableBiMap<K, V>
extends ImmutableMap<K, V>
implements BiMap<K, V>
{
private static final ImmutableBiMap<Object, Object> EMPTY_IMMUTABLE_BIMAP = new EmptyBiMap();

public static <K, V> ImmutableBiMap<K, V> of() {
return (ImmutableBiMap)EMPTY_IMMUTABLE_BIMAP;
}

public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1) {
return new RegularImmutableBiMap<K, V>(ImmutableMap.of(k1, v1));
}

public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2) {
return new RegularImmutableBiMap<K, V>(ImmutableMap.of(k1, v1, k2, v2));
}

public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
return new RegularImmutableBiMap<K, V>(ImmutableMap.of(k1, v1, k2, v2, k3, v3));
}

public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
return new RegularImmutableBiMap<K, V>(ImmutableMap.of(k1, v1, k2, v2, k3, v3, k4, v4));
}

public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
return new RegularImmutableBiMap<K, V>(ImmutableMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
}

public static <K, V> Builder<K, V> builder() {
return new Builder<K, V>();
}

public static final class Builder<K, V>
extends ImmutableMap.Builder<K, V>
{
public Builder<K, V> put(K key, V value) {
super.put(key, value);
return this;
}

public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
super.putAll(map);
return this;
}

public ImmutableBiMap<K, V> build() {
ImmutableMap<K, V> map = super.build();
if (map.isEmpty()) {
return ImmutableBiMap.of();
}
return new RegularImmutableBiMap<K, V>(map);
}
}

public static <K, V> ImmutableBiMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
if (map instanceof ImmutableBiMap) {

ImmutableBiMap<K, V> bimap = (ImmutableBiMap)map;

if (!bimap.isPartialView()) {
return bimap;
}
} 

if (map.isEmpty()) {
return of();
}

ImmutableMap<K, V> immutableMap = ImmutableMap.copyOf(map);
return new RegularImmutableBiMap<K, V>(immutableMap);
}

public boolean containsKey(@Nullable Object key) {
return delegate().containsKey(key);
}

public boolean containsValue(@Nullable Object value) {
return inverse().containsKey(value);
}

public ImmutableSet<Map.Entry<K, V>> entrySet() {
return delegate().entrySet();
}

public V get(@Nullable Object key) {
return delegate().get(key);
}

public ImmutableSet<K> keySet() {
return delegate().keySet();
}

public ImmutableSet<V> values() {
return inverse().keySet();
}

public V forcePut(K key, V value) {
throw new UnsupportedOperationException();
}

public boolean isEmpty() {
return delegate().isEmpty();
}

public int size() {
return delegate().size();
}

public boolean equals(@Nullable Object object) {
return (object == this || delegate().equals(object));
}

public int hashCode() {
return delegate().hashCode();
}

public String toString() {
return delegate().toString();
}

static class EmptyBiMap
extends ImmutableBiMap<Object, Object>
{
ImmutableMap<Object, Object> delegate() {
return ImmutableMap.of();
}
public ImmutableBiMap<Object, Object> inverse() {
return this;
}
boolean isPartialView() {
return false;
}
Object readResolve() {
return ImmutableBiMap.EMPTY_IMMUTABLE_BIMAP;
}
}

private static class SerializedForm
extends ImmutableMap.SerializedForm
{
private static final long serialVersionUID = 0L;

SerializedForm(ImmutableBiMap<?, ?> bimap) {
super(bimap);
}
Object readResolve() {
ImmutableBiMap.Builder<Object, Object> builder = new ImmutableBiMap.Builder<Object, Object>();
return createMap(builder);
}
}

Object writeReplace() {
return new SerializedForm(this);
}

abstract ImmutableMap<K, V> delegate();

public abstract ImmutableBiMap<V, K> inverse();
}


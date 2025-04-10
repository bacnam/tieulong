package javolution.util;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import javolution.lang.Realtime;
import javolution.util.function.Equalities;
import javolution.util.function.Equality;
import javolution.util.internal.map.sorted.AtomicSortedMapImpl;
import javolution.util.internal.map.sorted.FastSortedMapImpl;
import javolution.util.internal.map.sorted.SharedSortedMapImpl;
import javolution.util.internal.map.sorted.UnmodifiableSortedMapImpl;
import javolution.util.service.MapService;
import javolution.util.service.SortedMapService;

public class FastSortedMap<K, V>
extends FastMap<K, V>
implements SortedMap<K, V>
{
private static final long serialVersionUID = 1536L;

public FastSortedMap() {
this(Equalities.STANDARD);
}

public FastSortedMap(Equality<? super K> keyComparator) {
this(keyComparator, Equalities.STANDARD);
}

public FastSortedMap(Equality<? super K> keyComparator, Equality<? super V> valueComparator) {
super((MapService<K, V>)new FastSortedMapImpl(keyComparator, valueComparator));
}

protected FastSortedMap(SortedMapService<K, V> service) {
super((MapService<K, V>)service);
}

public FastSortedMap<K, V> atomic() {
return new FastSortedMap((SortedMapService<K, V>)new AtomicSortedMapImpl(service()));
}

public FastSortedMap<K, V> shared() {
return new FastSortedMap((SortedMapService<K, V>)new SharedSortedMapImpl(service()));
}

public FastSortedMap<K, V> unmodifiable() {
return new FastSortedMap((SortedMapService<K, V>)new UnmodifiableSortedMapImpl(service()));
}

public FastSortedSet<Map.Entry<K, V>> entrySet() {
return new FastSortedSet<Map.Entry<K, V>>(service().entrySet());
}

public FastSortedSet<K> keySet() {
return new FastSortedSet<K>(service().keySet());
}

public FastSortedMap<K, V> subMap(K fromKey, K toKey) {
return new FastSortedMap(service().subMap(fromKey, toKey));
}

public FastSortedMap<K, V> headMap(K toKey) {
return new FastSortedMap(service().subMap(firstKey(), toKey));
}

public FastSortedMap<K, V> tailMap(K fromKey) {
return new FastSortedMap(service().subMap(fromKey, lastKey()));
}

@Realtime(limit = Realtime.Limit.LOG_N)
public boolean containsKey(Object key) {
return super.containsKey(key);
}

@Realtime(limit = Realtime.Limit.LOG_N)
public V get(Object key) {
return super.get(key);
}

@Realtime(limit = Realtime.Limit.LOG_N)
public V put(K key, V value) {
return super.put(key, value);
}

@Realtime(limit = Realtime.Limit.LOG_N)
public V remove(Object key) {
return super.remove(key);
}

@Realtime(limit = Realtime.Limit.LOG_N)
public V putIfAbsent(K key, V value) {
return super.putIfAbsent(key, value);
}

@Realtime(limit = Realtime.Limit.LOG_N)
public boolean remove(Object key, Object value) {
return super.remove(key, value);
}

@Realtime(limit = Realtime.Limit.LOG_N)
public boolean replace(K key, V oldValue, V newValue) {
return super.replace(key, oldValue, newValue);
}

@Realtime(limit = Realtime.Limit.LOG_N)
public V replace(K key, V value) {
return super.replace(key, value);
}

public K firstKey() {
return (K)service().firstKey();
}

public K lastKey() {
return (K)service().lastKey();
}

public Comparator<? super K> comparator() {
return (Comparator<? super K>)keySet().comparator();
}

public FastSortedMap<K, V> putAll(FastMap<? extends K, ? extends V> that) {
return (FastSortedMap<K, V>)super.putAll(that);
}

protected SortedMapService<K, V> service() {
return (SortedMapService<K, V>)super.service();
}
}


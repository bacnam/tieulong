package javolution.util.internal.map.sorted;

import java.util.Iterator;
import java.util.Map;
import javolution.util.function.Equality;
import javolution.util.internal.map.MapView;
import javolution.util.internal.table.sorted.FastSortedTableImpl;
import javolution.util.service.SortedMapService;

public class FastSortedMapImpl<K, V>
extends SortedMapView<K, V>
{
private static final long serialVersionUID = 1536L;
private final Equality<? super K> keyComparator;
private FastSortedTableImpl<Map.Entry<K, V>> entries = new FastSortedTableImpl((Equality)new MapView.EntryComparator(this));

private final Equality<? super V> valueComparator;

public FastSortedMapImpl(Equality<? super K> keyComparator, Equality<? super V> valueComparator) {
super((SortedMapService<K, V>)null);
this.keyComparator = keyComparator;
this.valueComparator = valueComparator;
}

public boolean containsKey(Object key) {
return this.entries.contains(new MapEntryImpl<Object, Object>(key, null));
}

public K firstKey() {
return (K)((Map.Entry)this.entries.getFirst()).getKey();
}

public V get(Object key) {
int i = this.entries.indexOf(new MapEntryImpl<Object, Object>(key, null));
return (i >= 0) ? (V)((Map.Entry)this.entries.get(i)).getValue() : null;
}

public Iterator<Map.Entry<K, V>> iterator() {
return this.entries.iterator();
}

public Equality<? super K> keyComparator() {
return this.keyComparator;
}

public K lastKey() {
return (K)((Map.Entry)this.entries.getLast()).getKey();
}

public V put(K key, V value) {
MapEntryImpl<K, V> entry = new MapEntryImpl<K, V>(key, value);
int i = this.entries.positionOf(entry);
if (i < size()) {
Map.Entry<K, V> e = (Map.Entry<K, V>)this.entries.get(i);
if (keyComparator().areEqual(key, e.getKey())) {
V previous = e.getValue();
e.setValue(value);
return previous;
} 
} 
this.entries.add(i, entry);
return null;
}

public V remove(Object key) {
int i = this.entries.indexOf(new MapEntryImpl<Object, Object>(key, null));
if (i < 0) return null; 
Map.Entry<K, V> e = (Map.Entry<K, V>)this.entries.get(i);
V previous = e.getValue();
this.entries.remove(i);
return previous;
}

public Equality<? super V> valueComparator() {
return this.valueComparator;
}
}


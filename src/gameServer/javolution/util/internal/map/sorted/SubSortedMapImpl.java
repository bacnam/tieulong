package javolution.util.internal.map.sorted;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import javolution.util.function.Equality;
import javolution.util.service.SortedMapService;

public class SubSortedMapImpl<K, V>
extends SortedMapView<K, V>
{
private static final long serialVersionUID = 1536L;
private final K from;
private final K to;

private class IteratorImpl
implements Iterator<Map.Entry<K, V>>
{
private boolean ahead;
private final Equality<? super K> cmp = SubSortedMapImpl.this.keyComparator();
private Map.Entry<K, V> next;
private final Iterator<Map.Entry<K, V>> targetIterator = SubSortedMapImpl.this.target().iterator();

public boolean hasNext() {
if (this.ahead) return true; 
while (this.targetIterator.hasNext()) {
this.next = this.targetIterator.next();
if (SubSortedMapImpl.this.from != null && this.cmp.compare(this.next.getKey(), SubSortedMapImpl.this.from) < 0)
continue;  if (SubSortedMapImpl.this.to != null && this.cmp.compare(this.next.getKey(), SubSortedMapImpl.this.to) >= 0)
break;  this.ahead = true;
return true;
} 
return false;
}

public Map.Entry<K, V> next() {
hasNext();
this.ahead = false;
return this.next;
}

public void remove() {
this.targetIterator.remove();
}

private IteratorImpl() {}
}

public SubSortedMapImpl(SortedMapService<K, V> target, K from, K to) {
super(target);
if (from != null && to != null && keyComparator().compare(from, to) > 0) {
throw new IllegalArgumentException("from: " + from + ", to: " + to);
}
this.from = from;
this.to = to;
}

public boolean containsKey(Object key) {
Equality<? super K> cmp = keyComparator();
if (this.from != null && cmp.compare(key, this.from) < 0) return false; 
if (this.to != null && cmp.compare(key, this.to) >= 0) return false; 
return target().containsKey(key);
}

public K firstKey() {
if (this.from == null) return (K)target().firstKey(); 
Iterator<Map.Entry<K, V>> it = iterator();
if (!it.hasNext()) throw new NoSuchElementException(); 
return (K)((Map.Entry)it.next()).getKey();
}

public V get(Object key) {
Equality<? super K> cmp = keyComparator();
if (this.from != null && cmp.compare(key, this.from) < 0) return null; 
if (this.to != null && cmp.compare(key, this.to) >= 0) return null; 
return (V)target().get(key);
}

public Iterator<Map.Entry<K, V>> iterator() {
return new IteratorImpl();
}

public Equality<? super K> keyComparator() {
return target().keyComparator();
}

public K lastKey() {
if (this.to == null) return (K)target().lastKey(); 
Iterator<Map.Entry<K, V>> it = iterator();
if (!it.hasNext()) throw new NoSuchElementException(); 
Map.Entry<K, V> last = it.next();
while (it.hasNext()) {
last = it.next();
}
return last.getKey();
}

public V put(K key, V value) {
Equality<? super K> cmp = keyComparator();
if (this.from != null && cmp.compare(key, this.from) < 0) throw new IllegalArgumentException("Key: " + key + " outside of this sub-map bounds");

if (this.to != null && cmp.compare(key, this.to) >= 0) throw new IllegalArgumentException("Key: " + key + " outside of this sub-map bounds");

return (V)target().put(key, value);
}

public V remove(Object key) {
Equality<? super K> cmp = keyComparator();
if (this.from != null && cmp.compare(key, this.from) < 0) return null; 
if (this.to != null && cmp.compare(key, this.to) >= 0) return null; 
return (V)target().remove(key);
}

public Equality<? super V> valueComparator() {
return target().valueComparator();
}
}


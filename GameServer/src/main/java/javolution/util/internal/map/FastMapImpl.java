package javolution.util.internal.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import javolution.util.function.Equality;
import javolution.util.service.MapService;

public class FastMapImpl<K, V>
extends MapView<K, V>
{
private static final long serialVersionUID = 1536L;
transient MapEntryImpl<K, V> firstEntry = null;
transient FractalMapImpl fractal = new FractalMapImpl();
transient MapEntryImpl<K, V> freeEntry = new MapEntryImpl<K, V>();
final Equality<? super K> keyComparator;
transient MapEntryImpl<K, V> lastEntry = null;

transient int size;
final Equality<? super V> valueComparator;

public FastMapImpl(Equality<? super K> keyComparator, Equality<? super V> valueComparator) {
super(null);
this.keyComparator = keyComparator;
this.valueComparator = valueComparator;
}

public void clear() {
this.firstEntry = null;
this.lastEntry = null;
this.fractal = new FractalMapImpl();
this.size = 0;
}

public FastMapImpl<K, V> clone() {
FastMapImpl<K, V> copy = new FastMapImpl(keyComparator(), valueComparator());

copy.putAll((Map<? extends K, ? extends V>)this);
return copy;
}

public boolean containsKey(Object key) {
return (this.fractal.getEntry(key, this.keyComparator.hashCodeOf(key)) != null);
}

public V get(Object key) {
MapEntryImpl<K, V> entry = this.fractal.getEntry(key, this.keyComparator.hashCodeOf(key));

if (entry == null) return null; 
return entry.value;
}

public Iterator<Map.Entry<K, V>> iterator() {
return new Iterator<Map.Entry<K, V>>() {
MapEntryImpl<K, V> current;
MapEntryImpl<K, V> next = FastMapImpl.this.firstEntry;

public boolean hasNext() {
return (this.next != null);
}

public Map.Entry<K, V> next() {
if (this.next == null) throw new NoSuchElementException(); 
this.current = this.next;
this.next = this.next.next;
return this.current;
}

public void remove() {
if (this.current == null) throw new IllegalStateException(); 
FastMapImpl.this.fractal.removeEntry(this.current.key, this.current.hash);
FastMapImpl.this.detachEntry(this.current);
FastMapImpl.this.size--;
}
};
}

public Equality<? super K> keyComparator() {
return this.keyComparator;
}

public V put(K key, V value) {
int hash = this.keyComparator.hashCodeOf(key);
MapEntryImpl<K, V> tmp = this.fractal.addEntry(this.freeEntry, key, hash);
if (tmp == this.freeEntry) {
this.freeEntry = new MapEntryImpl<K, V>();
attachEntry(tmp);
this.size++;
tmp.value = value;
return null;
} 
V oldValue = tmp.value;
tmp.value = value;
return oldValue;
}

public V putIfAbsent(K key, V value) {
int hash = this.keyComparator.hashCodeOf(key);
MapEntryImpl<K, V> tmp = this.fractal.addEntry(this.freeEntry, key, hash);
if (tmp == this.freeEntry) {
this.freeEntry = new MapEntryImpl<K, V>();
attachEntry(tmp);
this.size++;
tmp.value = value;
return null;
} 
return tmp.value;
}

public V remove(Object key) {
MapEntryImpl<K, V> entry = this.fractal.removeEntry(key, this.keyComparator.hashCodeOf(key));

if (entry == null) return null; 
detachEntry(entry);
this.size--;
return entry.value;
}

public boolean remove(Object key, Object value) {
int hash = this.keyComparator.hashCodeOf(key);
MapEntryImpl<K, V> entry = this.fractal.getEntry(key, hash);
if (entry == null) return false; 
if (!this.valueComparator.areEqual(entry.value, value)) return false; 
this.fractal.removeEntry(key, hash);
detachEntry(entry);
this.size--;
return true;
}

public V replace(K key, V value) {
MapEntryImpl<K, V> entry = this.fractal.getEntry(key, this.keyComparator.hashCodeOf(key));

if (entry == null) return null; 
V oldValue = entry.value;
entry.value = value;
return oldValue;
}

public boolean replace(K key, V oldValue, V newValue) {
MapEntryImpl<K, V> entry = this.fractal.getEntry(key, this.keyComparator.hashCodeOf(key));

if (entry == null) return false; 
if (!this.valueComparator.areEqual(entry.value, oldValue)) return false; 
entry.value = newValue;
return true;
}

public int size() {
return this.size;
}

public MapService<K, V>[] split(int n) {
return (MapService<K, V>[])new MapService[] { this };
}

public Equality<? super V> valueComparator() {
return this.valueComparator;
}

private void attachEntry(MapEntryImpl<K, V> entry) {
if (this.lastEntry != null) {
this.lastEntry.next = entry;
entry.previous = this.lastEntry;
} 
this.lastEntry = entry;
if (this.firstEntry == null) {
this.firstEntry = entry;
}
}

private void detachEntry(MapEntryImpl<K, V> entry) {
if (entry == this.firstEntry) {
this.firstEntry = entry.next;
}
if (entry == this.lastEntry) {
this.lastEntry = entry.previous;
}
MapEntryImpl<K, V> previous = entry.previous;
MapEntryImpl<K, V> next = entry.next;
if (previous != null) {
previous.next = next;
}
if (next != null) {
next.previous = previous;
}
}

private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
s.defaultReadObject();
this.fractal = new FractalMapImpl();
this.freeEntry = new MapEntryImpl<K, V>();
int n = s.readInt();
for (int i = 0; i < n; i++) {
put((K)s.readObject(), (V)s.readObject());
}
}

private void writeObject(ObjectOutputStream s) throws IOException {
s.defaultWriteObject();
s.writeInt(this.size);
Iterator<Map.Entry<K, V>> it = iterator();
while (it.hasNext()) {
Map.Entry<K, V> e = it.next();
s.writeObject(e.getKey());
s.writeObject(e.getValue());
} 
}
}


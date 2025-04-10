package javolution.util.internal.map;

import java.util.Iterator;
import java.util.Map;
import javolution.util.function.Equality;
import javolution.util.service.MapService;

public class UnmodifiableMapImpl<K, V>
extends MapView<K, V>
{
private static final long serialVersionUID = 1536L;

private class IteratorImpl
implements Iterator<Map.Entry<K, V>>
{
private final Iterator<Map.Entry<K, V>> targetIterator = UnmodifiableMapImpl.this.target().iterator();

public boolean hasNext() {
return this.targetIterator.hasNext();
}

public Map.Entry<K, V> next() {
return this.targetIterator.next();
}

public void remove() {
throw new UnsupportedOperationException("Read-Only Map.");
}

private IteratorImpl() {}
}

public UnmodifiableMapImpl(MapService<K, V> target) {
super(target);
}

public void clear() {
throw new UnsupportedOperationException("Unmodifiable");
}

public boolean containsKey(Object key) {
return target().containsKey(key);
}

public V get(Object key) {
return (V)target().get(key);
}

public Iterator<Map.Entry<K, V>> iterator() {
return new IteratorImpl();
}

public Equality<? super K> keyComparator() {
return target().keyComparator();
}

public V put(K key, V value) {
throw new UnsupportedOperationException("Unmodifiable");
}

public V remove(Object key) {
throw new UnsupportedOperationException("Unmodifiable");
}

public MapService<K, V> threadSafe() {
return this;
}

public Equality<? super V> valueComparator() {
return target().valueComparator();
}
}


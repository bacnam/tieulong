package javolution.util.internal.map;

import java.util.Iterator;
import java.util.Map;
import javolution.util.function.Consumer;
import javolution.util.function.Equality;
import javolution.util.service.MapService;

public class SequentialMapImpl<K, V>
extends MapView<K, V>
{
private static final long serialVersionUID = 1536L;

public SequentialMapImpl(MapService<K, V> target) {
super(target);
}

public boolean containsKey(Object key) {
return target().containsKey(key);
}

public V get(Object key) {
return (V)target().get(key);
}

public Iterator<Map.Entry<K, V>> iterator() {
return target().iterator();
}

public Equality<? super K> keyComparator() {
return target().keyComparator();
}

public void perform(Consumer<MapService<K, V>> action, MapService<K, V> view) {
action.accept(view);
}

public V put(K key, V value) {
return (V)target().put(key, value);
}

public V remove(Object key) {
return (V)target().remove(key);
}

public void update(Consumer<MapService<K, V>> action, MapService<K, V> view) {
action.accept(view);
}

public Equality<? super V> valueComparator() {
return target().valueComparator();
}
}


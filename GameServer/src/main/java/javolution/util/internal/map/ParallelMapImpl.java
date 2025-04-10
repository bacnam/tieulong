package javolution.util.internal.map;

import java.util.Iterator;
import java.util.Map;
import javolution.context.ConcurrentContext;
import javolution.util.function.Consumer;
import javolution.util.function.Equality;
import javolution.util.service.MapService;

public class ParallelMapImpl<K, V>
extends MapView<K, V>
{
private static final long serialVersionUID = 1536L;

public ParallelMapImpl(MapService<K, V> target) {
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

public void perform(final Consumer<MapService<K, V>> action, MapService<K, V> view) {
ConcurrentContext ctx = ConcurrentContext.enter();
try {
int concurrency = ctx.getConcurrency();
MapService[] arrayOfMapService = (MapService[])view.split(concurrency + 1);
for (int i = 1; i < arrayOfMapService.length; i++) {
final MapService<K, V> subView = arrayOfMapService[i];
ctx.execute(new Runnable()
{
public void run() {
ParallelMapImpl.this.target().perform(action, subView);
}
});
} 
target().perform(action, arrayOfMapService[0]);
} finally {

ctx.exit();
} 
}

public V put(K key, V value) {
return (V)target().put(key, value);
}

public V remove(Object key) {
return (V)target().remove(key);
}

public void update(final Consumer<MapService<K, V>> action, MapService<K, V> view) {
ConcurrentContext ctx = ConcurrentContext.enter();
try {
int concurrency = ctx.getConcurrency();
MapService[] arrayOfMapService = (MapService[])view.threadSafe().split(concurrency + 1);

for (int i = 1; i < arrayOfMapService.length; i++) {
final MapService<K, V> subView = arrayOfMapService[i];
ctx.execute(new Runnable()
{
public void run() {
ParallelMapImpl.this.target().update(action, subView);
}
});
} 
target().perform(action, arrayOfMapService[0]);
} finally {

ctx.exit();
} 
}

public Equality<? super V> valueComparator() {
return target().valueComparator();
}
}


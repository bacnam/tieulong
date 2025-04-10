package javolution.util.service;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import javolution.util.function.Equality;
import javolution.util.function.Splittable;

public interface MapService<K, V> extends Map<K, V>, ConcurrentMap<K, V>, Splittable<MapService<K, V>>, Serializable, Cloneable {
  MapService<K, V> clone() throws CloneNotSupportedException;

  SetService<Map.Entry<K, V>> entrySet();

  Iterator<Map.Entry<K, V>> iterator();

  Equality<? super K> keyComparator();

  SetService<K> keySet();

  MapService<K, V> threadSafe();

  Equality<? super V> valueComparator();

  CollectionService<V> values();
}


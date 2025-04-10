package javolution.util.internal.map;

import java.util.Map;

public final class MapEntryImpl<K, V>
implements Map.Entry<K, V>
{
int hash;
K key;
MapEntryImpl<K, V> next;
MapEntryImpl<K, V> previous;
V value;

public K getKey() {
return this.key;
}

public V getValue() {
return this.value;
}

public V setValue(V value) {
V oldValue = this.value;
this.value = value;
return oldValue;
}

public String toString() {
return (new StringBuilder()).append(this.key).append("=").append(this.value).toString();
}
}


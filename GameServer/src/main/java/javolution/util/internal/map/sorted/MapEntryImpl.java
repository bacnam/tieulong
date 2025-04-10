package javolution.util.internal.map.sorted;

import java.io.Serializable;
import java.util.Map;

public final class MapEntryImpl<K, V>
implements Map.Entry<K, V>, Serializable
{
private static final long serialVersionUID = 1536L;
K key;
V value;

public MapEntryImpl(K key, V value) {
this.key = key;
this.value = value;
}

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


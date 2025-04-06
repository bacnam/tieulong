package javolution.util.service;

import java.util.Map;
import java.util.SortedMap;

public interface SortedMapService<K, V> extends MapService<K, V>, SortedMap<K, V> {
  SortedSetService<Map.Entry<K, V>> entrySet();
  
  SortedMapService<K, V> headMap(K paramK);
  
  SortedSetService<K> keySet();
  
  SortedMapService<K, V> subMap(K paramK1, K paramK2);
  
  SortedMapService<K, V> tailMap(K paramK);
  
  SortedMapService<K, V> threadSafe();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/service/SortedMapService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
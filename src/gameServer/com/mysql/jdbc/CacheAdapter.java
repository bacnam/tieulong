package com.mysql.jdbc;

import java.util.Set;

public interface CacheAdapter<K, V> {
  V get(K paramK);

  void put(K paramK, V paramV);

  void invalidate(K paramK);

  void invalidateAll(Set<K> paramSet);

  void invalidateAll();
}


package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.SortedMap;

@Beta
@GwtCompatible
public interface SortedMapDifference<K, V> extends MapDifference<K, V> {
  SortedMap<K, V> entriesOnlyOnLeft();

  SortedMap<K, V> entriesOnlyOnRight();

  SortedMap<K, V> entriesInCommon();

  SortedMap<K, MapDifference.ValueDifference<V>> entriesDiffering();
}


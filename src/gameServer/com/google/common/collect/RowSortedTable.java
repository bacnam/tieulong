package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

@GwtCompatible
@Beta
public interface RowSortedTable<R, C, V> extends Table<R, C, V> {
  SortedSet<R> rowKeySet();
  
  SortedMap<R, Map<C, V>> rowMap();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/google/common/collect/RowSortedTable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
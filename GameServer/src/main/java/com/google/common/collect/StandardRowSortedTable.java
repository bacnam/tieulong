package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

@GwtCompatible
class StandardRowSortedTable<R, C, V>
extends StandardTable<R, C, V>
implements RowSortedTable<R, C, V>
{
private transient SortedSet<R> rowKeySet;
private transient RowSortedMap rowMap;
private static final long serialVersionUID = 0L;

StandardRowSortedTable(SortedMap<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
super(backingMap, factory);
}

private SortedMap<R, Map<C, V>> sortedBackingMap() {
return (SortedMap<R, Map<C, V>>)this.backingMap;
}

public SortedSet<R> rowKeySet() {
SortedSet<R> result = this.rowKeySet;
return (result == null) ? (this.rowKeySet = new RowKeySortedSet()) : result;
}

private class RowKeySortedSet
extends StandardTable<R, C, V>.RowKeySet implements SortedSet<R> {
public Comparator<? super R> comparator() {
return StandardRowSortedTable.this.sortedBackingMap().comparator();
}
private RowKeySortedSet() {}

public R first() {
return (R)StandardRowSortedTable.this.sortedBackingMap().firstKey();
}

public R last() {
return (R)StandardRowSortedTable.this.sortedBackingMap().lastKey();
}

public SortedSet<R> headSet(R toElement) {
Preconditions.checkNotNull(toElement);
return (new StandardRowSortedTable<R, Object, Object>(StandardRowSortedTable.this.sortedBackingMap().headMap(toElement), StandardRowSortedTable.this.factory)).rowKeySet();
}

public SortedSet<R> subSet(R fromElement, R toElement) {
Preconditions.checkNotNull(fromElement);
Preconditions.checkNotNull(toElement);
return (new StandardRowSortedTable<R, Object, Object>(StandardRowSortedTable.this.sortedBackingMap().subMap(fromElement, toElement), StandardRowSortedTable.this.factory)).rowKeySet();
}

public SortedSet<R> tailSet(R fromElement) {
Preconditions.checkNotNull(fromElement);
return (new StandardRowSortedTable<R, Object, Object>(StandardRowSortedTable.this.sortedBackingMap().tailMap(fromElement), StandardRowSortedTable.this.factory)).rowKeySet();
}
}

public SortedMap<R, Map<C, V>> rowMap() {
RowSortedMap result = this.rowMap;
return (result == null) ? (this.rowMap = new RowSortedMap()) : result;
}

private class RowSortedMap extends StandardTable<R, C, V>.RowMap implements SortedMap<R, Map<C, V>> { private RowSortedMap() {}

public Comparator<? super R> comparator() {
return StandardRowSortedTable.this.sortedBackingMap().comparator();
}

public R firstKey() {
return (R)StandardRowSortedTable.this.sortedBackingMap().firstKey();
}

public R lastKey() {
return (R)StandardRowSortedTable.this.sortedBackingMap().lastKey();
}

public SortedMap<R, Map<C, V>> headMap(R toKey) {
Preconditions.checkNotNull(toKey);
return (new StandardRowSortedTable<R, C, V>(StandardRowSortedTable.this.sortedBackingMap().headMap(toKey), StandardRowSortedTable.this.factory)).rowMap();
}

public SortedMap<R, Map<C, V>> subMap(R fromKey, R toKey) {
Preconditions.checkNotNull(fromKey);
Preconditions.checkNotNull(toKey);
return (new StandardRowSortedTable<R, C, V>(StandardRowSortedTable.this.sortedBackingMap().subMap(fromKey, toKey), StandardRowSortedTable.this.factory)).rowMap();
}

public SortedMap<R, Map<C, V>> tailMap(R fromKey) {
Preconditions.checkNotNull(fromKey);
return (new StandardRowSortedTable<R, C, V>(StandardRowSortedTable.this.sortedBackingMap().tailMap(fromKey), StandardRowSortedTable.this.factory)).rowMap();
} }

}


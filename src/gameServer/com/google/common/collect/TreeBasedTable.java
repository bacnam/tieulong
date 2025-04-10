package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
@Beta
public class TreeBasedTable<R, C, V>
extends StandardRowSortedTable<R, C, V>
{
private final Comparator<? super C> columnComparator;
private static final long serialVersionUID = 0L;

private static class Factory<C, V>
implements Supplier<TreeMap<C, V>>, Serializable
{
final Comparator<? super C> comparator;
private static final long serialVersionUID = 0L;

Factory(Comparator<? super C> comparator) {
this.comparator = comparator;
}

public TreeMap<C, V> get() {
return new TreeMap<C, V>(this.comparator);
}
}

public static <R extends Comparable, C extends Comparable, V> TreeBasedTable<R, C, V> create() {
return new TreeBasedTable<R, C, V>(Ordering.natural(), Ordering.natural());
}

public static <R, C, V> TreeBasedTable<R, C, V> create(Comparator<? super R> rowComparator, Comparator<? super C> columnComparator) {
Preconditions.checkNotNull(rowComparator);
Preconditions.checkNotNull(columnComparator);
return new TreeBasedTable<R, C, V>(rowComparator, columnComparator);
}

public static <R, C, V> TreeBasedTable<R, C, V> create(TreeBasedTable<R, C, ? extends V> table) {
TreeBasedTable<R, C, V> result = new TreeBasedTable<R, C, V>(table.rowComparator(), table.columnComparator());

result.putAll(table);
return result;
}

TreeBasedTable(Comparator<? super R> rowComparator, Comparator<? super C> columnComparator) {
super(new TreeMap<R, Map<C, V>>(rowComparator), (Supplier)new Factory<C, Object>(columnComparator));

this.columnComparator = columnComparator;
}

public Comparator<? super R> rowComparator() {
return rowKeySet().comparator();
}

public Comparator<? super C> columnComparator() {
return this.columnComparator;
}

public SortedMap<C, V> row(R rowKey) {
return new TreeRow(rowKey);
}

private class TreeRow extends StandardTable<R, C, V>.Row implements SortedMap<C, V> {
@Nullable
final C lowerBound;

TreeRow(R rowKey) {
this(rowKey, null, null);
} @Nullable
final C upperBound; transient SortedMap<C, V> wholeRow;
TreeRow(@Nullable R rowKey, @Nullable C lowerBound, C upperBound) {
super(rowKey);
this.lowerBound = lowerBound;
this.upperBound = upperBound;
Preconditions.checkArgument((lowerBound == null || upperBound == null || compare(lowerBound, upperBound) <= 0));
}

public Comparator<? super C> comparator() {
return TreeBasedTable.this.columnComparator();
}

int compare(Object a, Object b) {
Comparator<Object> cmp = (Comparator)comparator();
return cmp.compare(a, b);
}

boolean rangeContains(@Nullable Object o) {
return (o != null && (this.lowerBound == null || compare(this.lowerBound, o) <= 0) && (this.upperBound == null || compare(this.upperBound, o) > 0));
}

public SortedMap<C, V> subMap(C fromKey, C toKey) {
Preconditions.checkArgument((rangeContains(Preconditions.checkNotNull(fromKey)) && rangeContains(Preconditions.checkNotNull(toKey))));

return new TreeRow(this.rowKey, fromKey, toKey);
}

public SortedMap<C, V> headMap(C toKey) {
Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(toKey)));
return new TreeRow(this.rowKey, this.lowerBound, toKey);
}

public SortedMap<C, V> tailMap(C fromKey) {
Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(fromKey)));
return new TreeRow(this.rowKey, fromKey, this.upperBound);
}

public C firstKey() {
SortedMap<C, V> backing = backingRowMap();
if (backing == null) {
throw new NoSuchElementException();
}
return backingRowMap().firstKey();
}

public C lastKey() {
SortedMap<C, V> backing = backingRowMap();
if (backing == null) {
throw new NoSuchElementException();
}
return backingRowMap().lastKey();
}

SortedMap<C, V> wholeRow() {
if (this.wholeRow == null || (this.wholeRow.isEmpty() && TreeBasedTable.this.backingMap.containsKey(this.rowKey)))
{
this.wholeRow = (SortedMap<C, V>)TreeBasedTable.this.backingMap.get(this.rowKey);
}
return this.wholeRow;
}

SortedMap<C, V> backingRowMap() {
return (SortedMap<C, V>)super.backingRowMap();
}

SortedMap<C, V> computeBackingRowMap() {
SortedMap<C, V> map = wholeRow();
if (map != null) {
if (this.lowerBound != null) {
map = map.tailMap(this.lowerBound);
}
if (this.upperBound != null) {
map = map.headMap(this.upperBound);
}
return map;
} 
return null;
}

void maintainEmptyInvariant() {
if (wholeRow() != null && this.wholeRow.isEmpty()) {
TreeBasedTable.this.backingMap.remove(this.rowKey);
this.wholeRow = null;
this.backingRowMap = null;
} 
}

public boolean containsKey(Object key) {
return (rangeContains(key) && super.containsKey(key));
}

public V put(C key, V value) {
Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(key)));
return super.put(key, value);
}
}

public SortedSet<R> rowKeySet() {
return super.rowKeySet();
}

public SortedMap<R, Map<C, V>> rowMap() {
return super.rowMap();
}

public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
return super.contains(rowKey, columnKey);
}

public boolean containsColumn(@Nullable Object columnKey) {
return super.containsColumn(columnKey);
}

public boolean containsRow(@Nullable Object rowKey) {
return super.containsRow(rowKey);
}

public boolean containsValue(@Nullable Object value) {
return super.containsValue(value);
}

public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
return super.get(rowKey, columnKey);
}

public boolean equals(@Nullable Object obj) {
return super.equals(obj);
}

public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
return super.remove(rowKey, columnKey);
}

Iterator<C> createColumnKeyIterator() {
return new MergingIterator<C>(Iterables.transform(this.backingMap.values(), new Function<Map<C, V>, Iterator<C>>()
{

public Iterator<C> apply(Map<C, V> input)
{
return input.keySet().iterator();
}
}), columnComparator());
}

private static class MergingIterator<T>
extends AbstractIterator<T>
{
private final Queue<PeekingIterator<T>> queue;

private final Comparator<? super T> comparator;

private T lastValue = null;

public MergingIterator(Iterable<? extends Iterator<T>> iterators, Comparator<? super T> itemComparator) {
this.comparator = itemComparator;

Comparator<PeekingIterator<T>> heapComparator = (Comparator)new Comparator<PeekingIterator<PeekingIterator<T>>>()
{
public int compare(PeekingIterator<T> o1, PeekingIterator<T> o2)
{
return TreeBasedTable.MergingIterator.this.comparator.compare(o1.peek(), o2.peek());
}
};

this.queue = new PriorityQueue<PeekingIterator<T>>(Math.max(1, Iterables.size(iterators)), heapComparator);

for (Iterator<T> iterator : iterators) {
if (iterator.hasNext()) {
this.queue.add(Iterators.peekingIterator(iterator));
}
} 
}

protected T computeNext() {
while (!this.queue.isEmpty()) {
PeekingIterator<T> nextIter = this.queue.poll();

T next = nextIter.next();
boolean duplicate = (this.lastValue != null && this.comparator.compare(next, this.lastValue) == 0);

if (nextIter.hasNext()) {
this.queue.add(nextIter);
}

if (!duplicate) {
this.lastValue = next;
return this.lastValue;
} 
} 

this.lastValue = null;
return endOfData();
}
}
}


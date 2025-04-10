package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
public final class SortedMaps
{
public static <K, V1, V2> SortedMap<K, V2> transformValues(SortedMap<K, V1> fromMap, final Function<? super V1, V2> function) {
Preconditions.checkNotNull(function);
Maps.EntryTransformer<K, V1, V2> transformer = new Maps.EntryTransformer<K, V1, V2>()
{
public V2 transformEntry(K key, V1 value)
{
return (V2)function.apply(value);
}
};
return transformEntries(fromMap, transformer);
}

public static <K, V1, V2> SortedMap<K, V2> transformEntries(SortedMap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
return new TransformedEntriesSortedMap<K, V1, V2>(fromMap, transformer);
}

static class TransformedEntriesSortedMap<K, V1, V2>
extends Maps.TransformedEntriesMap<K, V1, V2>
implements SortedMap<K, V2> {
protected SortedMap<K, V1> fromMap() {
return (SortedMap<K, V1>)this.fromMap;
}

TransformedEntriesSortedMap(SortedMap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
super(fromMap, transformer);
}

public Comparator<? super K> comparator() {
return fromMap().comparator();
}

public K firstKey() {
return fromMap().firstKey();
}

public SortedMap<K, V2> headMap(K toKey) {
return SortedMaps.transformEntries(fromMap().headMap(toKey), this.transformer);
}

public K lastKey() {
return fromMap().lastKey();
}

public SortedMap<K, V2> subMap(K fromKey, K toKey) {
return SortedMaps.transformEntries(fromMap().subMap(fromKey, toKey), this.transformer);
}

public SortedMap<K, V2> tailMap(K fromKey) {
return SortedMaps.transformEntries(fromMap().tailMap(fromKey), this.transformer);
}
}

public static <K, V> SortedMapDifference<K, V> difference(SortedMap<K, ? extends V> left, Map<? extends K, ? extends V> right) {
Comparator<? super K> comparator = orNaturalOrder(left.comparator());
SortedMap<K, V> onlyOnLeft = Maps.newTreeMap(comparator);
SortedMap<K, V> onlyOnRight = Maps.newTreeMap(comparator);
onlyOnRight.putAll(right);
SortedMap<K, V> onBoth = Maps.newTreeMap(comparator);
SortedMap<K, MapDifference.ValueDifference<V>> differences = Maps.newTreeMap(comparator);

boolean eq = true;

for (Map.Entry<? extends K, ? extends V> entry : left.entrySet()) {
K leftKey = entry.getKey();
V leftValue = entry.getValue();
if (right.containsKey(leftKey)) {
V rightValue = onlyOnRight.remove(leftKey);
if (Objects.equal(leftValue, rightValue)) {
onBoth.put(leftKey, leftValue); continue;
} 
eq = false;
differences.put(leftKey, Maps.ValueDifferenceImpl.create(leftValue, rightValue));

continue;
} 
eq = false;
onlyOnLeft.put(leftKey, leftValue);
} 

boolean areEqual = (eq && onlyOnRight.isEmpty());
return sortedMapDifference(areEqual, onlyOnLeft, onlyOnRight, onBoth, differences);
}

private static <K, V> SortedMapDifference<K, V> sortedMapDifference(boolean areEqual, SortedMap<K, V> onlyOnLeft, SortedMap<K, V> onlyOnRight, SortedMap<K, V> onBoth, SortedMap<K, MapDifference.ValueDifference<V>> differences) {
return new SortedMapDifferenceImpl<K, V>(areEqual, Collections.unmodifiableSortedMap(onlyOnLeft), Collections.unmodifiableSortedMap(onlyOnRight), Collections.unmodifiableSortedMap(onBoth), Collections.unmodifiableSortedMap(differences));
}

static class SortedMapDifferenceImpl<K, V>
extends Maps.MapDifferenceImpl<K, V>
implements SortedMapDifference<K, V>
{
SortedMapDifferenceImpl(boolean areEqual, SortedMap<K, V> onlyOnLeft, SortedMap<K, V> onlyOnRight, SortedMap<K, V> onBoth, SortedMap<K, MapDifference.ValueDifference<V>> differences) {
super(areEqual, onlyOnLeft, onlyOnRight, onBoth, differences);
}

public SortedMap<K, MapDifference.ValueDifference<V>> entriesDiffering() {
return (SortedMap<K, MapDifference.ValueDifference<V>>)super.entriesDiffering();
}

public SortedMap<K, V> entriesInCommon() {
return (SortedMap<K, V>)super.entriesInCommon();
}

public SortedMap<K, V> entriesOnlyOnLeft() {
return (SortedMap<K, V>)super.entriesOnlyOnLeft();
}

public SortedMap<K, V> entriesOnlyOnRight() {
return (SortedMap<K, V>)super.entriesOnlyOnRight();
}
}

static <E> Comparator<? super E> orNaturalOrder(@Nullable Comparator<? super E> comparator) {
if (comparator != null) {
return comparator;
}
return Ordering.natural();
}

@GwtIncompatible("untested")
public static <K, V> SortedMap<K, V> filterKeys(SortedMap<K, V> unfiltered, final Predicate<? super K> keyPredicate) {
Preconditions.checkNotNull(keyPredicate);
Predicate<Map.Entry<K, V>> entryPredicate = new Predicate<Map.Entry<K, V>>()
{
public boolean apply(Map.Entry<K, V> input) {
return keyPredicate.apply(input.getKey());
}
};
return filterEntries(unfiltered, entryPredicate);
}

@GwtIncompatible("untested")
public static <K, V> SortedMap<K, V> filterValues(SortedMap<K, V> unfiltered, final Predicate<? super V> valuePredicate) {
Preconditions.checkNotNull(valuePredicate);
Predicate<Map.Entry<K, V>> entryPredicate = new Predicate<Map.Entry<K, V>>()
{
public boolean apply(Map.Entry<K, V> input)
{
return valuePredicate.apply(input.getValue());
}
};
return filterEntries(unfiltered, entryPredicate);
}

@GwtIncompatible("untested")
public static <K, V> SortedMap<K, V> filterEntries(SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
Preconditions.checkNotNull(entryPredicate);
return (unfiltered instanceof FilteredSortedMap) ? filterFiltered((FilteredSortedMap<K, V>)unfiltered, entryPredicate) : new FilteredSortedMap<K, V>((SortedMap<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
}

private static <K, V> SortedMap<K, V> filterFiltered(FilteredSortedMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.predicate, entryPredicate);

return new FilteredSortedMap<K, V>(map.sortedMap(), predicate);
}

private static class FilteredSortedMap<K, V>
extends Maps.FilteredEntryMap<K, V>
implements SortedMap<K, V>
{
FilteredSortedMap(SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
super(unfiltered, entryPredicate);
}

SortedMap<K, V> sortedMap() {
return (SortedMap<K, V>)this.unfiltered;
}

public Comparator<? super K> comparator() {
return sortedMap().comparator();
}

public K firstKey() {
return keySet().iterator().next();
}

public K lastKey() {
SortedMap<K, V> headMap = sortedMap();

while (true) {
K key = headMap.lastKey();
if (apply(key, this.unfiltered.get(key))) {
return key;
}
headMap = sortedMap().headMap(key);
} 
}

public SortedMap<K, V> headMap(K toKey) {
return new FilteredSortedMap(sortedMap().headMap(toKey), this.predicate);
}

public SortedMap<K, V> subMap(K fromKey, K toKey) {
return new FilteredSortedMap(sortedMap().subMap(fromKey, toKey), this.predicate);
}

public SortedMap<K, V> tailMap(K fromKey) {
return new FilteredSortedMap(sortedMap().tailMap(fromKey), this.predicate);
}
}
}


package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

@GwtCompatible
final class SortedIterables
{
public static boolean hasSameComparator(Comparator<?> comparator, Iterable<?> elements) {
Comparator<?> comparator2;
Preconditions.checkNotNull(comparator);
Preconditions.checkNotNull(elements);

if (elements instanceof SortedSet) {
SortedSet<?> sortedSet = (SortedSet)elements;
comparator2 = sortedSet.comparator();
if (comparator2 == null) {
comparator2 = Ordering.natural();
}
} else if (elements instanceof SortedIterable) {
comparator2 = ((SortedIterable)elements).comparator();
} else {
comparator2 = null;
} 
return comparator.equals(comparator2);
}

public static <E> Collection<E> sortedUnique(Comparator<? super E> comparator, Iterator<E> elements) {
SortedSet<E> sortedSet = Sets.newTreeSet(comparator);
Iterators.addAll(sortedSet, elements);
return sortedSet;
}

public static <E> Collection<E> sortedUnique(Comparator<? super E> comparator, Iterable<E> elements) {
if (elements instanceof Multiset) {
elements = ((Multiset<E>)elements).elementSet();
}
if (elements instanceof Set) {
if (hasSameComparator(comparator, elements)) {
return (Set)elements;
}
List<E> list = Lists.newArrayList(elements);
Collections.sort(list, comparator);
return list;
} 
E[] array = (E[])Iterables.toArray(elements);
if (!hasSameComparator(comparator, elements)) {
Arrays.sort(array, comparator);
}
return uniquifySortedArray(comparator, array);
}

private static <E> Collection<E> uniquifySortedArray(Comparator<? super E> comparator, E[] array) {
if (array.length == 0) {
return Collections.emptySet();
}
int length = 1;
for (int i = 1; i < array.length; i++) {
int cmp = comparator.compare(array[i], array[length - 1]);
if (cmp != 0) {
array[length++] = array[i];
}
} 
if (length < array.length) {
array = ObjectArrays.arraysCopyOf(array, length);
}
return Arrays.asList(array);
}

public static <E> Collection<Multiset.Entry<E>> sortedCounts(Comparator<? super E> comparator, Iterator<E> elements) {
TreeMultiset<E> multiset = TreeMultiset.create(comparator);
Iterators.addAll(multiset, elements);
return multiset.entrySet();
}

public static <E> Collection<Multiset.Entry<E>> sortedCounts(Comparator<? super E> comparator, Iterable<E> elements) {
if (elements instanceof Multiset) {
Multiset<E> multiset1 = (Multiset<E>)elements;
if (hasSameComparator(comparator, elements)) {
return multiset1.entrySet();
}
List<Multiset.Entry<E>> entries = Lists.newArrayList(multiset1.entrySet());
Collections.sort(entries, Ordering.<E>from((Comparator)comparator).onResultOf(new Function<Multiset.Entry<E>, E>()
{
public E apply(Multiset.Entry<E> entry)
{
return entry.getElement();
}
}));
return entries;
}  if (elements instanceof Set) {
Collection<E> sortedElements;
if (hasSameComparator(comparator, elements)) {
sortedElements = (Collection<E>)elements;
} else {
List<E> list = Lists.newArrayList(elements);
Collections.sort(list, comparator);
sortedElements = list;
} 
return singletonEntries(sortedElements);
}  if (hasSameComparator(comparator, elements)) {
E current = null;
int currentCount = 0;
List<Multiset.Entry<E>> sortedEntries = Lists.newArrayList();
for (E e : elements) {
if (currentCount > 0) {
if (comparator.compare(current, e) == 0) {
currentCount++; continue;
} 
sortedEntries.add(Multisets.immutableEntry(current, currentCount));
current = e;
currentCount = 1;
continue;
} 
current = e;
currentCount = 1;
} 

if (currentCount > 0) {
sortedEntries.add(Multisets.immutableEntry(current, currentCount));
}
return sortedEntries;
} 
TreeMultiset<E> multiset = TreeMultiset.create(comparator);
Iterables.addAll(multiset, elements);
return multiset.entrySet();
}

static <E> Collection<Multiset.Entry<E>> singletonEntries(Collection<E> set) {
return Collections2.transform(set, new Function<E, Multiset.Entry<E>>()
{
public Multiset.Entry<E> apply(E elem) {
return Multisets.immutableEntry(elem, 1);
}
});
}
}


package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
final class RegularImmutableSortedSet<E>
extends ImmutableSortedSet<E>
{
private final transient ImmutableList<E> elements;

RegularImmutableSortedSet(ImmutableList<E> elements, Comparator<? super E> comparator) {
super(comparator);
this.elements = elements;
Preconditions.checkArgument(!elements.isEmpty());
}

public UnmodifiableIterator<E> iterator() {
return this.elements.iterator();
}

public boolean isEmpty() {
return false;
}

public int size() {
return this.elements.size();
}

public boolean contains(Object o) {
if (o == null) {
return false;
}
try {
return (binarySearch(o) >= 0);
} catch (ClassCastException e) {
return false;
} 
}

public boolean containsAll(Collection<?> targets) {
if (!SortedIterables.hasSameComparator(comparator(), targets) || targets.size() <= 1)
{
return super.containsAll(targets);
}

Iterator<E> thisIterator = iterator();
Iterator<?> thatIterator = targets.iterator();
Object target = thatIterator.next();

try {
while (thisIterator.hasNext()) {

int cmp = unsafeCompare(thisIterator.next(), target);

if (cmp == 0) {

if (!thatIterator.hasNext())
{
return true;
}

target = thatIterator.next(); continue;
} 
if (cmp > 0) {
return false;
}
} 
} catch (NullPointerException e) {
return false;
} catch (ClassCastException e) {
return false;
} 

return false;
}

private int binarySearch(Object key) {
Comparator<Object> unsafeComparator = (Comparator)this.comparator;

return Collections.binarySearch(this.elements, (E)key, (Comparator)unsafeComparator);
}

boolean isPartialView() {
return this.elements.isPartialView();
}

public Object[] toArray() {
return this.elements.toArray();
}

public <T> T[] toArray(T[] array) {
return (T[])this.elements.toArray((Object[])array);
}

public boolean equals(@Nullable Object object) {
if (object == this) {
return true;
}
if (!(object instanceof Set)) {
return false;
}

Set<?> that = (Set)object;
if (size() != that.size()) {
return false;
}

if (SortedIterables.hasSameComparator(this.comparator, that)) {
Iterator<?> otherIterator = that.iterator();
try {
Iterator<E> iterator = iterator();
while (iterator.hasNext()) {
Object element = iterator.next();
Object otherElement = otherIterator.next();
if (otherElement == null || unsafeCompare(element, otherElement) != 0)
{
return false;
}
} 
return true;
} catch (ClassCastException e) {
return false;
} catch (NoSuchElementException e) {
return false;
} 
} 
return containsAll(that);
}

public E first() {
return this.elements.get(0);
}

public E last() {
return this.elements.get(size() - 1);
}

ImmutableSortedSet<E> headSetImpl(E toElement, boolean inclusive) {
int index;
if (inclusive) {
index = SortedLists.binarySearch(this.elements, (E)Preconditions.checkNotNull(toElement), comparator(), SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
} else {

index = SortedLists.binarySearch(this.elements, (E)Preconditions.checkNotNull(toElement), comparator(), SortedLists.KeyPresentBehavior.FIRST_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
} 

return createSubset(0, index);
}

ImmutableSortedSet<E> subSetImpl(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
return tailSetImpl(fromElement, fromInclusive).headSetImpl(toElement, toInclusive);
}

ImmutableSortedSet<E> tailSetImpl(E fromElement, boolean inclusive) {
int index;
if (inclusive) {
index = SortedLists.binarySearch(this.elements, (E)Preconditions.checkNotNull(fromElement), comparator(), SortedLists.KeyPresentBehavior.FIRST_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
} else {

index = SortedLists.binarySearch(this.elements, (E)Preconditions.checkNotNull(fromElement), comparator(), SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
} 

return createSubset(index, size());
}

Comparator<Object> unsafeComparator() {
return (Comparator)this.comparator;
}

private ImmutableSortedSet<E> createSubset(int newFromIndex, int newToIndex) {
if (newFromIndex == 0 && newToIndex == size())
return this; 
if (newFromIndex < newToIndex) {
return new RegularImmutableSortedSet(this.elements.subList(newFromIndex, newToIndex), this.comparator);
}

return emptySet(this.comparator);
}

int indexOf(@Nullable Object target) {
int position;
if (target == null) {
return -1;
}

try {
position = SortedLists.binarySearch(this.elements, (E)target, comparator(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.INVERTED_INSERTION_INDEX);
}
catch (ClassCastException e) {
return -1;
} 

return (position >= 0 && this.elements.get(position).equals(target)) ? position : -1;
}

ImmutableList<E> createAsList() {
return new ImmutableSortedAsList<E>(this, this.elements);
}
}


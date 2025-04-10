package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
class EmptyImmutableSortedSet<E>
extends ImmutableSortedSet<E>
{
EmptyImmutableSortedSet(Comparator<? super E> comparator) {
super(comparator);
}

public int size() {
return 0;
}

public boolean isEmpty() {
return true;
}

public boolean contains(Object target) {
return false;
}

public UnmodifiableIterator<E> iterator() {
return Iterators.emptyIterator();
}

boolean isPartialView() {
return false;
}

private static final Object[] EMPTY_ARRAY = new Object[0];

public Object[] toArray() {
return EMPTY_ARRAY;
}

public <T> T[] toArray(T[] a) {
if (a.length > 0) {
a[0] = null;
}
return a;
}

public boolean containsAll(Collection<?> targets) {
return targets.isEmpty();
}

public boolean equals(@Nullable Object object) {
if (object instanceof Set) {
Set<?> that = (Set)object;
return that.isEmpty();
} 
return false;
}

public int hashCode() {
return 0;
}

public String toString() {
return "[]";
}

public E first() {
throw new NoSuchElementException();
}

public E last() {
throw new NoSuchElementException();
}

ImmutableSortedSet<E> headSetImpl(E toElement, boolean inclusive) {
return this;
}

ImmutableSortedSet<E> subSetImpl(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
return this;
}

ImmutableSortedSet<E> tailSetImpl(E fromElement, boolean inclusive) {
return this;
}

int indexOf(@Nullable Object target) {
return -1;
}
}


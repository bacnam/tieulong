package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Comparator;
import javax.annotation.Nullable;

final class EmptyImmutableSortedMultiset<E>
extends ImmutableSortedMultiset<E>
{
EmptyImmutableSortedMultiset(Comparator<? super E> comparator) {
super(comparator);
}

public Multiset.Entry<E> firstEntry() {
return null;
}

public Multiset.Entry<E> lastEntry() {
return null;
}

public int count(@Nullable Object element) {
return 0;
}

public int size() {
return 0;
}

ImmutableSortedSet<E> createElementSet() {
return ImmutableSortedSet.emptySet(comparator());
}

ImmutableSortedSet<E> createDescendingElementSet() {
return ImmutableSortedSet.emptySet(reverseComparator());
}

UnmodifiableIterator<Multiset.Entry<E>> descendingEntryIterator() {
return Iterators.emptyIterator();
}

UnmodifiableIterator<Multiset.Entry<E>> entryIterator() {
return Iterators.emptyIterator();
}

public ImmutableSortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
Preconditions.checkNotNull(upperBound);
Preconditions.checkNotNull(boundType);
return this;
}

public ImmutableSortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
Preconditions.checkNotNull(lowerBound);
Preconditions.checkNotNull(boundType);
return this;
}

int distinctElements() {
return 0;
}

boolean isPartialView() {
return false;
}
}


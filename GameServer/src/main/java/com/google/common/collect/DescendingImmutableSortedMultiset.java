package com.google.common.collect;

import javax.annotation.Nullable;

final class DescendingImmutableSortedMultiset<E>
        extends ImmutableSortedMultiset<E> {
    private final transient ImmutableSortedMultiset<E> forward;

    DescendingImmutableSortedMultiset(ImmutableSortedMultiset<E> forward) {
        super(forward.reverseComparator());
        this.forward = forward;
    }

    public int count(@Nullable Object element) {
        return this.forward.count(element);
    }

    public Multiset.Entry<E> firstEntry() {
        return this.forward.lastEntry();
    }

    public Multiset.Entry<E> lastEntry() {
        return this.forward.firstEntry();
    }

    public int size() {
        return this.forward.size();
    }

    ImmutableSortedSet<E> createElementSet() {
        return this.forward.createDescendingElementSet();
    }

    ImmutableSortedSet<E> createDescendingElementSet() {
        return this.forward.elementSet();
    }

    UnmodifiableIterator<Multiset.Entry<E>> descendingEntryIterator() {
        return this.forward.entryIterator();
    }

    public ImmutableSortedMultiset<E> descendingMultiset() {
        return this.forward;
    }

    public ImmutableSortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
        return this.forward.tailMultiset(upperBound, boundType).descendingMultiset();
    }

    public ImmutableSortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
        return this.forward.headMultiset(lowerBound, boundType).descendingMultiset();
    }

    UnmodifiableIterator<Multiset.Entry<E>> entryIterator() {
        return this.forward.descendingEntryIterator();
    }

    int distinctElements() {
        return this.forward.distinctElements();
    }

    boolean isPartialView() {
        return this.forward.isPartialView();
    }
}


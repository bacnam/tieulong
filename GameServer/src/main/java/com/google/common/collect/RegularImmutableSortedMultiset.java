package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

final class RegularImmutableSortedMultiset<E>
        extends ImmutableSortedMultiset<E> {
    final transient ImmutableList<CumulativeCountEntry<E>> entries;

    RegularImmutableSortedMultiset(Comparator<? super E> comparator, ImmutableList<CumulativeCountEntry<E>> entries) {
        super(comparator);
        this.entries = entries;
        assert !entries.isEmpty();
    }

    static <E> RegularImmutableSortedMultiset<E> createFromSorted(Comparator<? super E> comparator, List<? extends Multiset.Entry<E>> entries) {
        List<CumulativeCountEntry<E>> newEntries = Lists.newArrayListWithCapacity(entries.size());
        CumulativeCountEntry<E> previous = null;
        for (Multiset.Entry<E> entry : entries) {
            newEntries.add(previous = new CumulativeCountEntry<E>(entry.getElement(), entry.getCount(), previous));
        }

        return new RegularImmutableSortedMultiset<E>(comparator, ImmutableList.copyOf(newEntries));
    }

    ImmutableList<E> elementList() {
        return new TransformedImmutableList<CumulativeCountEntry<E>, E>(this.entries) {
            E transform(RegularImmutableSortedMultiset.CumulativeCountEntry<E> entry) {
                return entry.getElement();
            }
        };
    }

    ImmutableSortedSet<E> createElementSet() {
        return new RegularImmutableSortedSet<E>(elementList(), comparator());
    }

    ImmutableSortedSet<E> createDescendingElementSet() {
        return new RegularImmutableSortedSet<E>(elementList().reverse(), reverseComparator());
    }

    UnmodifiableIterator<Multiset.Entry<E>> entryIterator() {
        return this.entries.iterator();
    }

    UnmodifiableIterator<Multiset.Entry<E>> descendingEntryIterator() {
        return this.entries.reverse().iterator();
    }

    public CumulativeCountEntry<E> firstEntry() {
        return this.entries.get(0);
    }

    public CumulativeCountEntry<E> lastEntry() {
        return this.entries.get(this.entries.size() - 1);
    }

    public int size() {
        CumulativeCountEntry<E> firstEntry = firstEntry();
        CumulativeCountEntry<E> lastEntry = lastEntry();
        return Ints.saturatedCast(lastEntry.cumulativeCount - firstEntry.cumulativeCount + firstEntry.count);
    }

    int distinctElements() {
        return this.entries.size();
    }

    boolean isPartialView() {
        return this.entries.isPartialView();
    }

    public int count(@Nullable Object element) {
        if (element == null) {
            return 0;
        }
        try {
            int index = SortedLists.binarySearch(elementList(), (E) element, comparator(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.INVERTED_INSERTION_INDEX);

            return (index >= 0) ? ((CumulativeCountEntry) this.entries.get(index)).getCount() : 0;
        } catch (ClassCastException e) {
            return 0;
        }
    }

    public ImmutableSortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
        int index;
        switch (boundType) {
            case OPEN:
                index = SortedLists.binarySearch(elementList(), (E) Preconditions.checkNotNull(upperBound), comparator(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);

                return createSubMultiset(0, index);
            case CLOSED:
                index = SortedLists.<E>binarySearch(elementList(), (E) Preconditions.checkNotNull(upperBound), comparator(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER) + 1;
                return createSubMultiset(0, index);
        }
        throw new AssertionError();
    }

    public ImmutableSortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
        int index;
        switch (boundType) {
            case OPEN:
                index = SortedLists.<E>binarySearch(elementList(), (E) Preconditions.checkNotNull(lowerBound), comparator(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER) + 1;

                return createSubMultiset(index, distinctElements());
            case CLOSED:
                index = SortedLists.binarySearch(elementList(), (E) Preconditions.checkNotNull(lowerBound), comparator(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
                return createSubMultiset(index, distinctElements());
        }
        throw new AssertionError();
    }

    private ImmutableSortedMultiset<E> createSubMultiset(int newFromIndex, int newToIndex) {
        if (newFromIndex == 0 && newToIndex == this.entries.size())
            return this;
        if (newFromIndex >= newToIndex) {
            return emptyMultiset(comparator());
        }
        return new RegularImmutableSortedMultiset(comparator(), this.entries.subList(newFromIndex, newToIndex));
    }

    private static final class CumulativeCountEntry<E>
            extends Multisets.AbstractEntry<E> {
        final E element;
        final int count;
        final long cumulativeCount;

        CumulativeCountEntry(E element, int count, @Nullable CumulativeCountEntry<E> previous) {
            this.element = element;
            this.count = count;
            this.cumulativeCount = count + ((previous == null) ? 0L : previous.cumulativeCount);
        }

        public E getElement() {
            return this.element;
        }

        public int getCount() {
            return this.count;
        }
    }
}


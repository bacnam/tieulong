package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.*;

@GwtIncompatible("hasn't been tested yet")
abstract class ImmutableSortedMultiset<E>
        extends ImmutableSortedMultisetFauxverideShim<E>
        implements SortedMultiset<E> {
    private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();

    private static final ImmutableSortedMultiset<Comparable> NATURAL_EMPTY_MULTISET = new EmptyImmutableSortedMultiset<Comparable>(NATURAL_ORDER);

    private final transient Comparator<? super E> comparator;
    transient ImmutableSortedMultiset<E> descendingMultiset;
    private transient Comparator<? super E> reverseComparator;
    private transient ImmutableSortedSet<E> elementSet;

    ImmutableSortedMultiset(Comparator<? super E> comparator) {
        this.comparator = (Comparator<? super E>) Preconditions.checkNotNull(comparator);
    }

    public static <E> ImmutableSortedMultiset<E> of() {
        return (ImmutableSortedMultiset) NATURAL_EMPTY_MULTISET;
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E element) {
        return RegularImmutableSortedMultiset.createFromSorted((Comparator) NATURAL_ORDER, ImmutableList.of(Multisets.immutableEntry((E) Preconditions.checkNotNull(element), 1)));
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2) {
        return copyOf(Ordering.natural(), Arrays.asList((E[]) new Comparable[]{(Comparable) e1, (Comparable) e2}));
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3) {
        return copyOf(Ordering.natural(), Arrays.asList((E[]) new Comparable[]{(Comparable) e1, (Comparable) e2, (Comparable) e3}));
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4) {
        return copyOf(Ordering.natural(), Arrays.asList((E[]) new Comparable[]{(Comparable) e1, (Comparable) e2, (Comparable) e3, (Comparable) e4}));
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4, E e5) {
        return copyOf(Ordering.natural(), Arrays.asList((E[]) new Comparable[]{(Comparable) e1, (Comparable) e2, (Comparable) e3, (Comparable) e4, (Comparable) e5}));
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... remaining) {
        int size = remaining.length + 6;
        List<E> all = new ArrayList<E>(size);
        Collections.addAll(all, (E[]) new Comparable[]{(Comparable) e1, (Comparable) e2, (Comparable) e3, (Comparable) e4, (Comparable) e5, (Comparable) e6});
        Collections.addAll(all, remaining);
        return copyOf(Ordering.natural(), all);
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> copyOf(E[] elements) {
        return copyOf(Ordering.natural(), Arrays.asList(elements));
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Iterable<? extends E> elements) {
        Ordering<E> naturalOrder = Ordering.natural();
        return copyOf(naturalOrder, elements);
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Iterator<? extends E> elements) {
        Ordering<E> naturalOrder = Ordering.natural();
        return copyOfInternal(naturalOrder, elements);
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements) {
        Preconditions.checkNotNull(comparator);
        return copyOfInternal(comparator, elements);
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
        Preconditions.checkNotNull(comparator);
        return copyOfInternal(comparator, elements);
    }

    public static <E> ImmutableSortedMultiset<E> copyOfSorted(SortedMultiset<E> sortedMultiset) {
        Comparator<Comparable> comparator1;
        Comparator<? super E> comparator = sortedMultiset.comparator();
        if (comparator == null) {
            comparator1 = NATURAL_ORDER;
        }
        return copyOfInternal((Comparator) comparator1, sortedMultiset);
    }

    private static <E> ImmutableSortedMultiset<E> copyOfInternal(Comparator<? super E> comparator, Iterable<? extends E> iterable) {
        if (SortedIterables.hasSameComparator(comparator, iterable) && iterable instanceof ImmutableSortedMultiset) {

            ImmutableSortedMultiset<E> multiset = (ImmutableSortedMultiset) iterable;
            if (!multiset.isPartialView()) {
                return (ImmutableSortedMultiset) iterable;
            }
        }
        ImmutableList<Multiset.Entry<E>> entries = ImmutableList.copyOf(SortedIterables.sortedCounts(comparator, (Iterable) iterable));

        if (entries.isEmpty()) {
            return emptyMultiset(comparator);
        }
        verifyEntries(entries);
        return RegularImmutableSortedMultiset.createFromSorted(comparator, entries);
    }

    private static <E> ImmutableSortedMultiset<E> copyOfInternal(Comparator<? super E> comparator, Iterator<? extends E> iterator) {
        ImmutableList<Multiset.Entry<E>> entries = ImmutableList.copyOf(SortedIterables.sortedCounts(comparator, (Iterator) iterator));

        if (entries.isEmpty()) {
            return emptyMultiset(comparator);
        }
        verifyEntries(entries);
        return RegularImmutableSortedMultiset.createFromSorted(comparator, entries);
    }

    private static <E> void verifyEntries(Collection<Multiset.Entry<E>> entries) {
        for (Multiset.Entry<E> entry : entries) {
            Preconditions.checkNotNull(entry.getElement());
        }
    }

    static <E> ImmutableSortedMultiset<E> emptyMultiset(Comparator<? super E> comparator) {
        if (NATURAL_ORDER.equals(comparator)) {
            return (ImmutableSortedMultiset) NATURAL_EMPTY_MULTISET;
        }
        return new EmptyImmutableSortedMultiset<E>(comparator);
    }

    public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
        return new Builder<E>(comparator);
    }

    public static <E extends Comparable<E>> Builder<E> reverseOrder() {
        return new Builder<E>(Ordering.<Comparable>natural().reverse());
    }

    public static <E extends Comparable<E>> Builder<E> naturalOrder() {
        return new Builder<E>(Ordering.natural());
    }

    public Comparator<? super E> comparator() {
        return this.comparator;
    }

    Comparator<Object> unsafeComparator() {
        return (Comparator) this.comparator;
    }

    Comparator<? super E> reverseComparator() {
        Comparator<? super E> result = this.reverseComparator;
        if (result == null) {
            return this.reverseComparator = Ordering.from(this.comparator).reverse();
        }
        return result;
    }

    public ImmutableSortedSet<E> elementSet() {
        ImmutableSortedSet<E> result = this.elementSet;
        if (result == null) {
            return this.elementSet = createElementSet();
        }
        return result;
    }

    public ImmutableSortedMultiset<E> descendingMultiset() {
        ImmutableSortedMultiset<E> result = this.descendingMultiset;
        if (result == null) {
            return this.descendingMultiset = new DescendingImmutableSortedMultiset<E>(this);
        }
        return result;
    }

    public final Multiset.Entry<E> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    public Multiset.Entry<E> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    public ImmutableSortedMultiset<E> subMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) {
        return tailMultiset(lowerBound, lowerBoundType).headMultiset(upperBound, upperBoundType);
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }

    abstract ImmutableSortedSet<E> createElementSet();

    abstract ImmutableSortedSet<E> createDescendingElementSet();

    abstract UnmodifiableIterator<Multiset.Entry<E>> descendingEntryIterator();

    public abstract ImmutableSortedMultiset<E> headMultiset(E paramE, BoundType paramBoundType);

    public abstract ImmutableSortedMultiset<E> tailMultiset(E paramE, BoundType paramBoundType);

    public static class Builder<E>
            extends ImmutableMultiset.Builder<E> {
        private final Comparator<? super E> comparator;

        public Builder(Comparator<? super E> comparator) {
            super(TreeMultiset.create(comparator));
            this.comparator = (Comparator<? super E>) Preconditions.checkNotNull(comparator);
        }

        public Builder<E> add(E element) {
            super.add(element);
            return this;
        }

        public Builder<E> addCopies(E element, int occurrences) {
            super.addCopies(element, occurrences);
            return this;
        }

        public Builder<E> setCount(E element, int count) {
            super.setCount(element, count);
            return this;
        }

        public Builder<E> add(E... elements) {
            super.add(elements);
            return this;
        }

        public Builder<E> addAll(Iterable<? extends E> elements) {
            super.addAll(elements);
            return this;
        }

        public Builder<E> addAll(Iterator<? extends E> elements) {
            super.addAll(elements);
            return this;
        }

        public ImmutableSortedMultiset<E> build() {
            return ImmutableSortedMultiset.copyOf(this.comparator, this.contents);
        }
    }

    private static final class SerializedForm implements Serializable {
        Comparator comparator;
        Object[] elements;
        int[] counts;

        SerializedForm(SortedMultiset<?> multiset) {
            this.comparator = multiset.comparator();
            int n = multiset.entrySet().size();
            this.elements = new Object[n];
            this.counts = new int[n];
            int i = 0;
            for (Multiset.Entry<?> entry : multiset.entrySet()) {
                this.elements[i] = entry.getElement();
                this.counts[i] = entry.getCount();
                i++;
            }
        }

        Object readResolve() {
            int n = this.elements.length;
            ImmutableSortedMultiset.Builder<Object> builder = ImmutableSortedMultiset.orderedBy(this.comparator);
            for (int i = 0; i < n; i++) {
                builder.addCopies(this.elements[i], this.counts[i]);
            }
            return builder.build();
        }
    }
}


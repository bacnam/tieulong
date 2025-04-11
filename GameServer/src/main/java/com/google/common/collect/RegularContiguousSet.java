package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Collection;

@GwtCompatible(emulated = true)
final class RegularContiguousSet<C extends Comparable>
        extends ContiguousSet<C> {
    private static final long serialVersionUID = 0L;
    private final Range<C> range;

    RegularContiguousSet(Range<C> range, DiscreteDomain<C> domain) {
        super(domain);
        this.range = range;
    }

    private static boolean equalsOrThrow(Comparable<?> left, @Nullable Comparable<?> right) {
        return (right != null && Range.compareOrThrow(left, right) == 0);
    }

    ContiguousSet<C> headSetImpl(C toElement, boolean inclusive) {
        return this.range.intersection((Range) Ranges.upTo((Comparable<?>) toElement, BoundType.forBoolean(inclusive))).asSet(this.domain);
    }

    int indexOf(Object target) {
        return contains(target) ? (int) this.domain.distance(first(), (C) target) : -1;
    }

    ContiguousSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive) {
        return this.range.intersection((Range) Ranges.range((Comparable<?>) fromElement, BoundType.forBoolean(fromInclusive), (Comparable<?>) toElement, BoundType.forBoolean(toInclusive))).asSet(this.domain);
    }

    ContiguousSet<C> tailSetImpl(C fromElement, boolean inclusive) {
        return this.range.intersection((Range) Ranges.downTo((Comparable<?>) fromElement, BoundType.forBoolean(inclusive))).asSet(this.domain);
    }

    public UnmodifiableIterator<C> iterator() {
        return new AbstractLinkedIterator<C>((Comparable) first()) {
            final C last = RegularContiguousSet.this.last();

            protected C computeNext(C previous) {
                return RegularContiguousSet.equalsOrThrow((Comparable<?>) previous, (Comparable<?>) this.last) ? null : RegularContiguousSet.this.domain.next(previous);
            }
        };
    }

    boolean isPartialView() {
        return false;
    }

    public C first() {
        return this.range.lowerBound.leastValueAbove(this.domain);
    }

    public C last() {
        return this.range.upperBound.greatestValueBelow(this.domain);
    }

    public int size() {
        long distance = this.domain.distance(first(), last());
        return (distance >= 2147483647L) ? Integer.MAX_VALUE : ((int) distance + 1);
    }

    public boolean contains(Object object) {
        try {
            return this.range.contains((C) object);
        } catch (ClassCastException e) {
            return false;
        }
    }

    public boolean containsAll(Collection<?> targets) {
        try {
            return this.range.containsAll((Iterable) targets);
        } catch (ClassCastException e) {
            return false;
        }
    }

    public boolean isEmpty() {
        return false;
    }

    public Object[] toArray() {
        return ObjectArrays.toArrayImpl(this);
    }

    public <T> T[] toArray(T[] other) {
        return ObjectArrays.toArrayImpl(this, other);
    }

    public ContiguousSet<C> intersection(ContiguousSet<C> other) {
        Preconditions.checkNotNull(other);
        Preconditions.checkArgument(this.domain.equals(other.domain));
        if (other.isEmpty()) {
            return other;
        }
        Comparable<Comparable> comparable1 = (Comparable) Ordering.<Comparable>natural().max(first(), other.first());
        Comparable<Comparable> comparable2 = (Comparable) Ordering.<Comparable>natural().min(last(), other.last());
        return (comparable1.compareTo(comparable2) < 0) ? Ranges.<Comparable<Comparable>>closed(comparable1, comparable2).asSet(this.domain) : new EmptyContiguousSet<C>(this.domain);
    }

    public Range<C> range() {
        return range(BoundType.CLOSED, BoundType.CLOSED);
    }

    public Range<C> range(BoundType lowerBoundType, BoundType upperBoundType) {
        return (Range) Ranges.create(this.range.lowerBound.withLowerBoundType(lowerBoundType, this.domain), this.range.upperBound.withUpperBoundType(upperBoundType, this.domain));
    }

    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (object instanceof RegularContiguousSet) {
            RegularContiguousSet<?> that = (RegularContiguousSet) object;
            if (this.domain.equals(that.domain)) {
                return (first().equals(that.first()) && last().equals(that.last()));
            }
        }

        return super.equals(object);
    }

    public int hashCode() {
        return Sets.hashCodeImpl(this);
    }

    @GwtIncompatible("serialization")
    Object writeReplace() {
        return new SerializedForm<Comparable>(this.range, this.domain);
    }

    @GwtIncompatible("serialization")
    private static final class SerializedForm<C extends Comparable> implements Serializable {
        final Range<C> range;
        final DiscreteDomain<C> domain;

        private SerializedForm(Range<C> range, DiscreteDomain<C> domain) {
            this.range = range;
            this.domain = domain;
        }

        private Object readResolve() {
            return new RegularContiguousSet<C>(this.range, this.domain);
        }
    }
}


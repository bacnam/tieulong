package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.SortedSet;

@GwtCompatible
@Beta
public final class Range<C extends Comparable>
        implements Predicate<C>, Serializable {
    private static final long serialVersionUID = 0L;
    final Cut<C> lowerBound;
    final Cut<C> upperBound;

    Range(Cut<C> lowerBound, Cut<C> upperBound) {
        if (lowerBound.compareTo(upperBound) > 0) {
            throw new IllegalArgumentException("Invalid range: " + toString(lowerBound, upperBound));
        }

        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    private static String toString(Cut<?> lowerBound, Cut<?> upperBound) {
        StringBuilder sb = new StringBuilder(16);
        lowerBound.describeAsLowerBound(sb);
        sb.append('‥');
        upperBound.describeAsUpperBound(sb);
        return sb.toString();
    }

    private static <T> SortedSet<T> cast(Iterable<T> iterable) {
        return (SortedSet<T>) iterable;
    }

    static int compareOrThrow(Comparable<Comparable> left, Comparable right) {
        return left.compareTo(right);
    }

    public boolean hasLowerBound() {
        return (this.lowerBound != Cut.belowAll());
    }

    public C lowerEndpoint() {
        return this.lowerBound.endpoint();
    }

    public BoundType lowerBoundType() {
        return this.lowerBound.typeAsLowerBound();
    }

    public boolean hasUpperBound() {
        return (this.upperBound != Cut.aboveAll());
    }

    public C upperEndpoint() {
        return this.upperBound.endpoint();
    }

    public BoundType upperBoundType() {
        return this.upperBound.typeAsUpperBound();
    }

    public boolean isEmpty() {
        return this.lowerBound.equals(this.upperBound);
    }

    public boolean contains(C value) {
        Preconditions.checkNotNull(value);

        return (this.lowerBound.isLessThan(value) && !this.upperBound.isLessThan(value));
    }

    public boolean apply(C input) {
        return contains(input);
    }

    public boolean containsAll(Iterable<? extends C> values) {
        if (Iterables.isEmpty(values)) {
            return true;
        }

        if (values instanceof SortedSet) {
            SortedSet<? extends C> set = cast(values);
            Comparator<?> comparator = set.comparator();
            if (Ordering.<Comparable>natural().equals(comparator) || comparator == null) {
                return (contains(set.first()) && contains(set.last()));
            }
        }

        for (Comparable comparable : values) {
            if (!contains((C) comparable)) {
                return false;
            }
        }
        return true;
    }

    public boolean encloses(Range<C> other) {
        return (this.lowerBound.compareTo(other.lowerBound) <= 0 && this.upperBound.compareTo(other.upperBound) >= 0);
    }

    public Range<C> intersection(Range<C> other) {
        Cut<C> newLower = (Cut<C>) Ordering.<Comparable>natural().max(this.lowerBound, other.lowerBound);
        Cut<C> newUpper = (Cut<C>) Ordering.<Comparable>natural().min(this.upperBound, other.upperBound);
        return (Range) Ranges.create(newLower, newUpper);
    }

    public boolean isConnected(Range<C> other) {
        return (this.lowerBound.compareTo(other.upperBound) <= 0 && other.lowerBound.compareTo(this.upperBound) <= 0);
    }

    public Range<C> span(Range<C> other) {
        Cut<C> newLower = (Cut<C>) Ordering.<Comparable>natural().min(this.lowerBound, other.lowerBound);
        Cut<C> newUpper = (Cut<C>) Ordering.<Comparable>natural().max(this.upperBound, other.upperBound);
        return (Range) Ranges.create(newLower, newUpper);
    }

    @GwtCompatible(serializable = false)
    public ContiguousSet<C> asSet(DiscreteDomain<C> domain) {
        Preconditions.checkNotNull(domain);
        Range<C> effectiveRange = this;
        try {
            if (!hasLowerBound()) {
                effectiveRange = effectiveRange.intersection((Range) Ranges.atLeast((Comparable<?>) domain.minValue()));
            }

            if (!hasUpperBound()) {
                effectiveRange = effectiveRange.intersection((Range) Ranges.atMost((Comparable<?>) domain.maxValue()));
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException(e);
        }

        boolean empty = (effectiveRange.isEmpty() || compareOrThrow((Comparable) this.lowerBound.leastValueAbove(domain), (Comparable) this.upperBound.greatestValueBelow(domain)) > 0);

        return empty ? new EmptyContiguousSet<C>(domain) : new RegularContiguousSet<C>(effectiveRange, domain);
    }

    public Range<C> canonical(DiscreteDomain<C> domain) {
        Preconditions.checkNotNull(domain);
        Cut<C> lower = this.lowerBound.canonical(domain);
        Cut<C> upper = this.upperBound.canonical(domain);
        return (lower == this.lowerBound && upper == this.upperBound) ? this : (Range) Ranges.<Comparable<?>>create(lower, upper);
    }

    public boolean equals(@Nullable Object object) {
        if (object instanceof Range) {
            Range<?> other = (Range) object;
            return (this.lowerBound.equals(other.lowerBound) && this.upperBound.equals(other.upperBound));
        }

        return false;
    }

    public int hashCode() {
        return this.lowerBound.hashCode() * 31 + this.upperBound.hashCode();
    }

    public String toString() {
        return toString(this.lowerBound, this.upperBound);
    }
}


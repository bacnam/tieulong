package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

import java.util.Iterator;

@GwtCompatible
@Beta
public final class Ranges {
    static <C extends Comparable<?>> Range<C> create(Cut<C> lowerBound, Cut<C> upperBound) {
        return (Range) new Range<Comparable>(lowerBound, upperBound);
    }

    public static <C extends Comparable<?>> Range<C> open(C lower, C upper) {
        return create((Cut) Cut.aboveValue((Comparable) lower), (Cut) Cut.belowValue((Comparable) upper));
    }

    public static <C extends Comparable<?>> Range<C> closed(C lower, C upper) {
        return create((Cut) Cut.belowValue((Comparable) lower), (Cut) Cut.aboveValue((Comparable) upper));
    }

    public static <C extends Comparable<?>> Range<C> closedOpen(C lower, C upper) {
        return create((Cut) Cut.belowValue((Comparable) lower), (Cut) Cut.belowValue((Comparable) upper));
    }

    public static <C extends Comparable<?>> Range<C> openClosed(C lower, C upper) {
        return create((Cut) Cut.aboveValue((Comparable) lower), (Cut) Cut.aboveValue((Comparable) upper));
    }

    public static <C extends Comparable<?>> Range<C> range(C lower, BoundType lowerType, C upper, BoundType upperType) {
        Preconditions.checkNotNull(lowerType);
        Preconditions.checkNotNull(upperType);

        Cut<C> lowerBound = (lowerType == BoundType.OPEN) ? (Cut) Cut.<Comparable>aboveValue((Comparable) lower) : (Cut) Cut.<Comparable>belowValue((Comparable) lower);

        Cut<C> upperBound = (upperType == BoundType.OPEN) ? (Cut) Cut.<Comparable>belowValue((Comparable) upper) : (Cut) Cut.<Comparable>aboveValue((Comparable) upper);

        return create(lowerBound, upperBound);
    }

    public static <C extends Comparable<?>> Range<C> lessThan(C endpoint) {
        return create((Cut) Cut.belowAll(), (Cut) Cut.belowValue((Comparable) endpoint));
    }

    public static <C extends Comparable<?>> Range<C> atMost(C endpoint) {
        return create((Cut) Cut.belowAll(), (Cut) Cut.aboveValue((Comparable) endpoint));
    }

    public static <C extends Comparable<?>> Range<C> upTo(C endpoint, BoundType boundType) {
        switch (boundType) {
            case OPEN:
                return lessThan(endpoint);
            case CLOSED:
                return atMost(endpoint);
        }
        throw new AssertionError();
    }

    public static <C extends Comparable<?>> Range<C> greaterThan(C endpoint) {
        return create((Cut) Cut.aboveValue((Comparable) endpoint), (Cut) Cut.aboveAll());
    }

    public static <C extends Comparable<?>> Range<C> atLeast(C endpoint) {
        return create((Cut) Cut.belowValue((Comparable) endpoint), (Cut) Cut.aboveAll());
    }

    public static <C extends Comparable<?>> Range<C> downTo(C endpoint, BoundType boundType) {
        switch (boundType) {
            case OPEN:
                return greaterThan(endpoint);
            case CLOSED:
                return atLeast(endpoint);
        }
        throw new AssertionError();
    }

    public static <C extends Comparable<?>> Range<C> all() {
        return create((Cut) Cut.belowAll(), (Cut) Cut.aboveAll());
    }

    public static <C extends Comparable<?>> Range<C> singleton(C value) {
        return closed(value, value);
    }

    public static <C extends Comparable<?>> Range<C> encloseAll(Iterable<C> values) {
        Preconditions.checkNotNull(values);
        if (values instanceof ContiguousSet) {
            return ((ContiguousSet) values).range();
        }
        Iterator<C> valueIterator = values.iterator();
        Comparable comparable1 = (Comparable) Preconditions.checkNotNull(valueIterator.next());
        Comparable comparable2 = comparable1;
        while (valueIterator.hasNext()) {
            Comparable comparable = (Comparable) Preconditions.checkNotNull(valueIterator.next());
            comparable1 = (Comparable) Ordering.<Comparable>natural().min(comparable1, comparable);
            comparable2 = (Comparable) Ordering.<Comparable>natural().max(comparable2, comparable);
        }
        return closed((C) comparable1, (C) comparable2);
    }
}


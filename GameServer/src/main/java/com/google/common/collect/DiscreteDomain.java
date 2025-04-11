package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

import java.util.NoSuchElementException;

@GwtCompatible
@Beta
public abstract class DiscreteDomain<C extends Comparable> {
    public abstract C next(C paramC);

    public abstract C previous(C paramC);

    public abstract long distance(C paramC1, C paramC2);

    public C minValue() {
        throw new NoSuchElementException();
    }

    public C maxValue() {
        throw new NoSuchElementException();
    }
}


package com.mchange.v2.coalesce;

import java.util.Iterator;

class CoalescerIterator
        implements Iterator {
    Iterator inner;

    CoalescerIterator(Iterator paramIterator) {
        this.inner = paramIterator;
    }

    public boolean hasNext() {
        return this.inner.hasNext();
    }

    public Object next() {
        return this.inner.next();
    }

    public void remove() {
        throw new UnsupportedOperationException("Objects cannot be removed from a coalescer!");
    }
}


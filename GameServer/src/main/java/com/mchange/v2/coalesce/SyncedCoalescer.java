package com.mchange.v2.coalesce;

import java.util.Iterator;

class SyncedCoalescer
        implements Coalescer {
    Coalescer inner;

    public SyncedCoalescer(Coalescer paramCoalescer) {
        this.inner = paramCoalescer;
    }

    public synchronized Object coalesce(Object paramObject) {
        return this.inner.coalesce(paramObject);
    }

    public synchronized int countCoalesced() {
        return this.inner.countCoalesced();
    }

    public synchronized Iterator iterator() {
        return this.inner.iterator();
    }
}


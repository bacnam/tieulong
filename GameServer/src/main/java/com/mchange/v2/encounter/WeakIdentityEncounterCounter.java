package com.mchange.v2.encounter;

import com.mchange.v2.util.WeakIdentityHashMapFactory;

public class WeakIdentityEncounterCounter
        extends AbstractEncounterCounter {
    public WeakIdentityEncounterCounter() {
        super(WeakIdentityHashMapFactory.create());
    }
}


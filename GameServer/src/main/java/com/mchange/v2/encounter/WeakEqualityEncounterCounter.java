package com.mchange.v2.encounter;

import java.util.WeakHashMap;

public class WeakEqualityEncounterCounter
        extends AbstractEncounterCounter {
    public WeakEqualityEncounterCounter() {
        super(new WeakHashMap<Object, Object>());
    }
}


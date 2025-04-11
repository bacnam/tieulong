package com.mchange.v2.encounter;

import java.util.Map;

class AbstractEncounterCounter
        implements EncounterCounter {
    static final Long ONE = new Long(1L);
    Map m;

    AbstractEncounterCounter(Map paramMap) {
        this.m = paramMap;
    }

    public long encounter(Object paramObject) {
        Long long_2;
        long l;
        Long long_1 = (Long) this.m.get(paramObject);

        if (long_1 == null) {

            l = 0L;
            long_2 = ONE;
        } else {

            l = long_1.longValue();
            long_2 = new Long(l + 1L);
        }
        this.m.put(paramObject, long_2);
        return l;
    }

    public long reset(Object paramObject) {
        long l = encounter(paramObject);
        this.m.remove(paramObject);
        return l;
    }

    public void resetAll() {
        this.m.clear();
    }
}


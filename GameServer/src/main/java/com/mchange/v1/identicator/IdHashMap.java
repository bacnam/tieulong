package com.mchange.v1.identicator;

import java.util.HashMap;
import java.util.Map;

public final class IdHashMap
        extends IdMap
        implements Map {
    public IdHashMap(Identicator paramIdenticator) {
        super(new HashMap<Object, Object>(), paramIdenticator);
    }

    protected IdHashKey createIdKey(Object paramObject) {
        return new StrongIdHashKey(paramObject, this.id);
    }
}


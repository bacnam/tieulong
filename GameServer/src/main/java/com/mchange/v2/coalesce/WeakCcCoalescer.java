package com.mchange.v2.coalesce;

import com.mchange.v1.identicator.IdWeakHashMap;

import java.util.Map;

final class WeakCcCoalescer
        extends AbstractWeakCoalescer
        implements Coalescer {
    WeakCcCoalescer(CoalesceChecker paramCoalesceChecker) {
        super((Map) new IdWeakHashMap(new CoalesceIdenticator(paramCoalesceChecker)));
    }
}


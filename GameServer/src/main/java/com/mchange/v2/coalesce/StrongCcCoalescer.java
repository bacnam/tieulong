package com.mchange.v2.coalesce;

import com.mchange.v1.identicator.IdHashMap;

import java.util.Map;

final class StrongCcCoalescer
        extends AbstractStrongCoalescer
        implements Coalescer {
    StrongCcCoalescer(CoalesceChecker paramCoalesceChecker) {
        super((Map) new IdHashMap(new CoalesceIdenticator(paramCoalesceChecker)));
    }
}


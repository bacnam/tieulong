package com.mchange.v2.coalesce;

import java.util.WeakHashMap;

class WeakEqualsCoalescer
        extends AbstractWeakCoalescer {
    WeakEqualsCoalescer() {
        super(new WeakHashMap<Object, Object>());
    }
}


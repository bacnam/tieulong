package com.mchange.v2.coalesce;

import java.util.Iterator;

public interface Coalescer {
    Object coalesce(Object paramObject);

    int countCoalesced();

    Iterator iterator();
}


package com.mchange.v2.coalesce;

public interface CoalesceChecker {
    boolean checkCoalesce(Object paramObject1, Object paramObject2);

    int coalesceHash(Object paramObject);
}


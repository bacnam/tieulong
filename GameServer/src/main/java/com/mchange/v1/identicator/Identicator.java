package com.mchange.v1.identicator;

public interface Identicator {
    boolean identical(Object paramObject1, Object paramObject2);

    int hash(Object paramObject);
}


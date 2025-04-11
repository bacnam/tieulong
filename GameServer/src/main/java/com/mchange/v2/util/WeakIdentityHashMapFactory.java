package com.mchange.v2.util;

import com.mchange.v1.identicator.IdWeakHashMap;
import com.mchange.v1.identicator.Identicator;
import com.mchange.v1.identicator.StrongIdentityIdenticator;

import java.util.Map;

public final class WeakIdentityHashMapFactory {
    public static Map create() {
        StrongIdentityIdenticator strongIdentityIdenticator = new StrongIdentityIdenticator();
        return (Map) new IdWeakHashMap((Identicator) strongIdentityIdenticator);
    }
}


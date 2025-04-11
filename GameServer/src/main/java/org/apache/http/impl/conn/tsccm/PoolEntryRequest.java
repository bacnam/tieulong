package org.apache.http.impl.conn.tsccm;

import org.apache.http.conn.ConnectionPoolTimeoutException;

import java.util.concurrent.TimeUnit;

@Deprecated
public interface PoolEntryRequest {
    BasicPoolEntry getPoolEntry(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException, ConnectionPoolTimeoutException;

    void abortRequest();
}


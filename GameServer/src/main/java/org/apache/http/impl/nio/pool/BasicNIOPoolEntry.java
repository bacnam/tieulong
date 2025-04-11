package org.apache.http.impl.nio.pool;

import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.pool.PoolEntry;

import java.io.IOException;

@ThreadSafe
public class BasicNIOPoolEntry
        extends PoolEntry<HttpHost, NHttpClientConnection> {
    private volatile int socketTimeout;

    public BasicNIOPoolEntry(String id, HttpHost route, NHttpClientConnection conn) {
        super(id, route, conn);
    }

    public void close() {
        try {
            ((NHttpClientConnection) getConnection()).close();
        } catch (IOException ignore) {
        }
    }

    public boolean isClosed() {
        return !((NHttpClientConnection) getConnection()).isOpen();
    }

    int getSocketTimeout() {
        return this.socketTimeout;
    }

    void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
}


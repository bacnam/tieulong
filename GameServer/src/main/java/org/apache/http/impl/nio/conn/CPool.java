package org.apache.http.impl.nio.conn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.nio.conn.ManagedNHttpClientConnection;
import org.apache.http.nio.pool.AbstractNIOConnPool;
import org.apache.http.nio.pool.NIOConnFactory;
import org.apache.http.nio.pool.SocketAddressResolver;
import org.apache.http.nio.reactor.ConnectingIOReactor;

import java.util.concurrent.TimeUnit;

@ThreadSafe
class CPool
        extends AbstractNIOConnPool<HttpRoute, ManagedNHttpClientConnection, CPoolEntry> {
    private final Log log = LogFactory.getLog(CPool.class);

    private final long timeToLive;

    private final TimeUnit tunit;

    public CPool(ConnectingIOReactor ioreactor, NIOConnFactory<HttpRoute, ManagedNHttpClientConnection> connFactory, SocketAddressResolver<HttpRoute> addressResolver, int defaultMaxPerRoute, int maxTotal, long timeToLive, TimeUnit tunit) {
        super(ioreactor, connFactory, addressResolver, defaultMaxPerRoute, maxTotal);
        this.timeToLive = timeToLive;
        this.tunit = tunit;
    }

    protected CPoolEntry createEntry(HttpRoute route, ManagedNHttpClientConnection conn) {
        return new CPoolEntry(this.log, conn.getId(), route, conn, this.timeToLive, this.tunit);
    }
}


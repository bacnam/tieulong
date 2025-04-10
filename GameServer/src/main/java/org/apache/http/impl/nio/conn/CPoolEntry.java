package org.apache.http.impl.nio.conn;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.nio.conn.ManagedNHttpClientConnection;
import org.apache.http.pool.PoolEntry;

@ThreadSafe
class CPoolEntry
extends PoolEntry<HttpRoute, ManagedNHttpClientConnection>
{
private final Log log;
private volatile boolean routeComplete;

public CPoolEntry(Log log, String id, HttpRoute route, ManagedNHttpClientConnection conn, long timeToLive, TimeUnit tunit) {
super(id, route, conn, timeToLive, tunit);
this.log = log;
}

public boolean isRouteComplete() {
return this.routeComplete;
}

public void markRouteComplete() {
this.routeComplete = true;
}

public void closeConnection() throws IOException {
ManagedNHttpClientConnection conn = (ManagedNHttpClientConnection)getConnection();
conn.close();
}

public void shutdownConnection() throws IOException {
ManagedNHttpClientConnection conn = (ManagedNHttpClientConnection)getConnection();
conn.shutdown();
}

public boolean isExpired(long now) {
boolean expired = super.isExpired(now);
if (expired && this.log.isDebugEnabled()) {
this.log.debug("Connection " + this + " expired @ " + new Date(getExpiry()));
}
return expired;
}

public boolean isClosed() {
ManagedNHttpClientConnection conn = (ManagedNHttpClientConnection)getConnection();
return !conn.isOpen();
}

public void close() {
try {
closeConnection();
} catch (IOException ex) {
this.log.debug("I/O error closing connection", ex);
} 
}
}


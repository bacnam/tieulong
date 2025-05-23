package org.apache.http.impl.nio.conn;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.concurrent.BasicFuture;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.nio.conn.ClientAsyncConnectionFactory;
import org.apache.http.nio.conn.ClientAsyncConnectionManager;
import org.apache.http.nio.conn.ManagedClientAsyncConnection;
import org.apache.http.nio.conn.scheme.AsyncSchemeRegistry;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.nio.reactor.IOReactorStatus;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.pool.PoolStats;
import org.apache.http.util.Args;

@Deprecated
public class PoolingClientAsyncConnectionManager
implements ClientAsyncConnectionManager, ConnPoolControl<HttpRoute>
{
private final Log log = LogFactory.getLog(getClass());

private final ConnectingIOReactor ioreactor;

private final HttpNIOConnPool pool;

private final AsyncSchemeRegistry schemeRegistry;

private final ClientAsyncConnectionFactory connFactory;

public PoolingClientAsyncConnectionManager(ConnectingIOReactor ioreactor, AsyncSchemeRegistry schemeRegistry, long timeToLive, TimeUnit tunit) {
Args.notNull(ioreactor, "I/O reactor");
Args.notNull(schemeRegistry, "Scheme registory");
Args.notNull(tunit, "Time unit");
this.ioreactor = ioreactor;
this.pool = new HttpNIOConnPool(this.log, ioreactor, schemeRegistry, timeToLive, tunit);
this.schemeRegistry = schemeRegistry;
this.connFactory = createClientAsyncConnectionFactory();
}

public PoolingClientAsyncConnectionManager(ConnectingIOReactor ioreactor, AsyncSchemeRegistry schemeRegistry) throws IOReactorException {
this(ioreactor, schemeRegistry, -1L, TimeUnit.MILLISECONDS);
}

public PoolingClientAsyncConnectionManager(ConnectingIOReactor ioreactor) throws IOReactorException {
this(ioreactor, AsyncSchemeRegistryFactory.createDefault());
}

protected void finalize() throws Throwable {
try {
shutdown();
} finally {
super.finalize();
} 
}

protected ClientAsyncConnectionFactory createClientAsyncConnectionFactory() {
return new DefaultClientAsyncConnectionFactory();
}

public AsyncSchemeRegistry getSchemeRegistry() {
return this.schemeRegistry;
}

public void execute(IOEventDispatch eventDispatch) throws IOException {
this.ioreactor.execute(eventDispatch);
}

public IOReactorStatus getStatus() {
return this.ioreactor.getStatus();
}

public void shutdown(long waitMs) throws IOException {
this.log.debug("Connection manager is shutting down");
this.pool.shutdown(waitMs);
this.log.debug("Connection manager shut down");
}

public void shutdown() throws IOException {
this.log.debug("Connection manager is shutting down");
this.pool.shutdown(2000L);
this.log.debug("Connection manager shut down");
}

private String format(HttpRoute route, Object state) {
StringBuilder buf = new StringBuilder();
buf.append("[route: ").append(route).append("]");
if (state != null) {
buf.append("[state: ").append(state).append("]");
}
return buf.toString();
}

private String formatStats(HttpRoute route) {
StringBuilder buf = new StringBuilder();
PoolStats totals = this.pool.getTotalStats();
PoolStats stats = this.pool.getStats(route);
buf.append("[total kept alive: ").append(totals.getAvailable()).append("; ");
buf.append("route allocated: ").append(stats.getLeased() + stats.getAvailable());
buf.append(" of ").append(stats.getMax()).append("; ");
buf.append("total allocated: ").append(totals.getLeased() + totals.getAvailable());
buf.append(" of ").append(totals.getMax()).append("]");
return buf.toString();
}

private String format(HttpPoolEntry entry) {
StringBuilder buf = new StringBuilder();
buf.append("[id: ").append(entry.getId()).append("]");
buf.append("[route: ").append(entry.getRoute()).append("]");
Object state = entry.getState();
if (state != null) {
buf.append("[state: ").append(state).append("]");
}
return buf.toString();
}

public Future<ManagedClientAsyncConnection> leaseConnection(HttpRoute route, Object state, long connectTimeout, TimeUnit tunit, FutureCallback<ManagedClientAsyncConnection> callback) {
Args.notNull(route, "HTTP route");
Args.notNull(tunit, "Time unit");
if (this.log.isDebugEnabled()) {
this.log.debug("Connection request: " + format(route, state) + formatStats(route));
}
BasicFuture<ManagedClientAsyncConnection> future = new BasicFuture(callback);

this.pool.lease(route, state, connectTimeout, tunit, new InternalPoolEntryCallback(future));
return (Future<ManagedClientAsyncConnection>)future;
}

public void releaseConnection(ManagedClientAsyncConnection conn, long keepalive, TimeUnit tunit) {
Args.notNull(conn, "HTTP connection");
if (!(conn instanceof ManagedClientAsyncConnectionImpl)) {
throw new IllegalArgumentException("Connection class mismatch, connection not obtained from this manager");
}

Args.notNull(tunit, "Time unit");
ManagedClientAsyncConnectionImpl managedConn = (ManagedClientAsyncConnectionImpl)conn;
ClientAsyncConnectionManager manager = managedConn.getManager();
if (manager != null && manager != this) {
throw new IllegalArgumentException("Connection not obtained from this manager");
}
if (this.pool.isShutdown()) {
return;
}

synchronized (managedConn) {
HttpPoolEntry entry = managedConn.getPoolEntry();
if (entry == null) {
return;
}
try {
if (managedConn.isOpen() && !managedConn.isMarkedReusable()) {
try {
managedConn.shutdown();
} catch (IOException iox) {
if (this.log.isDebugEnabled()) {
this.log.debug("I/O exception shutting down released connection", iox);
}
} 
}
if (managedConn.isOpen()) {
entry.updateExpiry(keepalive, (tunit != null) ? tunit : TimeUnit.MILLISECONDS);
if (this.log.isDebugEnabled()) {
String s;
if (keepalive > 0L) {
s = "for " + keepalive + " " + tunit;
} else {
s = "indefinitely";
} 
this.log.debug("Connection " + format(entry) + " can be kept alive " + s);
} 

managedConn.setSocketTimeout(0);
} 
} finally {
this.pool.release(managedConn.detach(), managedConn.isMarkedReusable());
} 
if (this.log.isDebugEnabled()) {
this.log.debug("Connection released: " + format(entry) + formatStats((HttpRoute)entry.getRoute()));
}
} 
}

public PoolStats getTotalStats() {
return this.pool.getTotalStats();
}

public PoolStats getStats(HttpRoute route) {
return this.pool.getStats(route);
}

public void setMaxTotal(int max) {
this.pool.setMaxTotal(max);
}

public void setDefaultMaxPerRoute(int max) {
this.pool.setDefaultMaxPerRoute(max);
}

public void setMaxPerRoute(HttpRoute route, int max) {
this.pool.setMaxPerRoute(route, max);
}

public int getMaxTotal() {
return this.pool.getMaxTotal();
}

public int getDefaultMaxPerRoute() {
return this.pool.getDefaultMaxPerRoute();
}

public int getMaxPerRoute(HttpRoute route) {
return this.pool.getMaxPerRoute(route);
}

public void closeIdleConnections(long idleTimeout, TimeUnit tunit) {
if (this.log.isDebugEnabled()) {
this.log.debug("Closing connections idle longer than " + idleTimeout + " " + tunit);
}
this.pool.closeIdle(idleTimeout, tunit);
}

public void closeExpiredConnections() {
this.log.debug("Closing expired connections");
this.pool.closeExpired();
}

class InternalPoolEntryCallback
implements FutureCallback<HttpPoolEntry>
{
private final BasicFuture<ManagedClientAsyncConnection> future;

public InternalPoolEntryCallback(BasicFuture<ManagedClientAsyncConnection> future) {
this.future = future;
}

public void completed(HttpPoolEntry entry) {
if (PoolingClientAsyncConnectionManager.this.log.isDebugEnabled()) {
PoolingClientAsyncConnectionManager.this.log.debug("Connection leased: " + PoolingClientAsyncConnectionManager.this.format(entry) + PoolingClientAsyncConnectionManager.this.formatStats((HttpRoute)entry.getRoute()));
}
ManagedClientAsyncConnection conn = new ManagedClientAsyncConnectionImpl(PoolingClientAsyncConnectionManager.this, PoolingClientAsyncConnectionManager.this.connFactory, entry);

if (!this.future.completed(conn)) {
PoolingClientAsyncConnectionManager.this.pool.release(entry, true);
}
}

public void failed(Exception ex) {
if (PoolingClientAsyncConnectionManager.this.log.isDebugEnabled()) {
PoolingClientAsyncConnectionManager.this.log.debug("Connection request failed", ex);
}
this.future.failed(ex);
}

public void cancelled() {
PoolingClientAsyncConnectionManager.this.log.debug("Connection request cancelled");
this.future.cancel(true);
}
}
}


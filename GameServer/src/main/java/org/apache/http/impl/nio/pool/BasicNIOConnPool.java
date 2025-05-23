package org.apache.http.impl.nio.pool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.nio.pool.AbstractNIOConnPool;
import org.apache.http.nio.pool.NIOConnFactory;
import org.apache.http.nio.pool.SocketAddressResolver;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.params.HttpParams;
import org.apache.http.pool.PoolEntry;
import org.apache.http.util.Args;

@ThreadSafe
public class BasicNIOConnPool
extends AbstractNIOConnPool<HttpHost, NHttpClientConnection, BasicNIOPoolEntry>
{
private static final AtomicLong COUNTER = new AtomicLong();

private final int connectTimeout;

static class BasicAddressResolver
implements SocketAddressResolver<HttpHost>
{
public SocketAddress resolveLocalAddress(HttpHost host) {
return null;
}

public SocketAddress resolveRemoteAddress(HttpHost host) {
String hostname = host.getHostName();
int port = host.getPort();
if (port == -1) {
if (host.getSchemeName().equalsIgnoreCase("http")) {
port = 80;
} else if (host.getSchemeName().equalsIgnoreCase("https")) {
port = 443;
} 
}
return new InetSocketAddress(hostname, port);
}
}

@Deprecated
public BasicNIOConnPool(ConnectingIOReactor ioreactor, NIOConnFactory<HttpHost, NHttpClientConnection> connFactory, HttpParams params) {
super(ioreactor, connFactory, 2, 20);
Args.notNull(params, "HTTP parameters");
this.connectTimeout = params.getIntParameter("http.connection.timeout", 0);
}

@Deprecated
public BasicNIOConnPool(ConnectingIOReactor ioreactor, HttpParams params) {
this(ioreactor, new BasicNIOConnFactory(params), params);
}

public BasicNIOConnPool(ConnectingIOReactor ioreactor, NIOConnFactory<HttpHost, NHttpClientConnection> connFactory, int connectTimeout) {
super(ioreactor, connFactory, new BasicAddressResolver(), 2, 20);
this.connectTimeout = connectTimeout;
}

public BasicNIOConnPool(ConnectingIOReactor ioreactor, int connectTimeout, ConnectionConfig config) {
this(ioreactor, new BasicNIOConnFactory(config), connectTimeout);
}

public BasicNIOConnPool(ConnectingIOReactor ioreactor, ConnectionConfig config) {
this(ioreactor, new BasicNIOConnFactory(config), 0);
}

public BasicNIOConnPool(ConnectingIOReactor ioreactor) {
this(ioreactor, new BasicNIOConnFactory(ConnectionConfig.DEFAULT), 0);
}

@Deprecated
protected SocketAddress resolveRemoteAddress(HttpHost host) {
return new InetSocketAddress(host.getHostName(), host.getPort());
}

@Deprecated
protected SocketAddress resolveLocalAddress(HttpHost host) {
return null;
}

protected BasicNIOPoolEntry createEntry(HttpHost host, NHttpClientConnection conn) {
BasicNIOPoolEntry entry = new BasicNIOPoolEntry(Long.toString(COUNTER.getAndIncrement()), host, conn);

entry.setSocketTimeout(conn.getSocketTimeout());
return entry;
}

public Future<BasicNIOPoolEntry> lease(HttpHost route, Object state, FutureCallback<BasicNIOPoolEntry> callback) {
return lease(route, state, this.connectTimeout, TimeUnit.MILLISECONDS, callback);
}

public Future<BasicNIOPoolEntry> lease(HttpHost route, Object state) {
return lease(route, state, this.connectTimeout, TimeUnit.MILLISECONDS, null);
}

protected void onLease(BasicNIOPoolEntry entry) {
NHttpClientConnection conn = (NHttpClientConnection)entry.getConnection();
conn.setSocketTimeout(entry.getSocketTimeout());
}

protected void onRelease(BasicNIOPoolEntry entry) {
NHttpClientConnection conn = (NHttpClientConnection)entry.getConnection();
entry.setSocketTimeout(conn.getSocketTimeout());
conn.setSocketTimeout(0);
}
}


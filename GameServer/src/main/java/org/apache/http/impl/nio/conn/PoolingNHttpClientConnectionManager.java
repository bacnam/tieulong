package org.apache.http.impl.nio.conn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.concurrent.BasicFuture;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.nio.conn.*;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.pool.NIOConnFactory;
import org.apache.http.nio.pool.SocketAddressResolver;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.pool.PoolStats;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@ThreadSafe
public class PoolingNHttpClientConnectionManager
        implements NHttpClientConnectionManager, ConnPoolControl<HttpRoute> {
    static final String IOSESSION_FACTORY_REGISTRY = "http.iosession-factory-registry";
    private final Log log = LogFactory.getLog(getClass());
    private final ConnectingIOReactor ioreactor;
    private final ConfigData configData;
    private final CPool pool;
    private final Registry<SchemeIOSessionStrategy> iosessionFactoryRegistry;

    public PoolingNHttpClientConnectionManager(ConnectingIOReactor ioreactor) {
        this(ioreactor, getDefaultRegistry());
    }

    public PoolingNHttpClientConnectionManager(ConnectingIOReactor ioreactor, Registry<SchemeIOSessionStrategy> iosessionFactoryRegistry) {
        this(ioreactor, null, iosessionFactoryRegistry, null);
    }

    public PoolingNHttpClientConnectionManager(ConnectingIOReactor ioreactor, NHttpConnectionFactory<ManagedNHttpClientConnection> connFactory, DnsResolver dnsResolver) {
        this(ioreactor, connFactory, getDefaultRegistry(), dnsResolver);
    }

    public PoolingNHttpClientConnectionManager(ConnectingIOReactor ioreactor, NHttpConnectionFactory<ManagedNHttpClientConnection> connFactory) {
        this(ioreactor, connFactory, getDefaultRegistry(), null);
    }

    public PoolingNHttpClientConnectionManager(ConnectingIOReactor ioreactor, NHttpConnectionFactory<ManagedNHttpClientConnection> connFactory, Registry<SchemeIOSessionStrategy> iosessionFactoryRegistry) {
        this(ioreactor, connFactory, iosessionFactoryRegistry, null);
    }

    public PoolingNHttpClientConnectionManager(ConnectingIOReactor ioreactor, NHttpConnectionFactory<ManagedNHttpClientConnection> connFactory, Registry<SchemeIOSessionStrategy> iosessionFactoryRegistry, DnsResolver dnsResolver) {
        this(ioreactor, connFactory, iosessionFactoryRegistry, null, dnsResolver, -1L, TimeUnit.MILLISECONDS);
    }

    public PoolingNHttpClientConnectionManager(ConnectingIOReactor ioreactor, NHttpConnectionFactory<ManagedNHttpClientConnection> connFactory, Registry<SchemeIOSessionStrategy> iosessionFactoryRegistry, SchemePortResolver schemePortResolver, DnsResolver dnsResolver, long timeToLive, TimeUnit tunit) {
        Args.notNull(ioreactor, "I/O reactor");
        Args.notNull(iosessionFactoryRegistry, "I/O session factory registry");
        this.ioreactor = ioreactor;
        this.configData = new ConfigData();
        this.pool = new CPool(ioreactor, new InternalConnectionFactory(this.configData, connFactory), new InternalAddressResolver(schemePortResolver, dnsResolver), 2, 20, timeToLive, (tunit != null) ? tunit : TimeUnit.MILLISECONDS);

        this.iosessionFactoryRegistry = iosessionFactoryRegistry;
    }

    PoolingNHttpClientConnectionManager(ConnectingIOReactor ioreactor, CPool pool, Registry<SchemeIOSessionStrategy> iosessionFactoryRegistry) {
        this.ioreactor = ioreactor;
        this.configData = new ConfigData();
        this.pool = pool;
        this.iosessionFactoryRegistry = iosessionFactoryRegistry;
    }

    private static Registry<SchemeIOSessionStrategy> getDefaultRegistry() {
        return RegistryBuilder.create().register("http", NoopIOSessionStrategy.INSTANCE).register("https", SSLIOSessionStrategy.getDefaultStrategy()).build();
    }

    protected void finalize() throws Throwable {
        try {
            shutdown();
        } finally {
            super.finalize();
        }
    }

    public void execute(IOEventDispatch eventDispatch) throws IOException {
        this.ioreactor.execute(eventDispatch);
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

    private String format(CPoolEntry entry) {
        StringBuilder buf = new StringBuilder();
        buf.append("[id: ").append(entry.getId()).append("]");
        buf.append("[route: ").append(entry.getRoute()).append("]");
        Object state = entry.getState();
        if (state != null) {
            buf.append("[state: ").append(state).append("]");
        }
        return buf.toString();
    }

    public Future<NHttpClientConnection> requestConnection(HttpRoute route, Object state, long connectTimeout, long leaseTimeout, TimeUnit tunit, FutureCallback<NHttpClientConnection> callback) {
        HttpHost host;
        Args.notNull(route, "HTTP route");
        if (this.log.isDebugEnabled()) {
            this.log.debug("Connection request: " + format(route, state) + formatStats(route));
        }
        BasicFuture<NHttpClientConnection> future = new BasicFuture(callback);

        if (route.getProxyHost() != null) {
            host = route.getProxyHost();
        } else {
            host = route.getTargetHost();
        }
        SchemeIOSessionStrategy sf = (SchemeIOSessionStrategy) this.iosessionFactoryRegistry.lookup(host.getSchemeName());

        if (sf == null) {
            future.failed((Exception) new UnsupportedSchemeException(host.getSchemeName() + " protocol is not supported"));

            return (Future<NHttpClientConnection>) future;
        }
        this.pool.lease(route, state, connectTimeout, leaseTimeout, (tunit != null) ? tunit : TimeUnit.MILLISECONDS, new InternalPoolEntryCallback(future));

        return (Future<NHttpClientConnection>) future;
    }

    public void releaseConnection(NHttpClientConnection managedConn, Object state, long keepalive, TimeUnit tunit) {
        Args.notNull(managedConn, "Managed connection");
        synchronized (managedConn) {
            CPoolEntry entry = CPoolProxy.detach(managedConn);
            if (entry == null) {
                return;
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("Releasing connection: " + format(entry) + formatStats((HttpRoute) entry.getRoute()));
            }
            NHttpClientConnection conn = (NHttpClientConnection) entry.getConnection();
            try {
                if (conn.isOpen()) {
                    entry.setState(state);
                    entry.updateExpiry(keepalive, (tunit != null) ? tunit : TimeUnit.MILLISECONDS);
                    if (this.log.isDebugEnabled()) {
                        String s;
                        if (keepalive > 0L) {
                            s = "for " + (keepalive / 1000.0D) + " seconds";
                        } else {
                            s = "indefinitely";
                        }
                        this.log.debug("Connection " + format(entry) + " can be kept alive " + s);
                    }
                }
            } finally {
                this.pool.release(entry, (conn.isOpen() && entry.isRouteComplete()));
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Connection released: " + format(entry) + formatStats((HttpRoute) entry.getRoute()));
                }
            }
        }
    }

    private Lookup<SchemeIOSessionStrategy> getIOSessionFactoryRegistry(HttpContext context) {
        Registry<SchemeIOSessionStrategy> registry;
        Lookup<SchemeIOSessionStrategy> reg = (Lookup<SchemeIOSessionStrategy>) context.getAttribute("http.iosession-factory-registry");

        if (reg == null) {
            registry = this.iosessionFactoryRegistry;
        }
        return (Lookup<SchemeIOSessionStrategy>) registry;
    }

    public void startRoute(NHttpClientConnection managedConn, HttpRoute route, HttpContext context) throws IOException {
        HttpHost host;
        Args.notNull(managedConn, "Managed connection");
        Args.notNull(route, "HTTP route");

        if (route.getProxyHost() != null) {
            host = route.getProxyHost();
        } else {
            host = route.getTargetHost();
        }
        Lookup<SchemeIOSessionStrategy> reg = getIOSessionFactoryRegistry(context);
        SchemeIOSessionStrategy sf = (SchemeIOSessionStrategy) reg.lookup(host.getSchemeName());
        if (sf == null) {
            throw new UnsupportedSchemeException(host.getSchemeName() + " protocol is not supported");
        }

        if (sf.isLayeringRequired()) {
            synchronized (managedConn) {
                CPoolEntry entry = CPoolProxy.getPoolEntry(managedConn);
                ManagedNHttpClientConnection conn = (ManagedNHttpClientConnection) entry.getConnection();
                IOSession currentSession = sf.upgrade(host, conn.getIOSession());
                conn.bind(currentSession);
            }
        }
    }

    public void upgrade(NHttpClientConnection managedConn, HttpRoute route, HttpContext context) throws IOException {
        Args.notNull(managedConn, "Managed connection");
        Args.notNull(route, "HTTP route");
        HttpHost host = route.getTargetHost();
        Lookup<SchemeIOSessionStrategy> reg = getIOSessionFactoryRegistry(context);
        SchemeIOSessionStrategy sf = (SchemeIOSessionStrategy) reg.lookup(host.getSchemeName());
        if (sf == null) {
            throw new UnsupportedSchemeException(host.getSchemeName() + " protocol is not supported");
        }

        if (!sf.isLayeringRequired()) {
            throw new UnsupportedSchemeException(host.getSchemeName() + " protocol does not support connection upgrade");
        }

        synchronized (managedConn) {
            CPoolEntry entry = CPoolProxy.getPoolEntry(managedConn);
            ManagedNHttpClientConnection conn = (ManagedNHttpClientConnection) entry.getConnection();
            IOSession currentSession = sf.upgrade(host, conn.getIOSession());
            conn.bind(currentSession);
        }
    }

    public void routeComplete(NHttpClientConnection managedConn, HttpRoute route, HttpContext context) {
        Args.notNull(managedConn, "Managed connection");
        Args.notNull(route, "HTTP route");
        synchronized (managedConn) {
            CPoolEntry entry = CPoolProxy.getPoolEntry(managedConn);
            entry.markRouteComplete();
        }
    }

    public boolean isRouteComplete(NHttpClientConnection managedConn) {
        Args.notNull(managedConn, "Managed connection");
        synchronized (managedConn) {
            CPoolEntry entry = CPoolProxy.getPoolEntry(managedConn);
            return entry.isRouteComplete();
        }
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

    public int getMaxTotal() {
        return this.pool.getMaxTotal();
    }

    public void setMaxTotal(int max) {
        this.pool.setMaxTotal(max);
    }

    public int getDefaultMaxPerRoute() {
        return this.pool.getDefaultMaxPerRoute();
    }

    public void setDefaultMaxPerRoute(int max) {
        this.pool.setDefaultMaxPerRoute(max);
    }

    public int getMaxPerRoute(HttpRoute route) {
        return this.pool.getMaxPerRoute(route);
    }

    public void setMaxPerRoute(HttpRoute route, int max) {
        this.pool.setMaxPerRoute(route, max);
    }

    public PoolStats getTotalStats() {
        return this.pool.getTotalStats();
    }

    public PoolStats getStats(HttpRoute route) {
        return this.pool.getStats(route);
    }

    public ConnectionConfig getDefaultConnectionConfig() {
        return this.configData.getDefaultConnectionConfig();
    }

    public void setDefaultConnectionConfig(ConnectionConfig defaultConnectionConfig) {
        this.configData.setDefaultConnectionConfig(defaultConnectionConfig);
    }

    public ConnectionConfig getConnectionConfig(HttpHost host) {
        return this.configData.getConnectionConfig(host);
    }

    public void setConnectionConfig(HttpHost host, ConnectionConfig connectionConfig) {
        this.configData.setConnectionConfig(host, connectionConfig);
    }

    static class ConfigData {
        private final Map<HttpHost, ConnectionConfig> connectionConfigMap = new ConcurrentHashMap<HttpHost, ConnectionConfig>();
        private volatile ConnectionConfig defaultConnectionConfig;

        public ConnectionConfig getDefaultConnectionConfig() {
            return this.defaultConnectionConfig;
        }

        public void setDefaultConnectionConfig(ConnectionConfig defaultConnectionConfig) {
            this.defaultConnectionConfig = defaultConnectionConfig;
        }

        public ConnectionConfig getConnectionConfig(HttpHost host) {
            return this.connectionConfigMap.get(host);
        }

        public void setConnectionConfig(HttpHost host, ConnectionConfig connectionConfig) {
            this.connectionConfigMap.put(host, connectionConfig);
        }
    }

    static class InternalConnectionFactory
            implements NIOConnFactory<HttpRoute, ManagedNHttpClientConnection> {
        private final PoolingNHttpClientConnectionManager.ConfigData configData;

        private final NHttpConnectionFactory<ManagedNHttpClientConnection> connFactory;

        InternalConnectionFactory(PoolingNHttpClientConnectionManager.ConfigData configData, NHttpConnectionFactory<ManagedNHttpClientConnection> connFactory) {
            this.configData = (configData != null) ? configData : new PoolingNHttpClientConnectionManager.ConfigData();
            this.connFactory = (connFactory != null) ? connFactory : ManagedNHttpClientConnectionFactory.INSTANCE;
        }

        public ManagedNHttpClientConnection create(HttpRoute route, IOSession iosession) throws IOException {
            ConnectionConfig config = null;
            if (route.getProxyHost() != null) {
                config = this.configData.getConnectionConfig(route.getProxyHost());
            }
            if (config == null) {
                config = this.configData.getConnectionConfig(route.getTargetHost());
            }
            if (config == null) {
                config = this.configData.getDefaultConnectionConfig();
            }
            if (config == null) {
                config = ConnectionConfig.DEFAULT;
            }
            ManagedNHttpClientConnection conn = (ManagedNHttpClientConnection) this.connFactory.create(iosession, config);
            iosession.setAttribute("http.connection", conn);
            return conn;
        }
    }

    static class InternalAddressResolver
            implements SocketAddressResolver<HttpRoute> {
        private final SchemePortResolver schemePortResolver;

        private final DnsResolver dnsResolver;

        public InternalAddressResolver(SchemePortResolver schemePortResolver, DnsResolver dnsResolver) {
            this.schemePortResolver = (schemePortResolver != null) ? schemePortResolver : (SchemePortResolver) DefaultSchemePortResolver.INSTANCE;

            this.dnsResolver = (dnsResolver != null) ? dnsResolver : (DnsResolver) SystemDefaultDnsResolver.INSTANCE;
        }

        public SocketAddress resolveLocalAddress(HttpRoute route) throws IOException {
            return (route.getLocalAddress() != null) ? new InetSocketAddress(route.getLocalAddress(), 0) : null;
        }

        public SocketAddress resolveRemoteAddress(HttpRoute route) throws IOException {
            HttpHost host;
            if (route.getProxyHost() != null) {
                host = route.getProxyHost();
            } else {
                host = route.getTargetHost();
            }
            int port = this.schemePortResolver.resolve(host);
            InetAddress[] addresses = this.dnsResolver.resolve(host.getHostName());
            return new InetSocketAddress(addresses[0], port);
        }
    }

    class InternalPoolEntryCallback
            implements FutureCallback<CPoolEntry> {
        private final BasicFuture<NHttpClientConnection> future;

        public InternalPoolEntryCallback(BasicFuture<NHttpClientConnection> future) {
            this.future = future;
        }

        public void completed(CPoolEntry entry) {
            Asserts.check((entry.getConnection() != null), "Pool entry with no connection");
            if (PoolingNHttpClientConnectionManager.this.log.isDebugEnabled()) {
                PoolingNHttpClientConnectionManager.this.log.debug("Connection leased: " + PoolingNHttpClientConnectionManager.this.format(entry) + PoolingNHttpClientConnectionManager.this.formatStats((HttpRoute) entry.getRoute()));
            }
            NHttpClientConnection managedConn = CPoolProxy.newProxy(entry);
            if (!this.future.completed(managedConn)) {
                PoolingNHttpClientConnectionManager.this.pool.release(entry, true);
            }
        }

        public void failed(Exception ex) {
            if (PoolingNHttpClientConnectionManager.this.log.isDebugEnabled()) {
                PoolingNHttpClientConnectionManager.this.log.debug("Connection request failed", ex);
            }
            this.future.failed(ex);
        }

        public void cancelled() {
            PoolingNHttpClientConnectionManager.this.log.debug("Connection request cancelled");
            this.future.cancel(true);
        }
    }
}


/*     */ package org.apache.http.impl.nio.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.concurrent.BasicFuture;
/*     */ import org.apache.http.concurrent.FutureCallback;
/*     */ import org.apache.http.config.ConnectionConfig;
/*     */ import org.apache.http.config.Lookup;
/*     */ import org.apache.http.config.Registry;
/*     */ import org.apache.http.config.RegistryBuilder;
/*     */ import org.apache.http.conn.DnsResolver;
/*     */ import org.apache.http.conn.SchemePortResolver;
/*     */ import org.apache.http.conn.UnsupportedSchemeException;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.impl.conn.DefaultSchemePortResolver;
/*     */ import org.apache.http.impl.conn.SystemDefaultDnsResolver;
/*     */ import org.apache.http.nio.NHttpClientConnection;
/*     */ import org.apache.http.nio.conn.ManagedNHttpClientConnection;
/*     */ import org.apache.http.nio.conn.NHttpClientConnectionManager;
/*     */ import org.apache.http.nio.conn.NHttpConnectionFactory;
/*     */ import org.apache.http.nio.conn.NoopIOSessionStrategy;
/*     */ import org.apache.http.nio.conn.SchemeIOSessionStrategy;
/*     */ import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
/*     */ import org.apache.http.nio.pool.NIOConnFactory;
/*     */ import org.apache.http.nio.pool.SocketAddressResolver;
/*     */ import org.apache.http.nio.reactor.ConnectingIOReactor;
/*     */ import org.apache.http.nio.reactor.IOEventDispatch;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.pool.ConnPoolControl;
/*     */ import org.apache.http.pool.PoolStats;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ public class PoolingNHttpClientConnectionManager
/*     */   implements NHttpClientConnectionManager, ConnPoolControl<HttpRoute>
/*     */ {
/*  94 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   static final String IOSESSION_FACTORY_REGISTRY = "http.iosession-factory-registry";
/*     */   
/*     */   private final ConnectingIOReactor ioreactor;
/*     */   private final ConfigData configData;
/*     */   private final CPool pool;
/*     */   private final Registry<SchemeIOSessionStrategy> iosessionFactoryRegistry;
/*     */   
/*     */   private static Registry<SchemeIOSessionStrategy> getDefaultRegistry() {
/* 104 */     return RegistryBuilder.create().register("http", NoopIOSessionStrategy.INSTANCE).register("https", SSLIOSessionStrategy.getDefaultStrategy()).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingNHttpClientConnectionManager(ConnectingIOReactor ioreactor) {
/* 111 */     this(ioreactor, getDefaultRegistry());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingNHttpClientConnectionManager(ConnectingIOReactor ioreactor, Registry<SchemeIOSessionStrategy> iosessionFactoryRegistry) {
/* 117 */     this(ioreactor, null, iosessionFactoryRegistry, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingNHttpClientConnectionManager(ConnectingIOReactor ioreactor, NHttpConnectionFactory<ManagedNHttpClientConnection> connFactory, DnsResolver dnsResolver) {
/* 124 */     this(ioreactor, connFactory, getDefaultRegistry(), dnsResolver);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingNHttpClientConnectionManager(ConnectingIOReactor ioreactor, NHttpConnectionFactory<ManagedNHttpClientConnection> connFactory) {
/* 130 */     this(ioreactor, connFactory, getDefaultRegistry(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingNHttpClientConnectionManager(ConnectingIOReactor ioreactor, NHttpConnectionFactory<ManagedNHttpClientConnection> connFactory, Registry<SchemeIOSessionStrategy> iosessionFactoryRegistry) {
/* 137 */     this(ioreactor, connFactory, iosessionFactoryRegistry, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingNHttpClientConnectionManager(ConnectingIOReactor ioreactor, NHttpConnectionFactory<ManagedNHttpClientConnection> connFactory, Registry<SchemeIOSessionStrategy> iosessionFactoryRegistry, DnsResolver dnsResolver) {
/* 145 */     this(ioreactor, connFactory, iosessionFactoryRegistry, null, dnsResolver, -1L, TimeUnit.MILLISECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingNHttpClientConnectionManager(ConnectingIOReactor ioreactor, NHttpConnectionFactory<ManagedNHttpClientConnection> connFactory, Registry<SchemeIOSessionStrategy> iosessionFactoryRegistry, SchemePortResolver schemePortResolver, DnsResolver dnsResolver, long timeToLive, TimeUnit tunit) {
/* 157 */     Args.notNull(ioreactor, "I/O reactor");
/* 158 */     Args.notNull(iosessionFactoryRegistry, "I/O session factory registry");
/* 159 */     this.ioreactor = ioreactor;
/* 160 */     this.configData = new ConfigData();
/* 161 */     this.pool = new CPool(ioreactor, new InternalConnectionFactory(this.configData, connFactory), new InternalAddressResolver(schemePortResolver, dnsResolver), 2, 20, timeToLive, (tunit != null) ? tunit : TimeUnit.MILLISECONDS);
/*     */ 
/*     */ 
/*     */     
/* 165 */     this.iosessionFactoryRegistry = iosessionFactoryRegistry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PoolingNHttpClientConnectionManager(ConnectingIOReactor ioreactor, CPool pool, Registry<SchemeIOSessionStrategy> iosessionFactoryRegistry) {
/* 173 */     this.ioreactor = ioreactor;
/* 174 */     this.configData = new ConfigData();
/* 175 */     this.pool = pool;
/* 176 */     this.iosessionFactoryRegistry = iosessionFactoryRegistry;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*     */     try {
/* 182 */       shutdown();
/*     */     } finally {
/* 184 */       super.finalize();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void execute(IOEventDispatch eventDispatch) throws IOException {
/* 189 */     this.ioreactor.execute(eventDispatch);
/*     */   }
/*     */   
/*     */   public void shutdown(long waitMs) throws IOException {
/* 193 */     this.log.debug("Connection manager is shutting down");
/* 194 */     this.pool.shutdown(waitMs);
/* 195 */     this.log.debug("Connection manager shut down");
/*     */   }
/*     */   
/*     */   public void shutdown() throws IOException {
/* 199 */     this.log.debug("Connection manager is shutting down");
/* 200 */     this.pool.shutdown(2000L);
/* 201 */     this.log.debug("Connection manager shut down");
/*     */   }
/*     */   
/*     */   private String format(HttpRoute route, Object state) {
/* 205 */     StringBuilder buf = new StringBuilder();
/* 206 */     buf.append("[route: ").append(route).append("]");
/* 207 */     if (state != null) {
/* 208 */       buf.append("[state: ").append(state).append("]");
/*     */     }
/* 210 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private String formatStats(HttpRoute route) {
/* 214 */     StringBuilder buf = new StringBuilder();
/* 215 */     PoolStats totals = this.pool.getTotalStats();
/* 216 */     PoolStats stats = this.pool.getStats(route);
/* 217 */     buf.append("[total kept alive: ").append(totals.getAvailable()).append("; ");
/* 218 */     buf.append("route allocated: ").append(stats.getLeased() + stats.getAvailable());
/* 219 */     buf.append(" of ").append(stats.getMax()).append("; ");
/* 220 */     buf.append("total allocated: ").append(totals.getLeased() + totals.getAvailable());
/* 221 */     buf.append(" of ").append(totals.getMax()).append("]");
/* 222 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private String format(CPoolEntry entry) {
/* 226 */     StringBuilder buf = new StringBuilder();
/* 227 */     buf.append("[id: ").append(entry.getId()).append("]");
/* 228 */     buf.append("[route: ").append(entry.getRoute()).append("]");
/* 229 */     Object state = entry.getState();
/* 230 */     if (state != null) {
/* 231 */       buf.append("[state: ").append(state).append("]");
/*     */     }
/* 233 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<NHttpClientConnection> requestConnection(HttpRoute route, Object state, long connectTimeout, long leaseTimeout, TimeUnit tunit, FutureCallback<NHttpClientConnection> callback) {
/*     */     HttpHost host;
/* 243 */     Args.notNull(route, "HTTP route");
/* 244 */     if (this.log.isDebugEnabled()) {
/* 245 */       this.log.debug("Connection request: " + format(route, state) + formatStats(route));
/*     */     }
/* 247 */     BasicFuture<NHttpClientConnection> future = new BasicFuture(callback);
/*     */     
/* 249 */     if (route.getProxyHost() != null) {
/* 250 */       host = route.getProxyHost();
/*     */     } else {
/* 252 */       host = route.getTargetHost();
/*     */     } 
/* 254 */     SchemeIOSessionStrategy sf = (SchemeIOSessionStrategy)this.iosessionFactoryRegistry.lookup(host.getSchemeName());
/*     */     
/* 256 */     if (sf == null) {
/* 257 */       future.failed((Exception)new UnsupportedSchemeException(host.getSchemeName() + " protocol is not supported"));
/*     */       
/* 259 */       return (Future<NHttpClientConnection>)future;
/*     */     } 
/* 261 */     this.pool.lease(route, state, connectTimeout, leaseTimeout, (tunit != null) ? tunit : TimeUnit.MILLISECONDS, new InternalPoolEntryCallback(future));
/*     */ 
/*     */     
/* 264 */     return (Future<NHttpClientConnection>)future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseConnection(NHttpClientConnection managedConn, Object state, long keepalive, TimeUnit tunit) {
/* 272 */     Args.notNull(managedConn, "Managed connection");
/* 273 */     synchronized (managedConn) {
/* 274 */       CPoolEntry entry = CPoolProxy.detach(managedConn);
/* 275 */       if (entry == null) {
/*     */         return;
/*     */       }
/* 278 */       if (this.log.isDebugEnabled()) {
/* 279 */         this.log.debug("Releasing connection: " + format(entry) + formatStats((HttpRoute)entry.getRoute()));
/*     */       }
/* 281 */       NHttpClientConnection conn = (NHttpClientConnection)entry.getConnection();
/*     */       try {
/* 283 */         if (conn.isOpen()) {
/* 284 */           entry.setState(state);
/* 285 */           entry.updateExpiry(keepalive, (tunit != null) ? tunit : TimeUnit.MILLISECONDS);
/* 286 */           if (this.log.isDebugEnabled()) {
/*     */             String s;
/* 288 */             if (keepalive > 0L) {
/* 289 */               s = "for " + (keepalive / 1000.0D) + " seconds";
/*     */             } else {
/* 291 */               s = "indefinitely";
/*     */             } 
/* 293 */             this.log.debug("Connection " + format(entry) + " can be kept alive " + s);
/*     */           } 
/*     */         } 
/*     */       } finally {
/* 297 */         this.pool.release(entry, (conn.isOpen() && entry.isRouteComplete()));
/* 298 */         if (this.log.isDebugEnabled()) {
/* 299 */           this.log.debug("Connection released: " + format(entry) + formatStats((HttpRoute)entry.getRoute()));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Lookup<SchemeIOSessionStrategy> getIOSessionFactoryRegistry(HttpContext context) {
/*     */     Registry<SchemeIOSessionStrategy> registry;
/* 307 */     Lookup<SchemeIOSessionStrategy> reg = (Lookup<SchemeIOSessionStrategy>)context.getAttribute("http.iosession-factory-registry");
/*     */     
/* 309 */     if (reg == null) {
/* 310 */       registry = this.iosessionFactoryRegistry;
/*     */     }
/* 312 */     return (Lookup<SchemeIOSessionStrategy>)registry;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startRoute(NHttpClientConnection managedConn, HttpRoute route, HttpContext context) throws IOException {
/*     */     HttpHost host;
/* 319 */     Args.notNull(managedConn, "Managed connection");
/* 320 */     Args.notNull(route, "HTTP route");
/*     */     
/* 322 */     if (route.getProxyHost() != null) {
/* 323 */       host = route.getProxyHost();
/*     */     } else {
/* 325 */       host = route.getTargetHost();
/*     */     } 
/* 327 */     Lookup<SchemeIOSessionStrategy> reg = getIOSessionFactoryRegistry(context);
/* 328 */     SchemeIOSessionStrategy sf = (SchemeIOSessionStrategy)reg.lookup(host.getSchemeName());
/* 329 */     if (sf == null) {
/* 330 */       throw new UnsupportedSchemeException(host.getSchemeName() + " protocol is not supported");
/*     */     }
/*     */     
/* 333 */     if (sf.isLayeringRequired()) {
/* 334 */       synchronized (managedConn) {
/* 335 */         CPoolEntry entry = CPoolProxy.getPoolEntry(managedConn);
/* 336 */         ManagedNHttpClientConnection conn = (ManagedNHttpClientConnection)entry.getConnection();
/* 337 */         IOSession currentSession = sf.upgrade(host, conn.getIOSession());
/* 338 */         conn.bind(currentSession);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgrade(NHttpClientConnection managedConn, HttpRoute route, HttpContext context) throws IOException {
/* 347 */     Args.notNull(managedConn, "Managed connection");
/* 348 */     Args.notNull(route, "HTTP route");
/* 349 */     HttpHost host = route.getTargetHost();
/* 350 */     Lookup<SchemeIOSessionStrategy> reg = getIOSessionFactoryRegistry(context);
/* 351 */     SchemeIOSessionStrategy sf = (SchemeIOSessionStrategy)reg.lookup(host.getSchemeName());
/* 352 */     if (sf == null) {
/* 353 */       throw new UnsupportedSchemeException(host.getSchemeName() + " protocol is not supported");
/*     */     }
/*     */     
/* 356 */     if (!sf.isLayeringRequired()) {
/* 357 */       throw new UnsupportedSchemeException(host.getSchemeName() + " protocol does not support connection upgrade");
/*     */     }
/*     */     
/* 360 */     synchronized (managedConn) {
/* 361 */       CPoolEntry entry = CPoolProxy.getPoolEntry(managedConn);
/* 362 */       ManagedNHttpClientConnection conn = (ManagedNHttpClientConnection)entry.getConnection();
/* 363 */       IOSession currentSession = sf.upgrade(host, conn.getIOSession());
/* 364 */       conn.bind(currentSession);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void routeComplete(NHttpClientConnection managedConn, HttpRoute route, HttpContext context) {
/* 372 */     Args.notNull(managedConn, "Managed connection");
/* 373 */     Args.notNull(route, "HTTP route");
/* 374 */     synchronized (managedConn) {
/* 375 */       CPoolEntry entry = CPoolProxy.getPoolEntry(managedConn);
/* 376 */       entry.markRouteComplete();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRouteComplete(NHttpClientConnection managedConn) {
/* 382 */     Args.notNull(managedConn, "Managed connection");
/* 383 */     synchronized (managedConn) {
/* 384 */       CPoolEntry entry = CPoolProxy.getPoolEntry(managedConn);
/* 385 */       return entry.isRouteComplete();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void closeIdleConnections(long idleTimeout, TimeUnit tunit) {
/* 390 */     if (this.log.isDebugEnabled()) {
/* 391 */       this.log.debug("Closing connections idle longer than " + idleTimeout + " " + tunit);
/*     */     }
/* 393 */     this.pool.closeIdle(idleTimeout, tunit);
/*     */   }
/*     */   
/*     */   public void closeExpiredConnections() {
/* 397 */     this.log.debug("Closing expired connections");
/* 398 */     this.pool.closeExpired();
/*     */   }
/*     */   
/*     */   public int getMaxTotal() {
/* 402 */     return this.pool.getMaxTotal();
/*     */   }
/*     */   
/*     */   public void setMaxTotal(int max) {
/* 406 */     this.pool.setMaxTotal(max);
/*     */   }
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/* 410 */     return this.pool.getDefaultMaxPerRoute();
/*     */   }
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/* 414 */     this.pool.setDefaultMaxPerRoute(max);
/*     */   }
/*     */   
/*     */   public int getMaxPerRoute(HttpRoute route) {
/* 418 */     return this.pool.getMaxPerRoute(route);
/*     */   }
/*     */   
/*     */   public void setMaxPerRoute(HttpRoute route, int max) {
/* 422 */     this.pool.setMaxPerRoute(route, max);
/*     */   }
/*     */   
/*     */   public PoolStats getTotalStats() {
/* 426 */     return this.pool.getTotalStats();
/*     */   }
/*     */   
/*     */   public PoolStats getStats(HttpRoute route) {
/* 430 */     return this.pool.getStats(route);
/*     */   }
/*     */   
/*     */   public ConnectionConfig getDefaultConnectionConfig() {
/* 434 */     return this.configData.getDefaultConnectionConfig();
/*     */   }
/*     */   
/*     */   public void setDefaultConnectionConfig(ConnectionConfig defaultConnectionConfig) {
/* 438 */     this.configData.setDefaultConnectionConfig(defaultConnectionConfig);
/*     */   }
/*     */   
/*     */   public ConnectionConfig getConnectionConfig(HttpHost host) {
/* 442 */     return this.configData.getConnectionConfig(host);
/*     */   }
/*     */   
/*     */   public void setConnectionConfig(HttpHost host, ConnectionConfig connectionConfig) {
/* 446 */     this.configData.setConnectionConfig(host, connectionConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   class InternalPoolEntryCallback
/*     */     implements FutureCallback<CPoolEntry>
/*     */   {
/*     */     private final BasicFuture<NHttpClientConnection> future;
/*     */     
/*     */     public InternalPoolEntryCallback(BasicFuture<NHttpClientConnection> future) {
/* 456 */       this.future = future;
/*     */     }
/*     */     
/*     */     public void completed(CPoolEntry entry) {
/* 460 */       Asserts.check((entry.getConnection() != null), "Pool entry with no connection");
/* 461 */       if (PoolingNHttpClientConnectionManager.this.log.isDebugEnabled()) {
/* 462 */         PoolingNHttpClientConnectionManager.this.log.debug("Connection leased: " + PoolingNHttpClientConnectionManager.this.format(entry) + PoolingNHttpClientConnectionManager.this.formatStats((HttpRoute)entry.getRoute()));
/*     */       }
/* 464 */       NHttpClientConnection managedConn = CPoolProxy.newProxy(entry);
/* 465 */       if (!this.future.completed(managedConn)) {
/* 466 */         PoolingNHttpClientConnectionManager.this.pool.release(entry, true);
/*     */       }
/*     */     }
/*     */     
/*     */     public void failed(Exception ex) {
/* 471 */       if (PoolingNHttpClientConnectionManager.this.log.isDebugEnabled()) {
/* 472 */         PoolingNHttpClientConnectionManager.this.log.debug("Connection request failed", ex);
/*     */       }
/* 474 */       this.future.failed(ex);
/*     */     }
/*     */     
/*     */     public void cancelled() {
/* 478 */       PoolingNHttpClientConnectionManager.this.log.debug("Connection request cancelled");
/* 479 */       this.future.cancel(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class ConfigData
/*     */   {
/* 491 */     private final Map<HttpHost, ConnectionConfig> connectionConfigMap = new ConcurrentHashMap<HttpHost, ConnectionConfig>();
/*     */     private volatile ConnectionConfig defaultConnectionConfig;
/*     */     
/*     */     public ConnectionConfig getDefaultConnectionConfig() {
/* 495 */       return this.defaultConnectionConfig;
/*     */     }
/*     */     
/*     */     public void setDefaultConnectionConfig(ConnectionConfig defaultConnectionConfig) {
/* 499 */       this.defaultConnectionConfig = defaultConnectionConfig;
/*     */     }
/*     */     
/*     */     public ConnectionConfig getConnectionConfig(HttpHost host) {
/* 503 */       return this.connectionConfigMap.get(host);
/*     */     }
/*     */     
/*     */     public void setConnectionConfig(HttpHost host, ConnectionConfig connectionConfig) {
/* 507 */       this.connectionConfigMap.put(host, connectionConfig);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class InternalConnectionFactory
/*     */     implements NIOConnFactory<HttpRoute, ManagedNHttpClientConnection>
/*     */   {
/*     */     private final PoolingNHttpClientConnectionManager.ConfigData configData;
/*     */     
/*     */     private final NHttpConnectionFactory<ManagedNHttpClientConnection> connFactory;
/*     */ 
/*     */     
/*     */     InternalConnectionFactory(PoolingNHttpClientConnectionManager.ConfigData configData, NHttpConnectionFactory<ManagedNHttpClientConnection> connFactory) {
/* 521 */       this.configData = (configData != null) ? configData : new PoolingNHttpClientConnectionManager.ConfigData();
/* 522 */       this.connFactory = (connFactory != null) ? connFactory : ManagedNHttpClientConnectionFactory.INSTANCE;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ManagedNHttpClientConnection create(HttpRoute route, IOSession iosession) throws IOException {
/* 528 */       ConnectionConfig config = null;
/* 529 */       if (route.getProxyHost() != null) {
/* 530 */         config = this.configData.getConnectionConfig(route.getProxyHost());
/*     */       }
/* 532 */       if (config == null) {
/* 533 */         config = this.configData.getConnectionConfig(route.getTargetHost());
/*     */       }
/* 535 */       if (config == null) {
/* 536 */         config = this.configData.getDefaultConnectionConfig();
/*     */       }
/* 538 */       if (config == null) {
/* 539 */         config = ConnectionConfig.DEFAULT;
/*     */       }
/* 541 */       ManagedNHttpClientConnection conn = (ManagedNHttpClientConnection)this.connFactory.create(iosession, config);
/* 542 */       iosession.setAttribute("http.connection", conn);
/* 543 */       return conn;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class InternalAddressResolver
/*     */     implements SocketAddressResolver<HttpRoute>
/*     */   {
/*     */     private final SchemePortResolver schemePortResolver;
/*     */     
/*     */     private final DnsResolver dnsResolver;
/*     */ 
/*     */     
/*     */     public InternalAddressResolver(SchemePortResolver schemePortResolver, DnsResolver dnsResolver) {
/* 557 */       this.schemePortResolver = (schemePortResolver != null) ? schemePortResolver : (SchemePortResolver)DefaultSchemePortResolver.INSTANCE;
/*     */       
/* 559 */       this.dnsResolver = (dnsResolver != null) ? dnsResolver : (DnsResolver)SystemDefaultDnsResolver.INSTANCE;
/*     */     }
/*     */ 
/*     */     
/*     */     public SocketAddress resolveLocalAddress(HttpRoute route) throws IOException {
/* 564 */       return (route.getLocalAddress() != null) ? new InetSocketAddress(route.getLocalAddress(), 0) : null;
/*     */     }
/*     */     
/*     */     public SocketAddress resolveRemoteAddress(HttpRoute route) throws IOException {
/*     */       HttpHost host;
/* 569 */       if (route.getProxyHost() != null) {
/* 570 */         host = route.getProxyHost();
/*     */       } else {
/* 572 */         host = route.getTargetHost();
/*     */       } 
/* 574 */       int port = this.schemePortResolver.resolve(host);
/* 575 */       InetAddress[] addresses = this.dnsResolver.resolve(host.getHostName());
/* 576 */       return new InetSocketAddress(addresses[0], port);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/conn/PoolingNHttpClientConnectionManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
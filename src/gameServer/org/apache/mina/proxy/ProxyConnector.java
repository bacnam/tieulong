/*     */ package org.apache.mina.proxy;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.concurrent.Executor;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.file.FileRegion;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.future.ConnectFuture;
/*     */ import org.apache.mina.core.future.DefaultConnectFuture;
/*     */ import org.apache.mina.core.service.AbstractIoConnector;
/*     */ import org.apache.mina.core.service.DefaultTransportMetadata;
/*     */ import org.apache.mina.core.service.IoHandler;
/*     */ import org.apache.mina.core.service.TransportMetadata;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.session.IoSessionConfig;
/*     */ import org.apache.mina.core.session.IoSessionInitializer;
/*     */ import org.apache.mina.proxy.filter.ProxyFilter;
/*     */ import org.apache.mina.proxy.session.ProxyIoSession;
/*     */ import org.apache.mina.proxy.session.ProxyIoSessionInitializer;
/*     */ import org.apache.mina.transport.socket.DefaultSocketSessionConfig;
/*     */ import org.apache.mina.transport.socket.SocketConnector;
/*     */ import org.apache.mina.transport.socket.SocketSessionConfig;
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
/*     */ public class ProxyConnector
/*     */   extends AbstractIoConnector
/*     */ {
/*  66 */   private static final TransportMetadata METADATA = (TransportMetadata)new DefaultTransportMetadata("proxy", "proxyconnector", false, true, InetSocketAddress.class, SocketSessionConfig.class, new Class[] { IoBuffer.class, FileRegion.class });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   private SocketConnector connector = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   private final ProxyFilter proxyFilter = new ProxyFilter();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ProxyIoSession proxyIoSession;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DefaultConnectFuture future;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxyConnector() {
/*  93 */     super((IoSessionConfig)new DefaultSocketSessionConfig(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxyConnector(SocketConnector connector) {
/* 102 */     this(connector, (IoSessionConfig)new DefaultSocketSessionConfig(), (Executor)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxyConnector(SocketConnector connector, IoSessionConfig config, Executor executor) {
/* 110 */     super(config, executor);
/* 111 */     setConnector(connector);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoSessionConfig getSessionConfig() {
/* 118 */     return (IoSessionConfig)this.connector.getSessionConfig();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxyIoSession getProxyIoSession() {
/* 125 */     return this.proxyIoSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyIoSession(ProxyIoSession proxyIoSession) {
/* 133 */     if (proxyIoSession == null) {
/* 134 */       throw new IllegalArgumentException("proxySession object cannot be null");
/*     */     }
/*     */     
/* 137 */     if (proxyIoSession.getProxyAddress() == null) {
/* 138 */       throw new IllegalArgumentException("proxySession.proxyAddress cannot be null");
/*     */     }
/*     */     
/* 141 */     proxyIoSession.setConnector(this);
/* 142 */     setDefaultRemoteAddress(proxyIoSession.getProxyAddress());
/* 143 */     this.proxyIoSession = proxyIoSession;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConnectFuture connect0(SocketAddress remoteAddress, SocketAddress localAddress, IoSessionInitializer<? extends ConnectFuture> sessionInitializer) {
/* 159 */     if (!this.proxyIoSession.isReconnectionNeeded()) {
/*     */       
/* 161 */       IoHandler handler = getHandler();
/* 162 */       if (!(handler instanceof AbstractProxyIoHandler)) {
/* 163 */         throw new IllegalArgumentException("IoHandler must be an instance of AbstractProxyIoHandler");
/*     */       }
/*     */       
/* 166 */       this.connector.setHandler(handler);
/* 167 */       this.future = new DefaultConnectFuture();
/*     */     } 
/*     */     
/* 170 */     ConnectFuture conFuture = this.connector.connect(this.proxyIoSession.getProxyAddress(), (IoSessionInitializer)new ProxyIoSessionInitializer(sessionInitializer, this.proxyIoSession));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 178 */     if (this.proxyIoSession.getRequest() instanceof org.apache.mina.proxy.handlers.socks.SocksProxyRequest || this.proxyIoSession.isReconnectionNeeded()) {
/* 179 */       return conFuture;
/*     */     }
/*     */     
/* 182 */     return (ConnectFuture)this.future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancelConnectFuture() {
/* 189 */     this.future.cancel();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConnectFuture fireConnected(IoSession session) {
/* 199 */     this.future.setSession(session);
/* 200 */     return (ConnectFuture)this.future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SocketConnector getConnector() {
/* 208 */     return this.connector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final void setConnector(SocketConnector connector) {
/* 218 */     if (connector == null) {
/* 219 */       throw new IllegalArgumentException("connector cannot be null");
/*     */     }
/*     */     
/* 222 */     this.connector = connector;
/* 223 */     String className = ProxyFilter.class.getName();
/*     */ 
/*     */     
/* 226 */     if (connector.getFilterChain().contains(className)) {
/* 227 */       connector.getFilterChain().remove(className);
/*     */     }
/*     */ 
/*     */     
/* 231 */     connector.getFilterChain().addFirst(className, (IoFilter)this.proxyFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dispose0() throws Exception {
/* 239 */     if (this.connector != null) {
/* 240 */       this.connector.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TransportMetadata getTransportMetadata() {
/* 248 */     return METADATA;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/ProxyConnector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
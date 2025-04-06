/*     */ package org.apache.mina.core.service;
/*     */ 
/*     */ import java.net.SocketAddress;
/*     */ import java.util.concurrent.Executor;
/*     */ import org.apache.mina.core.future.ConnectFuture;
/*     */ import org.apache.mina.core.future.IoFuture;
/*     */ import org.apache.mina.core.future.IoFutureListener;
/*     */ import org.apache.mina.core.session.IdleStatus;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.session.IoSessionConfig;
/*     */ import org.apache.mina.core.session.IoSessionInitializer;
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
/*     */ public abstract class AbstractIoConnector
/*     */   extends AbstractIoService
/*     */   implements IoConnector
/*     */ {
/*  43 */   private long connectTimeoutCheckInterval = 50L;
/*     */   
/*  45 */   private long connectTimeoutInMillis = 60000L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SocketAddress defaultRemoteAddress;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SocketAddress defaultLocalAddress;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractIoConnector(IoSessionConfig sessionConfig, Executor executor) {
/*  68 */     super(sessionConfig, executor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getConnectTimeoutCheckInterval() {
/*  79 */     return this.connectTimeoutCheckInterval;
/*     */   }
/*     */   
/*     */   public void setConnectTimeoutCheckInterval(long minimumConnectTimeout) {
/*  83 */     if (getConnectTimeoutMillis() < minimumConnectTimeout) {
/*  84 */       this.connectTimeoutInMillis = minimumConnectTimeout;
/*     */     }
/*     */     
/*  87 */     this.connectTimeoutCheckInterval = minimumConnectTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getConnectTimeout() {
/*  95 */     return (int)this.connectTimeoutInMillis / 1000;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getConnectTimeoutMillis() {
/* 102 */     return this.connectTimeoutInMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setConnectTimeout(int connectTimeout) {
/* 111 */     setConnectTimeoutMillis(connectTimeout * 1000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setConnectTimeoutMillis(long connectTimeoutInMillis) {
/* 119 */     if (connectTimeoutInMillis <= this.connectTimeoutCheckInterval) {
/* 120 */       this.connectTimeoutCheckInterval = connectTimeoutInMillis;
/*     */     }
/* 122 */     this.connectTimeoutInMillis = connectTimeoutInMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketAddress getDefaultRemoteAddress() {
/* 129 */     return this.defaultRemoteAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setDefaultLocalAddress(SocketAddress localAddress) {
/* 136 */     this.defaultLocalAddress = localAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SocketAddress getDefaultLocalAddress() {
/* 143 */     return this.defaultLocalAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setDefaultRemoteAddress(SocketAddress defaultRemoteAddress) {
/* 150 */     if (defaultRemoteAddress == null) {
/* 151 */       throw new IllegalArgumentException("defaultRemoteAddress");
/*     */     }
/*     */     
/* 154 */     if (!getTransportMetadata().getAddressType().isAssignableFrom(defaultRemoteAddress.getClass())) {
/* 155 */       throw new IllegalArgumentException("defaultRemoteAddress type: " + defaultRemoteAddress.getClass() + " (expected: " + getTransportMetadata().getAddressType() + ")");
/*     */     }
/*     */     
/* 158 */     this.defaultRemoteAddress = defaultRemoteAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ConnectFuture connect() {
/* 165 */     SocketAddress defaultRemoteAddress = getDefaultRemoteAddress();
/* 166 */     if (defaultRemoteAddress == null) {
/* 167 */       throw new IllegalStateException("defaultRemoteAddress is not set.");
/*     */     }
/*     */     
/* 170 */     return connect(defaultRemoteAddress, (SocketAddress)null, (IoSessionInitializer<? extends ConnectFuture>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConnectFuture connect(IoSessionInitializer<? extends ConnectFuture> sessionInitializer) {
/* 177 */     SocketAddress defaultRemoteAddress = getDefaultRemoteAddress();
/* 178 */     if (defaultRemoteAddress == null) {
/* 179 */       throw new IllegalStateException("defaultRemoteAddress is not set.");
/*     */     }
/*     */     
/* 182 */     return connect(defaultRemoteAddress, (SocketAddress)null, sessionInitializer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ConnectFuture connect(SocketAddress remoteAddress) {
/* 189 */     return connect(remoteAddress, (SocketAddress)null, (IoSessionInitializer<? extends ConnectFuture>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConnectFuture connect(SocketAddress remoteAddress, IoSessionInitializer<? extends ConnectFuture> sessionInitializer) {
/* 197 */     return connect(remoteAddress, (SocketAddress)null, sessionInitializer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConnectFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
/* 204 */     return connect(remoteAddress, localAddress, (IoSessionInitializer<? extends ConnectFuture>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ConnectFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, IoSessionInitializer<? extends ConnectFuture> sessionInitializer) {
/* 212 */     if (isDisposing()) {
/* 213 */       throw new IllegalStateException("The connector has been disposed.");
/*     */     }
/*     */     
/* 216 */     if (remoteAddress == null) {
/* 217 */       throw new IllegalArgumentException("remoteAddress");
/*     */     }
/*     */     
/* 220 */     if (!getTransportMetadata().getAddressType().isAssignableFrom(remoteAddress.getClass())) {
/* 221 */       throw new IllegalArgumentException("remoteAddress type: " + remoteAddress.getClass() + " (expected: " + getTransportMetadata().getAddressType() + ")");
/*     */     }
/*     */ 
/*     */     
/* 225 */     if (localAddress != null && !getTransportMetadata().getAddressType().isAssignableFrom(localAddress.getClass())) {
/* 226 */       throw new IllegalArgumentException("localAddress type: " + localAddress.getClass() + " (expected: " + getTransportMetadata().getAddressType() + ")");
/*     */     }
/*     */ 
/*     */     
/* 230 */     if (getHandler() == null) {
/* 231 */       if (getSessionConfig().isUseReadOperation()) {
/* 232 */         setHandler(new IoHandler()
/*     */             {
/*     */               public void exceptionCaught(IoSession session, Throwable cause) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               public void messageReceived(IoSession session, Object message) throws Exception {}
/*     */ 
/*     */ 
/*     */               
/*     */               public void messageSent(IoSession session, Object message) throws Exception {}
/*     */ 
/*     */ 
/*     */               
/*     */               public void sessionClosed(IoSession session) throws Exception {}
/*     */ 
/*     */ 
/*     */               
/*     */               public void sessionCreated(IoSession session) throws Exception {}
/*     */ 
/*     */ 
/*     */               
/*     */               public void sessionIdle(IoSession session, IdleStatus status) throws Exception {}
/*     */ 
/*     */ 
/*     */               
/*     */               public void sessionOpened(IoSession session) throws Exception {}
/*     */ 
/*     */ 
/*     */               
/*     */               public void inputClosed(IoSession session) throws Exception {}
/*     */             });
/*     */       } else {
/* 266 */         throw new IllegalStateException("handler is not set.");
/*     */       } 
/*     */     }
/*     */     
/* 270 */     return connect0(remoteAddress, localAddress, sessionInitializer);
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
/*     */   protected abstract ConnectFuture connect0(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, IoSessionInitializer<? extends ConnectFuture> paramIoSessionInitializer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void finishSessionInitialization0(final IoSession session, IoFuture future) {
/* 291 */     future.addListener(new IoFutureListener<ConnectFuture>() {
/*     */           public void operationComplete(ConnectFuture future) {
/* 293 */             if (future.isCanceled()) {
/* 294 */               session.close(true);
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 305 */     TransportMetadata m = getTransportMetadata();
/* 306 */     return '(' + m.getProviderName() + ' ' + m.getName() + " connector: " + "managedSessionCount: " + getManagedSessionCount() + ')';
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/service/AbstractIoConnector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.zhonglian.server.websocket;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import BaseTask.SyncTask.SyncTaskManager;
/*     */ import com.zhonglian.server.websocket.codecfactory.WebDecoder;
/*     */ import com.zhonglian.server.websocket.codecfactory.WebEncoder;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.buffer.IoBufferAllocator;
/*     */ import org.apache.mina.core.buffer.SimpleBufferAllocator;
/*     */ import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.future.ConnectFuture;
/*     */ import org.apache.mina.filter.codec.ProtocolCodecFilter;
/*     */ import org.apache.mina.filter.codec.ProtocolDecoder;
/*     */ import org.apache.mina.filter.codec.ProtocolEncoder;
/*     */ import org.apache.mina.filter.logging.LoggingFilter;
/*     */ import org.apache.mina.transport.socket.SocketSessionConfig;
/*     */ import org.apache.mina.transport.socket.nio.NioSocketConnector;
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
/*     */ public abstract class BaseConnector<Session extends BaseSession>
/*     */ {
/*     */   protected String ip;
/*     */   protected int port;
/*     */   protected NioSocketConnector connector;
/*     */   protected BaseIoHandler<Session> handler;
/*     */   protected ProtocolCodecFilter codecFilter;
/*     */   protected Session _session;
/*     */   
/*     */   public BaseConnector(BaseIoHandler<Session> ioHandler) {
/*  45 */     this.handler = ioHandler;
/*  46 */     this.codecFilter = new ProtocolCodecFilter((ProtocolEncoder)new WebEncoder(), (ProtocolDecoder)new WebDecoder());
/*     */   }
/*     */   
/*     */   public BaseConnector(BaseIoHandler<Session> ioHandler, ProtocolEncoder encoder, ProtocolDecoder decoder) {
/*  50 */     this.handler = ioHandler;
/*  51 */     this.codecFilter = new ProtocolCodecFilter(encoder, decoder);
/*     */   }
/*     */   
/*     */   public Session getSocketSession() {
/*  55 */     return this._session;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean connect(SocketAddress sa, long timeout) {
/*     */     try {
/*  66 */       this.connector = new NioSocketConnector();
/*  67 */       config(this.connector);
/*     */       
/*  69 */       DefaultIoFilterChainBuilder chain = this.connector.getFilterChain();
/*  70 */       chain.addLast("logger", (IoFilter)new LoggingFilter());
/*  71 */       chain.addLast("codec", (IoFilter)this.codecFilter);
/*     */       
/*  73 */       this.connector.setHandler(this.handler);
/*     */       
/*  75 */       ConnectFuture f = this.connector.connect(sa);
/*     */       
/*  77 */       f.awaitUninterruptibly(timeout);
/*     */       
/*  79 */       if (f.isConnected()) {
/*  80 */         Long sessionId = (Long)f.getSession().getAttribute(BaseIoHandler.SESSION_ID);
/*  81 */         this._session = this.handler.getSession(sessionId.longValue());
/*  82 */         CommLog.info("Connector {} Connnect to {} Success!", getClass().getSimpleName(), sa.toString());
/*     */       } else {
/*  84 */         CommLog.error("Connector {} Connnect to {} Failed!", getClass().getSimpleName(), sa.toString());
/*  85 */         onConnectFailed();
/*     */       } 
/*  87 */       return f.isConnected();
/*  88 */     } catch (Exception e) {
/*  89 */       CommLog.error("Connector {} Connnect to {} Failed! \n{}", new Object[] { getClass().getName(), sa.toString(), e });
/*     */       
/*  91 */       return false;
/*     */     } 
/*     */   }
/*     */   public BaseIoHandler<Session> getSocketHandler() {
/*  95 */     return this.handler;
/*     */   }
/*     */   
/*     */   protected void config(NioSocketConnector connector) {
/*  99 */     SocketSessionConfig sessioncfg = connector.getSessionConfig();
/* 100 */     sessioncfg.setReceiveBufferSize(8192);
/* 101 */     sessioncfg.setSendBufferSize(65536);
/* 102 */     sessioncfg.setKeepAlive(true);
/* 103 */     sessioncfg.setTcpNoDelay(false);
/*     */     
/* 105 */     IoBuffer.setUseDirectBuffer(false);
/* 106 */     IoBuffer.setAllocator((IoBufferAllocator)new SimpleBufferAllocator());
/*     */   }
/*     */   
/*     */   public boolean isConnected() {
/* 110 */     return (this._session != null && this._session.isConnected());
/*     */   }
/*     */   
/*     */   public void disconnect() {
/*     */     try {
/* 115 */       if (this.connector != null) {
/* 116 */         this.connector.dispose();
/*     */       }
/* 118 */       this._session = null;
/* 119 */     } catch (Throwable ex) {
/* 120 */       CommLog.error(null, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean reconnect(String ip, int port) {
/* 128 */     if (isConnected()) {
/* 129 */       return true;
/*     */     }
/* 131 */     this.ip = ip;
/* 132 */     this.port = port;
/*     */     
/* 134 */     disconnect();
/*     */     
/* 136 */     return connect(new InetSocketAddress(ip, port), 25000L);
/*     */   }
/*     */   
/*     */   protected void onConnectFailed() {
/* 140 */     SyncTaskManager.task(() -> reconnect(this.ip, this.port), 
/*     */         
/* 142 */         3000);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/BaseConnector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
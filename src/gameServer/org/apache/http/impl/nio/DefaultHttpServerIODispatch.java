/*     */ package org.apache.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.config.ConnectionConfig;
/*     */ import org.apache.http.impl.nio.reactor.AbstractIODispatch;
/*     */ import org.apache.http.nio.NHttpConnectionFactory;
/*     */ import org.apache.http.nio.NHttpServerConnection;
/*     */ import org.apache.http.nio.NHttpServerEventHandler;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.reactor.ssl.SSLSetupHandler;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.util.Args;
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
/*     */ @Immutable
/*     */ public class DefaultHttpServerIODispatch
/*     */   extends AbstractIODispatch<DefaultNHttpServerConnection>
/*     */ {
/*     */   private final NHttpServerEventHandler handler;
/*     */   private final NHttpConnectionFactory<? extends DefaultNHttpServerConnection> connFactory;
/*     */   
/*     */   public DefaultHttpServerIODispatch(NHttpServerEventHandler handler, NHttpConnectionFactory<? extends DefaultNHttpServerConnection> connFactory) {
/*  63 */     this.handler = (NHttpServerEventHandler)Args.notNull(handler, "HTTP client handler");
/*  64 */     this.connFactory = (NHttpConnectionFactory<? extends DefaultNHttpServerConnection>)Args.notNull(connFactory, "HTTP server connection factory");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public DefaultHttpServerIODispatch(NHttpServerEventHandler handler, HttpParams params) {
/*  75 */     this(handler, new DefaultNHttpServerConnectionFactory(params));
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
/*     */   @Deprecated
/*     */   public DefaultHttpServerIODispatch(NHttpServerEventHandler handler, SSLContext sslcontext, SSLSetupHandler sslHandler, HttpParams params) {
/*  88 */     this(handler, new SSLNHttpServerConnectionFactory(sslcontext, sslHandler, params));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public DefaultHttpServerIODispatch(NHttpServerEventHandler handler, SSLContext sslcontext, HttpParams params) {
/* 100 */     this(handler, sslcontext, (SSLSetupHandler)null, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttpServerIODispatch(NHttpServerEventHandler handler, ConnectionConfig config) {
/* 107 */     this(handler, new DefaultNHttpServerConnectionFactory(config));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttpServerIODispatch(NHttpServerEventHandler handler, SSLContext sslcontext, SSLSetupHandler sslHandler, ConnectionConfig config) {
/* 118 */     this(handler, new SSLNHttpServerConnectionFactory(sslcontext, sslHandler, config));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttpServerIODispatch(NHttpServerEventHandler handler, SSLContext sslcontext, ConnectionConfig config) {
/* 128 */     this(handler, new SSLNHttpServerConnectionFactory(sslcontext, null, config));
/*     */   }
/*     */ 
/*     */   
/*     */   protected DefaultNHttpServerConnection createConnection(IOSession session) {
/* 133 */     return (DefaultNHttpServerConnection)this.connFactory.createConnection(session);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onConnected(DefaultNHttpServerConnection conn) {
/*     */     try {
/* 139 */       this.handler.connected((NHttpServerConnection)conn);
/* 140 */     } catch (Exception ex) {
/* 141 */       this.handler.exception((NHttpServerConnection)conn, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onClosed(DefaultNHttpServerConnection conn) {
/* 147 */     this.handler.closed((NHttpServerConnection)conn);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onException(DefaultNHttpServerConnection conn, IOException ex) {
/* 152 */     this.handler.exception((NHttpServerConnection)conn, ex);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onInputReady(DefaultNHttpServerConnection conn) {
/* 157 */     conn.consumeInput(this.handler);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onOutputReady(DefaultNHttpServerConnection conn) {
/* 162 */     conn.produceOutput(this.handler);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onTimeout(DefaultNHttpServerConnection conn) {
/*     */     try {
/* 168 */       this.handler.timeout((NHttpServerConnection)conn);
/* 169 */     } catch (Exception ex) {
/* 170 */       this.handler.exception((NHttpServerConnection)conn, ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/DefaultHttpServerIODispatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
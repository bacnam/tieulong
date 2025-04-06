/*     */ package org.apache.http.impl.nio.ssl;
/*     */ 
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLException;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.impl.nio.DefaultServerIOEventDispatch;
/*     */ import org.apache.http.impl.nio.reactor.SSLIOSession;
/*     */ import org.apache.http.impl.nio.reactor.SSLSetupHandler;
/*     */ import org.apache.http.nio.NHttpServerConnection;
/*     */ import org.apache.http.nio.NHttpServerIOTarget;
/*     */ import org.apache.http.nio.NHttpServiceHandler;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.params.HttpConnectionParams;
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
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public class SSLServerIOEventDispatch
/*     */   extends DefaultServerIOEventDispatch
/*     */ {
/*     */   private final SSLContext sslcontext;
/*     */   private final SSLSetupHandler sslHandler;
/*     */   
/*     */   public SSLServerIOEventDispatch(NHttpServiceHandler handler, SSLContext sslcontext, SSLSetupHandler sslHandler, HttpParams params) {
/*  75 */     super(handler, params);
/*  76 */     Args.notNull(sslcontext, "SSL context");
/*  77 */     Args.notNull(params, "HTTP parameters");
/*  78 */     this.sslcontext = sslcontext;
/*  79 */     this.sslHandler = sslHandler;
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
/*     */   
/*     */   public SSLServerIOEventDispatch(NHttpServiceHandler handler, SSLContext sslcontext, HttpParams params) {
/*  96 */     this(handler, sslcontext, (SSLSetupHandler)null, params);
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected SSLIOSession createSSLIOSession(IOSession session, SSLContext sslcontext, SSLSetupHandler sslHandler) {
/* 115 */     return new SSLIOSession(session, sslcontext, sslHandler);
/*     */   }
/*     */   
/*     */   protected NHttpServerIOTarget createSSLConnection(SSLIOSession ssliosession) {
/* 119 */     return super.createConnection((IOSession)ssliosession);
/*     */   }
/*     */ 
/*     */   
/*     */   protected NHttpServerIOTarget createConnection(IOSession session) {
/* 124 */     SSLIOSession ssliosession = createSSLIOSession(session, this.sslcontext, this.sslHandler);
/* 125 */     session.setAttribute("http.session.ssl", ssliosession);
/* 126 */     NHttpServerIOTarget conn = createSSLConnection(ssliosession);
/*     */     try {
/* 128 */       ssliosession.initialize();
/* 129 */     } catch (SSLException ex) {
/* 130 */       this.handler.exception((NHttpServerConnection)conn, ex);
/* 131 */       ssliosession.shutdown();
/*     */     } 
/* 133 */     return conn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onConnected(NHttpServerIOTarget conn) {
/* 138 */     int timeout = HttpConnectionParams.getSoTimeout(this.params);
/* 139 */     conn.setSocketTimeout(timeout);
/* 140 */     this.handler.connected((NHttpServerConnection)conn);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/ssl/SSLServerIOEventDispatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
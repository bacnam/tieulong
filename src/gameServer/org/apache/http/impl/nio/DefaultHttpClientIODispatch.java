/*     */ package org.apache.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.config.ConnectionConfig;
/*     */ import org.apache.http.impl.nio.reactor.AbstractIODispatch;
/*     */ import org.apache.http.nio.NHttpClientConnection;
/*     */ import org.apache.http.nio.NHttpClientEventHandler;
/*     */ import org.apache.http.nio.NHttpConnectionFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class DefaultHttpClientIODispatch
/*     */   extends AbstractIODispatch<DefaultNHttpClientConnection>
/*     */ {
/*     */   private final NHttpClientEventHandler handler;
/*     */   private final NHttpConnectionFactory<DefaultNHttpClientConnection> connFactory;
/*     */   
/*     */   public DefaultHttpClientIODispatch(NHttpClientEventHandler handler, NHttpConnectionFactory<DefaultNHttpClientConnection> connFactory) {
/*  70 */     this.handler = (NHttpClientEventHandler)Args.notNull(handler, "HTTP client handler");
/*  71 */     this.connFactory = (NHttpConnectionFactory<DefaultNHttpClientConnection>)Args.notNull(connFactory, "HTTP client connection factory");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public DefaultHttpClientIODispatch(NHttpClientEventHandler handler, HttpParams params) {
/*  82 */     this(handler, new DefaultNHttpClientConnectionFactory(params));
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
/*     */   public DefaultHttpClientIODispatch(NHttpClientEventHandler handler, SSLContext sslcontext, SSLSetupHandler sslHandler, HttpParams params) {
/*  95 */     this(handler, new SSLNHttpClientConnectionFactory(sslcontext, sslHandler, params));
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
/*     */   public DefaultHttpClientIODispatch(NHttpClientEventHandler handler, SSLContext sslcontext, HttpParams params) {
/* 107 */     this(handler, sslcontext, (SSLSetupHandler)null, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttpClientIODispatch(NHttpClientEventHandler handler, ConnectionConfig config) {
/* 114 */     this(handler, new DefaultNHttpClientConnectionFactory(config));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttpClientIODispatch(NHttpClientEventHandler handler, SSLContext sslcontext, SSLSetupHandler sslHandler, ConnectionConfig config) {
/* 125 */     this(handler, new SSLNHttpClientConnectionFactory(sslcontext, sslHandler, config));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttpClientIODispatch(NHttpClientEventHandler handler, SSLContext sslcontext, ConnectionConfig config) {
/* 135 */     this(handler, new SSLNHttpClientConnectionFactory(sslcontext, null, config));
/*     */   }
/*     */ 
/*     */   
/*     */   protected DefaultNHttpClientConnection createConnection(IOSession session) {
/* 140 */     return (DefaultNHttpClientConnection)this.connFactory.createConnection(session);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onConnected(DefaultNHttpClientConnection conn) {
/* 145 */     Object attachment = conn.getContext().getAttribute("http.session.attachment");
/*     */     try {
/* 147 */       this.handler.connected((NHttpClientConnection)conn, attachment);
/* 148 */     } catch (Exception ex) {
/* 149 */       this.handler.exception((NHttpClientConnection)conn, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onClosed(DefaultNHttpClientConnection conn) {
/* 155 */     this.handler.closed((NHttpClientConnection)conn);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onException(DefaultNHttpClientConnection conn, IOException ex) {
/* 160 */     this.handler.exception((NHttpClientConnection)conn, ex);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onInputReady(DefaultNHttpClientConnection conn) {
/* 165 */     conn.consumeInput(this.handler);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onOutputReady(DefaultNHttpClientConnection conn) {
/* 170 */     conn.produceOutput(this.handler);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onTimeout(DefaultNHttpClientConnection conn) {
/*     */     try {
/* 176 */       this.handler.timeout((NHttpClientConnection)conn);
/* 177 */     } catch (Exception ex) {
/* 178 */       this.handler.exception((NHttpClientConnection)conn, ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/DefaultHttpClientIODispatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
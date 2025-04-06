/*     */ package org.apache.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLException;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.impl.DefaultHttpResponseFactory;
/*     */ import org.apache.http.impl.nio.reactor.SSLIOSession;
/*     */ import org.apache.http.impl.nio.reactor.SSLIOSessionHandler;
/*     */ import org.apache.http.impl.nio.reactor.SSLMode;
/*     */ import org.apache.http.nio.NHttpClientConnection;
/*     */ import org.apache.http.nio.NHttpClientHandler;
/*     */ import org.apache.http.nio.NHttpClientIOTarget;
/*     */ import org.apache.http.nio.reactor.IOEventDispatch;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.util.ByteBufferAllocator;
/*     */ import org.apache.http.nio.util.HeapByteBufferAllocator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class SSLClientIOEventDispatch
/*     */   implements IOEventDispatch
/*     */ {
/*     */   private static final String SSL_SESSION = "SSL_SESSION";
/*     */   protected final NHttpClientHandler handler;
/*     */   protected final SSLContext sslcontext;
/*     */   protected final SSLIOSessionHandler sslHandler;
/*     */   protected final HttpParams params;
/*     */   
/*     */   public SSLClientIOEventDispatch(NHttpClientHandler handler, SSLContext sslcontext, SSLIOSessionHandler sslHandler, HttpParams params) {
/*  85 */     Args.notNull(handler, "HTTP client handler");
/*  86 */     Args.notNull(sslcontext, "SSL context");
/*  87 */     Args.notNull(params, "HTTP parameters");
/*  88 */     this.handler = handler;
/*  89 */     this.params = params;
/*  90 */     this.sslcontext = sslcontext;
/*  91 */     this.sslHandler = sslHandler;
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
/*     */   public SSLClientIOEventDispatch(NHttpClientHandler handler, SSLContext sslcontext, HttpParams params) {
/* 108 */     this(handler, sslcontext, null, params);
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
/*     */   protected ByteBufferAllocator createByteBufferAllocator() {
/* 121 */     return (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE;
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
/*     */   protected HttpResponseFactory createHttpResponseFactory() {
/* 135 */     return (HttpResponseFactory)DefaultHttpResponseFactory.INSTANCE;
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
/*     */   protected NHttpClientIOTarget createConnection(IOSession session) {
/* 150 */     return new DefaultNHttpClientConnection(session, createHttpResponseFactory(), createByteBufferAllocator(), this.params);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SSLIOSession createSSLIOSession(IOSession session, SSLContext sslcontext, SSLIOSessionHandler sslHandler) {
/* 173 */     return new SSLIOSession(session, sslcontext, sslHandler);
/*     */   }
/*     */ 
/*     */   
/*     */   public void connected(IOSession session) {
/* 178 */     SSLIOSession sslSession = createSSLIOSession(session, this.sslcontext, this.sslHandler);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     NHttpClientIOTarget conn = createConnection((IOSession)sslSession);
/*     */ 
/*     */     
/* 186 */     session.setAttribute("http.connection", conn);
/* 187 */     session.setAttribute("SSL_SESSION", sslSession);
/*     */     
/* 189 */     Object attachment = session.getAttribute("http.session.attachment");
/* 190 */     this.handler.connected((NHttpClientConnection)conn, attachment);
/*     */     
/*     */     try {
/* 193 */       sslSession.bind(SSLMode.CLIENT, this.params);
/* 194 */     } catch (SSLException ex) {
/* 195 */       this.handler.exception((NHttpClientConnection)conn, ex);
/* 196 */       sslSession.shutdown();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void disconnected(IOSession session) {
/* 201 */     NHttpClientIOTarget conn = (NHttpClientIOTarget)session.getAttribute("http.connection");
/*     */     
/* 203 */     if (conn != null) {
/* 204 */       this.handler.closed((NHttpClientConnection)conn);
/*     */     }
/*     */   }
/*     */   
/*     */   public void inputReady(IOSession session) {
/* 209 */     NHttpClientIOTarget conn = (NHttpClientIOTarget)session.getAttribute("http.connection");
/*     */     
/* 211 */     SSLIOSession sslSession = (SSLIOSession)session.getAttribute("SSL_SESSION");
/*     */ 
/*     */     
/*     */     try {
/* 215 */       if (sslSession.isAppInputReady()) {
/* 216 */         conn.consumeInput(this.handler);
/*     */       }
/* 218 */       sslSession.inboundTransport();
/* 219 */     } catch (IOException ex) {
/* 220 */       this.handler.exception((NHttpClientConnection)conn, ex);
/* 221 */       sslSession.shutdown();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void outputReady(IOSession session) {
/* 226 */     NHttpClientIOTarget conn = (NHttpClientIOTarget)session.getAttribute("http.connection");
/*     */     
/* 228 */     SSLIOSession sslSession = (SSLIOSession)session.getAttribute("SSL_SESSION");
/*     */ 
/*     */     
/*     */     try {
/* 232 */       if (sslSession.isAppOutputReady()) {
/* 233 */         conn.produceOutput(this.handler);
/*     */       }
/* 235 */       sslSession.outboundTransport();
/* 236 */     } catch (IOException ex) {
/* 237 */       this.handler.exception((NHttpClientConnection)conn, ex);
/* 238 */       sslSession.shutdown();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void timeout(IOSession session) {
/* 243 */     NHttpClientIOTarget conn = (NHttpClientIOTarget)session.getAttribute("http.connection");
/*     */     
/* 245 */     SSLIOSession sslSession = (SSLIOSession)session.getAttribute("SSL_SESSION");
/*     */ 
/*     */     
/* 248 */     this.handler.timeout((NHttpClientConnection)conn);
/* 249 */     synchronized (sslSession) {
/* 250 */       if (sslSession.isOutboundDone() && !sslSession.isInboundDone())
/*     */       {
/* 252 */         sslSession.shutdown();
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/SSLClientIOEventDispatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
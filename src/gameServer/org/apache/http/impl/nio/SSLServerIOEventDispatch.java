/*     */ package org.apache.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLException;
/*     */ import org.apache.http.HttpRequestFactory;
/*     */ import org.apache.http.impl.DefaultHttpRequestFactory;
/*     */ import org.apache.http.impl.nio.reactor.SSLIOSession;
/*     */ import org.apache.http.impl.nio.reactor.SSLIOSessionHandler;
/*     */ import org.apache.http.impl.nio.reactor.SSLMode;
/*     */ import org.apache.http.nio.NHttpServerConnection;
/*     */ import org.apache.http.nio.NHttpServerIOTarget;
/*     */ import org.apache.http.nio.NHttpServiceHandler;
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
/*     */ public class SSLServerIOEventDispatch
/*     */   implements IOEventDispatch
/*     */ {
/*     */   private static final String SSL_SESSION = "SSL_SESSION";
/*     */   protected final NHttpServiceHandler handler;
/*     */   protected final SSLContext sslcontext;
/*     */   protected final SSLIOSessionHandler sslHandler;
/*     */   protected final HttpParams params;
/*     */   
/*     */   public SSLServerIOEventDispatch(NHttpServiceHandler handler, SSLContext sslcontext, SSLIOSessionHandler sslHandler, HttpParams params) {
/*  85 */     Args.notNull(handler, "HTTP service handler");
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
/*     */   public SSLServerIOEventDispatch(NHttpServiceHandler handler, SSLContext sslcontext, HttpParams params) {
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
/*     */   protected HttpRequestFactory createHttpRequestFactory() {
/* 135 */     return (HttpRequestFactory)DefaultHttpRequestFactory.INSTANCE;
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
/*     */   protected NHttpServerIOTarget createConnection(IOSession session) {
/* 150 */     return new DefaultNHttpServerConnection(session, createHttpRequestFactory(), createByteBufferAllocator(), this.params);
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
/* 183 */     NHttpServerIOTarget conn = createConnection((IOSession)sslSession);
/*     */ 
/*     */     
/* 186 */     session.setAttribute("http.connection", conn);
/* 187 */     session.setAttribute("SSL_SESSION", sslSession);
/*     */     
/* 189 */     this.handler.connected((NHttpServerConnection)conn);
/*     */     
/*     */     try {
/* 192 */       sslSession.bind(SSLMode.SERVER, this.params);
/* 193 */     } catch (SSLException ex) {
/* 194 */       this.handler.exception((NHttpServerConnection)conn, ex);
/* 195 */       sslSession.shutdown();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void disconnected(IOSession session) {
/* 200 */     NHttpServerIOTarget conn = (NHttpServerIOTarget)session.getAttribute("http.connection");
/*     */ 
/*     */     
/* 203 */     if (conn != null) {
/* 204 */       this.handler.closed((NHttpServerConnection)conn);
/*     */     }
/*     */   }
/*     */   
/*     */   public void inputReady(IOSession session) {
/* 209 */     NHttpServerIOTarget conn = (NHttpServerIOTarget)session.getAttribute("http.connection");
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
/* 220 */       this.handler.exception((NHttpServerConnection)conn, ex);
/* 221 */       sslSession.shutdown();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void outputReady(IOSession session) {
/* 226 */     NHttpServerIOTarget conn = (NHttpServerIOTarget)session.getAttribute("http.connection");
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
/* 237 */       this.handler.exception((NHttpServerConnection)conn, ex);
/* 238 */       sslSession.shutdown();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void timeout(IOSession session) {
/* 243 */     NHttpServerIOTarget conn = (NHttpServerIOTarget)session.getAttribute("http.connection");
/*     */     
/* 245 */     SSLIOSession sslSession = (SSLIOSession)session.getAttribute("SSL_SESSION");
/*     */ 
/*     */     
/* 248 */     this.handler.timeout((NHttpServerConnection)conn);
/* 249 */     synchronized (sslSession) {
/* 250 */       if (sslSession.isOutboundDone() && !sslSession.isInboundDone())
/*     */       {
/* 252 */         sslSession.shutdown();
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/SSLServerIOEventDispatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
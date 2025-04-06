/*     */ package org.apache.http.impl.nio.pool;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.config.ConnectionConfig;
/*     */ import org.apache.http.impl.DefaultHttpResponseFactory;
/*     */ import org.apache.http.impl.nio.DefaultNHttpClientConnectionFactory;
/*     */ import org.apache.http.impl.nio.SSLNHttpClientConnectionFactory;
/*     */ import org.apache.http.nio.NHttpClientConnection;
/*     */ import org.apache.http.nio.NHttpConnectionFactory;
/*     */ import org.apache.http.nio.NHttpMessageParserFactory;
/*     */ import org.apache.http.nio.NHttpMessageWriterFactory;
/*     */ import org.apache.http.nio.pool.NIOConnFactory;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.reactor.ssl.SSLSetupHandler;
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
/*     */ @Immutable
/*     */ public class BasicNIOConnFactory
/*     */   implements NIOConnFactory<HttpHost, NHttpClientConnection>
/*     */ {
/*     */   private final NHttpConnectionFactory<? extends NHttpClientConnection> plainFactory;
/*     */   private final NHttpConnectionFactory<? extends NHttpClientConnection> sslFactory;
/*     */   
/*     */   public BasicNIOConnFactory(NHttpConnectionFactory<? extends NHttpClientConnection> plainFactory, NHttpConnectionFactory<? extends NHttpClientConnection> sslFactory) {
/*  72 */     Args.notNull(plainFactory, "Plain HTTP client connection factory");
/*  73 */     this.plainFactory = plainFactory;
/*  74 */     this.sslFactory = sslFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicNIOConnFactory(NHttpConnectionFactory<? extends NHttpClientConnection> plainFactory) {
/*  79 */     this(plainFactory, null);
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
/*     */   @Deprecated
/*     */   public BasicNIOConnFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, HttpResponseFactory responseFactory, ByteBufferAllocator allocator, HttpParams params) {
/*  94 */     this((NHttpConnectionFactory<? extends NHttpClientConnection>)new DefaultNHttpClientConnectionFactory(responseFactory, allocator, params), (NHttpConnectionFactory<? extends NHttpClientConnection>)new SSLNHttpClientConnectionFactory(sslcontext, sslHandler, responseFactory, allocator, params));
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
/*     */   @Deprecated
/*     */   public BasicNIOConnFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, HttpParams params) {
/* 109 */     this(sslcontext, sslHandler, (HttpResponseFactory)DefaultHttpResponseFactory.INSTANCE, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public BasicNIOConnFactory(HttpParams params) {
/* 118 */     this((SSLContext)null, (SSLSetupHandler)null, params);
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
/*     */   public BasicNIOConnFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, NHttpMessageParserFactory<HttpResponse> responseParserFactory, NHttpMessageWriterFactory<HttpRequest> requestWriterFactory, ByteBufferAllocator allocator, ConnectionConfig config) {
/* 131 */     this((NHttpConnectionFactory<? extends NHttpClientConnection>)new DefaultNHttpClientConnectionFactory(responseParserFactory, requestWriterFactory, allocator, config), (NHttpConnectionFactory<? extends NHttpClientConnection>)new SSLNHttpClientConnectionFactory(sslcontext, sslHandler, responseParserFactory, requestWriterFactory, allocator, config));
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
/*     */   public BasicNIOConnFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, ConnectionConfig config) {
/* 145 */     this(sslcontext, sslHandler, null, null, null, config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicNIOConnFactory(ConnectionConfig config) {
/* 152 */     this((NHttpConnectionFactory<? extends NHttpClientConnection>)new DefaultNHttpClientConnectionFactory(config), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public NHttpClientConnection create(HttpHost route, IOSession session) throws IOException {
/*     */     NHttpClientConnection conn;
/* 158 */     if (route.getSchemeName().equalsIgnoreCase("https")) {
/* 159 */       if (this.sslFactory == null) {
/* 160 */         throw new IOException("SSL not supported");
/*     */       }
/* 162 */       conn = (NHttpClientConnection)this.sslFactory.createConnection(session);
/*     */     } else {
/* 164 */       conn = (NHttpClientConnection)this.plainFactory.createConnection(session);
/*     */     } 
/* 166 */     session.setAttribute("http.connection", conn);
/* 167 */     return conn;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/pool/BasicNIOConnFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
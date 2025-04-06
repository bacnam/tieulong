/*     */ package org.apache.http.impl.nio;
/*     */ 
/*     */ import javax.net.ssl.SSLContext;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestFactory;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.config.ConnectionConfig;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.impl.ConnSupport;
/*     */ import org.apache.http.impl.DefaultHttpRequestFactory;
/*     */ import org.apache.http.impl.nio.codecs.DefaultHttpRequestParserFactory;
/*     */ import org.apache.http.nio.NHttpConnection;
/*     */ import org.apache.http.nio.NHttpConnectionFactory;
/*     */ import org.apache.http.nio.NHttpMessageParserFactory;
/*     */ import org.apache.http.nio.NHttpMessageWriterFactory;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.reactor.ssl.SSLIOSession;
/*     */ import org.apache.http.nio.reactor.ssl.SSLMode;
/*     */ import org.apache.http.nio.reactor.ssl.SSLSetupHandler;
/*     */ import org.apache.http.nio.util.ByteBufferAllocator;
/*     */ import org.apache.http.nio.util.HeapByteBufferAllocator;
/*     */ import org.apache.http.params.HttpParamConfig;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.ssl.SSLContexts;
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
/*     */ @Immutable
/*     */ public class SSLNHttpServerConnectionFactory
/*     */   implements NHttpConnectionFactory<DefaultNHttpServerConnection>
/*     */ {
/*     */   private final SSLContext sslcontext;
/*     */   private final SSLSetupHandler sslHandler;
/*     */   private final ContentLengthStrategy incomingContentStrategy;
/*     */   private final ContentLengthStrategy outgoingContentStrategy;
/*     */   private final NHttpMessageParserFactory<HttpRequest> requestParserFactory;
/*     */   private final NHttpMessageWriterFactory<HttpResponse> responseWriterFactory;
/*     */   private final ByteBufferAllocator allocator;
/*     */   private final ConnectionConfig cconfig;
/*     */   
/*     */   @Deprecated
/*     */   public SSLNHttpServerConnectionFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, HttpRequestFactory requestFactory, ByteBufferAllocator allocator, HttpParams params) {
/*  88 */     Args.notNull(requestFactory, "HTTP request factory");
/*  89 */     Args.notNull(allocator, "Byte buffer allocator");
/*  90 */     Args.notNull(params, "HTTP parameters");
/*  91 */     this.sslcontext = (sslcontext != null) ? sslcontext : SSLContexts.createSystemDefault();
/*  92 */     this.sslHandler = sslHandler;
/*  93 */     this.incomingContentStrategy = null;
/*  94 */     this.outgoingContentStrategy = null;
/*  95 */     this.requestParserFactory = (NHttpMessageParserFactory<HttpRequest>)new DefaultHttpRequestParserFactory(null, requestFactory);
/*  96 */     this.responseWriterFactory = null;
/*  97 */     this.allocator = allocator;
/*  98 */     this.cconfig = HttpParamConfig.getConnectionConfig(params);
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
/*     */   public SSLNHttpServerConnectionFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, HttpParams params) {
/* 111 */     this(sslcontext, sslHandler, (HttpRequestFactory)DefaultHttpRequestFactory.INSTANCE, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public SSLNHttpServerConnectionFactory(HttpParams params) {
/* 121 */     this((SSLContext)null, (SSLSetupHandler)null, params);
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
/*     */   public SSLNHttpServerConnectionFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, NHttpMessageParserFactory<HttpRequest> requestParserFactory, NHttpMessageWriterFactory<HttpResponse> responseWriterFactory, ByteBufferAllocator allocator, ConnectionConfig cconfig) {
/* 137 */     this.sslcontext = (sslcontext != null) ? sslcontext : SSLContexts.createSystemDefault();
/* 138 */     this.sslHandler = sslHandler;
/* 139 */     this.incomingContentStrategy = incomingContentStrategy;
/* 140 */     this.outgoingContentStrategy = outgoingContentStrategy;
/* 141 */     this.requestParserFactory = requestParserFactory;
/* 142 */     this.responseWriterFactory = responseWriterFactory;
/* 143 */     this.allocator = allocator;
/* 144 */     this.cconfig = (cconfig != null) ? cconfig : ConnectionConfig.DEFAULT;
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
/*     */   public SSLNHttpServerConnectionFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, NHttpMessageParserFactory<HttpRequest> requestParserFactory, NHttpMessageWriterFactory<HttpResponse> responseWriterFactory, ByteBufferAllocator allocator, ConnectionConfig cconfig) {
/* 157 */     this(sslcontext, sslHandler, null, null, requestParserFactory, responseWriterFactory, allocator, cconfig);
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
/*     */   public SSLNHttpServerConnectionFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, NHttpMessageParserFactory<HttpRequest> requestParserFactory, NHttpMessageWriterFactory<HttpResponse> responseWriterFactory, ConnectionConfig cconfig) {
/* 170 */     this(sslcontext, sslHandler, null, null, requestParserFactory, responseWriterFactory, null, cconfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLNHttpServerConnectionFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, ConnectionConfig config) {
/* 181 */     this(sslcontext, sslHandler, null, null, null, null, null, config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLNHttpServerConnectionFactory(ConnectionConfig config) {
/* 188 */     this(null, null, null, null, null, null, null, config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLNHttpServerConnectionFactory() {
/* 195 */     this(null, null, null, null, null, null, null, null);
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
/*     */   protected DefaultNHttpServerConnection createConnection(IOSession session, HttpRequestFactory requestFactory, ByteBufferAllocator allocator, HttpParams params) {
/* 207 */     return new DefaultNHttpServerConnection(session, requestFactory, allocator, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SSLIOSession createSSLIOSession(IOSession iosession, SSLContext sslcontext, SSLSetupHandler sslHandler) {
/* 217 */     SSLIOSession ssliosession = new SSLIOSession(iosession, SSLMode.SERVER, sslcontext, sslHandler);
/*     */     
/* 219 */     return ssliosession;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultNHttpServerConnection createConnection(IOSession iosession) {
/* 224 */     SSLIOSession ssliosession = createSSLIOSession(iosession, this.sslcontext, this.sslHandler);
/* 225 */     iosession.setAttribute("http.session.ssl", ssliosession);
/* 226 */     return new DefaultNHttpServerConnection((IOSession)ssliosession, this.cconfig.getBufferSize(), this.cconfig.getFragmentSizeHint(), this.allocator, ConnSupport.createDecoder(this.cconfig), ConnSupport.createEncoder(this.cconfig), this.cconfig.getMessageConstraints(), this.incomingContentStrategy, this.outgoingContentStrategy, this.requestParserFactory, this.responseWriterFactory);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/SSLNHttpServerConnectionFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
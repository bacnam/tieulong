/*     */ package org.apache.http.impl.nio;
/*     */ 
/*     */ import javax.net.ssl.SSLContext;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.config.ConnectionConfig;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.impl.ConnSupport;
/*     */ import org.apache.http.impl.DefaultHttpResponseFactory;
/*     */ import org.apache.http.impl.nio.codecs.DefaultHttpResponseParserFactory;
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
/*     */ @Immutable
/*     */ public class SSLNHttpClientConnectionFactory
/*     */   implements NHttpConnectionFactory<DefaultNHttpClientConnection>
/*     */ {
/*  65 */   public static final SSLNHttpClientConnectionFactory INSTANCE = new SSLNHttpClientConnectionFactory();
/*     */ 
/*     */   
/*     */   private final ContentLengthStrategy incomingContentStrategy;
/*     */ 
/*     */   
/*     */   private final ContentLengthStrategy outgoingContentStrategy;
/*     */ 
/*     */   
/*     */   private final NHttpMessageParserFactory<HttpResponse> responseParserFactory;
/*     */ 
/*     */   
/*     */   private final NHttpMessageWriterFactory<HttpRequest> requestWriterFactory;
/*     */   
/*     */   private final ByteBufferAllocator allocator;
/*     */   
/*     */   private final SSLContext sslcontext;
/*     */   
/*     */   private final SSLSetupHandler sslHandler;
/*     */   
/*     */   private final ConnectionConfig cconfig;
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public SSLNHttpClientConnectionFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, HttpResponseFactory responseFactory, ByteBufferAllocator allocator, HttpParams params) {
/*  90 */     Args.notNull(responseFactory, "HTTP response factory");
/*  91 */     Args.notNull(allocator, "Byte buffer allocator");
/*  92 */     Args.notNull(params, "HTTP parameters");
/*  93 */     this.sslcontext = (sslcontext != null) ? sslcontext : SSLContexts.createSystemDefault();
/*  94 */     this.sslHandler = sslHandler;
/*  95 */     this.allocator = allocator;
/*  96 */     this.incomingContentStrategy = null;
/*  97 */     this.outgoingContentStrategy = null;
/*  98 */     this.responseParserFactory = (NHttpMessageParserFactory<HttpResponse>)new DefaultHttpResponseParserFactory(null, responseFactory);
/*  99 */     this.requestWriterFactory = null;
/* 100 */     this.cconfig = HttpParamConfig.getConnectionConfig(params);
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
/*     */   public SSLNHttpClientConnectionFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, HttpParams params) {
/* 113 */     this(sslcontext, sslHandler, (HttpResponseFactory)DefaultHttpResponseFactory.INSTANCE, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public SSLNHttpClientConnectionFactory(HttpParams params) {
/* 123 */     this((SSLContext)null, (SSLSetupHandler)null, params);
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
/*     */   public SSLNHttpClientConnectionFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, NHttpMessageParserFactory<HttpResponse> responseParserFactory, NHttpMessageWriterFactory<HttpRequest> requestWriterFactory, ByteBufferAllocator allocator, ConnectionConfig cconfig) {
/* 139 */     this.sslcontext = (sslcontext != null) ? sslcontext : SSLContexts.createSystemDefault();
/* 140 */     this.sslHandler = sslHandler;
/* 141 */     this.incomingContentStrategy = incomingContentStrategy;
/* 142 */     this.outgoingContentStrategy = outgoingContentStrategy;
/* 143 */     this.responseParserFactory = responseParserFactory;
/* 144 */     this.requestWriterFactory = requestWriterFactory;
/* 145 */     this.allocator = allocator;
/* 146 */     this.cconfig = (cconfig != null) ? cconfig : ConnectionConfig.DEFAULT;
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
/*     */   public SSLNHttpClientConnectionFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, NHttpMessageParserFactory<HttpResponse> responseParserFactory, NHttpMessageWriterFactory<HttpRequest> requestWriterFactory, ByteBufferAllocator allocator, ConnectionConfig cconfig) {
/* 159 */     this(sslcontext, sslHandler, null, null, responseParserFactory, requestWriterFactory, allocator, cconfig);
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
/*     */   public SSLNHttpClientConnectionFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, NHttpMessageParserFactory<HttpResponse> responseParserFactory, NHttpMessageWriterFactory<HttpRequest> requestWriterFactory, ConnectionConfig cconfig) {
/* 172 */     this(sslcontext, sslHandler, null, null, responseParserFactory, requestWriterFactory, null, cconfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLNHttpClientConnectionFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, ConnectionConfig config) {
/* 183 */     this(sslcontext, sslHandler, null, null, null, null, null, config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLNHttpClientConnectionFactory(ConnectionConfig config) {
/* 190 */     this(null, null, null, null, null, null, null, config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLNHttpClientConnectionFactory() {
/* 197 */     this(null, null, null, null, null, null);
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
/*     */   protected DefaultNHttpClientConnection createConnection(IOSession session, HttpResponseFactory responseFactory, ByteBufferAllocator allocator, HttpParams params) {
/* 209 */     return new DefaultNHttpClientConnection(session, responseFactory, allocator, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SSLIOSession createSSLIOSession(IOSession iosession, SSLContext sslcontext, SSLSetupHandler sslHandler) {
/* 219 */     SSLIOSession ssliosession = new SSLIOSession(iosession, SSLMode.CLIENT, sslcontext, sslHandler);
/*     */     
/* 221 */     return ssliosession;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultNHttpClientConnection createConnection(IOSession iosession) {
/* 226 */     SSLIOSession ssliosession = createSSLIOSession(iosession, this.sslcontext, this.sslHandler);
/* 227 */     iosession.setAttribute("http.session.ssl", ssliosession);
/* 228 */     return new DefaultNHttpClientConnection((IOSession)ssliosession, this.cconfig.getBufferSize(), this.cconfig.getFragmentSizeHint(), this.allocator, ConnSupport.createDecoder(this.cconfig), ConnSupport.createEncoder(this.cconfig), this.cconfig.getMessageConstraints(), this.incomingContentStrategy, this.outgoingContentStrategy, this.requestWriterFactory, this.responseParserFactory);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/SSLNHttpClientConnectionFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
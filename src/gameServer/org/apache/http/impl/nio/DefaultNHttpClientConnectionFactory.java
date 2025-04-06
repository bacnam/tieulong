/*     */ package org.apache.http.impl.nio;
/*     */ 
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
/*     */ import org.apache.http.nio.util.ByteBufferAllocator;
/*     */ import org.apache.http.nio.util.HeapByteBufferAllocator;
/*     */ import org.apache.http.params.HttpParamConfig;
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
/*     */ @Immutable
/*     */ public class DefaultNHttpClientConnectionFactory
/*     */   implements NHttpConnectionFactory<DefaultNHttpClientConnection>
/*     */ {
/*  59 */   public static final DefaultNHttpClientConnectionFactory INSTANCE = new DefaultNHttpClientConnectionFactory();
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
/*     */   private final ConnectionConfig cconfig;
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public DefaultNHttpClientConnectionFactory(HttpResponseFactory responseFactory, ByteBufferAllocator allocator, HttpParams params) {
/*  80 */     Args.notNull(responseFactory, "HTTP response factory");
/*  81 */     Args.notNull(allocator, "Byte buffer allocator");
/*  82 */     Args.notNull(params, "HTTP parameters");
/*  83 */     this.allocator = allocator;
/*  84 */     this.incomingContentStrategy = null;
/*  85 */     this.outgoingContentStrategy = null;
/*  86 */     this.responseParserFactory = (NHttpMessageParserFactory<HttpResponse>)new DefaultHttpResponseParserFactory(null, responseFactory);
/*  87 */     this.requestWriterFactory = null;
/*  88 */     this.cconfig = HttpParamConfig.getConnectionConfig(params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public DefaultNHttpClientConnectionFactory(HttpParams params) {
/*  98 */     this((HttpResponseFactory)DefaultHttpResponseFactory.INSTANCE, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE, params);
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
/*     */   public DefaultNHttpClientConnectionFactory(ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, NHttpMessageParserFactory<HttpResponse> responseParserFactory, NHttpMessageWriterFactory<HttpRequest> requestWriterFactory, ByteBufferAllocator allocator, ConnectionConfig cconfig) {
/* 112 */     this.incomingContentStrategy = incomingContentStrategy;
/* 113 */     this.outgoingContentStrategy = outgoingContentStrategy;
/* 114 */     this.responseParserFactory = responseParserFactory;
/* 115 */     this.requestWriterFactory = requestWriterFactory;
/* 116 */     this.allocator = allocator;
/* 117 */     this.cconfig = (cconfig != null) ? cconfig : ConnectionConfig.DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultNHttpClientConnectionFactory(NHttpMessageParserFactory<HttpResponse> responseParserFactory, NHttpMessageWriterFactory<HttpRequest> requestWriterFactory, ByteBufferAllocator allocator, ConnectionConfig cconfig) {
/* 128 */     this(null, null, responseParserFactory, requestWriterFactory, allocator, cconfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultNHttpClientConnectionFactory(NHttpMessageParserFactory<HttpResponse> responseParserFactory, NHttpMessageWriterFactory<HttpRequest> requestWriterFactory, ConnectionConfig cconfig) {
/* 138 */     this(null, null, responseParserFactory, requestWriterFactory, null, cconfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultNHttpClientConnectionFactory(ConnectionConfig cconfig) {
/* 145 */     this(null, null, null, null, null, cconfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultNHttpClientConnectionFactory() {
/* 152 */     this(null, null, null, null, null, null);
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
/* 164 */     return new DefaultNHttpClientConnection(session, responseFactory, allocator, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultNHttpClientConnection createConnection(IOSession session) {
/* 169 */     return new DefaultNHttpClientConnection(session, this.cconfig.getBufferSize(), this.cconfig.getFragmentSizeHint(), this.allocator, ConnSupport.createDecoder(this.cconfig), ConnSupport.createEncoder(this.cconfig), this.cconfig.getMessageConstraints(), this.incomingContentStrategy, this.outgoingContentStrategy, this.requestWriterFactory, this.responseParserFactory);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/DefaultNHttpClientConnectionFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
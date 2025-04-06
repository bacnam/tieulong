/*     */ package org.apache.http.impl.nio;
/*     */ 
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
/*     */ 
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
/*     */ public class DefaultNHttpServerConnectionFactory
/*     */   implements NHttpConnectionFactory<DefaultNHttpServerConnection>
/*     */ {
/*     */   private final ContentLengthStrategy incomingContentStrategy;
/*     */   private final ContentLengthStrategy outgoingContentStrategy;
/*     */   private final NHttpMessageParserFactory<HttpRequest> requestParserFactory;
/*     */   private final NHttpMessageWriterFactory<HttpResponse> responseWriterFactory;
/*     */   private final ByteBufferAllocator allocator;
/*     */   private final ConnectionConfig cconfig;
/*     */   
/*     */   @Deprecated
/*     */   public DefaultNHttpServerConnectionFactory(HttpRequestFactory requestFactory, ByteBufferAllocator allocator, HttpParams params) {
/*  78 */     Args.notNull(requestFactory, "HTTP request factory");
/*  79 */     Args.notNull(allocator, "Byte buffer allocator");
/*  80 */     Args.notNull(params, "HTTP parameters");
/*  81 */     this.incomingContentStrategy = null;
/*  82 */     this.outgoingContentStrategy = null;
/*  83 */     this.requestParserFactory = (NHttpMessageParserFactory<HttpRequest>)new DefaultHttpRequestParserFactory(null, requestFactory);
/*  84 */     this.responseWriterFactory = null;
/*  85 */     this.allocator = allocator;
/*  86 */     this.cconfig = HttpParamConfig.getConnectionConfig(params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public DefaultNHttpServerConnectionFactory(HttpParams params) {
/*  95 */     this((HttpRequestFactory)DefaultHttpRequestFactory.INSTANCE, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE, params);
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
/* 107 */     return new DefaultNHttpServerConnection(session, requestFactory, allocator, params);
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
/*     */   public DefaultNHttpServerConnectionFactory(ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, NHttpMessageParserFactory<HttpRequest> requestParserFactory, NHttpMessageWriterFactory<HttpResponse> responseWriterFactory, ByteBufferAllocator allocator, ConnectionConfig cconfig) {
/* 121 */     this.incomingContentStrategy = incomingContentStrategy;
/* 122 */     this.outgoingContentStrategy = outgoingContentStrategy;
/* 123 */     this.requestParserFactory = requestParserFactory;
/* 124 */     this.responseWriterFactory = responseWriterFactory;
/* 125 */     this.allocator = allocator;
/* 126 */     this.cconfig = (cconfig != null) ? cconfig : ConnectionConfig.DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultNHttpServerConnectionFactory(ByteBufferAllocator allocator, NHttpMessageParserFactory<HttpRequest> requestParserFactory, NHttpMessageWriterFactory<HttpResponse> responseWriterFactory, ConnectionConfig cconfig) {
/* 137 */     this(null, null, requestParserFactory, responseWriterFactory, allocator, cconfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultNHttpServerConnectionFactory(ConnectionConfig config) {
/* 144 */     this(null, null, null, null, null, config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultNHttpServerConnectionFactory() {
/* 151 */     this(null, null, null, null, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultNHttpServerConnection createConnection(IOSession session) {
/* 156 */     return new DefaultNHttpServerConnection(session, this.cconfig.getBufferSize(), this.cconfig.getFragmentSizeHint(), this.allocator, ConnSupport.createDecoder(this.cconfig), ConnSupport.createEncoder(this.cconfig), this.cconfig.getMessageConstraints(), this.incomingContentStrategy, this.outgoingContentStrategy, this.requestParserFactory, this.responseWriterFactory);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/DefaultNHttpServerConnectionFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
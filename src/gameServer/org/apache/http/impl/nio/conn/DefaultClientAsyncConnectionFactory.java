/*     */ package org.apache.http.impl.nio.conn;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.config.ConnectionConfig;
/*     */ import org.apache.http.impl.DefaultHttpResponseFactory;
/*     */ import org.apache.http.impl.nio.codecs.DefaultHttpResponseParserFactory;
/*     */ import org.apache.http.message.BasicLineParser;
/*     */ import org.apache.http.message.LineParser;
/*     */ import org.apache.http.nio.NHttpConnection;
/*     */ import org.apache.http.nio.NHttpMessageParserFactory;
/*     */ import org.apache.http.nio.conn.ClientAsyncConnection;
/*     */ import org.apache.http.nio.conn.ClientAsyncConnectionFactory;
/*     */ import org.apache.http.nio.conn.ManagedNHttpClientConnection;
/*     */ import org.apache.http.nio.conn.NHttpConnectionFactory;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.util.ByteBufferAllocator;
/*     */ import org.apache.http.nio.util.HeapByteBufferAllocator;
/*     */ import org.apache.http.params.HttpParams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class DefaultClientAsyncConnectionFactory
/*     */   implements ClientAsyncConnectionFactory, NHttpConnectionFactory<ManagedNHttpClientConnection>
/*     */ {
/*  58 */   private final Log headerlog = LogFactory.getLog("org.apache.http.headers");
/*  59 */   private final Log wirelog = LogFactory.getLog("org.apache.http.wire");
/*  60 */   private final Log log = LogFactory.getLog(ManagedNHttpClientConnectionImpl.class);
/*     */   
/*  62 */   public static final DefaultClientAsyncConnectionFactory INSTANCE = new DefaultClientAsyncConnectionFactory(null, null);
/*     */   
/*  64 */   private static AtomicLong COUNTER = new AtomicLong();
/*     */   
/*     */   private final HttpResponseFactory responseFactory;
/*     */   
/*     */   private final NHttpMessageParserFactory<HttpResponse> responseParserFactory;
/*     */   
/*     */   private final ByteBufferAllocator allocator;
/*     */ 
/*     */   
/*     */   public DefaultClientAsyncConnectionFactory(NHttpMessageParserFactory<HttpResponse> responseParserFactory, ByteBufferAllocator allocator) {
/*  74 */     this.responseFactory = createHttpResponseFactory();
/*  75 */     this.responseParserFactory = (responseParserFactory != null) ? responseParserFactory : (NHttpMessageParserFactory<HttpResponse>)DefaultHttpResponseParserFactory.INSTANCE;
/*     */     
/*  77 */     this.allocator = (allocator != null) ? allocator : (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultClientAsyncConnectionFactory() {
/*  82 */     this.responseFactory = createHttpResponseFactory();
/*  83 */     this.responseParserFactory = (NHttpMessageParserFactory<HttpResponse>)new DefaultHttpResponseParserFactory((LineParser)BasicLineParser.INSTANCE, this.responseFactory);
/*     */     
/*  85 */     this.allocator = createByteBufferAllocator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ClientAsyncConnection create(String id, IOSession iosession, HttpParams params) {
/*  93 */     return new DefaultClientAsyncConnection(id, iosession, this.responseFactory, this.allocator, params);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected ByteBufferAllocator createByteBufferAllocator() {
/*  99 */     return (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected HttpResponseFactory createHttpResponseFactory() {
/* 104 */     return (HttpResponseFactory)DefaultHttpResponseFactory.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public ManagedNHttpClientConnection create(IOSession iosession, ConnectionConfig config) {
/* 109 */     String id = "http-outgoing-" + Long.toString(COUNTER.getAndIncrement());
/* 110 */     CharsetDecoder chardecoder = null;
/* 111 */     CharsetEncoder charencoder = null;
/* 112 */     Charset charset = config.getCharset();
/* 113 */     CodingErrorAction malformedInputAction = (config.getMalformedInputAction() != null) ? config.getMalformedInputAction() : CodingErrorAction.REPORT;
/*     */     
/* 115 */     CodingErrorAction unmappableInputAction = (config.getUnmappableInputAction() != null) ? config.getUnmappableInputAction() : CodingErrorAction.REPORT;
/*     */     
/* 117 */     if (charset != null) {
/* 118 */       chardecoder = charset.newDecoder();
/* 119 */       chardecoder.onMalformedInput(malformedInputAction);
/* 120 */       chardecoder.onUnmappableCharacter(unmappableInputAction);
/* 121 */       charencoder = charset.newEncoder();
/* 122 */       charencoder.onMalformedInput(malformedInputAction);
/* 123 */       charencoder.onUnmappableCharacter(unmappableInputAction);
/*     */     } 
/* 125 */     ManagedNHttpClientConnection conn = new ManagedNHttpClientConnectionImpl(id, this.log, this.headerlog, this.wirelog, iosession, config.getBufferSize(), config.getFragmentSizeHint(), this.allocator, chardecoder, charencoder, config.getMessageConstraints(), null, null, null, this.responseParserFactory);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 137 */     iosession.setAttribute("http.connection", conn);
/* 138 */     return conn;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/conn/DefaultClientAsyncConnectionFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
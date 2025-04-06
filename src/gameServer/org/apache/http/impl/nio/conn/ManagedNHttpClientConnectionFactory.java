/*     */ package org.apache.http.impl.nio.conn;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.config.ConnectionConfig;
/*     */ import org.apache.http.impl.nio.codecs.DefaultHttpRequestWriterFactory;
/*     */ import org.apache.http.impl.nio.codecs.DefaultHttpResponseParserFactory;
/*     */ import org.apache.http.nio.NHttpConnection;
/*     */ import org.apache.http.nio.NHttpMessageParserFactory;
/*     */ import org.apache.http.nio.NHttpMessageWriterFactory;
/*     */ import org.apache.http.nio.conn.ManagedNHttpClientConnection;
/*     */ import org.apache.http.nio.conn.NHttpConnectionFactory;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.util.ByteBufferAllocator;
/*     */ import org.apache.http.nio.util.HeapByteBufferAllocator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ManagedNHttpClientConnectionFactory
/*     */   implements NHttpConnectionFactory<ManagedNHttpClientConnection>
/*     */ {
/*  58 */   private final Log headerlog = LogFactory.getLog("org.apache.http.headers");
/*  59 */   private final Log wirelog = LogFactory.getLog("org.apache.http.wire");
/*  60 */   private final Log log = LogFactory.getLog(ManagedNHttpClientConnectionImpl.class);
/*     */   
/*  62 */   private static final AtomicLong COUNTER = new AtomicLong();
/*     */   
/*  64 */   public static final ManagedNHttpClientConnectionFactory INSTANCE = new ManagedNHttpClientConnectionFactory();
/*     */ 
/*     */   
/*     */   private final ByteBufferAllocator allocator;
/*     */   
/*     */   private final NHttpMessageWriterFactory<HttpRequest> requestWriterFactory;
/*     */   
/*     */   private final NHttpMessageParserFactory<HttpResponse> responseParserFactory;
/*     */ 
/*     */   
/*     */   public ManagedNHttpClientConnectionFactory(NHttpMessageWriterFactory<HttpRequest> requestWriterFactory, NHttpMessageParserFactory<HttpResponse> responseParserFactory, ByteBufferAllocator allocator) {
/*  75 */     this.requestWriterFactory = (requestWriterFactory != null) ? requestWriterFactory : (NHttpMessageWriterFactory<HttpRequest>)DefaultHttpRequestWriterFactory.INSTANCE;
/*     */     
/*  77 */     this.responseParserFactory = (responseParserFactory != null) ? responseParserFactory : (NHttpMessageParserFactory<HttpResponse>)DefaultHttpResponseParserFactory.INSTANCE;
/*     */     
/*  79 */     this.allocator = (allocator != null) ? allocator : (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE;
/*     */   }
/*     */   
/*     */   public ManagedNHttpClientConnectionFactory() {
/*  83 */     this(null, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ManagedNHttpClientConnection create(IOSession iosession, ConnectionConfig config) {
/*  88 */     String id = "http-outgoing-" + Long.toString(COUNTER.getAndIncrement());
/*  89 */     CharsetDecoder chardecoder = null;
/*  90 */     CharsetEncoder charencoder = null;
/*  91 */     Charset charset = config.getCharset();
/*  92 */     CodingErrorAction malformedInputAction = (config.getMalformedInputAction() != null) ? config.getMalformedInputAction() : CodingErrorAction.REPORT;
/*     */     
/*  94 */     CodingErrorAction unmappableInputAction = (config.getUnmappableInputAction() != null) ? config.getUnmappableInputAction() : CodingErrorAction.REPORT;
/*     */     
/*  96 */     if (charset != null) {
/*  97 */       chardecoder = charset.newDecoder();
/*  98 */       chardecoder.onMalformedInput(malformedInputAction);
/*  99 */       chardecoder.onUnmappableCharacter(unmappableInputAction);
/* 100 */       charencoder = charset.newEncoder();
/* 101 */       charencoder.onMalformedInput(malformedInputAction);
/* 102 */       charencoder.onUnmappableCharacter(unmappableInputAction);
/*     */     } 
/* 104 */     ManagedNHttpClientConnection conn = new ManagedNHttpClientConnectionImpl(id, this.log, this.headerlog, this.wirelog, iosession, config.getBufferSize(), config.getFragmentSizeHint(), this.allocator, chardecoder, charencoder, config.getMessageConstraints(), null, null, this.requestWriterFactory, this.responseParserFactory);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     iosession.setAttribute("http.connection", conn);
/* 121 */     return conn;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/conn/ManagedNHttpClientConnectionFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
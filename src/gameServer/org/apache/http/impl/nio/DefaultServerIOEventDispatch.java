/*     */ package org.apache.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.HttpRequestFactory;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.impl.DefaultHttpRequestFactory;
/*     */ import org.apache.http.impl.nio.reactor.AbstractIODispatch;
/*     */ import org.apache.http.nio.NHttpServerConnection;
/*     */ import org.apache.http.nio.NHttpServerIOTarget;
/*     */ import org.apache.http.nio.NHttpServiceHandler;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.util.ByteBufferAllocator;
/*     */ import org.apache.http.nio.util.HeapByteBufferAllocator;
/*     */ import org.apache.http.params.HttpConnectionParams;
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
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public class DefaultServerIOEventDispatch
/*     */   extends AbstractIODispatch<NHttpServerIOTarget>
/*     */ {
/*     */   protected final ByteBufferAllocator allocator;
/*     */   protected final NHttpServiceHandler handler;
/*     */   protected final HttpParams params;
/*     */   
/*     */   public DefaultServerIOEventDispatch(NHttpServiceHandler handler, HttpParams params) {
/*  72 */     Args.notNull(handler, "HTTP service handler");
/*  73 */     Args.notNull(params, "HTTP parameters");
/*  74 */     this.allocator = createByteBufferAllocator();
/*  75 */     this.handler = handler;
/*  76 */     this.params = params;
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
/*  89 */     return (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE;
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
/* 103 */     return (HttpRequestFactory)DefaultHttpRequestFactory.INSTANCE;
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
/*     */   protected NHttpServerIOTarget createConnection(IOSession session) {
/* 119 */     return new DefaultNHttpServerConnection(session, createHttpRequestFactory(), this.allocator, this.params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onConnected(NHttpServerIOTarget conn) {
/* 128 */     int timeout = HttpConnectionParams.getSoTimeout(this.params);
/* 129 */     conn.setSocketTimeout(timeout);
/* 130 */     this.handler.connected((NHttpServerConnection)conn);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onClosed(NHttpServerIOTarget conn) {
/* 135 */     this.handler.closed((NHttpServerConnection)conn);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onException(NHttpServerIOTarget conn, IOException ex) {
/* 140 */     this.handler.exception((NHttpServerConnection)conn, ex);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onInputReady(NHttpServerIOTarget conn) {
/* 145 */     conn.consumeInput(this.handler);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onOutputReady(NHttpServerIOTarget conn) {
/* 150 */     conn.produceOutput(this.handler);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onTimeout(NHttpServerIOTarget conn) {
/* 155 */     this.handler.timeout((NHttpServerConnection)conn);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/DefaultServerIOEventDispatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
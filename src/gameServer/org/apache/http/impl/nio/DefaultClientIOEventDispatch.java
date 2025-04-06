/*     */ package org.apache.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.impl.DefaultHttpResponseFactory;
/*     */ import org.apache.http.impl.nio.reactor.AbstractIODispatch;
/*     */ import org.apache.http.nio.NHttpClientConnection;
/*     */ import org.apache.http.nio.NHttpClientHandler;
/*     */ import org.apache.http.nio.NHttpClientIOTarget;
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
/*     */ public class DefaultClientIOEventDispatch
/*     */   extends AbstractIODispatch<NHttpClientIOTarget>
/*     */ {
/*     */   protected final NHttpClientHandler handler;
/*     */   protected final ByteBufferAllocator allocator;
/*     */   protected final HttpParams params;
/*     */   
/*     */   public DefaultClientIOEventDispatch(NHttpClientHandler handler, HttpParams params) {
/*  72 */     Args.notNull(handler, "HTTP client handler");
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
/*     */   protected HttpResponseFactory createHttpResponseFactory() {
/* 103 */     return (HttpResponseFactory)DefaultHttpResponseFactory.INSTANCE;
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
/*     */   protected NHttpClientIOTarget createConnection(IOSession session) {
/* 119 */     return new DefaultNHttpClientConnection(session, createHttpResponseFactory(), this.allocator, this.params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onConnected(NHttpClientIOTarget conn) {
/* 128 */     int timeout = HttpConnectionParams.getSoTimeout(this.params);
/* 129 */     conn.setSocketTimeout(timeout);
/*     */     
/* 131 */     Object attachment = conn.getContext().getAttribute("http.session.attachment");
/* 132 */     this.handler.connected((NHttpClientConnection)conn, attachment);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onClosed(NHttpClientIOTarget conn) {
/* 137 */     this.handler.closed((NHttpClientConnection)conn);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onException(NHttpClientIOTarget conn, IOException ex) {
/* 142 */     this.handler.exception((NHttpClientConnection)conn, ex);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onInputReady(NHttpClientIOTarget conn) {
/* 147 */     conn.consumeInput(this.handler);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onOutputReady(NHttpClientIOTarget conn) {
/* 152 */     conn.produceOutput(this.handler);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onTimeout(NHttpClientIOTarget conn) {
/* 157 */     this.handler.timeout((NHttpClientConnection)conn);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/DefaultClientIOEventDispatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
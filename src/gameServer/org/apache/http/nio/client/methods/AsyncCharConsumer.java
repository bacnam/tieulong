/*     */ package org.apache.http.nio.client.methods;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.entity.ContentType;
/*     */ import org.apache.http.nio.ContentDecoder;
/*     */ import org.apache.http.nio.IOControl;
/*     */ import org.apache.http.nio.conn.ManagedNHttpClientConnection;
/*     */ import org.apache.http.nio.protocol.AbstractAsyncResponseConsumer;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.protocol.HTTP;
/*     */ import org.apache.http.util.Asserts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AsyncCharConsumer<T>
/*     */   extends AbstractAsyncResponseConsumer<T>
/*     */ {
/*     */   private final ByteBuffer bbuf;
/*     */   private final CharBuffer cbuf;
/*     */   private CharsetDecoder chardecoder;
/*     */   private ContentType contentType;
/*     */   
/*     */   public AsyncCharConsumer(int bufSize) {
/*  64 */     this.bbuf = ByteBuffer.allocate(bufSize);
/*  65 */     this.cbuf = CharBuffer.allocate(bufSize);
/*     */   }
/*     */   
/*     */   public AsyncCharConsumer() {
/*  69 */     this(8192);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void onCharReceived(CharBuffer paramCharBuffer, IOControl paramIOControl) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void onEntityEnclosed(HttpEntity entity, ContentType contentType) throws IOException {
/*  87 */     this.contentType = (contentType != null) ? contentType : ContentType.DEFAULT_TEXT;
/*  88 */     Charset charset = this.contentType.getCharset();
/*  89 */     if (charset == null) {
/*  90 */       charset = HTTP.DEF_CONTENT_CHARSET;
/*     */     }
/*  92 */     this.chardecoder = charset.newDecoder();
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void onContentReceived(ContentDecoder decoder, IOControl ioctrl) throws IOException {
/*     */     IOSession iosession;
/*  98 */     Asserts.notNull(this.bbuf, "Byte buffer");
/*     */ 
/*     */ 
/*     */     
/* 102 */     if (ioctrl instanceof ManagedNHttpClientConnection) {
/* 103 */       ManagedNHttpClientConnection conn = (ManagedNHttpClientConnection)ioctrl;
/* 104 */       iosession = (conn != null) ? conn.getIOSession() : null;
/*     */     } else {
/* 106 */       iosession = null;
/*     */     } 
/* 108 */     while (!isDone()) {
/* 109 */       int bytesRead = decoder.read(this.bbuf);
/* 110 */       if (bytesRead <= 0) {
/*     */         break;
/*     */       }
/* 113 */       this.bbuf.flip();
/* 114 */       boolean completed = decoder.isCompleted();
/* 115 */       CoderResult result = this.chardecoder.decode(this.bbuf, this.cbuf, completed);
/* 116 */       handleDecodingResult(result, ioctrl);
/* 117 */       this.bbuf.compact();
/* 118 */       if (completed) {
/* 119 */         result = this.chardecoder.flush(this.cbuf);
/* 120 */         handleDecodingResult(result, ioctrl);
/*     */         break;
/*     */       } 
/* 123 */       if (iosession != null && (iosession.isClosed() || (iosession.getEventMask() & 0x1) == 0)) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleDecodingResult(CoderResult result, IOControl ioctrl) throws IOException {
/* 133 */     if (result.isError()) {
/* 134 */       result.throwException();
/*     */     }
/* 136 */     this.cbuf.flip();
/* 137 */     if (this.cbuf.hasRemaining()) {
/* 138 */       onCharReceived(this.cbuf, ioctrl);
/*     */     }
/* 140 */     this.cbuf.clear();
/*     */   }
/*     */   
/*     */   protected void releaseResources() {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/client/methods/AsyncCharConsumer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
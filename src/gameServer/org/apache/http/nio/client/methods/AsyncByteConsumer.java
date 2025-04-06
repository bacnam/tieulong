/*     */ package org.apache.http.nio.client.methods;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.entity.ContentType;
/*     */ import org.apache.http.nio.ContentDecoder;
/*     */ import org.apache.http.nio.IOControl;
/*     */ import org.apache.http.nio.conn.ManagedNHttpClientConnection;
/*     */ import org.apache.http.nio.protocol.AbstractAsyncResponseConsumer;
/*     */ import org.apache.http.nio.reactor.IOSession;
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
/*     */ public abstract class AsyncByteConsumer<T>
/*     */   extends AbstractAsyncResponseConsumer<T>
/*     */ {
/*     */   private final ByteBuffer bbuf;
/*     */   
/*     */   public AsyncByteConsumer(int bufSize) {
/*  55 */     this.bbuf = ByteBuffer.allocate(bufSize);
/*     */   }
/*     */   
/*     */   public AsyncByteConsumer() {
/*  59 */     this(8192);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void onByteReceived(ByteBuffer paramByteBuffer, IOControl paramIOControl) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void onEntityEnclosed(HttpEntity entity, ContentType contentType) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void onContentReceived(ContentDecoder decoder, IOControl ioctrl) throws IOException {
/*     */     IOSession iosession;
/*  82 */     Asserts.notNull(this.bbuf, "Byte buffer");
/*     */ 
/*     */ 
/*     */     
/*  86 */     if (ioctrl instanceof ManagedNHttpClientConnection) {
/*  87 */       ManagedNHttpClientConnection conn = (ManagedNHttpClientConnection)ioctrl;
/*  88 */       iosession = (conn != null) ? conn.getIOSession() : null;
/*     */     } else {
/*  90 */       iosession = null;
/*     */     } 
/*  92 */     while (!isDone()) {
/*  93 */       int bytesRead = decoder.read(this.bbuf);
/*  94 */       if (bytesRead <= 0) {
/*     */         break;
/*     */       }
/*  97 */       this.bbuf.flip();
/*  98 */       onByteReceived(this.bbuf, ioctrl);
/*  99 */       this.bbuf.clear();
/* 100 */       if (decoder.isCompleted()) {
/*     */         break;
/*     */       }
/* 103 */       if (iosession != null && (iosession.isClosed() || (iosession.getEventMask() & 0x1) == 0))
/*     */         break; 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void releaseResources() {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/client/methods/AsyncByteConsumer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
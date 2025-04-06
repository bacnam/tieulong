/*     */ package org.apache.http.nio.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.entity.HttpEntityWrapper;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.IOControl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @NotThreadSafe
/*     */ public class NHttpEntityWrapper
/*     */   extends HttpEntityWrapper
/*     */   implements ProducingNHttpEntity
/*     */ {
/*     */   private final ReadableByteChannel channel;
/*     */   private final ByteBuffer buffer;
/*     */   
/*     */   public NHttpEntityWrapper(HttpEntity httpEntity) throws IOException {
/*  60 */     super(httpEntity);
/*  61 */     this.channel = Channels.newChannel(httpEntity.getContent());
/*  62 */     this.buffer = ByteBuffer.allocate(4096);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException, UnsupportedOperationException {
/*  70 */     throw new UnsupportedOperationException("Does not support blocking methods");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/*  75 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream out) throws IOException, UnsupportedOperationException {
/*  83 */     throw new UnsupportedOperationException("Does not support blocking methods");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
/*  89 */     int i = this.channel.read(this.buffer);
/*  90 */     this.buffer.flip();
/*  91 */     encoder.write(this.buffer);
/*  92 */     boolean buffering = this.buffer.hasRemaining();
/*  93 */     this.buffer.compact();
/*  94 */     if (i == -1 && !buffering) {
/*  95 */       encoder.complete();
/*  96 */       this.channel.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void finish() {
/*     */     try {
/* 102 */       this.channel.close();
/* 103 */     } catch (IOException ignore) {}
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/entity/NHttpEntityWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
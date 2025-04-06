/*     */ package org.apache.http.nio.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.IOControl;
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
/*     */ @NotThreadSafe
/*     */ public class EntityAsyncContentProducer
/*     */   implements HttpAsyncContentProducer
/*     */ {
/*     */   private final HttpEntity entity;
/*     */   private final ByteBuffer buffer;
/*     */   private ReadableByteChannel channel;
/*     */   
/*     */   public EntityAsyncContentProducer(HttpEntity entity) {
/*  58 */     Args.notNull(entity, "HTTP entity");
/*  59 */     this.entity = entity;
/*  60 */     this.buffer = ByteBuffer.allocate(4096);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
/*  66 */     if (this.channel == null) {
/*  67 */       this.channel = Channels.newChannel(this.entity.getContent());
/*     */     }
/*  69 */     int i = this.channel.read(this.buffer);
/*  70 */     this.buffer.flip();
/*  71 */     encoder.write(this.buffer);
/*  72 */     boolean buffering = this.buffer.hasRemaining();
/*  73 */     this.buffer.compact();
/*  74 */     if (i == -1 && !buffering) {
/*  75 */       encoder.complete();
/*  76 */       close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  82 */     return this.entity.isRepeatable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  87 */     ReadableByteChannel local = this.channel;
/*  88 */     this.channel = null;
/*  89 */     if (local != null) {
/*  90 */       local.close();
/*     */     }
/*  92 */     if (this.entity.isStreaming()) {
/*  93 */       InputStream instream = this.entity.getContent();
/*  94 */       instream.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 100 */     return this.entity.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/entity/EntityAsyncContentProducer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
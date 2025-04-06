/*     */ package org.apache.http.nio.entity;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.entity.AbstractHttpEntity;
/*     */ import org.apache.http.entity.ContentType;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class NByteArrayEntity
/*     */   extends AbstractHttpEntity
/*     */   implements HttpAsyncContentProducer, ProducingNHttpEntity
/*     */ {
/*     */   private final byte[] b;
/*     */   private final int off;
/*     */   private final int len;
/*     */   private final ByteBuffer buf;
/*     */   @Deprecated
/*     */   protected final byte[] content;
/*     */   @Deprecated
/*     */   protected final ByteBuffer buffer;
/*     */   
/*     */   public NByteArrayEntity(byte[] b, ContentType contentType) {
/*  73 */     Args.notNull(b, "Source byte array");
/*  74 */     this.b = b;
/*  75 */     this.off = 0;
/*  76 */     this.len = b.length;
/*  77 */     this.buf = ByteBuffer.wrap(b);
/*  78 */     this.content = b;
/*  79 */     this.buffer = this.buf;
/*  80 */     if (contentType != null) {
/*  81 */       setContentType(contentType.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NByteArrayEntity(byte[] b, int off, int len, ContentType contentType) {
/*  90 */     Args.notNull(b, "Source byte array");
/*  91 */     if (off < 0 || off > b.length || len < 0 || off + len < 0 || off + len > b.length)
/*     */     {
/*  93 */       throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
/*     */     }
/*  95 */     this.b = b;
/*  96 */     this.off = off;
/*  97 */     this.len = len;
/*  98 */     this.buf = ByteBuffer.wrap(b, off, len);
/*  99 */     this.content = b;
/* 100 */     this.buffer = this.buf;
/* 101 */     if (contentType != null) {
/* 102 */       setContentType(contentType.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public NByteArrayEntity(byte[] b) {
/* 107 */     this(b, (ContentType)null);
/*     */   }
/*     */   
/*     */   public NByteArrayEntity(byte[] b, int off, int len) {
/* 111 */     this(b, off, len, (ContentType)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 121 */     this.buf.rewind();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void finish() {
/* 131 */     close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
/* 137 */     encoder.write(this.buf);
/* 138 */     if (!this.buf.hasRemaining()) {
/* 139 */       encoder.complete();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/* 145 */     return this.len;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/* 150 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/* 155 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() {
/* 160 */     return new ByteArrayInputStream(this.b, this.off, this.len);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/* 165 */     Args.notNull(outstream, "Output stream");
/* 166 */     outstream.write(this.b, this.off, this.len);
/* 167 */     outstream.flush();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/entity/NByteArrayEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
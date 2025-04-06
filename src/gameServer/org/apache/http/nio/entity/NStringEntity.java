/*     */ package org.apache.http.nio.entity;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.entity.AbstractHttpEntity;
/*     */ import org.apache.http.entity.ContentType;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.IOControl;
/*     */ import org.apache.http.protocol.HTTP;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class NStringEntity
/*     */   extends AbstractHttpEntity
/*     */   implements HttpAsyncContentProducer, ProducingNHttpEntity
/*     */ {
/*     */   private final byte[] b;
/*     */   private final ByteBuffer buf;
/*     */   @Deprecated
/*     */   protected final byte[] content;
/*     */   @Deprecated
/*     */   protected final ByteBuffer buffer;
/*     */   
/*     */   public NStringEntity(String s, ContentType contentType) {
/*  83 */     Args.notNull(s, "Source string");
/*  84 */     Charset charset = (contentType != null) ? contentType.getCharset() : null;
/*  85 */     if (charset == null) {
/*  86 */       charset = HTTP.DEF_CONTENT_CHARSET;
/*     */     }
/*  88 */     this.b = s.getBytes(charset);
/*  89 */     this.buf = ByteBuffer.wrap(this.b);
/*  90 */     this.content = this.b;
/*  91 */     this.buffer = this.buf;
/*  92 */     if (contentType != null) {
/*  93 */       setContentType(contentType.toString());
/*     */     }
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
/*     */   
/*     */   public NStringEntity(String s, String charset) throws UnsupportedEncodingException {
/* 111 */     this(s, ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), charset));
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
/*     */   public NStringEntity(String s, Charset charset) {
/* 127 */     this(s, ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), charset));
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
/*     */   public NStringEntity(String s) throws UnsupportedEncodingException {
/* 140 */     this(s, ContentType.DEFAULT_TEXT);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/* 145 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/* 150 */     return this.b.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 160 */     this.buf.rewind();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void finish() {
/* 170 */     close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
/* 176 */     encoder.write(this.buf);
/* 177 */     if (!this.buf.hasRemaining()) {
/* 178 */       encoder.complete();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/* 184 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() {
/* 189 */     return new ByteArrayInputStream(this.b);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/* 194 */     Args.notNull(outstream, "Output stream");
/* 195 */     outstream.write(this.b);
/* 196 */     outstream.flush();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/entity/NStringEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
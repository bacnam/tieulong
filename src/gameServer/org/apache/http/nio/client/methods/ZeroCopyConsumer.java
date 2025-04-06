/*     */ package org.apache.http.nio.client.methods;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.entity.ContentType;
/*     */ import org.apache.http.entity.FileEntity;
/*     */ import org.apache.http.nio.ContentDecoder;
/*     */ import org.apache.http.nio.ContentDecoderChannel;
/*     */ import org.apache.http.nio.FileContentDecoder;
/*     */ import org.apache.http.nio.IOControl;
/*     */ import org.apache.http.nio.protocol.AbstractAsyncResponseConsumer;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ public abstract class ZeroCopyConsumer<T>
/*     */   extends AbstractAsyncResponseConsumer<T>
/*     */ {
/*     */   private final File file;
/*     */   private final RandomAccessFile accessfile;
/*     */   private HttpResponse response;
/*     */   private ContentType contentType;
/*     */   private FileChannel fileChannel;
/*  64 */   private long idx = -1L;
/*     */ 
/*     */   
/*     */   public ZeroCopyConsumer(File file) throws FileNotFoundException {
/*  68 */     if (file == null) {
/*  69 */       throw new IllegalArgumentException("File may nor be null");
/*     */     }
/*  71 */     this.file = file;
/*  72 */     this.accessfile = new RandomAccessFile(this.file, "rw");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onResponseReceived(HttpResponse response) {
/*  77 */     this.response = response;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onEntityEnclosed(HttpEntity entity, ContentType contentType) throws IOException {
/*  83 */     this.contentType = contentType;
/*  84 */     this.fileChannel = this.accessfile.getChannel();
/*  85 */     this.idx = 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onContentReceived(ContentDecoder decoder, IOControl ioctrl) throws IOException {
/*     */     long transferred;
/*  91 */     Asserts.notNull(this.fileChannel, "File channel");
/*     */     
/*  93 */     if (decoder instanceof FileContentDecoder) {
/*  94 */       transferred = ((FileContentDecoder)decoder).transfer(this.fileChannel, this.idx, 2147483647L);
/*     */     } else {
/*     */       
/*  97 */       transferred = this.fileChannel.transferFrom((ReadableByteChannel)new ContentDecoderChannel(decoder), this.idx, 2147483647L);
/*     */     } 
/*     */     
/* 100 */     if (transferred > 0L) {
/* 101 */       this.idx += transferred;
/*     */     }
/* 103 */     if (decoder.isCompleted()) {
/* 104 */       this.fileChannel.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract T process(HttpResponse paramHttpResponse, File paramFile, ContentType paramContentType) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected T buildResult(HttpContext context) throws Exception {
/* 121 */     this.response.setEntity((HttpEntity)new FileEntity(this.file, this.contentType));
/* 122 */     return process(this.response, this.file, this.contentType);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void releaseResources() {
/*     */     try {
/* 128 */       this.accessfile.close();
/* 129 */     } catch (IOException ignore) {}
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/client/methods/ZeroCopyConsumer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
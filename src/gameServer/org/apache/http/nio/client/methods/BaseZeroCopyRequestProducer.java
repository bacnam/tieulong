/*     */ package org.apache.http.nio.client.methods;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.net.URI;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.client.utils.URIUtils;
/*     */ import org.apache.http.entity.BasicHttpEntity;
/*     */ import org.apache.http.entity.ContentType;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.ContentEncoderChannel;
/*     */ import org.apache.http.nio.FileContentEncoder;
/*     */ import org.apache.http.nio.IOControl;
/*     */ import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ abstract class BaseZeroCopyRequestProducer
/*     */   implements HttpAsyncRequestProducer
/*     */ {
/*     */   private final URI requestURI;
/*     */   private final File file;
/*     */   private final RandomAccessFile accessfile;
/*     */   private final ContentType contentType;
/*     */   private FileChannel fileChannel;
/*  60 */   private long idx = -1L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BaseZeroCopyRequestProducer(URI requestURI, File file, ContentType contentType) throws FileNotFoundException {
/*  66 */     Args.notNull(requestURI, "Request URI");
/*  67 */     Args.notNull(file, "Source file");
/*  68 */     this.requestURI = requestURI;
/*  69 */     this.file = file;
/*  70 */     this.accessfile = new RandomAccessFile(file, "r");
/*  71 */     this.contentType = contentType;
/*     */   }
/*     */   
/*     */   private void closeChannel() throws IOException {
/*  75 */     if (this.fileChannel != null) {
/*  76 */       this.fileChannel.close();
/*  77 */       this.fileChannel = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract HttpEntityEnclosingRequest createRequest(URI paramURI, HttpEntity paramHttpEntity);
/*     */   
/*     */   public HttpRequest generateRequest() throws IOException, HttpException {
/*  84 */     BasicHttpEntity entity = new BasicHttpEntity();
/*  85 */     entity.setChunked(false);
/*  86 */     entity.setContentLength(this.file.length());
/*  87 */     if (this.contentType != null) {
/*  88 */       entity.setContentType(this.contentType.toString());
/*     */     }
/*  90 */     return (HttpRequest)createRequest(this.requestURI, (HttpEntity)entity);
/*     */   }
/*     */   
/*     */   public synchronized HttpHost getTarget() {
/*  94 */     return URIUtils.extractHost(this.requestURI);
/*     */   }
/*     */   
/*     */   public synchronized void produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
/*     */     long transferred;
/*  99 */     if (this.fileChannel == null) {
/* 100 */       this.fileChannel = this.accessfile.getChannel();
/* 101 */       this.idx = 0L;
/*     */     } 
/*     */     
/* 104 */     if (encoder instanceof FileContentEncoder) {
/* 105 */       transferred = ((FileContentEncoder)encoder).transfer(this.fileChannel, this.idx, 2147483647L);
/*     */     } else {
/*     */       
/* 108 */       transferred = this.fileChannel.transferTo(this.idx, 2147483647L, (WritableByteChannel)new ContentEncoderChannel(encoder));
/*     */     } 
/*     */     
/* 111 */     if (transferred > 0L) {
/* 112 */       this.idx += transferred;
/*     */     }
/*     */     
/* 115 */     if (this.idx >= this.fileChannel.size()) {
/* 116 */       encoder.complete();
/* 117 */       closeChannel();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void requestCompleted(HttpContext context) {}
/*     */ 
/*     */   
/*     */   public void failed(Exception ex) {}
/*     */   
/*     */   public boolean isRepeatable() {
/* 128 */     return true;
/*     */   }
/*     */   
/*     */   public synchronized void resetRequest() throws IOException {
/* 132 */     closeChannel();
/*     */   }
/*     */   
/*     */   public synchronized void close() throws IOException {
/*     */     try {
/* 137 */       this.accessfile.close();
/* 138 */     } catch (IOException ignore) {}
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/client/methods/BaseZeroCopyRequestProducer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
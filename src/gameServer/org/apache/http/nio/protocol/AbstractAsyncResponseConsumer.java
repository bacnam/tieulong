/*     */ package org.apache.http.nio.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.entity.ContentType;
/*     */ import org.apache.http.nio.ContentDecoder;
/*     */ import org.apache.http.nio.IOControl;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractAsyncResponseConsumer<T>
/*     */   implements HttpAsyncResponseConsumer<T>
/*     */ {
/*  56 */   private final AtomicBoolean completed = new AtomicBoolean(false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile T result;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile Exception ex;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void onResponseReceived(HttpResponse paramHttpResponse) throws HttpException, IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void onContentReceived(ContentDecoder paramContentDecoder, IOControl paramIOControl) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void onEntityEnclosed(HttpEntity paramHttpEntity, ContentType paramContentType) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract T buildResult(HttpContext paramHttpContext) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void releaseResources();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onClose() throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void responseReceived(HttpResponse response) throws IOException, HttpException {
/* 127 */     onResponseReceived(response);
/* 128 */     HttpEntity entity = response.getEntity();
/* 129 */     if (entity != null) {
/* 130 */       ContentType contentType = ContentType.getOrDefault(entity);
/* 131 */       onEntityEnclosed(entity, contentType);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void consumeContent(ContentDecoder decoder, IOControl ioctrl) throws IOException {
/* 141 */     onContentReceived(decoder, ioctrl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void responseCompleted(HttpContext context) {
/* 149 */     if (this.completed.compareAndSet(false, true)) {
/*     */       try {
/* 151 */         this.result = buildResult(context);
/* 152 */       } catch (Exception ex) {
/* 153 */         this.ex = ex;
/*     */       } finally {
/* 155 */         releaseResources();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean cancel() {
/* 162 */     if (this.completed.compareAndSet(false, true)) {
/* 163 */       releaseResources();
/* 164 */       return true;
/*     */     } 
/* 166 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void failed(Exception ex) {
/* 171 */     if (this.completed.compareAndSet(false, true)) {
/* 172 */       this.ex = ex;
/* 173 */       releaseResources();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void close() throws IOException {
/* 179 */     if (this.completed.compareAndSet(false, true)) {
/* 180 */       releaseResources();
/* 181 */       onClose();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Exception getException() {
/* 187 */     return this.ex;
/*     */   }
/*     */ 
/*     */   
/*     */   public T getResult() {
/* 192 */     return this.result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/* 197 */     return this.completed.get();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/protocol/AbstractAsyncResponseConsumer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
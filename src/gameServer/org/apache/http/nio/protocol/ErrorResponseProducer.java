/*    */ package org.apache.http.nio.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.nio.ContentEncoder;
/*    */ import org.apache.http.nio.IOControl;
/*    */ import org.apache.http.nio.entity.EntityAsyncContentProducer;
/*    */ import org.apache.http.nio.entity.HttpAsyncContentProducer;
/*    */ import org.apache.http.protocol.HttpContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ErrorResponseProducer
/*    */   implements HttpAsyncResponseProducer
/*    */ {
/*    */   private final HttpResponse response;
/*    */   private final HttpEntity entity;
/*    */   private final HttpAsyncContentProducer contentProducer;
/*    */   private final boolean keepAlive;
/*    */   
/*    */   ErrorResponseProducer(HttpResponse response, HttpEntity entity, boolean keepAlive) {
/* 53 */     this.response = response;
/* 54 */     this.entity = entity;
/* 55 */     if (entity instanceof HttpAsyncContentProducer) {
/* 56 */       this.contentProducer = (HttpAsyncContentProducer)entity;
/*    */     } else {
/* 58 */       this.contentProducer = (HttpAsyncContentProducer)new EntityAsyncContentProducer(entity);
/*    */     } 
/* 60 */     this.keepAlive = keepAlive;
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpResponse generateResponse() {
/* 65 */     if (this.keepAlive) {
/* 66 */       this.response.addHeader("Connection", "Keep-Alive");
/*    */     } else {
/* 68 */       this.response.addHeader("Connection", "Close");
/*    */     } 
/* 70 */     this.response.setEntity(this.entity);
/* 71 */     return this.response;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
/* 77 */     this.contentProducer.produceContent(encoder, ioctrl);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void responseCompleted(HttpContext context) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void failed(Exception ex) {}
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 90 */     this.contentProducer.close();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/protocol/ErrorResponseProducer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
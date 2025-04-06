/*    */ package org.apache.http.nio.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.ContentTooLongException;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.entity.ContentType;
/*    */ import org.apache.http.nio.ContentDecoder;
/*    */ import org.apache.http.nio.IOControl;
/*    */ import org.apache.http.nio.entity.ContentBufferEntity;
/*    */ import org.apache.http.nio.util.ByteBufferAllocator;
/*    */ import org.apache.http.nio.util.ContentInputBuffer;
/*    */ import org.apache.http.nio.util.HeapByteBufferAllocator;
/*    */ import org.apache.http.nio.util.SimpleInputBuffer;
/*    */ import org.apache.http.protocol.HttpContext;
/*    */ import org.apache.http.util.Asserts;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BasicAsyncResponseConsumer
/*    */   extends AbstractAsyncResponseConsumer<HttpResponse>
/*    */ {
/*    */   private volatile HttpResponse response;
/*    */   private volatile SimpleInputBuffer buf;
/*    */   
/*    */   protected void onResponseReceived(HttpResponse response) throws IOException {
/* 61 */     this.response = response;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onEntityEnclosed(HttpEntity entity, ContentType contentType) throws IOException {
/* 67 */     long len = entity.getContentLength();
/* 68 */     if (len > 2147483647L) {
/* 69 */       throw new ContentTooLongException("Entity content is too long: " + len);
/*    */     }
/* 71 */     if (len < 0L) {
/* 72 */       len = 4096L;
/*    */     }
/* 74 */     this.buf = new SimpleInputBuffer((int)len, (ByteBufferAllocator)new HeapByteBufferAllocator());
/* 75 */     this.response.setEntity((HttpEntity)new ContentBufferEntity(entity, (ContentInputBuffer)this.buf));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onContentReceived(ContentDecoder decoder, IOControl ioctrl) throws IOException {
/* 81 */     Asserts.notNull(this.buf, "Content buffer");
/* 82 */     this.buf.consumeContent(decoder);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void releaseResources() {
/* 87 */     this.response = null;
/* 88 */     this.buf = null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected HttpResponse buildResult(HttpContext context) {
/* 93 */     return this.response;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/protocol/BasicAsyncResponseConsumer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
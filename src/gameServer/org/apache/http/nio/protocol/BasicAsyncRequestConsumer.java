/*    */ package org.apache.http.nio.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.ContentTooLongException;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.HttpEntityEnclosingRequest;
/*    */ import org.apache.http.HttpRequest;
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
/*    */ public class BasicAsyncRequestConsumer
/*    */   extends AbstractAsyncRequestConsumer<HttpRequest>
/*    */ {
/*    */   private volatile HttpRequest request;
/*    */   private volatile SimpleInputBuffer buf;
/*    */   
/*    */   protected void onRequestReceived(HttpRequest request) throws IOException {
/* 62 */     this.request = request;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onEntityEnclosed(HttpEntity entity, ContentType contentType) throws IOException {
/* 68 */     long len = entity.getContentLength();
/* 69 */     if (len > 2147483647L) {
/* 70 */       throw new ContentTooLongException("Entity content is too long: " + len);
/*    */     }
/* 72 */     if (len < 0L) {
/* 73 */       len = 4096L;
/*    */     }
/* 75 */     this.buf = new SimpleInputBuffer((int)len, (ByteBufferAllocator)new HeapByteBufferAllocator());
/* 76 */     ((HttpEntityEnclosingRequest)this.request).setEntity((HttpEntity)new ContentBufferEntity(entity, (ContentInputBuffer)this.buf));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void onContentReceived(ContentDecoder decoder, IOControl ioctrl) throws IOException {
/* 83 */     Asserts.notNull(this.buf, "Content buffer");
/* 84 */     this.buf.consumeContent(decoder);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void releaseResources() {
/* 89 */     this.request = null;
/* 90 */     this.buf = null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected HttpRequest buildResult(HttpContext context) {
/* 95 */     return this.request;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/protocol/BasicAsyncRequestConsumer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
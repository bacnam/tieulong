/*    */ package org.apache.http.nio.entity;
/*    */ 
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.entity.BasicHttpEntity;
/*    */ import org.apache.http.nio.util.ContentInputBuffer;
/*    */ import org.apache.http.util.Args;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @NotThreadSafe
/*    */ public class ContentBufferEntity
/*    */   extends BasicHttpEntity
/*    */ {
/*    */   private final HttpEntity wrappedEntity;
/*    */   
/*    */   public ContentBufferEntity(HttpEntity entity, ContentInputBuffer buffer) {
/* 56 */     Args.notNull(entity, "HTTP entity");
/* 57 */     this.wrappedEntity = entity;
/* 58 */     setContent(new ContentInputStream(buffer));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isChunked() {
/* 63 */     return this.wrappedEntity.isChunked();
/*    */   }
/*    */ 
/*    */   
/*    */   public long getContentLength() {
/* 68 */     return this.wrappedEntity.getContentLength();
/*    */   }
/*    */ 
/*    */   
/*    */   public Header getContentType() {
/* 73 */     return this.wrappedEntity.getContentType();
/*    */   }
/*    */ 
/*    */   
/*    */   public Header getContentEncoding() {
/* 78 */     return this.wrappedEntity.getContentEncoding();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/entity/ContentBufferEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
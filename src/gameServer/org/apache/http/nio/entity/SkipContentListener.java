/*    */ package org.apache.http.nio.entity;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import org.apache.http.nio.ContentDecoder;
/*    */ import org.apache.http.nio.IOControl;
/*    */ import org.apache.http.nio.util.ByteBufferAllocator;
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
/*    */ @Deprecated
/*    */ public class SkipContentListener
/*    */   implements ContentListener
/*    */ {
/*    */   private final ByteBuffer buffer;
/*    */   
/*    */   public SkipContentListener(ByteBufferAllocator allocator) {
/* 52 */     Args.notNull(allocator, "ByteBuffer allocator");
/* 53 */     this.buffer = allocator.allocate(2048);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void contentAvailable(ContentDecoder decoder, IOControl ioctrl) throws IOException {
/*    */     int lastRead;
/*    */     do {
/* 61 */       this.buffer.clear();
/* 62 */       lastRead = decoder.read(this.buffer);
/* 63 */     } while (lastRead > 0);
/*    */   }
/*    */   
/*    */   public void finished() {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/entity/SkipContentListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
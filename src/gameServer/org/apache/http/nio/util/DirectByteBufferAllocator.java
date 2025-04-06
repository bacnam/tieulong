/*    */ package org.apache.http.nio.util;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ @Immutable
/*    */ public class DirectByteBufferAllocator
/*    */   implements ByteBufferAllocator
/*    */ {
/* 43 */   public static final DirectByteBufferAllocator INSTANCE = new DirectByteBufferAllocator();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ByteBuffer allocate(int size) {
/* 51 */     return ByteBuffer.allocateDirect(size);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/util/DirectByteBufferAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
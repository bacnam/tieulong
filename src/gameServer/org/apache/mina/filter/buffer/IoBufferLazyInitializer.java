/*    */ package org.apache.mina.filter.buffer;
/*    */ 
/*    */ import org.apache.mina.core.buffer.IoBuffer;
/*    */ import org.apache.mina.util.LazyInitializer;
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
/*    */ public class IoBufferLazyInitializer
/*    */   extends LazyInitializer<IoBuffer>
/*    */ {
/*    */   private int bufferSize;
/*    */   
/*    */   public IoBufferLazyInitializer(int bufferSize) {
/* 45 */     this.bufferSize = bufferSize;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public IoBuffer init() {
/* 52 */     return IoBuffer.allocate(this.bufferSize);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/buffer/IoBufferLazyInitializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
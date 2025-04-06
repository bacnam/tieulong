/*    */ package org.apache.mina.util.byteaccess;
/*    */ 
/*    */ import org.apache.mina.core.buffer.IoBuffer;
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
/*    */ public class SimpleByteArrayFactory
/*    */   implements ByteArrayFactory
/*    */ {
/*    */   public ByteArray create(int size) {
/* 45 */     if (size < 0) {
/* 46 */       throw new IllegalArgumentException("Buffer size must not be negative:" + size);
/*    */     }
/* 48 */     IoBuffer bb = IoBuffer.allocate(size);
/* 49 */     ByteArray ba = new BufferByteArray(bb)
/*    */       {
/*    */         public void free() {}
/*    */       };
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 57 */     return ba;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/byteaccess/SimpleByteArrayFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
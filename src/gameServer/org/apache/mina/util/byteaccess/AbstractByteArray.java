/*    */ package org.apache.mina.util.byteaccess;
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
/*    */ abstract class AbstractByteArray
/*    */   implements ByteArray
/*    */ {
/*    */   public final int length() {
/* 35 */     return last() - first();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean equals(Object other) {
/* 44 */     if (other == this) {
/* 45 */       return true;
/*    */     }
/*    */     
/* 48 */     if (!(other instanceof ByteArray)) {
/* 49 */       return false;
/*    */     }
/* 51 */     ByteArray otherByteArray = (ByteArray)other;
/*    */     
/* 53 */     if (first() != otherByteArray.first() || last() != otherByteArray.last() || !order().equals(otherByteArray.order()))
/*    */     {
/* 55 */       return false;
/*    */     }
/*    */     
/* 58 */     ByteArray.Cursor cursor = cursor();
/* 59 */     ByteArray.Cursor otherCursor = otherByteArray.cursor();
/* 60 */     for (int remaining = cursor.getRemaining(); remaining > 0; ) {
/*    */       
/* 62 */       if (remaining >= 4) {
/* 63 */         int i = cursor.getInt();
/* 64 */         int otherI = otherCursor.getInt();
/* 65 */         if (i != otherI)
/* 66 */           return false; 
/*    */         continue;
/*    */       } 
/* 69 */       byte b = cursor.get();
/* 70 */       byte otherB = otherCursor.get();
/* 71 */       if (b != otherB) {
/* 72 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 76 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/byteaccess/AbstractByteArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
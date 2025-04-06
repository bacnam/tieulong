/*    */ package com.mchange.lang;
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
/*    */ public final class DoubleUtils
/*    */ {
/*    */   public static byte[] byteArrayFromDouble(double paramDouble) {
/* 42 */     long l = Double.doubleToLongBits(paramDouble);
/* 43 */     return LongUtils.byteArrayFromLong(l);
/*    */   }
/*    */ 
/*    */   
/*    */   public static double doubleFromByteArray(byte[] paramArrayOfbyte, int paramInt) {
/* 48 */     long l = LongUtils.longFromByteArray(paramArrayOfbyte, paramInt);
/* 49 */     return Double.longBitsToDouble(l);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/lang/DoubleUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
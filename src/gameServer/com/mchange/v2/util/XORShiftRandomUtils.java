/*    */ package com.mchange.v2.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class XORShiftRandomUtils
/*    */ {
/*    */   public static long nextLong(long paramLong) {
/* 52 */     paramLong ^= paramLong << 21L;
/* 53 */     paramLong ^= paramLong >>> 35L;
/* 54 */     paramLong ^= paramLong << 4L;
/* 55 */     return paramLong;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void main(String[] paramArrayOfString) {
/* 60 */     long l = System.currentTimeMillis();
/* 61 */     byte b1 = 100;
/* 62 */     int[] arrayOfInt = new int[b1];
/*    */     byte b2;
/* 64 */     for (b2 = 0; b2 < 1000000; b2++) {
/*    */       
/* 66 */       l = nextLong(l);
/* 67 */       arrayOfInt[(int)(Math.abs(l) % b1)] = arrayOfInt[(int)(Math.abs(l) % b1)] + 1;
/* 68 */       if (b2 % 10000 == 0)
/* 69 */         System.out.println(l); 
/*    */     } 
/* 71 */     for (b2 = 0; b2 < b1; b2++) {
/*    */       
/* 73 */       if (b2 != 0) System.out.print(", "); 
/* 74 */       System.out.print(b2 + " -> " + arrayOfInt[b2]);
/*    */     } 
/* 76 */     System.out.println();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/util/XORShiftRandomUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
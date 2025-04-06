/*    */ package com.mchange.v2.coalesce;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CoalesceTest
/*    */ {
/*    */   static final int NUM_ITERS = 10000;
/* 42 */   static final Coalescer c = CoalescerFactory.createCoalescer(null, true, true);
/*    */ 
/*    */   
/*    */   public static void main(String[] paramArrayOfString) {
/* 46 */     doTest();
/* 47 */     System.gc();
/* 48 */     System.err.println("num coalesced after gc: " + c.countCoalesced());
/*    */   }
/*    */ 
/*    */   
/*    */   private static void doTest() {
/* 53 */     String[] arrayOfString = new String[10000];
/* 54 */     for (byte b1 = 0; b1 < '✐'; b1++) {
/* 55 */       arrayOfString[b1] = new String("Hello");
/*    */     }
/* 57 */     long l1 = System.currentTimeMillis();
/* 58 */     for (byte b2 = 0; b2 < '✐'; b2++) {
/*    */       
/* 60 */       String str = arrayOfString[b2];
/* 61 */       Object object = c.coalesce(str);
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 66 */     long l2 = System.currentTimeMillis() - l1;
/* 67 */     System.out.println("avg time: " + ((float)l2 / 10000.0F) + "ms (" + '✐' + " iterations)");
/*    */     
/* 69 */     System.err.println("num coalesced: " + c.countCoalesced());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/coalesce/CoalesceTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
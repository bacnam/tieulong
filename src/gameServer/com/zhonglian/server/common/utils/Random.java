/*    */ package com.zhonglian.server.common.utils;
/*    */ 
/*    */ import java.util.UUID;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Random
/*    */ {
/*    */   public static int nextInt(int n) {
/* 23 */     if (n == 0) {
/* 24 */       return 0;
/*    */     }
/* 26 */     int res = Math.abs(UUID.randomUUID().hashCode()) % n;
/* 27 */     return res;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int nextInt(int n, int offset) {
/* 39 */     if (n == 0) {
/* 40 */       return offset;
/*    */     }
/* 42 */     int res = Math.abs(UUID.randomUUID().hashCode()) % n;
/* 43 */     return res + offset;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean nextBoolean() {
/* 52 */     return (nextInt(2) == 1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static byte nextByte() {
/* 61 */     return (byte)nextInt(256);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static long nextLong(long n) {
/* 71 */     if (n == 0L) {
/* 72 */       return 0L;
/*    */     }
/* 74 */     long head = nextInt(2147483647);
/* 75 */     long l = nextInt(2147483647);
/*    */     
/* 77 */     long dividend = (head << 32L) + l;
/*    */     
/* 79 */     long remain = dividend - dividend / n * n;
/*    */     
/* 81 */     if (n < 0L) {
/* 82 */       return 0L - remain;
/*    */     }
/* 84 */     return remain;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isTrue(float ratio) {
/* 89 */     float fl = (float)Math.random();
/* 90 */     if (fl <= ratio)
/* 91 */       return true; 
/* 92 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/Random.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
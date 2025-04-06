/*    */ package com.zhonglian.server.common;
/*    */ 
/*    */ public class Scid
/*    */ {
/*    */   public static long getScid(int sid, int cid) {
/*  6 */     if (cid == 0)
/*  7 */       return 0L; 
/*  8 */     return sid * 100000000000L + cid % 100000000000L;
/*    */   }
/*    */   
/*    */   public static int getCid(long scid) {
/* 12 */     return (int)(scid % 100000000000L);
/*    */   }
/*    */   
/*    */   public static int getSid(long scid) {
/* 16 */     return (int)(scid / 100000000000L);
/*    */   }
/*    */   
/*    */   public static long getSgid(int sid, int gid) {
/* 20 */     if (gid == 0)
/* 21 */       return 0L; 
/* 22 */     return sid * 100000000000L + gid % 100000000000L;
/*    */   }
/*    */   
/*    */   public static int getSidBySgid(long sgid) {
/* 26 */     return (int)(sgid / 100000000000L);
/*    */   }
/*    */   
/*    */   public static int getGid(long sgid) {
/* 30 */     return (int)(sgid % 100000000000L);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/Scid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
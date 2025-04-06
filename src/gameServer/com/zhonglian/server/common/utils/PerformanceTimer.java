/*    */ package com.zhonglian.server.common.utils;
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
/*    */ public class PerformanceTimer
/*    */ {
/* 17 */   private long _begin = System.nanoTime();
/*    */   
/*    */   public void reset() {
/* 20 */     this._begin = System.nanoTime();
/*    */   }
/*    */   
/*    */   public long get() {
/* 24 */     return (System.nanoTime() - this._begin) / 1000000L;
/*    */   }
/*    */   
/*    */   public long getNano() {
/* 28 */     return System.nanoTime() - this._begin;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/PerformanceTimer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
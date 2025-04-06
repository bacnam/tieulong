/*    */ package com.zhonglian.server.common.mgr.daily;
/*    */ 
/*    */ import java.util.List;
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
/*    */ public interface IDailyRefreshRef
/*    */ {
/*    */   int getIndex();
/*    */   
/*    */   String getComment();
/*    */   
/*    */   StartRefer getStartRefer();
/*    */   
/*    */   int getFirstSec();
/*    */   
/*    */   int getInterval();
/*    */   
/*    */   DailyRefreshEventType getEventTypes();
/*    */   
/*    */   List<Integer> getEventValue();
/*    */   
/*    */   public enum DailyRefreshEventType
/*    */   {
/* 33 */     None,
/* 34 */     Day_h0,
/* 35 */     Day_h4,
/* 36 */     Day_h8,
/* 37 */     Day_h9,
/* 38 */     Day_h10,
/* 39 */     Day_h11,
/* 40 */     Day_h12,
/* 41 */     Day_h13,
/* 42 */     Day_h15,
/* 43 */     Day_h16,
/* 44 */     Day_h18,
/* 45 */     Day_h19,
/* 46 */     Day_h20,
/* 47 */     Day_h21,
/* 48 */     Day_h22,
/* 49 */     Day_h14,
/* 50 */     Week_d1h4,
/* 51 */     Week_d1h0,
/* 52 */     Every_m5,
/* 53 */     Every_m15,
/* 54 */     Every_m30,
/* 55 */     Every_h1,
/* 56 */     Every_h2,
/* 57 */     Every_h3,
/* 58 */     Every_h4,
/* 59 */     Every_h6,
/* 60 */     Every_h8,
/* 61 */     Every_h12;
/*    */   }
/*    */   
/*    */   public enum StartRefer
/*    */   {
/* 66 */     NowWeek,
/* 67 */     NowDay,
/* 68 */     NowHour,
/* 69 */     NowSec,
/* 70 */     StartServerDay;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/mgr/daily/IDailyRefreshRef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
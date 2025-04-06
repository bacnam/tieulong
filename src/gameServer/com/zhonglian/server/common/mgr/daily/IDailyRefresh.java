/*    */ package com.zhonglian.server.common.mgr.daily;
/*    */ 
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ public abstract class IDailyRefresh<Refresh extends BaseDailyRefreshEvent>
/*    */ {
/* 28 */   protected List<Refresh> refreshList = new ArrayList<>();
/* 29 */   protected Map<IDailyRefreshRef.DailyRefreshEventType, Refresh> refreshMap = new HashMap<>();
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
/*    */   public synchronized void process(int curSec) {
/* 41 */     List<Refresh> toDeals = new ArrayList<>();
/* 42 */     for (BaseDailyRefreshEvent baseDailyRefreshEvent : this.refreshList) {
/* 43 */       boolean needDeal = (curSec >= baseDailyRefreshEvent.getNextRefreshTime());
/* 44 */       if (needDeal) {
/* 45 */         toDeals.add((Refresh)baseDailyRefreshEvent);
/*    */       }
/*    */     } 
/* 48 */     if (toDeals.size() == 0) {
/*    */       return;
/*    */     }
/*    */     
/* 52 */     Collections.sort(toDeals, (o1, o2) -> {
/*    */           Refresh event1 = o1;
/*    */           
/*    */           Refresh event2 = o2;
/*    */           
/*    */           int diff = event1.getNextRefreshTime() - event2.getNextRefreshTime();
/*    */           
/*    */           return (diff != 0) ? diff : (((BaseDailyRefreshEvent)event1).ref.getIndex() - ((BaseDailyRefreshEvent)event2).ref.getIndex());
/*    */         });
/*    */     
/* 62 */     for (BaseDailyRefreshEvent baseDailyRefreshEvent : toDeals) {
/* 63 */       baseDailyRefreshEvent.process(curSec);
/*    */     }
/*    */   }
/*    */   
/*    */   public String toString() {
/* 68 */     StringBuilder sb = new StringBuilder();
/* 69 */     sb.append(String.valueOf(getClass().getSimpleName()) + " Refresh:").append(System.lineSeparator());
/* 70 */     sb.append(String.format("%-10s%-30s%-10s%-10s%s", new Object[] { "Index", "lastTime", "interval", "nextTime", "Comment" })).append(System.lineSeparator());
/* 71 */     for (BaseDailyRefreshEvent refresh : this.refreshList) {
/* 72 */       String lastTime = CommTime.getTimeStringS(refresh.getLastRefreshTime());
/* 73 */       String nextTime = CommTime.getTimeString((refresh.getLastRefreshTime() + refresh.ref.getInterval()));
/*    */       
/* 75 */       sb.append(String.format("%-10s%-30s%-10s%-10s%s", new Object[] { Integer.valueOf(refresh.ref.getIndex()), lastTime, Integer.valueOf(refresh.ref.getInterval()), nextTime, refresh.ref.getComment()
/* 76 */             })).append(System.lineSeparator());
/*    */     } 
/* 78 */     return sb.toString();
/*    */   }
/*    */   
/*    */   public Refresh getEvent(IDailyRefreshRef.DailyRefreshEventType event) {
/* 82 */     return this.refreshMap.get(event);
/*    */   }
/*    */   
/*    */   public int getNextFireTime(IDailyRefreshRef.DailyRefreshEventType event) {
/* 86 */     BaseDailyRefreshEvent refresh = (BaseDailyRefreshEvent)getEvent(event);
/* 87 */     if (refresh != null) {
/* 88 */       return refresh.getClosestRefreshTime();
/*    */     }
/* 90 */     return -1;
/*    */   }
/*    */   
/*    */   public abstract void setLastRefreshTime(int paramInt1, int paramInt2);
/*    */   
/*    */   public abstract int getLastRefreshTime(int paramInt);
/*    */   
/*    */   public abstract void gm_reset();
/*    */   
/*    */   public abstract void reload();
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/mgr/daily/IDailyRefresh.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
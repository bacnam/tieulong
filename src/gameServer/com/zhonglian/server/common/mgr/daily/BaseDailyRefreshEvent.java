/*     */ package com.zhonglian.server.common.mgr.daily;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import com.zhonglian.server.common.Config;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseDailyRefreshEvent
/*     */ {
/*  22 */   final int ONEDAY = 86400;
/*  23 */   final int ONEWEEK = 604800;
/*     */   
/*     */   protected IDailyRefresh<?> dailyRefresh;
/*     */   public IDailyRefreshRef ref;
/*     */   
/*     */   public BaseDailyRefreshEvent(IDailyRefreshRef ref, IDailyRefresh<?> dailyRefresh) {
/*  29 */     this.ref = ref;
/*  30 */     this.dailyRefresh = dailyRefresh;
/*     */   }
/*     */   
/*     */   public int getStartReferSec(int curSec) {
/*  34 */     int firstTime = 0;
/*  35 */     int firstSec = this.ref.getFirstSec();
/*  36 */     switch (this.ref.getStartRefer()) {
/*     */       case NowWeek:
/*  38 */         firstTime = CommTime.getFirstDayOfWeekZeroClockS() + firstSec;
/*     */         break;
/*     */       case null:
/*  41 */         firstTime = CommTime.getTodayZeroClockS() + firstSec;
/*     */         break;
/*     */       case NowHour:
/*  44 */         firstTime = CommTime.getTodayZeroClockS() + CommTime.getTodayHour() * 3600 + firstSec;
/*     */         break;
/*     */       
/*     */       case NowSec:
/*  48 */         firstTime = curSec + firstSec;
/*     */         break;
/*     */       case StartServerDay:
/*  51 */         firstTime = Config.getServerStartTime() + firstSec;
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  57 */     return firstTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInitLastSec() {
/*  64 */     int ret = -1;
/*     */     
/*  66 */     int curSec = CommTime.nowSecond();
/*  67 */     int interval = this.ref.getInterval();
/*  68 */     int firstTime = getStartReferSec(curSec);
/*     */     
/*  70 */     if (firstTime > curSec) {
/*     */       
/*  72 */       ret = firstTime - interval;
/*     */     } else {
/*     */       
/*  75 */       int passTimes = (curSec - firstTime) / interval;
/*  76 */       ret = firstTime + passTimes * interval;
/*     */     } 
/*     */     
/*  79 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void fixTime() {
/*  84 */     int lastRefresh = getLastRefreshTime();
/*     */ 
/*     */     
/*  87 */     if (lastRefresh == 0) {
/*  88 */       setLastRefreshTime(getInitLastSec());
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  93 */     boolean needFix = false;
/*  94 */     int interval = this.ref.getInterval();
/*  95 */     int newLastRefreshTime = 0;
/*     */     
/*  97 */     int thisRefreshTime = getStartReferSec(CommTime.nowSecond());
/*     */     
/*  99 */     if (interval == 86400) {
/* 100 */       newLastRefreshTime = CommTime.getZeroClockS(lastRefresh) + this.ref.getFirstSec();
/* 101 */       needFix = ((thisRefreshTime - lastRefresh) % interval != 0);
/* 102 */     } else if (interval == 604800) {
/* 103 */       newLastRefreshTime = (int)(CommTime.getFirstDayOfWeekZeroClockMS(lastRefresh * 1000L) / 1000L) + this.ref.getFirstSec();
/* 104 */       needFix = ((thisRefreshTime - lastRefresh) % interval != 0);
/*     */     } 
/*     */     
/* 107 */     if (!needFix) {
/*     */       return;
/*     */     }
/* 110 */     setLastRefreshTime(newLastRefreshTime);
/* 111 */     String oldTime = CommTime.getTimeStringS(lastRefresh);
/* 112 */     String newTime = CommTime.getTimeStringS(newLastRefreshTime);
/* 113 */     CommLog.info("fixTime index:{}, oldLast:{}, newLast{}", new Object[] { Integer.valueOf(this.ref.getIndex()), oldTime, newTime });
/*     */   }
/*     */ 
/*     */   
/*     */   public void process(int curSec) {
/* 118 */     int lastRefreshTime = getLastRefreshTime();
/* 119 */     int trigTimes = (curSec - lastRefreshTime) / this.ref.getInterval();
/*     */ 
/*     */     
/* 122 */     if (trigTimes > 0) {
/*     */       
/* 124 */       setLastRefreshTime(lastRefreshTime + trigTimes * this.ref.getInterval());
/* 125 */       onTriger(trigTimes);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onTriger(int trigTimes) {
/* 135 */     IDailyRefreshRef.DailyRefreshEventType event = this.ref.getEventTypes();
/*     */     
/* 137 */     long u1 = CommTime.nowMS();
/*     */     try {
/* 139 */       doEvent(trigTimes);
/* 140 */     } catch (Exception e) {
/* 141 */       CommLog.error("{}:onTriger id:{} event:{} time:{} times:{}", new Object[] { getClass().getSimpleName(), Integer.valueOf(this.ref.getIndex()), 
/* 142 */             (event == null) ? "null" : event.toString(), CommTime.getNowTimeString(), Integer.valueOf(trigTimes), e });
/*     */     } 
/*     */     
/* 145 */     long u2 = CommTime.nowMS();
/* 146 */     if (u2 - u1 > 50L) {
/* 147 */       CommLog.warn("daily refresh {} event:{} use {} ms", new Object[] { getClass().getSimpleName(), event, Long.valueOf(u2 - u1) });
/*     */     }
/*     */   }
/*     */   
/*     */   public int getClosestRefreshTime() {
/* 152 */     int closestRefreshTime = getNextRefreshTime() - CommTime.nowSecond();
/* 153 */     closestRefreshTime = (closestRefreshTime < 0) ? (closestRefreshTime % this.ref.getInterval()) : closestRefreshTime;
/* 154 */     return closestRefreshTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastRefreshTime(int nextRefreshTime) {
/* 164 */     this.dailyRefresh.setLastRefreshTime(this.ref.getIndex(), nextRefreshTime);
/*     */   }
/*     */   
/*     */   public int getLastRefreshTime() {
/* 168 */     return this.dailyRefresh.getLastRefreshTime(this.ref.getIndex());
/*     */   }
/*     */   
/*     */   public int getNextRefreshTime() {
/* 172 */     return this.dailyRefresh.getLastRefreshTime(this.ref.getIndex()) + this.ref.getInterval();
/*     */   }
/*     */   
/*     */   public abstract void doEvent(int paramInt);
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/mgr/daily/BaseDailyRefreshEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
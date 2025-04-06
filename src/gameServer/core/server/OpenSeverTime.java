/*    */ package core.server;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Calendar;
/*    */ import java.util.Date;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OpenSeverTime
/*    */ {
/*    */   private static OpenSeverTime instance;
/*    */   Date date;
/*    */   
/*    */   public static OpenSeverTime getInstance() {
/* 17 */     if (instance == null) {
/* 18 */       instance = new OpenSeverTime();
/*    */     }
/* 20 */     return instance;
/*    */   }
/*    */   
/*    */   public void init() {
/*    */     try {
/* 25 */       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 26 */       String time = System.getProperty("GameServer.OpenTime");
/* 27 */       time = time.trim();
/* 28 */       String minDate = "2016-04-30 00:00:00";
/* 29 */       if (time.isEmpty() || time.equalsIgnoreCase("0")) {
/* 30 */         this.date = sdf.parse(minDate);
/*    */         return;
/*    */       } 
/* 33 */       this.date = sdf.parse(time);
/* 34 */       Date min = sdf.parse(minDate);
/* 35 */       if (this.date.before(min)) {
/* 36 */         this.date = min;
/*    */       }
/* 38 */     } catch (Exception e) {
/* 39 */       CommLog.info(e.getMessage(), e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public Date getOpenDate() {
/* 44 */     return this.date;
/*    */   }
/*    */   
/*    */   public boolean isOverStartTime() {
/* 48 */     Date nowDate = new Date();
/* 49 */     return nowDate.after(this.date);
/*    */   }
/*    */   
/*    */   public int getOpenZeroTime() {
/* 53 */     Calendar calendar = Calendar.getInstance();
/* 54 */     calendar.setTime(this.date);
/* 55 */     calendar.set(11, 0);
/* 56 */     calendar.set(12, 0);
/* 57 */     calendar.set(13, 0);
/* 58 */     calendar.set(14, 0);
/* 59 */     return (int)(calendar.getTimeInMillis() / 1000L);
/*    */   }
/*    */   
/*    */   public int getOpenDays() {
/* 63 */     if (this.date == null) {
/* 64 */       return -1;
/*    */     }
/* 66 */     int today = CommTime.getZeroClockS(CommTime.nowSecond());
/* 67 */     int openday = getOpenZeroTime();
/* 68 */     int DaySec = 86400;
/* 69 */     int diff = (today - openday) / DaySec;
/*    */     
/* 71 */     return (diff >= 0) ? (diff + 1) : diff;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/server/OpenSeverTime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
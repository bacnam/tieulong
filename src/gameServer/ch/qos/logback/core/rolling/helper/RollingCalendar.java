/*     */ package ch.qos.logback.core.rolling.helper;
/*     */ 
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
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
/*     */ public class RollingCalendar
/*     */   extends GregorianCalendar
/*     */ {
/*     */   private static final long serialVersionUID = -5937537740925066161L;
/*  39 */   static final TimeZone GMT_TIMEZONE = TimeZone.getTimeZone("GMT");
/*     */   
/*  41 */   PeriodicityType periodicityType = PeriodicityType.ERRONEOUS;
/*     */ 
/*     */   
/*     */   public RollingCalendar() {}
/*     */ 
/*     */   
/*     */   public RollingCalendar(TimeZone tz, Locale locale) {
/*  48 */     super(tz, locale);
/*     */   }
/*     */   
/*     */   public void init(String datePattern) {
/*  52 */     this.periodicityType = computePeriodicityType(datePattern);
/*     */   }
/*     */   
/*     */   private void setPeriodicityType(PeriodicityType periodicityType) {
/*  56 */     this.periodicityType = periodicityType;
/*     */   }
/*     */   
/*     */   public PeriodicityType getPeriodicityType() {
/*  60 */     return this.periodicityType;
/*     */   }
/*     */   
/*     */   public long getNextTriggeringMillis(Date now) {
/*  64 */     return getNextTriggeringDate(now).getTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PeriodicityType computePeriodicityType(String datePattern) {
/*  76 */     RollingCalendar rollingCalendar = new RollingCalendar(GMT_TIMEZONE, Locale.getDefault());
/*     */ 
/*     */ 
/*     */     
/*  80 */     Date epoch = new Date(0L);
/*     */     
/*  82 */     if (datePattern != null) {
/*  83 */       for (PeriodicityType i : PeriodicityType.VALID_ORDERED_LIST) {
/*  84 */         SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
/*  85 */         simpleDateFormat.setTimeZone(GMT_TIMEZONE);
/*     */ 
/*     */         
/*  88 */         String r0 = simpleDateFormat.format(epoch);
/*  89 */         rollingCalendar.setPeriodicityType(i);
/*     */         
/*  91 */         Date next = new Date(rollingCalendar.getNextTriggeringMillis(epoch));
/*  92 */         String r1 = simpleDateFormat.format(next);
/*     */ 
/*     */         
/*  95 */         if (r0 != null && r1 != null && !r0.equals(r1)) {
/*  96 */           return i;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 101 */     return PeriodicityType.ERRONEOUS;
/*     */   }
/*     */   
/*     */   public void printPeriodicity(ContextAwareBase cab) {
/* 105 */     switch (this.periodicityType) {
/*     */       case TOP_OF_MILLISECOND:
/* 107 */         cab.addInfo("Roll-over every millisecond.");
/*     */         return;
/*     */       
/*     */       case TOP_OF_SECOND:
/* 111 */         cab.addInfo("Roll-over every second.");
/*     */         return;
/*     */       
/*     */       case TOP_OF_MINUTE:
/* 115 */         cab.addInfo("Roll-over every minute.");
/*     */         return;
/*     */       
/*     */       case TOP_OF_HOUR:
/* 119 */         cab.addInfo("Roll-over at the top of every hour.");
/*     */         return;
/*     */       
/*     */       case HALF_DAY:
/* 123 */         cab.addInfo("Roll-over at midday and midnight.");
/*     */         return;
/*     */       
/*     */       case TOP_OF_DAY:
/* 127 */         cab.addInfo("Roll-over at midnight.");
/*     */         return;
/*     */       
/*     */       case TOP_OF_WEEK:
/* 131 */         cab.addInfo("Rollover at the start of week.");
/*     */         return;
/*     */       
/*     */       case TOP_OF_MONTH:
/* 135 */         cab.addInfo("Rollover at start of every month.");
/*     */         return;
/*     */     } 
/*     */     
/* 139 */     cab.addInfo("Unknown periodicity.");
/*     */   }
/*     */ 
/*     */   
/*     */   public long periodsElapsed(long start, long end) {
/* 144 */     if (start > end) {
/* 145 */       throw new IllegalArgumentException("Start cannot come before end");
/*     */     }
/* 147 */     long diff = end - start;
/* 148 */     switch (this.periodicityType) {
/*     */       
/*     */       case TOP_OF_MILLISECOND:
/* 151 */         return diff;
/*     */       case TOP_OF_SECOND:
/* 153 */         return diff / 1000L;
/*     */       case TOP_OF_MINUTE:
/* 155 */         return diff / 60000L;
/*     */       case TOP_OF_HOUR:
/* 157 */         return ((int)diff / 3600000);
/*     */       case TOP_OF_DAY:
/* 159 */         return diff / 86400000L;
/*     */       case TOP_OF_WEEK:
/* 161 */         return diff / 604800000L;
/*     */       case TOP_OF_MONTH:
/* 163 */         return diffInMonths(start, end);
/*     */     } 
/* 165 */     throw new IllegalStateException("Unknown periodicity type.");
/*     */   }
/*     */ 
/*     */   
/*     */   public static int diffInMonths(long startTime, long endTime) {
/* 170 */     if (startTime > endTime)
/* 171 */       throw new IllegalArgumentException("startTime cannot be larger than endTime"); 
/* 172 */     Calendar startCal = Calendar.getInstance();
/* 173 */     startCal.setTimeInMillis(startTime);
/* 174 */     Calendar endCal = Calendar.getInstance();
/* 175 */     endCal.setTimeInMillis(endTime);
/* 176 */     int yearDiff = endCal.get(1) - startCal.get(1);
/* 177 */     int monthDiff = endCal.get(2) - startCal.get(2);
/* 178 */     return yearDiff * 12 + monthDiff;
/*     */   }
/*     */   
/*     */   public Date getRelativeDate(Date now, int periods) {
/* 182 */     setTime(now);
/*     */     
/* 184 */     switch (this.periodicityType) {
/*     */       case TOP_OF_MILLISECOND:
/* 186 */         add(14, periods);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 237 */         return getTime();case TOP_OF_SECOND: set(14, 0); add(13, periods); return getTime();case TOP_OF_MINUTE: set(13, 0); set(14, 0); add(12, periods); return getTime();case TOP_OF_HOUR: set(12, 0); set(13, 0); set(14, 0); add(11, periods); return getTime();case TOP_OF_DAY: set(11, 0); set(12, 0); set(13, 0); set(14, 0); add(5, periods); return getTime();case TOP_OF_WEEK: set(7, getFirstDayOfWeek()); set(11, 0); set(12, 0); set(13, 0); set(14, 0); add(3, periods); return getTime();case TOP_OF_MONTH: set(5, 1); set(11, 0); set(12, 0); set(13, 0); set(14, 0); add(2, periods); return getTime();
/*     */     } 
/*     */     throw new IllegalStateException("Unknown periodicity type.");
/*     */   } public Date getNextTriggeringDate(Date now) {
/* 241 */     return getRelativeDate(now, 1);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/rolling/helper/RollingCalendar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
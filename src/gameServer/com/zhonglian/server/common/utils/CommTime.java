/*     */ package com.zhonglian.server.common.utils;
/*     */ 
/*     */ import java.sql.Timestamp;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ public class CommTime
/*     */ {
/*  12 */   private static final TimeZone _timeZone = TimeZone.getDefault();
/*  13 */   private static Locale _loc = Locale.getDefault(Locale.Category.FORMAT);
/*     */   
/*  15 */   public static int RecentSec = 0;
/*     */   
/*     */   public static final int MinSec = 60;
/*     */   
/*     */   public static final int HourSec = 3600;
/*     */   
/*     */   public static final int DaySec = 86400;
/*     */   
/*     */   public static final int WeekSec = 604800;
/*     */ 
/*     */   
/*     */   public static long getTimezoneRawOffset() {
/*  27 */     return _timeZone.getRawOffset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getTimezoneDSTSavings() {
/*  36 */     return _timeZone.getDSTSavings();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getDateOffsetTiemZone(long timeMS) {
/*  45 */     return _timeZone.getOffset(timeMS) / 3600000;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TimeZone timezone() {
/*  54 */     return _timeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setTimezone(TimeZone tz) {
/*  63 */     _timeZone.setID(tz.getID());
/*  64 */     _timeZone.setRawOffset(tz.getRawOffset());
/*     */   }
/*     */   
/*     */   public static Locale getLocale() {
/*  68 */     return _loc;
/*     */   }
/*     */   
/*     */   public static void setLocale(Locale loc) {
/*  72 */     _loc = loc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getTodaySecond() {
/*  81 */     return nowSecond() - getTodayZeroClockS();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getTodayMinute() {
/*  90 */     Calendar objCalendar = newCalendar();
/*  91 */     int hour = objCalendar.get(11);
/*  92 */     int minute = objCalendar.get(12);
/*     */     
/*  94 */     return hour * 100 + minute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getTodayHour() {
/* 103 */     Calendar objCalendar = newCalendar();
/* 104 */     return objCalendar.get(11);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getTodayHourMS(int hour) {
/* 114 */     return getTodayZeroClockMS(0) + (hour * 3600000);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long nowMS() {
/* 123 */     Calendar objCalendar = newCalendar();
/*     */ 
/*     */     
/* 126 */     return objCalendar.getTimeInMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nowSecond() {
/* 135 */     Calendar objCalendar = newCalendar();
/* 136 */     int iNowTime = (int)(objCalendar.getTimeInMillis() / 1000L);
/* 137 */     return iNowTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getZeroClockMS(long timeMS) {
/* 147 */     Calendar objCalendar = newCalendar();
/* 148 */     objCalendar.setTimeInMillis(timeMS);
/* 149 */     int year = objCalendar.get(1);
/* 150 */     int month = objCalendar.get(2);
/* 151 */     int day = objCalendar.get(5);
/* 152 */     objCalendar.set(year, month, day, 0, 0, 0);
/*     */     
/* 154 */     return objCalendar.getTimeInMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getZeroClockS(int timeSec) {
/* 164 */     return (int)(getZeroClockMS(timeSec * 1000L) / 1000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long get24ClockMS(long timeMS) {
/* 174 */     Calendar objCalendar = newCalendar();
/* 175 */     objCalendar.setTimeInMillis(timeMS);
/* 176 */     int year = objCalendar.get(1);
/* 177 */     int month = objCalendar.get(2);
/* 178 */     int day = objCalendar.get(5);
/* 179 */     objCalendar.set(year, month, day + 1, 0, 0, 0);
/*     */     
/* 181 */     return objCalendar.getTimeInMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int get24ClockS(int timeSec) {
/* 191 */     return (int)(getZeroClockMS(timeSec * 1000L) / 1000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getTodayZeroClockMS() {
/* 200 */     return getTodayZeroClockMS(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getTodayZeroClockMS(int offsetSec) {
/* 210 */     Calendar objCalendar = newCalendar();
/* 211 */     objCalendar.add(11, -offsetSec / 3600);
/*     */     
/* 213 */     int year = objCalendar.get(1);
/* 214 */     int month = objCalendar.get(2);
/* 215 */     int day = objCalendar.get(5);
/* 216 */     objCalendar.set(year, month, day, 0, 0, 0);
/*     */     
/* 218 */     return objCalendar.getTimeInMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getTodayZeroClockS() {
/* 227 */     return (int)(getTodayZeroClockMS(0) / 1000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getTodayZeroClockS(int offsetSec) {
/* 237 */     return (int)(getTodayZeroClockMS(offsetSec) / 1000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getDayIndex(int time) {
/* 247 */     return getZeroClockS(time) / 86400;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getTodayIndex() {
/* 257 */     return getDayIndex(nowSecond());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getDayOfWeek() {
/* 266 */     Calendar cal = Calendar.getInstance();
/* 267 */     int w = cal.get(7) - 1;
/* 268 */     if (w == 0)
/* 269 */       w = 7; 
/* 270 */     return w;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNextHourSecond() {
/* 279 */     Calendar objCalendar = newCalendar();
/* 280 */     int minute = objCalendar.get(12);
/* 281 */     int second = objCalendar.get(13);
/* 282 */     return (60 - minute) * 60 - second;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNext12ColockLeft() {
/* 291 */     Calendar objCalendar = newCalendar();
/* 292 */     int year = objCalendar.get(1);
/* 293 */     int month = objCalendar.get(2);
/* 294 */     int day = objCalendar.get(5);
/* 295 */     Calendar targetCalendar = newCalendar();
/* 296 */     targetCalendar.set(year, month, day, 0, 0, 0);
/* 297 */     targetCalendar.add(5, 1);
/*     */     
/* 299 */     long nextRound = targetCalendar.getTimeInMillis() - objCalendar.getTimeInMillis();
/*     */     
/* 301 */     return (int)(nextRound / 1000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getFirstDayOfWeekZeroClockMS(long timeMS) {
/* 311 */     Calendar objCalendar = newCalendar();
/* 312 */     objCalendar.setTimeInMillis(timeMS);
/* 313 */     int year = objCalendar.get(1);
/* 314 */     int month = objCalendar.get(2);
/* 315 */     int day = objCalendar.get(5);
/* 316 */     int weekday = objCalendar.get(7);
/*     */     
/* 318 */     Calendar targetCalendar = newCalendar();
/* 319 */     targetCalendar.set(year, month, day - weekday + 1, 0, 0, 0);
/* 320 */     return targetCalendar.getTimeInMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getFirstDayOfWeekZeroClockMS() {
/* 329 */     Calendar objCalendar = newCalendar();
/* 330 */     int year = objCalendar.get(1);
/* 331 */     int month = objCalendar.get(2);
/* 332 */     int day = objCalendar.get(5);
/* 333 */     int weekday = objCalendar.get(7);
/* 334 */     Calendar targetCalendar = newCalendar();
/* 335 */     targetCalendar.set(year, month, day - weekday + 1, 0, 0, 0);
/*     */     
/* 337 */     return targetCalendar.getTimeInMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getFirstDayOfWeekZeroClockS() {
/* 346 */     return (int)(getFirstDayOfWeekZeroClockMS() / 1000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getFirstDayOfMonthZeroClockMS(int offsetSecond) {
/* 356 */     Calendar targetCalendar = newCalendar();
/* 357 */     if (offsetSecond != 0) {
/* 358 */       int offsetHour = offsetSecond / 3600;
/* 359 */       targetCalendar.add(11, -offsetHour);
/*     */     } 
/* 361 */     int year = targetCalendar.get(1);
/* 362 */     int month = targetCalendar.get(2);
/*     */     
/* 364 */     targetCalendar.set(year, month, 1, 0, 0, 0);
/* 365 */     targetCalendar.set(14, 0);
/*     */     
/* 367 */     return targetCalendar.getTimeInMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getFirstDayOfMonthZeroClockS(int offsetSecond) {
/* 376 */     return (int)(getFirstDayOfMonthZeroClockMS(0) / 1000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNowWeekNum(int offsetHour) {
/* 385 */     Calendar objCalendar = newCalendar();
/*     */     
/* 387 */     objCalendar.add(11, -offsetHour);
/* 388 */     objCalendar.add(6, -1);
/*     */     
/* 390 */     int yearNum = objCalendar.get(1);
/* 391 */     int weekNum = objCalendar.get(3);
/*     */     
/* 393 */     if (weekNum == 1) {
/* 394 */       Calendar tmpWeek = newCalendar();
/* 395 */       tmpWeek.add(11, -offsetHour);
/* 396 */       objCalendar.add(6, -1);
/*     */       
/* 398 */       tmpWeek.set(5, 1);
/* 399 */       if (tmpWeek.get(7) > 1) {
/*     */         
/* 401 */         yearNum--;
/*     */ 
/*     */         
/* 404 */         tmpWeek.set(1, yearNum);
/* 405 */         weekNum = tmpWeek.getActualMaximum(3);
/*     */       } 
/*     */     } 
/*     */     
/* 409 */     int weekSerialize = yearNum * 100 + weekNum;
/*     */     
/* 411 */     return weekSerialize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNowDay() {
/* 420 */     Calendar cal = newCalendar();
/* 421 */     cal.setTime(new Date());
/* 422 */     int calDay = cal.get(7) - 1;
/* 423 */     if (calDay == 0) {
/* 424 */       return 7;
/*     */     }
/* 426 */     return calDay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNowDay(int offsetSecond) {
/* 435 */     Calendar objCalendar = newCalendar();
/* 436 */     objCalendar.setTime(new Date());
/*     */     
/* 438 */     int offsetHour = offsetSecond / 3600;
/*     */     
/* 440 */     objCalendar.add(11, -offsetHour);
/* 441 */     int calDay = objCalendar.get(7) - 1;
/* 442 */     if (calDay == 0) {
/* 443 */       return 7;
/*     */     }
/* 445 */     return calDay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNowMonthNum() {
/* 454 */     Calendar objCalendar = newCalendar();
/* 455 */     int yearNum = objCalendar.get(1);
/* 456 */     int monthNum = objCalendar.get(2);
/*     */     
/* 458 */     int monthSerialize = yearNum * 100 + monthNum;
/*     */     
/* 460 */     return monthSerialize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNowMonth(int offsetSecond) {
/* 469 */     Calendar objCalendar = newCalendar();
/*     */     
/* 471 */     if (offsetSecond != 0) {
/* 472 */       int offsetHour = offsetSecond / 3600;
/* 473 */       objCalendar.add(11, -offsetHour);
/*     */     } 
/*     */     
/* 476 */     int monthNum = objCalendar.get(2);
/*     */     
/* 478 */     return monthNum + 1;
/*     */   }
/*     */   
/*     */   public static int getFirstDayOfMonth() {
/* 482 */     Calendar objCalendar = newCalendar();
/* 483 */     int day = objCalendar.get(5);
/* 484 */     return day;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNowMonth() {
/* 493 */     Calendar objCalendar = newCalendar();
/* 494 */     int monthNum = objCalendar.get(2);
/*     */     
/* 496 */     return monthNum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Timestamp getNowTimestamp() {
/* 505 */     Calendar objCalendar = newCalendar();
/* 506 */     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", _loc);
/* 507 */     return Timestamp.valueOf(df.format(objCalendar.getTime()));
/*     */   }
/*     */   
/*     */   public static Timestamp getTimestamp(int timeSec) {
/* 511 */     if (timeSec <= 0) {
/* 512 */       return null;
/*     */     }
/* 514 */     Calendar objCalendar = newCalendar();
/* 515 */     objCalendar.setTimeInMillis(timeSec * 1000L);
/* 516 */     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", _loc);
/* 517 */     return Timestamp.valueOf(df.format(objCalendar.getTime()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getNowTimeString() {
/* 527 */     Calendar objCalendar = newCalendar();
/* 528 */     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", _loc);
/* 529 */     return df.format(objCalendar.getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getStringMS() {
/* 538 */     Calendar objCalendar = newCalendar();
/* 539 */     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", _loc);
/* 540 */     return df.format(objCalendar.getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getNowTimeStringYMD() {
/* 549 */     Calendar objCalendar = newCalendar();
/* 550 */     SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", _loc);
/* 551 */     return df.format(objCalendar.getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getNowTimeStringYMDHMS() {
/* 560 */     Calendar objCalendar = newCalendar();
/* 561 */     SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", _loc);
/* 562 */     return df.format(objCalendar.getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getNowTimeString(String format) {
/* 571 */     Calendar objCalendar = newCalendar();
/* 572 */     SimpleDateFormat df = new SimpleDateFormat(format, _loc);
/* 573 */     return df.format(objCalendar.getTime());
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
/*     */   public static String getTimeStringMS(long lTimeMS) {
/* 585 */     Calendar objCalendar = newCalendar();
/* 586 */     objCalendar.setTimeInMillis(lTimeMS);
/* 587 */     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", _loc);
/* 588 */     return df.format(objCalendar.getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getTimeString(long lTimeS) {
/* 599 */     Calendar objCalendar = newCalendar();
/* 600 */     objCalendar.setTimeInMillis(lTimeS * 1000L);
/* 601 */     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", _loc);
/* 602 */     return df.format(objCalendar.getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getTimeStringS(long lTimeS) {
/* 613 */     Calendar objCalendar = newCalendar();
/* 614 */     objCalendar.setTimeInMillis(lTimeS * 1000L);
/* 615 */     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd(E) HH:mm:ss", _loc);
/* 616 */     return df.format(objCalendar.getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getTimeStringSYMD(long lTimeS) {
/* 627 */     if (lTimeS <= 0L) {
/* 628 */       return "";
/*     */     }
/* 630 */     Calendar objCalendar = newCalendar();
/* 631 */     objCalendar.setTimeInMillis(lTimeS * 1000L);
/* 632 */     SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", _loc);
/* 633 */     return df.format(objCalendar.getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getTimeString8601(long lTimeS) {
/* 644 */     if (lTimeS <= 0L) {
/* 645 */       return "";
/*     */     }
/* 647 */     Calendar objCalendar = newCalendar();
/* 648 */     objCalendar.setTimeInMillis(lTimeS * 1000L);
/* 649 */     SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.sssZ", _loc);
/* 650 */     return df.format(objCalendar.getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNowTimeDate(int offsetSecond) {
/* 659 */     Calendar objCalendar = newCalendar();
/*     */     
/* 661 */     int offsetHour = offsetSecond / 3600;
/* 662 */     objCalendar.add(11, -offsetHour);
/*     */     
/* 664 */     return _getDateNum(objCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNowTimeDate() {
/* 673 */     Calendar objCalendar = newCalendar();
/* 674 */     return _getDateNum(objCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNowTimeDate(Calendar objCalendar) {
/* 684 */     int nDate = objCalendar.get(1) * 10000 + (objCalendar.get(2) + 1) * 100 + objCalendar.get(5);
/* 685 */     return nDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getYesterdayDate() {
/* 694 */     Calendar objCalendar = newCalendar();
/* 695 */     objCalendar.add(5, -1);
/*     */     
/* 697 */     return _getDateNum(objCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getDateNum(int _deltaDay) {
/* 706 */     Calendar objCalendar = newCalendar();
/* 707 */     objCalendar.add(5, _deltaDay);
/*     */     
/* 709 */     return _getDateNum(objCalendar);
/*     */   }
/*     */   
/*     */   protected static int _getDateNum(Calendar _date) {
/* 713 */     int nDate = _date.get(1) * 10000 + (_date.get(2) + 1) * 100 + _date.get(5);
/* 714 */     return nDate;
/*     */   }
/*     */   
/*     */   private static Calendar newCalendar() {
/* 718 */     return Calendar.getInstance(_timeZone, _loc);
/*     */   }
/*     */   
/*     */   public static boolean isSameDayWithInTimeZone(int a, int b) {
/* 722 */     int offset = _timeZone.getRawOffset() / 1000;
/* 723 */     int ONEDAY = 86400;
/* 724 */     return ((a + offset) / 86400 == (b + offset) / 86400);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/CommTime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
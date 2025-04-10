package com.zhonglian.server.common.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CommTime
{
private static final TimeZone _timeZone = TimeZone.getDefault();
private static Locale _loc = Locale.getDefault(Locale.Category.FORMAT);

public static int RecentSec = 0;

public static final int MinSec = 60;

public static final int HourSec = 3600;

public static final int DaySec = 86400;

public static final int WeekSec = 604800;

public static long getTimezoneRawOffset() {
return _timeZone.getRawOffset();
}

public static long getTimezoneDSTSavings() {
return _timeZone.getDSTSavings();
}

public static int getDateOffsetTiemZone(long timeMS) {
return _timeZone.getOffset(timeMS) / 3600000;
}

public static TimeZone timezone() {
return _timeZone;
}

public static void setTimezone(TimeZone tz) {
_timeZone.setID(tz.getID());
_timeZone.setRawOffset(tz.getRawOffset());
}

public static Locale getLocale() {
return _loc;
}

public static void setLocale(Locale loc) {
_loc = loc;
}

public static int getTodaySecond() {
return nowSecond() - getTodayZeroClockS();
}

public static int getTodayMinute() {
Calendar objCalendar = newCalendar();
int hour = objCalendar.get(11);
int minute = objCalendar.get(12);

return hour * 100 + minute;
}

public static int getTodayHour() {
Calendar objCalendar = newCalendar();
return objCalendar.get(11);
}

public static long getTodayHourMS(int hour) {
return getTodayZeroClockMS(0) + (hour * 3600000);
}

public static long nowMS() {
Calendar objCalendar = newCalendar();

return objCalendar.getTimeInMillis();
}

public static int nowSecond() {
Calendar objCalendar = newCalendar();
int iNowTime = (int)(objCalendar.getTimeInMillis() / 1000L);
return iNowTime;
}

public static long getZeroClockMS(long timeMS) {
Calendar objCalendar = newCalendar();
objCalendar.setTimeInMillis(timeMS);
int year = objCalendar.get(1);
int month = objCalendar.get(2);
int day = objCalendar.get(5);
objCalendar.set(year, month, day, 0, 0, 0);

return objCalendar.getTimeInMillis();
}

public static int getZeroClockS(int timeSec) {
return (int)(getZeroClockMS(timeSec * 1000L) / 1000L);
}

public static long get24ClockMS(long timeMS) {
Calendar objCalendar = newCalendar();
objCalendar.setTimeInMillis(timeMS);
int year = objCalendar.get(1);
int month = objCalendar.get(2);
int day = objCalendar.get(5);
objCalendar.set(year, month, day + 1, 0, 0, 0);

return objCalendar.getTimeInMillis();
}

public static int get24ClockS(int timeSec) {
return (int)(getZeroClockMS(timeSec * 1000L) / 1000L);
}

public static long getTodayZeroClockMS() {
return getTodayZeroClockMS(0);
}

public static long getTodayZeroClockMS(int offsetSec) {
Calendar objCalendar = newCalendar();
objCalendar.add(11, -offsetSec / 3600);

int year = objCalendar.get(1);
int month = objCalendar.get(2);
int day = objCalendar.get(5);
objCalendar.set(year, month, day, 0, 0, 0);

return objCalendar.getTimeInMillis();
}

public static int getTodayZeroClockS() {
return (int)(getTodayZeroClockMS(0) / 1000L);
}

public static int getTodayZeroClockS(int offsetSec) {
return (int)(getTodayZeroClockMS(offsetSec) / 1000L);
}

public static int getDayIndex(int time) {
return getZeroClockS(time) / 86400;
}

public static int getTodayIndex() {
return getDayIndex(nowSecond());
}

public static int getDayOfWeek() {
Calendar cal = Calendar.getInstance();
int w = cal.get(7) - 1;
if (w == 0)
w = 7; 
return w;
}

public static int getNextHourSecond() {
Calendar objCalendar = newCalendar();
int minute = objCalendar.get(12);
int second = objCalendar.get(13);
return (60 - minute) * 60 - second;
}

public static int getNext12ColockLeft() {
Calendar objCalendar = newCalendar();
int year = objCalendar.get(1);
int month = objCalendar.get(2);
int day = objCalendar.get(5);
Calendar targetCalendar = newCalendar();
targetCalendar.set(year, month, day, 0, 0, 0);
targetCalendar.add(5, 1);

long nextRound = targetCalendar.getTimeInMillis() - objCalendar.getTimeInMillis();

return (int)(nextRound / 1000L);
}

public static long getFirstDayOfWeekZeroClockMS(long timeMS) {
Calendar objCalendar = newCalendar();
objCalendar.setTimeInMillis(timeMS);
int year = objCalendar.get(1);
int month = objCalendar.get(2);
int day = objCalendar.get(5);
int weekday = objCalendar.get(7);

Calendar targetCalendar = newCalendar();
targetCalendar.set(year, month, day - weekday + 1, 0, 0, 0);
return targetCalendar.getTimeInMillis();
}

public static long getFirstDayOfWeekZeroClockMS() {
Calendar objCalendar = newCalendar();
int year = objCalendar.get(1);
int month = objCalendar.get(2);
int day = objCalendar.get(5);
int weekday = objCalendar.get(7);
Calendar targetCalendar = newCalendar();
targetCalendar.set(year, month, day - weekday + 1, 0, 0, 0);

return targetCalendar.getTimeInMillis();
}

public static int getFirstDayOfWeekZeroClockS() {
return (int)(getFirstDayOfWeekZeroClockMS() / 1000L);
}

public static long getFirstDayOfMonthZeroClockMS(int offsetSecond) {
Calendar targetCalendar = newCalendar();
if (offsetSecond != 0) {
int offsetHour = offsetSecond / 3600;
targetCalendar.add(11, -offsetHour);
} 
int year = targetCalendar.get(1);
int month = targetCalendar.get(2);

targetCalendar.set(year, month, 1, 0, 0, 0);
targetCalendar.set(14, 0);

return targetCalendar.getTimeInMillis();
}

public static int getFirstDayOfMonthZeroClockS(int offsetSecond) {
return (int)(getFirstDayOfMonthZeroClockMS(0) / 1000L);
}

public static int getNowWeekNum(int offsetHour) {
Calendar objCalendar = newCalendar();

objCalendar.add(11, -offsetHour);
objCalendar.add(6, -1);

int yearNum = objCalendar.get(1);
int weekNum = objCalendar.get(3);

if (weekNum == 1) {
Calendar tmpWeek = newCalendar();
tmpWeek.add(11, -offsetHour);
objCalendar.add(6, -1);

tmpWeek.set(5, 1);
if (tmpWeek.get(7) > 1) {

yearNum--;

tmpWeek.set(1, yearNum);
weekNum = tmpWeek.getActualMaximum(3);
} 
} 

int weekSerialize = yearNum * 100 + weekNum;

return weekSerialize;
}

public static int getNowDay() {
Calendar cal = newCalendar();
cal.setTime(new Date());
int calDay = cal.get(7) - 1;
if (calDay == 0) {
return 7;
}
return calDay;
}

public static int getNowDay(int offsetSecond) {
Calendar objCalendar = newCalendar();
objCalendar.setTime(new Date());

int offsetHour = offsetSecond / 3600;

objCalendar.add(11, -offsetHour);
int calDay = objCalendar.get(7) - 1;
if (calDay == 0) {
return 7;
}
return calDay;
}

public static int getNowMonthNum() {
Calendar objCalendar = newCalendar();
int yearNum = objCalendar.get(1);
int monthNum = objCalendar.get(2);

int monthSerialize = yearNum * 100 + monthNum;

return monthSerialize;
}

public static int getNowMonth(int offsetSecond) {
Calendar objCalendar = newCalendar();

if (offsetSecond != 0) {
int offsetHour = offsetSecond / 3600;
objCalendar.add(11, -offsetHour);
} 

int monthNum = objCalendar.get(2);

return monthNum + 1;
}

public static int getFirstDayOfMonth() {
Calendar objCalendar = newCalendar();
int day = objCalendar.get(5);
return day;
}

public static int getNowMonth() {
Calendar objCalendar = newCalendar();
int monthNum = objCalendar.get(2);

return monthNum;
}

public static Timestamp getNowTimestamp() {
Calendar objCalendar = newCalendar();
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", _loc);
return Timestamp.valueOf(df.format(objCalendar.getTime()));
}

public static Timestamp getTimestamp(int timeSec) {
if (timeSec <= 0) {
return null;
}
Calendar objCalendar = newCalendar();
objCalendar.setTimeInMillis(timeSec * 1000L);
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", _loc);
return Timestamp.valueOf(df.format(objCalendar.getTime()));
}

public static String getNowTimeString() {
Calendar objCalendar = newCalendar();
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", _loc);
return df.format(objCalendar.getTime());
}

public static String getStringMS() {
Calendar objCalendar = newCalendar();
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", _loc);
return df.format(objCalendar.getTime());
}

public static String getNowTimeStringYMD() {
Calendar objCalendar = newCalendar();
SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", _loc);
return df.format(objCalendar.getTime());
}

public static String getNowTimeStringYMDHMS() {
Calendar objCalendar = newCalendar();
SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", _loc);
return df.format(objCalendar.getTime());
}

public static String getNowTimeString(String format) {
Calendar objCalendar = newCalendar();
SimpleDateFormat df = new SimpleDateFormat(format, _loc);
return df.format(objCalendar.getTime());
}

public static String getTimeStringMS(long lTimeMS) {
Calendar objCalendar = newCalendar();
objCalendar.setTimeInMillis(lTimeMS);
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", _loc);
return df.format(objCalendar.getTime());
}

public static String getTimeString(long lTimeS) {
Calendar objCalendar = newCalendar();
objCalendar.setTimeInMillis(lTimeS * 1000L);
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", _loc);
return df.format(objCalendar.getTime());
}

public static String getTimeStringS(long lTimeS) {
Calendar objCalendar = newCalendar();
objCalendar.setTimeInMillis(lTimeS * 1000L);
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd(E) HH:mm:ss", _loc);
return df.format(objCalendar.getTime());
}

public static String getTimeStringSYMD(long lTimeS) {
if (lTimeS <= 0L) {
return "";
}
Calendar objCalendar = newCalendar();
objCalendar.setTimeInMillis(lTimeS * 1000L);
SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", _loc);
return df.format(objCalendar.getTime());
}

public static String getTimeString8601(long lTimeS) {
if (lTimeS <= 0L) {
return "";
}
Calendar objCalendar = newCalendar();
objCalendar.setTimeInMillis(lTimeS * 1000L);
SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.sssZ", _loc);
return df.format(objCalendar.getTime());
}

public static int getNowTimeDate(int offsetSecond) {
Calendar objCalendar = newCalendar();

int offsetHour = offsetSecond / 3600;
objCalendar.add(11, -offsetHour);

return _getDateNum(objCalendar);
}

public static int getNowTimeDate() {
Calendar objCalendar = newCalendar();
return _getDateNum(objCalendar);
}

public static int getNowTimeDate(Calendar objCalendar) {
int nDate = objCalendar.get(1) * 10000 + (objCalendar.get(2) + 1) * 100 + objCalendar.get(5);
return nDate;
}

public static int getYesterdayDate() {
Calendar objCalendar = newCalendar();
objCalendar.add(5, -1);

return _getDateNum(objCalendar);
}

public static int getDateNum(int _deltaDay) {
Calendar objCalendar = newCalendar();
objCalendar.add(5, _deltaDay);

return _getDateNum(objCalendar);
}

protected static int _getDateNum(Calendar _date) {
int nDate = _date.get(1) * 10000 + (_date.get(2) + 1) * 100 + _date.get(5);
return nDate;
}

private static Calendar newCalendar() {
return Calendar.getInstance(_timeZone, _loc);
}

public static boolean isSameDayWithInTimeZone(int a, int b) {
int offset = _timeZone.getRawOffset() / 1000;
int ONEDAY = 86400;
return ((a + offset) / 86400 == (b + offset) / 86400);
}
}


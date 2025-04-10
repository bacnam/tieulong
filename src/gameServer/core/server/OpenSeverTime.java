package core.server;

import BaseCommon.CommLog;
import com.zhonglian.server.common.utils.CommTime;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OpenSeverTime
{
private static OpenSeverTime instance;
Date date;

public static OpenSeverTime getInstance() {
if (instance == null) {
instance = new OpenSeverTime();
}
return instance;
}

public void init() {
try {
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String time = System.getProperty("GameServer.OpenTime");
time = time.trim();
String minDate = "2016-04-30 00:00:00";
if (time.isEmpty() || time.equalsIgnoreCase("0")) {
this.date = sdf.parse(minDate);
return;
} 
this.date = sdf.parse(time);
Date min = sdf.parse(minDate);
if (this.date.before(min)) {
this.date = min;
}
} catch (Exception e) {
CommLog.info(e.getMessage(), e);
} 
}

public Date getOpenDate() {
return this.date;
}

public boolean isOverStartTime() {
Date nowDate = new Date();
return nowDate.after(this.date);
}

public int getOpenZeroTime() {
Calendar calendar = Calendar.getInstance();
calendar.setTime(this.date);
calendar.set(11, 0);
calendar.set(12, 0);
calendar.set(13, 0);
calendar.set(14, 0);
return (int)(calendar.getTimeInMillis() / 1000L);
}

public int getOpenDays() {
if (this.date == null) {
return -1;
}
int today = CommTime.getZeroClockS(CommTime.nowSecond());
int openday = getOpenZeroTime();
int DaySec = 86400;
int diff = (today - openday) / DaySec;

return (diff >= 0) ? (diff + 1) : diff;
}
}


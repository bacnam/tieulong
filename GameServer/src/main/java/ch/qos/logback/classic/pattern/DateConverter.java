package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.util.CachingDateFormatter;
import java.util.List;
import java.util.TimeZone;

public class DateConverter
extends ClassicConverter
{
long lastTimestamp = -1L;
String timestampStrCache = null;
CachingDateFormatter cachingDateFormatter = null;

public void start() {
String datePattern = getFirstOption();
if (datePattern == null) {
datePattern = "yyyy-MM-dd HH:mm:ss,SSS";
}

if (datePattern.equals("ISO8601")) {
datePattern = "yyyy-MM-dd HH:mm:ss,SSS";
}

try {
this.cachingDateFormatter = new CachingDateFormatter(datePattern);

}
catch (IllegalArgumentException e) {
addWarn("Could not instantiate SimpleDateFormat with pattern " + datePattern, e);

this.cachingDateFormatter = new CachingDateFormatter("yyyy-MM-dd HH:mm:ss,SSS");
} 

List<String> optionList = getOptionList();

if (optionList != null && optionList.size() > 1) {
TimeZone tz = TimeZone.getTimeZone(optionList.get(1));
this.cachingDateFormatter.setTimeZone(tz);
} 
}

public String convert(ILoggingEvent le) {
long timestamp = le.getTimeStamp();
return this.cachingDateFormatter.format(timestamp);
}
}


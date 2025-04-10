package com.mchange.v2.debug;

import com.mchange.lang.ThrowableUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ThreadNameStackTraceRecorder
{
static final String NL = System.getProperty("line.separator", "\r\n");

Set set = new HashSet();

String dumpHeader;
String stackTraceHeader;

public ThreadNameStackTraceRecorder(String paramString) {
this(paramString, "Debug Stack Trace.");
}

public ThreadNameStackTraceRecorder(String paramString1, String paramString2) {
this.dumpHeader = paramString1;
this.stackTraceHeader = paramString2;
}

public synchronized Object record() {
Record record = new Record(this.stackTraceHeader);
this.set.add(record);
return record;
}

public synchronized void remove(Object paramObject) {
this.set.remove(paramObject);
}
public synchronized int size() {
return this.set.size();
}
public synchronized String getDump() {
return getDump(null);
}

public synchronized String getDump(String paramString) {
SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss.SSSS");

StringBuffer stringBuffer = new StringBuffer(2047);
stringBuffer.append(NL);
stringBuffer.append("----------------------------------------------------");
stringBuffer.append(NL);
stringBuffer.append(this.dumpHeader);
stringBuffer.append(NL);
if (paramString != null) {

stringBuffer.append(paramString);
stringBuffer.append(NL);
} 
boolean bool = true;
for (Iterator<Record> iterator = this.set.iterator(); iterator.hasNext(); ) {

if (bool) {
bool = false;
} else {

stringBuffer.append("---");
stringBuffer.append(NL);
} 

Record record = iterator.next();
stringBuffer.append(simpleDateFormat.format(new Date(record.time)));
stringBuffer.append(" --> Thread Name: ");
stringBuffer.append(record.threadName);
stringBuffer.append(NL);
stringBuffer.append("Stack Trace: ");
stringBuffer.append(ThrowableUtils.extractStackTrace(record.stackTrace));
} 
stringBuffer.append("----------------------------------------------------");
stringBuffer.append(NL);
return stringBuffer.toString();
}

private static final class Record
implements Comparable
{
long time;
String threadName;
Throwable stackTrace;

Record(String param1String) {
this.time = System.currentTimeMillis();
this.threadName = Thread.currentThread().getName();
this.stackTrace = new Exception(param1String);
}

public int compareTo(Object param1Object) {
Record record = (Record)param1Object;
if (this.time > record.time)
return 1; 
if (this.time < record.time) {
return -1;
}

int i = System.identityHashCode(this);
int j = System.identityHashCode(record);
if (i > j)
return 1; 
if (i < j)
return -1; 
return 0;
}
}
}


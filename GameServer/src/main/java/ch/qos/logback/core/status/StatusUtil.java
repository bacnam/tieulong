package ch.qos.logback.core.status;

import ch.qos.logback.core.Context;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatusUtil
{
StatusManager sm;

public StatusUtil(StatusManager sm) {
this.sm = sm;
}

public StatusUtil(Context context) {
this.sm = context.getStatusManager();
}

public static boolean contextHasStatusListener(Context context) {
StatusManager sm = context.getStatusManager();
if (sm == null)
return false; 
List<StatusListener> listeners = sm.getCopyOfStatusListenerList();
if (listeners == null || listeners.size() == 0) {
return false;
}
return true;
}

public static List<Status> filterStatusListByTimeThreshold(List<Status> rawList, long threshold) {
List<Status> filteredList = new ArrayList<Status>();
for (Status s : rawList) {
if (s.getDate().longValue() >= threshold)
filteredList.add(s); 
} 
return filteredList;
}

public void addStatus(Status status) {
if (this.sm != null) {
this.sm.add(status);
}
}

public void addInfo(Object caller, String msg) {
addStatus(new InfoStatus(msg, caller));
}

public void addWarn(Object caller, String msg) {
addStatus(new WarnStatus(msg, caller));
}

public void addError(Object caller, String msg, Throwable t) {
addStatus(new ErrorStatus(msg, caller, t));
}

public boolean hasXMLParsingErrors(long threshold) {
return containsMatch(threshold, 2, "XML_PARSING");
}

public boolean noXMLParsingErrorsOccurred(long threshold) {
return !hasXMLParsingErrors(threshold);
}

public int getHighestLevel(long threshold) {
List<Status> filteredList = filterStatusListByTimeThreshold(this.sm.getCopyOfStatusList(), threshold);
int maxLevel = 0;
for (Status s : filteredList) {
if (s.getLevel() > maxLevel)
maxLevel = s.getLevel(); 
} 
return maxLevel;
}

public boolean isErrorFree(long threshold) {
return (2 > getHighestLevel(threshold));
}

public boolean containsMatch(long threshold, int level, String regex) {
List<Status> filteredList = filterStatusListByTimeThreshold(this.sm.getCopyOfStatusList(), threshold);
Pattern p = Pattern.compile(regex);

for (Status status : filteredList) {
if (level != status.getLevel()) {
continue;
}
String msg = status.getMessage();
Matcher matcher = p.matcher(msg);
if (matcher.lookingAt()) {
return true;
}
} 
return false;
}

public boolean containsMatch(int level, String regex) {
return containsMatch(0L, level, regex);
}

public boolean containsMatch(String regex) {
Pattern p = Pattern.compile(regex);
for (Status status : this.sm.getCopyOfStatusList()) {
String msg = status.getMessage();
Matcher matcher = p.matcher(msg);
if (matcher.lookingAt()) {
return true;
}
} 
return false;
}

public int matchCount(String regex) {
int count = 0;
Pattern p = Pattern.compile(regex);
for (Status status : this.sm.getCopyOfStatusList()) {
String msg = status.getMessage();
Matcher matcher = p.matcher(msg);
if (matcher.lookingAt()) {
count++;
}
} 
return count;
}

public boolean containsException(Class<?> exceptionType) {
Iterator<Status> stati = this.sm.getCopyOfStatusList().iterator();
while (stati.hasNext()) {
Status status = stati.next();
Throwable t = status.getThrowable();
if (t != null && t.getClass().getName().equals(exceptionType.getName())) {
return true;
}
} 
return false;
}

public long timeOfLastReset() {
List<Status> statusList = this.sm.getCopyOfStatusList();
if (statusList == null) {
return -1L;
}
int len = statusList.size();
for (int i = len - 1; i >= 0; i--) {
Status s = statusList.get(i);
if ("Will reset and reconfigure context ".equals(s.getMessage())) {
return s.getDate().longValue();
}
} 
return -1L;
}
}


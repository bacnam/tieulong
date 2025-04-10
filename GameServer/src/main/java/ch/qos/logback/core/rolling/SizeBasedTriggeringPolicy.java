package ch.qos.logback.core.rolling;

import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.InvocationGate;
import java.io.File;

public class SizeBasedTriggeringPolicy<E>
extends TriggeringPolicyBase<E>
{
public static final String SEE_SIZE_FORMAT = "http:
public static final long DEFAULT_MAX_FILE_SIZE = 10485760L;
String maxFileSizeAsString = Long.toString(10485760L);

FileSize maxFileSize;

private InvocationGate invocationGate;

public SizeBasedTriggeringPolicy() {
this.invocationGate = new InvocationGate(); } public SizeBasedTriggeringPolicy(String maxFileSize) { this.invocationGate = new InvocationGate();
setMaxFileSize(maxFileSize); }
public boolean isTriggeringEvent(File activeFile, E event) {
if (this.invocationGate.skipFurtherWork()) {
return false;
}
long now = System.currentTimeMillis();
this.invocationGate.updateMaskIfNecessary(now);

return (activeFile.length() >= this.maxFileSize.getSize());
}

public String getMaxFileSize() {
return this.maxFileSizeAsString;
}

public void setMaxFileSize(String maxFileSize) {
this.maxFileSizeAsString = maxFileSize;
this.maxFileSize = FileSize.valueOf(maxFileSize);
}

long toFileSize(String value) {
if (value == null) {
return 10485760L;
}
String s = value.trim().toUpperCase();
long multiplier = 1L;

int index;
if ((index = s.indexOf("KB")) != -1) {
multiplier = 1024L;
s = s.substring(0, index);
} else if ((index = s.indexOf("MB")) != -1) {
multiplier = 1048576L;
s = s.substring(0, index);
} else if ((index = s.indexOf("GB")) != -1) {
multiplier = 1073741824L;
s = s.substring(0, index);
} 
if (s != null) {
try {
return Long.valueOf(s).longValue() * multiplier;
} catch (NumberFormatException e) {
addError("[" + s + "] is not in proper int format. Please refer to " + "http:

addError("[" + value + "] not in expected format.", e);
} 
}
return 10485760L;
}
}


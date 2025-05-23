package com.mchange.v2.cfg;

import com.mchange.v2.lang.ObjectUtils;

public final class DelayedLogItem
{
private Level level;
private String text;
private Throwable exception;

public enum Level
{
ALL, CONFIG, FINE, FINER, FINEST, INFO, OFF, SEVERE, WARNING;
}

public Level getLevel() {
return this.level;
} public String getText() { return this.text; } public Throwable getException() {
return this.exception;
}

public DelayedLogItem(Level paramLevel, String paramString, Throwable paramThrowable) {
this.level = paramLevel;
this.text = paramString;
this.exception = paramThrowable;
}

public DelayedLogItem(Level paramLevel, String paramString) {
this(paramLevel, paramString, null);
}

public boolean equals(Object paramObject) {
if (paramObject instanceof DelayedLogItem) {

DelayedLogItem delayedLogItem = (DelayedLogItem)paramObject;
return (this.level.equals(delayedLogItem.level) && this.text.equals(delayedLogItem.text) && ObjectUtils.eqOrBothNull(this.exception, delayedLogItem.exception));
} 

return false;
}

public int hashCode() {
return this.level.hashCode() ^ this.text.hashCode() ^ ObjectUtils.hashOrZero(this.exception);
}

public String toString() {
StringBuffer stringBuffer = new StringBuffer();
stringBuffer.append(getClass().getName());
stringBuffer.append(String.format(" [ level -> %s, text -> \"%s\", exception -> %s]", new Object[] { this.level, this.text, this.exception }));
return stringBuffer.toString();
}
}


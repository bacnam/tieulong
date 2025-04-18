package ch.qos.logback.classic.spi;

import java.io.Serializable;
import java.util.Arrays;

public class ThrowableProxyVO
implements IThrowableProxy, Serializable
{
private static final long serialVersionUID = -773438177285807139L;
private String className;
private String message;
private int commonFramesCount;
private StackTraceElementProxy[] stackTraceElementProxyArray;
private IThrowableProxy cause;
private IThrowableProxy[] suppressed;

public String getMessage() {
return this.message;
}

public String getClassName() {
return this.className;
}

public int getCommonFrames() {
return this.commonFramesCount;
}

public IThrowableProxy getCause() {
return this.cause;
}

public StackTraceElementProxy[] getStackTraceElementProxyArray() {
return this.stackTraceElementProxyArray;
}

public IThrowableProxy[] getSuppressed() {
return this.suppressed;
}

public int hashCode() {
int prime = 31;
int result = 1;
result = 31 * result + ((this.className == null) ? 0 : this.className.hashCode());

return result;
}

public boolean equals(Object obj) {
if (this == obj)
return true; 
if (obj == null)
return false; 
if (getClass() != obj.getClass())
return false; 
ThrowableProxyVO other = (ThrowableProxyVO)obj;

if (this.className == null) {
if (other.className != null)
return false; 
} else if (!this.className.equals(other.className)) {
return false;
} 
if (!Arrays.equals((Object[])this.stackTraceElementProxyArray, (Object[])other.stackTraceElementProxyArray)) {
return false;
}
if (!Arrays.equals((Object[])this.suppressed, (Object[])other.suppressed)) {
return false;
}
if (this.cause == null) {
if (other.cause != null)
return false; 
} else if (!this.cause.equals(other.cause)) {
return false;
} 
return true;
}

public static ThrowableProxyVO build(IThrowableProxy throwableProxy) {
if (throwableProxy == null) {
return null;
}
ThrowableProxyVO tpvo = new ThrowableProxyVO();
tpvo.className = throwableProxy.getClassName();
tpvo.message = throwableProxy.getMessage();
tpvo.commonFramesCount = throwableProxy.getCommonFrames();
tpvo.stackTraceElementProxyArray = throwableProxy.getStackTraceElementProxyArray();
IThrowableProxy cause = throwableProxy.getCause();
if (cause != null) {
tpvo.cause = build(cause);
}
IThrowableProxy[] suppressed = throwableProxy.getSuppressed();
if (suppressed != null) {
tpvo.suppressed = new IThrowableProxy[suppressed.length];
for (int i = 0; i < suppressed.length; i++) {
tpvo.suppressed[i] = build(suppressed[i]);
}
} 
return tpvo;
}
}


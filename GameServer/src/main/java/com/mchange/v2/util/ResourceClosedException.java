package com.mchange.v2.util;

import com.mchange.v2.lang.VersionUtils;

public class ResourceClosedException
extends RuntimeException
{
Throwable rootCause;

public ResourceClosedException(String paramString, Throwable paramThrowable) {
super(paramString);
setRootCause(paramThrowable);
}

public ResourceClosedException(Throwable paramThrowable) {
setRootCause(paramThrowable);
}

public ResourceClosedException(String paramString) {
super(paramString);
}

public ResourceClosedException() {}

public Throwable getCause() {
return this.rootCause;
}

private void setRootCause(Throwable paramThrowable) {
this.rootCause = paramThrowable;
if (VersionUtils.isAtLeastJavaVersion14())
initCause(paramThrowable); 
}
}


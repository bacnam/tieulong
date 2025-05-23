package com.mchange.util.impl;

import com.mchange.util.FailSuppressedMessageLogger;
import com.mchange.util.MessageLogger;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FSMessageLoggerAdapter
implements FailSuppressedMessageLogger
{
MessageLogger inner;
List failures = null;

public FSMessageLoggerAdapter(MessageLogger paramMessageLogger) {
this.inner = paramMessageLogger;
}

public void log(String paramString) {
try {
this.inner.log(paramString);
} catch (IOException iOException) {
addFailure(iOException);
} 
}
public void log(Throwable paramThrowable, String paramString) {
try {
this.inner.log(paramThrowable, paramString);
} catch (IOException iOException) {
addFailure(iOException);
} 
}

public synchronized Iterator getFailures() {
if (this.inner instanceof FailSuppressedMessageLogger)
return ((FailSuppressedMessageLogger)this.inner).getFailures(); 
return (this.failures != null) ? this.failures.iterator() : null;
}

public synchronized void clearFailures() {
if (this.inner instanceof FailSuppressedMessageLogger)
{ ((FailSuppressedMessageLogger)this.inner).clearFailures(); }
else { this.failures = null; }

}

private synchronized void addFailure(IOException paramIOException) {
if (this.failures == null)
this.failures = new LinkedList(); 
this.failures.add(paramIOException);
}
}


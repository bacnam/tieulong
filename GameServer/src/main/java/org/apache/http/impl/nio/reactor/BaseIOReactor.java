package org.apache.http.impl.nio.reactor;

import java.io.InterruptedIOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.nio.reactor.IOReactorExceptionHandler;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.util.Args;

@ThreadSafe
public class BaseIOReactor
extends AbstractIOReactor
{
private final long timeoutCheckInterval;
private final Set<IOSession> bufferingSessions;
private long lastTimeoutCheck;
private IOReactorExceptionHandler exceptionHandler = null;
private IOEventDispatch eventDispatch = null;

public BaseIOReactor(long selectTimeout) throws IOReactorException {
this(selectTimeout, false);
}

public BaseIOReactor(long selectTimeout, boolean interestOpsQueueing) throws IOReactorException {
super(selectTimeout, interestOpsQueueing);
this.bufferingSessions = new HashSet<IOSession>();
this.timeoutCheckInterval = selectTimeout;
this.lastTimeoutCheck = System.currentTimeMillis();
}

public void execute(IOEventDispatch eventDispatch) throws InterruptedIOException, IOReactorException {
Args.notNull(eventDispatch, "Event dispatcher");
this.eventDispatch = eventDispatch;
execute();
}

public void setExceptionHandler(IOReactorExceptionHandler exceptionHandler) {
this.exceptionHandler = exceptionHandler;
}

protected void handleRuntimeException(RuntimeException ex) {
if (this.exceptionHandler == null || !this.exceptionHandler.handle(ex)) {
throw ex;
}
}

protected void acceptable(SelectionKey key) {}

protected void connectable(SelectionKey key) {}

protected void readable(SelectionKey key) {
IOSession session = getSession(key);

try {
for (int i = 0; i < 5; i++) {
this.eventDispatch.inputReady(session);
if (!session.hasBufferedInput() || (session.getEventMask() & 0x1) == 0) {
break;
}
} 

if (session.hasBufferedInput()) {
this.bufferingSessions.add(session);
}
} catch (CancelledKeyException ex) {
queueClosedSession(session);
key.attach(null);
} catch (RuntimeException ex) {
handleRuntimeException(ex);
} 
}

protected void writable(SelectionKey key) {
IOSession session = getSession(key);
try {
this.eventDispatch.outputReady(session);
} catch (CancelledKeyException ex) {
queueClosedSession(session);
key.attach(null);
} catch (RuntimeException ex) {
handleRuntimeException(ex);
} 
}

protected void validate(Set<SelectionKey> keys) {
long currentTime = System.currentTimeMillis();
if (currentTime - this.lastTimeoutCheck >= this.timeoutCheckInterval) {
this.lastTimeoutCheck = currentTime;
if (keys != null) {
for (SelectionKey key : keys) {
timeoutCheck(key, currentTime);
}
}
} 
if (!this.bufferingSessions.isEmpty()) {
for (Iterator<IOSession> it = this.bufferingSessions.iterator(); it.hasNext(); ) {
IOSession session = it.next();
if (!session.hasBufferedInput()) {
it.remove();
continue;
} 
try {
if ((session.getEventMask() & 0x1) > 0) {
this.eventDispatch.inputReady(session);
if (!session.hasBufferedInput()) {
it.remove();
}
} 
} catch (CancelledKeyException ex) {
it.remove();
queueClosedSession(session);
} catch (RuntimeException ex) {
handleRuntimeException(ex);
} 
} 
}
}

protected void sessionCreated(SelectionKey key, IOSession session) {
try {
this.eventDispatch.connected(session);
} catch (CancelledKeyException ex) {
queueClosedSession(session);
} catch (RuntimeException ex) {
handleRuntimeException(ex);
} 
}

protected void sessionTimedOut(IOSession session) {
try {
this.eventDispatch.timeout(session);
} catch (CancelledKeyException ex) {
queueClosedSession(session);
} catch (RuntimeException ex) {
handleRuntimeException(ex);
} 
}

protected void sessionClosed(IOSession session) {
try {
this.eventDispatch.disconnected(session);
} catch (CancelledKeyException ex) {

} catch (RuntimeException ex) {
handleRuntimeException(ex);
} 
}
}


package org.apache.mina.core.session;

import java.util.Iterator;
import java.util.Set;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.util.ConcurrentHashSet;

public class IdleStatusChecker
{
private final Set<AbstractIoSession> sessions = (Set<AbstractIoSession>)new ConcurrentHashSet();

private final NotifyingTask notifyingTask = new NotifyingTask();

private final IoFutureListener<IoFuture> sessionCloseListener = new SessionCloseListener();

public void addSession(AbstractIoSession session) {
this.sessions.add(session);
CloseFuture closeFuture = session.getCloseFuture();

closeFuture.addListener(this.sessionCloseListener);
}

private void removeSession(AbstractIoSession session) {
this.sessions.remove(session);
}

public NotifyingTask getNotifyingTask() {
return this.notifyingTask;
}

public class NotifyingTask
implements Runnable
{
private volatile boolean cancelled;

private volatile Thread thread;

public void run() {
this.thread = Thread.currentThread();
try {
while (!this.cancelled)
{
long currentTime = System.currentTimeMillis();

notifySessions(currentTime);

try {
Thread.sleep(1000L);
} catch (InterruptedException e) {}
}

} finally {

this.thread = null;
} 
}

public void cancel() {
this.cancelled = true;
Thread thread = this.thread;
if (thread != null) {
thread.interrupt();
}
}

private void notifySessions(long currentTime) {
Iterator<AbstractIoSession> it = IdleStatusChecker.this.sessions.iterator();
while (it.hasNext()) {
AbstractIoSession session = it.next();
if (session.isConnected()) {
AbstractIoSession.notifyIdleSession(session, currentTime);
}
} 
}
}

private class SessionCloseListener
implements IoFutureListener<IoFuture>
{
public void operationComplete(IoFuture future) {
IdleStatusChecker.this.removeSession((AbstractIoSession)future.getSession());
}
}
}


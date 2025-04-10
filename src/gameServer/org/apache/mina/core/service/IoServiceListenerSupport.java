package org.apache.mina.core.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.util.ExceptionMonitor;

public class IoServiceListenerSupport
{
private final IoService service;
private final List<IoServiceListener> listeners = new CopyOnWriteArrayList<IoServiceListener>();

private final ConcurrentMap<Long, IoSession> managedSessions = new ConcurrentHashMap<Long, IoSession>();

private final Map<Long, IoSession> readOnlyManagedSessions = Collections.unmodifiableMap(this.managedSessions);

private final AtomicBoolean activated = new AtomicBoolean();

private volatile long activationTime;

private volatile int largestManagedSessionCount = 0;

private volatile long cumulativeManagedSessionCount = 0L;

public IoServiceListenerSupport(IoService service) {
if (service == null) {
throw new IllegalArgumentException("service");
}

this.service = service;
}

public void add(IoServiceListener listener) {
if (listener != null) {
this.listeners.add(listener);
}
}

public void remove(IoServiceListener listener) {
if (listener != null) {
this.listeners.remove(listener);
}
}

public long getActivationTime() {
return this.activationTime;
}

public Map<Long, IoSession> getManagedSessions() {
return this.readOnlyManagedSessions;
}

public int getManagedSessionCount() {
return this.managedSessions.size();
}

public int getLargestManagedSessionCount() {
return this.largestManagedSessionCount;
}

public long getCumulativeManagedSessionCount() {
return this.cumulativeManagedSessionCount;
}

public boolean isActive() {
return this.activated.get();
}

public void fireServiceActivated() {
if (!this.activated.compareAndSet(false, true)) {
return;
}

this.activationTime = System.currentTimeMillis();

for (IoServiceListener listener : this.listeners) {
try {
listener.serviceActivated(this.service);
} catch (Exception e) {
ExceptionMonitor.getInstance().exceptionCaught(e);
} 
} 
}

public void fireServiceDeactivated() {
if (!this.activated.compareAndSet(true, false)) {
return;
}

try {
for (IoServiceListener listener : this.listeners) {
try {
listener.serviceDeactivated(this.service);
} catch (Exception e) {
ExceptionMonitor.getInstance().exceptionCaught(e);
} 
} 
} finally {
disconnectSessions();
} 
}

public void fireSessionCreated(IoSession session) {
boolean firstSession = false;

if (session.getService() instanceof IoConnector) {
synchronized (this.managedSessions) {
firstSession = this.managedSessions.isEmpty();
} 
}

if (this.managedSessions.putIfAbsent(Long.valueOf(session.getId()), session) != null) {
return;
}

if (firstSession) {
fireServiceActivated();
}

IoFilterChain filterChain = session.getFilterChain();
filterChain.fireSessionCreated();
filterChain.fireSessionOpened();

int managedSessionCount = this.managedSessions.size();

if (managedSessionCount > this.largestManagedSessionCount) {
this.largestManagedSessionCount = managedSessionCount;
}

this.cumulativeManagedSessionCount++;

for (IoServiceListener l : this.listeners) {
try {
l.sessionCreated(session);
} catch (Exception e) {
ExceptionMonitor.getInstance().exceptionCaught(e);
} 
} 
}

public void fireSessionDestroyed(IoSession session) {
if (this.managedSessions.remove(Long.valueOf(session.getId())) == null) {
return;
}

session.getFilterChain().fireSessionClosed();

try {
for (IoServiceListener l : this.listeners) {
try {
l.sessionDestroyed(session);
} catch (Exception e) {
ExceptionMonitor.getInstance().exceptionCaught(e);
} 
} 
} finally {

if (session.getService() instanceof IoConnector) {
boolean lastSession = false;

synchronized (this.managedSessions) {
lastSession = this.managedSessions.isEmpty();
} 

if (lastSession) {
fireServiceDeactivated();
}
} 
} 
}

private void disconnectSessions() {
if (!(this.service instanceof IoAcceptor)) {
return;
}

if (!((IoAcceptor)this.service).isCloseOnDeactivation()) {
return;
}

Object lock = new Object();
IoFutureListener<IoFuture> listener = new LockNotifyingListener(lock);

for (IoSession s : this.managedSessions.values()) {
s.close(true).addListener(listener);
}

try {
synchronized (lock) {
while (!this.managedSessions.isEmpty()) {
lock.wait(500L);
}
} 
} catch (InterruptedException ie) {}
}

private static class LockNotifyingListener
implements IoFutureListener<IoFuture>
{
private final Object lock;

public LockNotifyingListener(Object lock) {
this.lock = lock;
}

public void operationComplete(IoFuture future) {
synchronized (this.lock) {
this.lock.notifyAll();
} 
}
}
}


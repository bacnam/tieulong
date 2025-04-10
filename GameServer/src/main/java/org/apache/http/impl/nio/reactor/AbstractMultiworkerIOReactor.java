package org.apache.http.impl.nio.reactor;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.IOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.nio.reactor.IOReactorExceptionHandler;
import org.apache.http.nio.reactor.IOReactorStatus;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@ThreadSafe
public abstract class AbstractMultiworkerIOReactor
implements IOReactor
{
protected volatile IOReactorStatus status;
@Deprecated
protected final HttpParams params;
protected final IOReactorConfig config;
protected final Selector selector;
protected final long selectTimeout;
protected final boolean interestOpsQueueing;
private final int workerCount;
private final ThreadFactory threadFactory;
private final BaseIOReactor[] dispatchers;
private final Worker[] workers;
private final Thread[] threads;
private final Object statusLock;
protected IOReactorExceptionHandler exceptionHandler;
protected List<ExceptionEvent> auditLog;
private int currentWorker = 0;

public AbstractMultiworkerIOReactor(IOReactorConfig config, ThreadFactory threadFactory) throws IOReactorException {
this.config = (config != null) ? config : IOReactorConfig.DEFAULT;
this.params = (HttpParams)new BasicHttpParams();
try {
this.selector = Selector.open();
} catch (IOException ex) {
throw new IOReactorException("Failure opening selector", ex);
} 
this.selectTimeout = this.config.getSelectInterval();
this.interestOpsQueueing = this.config.isInterestOpQueued();
this.statusLock = new Object();
if (threadFactory != null) {
this.threadFactory = threadFactory;
} else {
this.threadFactory = new DefaultThreadFactory();
} 
this.auditLog = new ArrayList<ExceptionEvent>();
this.workerCount = this.config.getIoThreadCount();
this.dispatchers = new BaseIOReactor[this.workerCount];
this.workers = new Worker[this.workerCount];
this.threads = new Thread[this.workerCount];
this.status = IOReactorStatus.INACTIVE;
}

public AbstractMultiworkerIOReactor() throws IOReactorException {
this(null, null);
}

@Deprecated
static IOReactorConfig convert(int workerCount, HttpParams params) {
Args.notNull(params, "HTTP parameters");
return IOReactorConfig.custom().setSelectInterval(params.getLongParameter("http.nio.select-interval", 1000L)).setShutdownGracePeriod(params.getLongParameter("http.nio.grace-period", 500L)).setInterestOpQueued(params.getBooleanParameter("http.nio.select-interval", false)).setIoThreadCount(workerCount).setSoTimeout(params.getIntParameter("http.socket.timeout", 0)).setConnectTimeout(params.getIntParameter("http.connection.timeout", 0)).setSoTimeout(params.getIntParameter("http.socket.timeout", 0)).setSoReuseAddress(params.getBooleanParameter("http.socket.reuseaddr", false)).setSoKeepAlive(params.getBooleanParameter("http.socket.keepalive", false)).setSoLinger(params.getIntParameter("http.socket.linger", -1)).setTcpNoDelay(params.getBooleanParameter("http.tcp.nodelay", true)).build();
}

@Deprecated
public AbstractMultiworkerIOReactor(int workerCount, ThreadFactory threadFactory, HttpParams params) throws IOReactorException {
this(convert(workerCount, params), threadFactory);
}

public IOReactorStatus getStatus() {
return this.status;
}

public List<ExceptionEvent> getAuditLog() {
synchronized (this.auditLog) {
return new ArrayList<ExceptionEvent>(this.auditLog);
} 
}

protected synchronized void addExceptionEvent(Throwable ex, Date timestamp) {
if (ex == null) {
return;
}
synchronized (this.auditLog) {
this.auditLog.add(new ExceptionEvent(ex, (timestamp != null) ? timestamp : new Date()));
} 
}

protected void addExceptionEvent(Throwable ex) {
addExceptionEvent(ex, null);
}

public void setExceptionHandler(IOReactorExceptionHandler exceptionHandler) {
this.exceptionHandler = exceptionHandler;
}

protected abstract void processEvents(int paramInt) throws IOReactorException;

protected abstract void cancelRequests() throws IOReactorException;

public void execute(IOEventDispatch eventDispatch) throws InterruptedIOException, IOReactorException {
Args.notNull(eventDispatch, "Event dispatcher");
synchronized (this.statusLock) {
if (this.status.compareTo((Enum)IOReactorStatus.SHUTDOWN_REQUEST) >= 0) {
this.status = IOReactorStatus.SHUT_DOWN;
this.statusLock.notifyAll();
return;
} 
Asserts.check((this.status.compareTo((Enum)IOReactorStatus.INACTIVE) == 0), "Illegal state %s", this.status);

this.status = IOReactorStatus.ACTIVE;
int i;
for (i = 0; i < this.dispatchers.length; i++) {
BaseIOReactor dispatcher = new BaseIOReactor(this.selectTimeout, this.interestOpsQueueing);
dispatcher.setExceptionHandler(this.exceptionHandler);
this.dispatchers[i] = dispatcher;
} 
for (i = 0; i < this.workerCount; i++) {
BaseIOReactor dispatcher = this.dispatchers[i];
this.workers[i] = new Worker(dispatcher, eventDispatch);
this.threads[i] = this.threadFactory.newThread(this.workers[i]);
} 
} 

try {
for (int i = 0; i < this.workerCount; i++) {
if (this.status != IOReactorStatus.ACTIVE) {
return;
}
this.threads[i].start();
} 

do {
int readyCount;
try {
readyCount = this.selector.select(this.selectTimeout);
} catch (InterruptedIOException ex) {
throw ex;
} catch (IOException ex) {
throw new IOReactorException("Unexpected selector failure", ex);
} 

if (this.status.compareTo((Enum)IOReactorStatus.ACTIVE) == 0) {
processEvents(readyCount);
}

for (int j = 0; j < this.workerCount; j++) {
Worker worker = this.workers[j];
Exception ex = worker.getException();
if (ex != null) {
throw new IOReactorException("I/O dispatch worker terminated abnormally", ex);
}
}

}
while (this.status.compareTo((Enum)IOReactorStatus.ACTIVE) <= 0);

}
catch (ClosedSelectorException ex) {
addExceptionEvent(ex);
} catch (IOReactorException ex) {
if (ex.getCause() != null) {
addExceptionEvent(ex.getCause());
}
throw ex;
} finally {
doShutdown();
synchronized (this.statusLock) {
this.status = IOReactorStatus.SHUT_DOWN;
this.statusLock.notifyAll();
} 
} 
}

protected void doShutdown() throws InterruptedIOException {
synchronized (this.statusLock) {
if (this.status.compareTo((Enum)IOReactorStatus.SHUTTING_DOWN) >= 0) {
return;
}
this.status = IOReactorStatus.SHUTTING_DOWN;
} 
try {
cancelRequests();
} catch (IOReactorException ex) {
if (ex.getCause() != null) {
addExceptionEvent(ex.getCause());
}
} 
this.selector.wakeup();

if (this.selector.isOpen()) {
for (SelectionKey key : this.selector.keys()) {
try {
Channel channel = key.channel();
if (channel != null) {
channel.close();
}
} catch (IOException ex) {
addExceptionEvent(ex);
} 
} 

try {
this.selector.close();
} catch (IOException ex) {
addExceptionEvent(ex);
} 
} 

for (int i = 0; i < this.workerCount; i++) {
BaseIOReactor dispatcher = this.dispatchers[i];
dispatcher.gracefulShutdown();
} 

long gracePeriod = this.config.getShutdownGracePeriod();

try {
int j;

for (j = 0; j < this.workerCount; j++) {
BaseIOReactor dispatcher = this.dispatchers[j];
if (dispatcher.getStatus() != IOReactorStatus.INACTIVE) {
dispatcher.awaitShutdown(gracePeriod);
}
if (dispatcher.getStatus() != IOReactorStatus.SHUT_DOWN) {
try {
dispatcher.hardShutdown();
} catch (IOReactorException ex) {
if (ex.getCause() != null) {
addExceptionEvent(ex.getCause());
}
} 
}
} 

for (j = 0; j < this.workerCount; j++) {
Thread t = this.threads[j];
if (t != null) {
t.join(gracePeriod);
}
} 
} catch (InterruptedException ex) {
throw new InterruptedIOException(ex.getMessage());
} 
}

protected void addChannel(ChannelEntry entry) {
int i = Math.abs(this.currentWorker++ % this.workerCount);
this.dispatchers[i].addChannel(entry);
}

protected SelectionKey registerChannel(SelectableChannel channel, int ops) throws ClosedChannelException {
return channel.register(this.selector, ops);
}

protected void prepareSocket(Socket socket) throws IOException {
socket.setTcpNoDelay(this.config.isTcpNoDelay());
socket.setKeepAlive(this.config.isSoKeepalive());
if (this.config.getSoTimeout() > 0) {
socket.setSoTimeout(this.config.getSoTimeout());
}
if (this.config.getSndBufSize() > 0) {
socket.setSendBufferSize(this.config.getSndBufSize());
}
if (this.config.getRcvBufSize() > 0) {
socket.setReceiveBufferSize(this.config.getRcvBufSize());
}
int linger = this.config.getSoLinger();
if (linger >= 0) {
socket.setSoLinger(true, linger);
}
}

protected void awaitShutdown(long timeout) throws InterruptedException {
synchronized (this.statusLock) {
long deadline = System.currentTimeMillis() + timeout;
long remaining = timeout;
while (this.status != IOReactorStatus.SHUT_DOWN) {
this.statusLock.wait(remaining);
if (timeout > 0L) {
remaining = deadline - System.currentTimeMillis();
if (remaining <= 0L) {
break;
}
} 
} 
} 
}

public void shutdown() throws IOException {
shutdown(2000L);
}

public void shutdown(long waitMs) throws IOException {
synchronized (this.statusLock) {
if (this.status.compareTo((Enum)IOReactorStatus.ACTIVE) > 0) {
return;
}
if (this.status.compareTo((Enum)IOReactorStatus.INACTIVE) == 0) {
this.status = IOReactorStatus.SHUT_DOWN;
cancelRequests();
this.selector.close();
return;
} 
this.status = IOReactorStatus.SHUTDOWN_REQUEST;
} 
this.selector.wakeup();
try {
awaitShutdown(waitMs);
} catch (InterruptedException ignore) {}
}

static void closeChannel(Channel channel) {
try {
channel.close();
} catch (IOException ignore) {}
}

static class Worker
implements Runnable
{
final BaseIOReactor dispatcher;

final IOEventDispatch eventDispatch;
private volatile Exception exception;

public Worker(BaseIOReactor dispatcher, IOEventDispatch eventDispatch) {
this.dispatcher = dispatcher;
this.eventDispatch = eventDispatch;
}

public void run() {
try {
this.dispatcher.execute(this.eventDispatch);
} catch (Exception ex) {
this.exception = ex;
} 
}

public Exception getException() {
return this.exception;
}
}

static class DefaultThreadFactory
implements ThreadFactory
{
private static final AtomicLong COUNT = new AtomicLong(1L);

public Thread newThread(Runnable r) {
return new Thread(r, "I/O dispatcher " + COUNT.getAndIncrement());
}
}
}


package org.apache.http.impl.nio.reactor;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.nio.reactor.IOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.nio.reactor.IOReactorStatus;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@ThreadSafe
public abstract class AbstractIOReactor
implements IOReactor
{
private volatile IOReactorStatus status;
private final Object statusMutex;
private final long selectTimeout;
private final boolean interestOpsQueueing;
private final Selector selector;
private final Set<IOSession> sessions;
private final Queue<InterestOpEntry> interestOpsQueue;
private final Queue<IOSession> closedSessions;
private final Queue<ChannelEntry> newChannels;

public AbstractIOReactor(long selectTimeout) throws IOReactorException {
this(selectTimeout, false);
}

public AbstractIOReactor(long selectTimeout, boolean interestOpsQueueing) throws IOReactorException {
Args.positive(selectTimeout, "Select timeout");
this.selectTimeout = selectTimeout;
this.interestOpsQueueing = interestOpsQueueing;
this.sessions = Collections.synchronizedSet(new HashSet<IOSession>());
this.interestOpsQueue = new ConcurrentLinkedQueue<InterestOpEntry>();
this.closedSessions = new ConcurrentLinkedQueue<IOSession>();
this.newChannels = new ConcurrentLinkedQueue<ChannelEntry>();
try {
this.selector = Selector.open();
} catch (IOException ex) {
throw new IOReactorException("Failure opening selector", ex);
} 
this.statusMutex = new Object();
this.status = IOReactorStatus.INACTIVE;
}

protected void sessionCreated(SelectionKey key, IOSession session) {}

protected void sessionClosed(IOSession session) {}

protected void sessionTimedOut(IOSession session) {}

protected IOSession getSession(SelectionKey key) {
return (IOSession)key.attachment();
}

public IOReactorStatus getStatus() {
return this.status;
}

public boolean getInterestOpsQueueing() {
return this.interestOpsQueueing;
}

public void addChannel(ChannelEntry channelEntry) {
Args.notNull(channelEntry, "Channel entry");
this.newChannels.add(channelEntry);
this.selector.wakeup();
}

protected void execute() throws InterruptedIOException, IOReactorException {
this.status = IOReactorStatus.ACTIVE;

try { while (true) {
int readyCount;

try {
readyCount = this.selector.select(this.selectTimeout);
} catch (InterruptedIOException ex) {
throw ex;
} catch (IOException ex) {
throw new IOReactorException("Unexpected selector failure", ex);
} 

if (this.status == IOReactorStatus.SHUT_DOWN) {
break;
}

if (this.status == IOReactorStatus.SHUTTING_DOWN) {

closeSessions();
closeNewChannels();
} 

if (readyCount > 0) {
processEvents(this.selector.selectedKeys());
}

validate(this.selector.keys());

processClosedSessions();

if (this.status == IOReactorStatus.ACTIVE) {
processNewChannels();
}

if (this.status.compareTo((Enum)IOReactorStatus.ACTIVE) > 0 && this.sessions.isEmpty()) {
break;
}

if (this.interestOpsQueueing)
{
processPendingInterestOps();
}
}
}

catch (ClosedSelectorException ignore) {  }
finally
{ hardShutdown();
synchronized (this.statusMutex) {
this.statusMutex.notifyAll();
}  }

}

private void processEvents(Set<SelectionKey> selectedKeys) {
for (SelectionKey key : selectedKeys)
{
processEvent(key);
}

selectedKeys.clear();
}

protected void processEvent(SelectionKey key) {
IOSessionImpl session = (IOSessionImpl)key.attachment();
try {
if (key.isAcceptable()) {
acceptable(key);
}
if (key.isConnectable()) {
connectable(key);
}
if (key.isReadable()) {
session.resetLastRead();
readable(key);
} 
if (key.isWritable()) {
session.resetLastWrite();
writable(key);
} 
} catch (CancelledKeyException ex) {
queueClosedSession(session);
key.attach(null);
} 
}

protected void queueClosedSession(IOSession session) {
if (session != null) {
this.closedSessions.add(session);
}
}

private void processNewChannels() throws IOReactorException {
ChannelEntry entry;
while ((entry = this.newChannels.poll()) != null) {
SocketChannel channel;
SelectionKey key;
IOSession session;
try {
channel = entry.getChannel();
channel.configureBlocking(false);
key = channel.register(this.selector, 1);
} catch (ClosedChannelException ex) {
SessionRequestImpl sessionRequest = entry.getSessionRequest();
if (sessionRequest != null) {
sessionRequest.failed(ex);
}

return;
} catch (IOException ex) {
throw new IOReactorException("Failure registering channel with the selector", ex);
} 

SessionClosedCallback sessionClosedCallback = new SessionClosedCallback()
{
public void sessionClosed(IOSession session)
{
AbstractIOReactor.this.queueClosedSession(session);
}
};

InterestOpsCallback interestOpsCallback = null;
if (this.interestOpsQueueing) {
interestOpsCallback = new InterestOpsCallback()
{
public void addInterestOps(InterestOpEntry entry)
{
AbstractIOReactor.this.queueInterestOps(entry);
}
};
}

try {
session = new IOSessionImpl(key, interestOpsCallback, sessionClosedCallback);
int timeout = 0;
try {
timeout = channel.socket().getSoTimeout();
} catch (IOException ex) {}

session.setAttribute("http.session.attachment", entry.getAttachment());
session.setSocketTimeout(timeout);
} catch (CancelledKeyException ex) {
continue;
} 
try {
this.sessions.add(session);
SessionRequestImpl sessionRequest = entry.getSessionRequest();
if (sessionRequest != null) {
sessionRequest.completed(session);
}
key.attach(session);
sessionCreated(key, session);
} catch (CancelledKeyException ex) {
queueClosedSession(session);
key.attach(null);
} 
} 
}

private void processClosedSessions() {
IOSession session;
while ((session = this.closedSessions.poll()) != null) {
if (this.sessions.remove(session)) {
try {
sessionClosed(session);
} catch (CancelledKeyException ex) {}
}
} 
}

private void processPendingInterestOps() {
if (!this.interestOpsQueueing) {
return;
}
InterestOpEntry entry;
while ((entry = this.interestOpsQueue.poll()) != null) {

SelectionKey key = entry.getSelectionKey();
int eventMask = entry.getEventMask();
if (key.isValid()) {
key.interestOps(eventMask);
}
} 
}

private boolean queueInterestOps(InterestOpEntry entry) {
Asserts.check(this.interestOpsQueueing, "Interest ops queueing not enabled");
if (entry == null) {
return false;
}

this.interestOpsQueue.add(entry);

return true;
}

protected void timeoutCheck(SelectionKey key, long now) {
IOSessionImpl session = (IOSessionImpl)key.attachment();
if (session != null) {
int timeout = session.getSocketTimeout();
if (timeout > 0 && 
session.getLastAccessTime() + timeout < now) {
sessionTimedOut(session);
}
} 
}

protected void closeSessions() {
synchronized (this.sessions) {
for (IOSession session : this.sessions) {
session.close();
}
} 
}

protected void closeNewChannels() throws IOReactorException {
ChannelEntry entry;
while ((entry = this.newChannels.poll()) != null) {
SessionRequestImpl sessionRequest = entry.getSessionRequest();
if (sessionRequest != null) {
sessionRequest.cancel();
}
SocketChannel channel = entry.getChannel();
try {
channel.close();
} catch (IOException ignore) {}
} 
}

protected void closeActiveChannels() throws IOReactorException {
try {
Set<SelectionKey> keys = this.selector.keys();
for (SelectionKey key : keys) {
IOSession session = getSession(key);
if (session != null) {
session.close();
}
} 
this.selector.close();
} catch (IOException ignore) {}
}

public void gracefulShutdown() {
synchronized (this.statusMutex) {
if (this.status != IOReactorStatus.ACTIVE) {
return;
}

this.status = IOReactorStatus.SHUTTING_DOWN;
} 
this.selector.wakeup();
}

public void hardShutdown() throws IOReactorException {
synchronized (this.statusMutex) {
if (this.status == IOReactorStatus.SHUT_DOWN) {
return;
}

this.status = IOReactorStatus.SHUT_DOWN;
} 

closeNewChannels();
closeActiveChannels();
processClosedSessions();
}

public void awaitShutdown(long timeout) throws InterruptedException {
synchronized (this.statusMutex) {
long deadline = System.currentTimeMillis() + timeout;
long remaining = timeout;
while (this.status != IOReactorStatus.SHUT_DOWN) {
this.statusMutex.wait(remaining);
if (timeout > 0L) {
remaining = deadline - System.currentTimeMillis();
if (remaining <= 0L) {
break;
}
} 
} 
} 
}

public void shutdown(long gracePeriod) throws IOReactorException {
if (this.status != IOReactorStatus.INACTIVE) {
gracefulShutdown();
try {
awaitShutdown(gracePeriod);
} catch (InterruptedException ignore) {}
} 

if (this.status != IOReactorStatus.SHUT_DOWN) {
hardShutdown();
}
}

public void shutdown() throws IOReactorException {
shutdown(1000L);
}

protected abstract void acceptable(SelectionKey paramSelectionKey);

protected abstract void connectable(SelectionKey paramSelectionKey);

protected abstract void readable(SelectionKey paramSelectionKey);

protected abstract void writable(SelectionKey paramSelectionKey);

protected abstract void validate(Set<SelectionKey> paramSet);
}


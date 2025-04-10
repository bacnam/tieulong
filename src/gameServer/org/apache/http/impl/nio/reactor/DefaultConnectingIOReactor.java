package org.apache.http.impl.nio.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadFactory;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.nio.reactor.IOReactorStatus;
import org.apache.http.nio.reactor.SessionRequest;
import org.apache.http.nio.reactor.SessionRequestCallback;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Asserts;

@ThreadSafe
public class DefaultConnectingIOReactor
extends AbstractMultiworkerIOReactor
implements ConnectingIOReactor
{
private final Queue<SessionRequestImpl> requestQueue;
private long lastTimeoutCheck;

public DefaultConnectingIOReactor(IOReactorConfig config, ThreadFactory threadFactory) throws IOReactorException {
super(config, threadFactory);
this.requestQueue = new ConcurrentLinkedQueue<SessionRequestImpl>();
this.lastTimeoutCheck = System.currentTimeMillis();
}

public DefaultConnectingIOReactor(IOReactorConfig config) throws IOReactorException {
this(config, (ThreadFactory)null);
}

public DefaultConnectingIOReactor() throws IOReactorException {
this((IOReactorConfig)null, (ThreadFactory)null);
}

@Deprecated
public DefaultConnectingIOReactor(int workerCount, ThreadFactory threadFactory, HttpParams params) throws IOReactorException {
this(convert(workerCount, params), threadFactory);
}

@Deprecated
public DefaultConnectingIOReactor(int workerCount, HttpParams params) throws IOReactorException {
this(convert(workerCount, params), (ThreadFactory)null);
}

protected void cancelRequests() throws IOReactorException {
SessionRequestImpl request;
while ((request = this.requestQueue.poll()) != null) {
request.cancel();
}
}

protected void processEvents(int readyCount) throws IOReactorException {
processSessionRequests();

if (readyCount > 0) {
Set<SelectionKey> selectedKeys = this.selector.selectedKeys();
for (SelectionKey key : selectedKeys)
{
processEvent(key);
}

selectedKeys.clear();
} 

long currentTime = System.currentTimeMillis();
if (currentTime - this.lastTimeoutCheck >= this.selectTimeout) {
this.lastTimeoutCheck = currentTime;
Set<SelectionKey> keys = this.selector.keys();
processTimeouts(keys);
} 
}

private void processEvent(SelectionKey key) {
try {
if (key.isConnectable())
{
SocketChannel channel = (SocketChannel)key.channel();

SessionRequestHandle requestHandle = (SessionRequestHandle)key.attachment();
SessionRequestImpl sessionRequest = requestHandle.getSessionRequest();

try {
channel.finishConnect();
} catch (IOException ex) {
sessionRequest.failed(ex);
} 
key.cancel();
key.attach(null);
if (!sessionRequest.isCompleted()) {
addChannel(new ChannelEntry(channel, sessionRequest));
} else {
try {
channel.close();
} catch (IOException ignore) {}
}

}

} catch (CancelledKeyException ex) {
SessionRequestHandle requestHandle = (SessionRequestHandle)key.attachment();
key.attach(null);
if (requestHandle != null) {
SessionRequestImpl sessionRequest = requestHandle.getSessionRequest();
if (sessionRequest != null) {
sessionRequest.cancel();
}
} 
} 
}

private void processTimeouts(Set<SelectionKey> keys) {
long now = System.currentTimeMillis();
for (SelectionKey key : keys) {
Object attachment = key.attachment();

if (attachment instanceof SessionRequestHandle) {
SessionRequestHandle handle = (SessionRequestHandle)key.attachment();
SessionRequestImpl sessionRequest = handle.getSessionRequest();
int timeout = sessionRequest.getConnectTimeout();
if (timeout > 0 && 
handle.getRequestTime() + timeout < now) {
sessionRequest.timeout();
}
} 
} 
}

public SessionRequest connect(SocketAddress remoteAddress, SocketAddress localAddress, Object attachment, SessionRequestCallback callback) {
Asserts.check((this.status.compareTo((Enum)IOReactorStatus.ACTIVE) <= 0), "I/O reactor has been shut down");

SessionRequestImpl sessionRequest = new SessionRequestImpl(remoteAddress, localAddress, attachment, callback);

sessionRequest.setConnectTimeout(this.config.getConnectTimeout());

this.requestQueue.add(sessionRequest);
this.selector.wakeup();

return sessionRequest;
}

private void validateAddress(SocketAddress address) throws UnknownHostException {
if (address == null) {
return;
}
if (address instanceof InetSocketAddress) {
InetSocketAddress endpoint = (InetSocketAddress)address;
if (endpoint.isUnresolved()) {
throw new UnknownHostException(endpoint.getHostName());
}
} 
}

private void processSessionRequests() throws IOReactorException {
SessionRequestImpl request;
while ((request = this.requestQueue.poll()) != null) {
SocketChannel socketChannel; if (request.isCompleted()) {
continue;
}

try {
socketChannel = SocketChannel.open();
} catch (IOException ex) {
throw new IOReactorException("Failure opening socket", ex);
} 
try {
validateAddress(request.getLocalAddress());
validateAddress(request.getRemoteAddress());

socketChannel.configureBlocking(false);
prepareSocket(socketChannel.socket());

if (request.getLocalAddress() != null) {
Socket sock = socketChannel.socket();
sock.setReuseAddress(this.config.isSoReuseAddress());
sock.bind(request.getLocalAddress());
} 
boolean connected = socketChannel.connect(request.getRemoteAddress());
if (connected) {
ChannelEntry entry = new ChannelEntry(socketChannel, request);
addChannel(entry);
continue;
} 
} catch (IOException ex) {
closeChannel(socketChannel);
request.failed(ex);

return;
} 
SessionRequestHandle requestHandle = new SessionRequestHandle(request);
try {
SelectionKey key = socketChannel.register(this.selector, 8, requestHandle);

request.setKey(key);
} catch (IOException ex) {
closeChannel(socketChannel);
throw new IOReactorException("Failure registering channel with the selector", ex);
} 
} 
}
}


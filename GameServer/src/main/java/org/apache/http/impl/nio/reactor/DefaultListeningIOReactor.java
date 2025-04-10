package org.apache.http.impl.nio.reactor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadFactory;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.nio.reactor.IOReactorStatus;
import org.apache.http.nio.reactor.ListenerEndpoint;
import org.apache.http.nio.reactor.ListeningIOReactor;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Asserts;

@ThreadSafe
public class DefaultListeningIOReactor
extends AbstractMultiworkerIOReactor
implements ListeningIOReactor
{
private final Queue<ListenerEndpointImpl> requestQueue;
private final Set<ListenerEndpointImpl> endpoints;
private final Set<SocketAddress> pausedEndpoints;
private volatile boolean paused;

public DefaultListeningIOReactor(IOReactorConfig config, ThreadFactory threadFactory) throws IOReactorException {
super(config, threadFactory);
this.requestQueue = new ConcurrentLinkedQueue<ListenerEndpointImpl>();
this.endpoints = Collections.synchronizedSet(new HashSet<ListenerEndpointImpl>());
this.pausedEndpoints = new HashSet<SocketAddress>();
}

public DefaultListeningIOReactor(IOReactorConfig config) throws IOReactorException {
this(config, (ThreadFactory)null);
}

public DefaultListeningIOReactor() throws IOReactorException {
this((IOReactorConfig)null, (ThreadFactory)null);
}

@Deprecated
public DefaultListeningIOReactor(int workerCount, ThreadFactory threadFactory, HttpParams params) throws IOReactorException {
this(convert(workerCount, params), threadFactory);
}

@Deprecated
public DefaultListeningIOReactor(int workerCount, HttpParams params) throws IOReactorException {
this(convert(workerCount, params), (ThreadFactory)null);
}

protected void cancelRequests() throws IOReactorException {
ListenerEndpointImpl request;
while ((request = this.requestQueue.poll()) != null) {
request.cancel();
}
}

protected void processEvents(int readyCount) throws IOReactorException {
if (!this.paused) {
processSessionRequests();
}

if (readyCount > 0) {
Set<SelectionKey> selectedKeys = this.selector.selectedKeys();
for (SelectionKey key : selectedKeys)
{
processEvent(key);
}

selectedKeys.clear();
} 
}

private void processEvent(SelectionKey key) throws IOReactorException {
try {
if (key.isAcceptable()) {

ServerSocketChannel serverChannel = (ServerSocketChannel)key.channel();
while (true) {
SocketChannel socketChannel = null;
try {
socketChannel = serverChannel.accept();
} catch (IOException ex) {
if (this.exceptionHandler == null || !this.exceptionHandler.handle(ex))
{
throw new IOReactorException("Failure accepting connection", ex);
}
} 

if (socketChannel == null) {
break;
}
try {
prepareSocket(socketChannel.socket());
} catch (IOException ex) {
if (this.exceptionHandler == null || !this.exceptionHandler.handle(ex))
{
throw new IOReactorException("Failure initalizing socket", ex);
}
} 

ChannelEntry entry = new ChannelEntry(socketChannel);
addChannel(entry);
}

} 
} catch (CancelledKeyException ex) {
ListenerEndpoint endpoint = (ListenerEndpoint)key.attachment();
this.endpoints.remove(endpoint);
key.attach(null);
} 
}

private ListenerEndpointImpl createEndpoint(SocketAddress address) {
return new ListenerEndpointImpl(address, new ListenerEndpointClosedCallback()
{

public void endpointClosed(ListenerEndpoint endpoint)
{
DefaultListeningIOReactor.this.endpoints.remove(endpoint);
}
});
}

public ListenerEndpoint listen(SocketAddress address) {
Asserts.check((this.status.compareTo((Enum)IOReactorStatus.ACTIVE) <= 0), "I/O reactor has been shut down");

ListenerEndpointImpl request = createEndpoint(address);
this.requestQueue.add(request);
this.selector.wakeup();
return request;
}

private void processSessionRequests() throws IOReactorException {
ListenerEndpointImpl request;
while ((request = this.requestQueue.poll()) != null) {
ServerSocketChannel serverChannel; SocketAddress address = request.getAddress();

try {
serverChannel = ServerSocketChannel.open();
} catch (IOException ex) {
throw new IOReactorException("Failure opening server socket", ex);
} 
try {
ServerSocket socket = serverChannel.socket();
socket.setReuseAddress(this.config.isSoReuseAddress());
if (this.config.getSoTimeout() > 0) {
socket.setSoTimeout(this.config.getSoTimeout());
}
if (this.config.getRcvBufSize() > 0) {
socket.setReceiveBufferSize(this.config.getRcvBufSize());
}
serverChannel.configureBlocking(false);
socket.bind(address, this.config.getBacklogSize());
} catch (IOException ex) {
closeChannel(serverChannel);
request.failed(ex);
if (this.exceptionHandler == null || !this.exceptionHandler.handle(ex)) {
throw new IOReactorException("Failure binding socket to address " + address, ex);
}

return;
} 

try {
SelectionKey key = serverChannel.register(this.selector, 16);
key.attach(request);
request.setKey(key);
} catch (IOException ex) {
closeChannel(serverChannel);
throw new IOReactorException("Failure registering channel with the selector", ex);
} 

this.endpoints.add(request);
request.completed(serverChannel.socket().getLocalSocketAddress());
} 
}

public Set<ListenerEndpoint> getEndpoints() {
Set<ListenerEndpoint> set = new HashSet<ListenerEndpoint>();
synchronized (this.endpoints) {
Iterator<ListenerEndpointImpl> it = this.endpoints.iterator();
while (it.hasNext()) {
ListenerEndpoint endpoint = it.next();
if (!endpoint.isClosed()) {
set.add(endpoint); continue;
} 
it.remove();
} 
} 

return set;
}

public void pause() throws IOException {
if (this.paused) {
return;
}
this.paused = true;
synchronized (this.endpoints) {
for (ListenerEndpointImpl endpoint : this.endpoints) {
if (!endpoint.isClosed()) {
endpoint.close();
this.pausedEndpoints.add(endpoint.getAddress());
} 
} 
this.endpoints.clear();
} 
}

public void resume() throws IOException {
if (!this.paused) {
return;
}
this.paused = false;
for (SocketAddress address : this.pausedEndpoints) {
ListenerEndpointImpl request = createEndpoint(address);
this.requestQueue.add(request);
} 
this.pausedEndpoints.clear();
this.selector.wakeup();
}
}


package org.apache.mina.transport.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Executor;
import org.apache.mina.core.polling.AbstractPollingIoConnector;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.TransportMetadata;
import org.apache.mina.core.session.AbstractIoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.transport.socket.DefaultSocketSessionConfig;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.SocketSessionConfig;

public final class NioSocketConnector
extends AbstractPollingIoConnector<NioSession, SocketChannel>
implements SocketConnector
{
private volatile Selector selector;

public NioSocketConnector() {
super((IoSessionConfig)new DefaultSocketSessionConfig(), NioProcessor.class);
((DefaultSocketSessionConfig)getSessionConfig()).init((IoService)this);
}

public NioSocketConnector(int processorCount) {
super((IoSessionConfig)new DefaultSocketSessionConfig(), NioProcessor.class, processorCount);
((DefaultSocketSessionConfig)getSessionConfig()).init((IoService)this);
}

public NioSocketConnector(IoProcessor<NioSession> processor) {
super((IoSessionConfig)new DefaultSocketSessionConfig(), processor);
((DefaultSocketSessionConfig)getSessionConfig()).init((IoService)this);
}

public NioSocketConnector(Executor executor, IoProcessor<NioSession> processor) {
super((IoSessionConfig)new DefaultSocketSessionConfig(), executor, processor);
((DefaultSocketSessionConfig)getSessionConfig()).init((IoService)this);
}

public NioSocketConnector(Class<? extends IoProcessor<NioSession>> processorClass, int processorCount) {
super((IoSessionConfig)new DefaultSocketSessionConfig(), processorClass, processorCount);
}

public NioSocketConnector(Class<? extends IoProcessor<NioSession>> processorClass) {
super((IoSessionConfig)new DefaultSocketSessionConfig(), processorClass);
}

protected void init() throws Exception {
this.selector = Selector.open();
}

protected void destroy() throws Exception {
if (this.selector != null) {
this.selector.close();
}
}

public TransportMetadata getTransportMetadata() {
return NioSocketSession.METADATA;
}

public SocketSessionConfig getSessionConfig() {
return (SocketSessionConfig)this.sessionConfig;
}

public InetSocketAddress getDefaultRemoteAddress() {
return (InetSocketAddress)super.getDefaultRemoteAddress();
}

public void setDefaultRemoteAddress(InetSocketAddress defaultRemoteAddress) {
setDefaultRemoteAddress(defaultRemoteAddress);
}

protected Iterator<SocketChannel> allHandles() {
return new SocketChannelIterator(this.selector.keys());
}

protected boolean connect(SocketChannel handle, SocketAddress remoteAddress) throws Exception {
return handle.connect(remoteAddress);
}

protected AbstractPollingIoConnector<NioSession, SocketChannel>.ConnectionRequest getConnectionRequest(SocketChannel handle) {
SelectionKey key = handle.keyFor(this.selector);

if (key == null || !key.isValid()) {
return null;
}

return (AbstractPollingIoConnector.ConnectionRequest)key.attachment();
}

protected void close(SocketChannel handle) throws Exception {
SelectionKey key = handle.keyFor(this.selector);

if (key != null) {
key.cancel();
}

handle.close();
}

protected boolean finishConnect(SocketChannel handle) throws Exception {
if (handle.finishConnect()) {
SelectionKey key = handle.keyFor(this.selector);

if (key != null) {
key.cancel();
}

return true;
} 

return false;
}

protected SocketChannel newHandle(SocketAddress localAddress) throws Exception {
SocketChannel ch = SocketChannel.open();

int receiveBufferSize = getSessionConfig().getReceiveBufferSize();

if (receiveBufferSize > 65535) {
ch.socket().setReceiveBufferSize(receiveBufferSize);
}

if (localAddress != null) {
try {
ch.socket().bind(localAddress);
} catch (IOException ioe) {

String newMessage = "Error while binding on " + localAddress + "\n" + "original message : " + ioe.getMessage();

Exception e = new IOException(newMessage);
e.initCause(ioe.getCause());

ch.close();
throw ioe;
} 
}

ch.configureBlocking(false);

return ch;
}

protected NioSession newSession(IoProcessor<NioSession> processor, SocketChannel handle) {
return new NioSocketSession((IoService)this, processor, handle);
}

protected void register(SocketChannel handle, AbstractPollingIoConnector<NioSession, SocketChannel>.ConnectionRequest request) throws Exception {
handle.register(this.selector, 8, request);
}

protected int select(int timeout) throws Exception {
return this.selector.select(timeout);
}

protected Iterator<SocketChannel> selectedHandles() {
return new SocketChannelIterator(this.selector.selectedKeys());
}

protected void wakeup() {
this.selector.wakeup();
}

private static class SocketChannelIterator
implements Iterator<SocketChannel> {
private final Iterator<SelectionKey> i;

private SocketChannelIterator(Collection<SelectionKey> selectedKeys) {
this.i = selectedKeys.iterator();
}

public boolean hasNext() {
return this.i.hasNext();
}

public SocketChannel next() {
SelectionKey key = this.i.next();
return (SocketChannel)key.channel();
}

public void remove() {
this.i.remove();
}
}
}


package org.apache.mina.transport.socket.nio;

import org.apache.mina.core.polling.AbstractPollingIoAcceptor;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.TransportMetadata;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.transport.socket.DefaultSocketSessionConfig;
import org.apache.mina.transport.socket.SocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Executor;

public final class NioSocketAcceptor
        extends AbstractPollingIoAcceptor<NioSession, ServerSocketChannel>
        implements SocketAcceptor {
    private volatile Selector selector;
    private volatile SelectorProvider selectorProvider = null;

    public NioSocketAcceptor() {
        super((IoSessionConfig) new DefaultSocketSessionConfig(), NioProcessor.class);
        ((DefaultSocketSessionConfig) getSessionConfig()).init((IoService) this);
    }

    public NioSocketAcceptor(int processorCount) {
        super((IoSessionConfig) new DefaultSocketSessionConfig(), NioProcessor.class, processorCount);
        ((DefaultSocketSessionConfig) getSessionConfig()).init((IoService) this);
    }

    public NioSocketAcceptor(IoProcessor<NioSession> processor) {
        super((IoSessionConfig) new DefaultSocketSessionConfig(), processor);
        ((DefaultSocketSessionConfig) getSessionConfig()).init((IoService) this);
    }

    public NioSocketAcceptor(Executor executor, IoProcessor<NioSession> processor) {
        super((IoSessionConfig) new DefaultSocketSessionConfig(), executor, processor);
        ((DefaultSocketSessionConfig) getSessionConfig()).init((IoService) this);
    }

    public NioSocketAcceptor(int processorCount, SelectorProvider selectorProvider) {
        super((IoSessionConfig) new DefaultSocketSessionConfig(), NioProcessor.class, processorCount, selectorProvider);
        ((DefaultSocketSessionConfig) getSessionConfig()).init((IoService) this);
        this.selectorProvider = selectorProvider;
    }

    protected void init() throws Exception {
        this.selector = Selector.open();
    }

    protected void init(SelectorProvider selectorProvider) throws Exception {
        this.selectorProvider = selectorProvider;

        if (selectorProvider == null) {
            this.selector = Selector.open();
        } else {
            this.selector = selectorProvider.openSelector();
        }
    }

    protected void destroy() throws Exception {
        if (this.selector != null) {
            this.selector.close();
        }
    }

    public TransportMetadata getTransportMetadata() {
        return NioSocketSession.METADATA;
    }

    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) super.getLocalAddress();
    }

    public InetSocketAddress getDefaultLocalAddress() {
        return (InetSocketAddress) super.getDefaultLocalAddress();
    }

    public void setDefaultLocalAddress(InetSocketAddress localAddress) {
        setDefaultLocalAddress(localAddress);
    }

    protected NioSession accept(IoProcessor<NioSession> processor, ServerSocketChannel handle) throws Exception {
        SelectionKey key = null;

        if (handle != null) {
            key = handle.keyFor(this.selector);
        }

        if (key == null || !key.isValid() || !key.isAcceptable()) {
            return null;
        }

        SocketChannel ch = handle.accept();

        if (ch == null) {
            return null;
        }

        return new NioSocketSession((IoService) this, processor, ch);
    }

    protected ServerSocketChannel open(SocketAddress localAddress) throws Exception {
        ServerSocketChannel channel = null;

        if (this.selectorProvider != null) {
            channel = this.selectorProvider.openServerSocketChannel();
        } else {
            channel = ServerSocketChannel.open();
        }

        boolean success = false;

        try {
            channel.configureBlocking(false);

            ServerSocket socket = channel.socket();

            socket.setReuseAddress(isReuseAddress());

            try {
                socket.bind(localAddress, getBacklog());
            } catch (IOException ioe) {

                String newMessage = "Error while binding on " + localAddress + "\n" + "original message : " + ioe.getMessage();

                Exception e = new IOException(newMessage);
                e.initCause(ioe.getCause());

                channel.close();

                throw e;
            }

            channel.register(this.selector, 16);
            success = true;
        } finally {
            if (!success) {
                close(channel);
            }
        }
        return channel;
    }

    protected SocketAddress localAddress(ServerSocketChannel handle) throws Exception {
        return handle.socket().getLocalSocketAddress();
    }

    protected int select() throws Exception {
        return this.selector.select();
    }

    protected Iterator<ServerSocketChannel> selectedHandles() {
        return new ServerSocketChannelIterator(this.selector.selectedKeys());
    }

    protected void close(ServerSocketChannel handle) throws Exception {
        SelectionKey key = handle.keyFor(this.selector);

        if (key != null) {
            key.cancel();
        }

        handle.close();
    }

    protected void wakeup() {
        this.selector.wakeup();
    }

    private static class ServerSocketChannelIterator
            implements Iterator<ServerSocketChannel> {
        private final Iterator<SelectionKey> iterator;

        private ServerSocketChannelIterator(Collection<SelectionKey> selectedKeys) {
            this.iterator = selectedKeys.iterator();
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public ServerSocketChannel next() {
            SelectionKey key = this.iterator.next();

            if (key.isValid() && key.isAcceptable()) {
                return (ServerSocketChannel) key.channel();
            }

            return null;
        }

        public void remove() {
            this.iterator.remove();
        }
    }
}


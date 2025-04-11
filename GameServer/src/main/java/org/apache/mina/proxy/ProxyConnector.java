package org.apache.mina.proxy;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.file.FileRegion;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.DefaultConnectFuture;
import org.apache.mina.core.service.AbstractIoConnector;
import org.apache.mina.core.service.DefaultTransportMetadata;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.TransportMetadata;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.core.session.IoSessionInitializer;
import org.apache.mina.proxy.filter.ProxyFilter;
import org.apache.mina.proxy.session.ProxyIoSession;
import org.apache.mina.proxy.session.ProxyIoSessionInitializer;
import org.apache.mina.transport.socket.DefaultSocketSessionConfig;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.SocketSessionConfig;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executor;

public class ProxyConnector
        extends AbstractIoConnector {
    private static final TransportMetadata METADATA = (TransportMetadata) new DefaultTransportMetadata("proxy", "proxyconnector", false, true, InetSocketAddress.class, SocketSessionConfig.class, new Class[]{IoBuffer.class, FileRegion.class});
    private final ProxyFilter proxyFilter = new ProxyFilter();
    private SocketConnector connector = null;
    private ProxyIoSession proxyIoSession;

    private DefaultConnectFuture future;

    public ProxyConnector() {
        super((IoSessionConfig) new DefaultSocketSessionConfig(), null);
    }

    public ProxyConnector(SocketConnector connector) {
        this(connector, (IoSessionConfig) new DefaultSocketSessionConfig(), (Executor) null);
    }

    public ProxyConnector(SocketConnector connector, IoSessionConfig config, Executor executor) {
        super(config, executor);
        setConnector(connector);
    }

    public IoSessionConfig getSessionConfig() {
        return (IoSessionConfig) this.connector.getSessionConfig();
    }

    public ProxyIoSession getProxyIoSession() {
        return this.proxyIoSession;
    }

    public void setProxyIoSession(ProxyIoSession proxyIoSession) {
        if (proxyIoSession == null) {
            throw new IllegalArgumentException("proxySession object cannot be null");
        }

        if (proxyIoSession.getProxyAddress() == null) {
            throw new IllegalArgumentException("proxySession.proxyAddress cannot be null");
        }

        proxyIoSession.setConnector(this);
        setDefaultRemoteAddress(proxyIoSession.getProxyAddress());
        this.proxyIoSession = proxyIoSession;
    }

    protected ConnectFuture connect0(SocketAddress remoteAddress, SocketAddress localAddress, IoSessionInitializer<? extends ConnectFuture> sessionInitializer) {
        if (!this.proxyIoSession.isReconnectionNeeded()) {

            IoHandler handler = getHandler();
            if (!(handler instanceof AbstractProxyIoHandler)) {
                throw new IllegalArgumentException("IoHandler must be an instance of AbstractProxyIoHandler");
            }

            this.connector.setHandler(handler);
            this.future = new DefaultConnectFuture();
        }

        ConnectFuture conFuture = this.connector.connect(this.proxyIoSession.getProxyAddress(), (IoSessionInitializer) new ProxyIoSessionInitializer(sessionInitializer, this.proxyIoSession));

        if (this.proxyIoSession.getRequest() instanceof org.apache.mina.proxy.handlers.socks.SocksProxyRequest || this.proxyIoSession.isReconnectionNeeded()) {
            return conFuture;
        }

        return (ConnectFuture) this.future;
    }

    public void cancelConnectFuture() {
        this.future.cancel();
    }

    protected ConnectFuture fireConnected(IoSession session) {
        this.future.setSession(session);
        return (ConnectFuture) this.future;
    }

    public final SocketConnector getConnector() {
        return this.connector;
    }

    private final void setConnector(SocketConnector connector) {
        if (connector == null) {
            throw new IllegalArgumentException("connector cannot be null");
        }

        this.connector = connector;
        String className = ProxyFilter.class.getName();

        if (connector.getFilterChain().contains(className)) {
            connector.getFilterChain().remove(className);
        }

        connector.getFilterChain().addFirst(className, (IoFilter) this.proxyFilter);
    }

    protected void dispose0() throws Exception {
        if (this.connector != null) {
            this.connector.dispose();
        }
    }

    public TransportMetadata getTransportMetadata() {
        return METADATA;
    }
}


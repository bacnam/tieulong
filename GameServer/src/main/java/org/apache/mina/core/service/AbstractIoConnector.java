package org.apache.mina.core.service;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.core.session.IoSessionInitializer;

import java.net.SocketAddress;
import java.util.concurrent.Executor;

public abstract class AbstractIoConnector
        extends AbstractIoService
        implements IoConnector {
    private long connectTimeoutCheckInterval = 50L;

    private long connectTimeoutInMillis = 60000L;

    private SocketAddress defaultRemoteAddress;

    private SocketAddress defaultLocalAddress;

    protected AbstractIoConnector(IoSessionConfig sessionConfig, Executor executor) {
        super(sessionConfig, executor);
    }

    public long getConnectTimeoutCheckInterval() {
        return this.connectTimeoutCheckInterval;
    }

    public void setConnectTimeoutCheckInterval(long minimumConnectTimeout) {
        if (getConnectTimeoutMillis() < minimumConnectTimeout) {
            this.connectTimeoutInMillis = minimumConnectTimeout;
        }

        this.connectTimeoutCheckInterval = minimumConnectTimeout;
    }

    public final int getConnectTimeout() {
        return (int) this.connectTimeoutInMillis / 1000;
    }

    public final void setConnectTimeout(int connectTimeout) {
        setConnectTimeoutMillis(connectTimeout * 1000L);
    }

    public final long getConnectTimeoutMillis() {
        return this.connectTimeoutInMillis;
    }

    public final void setConnectTimeoutMillis(long connectTimeoutInMillis) {
        if (connectTimeoutInMillis <= this.connectTimeoutCheckInterval) {
            this.connectTimeoutCheckInterval = connectTimeoutInMillis;
        }
        this.connectTimeoutInMillis = connectTimeoutInMillis;
    }

    public SocketAddress getDefaultRemoteAddress() {
        return this.defaultRemoteAddress;
    }

    public final void setDefaultRemoteAddress(SocketAddress defaultRemoteAddress) {
        if (defaultRemoteAddress == null) {
            throw new IllegalArgumentException("defaultRemoteAddress");
        }

        if (!getTransportMetadata().getAddressType().isAssignableFrom(defaultRemoteAddress.getClass())) {
            throw new IllegalArgumentException("defaultRemoteAddress type: " + defaultRemoteAddress.getClass() + " (expected: " + getTransportMetadata().getAddressType() + ")");
        }

        this.defaultRemoteAddress = defaultRemoteAddress;
    }

    public final SocketAddress getDefaultLocalAddress() {
        return this.defaultLocalAddress;
    }

    public final void setDefaultLocalAddress(SocketAddress localAddress) {
        this.defaultLocalAddress = localAddress;
    }

    public final ConnectFuture connect() {
        SocketAddress defaultRemoteAddress = getDefaultRemoteAddress();
        if (defaultRemoteAddress == null) {
            throw new IllegalStateException("defaultRemoteAddress is not set.");
        }

        return connect(defaultRemoteAddress, (SocketAddress) null, (IoSessionInitializer<? extends ConnectFuture>) null);
    }

    public ConnectFuture connect(IoSessionInitializer<? extends ConnectFuture> sessionInitializer) {
        SocketAddress defaultRemoteAddress = getDefaultRemoteAddress();
        if (defaultRemoteAddress == null) {
            throw new IllegalStateException("defaultRemoteAddress is not set.");
        }

        return connect(defaultRemoteAddress, (SocketAddress) null, sessionInitializer);
    }

    public final ConnectFuture connect(SocketAddress remoteAddress) {
        return connect(remoteAddress, (SocketAddress) null, (IoSessionInitializer<? extends ConnectFuture>) null);
    }

    public ConnectFuture connect(SocketAddress remoteAddress, IoSessionInitializer<? extends ConnectFuture> sessionInitializer) {
        return connect(remoteAddress, (SocketAddress) null, sessionInitializer);
    }

    public ConnectFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
        return connect(remoteAddress, localAddress, (IoSessionInitializer<? extends ConnectFuture>) null);
    }

    public final ConnectFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, IoSessionInitializer<? extends ConnectFuture> sessionInitializer) {
        if (isDisposing()) {
            throw new IllegalStateException("The connector has been disposed.");
        }

        if (remoteAddress == null) {
            throw new IllegalArgumentException("remoteAddress");
        }

        if (!getTransportMetadata().getAddressType().isAssignableFrom(remoteAddress.getClass())) {
            throw new IllegalArgumentException("remoteAddress type: " + remoteAddress.getClass() + " (expected: " + getTransportMetadata().getAddressType() + ")");
        }

        if (localAddress != null && !getTransportMetadata().getAddressType().isAssignableFrom(localAddress.getClass())) {
            throw new IllegalArgumentException("localAddress type: " + localAddress.getClass() + " (expected: " + getTransportMetadata().getAddressType() + ")");
        }

        if (getHandler() == null) {
            if (getSessionConfig().isUseReadOperation()) {
                setHandler(new IoHandler() {
                    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
                    }

                    public void messageReceived(IoSession session, Object message) throws Exception {
                    }

                    public void messageSent(IoSession session, Object message) throws Exception {
                    }

                    public void sessionClosed(IoSession session) throws Exception {
                    }

                    public void sessionCreated(IoSession session) throws Exception {
                    }

                    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
                    }

                    public void sessionOpened(IoSession session) throws Exception {
                    }

                    public void inputClosed(IoSession session) throws Exception {
                    }
                });
            } else {
                throw new IllegalStateException("handler is not set.");
            }
        }

        return connect0(remoteAddress, localAddress, sessionInitializer);
    }

    protected abstract ConnectFuture connect0(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, IoSessionInitializer<? extends ConnectFuture> paramIoSessionInitializer);

    protected final void finishSessionInitialization0(final IoSession session, IoFuture future) {
        future.addListener(new IoFutureListener<ConnectFuture>() {
            public void operationComplete(ConnectFuture future) {
                if (future.isCanceled()) {
                    session.close(true);
                }
            }
        });
    }

    public String toString() {
        TransportMetadata m = getTransportMetadata();
        return '(' + m.getProviderName() + ' ' + m.getName() + " connector: " + "managedSessionCount: " + getManagedSessionCount() + ')';
    }
}


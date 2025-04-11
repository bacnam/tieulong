package org.apache.mina.core.service;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSessionInitializer;

import java.net.SocketAddress;

public interface IoConnector extends IoService {
    int getConnectTimeout();

    void setConnectTimeout(int paramInt);

    long getConnectTimeoutMillis();

    void setConnectTimeoutMillis(long paramLong);

    SocketAddress getDefaultRemoteAddress();

    void setDefaultRemoteAddress(SocketAddress paramSocketAddress);

    SocketAddress getDefaultLocalAddress();

    void setDefaultLocalAddress(SocketAddress paramSocketAddress);

    ConnectFuture connect();

    ConnectFuture connect(IoSessionInitializer<? extends ConnectFuture> paramIoSessionInitializer);

    ConnectFuture connect(SocketAddress paramSocketAddress);

    ConnectFuture connect(SocketAddress paramSocketAddress, IoSessionInitializer<? extends ConnectFuture> paramIoSessionInitializer);

    ConnectFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2);

    ConnectFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, IoSessionInitializer<? extends ConnectFuture> paramIoSessionInitializer);
}


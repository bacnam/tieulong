package org.apache.mina.transport.socket;

import org.apache.mina.core.service.IoAcceptor;

import java.net.InetSocketAddress;

public interface SocketAcceptor extends IoAcceptor {
    InetSocketAddress getLocalAddress();

    InetSocketAddress getDefaultLocalAddress();

    void setDefaultLocalAddress(InetSocketAddress paramInetSocketAddress);

    boolean isReuseAddress();

    void setReuseAddress(boolean paramBoolean);

    int getBacklog();

    void setBacklog(int paramInt);

    SocketSessionConfig getSessionConfig();
}


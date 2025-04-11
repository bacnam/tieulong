package org.apache.mina.transport.socket;

import org.apache.mina.core.service.IoConnector;

import java.net.InetSocketAddress;

public interface SocketConnector extends IoConnector {
    InetSocketAddress getDefaultRemoteAddress();

    void setDefaultRemoteAddress(InetSocketAddress paramInetSocketAddress);

    SocketSessionConfig getSessionConfig();
}


package org.apache.mina.transport.socket;

import org.apache.mina.core.service.IoConnector;

import java.net.InetSocketAddress;

public interface DatagramConnector extends IoConnector {
    InetSocketAddress getDefaultRemoteAddress();

    void setDefaultRemoteAddress(InetSocketAddress paramInetSocketAddress);

    DatagramSessionConfig getSessionConfig();
}


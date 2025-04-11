package org.apache.mina.transport.socket;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoSessionRecycler;

import java.net.InetSocketAddress;

public interface DatagramAcceptor extends IoAcceptor {
    InetSocketAddress getLocalAddress();

    InetSocketAddress getDefaultLocalAddress();

    void setDefaultLocalAddress(InetSocketAddress paramInetSocketAddress);

    IoSessionRecycler getSessionRecycler();

    void setSessionRecycler(IoSessionRecycler paramIoSessionRecycler);

    DatagramSessionConfig getSessionConfig();
}


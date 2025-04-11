package org.apache.mina.core.service;

import org.apache.mina.core.session.IoSession;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.List;
import java.util.Set;

public interface IoAcceptor extends IoService {
    SocketAddress getLocalAddress();

    Set<SocketAddress> getLocalAddresses();

    SocketAddress getDefaultLocalAddress();

    void setDefaultLocalAddress(SocketAddress paramSocketAddress);

    List<SocketAddress> getDefaultLocalAddresses();

    void setDefaultLocalAddresses(Iterable<? extends SocketAddress> paramIterable);

    void setDefaultLocalAddresses(List<? extends SocketAddress> paramList);

    void setDefaultLocalAddresses(SocketAddress paramSocketAddress, SocketAddress... paramVarArgs);

    boolean isCloseOnDeactivation();

    void setCloseOnDeactivation(boolean paramBoolean);

    void bind() throws IOException;

    void bind(SocketAddress paramSocketAddress) throws IOException;

    void bind(SocketAddress paramSocketAddress, SocketAddress... paramVarArgs) throws IOException;

    void bind(SocketAddress... paramVarArgs) throws IOException;

    void bind(Iterable<? extends SocketAddress> paramIterable) throws IOException;

    void unbind();

    void unbind(SocketAddress paramSocketAddress);

    void unbind(SocketAddress paramSocketAddress, SocketAddress... paramVarArgs);

    void unbind(Iterable<? extends SocketAddress> paramIterable);

    IoSession newSession(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2);
}


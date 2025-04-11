package org.apache.mina.core.service;

import org.apache.mina.core.session.IoSessionConfig;

import java.net.SocketAddress;
import java.util.Set;

public interface TransportMetadata {
    String getProviderName();

    String getName();

    boolean isConnectionless();

    boolean hasFragmentation();

    Class<? extends SocketAddress> getAddressType();

    Set<Class<? extends Object>> getEnvelopeTypes();

    Class<? extends IoSessionConfig> getSessionConfigType();
}


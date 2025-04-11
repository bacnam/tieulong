package org.apache.mina.core.service;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilterChainBuilder;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.core.session.IoSessionDataStructureFactory;

import java.util.Map;
import java.util.Set;

public interface IoService {
    TransportMetadata getTransportMetadata();

    void addListener(IoServiceListener paramIoServiceListener);

    void removeListener(IoServiceListener paramIoServiceListener);

    boolean isDisposing();

    boolean isDisposed();

    void dispose();

    void dispose(boolean paramBoolean);

    IoHandler getHandler();

    void setHandler(IoHandler paramIoHandler);

    Map<Long, IoSession> getManagedSessions();

    int getManagedSessionCount();

    IoSessionConfig getSessionConfig();

    IoFilterChainBuilder getFilterChainBuilder();

    void setFilterChainBuilder(IoFilterChainBuilder paramIoFilterChainBuilder);

    DefaultIoFilterChainBuilder getFilterChain();

    boolean isActive();

    long getActivationTime();

    Set<WriteFuture> broadcast(Object paramObject);

    IoSessionDataStructureFactory getSessionDataStructureFactory();

    void setSessionDataStructureFactory(IoSessionDataStructureFactory paramIoSessionDataStructureFactory);

    int getScheduledWriteBytes();

    int getScheduledWriteMessages();

    IoServiceStatistics getStatistics();
}


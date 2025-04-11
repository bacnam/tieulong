package org.apache.mina.transport.vmpipe;

import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.service.*;
import org.apache.mina.core.session.AbstractIoSession;
import org.apache.mina.core.write.WriteRequestQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class VmPipeSession
        extends AbstractIoSession {
    static final TransportMetadata METADATA = (TransportMetadata) new DefaultTransportMetadata("mina", "vmpipe", false, false, VmPipeAddress.class, VmPipeSessionConfig.class, new Class[]{Object.class});
    final BlockingQueue<Object> receivedMessageQueue;
    private final IoServiceListenerSupport serviceListeners;
    private final VmPipeAddress localAddress;
    private final VmPipeAddress remoteAddress;
    private final VmPipeAddress serviceAddress;
    private final VmPipeFilterChain filterChain;
    private final VmPipeSession remoteSession;
    private final Lock lock;

    VmPipeSession(IoService service, IoServiceListenerSupport serviceListeners, VmPipeAddress localAddress, IoHandler handler, VmPipe remoteEntry) {
        super(service);
        this.config = new DefaultVmPipeSessionConfig();
        this.serviceListeners = serviceListeners;
        this.lock = new ReentrantLock();
        this.localAddress = localAddress;
        this.remoteAddress = this.serviceAddress = remoteEntry.getAddress();
        this.filterChain = new VmPipeFilterChain(this);
        this.receivedMessageQueue = new LinkedBlockingQueue();

        this.remoteSession = new VmPipeSession(this, remoteEntry);
    }

    private VmPipeSession(VmPipeSession remoteSession, VmPipe entry) {
        super((IoService) entry.getAcceptor());
        this.config = new DefaultVmPipeSessionConfig();
        this.serviceListeners = entry.getListeners();
        this.lock = remoteSession.lock;
        this.localAddress = this.serviceAddress = remoteSession.remoteAddress;
        this.remoteAddress = remoteSession.localAddress;
        this.filterChain = new VmPipeFilterChain(this);
        this.remoteSession = remoteSession;
        this.receivedMessageQueue = new LinkedBlockingQueue();
    }

    public IoProcessor<VmPipeSession> getProcessor() {
        return this.filterChain.getProcessor();
    }

    IoServiceListenerSupport getServiceListeners() {
        return this.serviceListeners;
    }

    public VmPipeSessionConfig getConfig() {
        return (VmPipeSessionConfig) this.config;
    }

    public IoFilterChain getFilterChain() {
        return (IoFilterChain) this.filterChain;
    }

    public VmPipeSession getRemoteSession() {
        return this.remoteSession;
    }

    public TransportMetadata getTransportMetadata() {
        return METADATA;
    }

    public VmPipeAddress getRemoteAddress() {
        return this.remoteAddress;
    }

    public VmPipeAddress getLocalAddress() {
        return this.localAddress;
    }

    public VmPipeAddress getServiceAddress() {
        return this.serviceAddress;
    }

    void increaseWrittenBytes0(int increment, long currentTime) {
        increaseWrittenBytes(increment, currentTime);
    }

    WriteRequestQueue getWriteRequestQueue0() {
        return getWriteRequestQueue();
    }

    Lock getLock() {
        return this.lock;
    }
}


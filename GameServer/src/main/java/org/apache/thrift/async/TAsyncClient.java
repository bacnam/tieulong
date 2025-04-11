package org.apache.thrift.async;

import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TNonblockingTransport;

public abstract class TAsyncClient {
    protected final TProtocolFactory protocolFactory;
    protected final TNonblockingTransport transport;
    protected final TAsyncClientManager manager;
    private TAsyncMethodCall currentMethod;
    private Throwable error;
    private long timeout;

    public TAsyncClient(TProtocolFactory protocolFactory, TAsyncClientManager manager, TNonblockingTransport transport) {
        this(protocolFactory, manager, transport, 0);
    }

    public TAsyncClient(TProtocolFactory protocolFactory, TAsyncClientManager manager, TNonblockingTransport transport, long timeout) {
        this.protocolFactory = protocolFactory;
        this.manager = manager;
        this.transport = transport;
        this.timeout = timeout;
    }

    public TProtocolFactory getProtocolFactory() {
        return protocolFactory;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public boolean hasTimeout() {
        return timeout > 0;
    }

    public boolean hasError() {
        return error != null;
    }

    public Throwable getError() {
        return error;
    }

    protected void checkReady() {

        if (currentMethod != null) {
            throw new IllegalStateException("Client is currently executing another method: " + currentMethod.getClass().getName());
        }

        if (error != null) {
            throw new IllegalStateException("Client has an error!", error);
        }
    }

    protected void onComplete() {
        currentMethod = null;
    }

    protected void onError(Throwable throwable) {
        transport.close();
        currentMethod = null;
        error = throwable;
    }
}

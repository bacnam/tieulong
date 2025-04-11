package org.apache.http.impl.nio.reactor;

import org.apache.http.annotation.ThreadSafe;
import org.apache.http.nio.reactor.ListenerEndpoint;
import org.apache.http.util.Args;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;

@ThreadSafe
public class ListenerEndpointImpl
        implements ListenerEndpoint {
    private final ListenerEndpointClosedCallback callback;
    private volatile boolean completed;
    private volatile boolean closed;
    private volatile SelectionKey key;
    private volatile SocketAddress address;
    private volatile IOException exception;

    public ListenerEndpointImpl(SocketAddress address, ListenerEndpointClosedCallback callback) {
        Args.notNull(address, "Address");
        this.address = address;
        this.callback = callback;
    }

    public SocketAddress getAddress() {
        return this.address;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public IOException getException() {
        return this.exception;
    }

    public void waitFor() throws InterruptedException {
        if (this.completed) {
            return;
        }
        synchronized (this) {
            while (!this.completed) {
                wait();
            }
        }
    }

    public void completed(SocketAddress address) {
        Args.notNull(address, "Address");
        if (this.completed) {
            return;
        }
        this.completed = true;
        synchronized (this) {
            this.address = address;
            notifyAll();
        }
    }

    public void failed(IOException exception) {
        if (exception == null) {
            return;
        }
        if (this.completed) {
            return;
        }
        this.completed = true;
        synchronized (this) {
            this.exception = exception;
            notifyAll();
        }
    }

    public void cancel() {
        if (this.completed) {
            return;
        }
        this.completed = true;
        this.closed = true;
        synchronized (this) {
            notifyAll();
        }
    }

    protected void setKey(SelectionKey key) {
        this.key = key;
    }

    public boolean isClosed() {
        return (this.closed || (this.key != null && !this.key.isValid()));
    }

    public void close() {
        if (this.closed) {
            return;
        }
        this.completed = true;
        this.closed = true;
        if (this.key != null) {
            this.key.cancel();
            Channel channel = this.key.channel();
            try {
                channel.close();
            } catch (IOException ignore) {
            }
        }
        if (this.callback != null) {
            this.callback.endpointClosed(this);
        }
        synchronized (this) {
            notifyAll();
        }
    }
}


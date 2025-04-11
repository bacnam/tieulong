package org.apache.http.impl.nio.reactor;

import org.apache.http.annotation.ThreadSafe;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.nio.reactor.SessionRequest;
import org.apache.http.nio.reactor.SessionRequestCallback;
import org.apache.http.util.Args;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;

@ThreadSafe
public class SessionRequestImpl
        implements SessionRequest {
    private final SocketAddress remoteAddress;
    private final SocketAddress localAddress;
    private final Object attachment;
    private final SessionRequestCallback callback;
    private volatile boolean completed;
    private volatile SelectionKey key;
    private volatile int connectTimeout;
    private volatile IOSession session = null;
    private volatile IOException exception = null;

    public SessionRequestImpl(SocketAddress remoteAddress, SocketAddress localAddress, Object attachment, SessionRequestCallback callback) {
        Args.notNull(remoteAddress, "Remote address");
        this.remoteAddress = remoteAddress;
        this.localAddress = localAddress;
        this.attachment = attachment;
        this.callback = callback;
        this.connectTimeout = 0;
    }

    public SocketAddress getRemoteAddress() {
        return this.remoteAddress;
    }

    public SocketAddress getLocalAddress() {
        return this.localAddress;
    }

    public Object getAttachment() {
        return this.attachment;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    protected void setKey(SelectionKey key) {
        this.key = key;
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

    public IOSession getSession() {
        synchronized (this) {
            return this.session;
        }
    }

    public IOException getException() {
        synchronized (this) {
            return this.exception;
        }
    }

    public void completed(IOSession session) {
        Args.notNull(session, "Session");
        if (this.completed) {
            return;
        }
        this.completed = true;
        synchronized (this) {
            this.session = session;
            if (this.callback != null) {
                this.callback.completed(this);
            }
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
        SelectionKey key = this.key;
        if (key != null) {
            key.cancel();
            Channel channel = key.channel();
            try {
                channel.close();
            } catch (IOException ignore) {
            }
        }
        synchronized (this) {
            this.exception = exception;
            if (this.callback != null) {
                this.callback.failed(this);
            }
            notifyAll();
        }
    }

    public void timeout() {
        if (this.completed) {
            return;
        }
        this.completed = true;
        SelectionKey key = this.key;
        if (key != null) {
            key.cancel();
            Channel channel = key.channel();
            if (channel.isOpen()) {
                try {
                    channel.close();
                } catch (IOException ignore) {
                }
            }
        }
        synchronized (this) {
            if (this.callback != null) {
                this.callback.timeout(this);
            }
        }
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setConnectTimeout(int timeout) {
        if (this.connectTimeout != timeout) {
            this.connectTimeout = timeout;
            SelectionKey key = this.key;
            if (key != null) {
                key.selector().wakeup();
            }
        }
    }

    public void cancel() {
        if (this.completed) {
            return;
        }
        this.completed = true;
        SelectionKey key = this.key;
        if (key != null) {
            key.cancel();
            Channel channel = key.channel();
            if (channel.isOpen()) {
                try {
                    channel.close();
                } catch (IOException ignore) {
                }
            }
        }
        synchronized (this) {
            if (this.callback != null) {
                this.callback.cancelled(this);
            }
            notifyAll();
        }
    }
}


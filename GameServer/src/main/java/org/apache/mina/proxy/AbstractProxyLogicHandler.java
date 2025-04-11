package org.apache.mina.proxy;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.DefaultWriteFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.DefaultWriteRequest;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.proxy.filter.ProxyFilter;
import org.apache.mina.proxy.filter.ProxyHandshakeIoBuffer;
import org.apache.mina.proxy.session.ProxyIoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;

public abstract class AbstractProxyLogicHandler
        implements ProxyLogicHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProxyLogicHandler.class);

    private ProxyIoSession proxyIoSession;

    private Queue<Event> writeRequestQueue = null;

    private boolean handshakeComplete = false;

    public AbstractProxyLogicHandler(ProxyIoSession proxyIoSession) {
        this.proxyIoSession = proxyIoSession;
    }

    protected ProxyFilter getProxyFilter() {
        return this.proxyIoSession.getProxyFilter();
    }

    protected IoSession getSession() {
        return this.proxyIoSession.getSession();
    }

    public ProxyIoSession getProxyIoSession() {
        return this.proxyIoSession;
    }

    protected WriteFuture writeData(IoFilter.NextFilter nextFilter, IoBuffer data) {
        ProxyHandshakeIoBuffer writeBuffer = new ProxyHandshakeIoBuffer(data);

        LOGGER.debug("   session write: {}", writeBuffer);

        DefaultWriteFuture defaultWriteFuture = new DefaultWriteFuture(getSession());
        getProxyFilter().writeData(nextFilter, getSession(), (WriteRequest) new DefaultWriteRequest(writeBuffer, (WriteFuture) defaultWriteFuture), true);

        return (WriteFuture) defaultWriteFuture;
    }

    public boolean isHandshakeComplete() {
        synchronized (this) {
            return this.handshakeComplete;
        }
    }

    protected final void setHandshakeComplete() {
        synchronized (this) {
            this.handshakeComplete = true;
        }

        ProxyIoSession proxyIoSession = getProxyIoSession();
        proxyIoSession.getConnector().fireConnected(proxyIoSession.getSession()).awaitUninterruptibly();

        LOGGER.debug("  handshake completed");

        try {
            proxyIoSession.getEventQueue().flushPendingSessionEvents();
            flushPendingWriteRequests();
        } catch (Exception ex) {
            LOGGER.error("Unable to flush pending write requests", ex);
        }
    }

    protected synchronized void flushPendingWriteRequests() throws Exception {
        LOGGER.debug(" flushPendingWriteRequests()");

        if (this.writeRequestQueue == null) {
            return;
        }

        Event scheduledWrite;
        while ((scheduledWrite = this.writeRequestQueue.poll()) != null) {
            LOGGER.debug(" Flushing buffered write request: {}", scheduledWrite.data);

            getProxyFilter().filterWrite(scheduledWrite.nextFilter, getSession(), (WriteRequest) scheduledWrite.data);
        }

        this.writeRequestQueue = null;
    }

    public synchronized void enqueueWriteRequest(IoFilter.NextFilter nextFilter, WriteRequest writeRequest) {
        if (this.writeRequestQueue == null) {
            this.writeRequestQueue = new LinkedList<Event>();
        }

        this.writeRequestQueue.offer(new Event(nextFilter, writeRequest));
    }

    protected void closeSession(String message, Throwable t) {
        if (t != null) {
            LOGGER.error(message, t);
            this.proxyIoSession.setAuthenticationFailed(true);
        } else {
            LOGGER.error(message);
        }

        getSession().close(true);
    }

    protected void closeSession(String message) {
        closeSession(message, null);
    }

    private static final class Event {
        private final IoFilter.NextFilter nextFilter;

        private final Object data;

        Event(IoFilter.NextFilter nextFilter, Object data) {
            this.nextFilter = nextFilter;
            this.data = data;
        }

        public Object getData() {
            return this.data;
        }

        public IoFilter.NextFilter getNextFilter() {
            return this.nextFilter;
        }
    }
}


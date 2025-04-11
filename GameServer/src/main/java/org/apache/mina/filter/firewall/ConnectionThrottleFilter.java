package org.apache.mina.filter.firewall;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionThrottleFilter
        extends IoFilterAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionThrottleFilter.class);

    private static final long DEFAULT_TIME = 1000L;
    private final Map<String, Long> clients;
    private long allowedInterval;
    private Lock lock = new ReentrantLock();

    public ConnectionThrottleFilter() {
        this(1000L);
    }

    public ConnectionThrottleFilter(long allowedInterval) {
        this.allowedInterval = allowedInterval;
        this.clients = new ConcurrentHashMap<String, Long>();

        ExpiredSessionThread cleanupThread = new ExpiredSessionThread();

        cleanupThread.setDaemon(true);

        cleanupThread.start();
    }

    public void setAllowedInterval(long allowedInterval) {
        this.lock.lock();

        try {
            this.allowedInterval = allowedInterval;
        } finally {
            this.lock.unlock();
        }
    }

    protected boolean isConnectionOk(IoSession session) {
        SocketAddress remoteAddress = session.getRemoteAddress();

        if (remoteAddress instanceof InetSocketAddress) {
            InetSocketAddress addr = (InetSocketAddress) remoteAddress;
            long now = System.currentTimeMillis();

            this.lock.lock();

            try {
                if (this.clients.containsKey(addr.getAddress().getHostAddress())) {

                    LOGGER.debug("This is not a new client");
                    Long lastConnTime = this.clients.get(addr.getAddress().getHostAddress());

                    this.clients.put(addr.getAddress().getHostAddress(), Long.valueOf(now));

                    if (now - lastConnTime.longValue() < this.allowedInterval) {
                        LOGGER.warn("Session connection interval too short");
                        return false;
                    }

                    return true;
                }

                this.clients.put(addr.getAddress().getHostAddress(), Long.valueOf(now));
            } finally {
                this.lock.unlock();
            }

            return true;
        }

        return false;
    }

    public void sessionCreated(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
        if (!isConnectionOk(session)) {
            LOGGER.warn("Connections coming in too fast; closing.");
            session.close(true);
        }

        nextFilter.sessionCreated(session);
    }

    private class ExpiredSessionThread
            extends Thread {
        private ExpiredSessionThread() {
        }

        public void run() {
            try {
                Thread.sleep(ConnectionThrottleFilter.this.allowedInterval);
            } catch (InterruptedException e) {
                return;
            }

            long currentTime = System.currentTimeMillis();

            ConnectionThrottleFilter.this.lock.lock();

            try {
                Iterator<String> sessions = ConnectionThrottleFilter.this.clients.keySet().iterator();

                while (sessions.hasNext()) {
                    String session = sessions.next();
                    long creationTime = ((Long) ConnectionThrottleFilter.this.clients.get(session)).longValue();

                    if (creationTime + ConnectionThrottleFilter.this.allowedInterval < currentTime) {
                        ConnectionThrottleFilter.this.clients.remove(session);
                    }
                }
            } finally {
                ConnectionThrottleFilter.this.lock.unlock();
            }
        }
    }
}


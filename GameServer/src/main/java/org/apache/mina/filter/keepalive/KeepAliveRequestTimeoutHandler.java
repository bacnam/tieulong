package org.apache.mina.filter.keepalive;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface KeepAliveRequestTimeoutHandler {
    public static final KeepAliveRequestTimeoutHandler NOOP = new KeepAliveRequestTimeoutHandler() {
        public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
        }
    };

    public static final KeepAliveRequestTimeoutHandler LOG = new KeepAliveRequestTimeoutHandler() {
        private final Logger LOGGER = LoggerFactory.getLogger(KeepAliveFilter.class);

        public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
            this.LOGGER.warn("A keep-alive response message was not received within {} second(s).", Integer.valueOf(filter.getRequestTimeout()));
        }
    };

    public static final KeepAliveRequestTimeoutHandler EXCEPTION = new KeepAliveRequestTimeoutHandler() {
        public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
            throw new KeepAliveRequestTimeoutException("A keep-alive response message was not received within " + filter.getRequestTimeout() + " second(s).");
        }
    };

    public static final KeepAliveRequestTimeoutHandler CLOSE = new KeepAliveRequestTimeoutHandler() {
        private final Logger LOGGER = LoggerFactory.getLogger(KeepAliveFilter.class);

        public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
            this.LOGGER.warn("Closing the session because a keep-alive response message was not received within {} second(s).", Integer.valueOf(filter.getRequestTimeout()));

            session.close(true);
        }
    };

    public static final KeepAliveRequestTimeoutHandler DEAF_SPEAKER = new KeepAliveRequestTimeoutHandler() {
        public void keepAliveRequestTimedOut(KeepAliveFilter filter, IoSession session) throws Exception {
            throw new Error("Shouldn't be invoked.  Please file a bug report.");
        }
    };

    void keepAliveRequestTimedOut(KeepAliveFilter paramKeepAliveFilter, IoSession paramIoSession) throws Exception;
}


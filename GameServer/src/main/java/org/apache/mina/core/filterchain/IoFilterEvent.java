package org.apache.mina.core.filterchain;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoEvent;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoFilterEvent
        extends IoEvent {
    private static Logger LOGGER = LoggerFactory.getLogger(IoFilterEvent.class);

    private static boolean DEBUG = LOGGER.isDebugEnabled();

    private final IoFilter.NextFilter nextFilter;

    public IoFilterEvent(IoFilter.NextFilter nextFilter, IoEventType type, IoSession session, Object parameter) {
        super(type, session, parameter);

        if (nextFilter == null) {
            throw new IllegalArgumentException("nextFilter must not be null");
        }

        this.nextFilter = nextFilter;
    }

    public IoFilter.NextFilter getNextFilter() {
        return this.nextFilter;
    }

    public void fire() {
        Object parameter;
        WriteRequest writeRequest;
        Throwable throwable;
        IoSession session = getSession();
        IoFilter.NextFilter nextFilter = getNextFilter();
        IoEventType type = getType();

        if (DEBUG) {
            LOGGER.debug("Firing a {} event for session {}", type, Long.valueOf(session.getId()));
        }

        switch (type) {
            case MESSAGE_RECEIVED:
                parameter = getParameter();
                nextFilter.messageReceived(session, parameter);
                break;

            case MESSAGE_SENT:
                writeRequest = (WriteRequest) getParameter();
                nextFilter.messageSent(session, writeRequest);
                break;

            case WRITE:
                writeRequest = (WriteRequest) getParameter();
                nextFilter.filterWrite(session, writeRequest);
                break;

            case CLOSE:
                nextFilter.filterClose(session);
                break;

            case EXCEPTION_CAUGHT:
                throwable = (Throwable) getParameter();
                nextFilter.exceptionCaught(session, throwable);
                break;

            case SESSION_IDLE:
                nextFilter.sessionIdle(session, (IdleStatus) getParameter());
                break;

            case SESSION_OPENED:
                nextFilter.sessionOpened(session);
                break;

            case SESSION_CREATED:
                nextFilter.sessionCreated(session);
                break;

            case SESSION_CLOSED:
                nextFilter.sessionClosed(session);
                break;

            default:
                throw new IllegalArgumentException("Unknown event type: " + type);
        }

        if (DEBUG)
            LOGGER.debug("Event {} has been fired for session {}", type, Long.valueOf(session.getId()));
    }
}


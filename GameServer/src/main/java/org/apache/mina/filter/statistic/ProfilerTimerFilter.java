package org.apache.mina.filter.statistic;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ProfilerTimerFilter
        extends IoFilterAdapter {
    private volatile TimeUnit timeUnit;
    private TimerWorker messageReceivedTimerWorker;
    private boolean profileMessageReceived = false;
    private TimerWorker messageSentTimerWorker;
    private boolean profileMessageSent = false;
    private TimerWorker sessionCreatedTimerWorker;
    private boolean profileSessionCreated = false;
    private TimerWorker sessionOpenedTimerWorker;
    private boolean profileSessionOpened = false;
    private TimerWorker sessionIdleTimerWorker;
    private boolean profileSessionIdle = false;
    private TimerWorker sessionClosedTimerWorker;
    private boolean profileSessionClosed = false;

    public ProfilerTimerFilter() {
        this(TimeUnit.MILLISECONDS, new IoEventType[]{IoEventType.MESSAGE_RECEIVED, IoEventType.MESSAGE_SENT});
    }

    public ProfilerTimerFilter(TimeUnit timeUnit) {
        this(timeUnit, new IoEventType[]{IoEventType.MESSAGE_RECEIVED, IoEventType.MESSAGE_SENT});
    }

    public ProfilerTimerFilter(TimeUnit timeUnit, IoEventType... eventTypes) {
        this.timeUnit = timeUnit;

        setProfilers(eventTypes);
    }

    private void setProfilers(IoEventType... eventTypes) {
        for (IoEventType type : eventTypes) {
            switch (type) {
                case SECONDS:
                    this.messageReceivedTimerWorker = new TimerWorker();
                    this.profileMessageReceived = true;
                    break;

                case MICROSECONDS:
                    this.messageSentTimerWorker = new TimerWorker();
                    this.profileMessageSent = true;
                    break;

                case NANOSECONDS:
                    this.sessionCreatedTimerWorker = new TimerWorker();
                    this.profileSessionCreated = true;
                    break;

                case null:
                    this.sessionOpenedTimerWorker = new TimerWorker();
                    this.profileSessionOpened = true;
                    break;

                case null:
                    this.sessionIdleTimerWorker = new TimerWorker();
                    this.profileSessionIdle = true;
                    break;

                case null:
                    this.sessionClosedTimerWorker = new TimerWorker();
                    this.profileSessionClosed = true;
                    break;
            }
        }
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public void profile(IoEventType type) {
        switch (type) {
            case SECONDS:
                this.profileMessageReceived = true;

                if (this.messageReceivedTimerWorker == null) {
                    this.messageReceivedTimerWorker = new TimerWorker();
                }
                return;

            case MICROSECONDS:
                this.profileMessageSent = true;

                if (this.messageSentTimerWorker == null) {
                    this.messageSentTimerWorker = new TimerWorker();
                }
                return;

            case NANOSECONDS:
                this.profileSessionCreated = true;

                if (this.sessionCreatedTimerWorker == null) {
                    this.sessionCreatedTimerWorker = new TimerWorker();
                }
                return;

            case null:
                this.profileSessionOpened = true;

                if (this.sessionOpenedTimerWorker == null) {
                    this.sessionOpenedTimerWorker = new TimerWorker();
                }
                return;

            case null:
                this.profileSessionIdle = true;

                if (this.sessionIdleTimerWorker == null) {
                    this.sessionIdleTimerWorker = new TimerWorker();
                }
                return;

            case null:
                this.profileSessionClosed = true;

                if (this.sessionClosedTimerWorker == null) {
                    this.sessionClosedTimerWorker = new TimerWorker();
                }
                return;
        }
    }

    public void stopProfile(IoEventType type) {
        switch (type) {
            case SECONDS:
                this.profileMessageReceived = false;
                return;

            case MICROSECONDS:
                this.profileMessageSent = false;
                return;

            case NANOSECONDS:
                this.profileSessionCreated = false;
                return;

            case null:
                this.profileSessionOpened = false;
                return;

            case null:
                this.profileSessionIdle = false;
                return;

            case null:
                this.profileSessionClosed = false;
                return;
        }
    }

    public Set<IoEventType> getEventsToProfile() {
        Set<IoEventType> set = new HashSet<IoEventType>();

        if (this.profileMessageReceived) {
            set.add(IoEventType.MESSAGE_RECEIVED);
        }

        if (this.profileMessageSent) {
            set.add(IoEventType.MESSAGE_SENT);
        }

        if (this.profileSessionCreated) {
            set.add(IoEventType.SESSION_CREATED);
        }

        if (this.profileSessionOpened) {
            set.add(IoEventType.SESSION_OPENED);
        }

        if (this.profileSessionIdle) {
            set.add(IoEventType.SESSION_IDLE);
        }

        if (this.profileSessionClosed) {
            set.add(IoEventType.SESSION_CLOSED);
        }

        return set;
    }

    public void setEventsToProfile(IoEventType... eventTypes) {
        setProfilers(eventTypes);
    }

    public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) throws Exception {
        if (this.profileMessageReceived) {
            long start = timeNow();
            nextFilter.messageReceived(session, message);
            long end = timeNow();
            this.messageReceivedTimerWorker.addNewDuration(end - start);
        } else {
            nextFilter.messageReceived(session, message);
        }
    }

    public void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
        if (this.profileMessageSent) {
            long start = timeNow();
            nextFilter.messageSent(session, writeRequest);
            long end = timeNow();
            this.messageSentTimerWorker.addNewDuration(end - start);
        } else {
            nextFilter.messageSent(session, writeRequest);
        }
    }

    public void sessionCreated(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
        if (this.profileSessionCreated) {
            long start = timeNow();
            nextFilter.sessionCreated(session);
            long end = timeNow();
            this.sessionCreatedTimerWorker.addNewDuration(end - start);
        } else {
            nextFilter.sessionCreated(session);
        }
    }

    public void sessionOpened(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
        if (this.profileSessionOpened) {
            long start = timeNow();
            nextFilter.sessionOpened(session);
            long end = timeNow();
            this.sessionOpenedTimerWorker.addNewDuration(end - start);
        } else {
            nextFilter.sessionOpened(session);
        }
    }

    public void sessionIdle(IoFilter.NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception {
        if (this.profileSessionIdle) {
            long start = timeNow();
            nextFilter.sessionIdle(session, status);
            long end = timeNow();
            this.sessionIdleTimerWorker.addNewDuration(end - start);
        } else {
            nextFilter.sessionIdle(session, status);
        }
    }

    public void sessionClosed(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
        if (this.profileSessionClosed) {
            long start = timeNow();
            nextFilter.sessionClosed(session);
            long end = timeNow();
            this.sessionClosedTimerWorker.addNewDuration(end - start);
        } else {
            nextFilter.sessionClosed(session);
        }
    }

    public double getAverageTime(IoEventType type) {
        switch (type) {
            case SECONDS:
                if (this.profileMessageReceived) {
                    return this.messageReceivedTimerWorker.getAverage();
                }
                break;

            case MICROSECONDS:
                if (this.profileMessageSent) {
                    return this.messageSentTimerWorker.getAverage();
                }
                break;

            case NANOSECONDS:
                if (this.profileSessionCreated) {
                    return this.sessionCreatedTimerWorker.getAverage();
                }
                break;

            case null:
                if (this.profileSessionOpened) {
                    return this.sessionOpenedTimerWorker.getAverage();
                }
                break;

            case null:
                if (this.profileSessionIdle) {
                    return this.sessionIdleTimerWorker.getAverage();
                }
                break;

            case null:
                if (this.profileSessionClosed) {
                    return this.sessionClosedTimerWorker.getAverage();
                }
                break;
        }

        throw new IllegalArgumentException("You are not monitoring this event.  Please add this event first.");
    }

    public long getTotalCalls(IoEventType type) {
        switch (type) {
            case SECONDS:
                if (this.profileMessageReceived) {
                    return this.messageReceivedTimerWorker.getCallsNumber();
                }
                break;

            case MICROSECONDS:
                if (this.profileMessageSent) {
                    return this.messageSentTimerWorker.getCallsNumber();
                }
                break;

            case NANOSECONDS:
                if (this.profileSessionCreated) {
                    return this.sessionCreatedTimerWorker.getCallsNumber();
                }
                break;

            case null:
                if (this.profileSessionOpened) {
                    return this.sessionOpenedTimerWorker.getCallsNumber();
                }
                break;

            case null:
                if (this.profileSessionIdle) {
                    return this.sessionIdleTimerWorker.getCallsNumber();
                }
                break;

            case null:
                if (this.profileSessionClosed) {
                    return this.sessionClosedTimerWorker.getCallsNumber();
                }
                break;
        }

        throw new IllegalArgumentException("You are not monitoring this event.  Please add this event first.");
    }

    public long getTotalTime(IoEventType type) {
        switch (type) {
            case SECONDS:
                if (this.profileMessageReceived) {
                    return this.messageReceivedTimerWorker.getTotal();
                }
                break;

            case MICROSECONDS:
                if (this.profileMessageSent) {
                    return this.messageSentTimerWorker.getTotal();
                }
                break;

            case NANOSECONDS:
                if (this.profileSessionCreated) {
                    return this.sessionCreatedTimerWorker.getTotal();
                }
                break;

            case null:
                if (this.profileSessionOpened) {
                    return this.sessionOpenedTimerWorker.getTotal();
                }
                break;

            case null:
                if (this.profileSessionIdle) {
                    return this.sessionIdleTimerWorker.getTotal();
                }
                break;

            case null:
                if (this.profileSessionClosed) {
                    return this.sessionClosedTimerWorker.getTotal();
                }
                break;
        }

        throw new IllegalArgumentException("You are not monitoring this event.  Please add this event first.");
    }

    public long getMinimumTime(IoEventType type) {
        switch (type) {
            case SECONDS:
                if (this.profileMessageReceived) {
                    return this.messageReceivedTimerWorker.getMinimum();
                }
                break;

            case MICROSECONDS:
                if (this.profileMessageSent) {
                    return this.messageSentTimerWorker.getMinimum();
                }
                break;

            case NANOSECONDS:
                if (this.profileSessionCreated) {
                    return this.sessionCreatedTimerWorker.getMinimum();
                }
                break;

            case null:
                if (this.profileSessionOpened) {
                    return this.sessionOpenedTimerWorker.getMinimum();
                }
                break;

            case null:
                if (this.profileSessionIdle) {
                    return this.sessionIdleTimerWorker.getMinimum();
                }
                break;

            case null:
                if (this.profileSessionClosed) {
                    return this.sessionClosedTimerWorker.getMinimum();
                }
                break;
        }

        throw new IllegalArgumentException("You are not monitoring this event.  Please add this event first.");
    }

    public long getMaximumTime(IoEventType type) {
        switch (type) {
            case SECONDS:
                if (this.profileMessageReceived) {
                    return this.messageReceivedTimerWorker.getMaximum();
                }
                break;

            case MICROSECONDS:
                if (this.profileMessageSent) {
                    return this.messageSentTimerWorker.getMaximum();
                }
                break;

            case NANOSECONDS:
                if (this.profileSessionCreated) {
                    return this.sessionCreatedTimerWorker.getMaximum();
                }
                break;

            case null:
                if (this.profileSessionOpened) {
                    return this.sessionOpenedTimerWorker.getMaximum();
                }
                break;

            case null:
                if (this.profileSessionIdle) {
                    return this.sessionIdleTimerWorker.getMaximum();
                }
                break;

            case null:
                if (this.profileSessionClosed) {
                    return this.sessionClosedTimerWorker.getMaximum();
                }
                break;
        }

        throw new IllegalArgumentException("You are not monitoring this event.  Please add this event first.");
    }

    private long timeNow() {
        switch (this.timeUnit) {
            case SECONDS:
                return System.currentTimeMillis() / 1000L;

            case MICROSECONDS:
                return System.nanoTime() / 1000L;

            case NANOSECONDS:
                return System.nanoTime();
        }

        return System.currentTimeMillis();
    }

    private class TimerWorker {
        private final AtomicLong total;

        private final AtomicLong callsNumber;

        private final AtomicLong minimum;

        private final AtomicLong maximum;

        private final Object lock = new Object();

        public TimerWorker() {
            this.total = new AtomicLong();
            this.callsNumber = new AtomicLong();
            this.minimum = new AtomicLong();
            this.maximum = new AtomicLong();
        }

        public void addNewDuration(long duration) {
            this.callsNumber.incrementAndGet();
            this.total.addAndGet(duration);

            synchronized (this.lock) {

                if (duration < this.minimum.longValue()) {
                    this.minimum.set(duration);
                }

                if (duration > this.maximum.longValue()) {
                    this.maximum.set(duration);
                }
            }
        }

        public double getAverage() {
            synchronized (this.lock) {

                return (this.total.longValue() / this.callsNumber.longValue());
            }
        }

        public long getCallsNumber() {
            return this.callsNumber.longValue();
        }

        public long getTotal() {
            return this.total.longValue();
        }

        public long getMinimum() {
            return this.minimum.longValue();
        }

        public long getMaximum() {
            return this.maximum.longValue();
        }
    }
}


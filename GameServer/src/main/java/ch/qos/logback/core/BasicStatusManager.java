package ch.qos.logback.core;

import ch.qos.logback.core.helpers.CyclicBuffer;
import ch.qos.logback.core.spi.LogbackLock;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusListener;
import ch.qos.logback.core.status.StatusManager;

import java.util.ArrayList;
import java.util.List;

public class BasicStatusManager
        implements StatusManager {
    public static final int MAX_HEADER_COUNT = 150;
    public static final int TAIL_SIZE = 150;
    protected final List<Status> statusList = new ArrayList<Status>();
    protected final CyclicBuffer<Status> tailBuffer = new CyclicBuffer(150);
    protected final LogbackLock statusListLock = new LogbackLock();
    protected final List<StatusListener> statusListenerList = new ArrayList<StatusListener>();
    protected final LogbackLock statusListenerListLock = new LogbackLock();
    int count = 0;
    int level = 0;

    public void add(Status newStatus) {
        fireStatusAddEvent(newStatus);

        this.count++;
        if (newStatus.getLevel() > this.level) {
            this.level = newStatus.getLevel();
        }

        synchronized (this.statusListLock) {
            if (this.statusList.size() < 150) {
                this.statusList.add(newStatus);
            } else {
                this.tailBuffer.add(newStatus);
            }
        }
    }

    public List<Status> getCopyOfStatusList() {
        synchronized (this.statusListLock) {
            List<Status> tList = new ArrayList<Status>(this.statusList);
            tList.addAll(this.tailBuffer.asList());
            return tList;
        }
    }

    private void fireStatusAddEvent(Status status) {
        synchronized (this.statusListenerListLock) {
            for (StatusListener sl : this.statusListenerList) {
                sl.addStatusEvent(status);
            }
        }
    }

    public void clear() {
        synchronized (this.statusListLock) {
            this.count = 0;
            this.statusList.clear();
            this.tailBuffer.clear();
        }
    }

    public int getLevel() {
        return this.level;
    }

    public int getCount() {
        return this.count;
    }

    public void add(StatusListener listener) {
        synchronized (this.statusListenerListLock) {
            if (listener instanceof ch.qos.logback.core.status.OnConsoleStatusListener) {
                boolean alreadyPresent = checkForPresence(this.statusListenerList, listener.getClass());
                if (alreadyPresent)
                    return;
            }
            this.statusListenerList.add(listener);
        }
    }

    private boolean checkForPresence(List<StatusListener> statusListenerList, Class<?> aClass) {
        for (StatusListener e : statusListenerList) {
            if (e.getClass() == aClass)
                return true;
        }
        return false;
    }

    public void remove(StatusListener listener) {
        synchronized (this.statusListenerListLock) {
            this.statusListenerList.remove(listener);
        }
    }

    public List<StatusListener> getCopyOfStatusListenerList() {
        synchronized (this.statusListenerListLock) {
            return new ArrayList<StatusListener>(this.statusListenerList);
        }
    }
}


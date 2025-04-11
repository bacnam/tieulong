package ch.qos.logback.core.status;

import java.util.List;

public interface StatusManager {
    void add(Status paramStatus);

    List<Status> getCopyOfStatusList();

    int getCount();

    void add(StatusListener paramStatusListener);

    void remove(StatusListener paramStatusListener);

    void clear();

    List<StatusListener> getCopyOfStatusListenerList();
}


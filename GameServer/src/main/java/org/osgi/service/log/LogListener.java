package org.osgi.service.log;

import java.util.EventListener;

public interface LogListener extends EventListener {
    void logged(LogEntry paramLogEntry);
}


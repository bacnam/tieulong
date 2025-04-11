package com.jolbox.bonecp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloseThreadMonitor
        implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(CloseThreadMonitor.class);
    private ConnectionHandle connectionHandle;
    private String stackTrace;
    private Thread threadToMonitor;
    private long closeConnectionWatchTimeout;

    public CloseThreadMonitor(Thread threadToMonitor, ConnectionHandle connectionHandle, String stackTrace, long closeConnectionWatchTimeout) {
        this.connectionHandle = connectionHandle;
        this.stackTrace = stackTrace;
        this.threadToMonitor = threadToMonitor;
        this.closeConnectionWatchTimeout = closeConnectionWatchTimeout;
    }

    public void run() {
        try {
            this.connectionHandle.setThreadWatch(Thread.currentThread());

            this.threadToMonitor.join(this.closeConnectionWatchTimeout);
            if (!this.connectionHandle.isClosed() && this.threadToMonitor.equals(this.connectionHandle.getThreadUsingConnection())) {

                logger.error(this.stackTrace);
            }
        } catch (Exception e) {

            if (this.connectionHandle != null)
                this.connectionHandle.setThreadWatch(null);
        }
    }
}


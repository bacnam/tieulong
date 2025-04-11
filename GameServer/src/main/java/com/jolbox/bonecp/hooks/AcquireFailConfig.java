package com.jolbox.bonecp.hooks;

import java.util.concurrent.atomic.AtomicInteger;

public class AcquireFailConfig {
    private long acquireRetryDelayInMs;
    private AtomicInteger acquireRetryAttempts = new AtomicInteger();

    private String logMessage = "";

    private Object debugHandle;

    @Deprecated
    public long getAcquireRetryDelay() {
        return getAcquireRetryDelayInMs();
    }

    @Deprecated
    public void setAcquireRetryDelay(long acquireRetryDelayInMs) {
        setAcquireRetryDelayInMs(acquireRetryDelayInMs);
    }

    public long getAcquireRetryDelayInMs() {
        return this.acquireRetryDelayInMs;
    }

    public void setAcquireRetryDelayInMs(long acquireRetryDelayInMs) {
        this.acquireRetryDelayInMs = acquireRetryDelayInMs;
    }

    public AtomicInteger getAcquireRetryAttempts() {
        return this.acquireRetryAttempts;
    }

    public void setAcquireRetryAttempts(AtomicInteger acquireRetryAttempts) {
        this.acquireRetryAttempts = acquireRetryAttempts;
    }

    public String getLogMessage() {
        return this.logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    public Object getDebugHandle() {
        return this.debugHandle;
    }

    public void setDebugHandle(Object debugHandle) {
        this.debugHandle = debugHandle;
    }
}


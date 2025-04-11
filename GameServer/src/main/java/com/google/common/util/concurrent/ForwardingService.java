package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.collect.ForwardingObject;

@Beta
public abstract class ForwardingService
        extends ForwardingObject
        implements Service {
    public ListenableFuture<Service.State> start() {
        return delegate().start();
    }

    public Service.State state() {
        return delegate().state();
    }

    public ListenableFuture<Service.State> stop() {
        return delegate().stop();
    }

    public Service.State startAndWait() {
        return delegate().startAndWait();
    }

    public Service.State stopAndWait() {
        return delegate().stopAndWait();
    }

    public boolean isRunning() {
        return delegate().isRunning();
    }

    protected Service.State standardStartAndWait() {
        return Futures.<Service.State>getUnchecked(start());
    }

    protected Service.State standardStopAndWait() {
        return Futures.<Service.State>getUnchecked(stop());
    }

    protected abstract Service delegate();
}


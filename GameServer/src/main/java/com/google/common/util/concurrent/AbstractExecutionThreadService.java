package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Throwables;

import java.util.concurrent.Executor;

@Beta
public abstract class AbstractExecutionThreadService implements Service {
    private final Service delegate = new AbstractService() {
        @Override
        protected void doStart() {
            AbstractExecutionThreadService.this.executor().execute(new Runnable() {
                public void run() {
                    try {
                        AbstractExecutionThreadService.this.startUp();
                        notifyStarted();  // ✅ sửa lỗi

                        if (isRunning()) { // ✅ sửa lỗi
                            try {
                                AbstractExecutionThreadService.this.run();
                            } catch (Throwable t) {
                                try {
                                    AbstractExecutionThreadService.this.shutDown();
                                } catch (Exception ignored) {}
                                throw t;
                            }
                        }

                        AbstractExecutionThreadService.this.shutDown();
                        notifyStopped();  // ✅ sửa lỗi
                    } catch (Throwable t) {
                        notifyFailed(t);  // ✅ sửa lỗi
                        throw Throwables.propagate(t);
                    }
                }
            });
        }

        @Override
        protected void doStop() {
            AbstractExecutionThreadService.this.triggerShutdown();
        }
    };

    protected void startUp() throws Exception {
    }

    protected abstract void run() throws Exception;

    protected void shutDown() throws Exception {
    }

    protected void triggerShutdown() {
    }

    protected Executor executor() {
        return new Executor() {
            public void execute(Runnable command) {
                (new Thread(command, AbstractExecutionThreadService.this.getServiceName())).start();
            }
        };
    }

    public String toString() {
        return getServiceName() + " [" + state() + "]";
    }

    public final ListenableFuture<Service.State> start() {
        return this.delegate.start();
    }

    public final Service.State startAndWait() {
        return this.delegate.startAndWait();
    }

    public final boolean isRunning() {
        return this.delegate.isRunning();
    }

    public final Service.State state() {
        return this.delegate.state();
    }

    public final ListenableFuture<Service.State> stop() {
        return this.delegate.stop();
    }

    public final Service.State stopAndWait() {
        return this.delegate.stopAndWait();
    }

    protected String getServiceName() {
        return getClass().getSimpleName();
    }
}


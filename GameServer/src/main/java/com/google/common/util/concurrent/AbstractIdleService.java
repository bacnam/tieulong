package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Throwables;

import java.util.concurrent.Executor;

@Beta
public abstract class AbstractIdleService implements Service {
    private final Service delegate = new AbstractService() {
        @Override
        protected final void doStart() {
            AbstractIdleService.this.executor(Service.State.STARTING).execute(new Runnable() {
                public void run() {
                    try {
                        AbstractIdleService.this.startUp();
                        notifyStarted();  // ✅ gọi trực tiếp
                    } catch (Throwable t) {
                        notifyFailed(t);  // ✅ gọi trực tiếp
                        throw Throwables.propagate(t);
                    }
                }
            });
        }

        @Override
        protected final void doStop() {
            AbstractIdleService.this.executor(Service.State.STOPPING).execute(new Runnable() {
                public void run() {
                    try {
                        AbstractIdleService.this.shutDown();
                        notifyStopped();  // ✅ gọi trực tiếp
                    } catch (Throwable t) {
                        notifyFailed(t);  // ✅ gọi trực tiếp
                        throw Throwables.propagate(t);
                    }
                }
            });
        }
    };

    protected Executor executor(final Service.State state) {
        return new Executor() {
            public void execute(Runnable command) {
                (new Thread(command, AbstractIdleService.this.getServiceName() + " " + state)).start();
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

    private String getServiceName() {
        return getClass().getSimpleName();
    }

    protected abstract void startUp() throws Exception;

    protected abstract void shutDown() throws Exception;
}


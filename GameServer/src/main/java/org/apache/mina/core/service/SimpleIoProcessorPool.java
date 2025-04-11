package org.apache.mina.core.service;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.session.AbstractIoSession;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.write.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.nio.channels.spi.SelectorProvider;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SimpleIoProcessorPool<S extends AbstractIoSession>
        implements IoProcessor<S> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleIoProcessorPool.class);

    private static final int DEFAULT_SIZE = Runtime.getRuntime().availableProcessors() + 1;

    private static final AttributeKey PROCESSOR = new AttributeKey(SimpleIoProcessorPool.class, "processor");

    private final IoProcessor<S>[] pool;

    private final Executor executor;

    private final boolean createdExecutor;

    private final Object disposalLock = new Object();

    private volatile boolean disposing;

    private volatile boolean disposed;

    public SimpleIoProcessorPool(Class<? extends IoProcessor<S>> processorType) {
        this(processorType, null, DEFAULT_SIZE, null);
    }

    public SimpleIoProcessorPool(Class<? extends IoProcessor<S>> processorType, int size) {
        this(processorType, null, size, null);
    }

    public SimpleIoProcessorPool(Class<? extends IoProcessor<S>> processorType, int size, SelectorProvider selectorProvider) {
        this(processorType, null, size, selectorProvider);
    }

    public SimpleIoProcessorPool(Class<? extends IoProcessor<S>> processorType, Executor executor) {
        this(processorType, executor, DEFAULT_SIZE, null);
    }

    public SimpleIoProcessorPool(Class<? extends IoProcessor<S>> processorType, Executor executor, int size, SelectorProvider selectorProvider) {
        if (processorType == null) {
            throw new IllegalArgumentException("processorType");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("size: " + size + " (expected: positive integer)");
        }

        this.createdExecutor = (executor == null);

        if (this.createdExecutor) {
            this.executor = Executors.newCachedThreadPool();

            ((ThreadPoolExecutor) this.executor).setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        } else {
            this.executor = executor;
        }

        this.pool = (IoProcessor<S>[]) new IoProcessor[size];

        boolean success = false;
        Constructor<? extends IoProcessor<S>> processorConstructor = null;
        boolean usesExecutorArg = true;

        try {
            try {
                try {
                    processorConstructor = processorType.getConstructor(new Class[]{ExecutorService.class});
                    this.pool[0] = processorConstructor.newInstance(new Object[]{this.executor});
                } catch (NoSuchMethodException e1) {

                    try {
                        if (selectorProvider == null) {
                            processorConstructor = processorType.getConstructor(new Class[]{Executor.class});
                            this.pool[0] = processorConstructor.newInstance(new Object[]{this.executor});
                        } else {
                            processorConstructor = processorType.getConstructor(new Class[]{Executor.class, SelectorProvider.class});
                            this.pool[0] = processorConstructor.newInstance(new Object[]{this.executor, selectorProvider});
                        }
                    } catch (NoSuchMethodException e2) {

                        try {
                            processorConstructor = processorType.getConstructor(new Class[0]);
                            usesExecutorArg = false;
                            this.pool[0] = processorConstructor.newInstance(new Object[0]);
                        } catch (NoSuchMethodException e3) {
                        }
                    }

                }

            } catch (RuntimeException re) {
                LOGGER.error("Cannot create an IoProcessor :{}", re.getMessage());
                throw re;
            } catch (Exception e) {
                String msg = "Failed to create a new instance of " + processorType.getName() + ":" + e.getMessage();
                LOGGER.error(msg, e);
                throw new RuntimeIoException(msg, e);
            }

            if (processorConstructor == null) {

                String msg = String.valueOf(processorType) + " must have a public constructor with one " + ExecutorService.class.getSimpleName() + " parameter, a public constructor with one " + Executor.class.getSimpleName() + " parameter or a public default constructor.";

                LOGGER.error(msg);
                throw new IllegalArgumentException(msg);
            }

            for (int i = 1; i < this.pool.length; i++) {
                try {
                    if (usesExecutorArg) {
                        if (selectorProvider == null) {
                            this.pool[i] = processorConstructor.newInstance(new Object[]{this.executor});
                        } else {
                            this.pool[i] = processorConstructor.newInstance(new Object[]{this.executor, selectorProvider});
                        }
                    } else {
                        this.pool[i] = processorConstructor.newInstance(new Object[0]);
                    }
                } catch (Exception e) {
                }
            }

            success = true;
        } finally {
            if (!success) {
                dispose();
            }
        }
    }

    public final void add(S session) {
        getProcessor(session).add(session);
    }

    public final void flush(S session) {
        getProcessor(session).flush(session);
    }

    public final void write(S session, WriteRequest writeRequest) {
        getProcessor(session).write(session, writeRequest);
    }

    public final void remove(S session) {
        getProcessor(session).remove(session);
    }

    public final void updateTrafficControl(S session) {
        getProcessor(session).updateTrafficControl(session);
    }

    public boolean isDisposed() {
        return this.disposed;
    }

    public boolean isDisposing() {
        return this.disposing;
    }

    public final void dispose() {
        if (this.disposed) {
            return;
        }

        synchronized (this.disposalLock) {
            if (!this.disposing) {
                this.disposing = true;

                for (IoProcessor<S> ioProcessor : this.pool) {
                    if (ioProcessor != null) {

                        if (!ioProcessor.isDisposing())

                            try {

                                ioProcessor.dispose();
                            } catch (Exception e) {
                                LOGGER.warn("Failed to dispose the {} IoProcessor.", ioProcessor.getClass().getSimpleName(), e);
                            }
                    }
                }
                if (this.createdExecutor) {
                    ((ExecutorService) this.executor).shutdown();
                }
            }

            Arrays.fill((Object[]) this.pool, (Object) null);
            this.disposed = true;
        }
    }

    private IoProcessor<S> getProcessor(S session) {
        IoProcessor<S> processor = (IoProcessor<S>) session.getAttribute(PROCESSOR);

        if (processor == null) {
            if (this.disposed || this.disposing) {
                throw new IllegalStateException("A disposed processor cannot be accessed.");
            }

            processor = this.pool[Math.abs((int) session.getId()) % this.pool.length];

            if (processor == null) {
                throw new IllegalStateException("A disposed processor cannot be accessed.");
            }

            session.setAttributeIfAbsent(PROCESSOR, processor);
        }

        return processor;
    }
}


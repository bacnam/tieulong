package org.apache.mina.core.future;

import org.apache.mina.core.session.IoSession;

import java.util.concurrent.TimeUnit;

public interface IoFuture {
    IoSession getSession();

    IoFuture await() throws InterruptedException;

    boolean await(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException;

    boolean await(long paramLong) throws InterruptedException;

    IoFuture awaitUninterruptibly();

    boolean awaitUninterruptibly(long paramLong, TimeUnit paramTimeUnit);

    boolean awaitUninterruptibly(long paramLong);

    @Deprecated
    void join();

    @Deprecated
    boolean join(long paramLong);

    boolean isDone();

    IoFuture addListener(IoFutureListener<?> paramIoFutureListener);

    IoFuture removeListener(IoFutureListener<?> paramIoFutureListener);
}


package org.apache.mina.core.future;

public interface CloseFuture extends IoFuture {
    boolean isClosed();

    void setClosed();

    CloseFuture await() throws InterruptedException;

    CloseFuture awaitUninterruptibly();

    CloseFuture addListener(IoFutureListener<?> paramIoFutureListener);

    CloseFuture removeListener(IoFutureListener<?> paramIoFutureListener);
}


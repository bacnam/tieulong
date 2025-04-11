package org.apache.mina.core.future;

public interface ReadFuture extends IoFuture {
    Object getMessage();

    boolean isRead();

    void setRead(Object paramObject);

    boolean isClosed();

    Throwable getException();

    void setException(Throwable paramThrowable);

    void setClosed();

    ReadFuture await() throws InterruptedException;

    ReadFuture awaitUninterruptibly();

    ReadFuture addListener(IoFutureListener<?> paramIoFutureListener);

    ReadFuture removeListener(IoFutureListener<?> paramIoFutureListener);
}


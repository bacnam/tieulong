package org.apache.mina.core.future;

import org.apache.mina.core.session.IoSession;

public interface ConnectFuture extends IoFuture {
    IoSession getSession();

    void setSession(IoSession paramIoSession);

    Throwable getException();

    void setException(Throwable paramThrowable);

    boolean isConnected();

    boolean isCanceled();

    void cancel();

    ConnectFuture await() throws InterruptedException;

    ConnectFuture awaitUninterruptibly();

    ConnectFuture addListener(IoFutureListener<?> paramIoFutureListener);

    ConnectFuture removeListener(IoFutureListener<?> paramIoFutureListener);
}


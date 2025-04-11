package org.apache.mina.core.future;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.session.IoSession;

public class DefaultConnectFuture
        extends DefaultIoFuture
        implements ConnectFuture {
    private static final Object CANCELED = new Object();

    public DefaultConnectFuture() {
        super(null);
    }

    public static ConnectFuture newFailedFuture(Throwable exception) {
        DefaultConnectFuture failedFuture = new DefaultConnectFuture();
        failedFuture.setException(exception);
        return failedFuture;
    }

    public IoSession getSession() {
        Object v = getValue();
        if (v instanceof RuntimeException)
            throw (RuntimeException) v;
        if (v instanceof Error)
            throw (Error) v;
        if (v instanceof Throwable)
            throw (RuntimeIoException) (new RuntimeIoException("Failed to get the session.")).initCause((Throwable) v);
        if (v instanceof IoSession) {
            return (IoSession) v;
        }
        return null;
    }

    public void setSession(IoSession session) {
        if (session == null) {
            throw new IllegalArgumentException("session");
        }
        setValue(session);
    }

    public Throwable getException() {
        Object v = getValue();
        if (v instanceof Throwable) {
            return (Throwable) v;
        }
        return null;
    }

    public void setException(Throwable exception) {
        if (exception == null) {
            throw new IllegalArgumentException("exception");
        }
        setValue(exception);
    }

    public boolean isConnected() {
        return getValue() instanceof IoSession;
    }

    public boolean isCanceled() {
        return (getValue() == CANCELED);
    }

    public void cancel() {
        setValue(CANCELED);
    }

    public ConnectFuture await() throws InterruptedException {
        return (ConnectFuture) super.await();
    }

    public ConnectFuture awaitUninterruptibly() {
        return (ConnectFuture) super.awaitUninterruptibly();
    }

    public ConnectFuture addListener(IoFutureListener<?> listener) {
        return (ConnectFuture) super.addListener(listener);
    }

    public ConnectFuture removeListener(IoFutureListener<?> listener) {
        return (ConnectFuture) super.removeListener(listener);
    }
}


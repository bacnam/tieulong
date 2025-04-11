package org.apache.mina.core.future;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.session.IoSession;

public class DefaultReadFuture
        extends DefaultIoFuture
        implements ReadFuture {
    private static final Object CLOSED = new Object();

    public DefaultReadFuture(IoSession session) {
        super(session);
    }

    public Object getMessage() {
        if (isDone()) {
            Object v = getValue();
            if (v == CLOSED) {
                return null;
            }

            if (v instanceof ExceptionHolder) {
                v = ((ExceptionHolder) v).exception;
                if (v instanceof RuntimeException) {
                    throw (RuntimeException) v;
                }
                if (v instanceof Error) {
                    throw (Error) v;
                }
                if (v instanceof java.io.IOException || v instanceof Exception) {
                    throw new RuntimeIoException((Exception) v);
                }
            }

            return v;
        }

        return null;
    }

    public boolean isRead() {
        if (isDone()) {
            Object v = getValue();
            return (v != CLOSED && !(v instanceof ExceptionHolder));
        }
        return false;
    }

    public void setRead(Object message) {
        if (message == null) {
            throw new IllegalArgumentException("message");
        }
        setValue(message);
    }

    public boolean isClosed() {
        if (isDone()) {
            return (getValue() == CLOSED);
        }
        return false;
    }

    public Throwable getException() {
        if (isDone()) {
            Object v = getValue();
            if (v instanceof ExceptionHolder) {
                return ((ExceptionHolder) v).exception;
            }
        }
        return null;
    }

    public void setException(Throwable exception) {
        if (exception == null) {
            throw new IllegalArgumentException("exception");
        }

        setValue(new ExceptionHolder(exception));
    }

    public void setClosed() {
        setValue(CLOSED);
    }

    public ReadFuture await() throws InterruptedException {
        return (ReadFuture) super.await();
    }

    public ReadFuture awaitUninterruptibly() {
        return (ReadFuture) super.awaitUninterruptibly();
    }

    public ReadFuture addListener(IoFutureListener<?> listener) {
        return (ReadFuture) super.addListener(listener);
    }

    public ReadFuture removeListener(IoFutureListener<?> listener) {
        return (ReadFuture) super.removeListener(listener);
    }

    private static class ExceptionHolder {
        private final Throwable exception;

        private ExceptionHolder(Throwable exception) {
            this.exception = exception;
        }
    }
}


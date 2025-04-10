package org.apache.mina.core.future;

public interface ReadFuture extends IoFuture {
  Object getMessage();

  boolean isRead();

  boolean isClosed();

  Throwable getException();

  void setRead(Object paramObject);

  void setClosed();

  void setException(Throwable paramThrowable);

  ReadFuture await() throws InterruptedException;

  ReadFuture awaitUninterruptibly();

  ReadFuture addListener(IoFutureListener<?> paramIoFutureListener);

  ReadFuture removeListener(IoFutureListener<?> paramIoFutureListener);
}


package org.apache.mina.core.future;

public interface WriteFuture extends IoFuture {
  boolean isWritten();

  Throwable getException();

  void setWritten();

  void setException(Throwable paramThrowable);

  WriteFuture await() throws InterruptedException;

  WriteFuture awaitUninterruptibly();

  WriteFuture addListener(IoFutureListener<?> paramIoFutureListener);

  WriteFuture removeListener(IoFutureListener<?> paramIoFutureListener);
}


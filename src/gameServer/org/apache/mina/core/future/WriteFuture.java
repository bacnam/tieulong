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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/future/WriteFuture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
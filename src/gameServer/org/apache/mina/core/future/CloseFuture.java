package org.apache.mina.core.future;

public interface CloseFuture extends IoFuture {
  boolean isClosed();
  
  void setClosed();
  
  CloseFuture await() throws InterruptedException;
  
  CloseFuture awaitUninterruptibly();
  
  CloseFuture addListener(IoFutureListener<?> paramIoFutureListener);
  
  CloseFuture removeListener(IoFutureListener<?> paramIoFutureListener);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/future/CloseFuture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
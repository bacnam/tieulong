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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/future/ReadFuture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
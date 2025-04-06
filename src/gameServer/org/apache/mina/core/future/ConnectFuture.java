package org.apache.mina.core.future;

import org.apache.mina.core.session.IoSession;

public interface ConnectFuture extends IoFuture {
  IoSession getSession();
  
  Throwable getException();
  
  boolean isConnected();
  
  boolean isCanceled();
  
  void setSession(IoSession paramIoSession);
  
  void setException(Throwable paramThrowable);
  
  void cancel();
  
  ConnectFuture await() throws InterruptedException;
  
  ConnectFuture awaitUninterruptibly();
  
  ConnectFuture addListener(IoFutureListener<?> paramIoFutureListener);
  
  ConnectFuture removeListener(IoFutureListener<?> paramIoFutureListener);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/future/ConnectFuture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
package org.apache.mina.core.future;

import java.util.concurrent.TimeUnit;
import org.apache.mina.core.session.IoSession;

public interface IoFuture {
  IoSession getSession();
  
  IoFuture await() throws InterruptedException;
  
  boolean await(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException;
  
  boolean await(long paramLong) throws InterruptedException;
  
  IoFuture awaitUninterruptibly();
  
  boolean awaitUninterruptibly(long paramLong, TimeUnit paramTimeUnit);
  
  boolean awaitUninterruptibly(long paramLong);
  
  @Deprecated
  void join();
  
  @Deprecated
  boolean join(long paramLong);
  
  boolean isDone();
  
  IoFuture addListener(IoFutureListener<?> paramIoFutureListener);
  
  IoFuture removeListener(IoFutureListener<?> paramIoFutureListener);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/future/IoFuture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
package org.apache.http.impl.nio.client;

import org.apache.http.nio.protocol.HttpAsyncRequestExecutionHandler;

@Deprecated
interface ResultCallback<T> {
  void completed(T paramT, HttpAsyncRequestExecutionHandler<T> paramHttpAsyncRequestExecutionHandler);
  
  void failed(Exception paramException, HttpAsyncRequestExecutionHandler<T> paramHttpAsyncRequestExecutionHandler);
  
  void cancelled(HttpAsyncRequestExecutionHandler<T> paramHttpAsyncRequestExecutionHandler);
  
  boolean isDone();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/ResultCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
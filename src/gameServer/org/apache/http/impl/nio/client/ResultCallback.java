package org.apache.http.impl.nio.client;

import org.apache.http.nio.protocol.HttpAsyncRequestExecutionHandler;

@Deprecated
interface ResultCallback<T> {
  void completed(T paramT, HttpAsyncRequestExecutionHandler<T> paramHttpAsyncRequestExecutionHandler);

  void failed(Exception paramException, HttpAsyncRequestExecutionHandler<T> paramHttpAsyncRequestExecutionHandler);

  void cancelled(HttpAsyncRequestExecutionHandler<T> paramHttpAsyncRequestExecutionHandler);

  boolean isDone();
}


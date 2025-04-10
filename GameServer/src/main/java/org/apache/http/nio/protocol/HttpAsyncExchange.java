package org.apache.http.nio.protocol;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.Cancellable;

public interface HttpAsyncExchange {
  HttpRequest getRequest();

  HttpResponse getResponse();

  void submitResponse();

  void submitResponse(HttpAsyncResponseProducer paramHttpAsyncResponseProducer);

  boolean isCompleted();

  void setCallback(Cancellable paramCancellable);

  void setTimeout(int paramInt);

  int getTimeout();
}


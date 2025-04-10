package org.apache.http.nio.protocol;

import java.io.Closeable;
import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.Cancellable;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.IOControl;
import org.apache.http.protocol.HttpContext;

public interface HttpAsyncResponseConsumer<T> extends Closeable, Cancellable {
  void responseReceived(HttpResponse paramHttpResponse) throws IOException, HttpException;

  void consumeContent(ContentDecoder paramContentDecoder, IOControl paramIOControl) throws IOException;

  void responseCompleted(HttpContext paramHttpContext);

  void failed(Exception paramException);

  Exception getException();

  T getResult();

  boolean isDone();
}


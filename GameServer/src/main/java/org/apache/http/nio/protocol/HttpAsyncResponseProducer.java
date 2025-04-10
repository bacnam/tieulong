package org.apache.http.nio.protocol;

import java.io.Closeable;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.IOControl;
import org.apache.http.protocol.HttpContext;

public interface HttpAsyncResponseProducer extends Closeable {
  HttpResponse generateResponse();

  void produceContent(ContentEncoder paramContentEncoder, IOControl paramIOControl) throws IOException;

  void responseCompleted(HttpContext paramHttpContext);

  void failed(Exception paramException);
}


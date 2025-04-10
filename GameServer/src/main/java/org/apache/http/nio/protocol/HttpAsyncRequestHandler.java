package org.apache.http.nio.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;

public interface HttpAsyncRequestHandler<T> {
  HttpAsyncRequestConsumer<T> processRequest(HttpRequest paramHttpRequest, HttpContext paramHttpContext) throws HttpException, IOException;

  void handle(T paramT, HttpAsyncExchange paramHttpAsyncExchange, HttpContext paramHttpContext) throws HttpException, IOException;
}


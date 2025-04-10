package org.apache.http.nio.protocol;

import org.apache.http.HttpRequest;

public interface HttpAsyncRequestHandlerMapper {
  HttpAsyncRequestHandler<?> lookup(HttpRequest paramHttpRequest);
}


package org.apache.http.nio.protocol;

import java.io.IOException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

@Deprecated
public interface HttpRequestExecutionHandler {
  void initalizeContext(HttpContext paramHttpContext, Object paramObject);

  HttpRequest submitRequest(HttpContext paramHttpContext);

  void handleResponse(HttpResponse paramHttpResponse, HttpContext paramHttpContext) throws IOException;

  void finalizeContext(HttpContext paramHttpContext);
}


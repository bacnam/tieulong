package org.apache.http;

import java.io.IOException;
import org.apache.http.protocol.HttpContext;

public interface HttpResponseInterceptor {
  void process(HttpResponse paramHttpResponse, HttpContext paramHttpContext) throws HttpException, IOException;
}


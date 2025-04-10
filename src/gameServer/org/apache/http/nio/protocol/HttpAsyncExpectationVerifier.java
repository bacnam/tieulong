package org.apache.http.nio.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.protocol.HttpContext;

public interface HttpAsyncExpectationVerifier {
  void verify(HttpAsyncExchange paramHttpAsyncExchange, HttpContext paramHttpContext) throws HttpException, IOException;
}


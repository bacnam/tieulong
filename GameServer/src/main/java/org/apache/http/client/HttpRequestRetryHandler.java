package org.apache.http.client;

import java.io.IOException;
import org.apache.http.protocol.HttpContext;

public interface HttpRequestRetryHandler {
  boolean retryRequest(IOException paramIOException, int paramInt, HttpContext paramHttpContext);
}


package org.apache.http.client;

import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public interface HttpRequestRetryHandler {
    boolean retryRequest(IOException paramIOException, int paramInt, HttpContext paramHttpContext);
}


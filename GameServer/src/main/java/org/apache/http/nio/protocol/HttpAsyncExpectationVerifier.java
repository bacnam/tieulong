package org.apache.http.nio.protocol;

import org.apache.http.HttpException;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public interface HttpAsyncExpectationVerifier {
    void verify(HttpAsyncExchange paramHttpAsyncExchange, HttpContext paramHttpContext) throws HttpException, IOException;
}


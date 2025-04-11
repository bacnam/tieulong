package org.apache.http;

import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public interface HttpRequestInterceptor {
    void process(HttpRequest paramHttpRequest, HttpContext paramHttpContext) throws HttpException, IOException;
}


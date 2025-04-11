package org.apache.http;

import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public interface HttpResponseInterceptor {
    void process(HttpResponse paramHttpResponse, HttpContext paramHttpContext) throws HttpException, IOException;
}


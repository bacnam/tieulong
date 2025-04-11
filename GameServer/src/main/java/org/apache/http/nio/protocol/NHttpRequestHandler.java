package org.apache.http.nio.protocol;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.nio.entity.ConsumingNHttpEntity;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

@Deprecated
public interface NHttpRequestHandler {
    ConsumingNHttpEntity entityRequest(HttpEntityEnclosingRequest paramHttpEntityEnclosingRequest, HttpContext paramHttpContext) throws HttpException, IOException;

    void handle(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse, NHttpResponseTrigger paramNHttpResponseTrigger, HttpContext paramHttpContext) throws HttpException, IOException;
}


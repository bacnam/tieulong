package org.apache.http.nio.protocol;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.nio.entity.ConsumingNHttpEntity;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

@Deprecated
public interface NHttpRequestExecutionHandler {
    void initalizeContext(HttpContext paramHttpContext, Object paramObject);

    HttpRequest submitRequest(HttpContext paramHttpContext);

    ConsumingNHttpEntity responseEntity(HttpResponse paramHttpResponse, HttpContext paramHttpContext) throws IOException;

    void handleResponse(HttpResponse paramHttpResponse, HttpContext paramHttpContext) throws IOException;

    void finalizeContext(HttpContext paramHttpContext);
}


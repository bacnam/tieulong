package org.apache.http.nio.protocol;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

@Deprecated
@Immutable
public abstract class SimpleNHttpRequestHandler
        implements NHttpRequestHandler {
    public final void handle(HttpRequest request, HttpResponse response, NHttpResponseTrigger trigger, HttpContext context) throws HttpException, IOException {
        handle(request, response, context);
        trigger.submitResponse(response);
    }

    public abstract void handle(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse, HttpContext paramHttpContext) throws HttpException, IOException;
}


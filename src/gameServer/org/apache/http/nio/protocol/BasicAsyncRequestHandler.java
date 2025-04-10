package org.apache.http.nio.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.Args;

@Immutable
public class BasicAsyncRequestHandler
implements HttpAsyncRequestHandler<HttpRequest>
{
private final HttpRequestHandler handler;

public BasicAsyncRequestHandler(HttpRequestHandler handler) {
Args.notNull(handler, "Request handler");
this.handler = handler;
}

public HttpAsyncRequestConsumer<HttpRequest> processRequest(HttpRequest request, HttpContext context) {
return new BasicAsyncRequestConsumer();
}

public void handle(HttpRequest request, HttpAsyncExchange httpexchange, HttpContext context) throws HttpException, IOException {
this.handler.handle(request, httpexchange.getResponse(), context);
httpexchange.submitResponse();
}
}


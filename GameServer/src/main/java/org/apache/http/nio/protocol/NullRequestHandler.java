package org.apache.http.nio.protocol;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.protocol.HttpContext;

class NullRequestHandler
        implements HttpAsyncRequestHandler<Object> {
    public HttpAsyncRequestConsumer<Object> processRequest(HttpRequest request, HttpContext context) {
        return new NullRequestConsumer();
    }

    public void handle(Object obj, HttpAsyncExchange httpexchange, HttpContext context) {
        HttpResponse response = httpexchange.getResponse();
        response.setStatusCode(501);
        httpexchange.submitResponse(new ErrorResponseProducer(response, (HttpEntity) new NStringEntity("Service not implemented", ContentType.TEXT_PLAIN), true));
    }
}


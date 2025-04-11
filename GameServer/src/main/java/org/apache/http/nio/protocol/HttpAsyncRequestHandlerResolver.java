package org.apache.http.nio.protocol;

@Deprecated
public interface HttpAsyncRequestHandlerResolver {
    HttpAsyncRequestHandler<?> lookup(String paramString);
}


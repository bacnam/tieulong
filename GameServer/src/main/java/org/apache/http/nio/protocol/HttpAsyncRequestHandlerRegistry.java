package org.apache.http.nio.protocol;

import org.apache.http.annotation.ThreadSafe;
import org.apache.http.protocol.UriPatternMatcher;

import java.util.Map;

@Deprecated
@ThreadSafe
public class HttpAsyncRequestHandlerRegistry
        implements HttpAsyncRequestHandlerResolver {
    private final UriPatternMatcher<HttpAsyncRequestHandler<?>> matcher = new UriPatternMatcher();

    public void register(String pattern, HttpAsyncRequestHandler<?> handler) {
        this.matcher.register(pattern, handler);
    }

    public void unregister(String pattern) {
        this.matcher.unregister(pattern);
    }

    public Map<String, HttpAsyncRequestHandler<?>> getHandlers() {
        return this.matcher.getObjects();
    }

    public void setHandlers(Map<String, HttpAsyncRequestHandler<?>> map) {
        this.matcher.setObjects(map);
    }

    public HttpAsyncRequestHandler<?> lookup(String requestURI) {
        return (HttpAsyncRequestHandler) this.matcher.lookup(requestURI);
    }
}


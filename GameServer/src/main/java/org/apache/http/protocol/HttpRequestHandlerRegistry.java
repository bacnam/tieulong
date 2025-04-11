package org.apache.http.protocol;

import org.apache.http.annotation.ThreadSafe;
import org.apache.http.util.Args;

import java.util.Map;

@Deprecated
@ThreadSafe
public class HttpRequestHandlerRegistry
        implements HttpRequestHandlerResolver {
    private final UriPatternMatcher<HttpRequestHandler> matcher = new UriPatternMatcher<HttpRequestHandler>();

    public void register(String pattern, HttpRequestHandler handler) {
        Args.notNull(pattern, "URI request pattern");
        Args.notNull(handler, "Request handler");
        this.matcher.register(pattern, handler);
    }

    public void unregister(String pattern) {
        this.matcher.unregister(pattern);
    }

    public Map<String, HttpRequestHandler> getHandlers() {
        return this.matcher.getObjects();
    }

    public void setHandlers(Map<String, HttpRequestHandler> map) {
        this.matcher.setObjects(map);
    }

    public HttpRequestHandler lookup(String requestURI) {
        return this.matcher.lookup(requestURI);
    }
}


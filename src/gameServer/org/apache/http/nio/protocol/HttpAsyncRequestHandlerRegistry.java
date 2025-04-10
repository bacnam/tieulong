package org.apache.http.nio.protocol;

import java.util.Map;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.protocol.UriPatternMatcher;

@Deprecated
@ThreadSafe
public class HttpAsyncRequestHandlerRegistry
implements HttpAsyncRequestHandlerResolver
{
private final UriPatternMatcher<HttpAsyncRequestHandler<?>> matcher = new UriPatternMatcher();

public void register(String pattern, HttpAsyncRequestHandler<?> handler) {
this.matcher.register(pattern, handler);
}

public void unregister(String pattern) {
this.matcher.unregister(pattern);
}

public void setHandlers(Map<String, HttpAsyncRequestHandler<?>> map) {
this.matcher.setObjects(map);
}

public Map<String, HttpAsyncRequestHandler<?>> getHandlers() {
return this.matcher.getObjects();
}

public HttpAsyncRequestHandler<?> lookup(String requestURI) {
return (HttpAsyncRequestHandler)this.matcher.lookup(requestURI);
}
}


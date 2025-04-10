package org.apache.http.nio.protocol;

import org.apache.http.HttpRequest;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.protocol.UriPatternMatcher;
import org.apache.http.util.Args;

@ThreadSafe
public class UriHttpAsyncRequestHandlerMapper
implements HttpAsyncRequestHandlerMapper
{
private final UriPatternMatcher<HttpAsyncRequestHandler<?>> matcher;

protected UriHttpAsyncRequestHandlerMapper(UriPatternMatcher<HttpAsyncRequestHandler<?>> matcher) {
this.matcher = (UriPatternMatcher<HttpAsyncRequestHandler<?>>)Args.notNull(matcher, "Pattern matcher");
}

public UriHttpAsyncRequestHandlerMapper() {
this(new UriPatternMatcher());
}

public void register(String pattern, HttpAsyncRequestHandler<?> handler) {
this.matcher.register(pattern, handler);
}

public void unregister(String pattern) {
this.matcher.unregister(pattern);
}

protected String getRequestPath(HttpRequest request) {
String uriPath = request.getRequestLine().getUri();
int index = uriPath.indexOf("?");
if (index != -1) {
uriPath = uriPath.substring(0, index);
} else {
index = uriPath.indexOf("#");
if (index != -1) {
uriPath = uriPath.substring(0, index);
}
} 
return uriPath;
}

public HttpAsyncRequestHandler<?> lookup(HttpRequest request) {
return (HttpAsyncRequestHandler)this.matcher.lookup(getRequestPath(request));
}
}


package org.apache.http.impl.client;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Immutable;

@Immutable
public class StandardHttpRequestRetryHandler
extends DefaultHttpRequestRetryHandler
{
private final Map<String, Boolean> idempotentMethods;

public StandardHttpRequestRetryHandler(int retryCount, boolean requestSentRetryEnabled) {
super(retryCount, requestSentRetryEnabled);
this.idempotentMethods = new ConcurrentHashMap<String, Boolean>();
this.idempotentMethods.put("GET", Boolean.TRUE);
this.idempotentMethods.put("HEAD", Boolean.TRUE);
this.idempotentMethods.put("PUT", Boolean.TRUE);
this.idempotentMethods.put("DELETE", Boolean.TRUE);
this.idempotentMethods.put("OPTIONS", Boolean.TRUE);
this.idempotentMethods.put("TRACE", Boolean.TRUE);
}

public StandardHttpRequestRetryHandler() {
this(3, false);
}

protected boolean handleAsIdempotent(HttpRequest request) {
String method = request.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
Boolean b = this.idempotentMethods.get(method);
return (b != null && b.booleanValue());
}
}


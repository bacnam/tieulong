package org.apache.http.nio.protocol;

@Deprecated
public interface NHttpRequestHandlerResolver {
  NHttpRequestHandler lookup(String paramString);
}


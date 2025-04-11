package org.apache.http.client;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.protocol.HttpContext;

import java.net.URI;

@Deprecated
public interface RedirectHandler {
    boolean isRedirectRequested(HttpResponse paramHttpResponse, HttpContext paramHttpContext);

    URI getLocationURI(HttpResponse paramHttpResponse, HttpContext paramHttpContext) throws ProtocolException;
}


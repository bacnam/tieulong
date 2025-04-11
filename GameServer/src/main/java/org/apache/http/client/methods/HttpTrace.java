package org.apache.http.client.methods;

import org.apache.http.annotation.NotThreadSafe;

import java.net.URI;

@NotThreadSafe
public class HttpTrace
        extends HttpRequestBase {
    public static final String METHOD_NAME = "TRACE";

    public HttpTrace() {
    }

    public HttpTrace(URI uri) {
        setURI(uri);
    }

    public HttpTrace(String uri) {
        setURI(URI.create(uri));
    }

    public String getMethod() {
        return "TRACE";
    }
}


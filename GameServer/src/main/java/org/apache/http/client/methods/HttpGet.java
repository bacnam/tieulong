package org.apache.http.client.methods;

import org.apache.http.annotation.NotThreadSafe;

import java.net.URI;

@NotThreadSafe
public class HttpGet
        extends HttpRequestBase {
    public static final String METHOD_NAME = "GET";

    public HttpGet() {
    }

    public HttpGet(URI uri) {
        setURI(uri);
    }

    public HttpGet(String uri) {
        setURI(URI.create(uri));
    }

    public String getMethod() {
        return "GET";
    }
}


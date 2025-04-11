package org.apache.http.client.methods;

import org.apache.http.annotation.NotThreadSafe;

import java.net.URI;

@NotThreadSafe
public class HttpHead
        extends HttpRequestBase {
    public static final String METHOD_NAME = "HEAD";

    public HttpHead() {
    }

    public HttpHead(URI uri) {
        setURI(uri);
    }

    public HttpHead(String uri) {
        setURI(URI.create(uri));
    }

    public String getMethod() {
        return "HEAD";
    }
}


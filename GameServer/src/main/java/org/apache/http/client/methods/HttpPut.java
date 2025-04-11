package org.apache.http.client.methods;

import org.apache.http.annotation.NotThreadSafe;

import java.net.URI;

@NotThreadSafe
public class HttpPut
        extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "PUT";

    public HttpPut() {
    }

    public HttpPut(URI uri) {
        setURI(uri);
    }

    public HttpPut(String uri) {
        setURI(URI.create(uri));
    }

    public String getMethod() {
        return "PUT";
    }
}


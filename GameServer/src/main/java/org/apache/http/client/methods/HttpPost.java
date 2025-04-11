package org.apache.http.client.methods;

import org.apache.http.annotation.NotThreadSafe;

import java.net.URI;

@NotThreadSafe
public class HttpPost
        extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "POST";

    public HttpPost() {
    }

    public HttpPost(URI uri) {
        setURI(uri);
    }

    public HttpPost(String uri) {
        setURI(URI.create(uri));
    }

    public String getMethod() {
        return "POST";
    }
}


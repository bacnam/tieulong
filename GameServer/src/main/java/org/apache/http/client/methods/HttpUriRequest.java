package org.apache.http.client.methods;

import org.apache.http.HttpRequest;

import java.net.URI;

public interface HttpUriRequest extends HttpRequest {
    String getMethod();

    URI getURI();

    void abort() throws UnsupportedOperationException;

    boolean isAborted();
}


package org.apache.http.client;

import org.apache.http.HttpResponse;

import java.io.IOException;

public interface ResponseHandler<T> {
    T handleResponse(HttpResponse paramHttpResponse) throws ClientProtocolException, IOException;
}


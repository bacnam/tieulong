package org.apache.http.nio;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;

import java.io.IOException;

public interface NHttpClientConnection extends NHttpConnection {
    void submitRequest(HttpRequest paramHttpRequest) throws IOException, HttpException;

    boolean isRequestSubmitted();

    void resetOutput();

    void resetInput();
}


package org.apache.http.nio;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;

import java.io.IOException;

public interface NHttpServerConnection extends NHttpConnection {
    void submitResponse(HttpResponse paramHttpResponse) throws IOException, HttpException;

    boolean isResponseSubmitted();

    void resetInput();

    void resetOutput();
}


package org.apache.http.nio.protocol;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;

import java.io.IOException;

@Deprecated
public interface NHttpResponseTrigger {
    void submitResponse(HttpResponse paramHttpResponse);

    void handleException(HttpException paramHttpException);

    void handleException(IOException paramIOException);
}


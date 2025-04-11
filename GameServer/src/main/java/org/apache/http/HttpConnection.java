package org.apache.http;

import java.io.Closeable;
import java.io.IOException;

public interface HttpConnection extends Closeable {
    void close() throws IOException;

    boolean isOpen();

    boolean isStale();

    int getSocketTimeout();

    void setSocketTimeout(int paramInt);

    void shutdown() throws IOException;

    HttpConnectionMetrics getMetrics();
}


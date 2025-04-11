package org.apache.http.nio.reactor;

public interface SessionBufferStatus {
    boolean hasBufferedInput();

    boolean hasBufferedOutput();
}


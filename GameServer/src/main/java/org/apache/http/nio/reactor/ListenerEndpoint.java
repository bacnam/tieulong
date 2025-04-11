package org.apache.http.nio.reactor;

import java.io.IOException;
import java.net.SocketAddress;

public interface ListenerEndpoint {
    SocketAddress getAddress();

    IOException getException();

    void waitFor() throws InterruptedException;

    boolean isClosed();

    void close();
}


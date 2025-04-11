package org.apache.http.nio;

import org.apache.http.nio.reactor.IOSession;

public interface NHttpConnectionFactory<T extends NHttpConnection> {
    T createConnection(IOSession paramIOSession);
}


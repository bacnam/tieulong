package org.apache.http.nio.conn.scheme;

import org.apache.http.nio.reactor.IOSession;

@Deprecated
public interface LayeringStrategy {
    boolean isSecure();

    IOSession layer(IOSession paramIOSession);
}


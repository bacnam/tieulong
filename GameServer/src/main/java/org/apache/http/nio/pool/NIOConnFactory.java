package org.apache.http.nio.pool;

import org.apache.http.nio.reactor.IOSession;

import java.io.IOException;

public interface NIOConnFactory<T, C> {
    C create(T paramT, IOSession paramIOSession) throws IOException;
}


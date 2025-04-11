package org.apache.http.nio.pool;

import java.io.IOException;
import java.net.SocketAddress;

public interface SocketAddressResolver<T> {
    SocketAddress resolveLocalAddress(T paramT) throws IOException;

    SocketAddress resolveRemoteAddress(T paramT) throws IOException;
}


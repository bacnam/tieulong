package org.apache.http.nio.conn;

import org.apache.http.HttpHost;
import org.apache.http.nio.reactor.IOSession;

import java.io.IOException;

public interface SchemeIOSessionStrategy {
    boolean isLayeringRequired();

    IOSession upgrade(HttpHost paramHttpHost, IOSession paramIOSession) throws IOException;
}


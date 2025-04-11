package org.apache.http.impl.nio.conn;

import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.nio.pool.NIOConnFactory;
import org.apache.http.nio.reactor.IOSession;

import java.io.IOException;

@Deprecated
class HttpNIOConnPoolFactory
        implements NIOConnFactory<HttpRoute, IOSession> {
    public IOSession create(HttpRoute route, IOSession session) throws IOException {
        return session;
    }
}


package org.apache.http.nio.pool;

import java.io.IOException;
import org.apache.http.nio.reactor.IOSession;

public interface NIOConnFactory<T, C> {
  C create(T paramT, IOSession paramIOSession) throws IOException;
}


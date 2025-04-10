package org.apache.http.nio.conn;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.http.nio.reactor.IOSession;

public interface SchemeIOSessionStrategy {
  boolean isLayeringRequired();

  IOSession upgrade(HttpHost paramHttpHost, IOSession paramIOSession) throws IOException;
}


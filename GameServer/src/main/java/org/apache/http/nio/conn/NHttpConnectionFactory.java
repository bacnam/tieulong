package org.apache.http.nio.conn;

import org.apache.http.config.ConnectionConfig;
import org.apache.http.nio.reactor.IOSession;

public interface NHttpConnectionFactory<T extends org.apache.http.nio.NHttpConnection> {
  T create(IOSession paramIOSession, ConnectionConfig paramConnectionConfig);
}


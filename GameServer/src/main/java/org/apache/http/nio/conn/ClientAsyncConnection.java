package org.apache.http.nio.conn;

import org.apache.http.HttpInetConnection;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.nio.reactor.IOSession;

@Deprecated
public interface ClientAsyncConnection extends NHttpClientConnection, HttpInetConnection {
  void upgrade(IOSession paramIOSession);

  IOSession getIOSession();
}


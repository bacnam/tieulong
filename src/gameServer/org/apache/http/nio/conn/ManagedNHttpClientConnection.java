package org.apache.http.nio.conn;

import javax.net.ssl.SSLSession;
import org.apache.http.HttpInetConnection;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.nio.reactor.IOSession;

public interface ManagedNHttpClientConnection extends NHttpClientConnection, HttpInetConnection {
  String getId();

  void bind(IOSession paramIOSession);

  IOSession getIOSession();

  SSLSession getSSLSession();
}


package org.apache.http.nio.conn;

import org.apache.http.HttpInetConnection;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.nio.reactor.IOSession;

import javax.net.ssl.SSLSession;

public interface ManagedNHttpClientConnection extends NHttpClientConnection, HttpInetConnection {
    String getId();

    void bind(IOSession paramIOSession);

    IOSession getIOSession();

    SSLSession getSSLSession();
}


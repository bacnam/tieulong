package org.apache.http.conn;

import org.apache.http.HttpInetConnection;
import org.apache.http.conn.routing.HttpRoute;

import javax.net.ssl.SSLSession;

@Deprecated
public interface HttpRoutedConnection extends HttpInetConnection {
    boolean isSecure();

    HttpRoute getRoute();

    SSLSession getSSLSession();
}


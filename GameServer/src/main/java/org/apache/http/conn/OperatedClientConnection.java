package org.apache.http.conn;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.HttpInetConnection;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.Socket;

@Deprecated
public interface OperatedClientConnection extends HttpClientConnection, HttpInetConnection {
    HttpHost getTargetHost();

    boolean isSecure();

    Socket getSocket();

    void opening(Socket paramSocket, HttpHost paramHttpHost) throws IOException;

    void openCompleted(boolean paramBoolean, HttpParams paramHttpParams) throws IOException;

    void update(Socket paramSocket, HttpHost paramHttpHost, boolean paramBoolean, HttpParams paramHttpParams) throws IOException;
}


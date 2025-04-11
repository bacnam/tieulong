package org.apache.http.conn;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpInetConnection;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.Socket;

public interface ManagedHttpClientConnection extends HttpClientConnection, HttpInetConnection {
    String getId();

    void bind(Socket paramSocket) throws IOException;

    Socket getSocket();

    SSLSession getSSLSession();
}


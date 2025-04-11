package org.apache.http.conn.socket;

import org.apache.http.HttpHost;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public interface ConnectionSocketFactory {
    Socket createSocket(HttpContext paramHttpContext) throws IOException;

    Socket connectSocket(int paramInt, Socket paramSocket, HttpHost paramHttpHost, InetSocketAddress paramInetSocketAddress1, InetSocketAddress paramInetSocketAddress2, HttpContext paramHttpContext) throws IOException;
}


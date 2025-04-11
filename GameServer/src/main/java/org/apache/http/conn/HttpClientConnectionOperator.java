package org.apache.http.conn;

import org.apache.http.HttpHost;
import org.apache.http.config.SocketConfig;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.InetSocketAddress;

public interface HttpClientConnectionOperator {
    void connect(ManagedHttpClientConnection paramManagedHttpClientConnection, HttpHost paramHttpHost, InetSocketAddress paramInetSocketAddress, int paramInt, SocketConfig paramSocketConfig, HttpContext paramHttpContext) throws IOException;

    void upgrade(ManagedHttpClientConnection paramManagedHttpClientConnection, HttpHost paramHttpHost, HttpContext paramHttpContext) throws IOException;
}


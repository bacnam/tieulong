package org.apache.http.impl.nio.reactor;

import org.apache.http.params.HttpParams;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import java.net.SocketAddress;

@Deprecated
public interface SSLIOSessionHandler {
    void initalize(SSLEngine paramSSLEngine, HttpParams paramHttpParams) throws SSLException;

    void verify(SocketAddress paramSocketAddress, SSLSession paramSSLSession) throws SSLException;
}


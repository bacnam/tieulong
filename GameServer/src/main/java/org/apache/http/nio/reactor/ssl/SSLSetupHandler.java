package org.apache.http.nio.reactor.ssl;

import org.apache.http.nio.reactor.IOSession;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

public interface SSLSetupHandler {
    void initalize(SSLEngine paramSSLEngine) throws SSLException;

    void verify(IOSession paramIOSession, SSLSession paramSSLSession) throws SSLException;
}


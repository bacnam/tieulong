package org.apache.http.nio.reactor.ssl;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import org.apache.http.nio.reactor.IOSession;

public interface SSLSetupHandler {
  void initalize(SSLEngine paramSSLEngine) throws SSLException;

  void verify(IOSession paramIOSession, SSLSession paramSSLSession) throws SSLException;
}


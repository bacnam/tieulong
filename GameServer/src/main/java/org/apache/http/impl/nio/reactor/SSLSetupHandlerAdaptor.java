package org.apache.http.impl.nio.reactor;

import org.apache.http.nio.reactor.IOSession;
import org.apache.http.nio.reactor.ssl.SSLSetupHandler;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

@Deprecated
class SSLSetupHandlerAdaptor
        implements SSLSetupHandler {
    private final SSLSetupHandler handler;
    private HttpParams params;

    public SSLSetupHandlerAdaptor(SSLSetupHandler handler) {
        this.handler = handler;
    }

    public void initalize(SSLEngine sslengine) throws SSLException {
        this.handler.initalize(sslengine, (this.params != null) ? this.params : (HttpParams) new BasicHttpParams());
    }

    public void verify(IOSession iosession, SSLSession sslsession) throws SSLException {
        this.handler.verify(iosession, sslsession);
    }

    public void setParams(HttpParams params) {
        this.params = params;
    }
}


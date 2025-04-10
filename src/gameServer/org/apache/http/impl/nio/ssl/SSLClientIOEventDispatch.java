package org.apache.http.impl.nio.ssl;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import org.apache.http.annotation.Immutable;
import org.apache.http.impl.nio.DefaultClientIOEventDispatch;
import org.apache.http.impl.nio.reactor.SSLIOSession;
import org.apache.http.impl.nio.reactor.SSLSetupHandler;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.nio.NHttpClientHandler;
import org.apache.http.nio.NHttpClientIOTarget;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
@Immutable
public class SSLClientIOEventDispatch
extends DefaultClientIOEventDispatch
{
private final SSLContext sslcontext;
private final SSLSetupHandler sslHandler;

public SSLClientIOEventDispatch(NHttpClientHandler handler, SSLContext sslcontext, SSLSetupHandler sslHandler, HttpParams params) {
super(handler, params);
Args.notNull(sslcontext, "SSL context");
Args.notNull(params, "HTTP parameters");
this.sslcontext = sslcontext;
this.sslHandler = sslHandler;
}

public SSLClientIOEventDispatch(NHttpClientHandler handler, SSLContext sslcontext, HttpParams params) {
this(handler, sslcontext, (SSLSetupHandler)null, params);
}

protected SSLIOSession createSSLIOSession(IOSession session, SSLContext sslcontext, SSLSetupHandler sslHandler) {
return new SSLIOSession(session, sslcontext, sslHandler);
}

protected NHttpClientIOTarget createSSLConnection(SSLIOSession ssliosession) {
return super.createConnection((IOSession)ssliosession);
}

protected NHttpClientIOTarget createConnection(IOSession session) {
SSLIOSession ssliosession = createSSLIOSession(session, this.sslcontext, this.sslHandler);
session.setAttribute("http.session.ssl", ssliosession);
NHttpClientIOTarget conn = createSSLConnection(ssliosession);
try {
ssliosession.initialize();
} catch (SSLException ex) {
this.handler.exception((NHttpClientConnection)conn, ex);
ssliosession.shutdown();
} 
return conn;
}

public void onConnected(NHttpClientIOTarget conn) {
int timeout = HttpConnectionParams.getSoTimeout(this.params);
conn.setSocketTimeout(timeout);

Object attachment = conn.getContext().getAttribute("http.session.attachment");
this.handler.connected((NHttpClientConnection)conn, attachment);
}
}


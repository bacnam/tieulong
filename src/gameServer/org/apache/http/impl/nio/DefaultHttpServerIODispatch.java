package org.apache.http.impl.nio;

import java.io.IOException;
import javax.net.ssl.SSLContext;
import org.apache.http.annotation.Immutable;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.nio.reactor.AbstractIODispatch;
import org.apache.http.nio.NHttpConnectionFactory;
import org.apache.http.nio.NHttpServerConnection;
import org.apache.http.nio.NHttpServerEventHandler;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.nio.reactor.ssl.SSLSetupHandler;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Immutable
public class DefaultHttpServerIODispatch
extends AbstractIODispatch<DefaultNHttpServerConnection>
{
private final NHttpServerEventHandler handler;
private final NHttpConnectionFactory<? extends DefaultNHttpServerConnection> connFactory;

public DefaultHttpServerIODispatch(NHttpServerEventHandler handler, NHttpConnectionFactory<? extends DefaultNHttpServerConnection> connFactory) {
this.handler = (NHttpServerEventHandler)Args.notNull(handler, "HTTP client handler");
this.connFactory = (NHttpConnectionFactory<? extends DefaultNHttpServerConnection>)Args.notNull(connFactory, "HTTP server connection factory");
}

@Deprecated
public DefaultHttpServerIODispatch(NHttpServerEventHandler handler, HttpParams params) {
this(handler, new DefaultNHttpServerConnectionFactory(params));
}

@Deprecated
public DefaultHttpServerIODispatch(NHttpServerEventHandler handler, SSLContext sslcontext, SSLSetupHandler sslHandler, HttpParams params) {
this(handler, new SSLNHttpServerConnectionFactory(sslcontext, sslHandler, params));
}

@Deprecated
public DefaultHttpServerIODispatch(NHttpServerEventHandler handler, SSLContext sslcontext, HttpParams params) {
this(handler, sslcontext, (SSLSetupHandler)null, params);
}

public DefaultHttpServerIODispatch(NHttpServerEventHandler handler, ConnectionConfig config) {
this(handler, new DefaultNHttpServerConnectionFactory(config));
}

public DefaultHttpServerIODispatch(NHttpServerEventHandler handler, SSLContext sslcontext, SSLSetupHandler sslHandler, ConnectionConfig config) {
this(handler, new SSLNHttpServerConnectionFactory(sslcontext, sslHandler, config));
}

public DefaultHttpServerIODispatch(NHttpServerEventHandler handler, SSLContext sslcontext, ConnectionConfig config) {
this(handler, new SSLNHttpServerConnectionFactory(sslcontext, null, config));
}

protected DefaultNHttpServerConnection createConnection(IOSession session) {
return (DefaultNHttpServerConnection)this.connFactory.createConnection(session);
}

protected void onConnected(DefaultNHttpServerConnection conn) {
try {
this.handler.connected((NHttpServerConnection)conn);
} catch (Exception ex) {
this.handler.exception((NHttpServerConnection)conn, ex);
} 
}

protected void onClosed(DefaultNHttpServerConnection conn) {
this.handler.closed((NHttpServerConnection)conn);
}

protected void onException(DefaultNHttpServerConnection conn, IOException ex) {
this.handler.exception((NHttpServerConnection)conn, ex);
}

protected void onInputReady(DefaultNHttpServerConnection conn) {
conn.consumeInput(this.handler);
}

protected void onOutputReady(DefaultNHttpServerConnection conn) {
conn.produceOutput(this.handler);
}

protected void onTimeout(DefaultNHttpServerConnection conn) {
try {
this.handler.timeout((NHttpServerConnection)conn);
} catch (Exception ex) {
this.handler.exception((NHttpServerConnection)conn, ex);
} 
}
}


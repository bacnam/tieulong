package org.apache.http.impl.nio;

import org.apache.http.annotation.Immutable;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.nio.reactor.AbstractIODispatch;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.nio.NHttpClientEventHandler;
import org.apache.http.nio.NHttpConnectionFactory;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.nio.reactor.ssl.SSLSetupHandler;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

import javax.net.ssl.SSLContext;
import java.io.IOException;

@Immutable
public class DefaultHttpClientIODispatch
        extends AbstractIODispatch<DefaultNHttpClientConnection> {
    private final NHttpClientEventHandler handler;
    private final NHttpConnectionFactory<DefaultNHttpClientConnection> connFactory;

    public DefaultHttpClientIODispatch(NHttpClientEventHandler handler, NHttpConnectionFactory<DefaultNHttpClientConnection> connFactory) {
        this.handler = (NHttpClientEventHandler) Args.notNull(handler, "HTTP client handler");
        this.connFactory = (NHttpConnectionFactory<DefaultNHttpClientConnection>) Args.notNull(connFactory, "HTTP client connection factory");
    }

    @Deprecated
    public DefaultHttpClientIODispatch(NHttpClientEventHandler handler, HttpParams params) {
        this(handler, new DefaultNHttpClientConnectionFactory(params));
    }

    @Deprecated
    public DefaultHttpClientIODispatch(NHttpClientEventHandler handler, SSLContext sslcontext, SSLSetupHandler sslHandler, HttpParams params) {
        this(handler, new SSLNHttpClientConnectionFactory(sslcontext, sslHandler, params));
    }

    @Deprecated
    public DefaultHttpClientIODispatch(NHttpClientEventHandler handler, SSLContext sslcontext, HttpParams params) {
        this(handler, sslcontext, (SSLSetupHandler) null, params);
    }

    public DefaultHttpClientIODispatch(NHttpClientEventHandler handler, ConnectionConfig config) {
        this(handler, new DefaultNHttpClientConnectionFactory(config));
    }

    public DefaultHttpClientIODispatch(NHttpClientEventHandler handler, SSLContext sslcontext, SSLSetupHandler sslHandler, ConnectionConfig config) {
        this(handler, new SSLNHttpClientConnectionFactory(sslcontext, sslHandler, config));
    }

    public DefaultHttpClientIODispatch(NHttpClientEventHandler handler, SSLContext sslcontext, ConnectionConfig config) {
        this(handler, new SSLNHttpClientConnectionFactory(sslcontext, null, config));
    }

    protected DefaultNHttpClientConnection createConnection(IOSession session) {
        return (DefaultNHttpClientConnection) this.connFactory.createConnection(session);
    }

    protected void onConnected(DefaultNHttpClientConnection conn) {
        Object attachment = conn.getContext().getAttribute("http.session.attachment");
        try {
            this.handler.connected((NHttpClientConnection) conn, attachment);
        } catch (Exception ex) {
            this.handler.exception((NHttpClientConnection) conn, ex);
        }
    }

    protected void onClosed(DefaultNHttpClientConnection conn) {
        this.handler.closed((NHttpClientConnection) conn);
    }

    protected void onException(DefaultNHttpClientConnection conn, IOException ex) {
        this.handler.exception((NHttpClientConnection) conn, ex);
    }

    protected void onInputReady(DefaultNHttpClientConnection conn) {
        conn.consumeInput(this.handler);
    }

    protected void onOutputReady(DefaultNHttpClientConnection conn) {
        conn.produceOutput(this.handler);
    }

    protected void onTimeout(DefaultNHttpClientConnection conn) {
        try {
            this.handler.timeout((NHttpClientConnection) conn);
        } catch (Exception ex) {
            this.handler.exception((NHttpClientConnection) conn, ex);
        }
    }
}


package org.apache.http.impl.nio.client;

import org.apache.http.impl.nio.DefaultNHttpClientConnection;
import org.apache.http.impl.nio.reactor.AbstractIODispatch;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.nio.NHttpClientEventHandler;
import org.apache.http.nio.reactor.IOSession;

import java.io.IOException;

class InternalIODispatch
        extends AbstractIODispatch<DefaultNHttpClientConnection> {
    private final NHttpClientEventHandler handler = (NHttpClientEventHandler) new LoggingAsyncRequestExecutor();

    protected DefaultNHttpClientConnection createConnection(IOSession session) {
        throw new IllegalStateException("Connection must be created by connection manager");
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


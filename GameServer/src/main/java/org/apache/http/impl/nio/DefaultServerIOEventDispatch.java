package org.apache.http.impl.nio;

import org.apache.http.HttpRequestFactory;
import org.apache.http.annotation.Immutable;
import org.apache.http.impl.DefaultHttpRequestFactory;
import org.apache.http.impl.nio.reactor.AbstractIODispatch;
import org.apache.http.nio.NHttpServerConnection;
import org.apache.http.nio.NHttpServerIOTarget;
import org.apache.http.nio.NHttpServiceHandler;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.nio.util.ByteBufferAllocator;
import org.apache.http.nio.util.HeapByteBufferAllocator;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

import java.io.IOException;

@Deprecated
@Immutable
public class DefaultServerIOEventDispatch
        extends AbstractIODispatch<NHttpServerIOTarget> {
    protected final ByteBufferAllocator allocator;
    protected final NHttpServiceHandler handler;
    protected final HttpParams params;

    public DefaultServerIOEventDispatch(NHttpServiceHandler handler, HttpParams params) {
        Args.notNull(handler, "HTTP service handler");
        Args.notNull(params, "HTTP parameters");
        this.allocator = createByteBufferAllocator();
        this.handler = handler;
        this.params = params;
    }

    protected ByteBufferAllocator createByteBufferAllocator() {
        return (ByteBufferAllocator) HeapByteBufferAllocator.INSTANCE;
    }

    protected HttpRequestFactory createHttpRequestFactory() {
        return (HttpRequestFactory) DefaultHttpRequestFactory.INSTANCE;
    }

    protected NHttpServerIOTarget createConnection(IOSession session) {
        return new DefaultNHttpServerConnection(session, createHttpRequestFactory(), this.allocator, this.params);
    }

    protected void onConnected(NHttpServerIOTarget conn) {
        int timeout = HttpConnectionParams.getSoTimeout(this.params);
        conn.setSocketTimeout(timeout);
        this.handler.connected((NHttpServerConnection) conn);
    }

    protected void onClosed(NHttpServerIOTarget conn) {
        this.handler.closed((NHttpServerConnection) conn);
    }

    protected void onException(NHttpServerIOTarget conn, IOException ex) {
        this.handler.exception((NHttpServerConnection) conn, ex);
    }

    protected void onInputReady(NHttpServerIOTarget conn) {
        conn.consumeInput(this.handler);
    }

    protected void onOutputReady(NHttpServerIOTarget conn) {
        conn.produceOutput(this.handler);
    }

    protected void onTimeout(NHttpServerIOTarget conn) {
        this.handler.timeout((NHttpServerConnection) conn);
    }
}


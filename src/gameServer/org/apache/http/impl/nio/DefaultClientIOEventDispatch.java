package org.apache.http.impl.nio;

import java.io.IOException;
import org.apache.http.HttpResponseFactory;
import org.apache.http.annotation.Immutable;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.nio.reactor.AbstractIODispatch;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.nio.NHttpClientHandler;
import org.apache.http.nio.NHttpClientIOTarget;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.nio.util.ByteBufferAllocator;
import org.apache.http.nio.util.HeapByteBufferAllocator;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
@Immutable
public class DefaultClientIOEventDispatch
extends AbstractIODispatch<NHttpClientIOTarget>
{
protected final NHttpClientHandler handler;
protected final ByteBufferAllocator allocator;
protected final HttpParams params;

public DefaultClientIOEventDispatch(NHttpClientHandler handler, HttpParams params) {
Args.notNull(handler, "HTTP client handler");
Args.notNull(params, "HTTP parameters");
this.allocator = createByteBufferAllocator();
this.handler = handler;
this.params = params;
}

protected ByteBufferAllocator createByteBufferAllocator() {
return (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE;
}

protected HttpResponseFactory createHttpResponseFactory() {
return (HttpResponseFactory)DefaultHttpResponseFactory.INSTANCE;
}

protected NHttpClientIOTarget createConnection(IOSession session) {
return new DefaultNHttpClientConnection(session, createHttpResponseFactory(), this.allocator, this.params);
}

protected void onConnected(NHttpClientIOTarget conn) {
int timeout = HttpConnectionParams.getSoTimeout(this.params);
conn.setSocketTimeout(timeout);

Object attachment = conn.getContext().getAttribute("http.session.attachment");
this.handler.connected((NHttpClientConnection)conn, attachment);
}

protected void onClosed(NHttpClientIOTarget conn) {
this.handler.closed((NHttpClientConnection)conn);
}

protected void onException(NHttpClientIOTarget conn, IOException ex) {
this.handler.exception((NHttpClientConnection)conn, ex);
}

protected void onInputReady(NHttpClientIOTarget conn) {
conn.consumeInput(this.handler);
}

protected void onOutputReady(NHttpClientIOTarget conn) {
conn.produceOutput(this.handler);
}

protected void onTimeout(NHttpClientIOTarget conn) {
this.handler.timeout((NHttpClientConnection)conn);
}
}


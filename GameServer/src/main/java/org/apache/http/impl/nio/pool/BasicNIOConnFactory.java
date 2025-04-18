package org.apache.http.impl.nio.pool;

import java.io.IOException;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.annotation.Immutable;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.nio.DefaultNHttpClientConnectionFactory;
import org.apache.http.impl.nio.SSLNHttpClientConnectionFactory;
import org.apache.http.nio.NHttpClientConnection;
import org.apache.http.nio.NHttpConnectionFactory;
import org.apache.http.nio.NHttpMessageParserFactory;
import org.apache.http.nio.NHttpMessageWriterFactory;
import org.apache.http.nio.pool.NIOConnFactory;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.nio.reactor.ssl.SSLSetupHandler;
import org.apache.http.nio.util.ByteBufferAllocator;
import org.apache.http.nio.util.HeapByteBufferAllocator;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Immutable
public class BasicNIOConnFactory
implements NIOConnFactory<HttpHost, NHttpClientConnection>
{
private final NHttpConnectionFactory<? extends NHttpClientConnection> plainFactory;
private final NHttpConnectionFactory<? extends NHttpClientConnection> sslFactory;

public BasicNIOConnFactory(NHttpConnectionFactory<? extends NHttpClientConnection> plainFactory, NHttpConnectionFactory<? extends NHttpClientConnection> sslFactory) {
Args.notNull(plainFactory, "Plain HTTP client connection factory");
this.plainFactory = plainFactory;
this.sslFactory = sslFactory;
}

public BasicNIOConnFactory(NHttpConnectionFactory<? extends NHttpClientConnection> plainFactory) {
this(plainFactory, null);
}

@Deprecated
public BasicNIOConnFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, HttpResponseFactory responseFactory, ByteBufferAllocator allocator, HttpParams params) {
this((NHttpConnectionFactory<? extends NHttpClientConnection>)new DefaultNHttpClientConnectionFactory(responseFactory, allocator, params), (NHttpConnectionFactory<? extends NHttpClientConnection>)new SSLNHttpClientConnectionFactory(sslcontext, sslHandler, responseFactory, allocator, params));
}

@Deprecated
public BasicNIOConnFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, HttpParams params) {
this(sslcontext, sslHandler, (HttpResponseFactory)DefaultHttpResponseFactory.INSTANCE, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE, params);
}

@Deprecated
public BasicNIOConnFactory(HttpParams params) {
this((SSLContext)null, (SSLSetupHandler)null, params);
}

public BasicNIOConnFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, NHttpMessageParserFactory<HttpResponse> responseParserFactory, NHttpMessageWriterFactory<HttpRequest> requestWriterFactory, ByteBufferAllocator allocator, ConnectionConfig config) {
this((NHttpConnectionFactory<? extends NHttpClientConnection>)new DefaultNHttpClientConnectionFactory(responseParserFactory, requestWriterFactory, allocator, config), (NHttpConnectionFactory<? extends NHttpClientConnection>)new SSLNHttpClientConnectionFactory(sslcontext, sslHandler, responseParserFactory, requestWriterFactory, allocator, config));
}

public BasicNIOConnFactory(SSLContext sslcontext, SSLSetupHandler sslHandler, ConnectionConfig config) {
this(sslcontext, sslHandler, null, null, null, config);
}

public BasicNIOConnFactory(ConnectionConfig config) {
this((NHttpConnectionFactory<? extends NHttpClientConnection>)new DefaultNHttpClientConnectionFactory(config), null);
}

public NHttpClientConnection create(HttpHost route, IOSession session) throws IOException {
NHttpClientConnection conn;
if (route.getSchemeName().equalsIgnoreCase("https")) {
if (this.sslFactory == null) {
throw new IOException("SSL not supported");
}
conn = (NHttpClientConnection)this.sslFactory.createConnection(session);
} else {
conn = (NHttpClientConnection)this.plainFactory.createConnection(session);
} 
session.setAttribute("http.connection", conn);
return conn;
}
}


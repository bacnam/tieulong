package org.apache.http.impl.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import org.apache.http.ConnectionClosedException;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpInetConnection;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.HttpConnectionMetricsImpl;
import org.apache.http.impl.entity.LaxContentLengthStrategy;
import org.apache.http.impl.entity.StrictContentLengthStrategy;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.impl.nio.codecs.ChunkDecoder;
import org.apache.http.impl.nio.codecs.ChunkEncoder;
import org.apache.http.impl.nio.codecs.IdentityDecoder;
import org.apache.http.impl.nio.codecs.IdentityEncoder;
import org.apache.http.impl.nio.codecs.LengthDelimitedDecoder;
import org.apache.http.impl.nio.codecs.LengthDelimitedEncoder;
import org.apache.http.impl.nio.reactor.SessionInputBufferImpl;
import org.apache.http.impl.nio.reactor.SessionOutputBufferImpl;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.nio.ContentDecoder;
import org.apache.http.nio.ContentEncoder;
import org.apache.http.nio.NHttpConnection;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.nio.reactor.SessionBufferStatus;
import org.apache.http.nio.reactor.SessionInputBuffer;
import org.apache.http.nio.reactor.SessionOutputBuffer;
import org.apache.http.nio.reactor.SocketAccessor;
import org.apache.http.nio.util.ByteBufferAllocator;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.NetUtils;

@NotThreadSafe
public class NHttpConnectionBase
implements NHttpConnection, HttpInetConnection, SessionBufferStatus, SocketAccessor
{
protected final ContentLengthStrategy incomingContentStrategy;
protected final ContentLengthStrategy outgoingContentStrategy;
protected final SessionInputBufferImpl inbuf;
protected final SessionOutputBufferImpl outbuf;
private final int fragmentSizeHint;
private final MessageConstraints constraints;
protected final HttpTransportMetricsImpl inTransportMetrics;
protected final HttpTransportMetricsImpl outTransportMetrics;
protected final HttpConnectionMetricsImpl connMetrics;
protected HttpContext context;
protected IOSession session;
protected SocketAddress remote;
protected volatile ContentDecoder contentDecoder;
protected volatile boolean hasBufferedInput;
protected volatile ContentEncoder contentEncoder;
protected volatile boolean hasBufferedOutput;
protected volatile HttpRequest request;
protected volatile HttpResponse response;
protected volatile int status;

@Deprecated
public NHttpConnectionBase(IOSession session, ByteBufferAllocator allocator, HttpParams params) {
Args.notNull(session, "I/O session");
Args.notNull(params, "HTTP params");

int buffersize = params.getIntParameter("http.socket.buffer-size", -1);
if (buffersize <= 0) {
buffersize = 4096;
}
int linebuffersize = buffersize;
if (linebuffersize > 512) {
linebuffersize = 512;
}

CharsetDecoder decoder = null;
CharsetEncoder encoder = null;
Charset charset = CharsetUtils.lookup((String)params.getParameter("http.protocol.element-charset"));

if (charset != null) {
charset = Consts.ASCII;
decoder = charset.newDecoder();
encoder = charset.newEncoder();
CodingErrorAction malformedCharAction = (CodingErrorAction)params.getParameter("http.malformed.input.action");

CodingErrorAction unmappableCharAction = (CodingErrorAction)params.getParameter("http.unmappable.input.action");

decoder.onMalformedInput(malformedCharAction).onUnmappableCharacter(unmappableCharAction);
encoder.onMalformedInput(malformedCharAction).onUnmappableCharacter(unmappableCharAction);
} 
this.inbuf = new SessionInputBufferImpl(buffersize, linebuffersize, decoder, allocator);
this.outbuf = new SessionOutputBufferImpl(buffersize, linebuffersize, encoder, allocator);
this.fragmentSizeHint = buffersize;
this.constraints = MessageConstraints.DEFAULT;

this.incomingContentStrategy = createIncomingContentStrategy();
this.outgoingContentStrategy = createOutgoingContentStrategy();

this.inTransportMetrics = createTransportMetrics();
this.outTransportMetrics = createTransportMetrics();
this.connMetrics = createConnectionMetrics((HttpTransportMetrics)this.inTransportMetrics, (HttpTransportMetrics)this.outTransportMetrics);

setSession(session);
this.status = 0;
}

protected NHttpConnectionBase(IOSession session, int buffersize, int fragmentSizeHint, ByteBufferAllocator allocator, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy) {
Args.notNull(session, "I/O session");
Args.positive(buffersize, "Buffer size");
int linebuffersize = buffersize;
if (linebuffersize > 512) {
linebuffersize = 512;
}
this.inbuf = new SessionInputBufferImpl(buffersize, linebuffersize, chardecoder, allocator);
this.outbuf = new SessionOutputBufferImpl(buffersize, linebuffersize, charencoder, allocator);
this.fragmentSizeHint = (fragmentSizeHint >= 0) ? fragmentSizeHint : buffersize;

this.inTransportMetrics = new HttpTransportMetricsImpl();
this.outTransportMetrics = new HttpTransportMetricsImpl();
this.connMetrics = new HttpConnectionMetricsImpl((HttpTransportMetrics)this.inTransportMetrics, (HttpTransportMetrics)this.outTransportMetrics);
this.constraints = (constraints != null) ? constraints : MessageConstraints.DEFAULT;
this.incomingContentStrategy = (incomingContentStrategy != null) ? incomingContentStrategy : (ContentLengthStrategy)LaxContentLengthStrategy.INSTANCE;

this.outgoingContentStrategy = (outgoingContentStrategy != null) ? outgoingContentStrategy : (ContentLengthStrategy)StrictContentLengthStrategy.INSTANCE;

setSession(session);
this.status = 0;
}

protected NHttpConnectionBase(IOSession session, int buffersize, int fragmentSizeHint, ByteBufferAllocator allocator, CharsetDecoder chardecoder, CharsetEncoder charencoder, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy) {
this(session, buffersize, fragmentSizeHint, allocator, chardecoder, charencoder, null, incomingContentStrategy, outgoingContentStrategy);
}

private void setSession(IOSession session) {
this.session = session;
this.context = new SessionHttpContext(this.session);
this.session.setBufferStatus(this);
this.remote = this.session.getRemoteAddress();
}

protected void bind(IOSession session) {
Args.notNull(session, "I/O session");
setSession(session);
}

@Deprecated
protected ContentLengthStrategy createIncomingContentStrategy() {
return (ContentLengthStrategy)new LaxContentLengthStrategy();
}

@Deprecated
protected ContentLengthStrategy createOutgoingContentStrategy() {
return (ContentLengthStrategy)new StrictContentLengthStrategy();
}

@Deprecated
protected HttpTransportMetricsImpl createTransportMetrics() {
return new HttpTransportMetricsImpl();
}

@Deprecated
protected HttpConnectionMetricsImpl createConnectionMetrics(HttpTransportMetrics inTransportMetric, HttpTransportMetrics outTransportMetric) {
return new HttpConnectionMetricsImpl(inTransportMetric, outTransportMetric);
}

public int getStatus() {
return this.status;
}

public HttpContext getContext() {
return this.context;
}

public HttpRequest getHttpRequest() {
return this.request;
}

public HttpResponse getHttpResponse() {
return this.response;
}

public void requestInput() {
this.session.setEvent(1);
}

public void requestOutput() {
this.session.setEvent(4);
}

public void suspendInput() {
this.session.clearEvent(1);
}

public void suspendOutput() {
this.session.clearEvent(4);
}

protected HttpEntity prepareDecoder(HttpMessage message) throws HttpException {
BasicHttpEntity entity = new BasicHttpEntity();
long len = this.incomingContentStrategy.determineLength(message);
this.contentDecoder = createContentDecoder(len, this.session.channel(), (SessionInputBuffer)this.inbuf, this.inTransportMetrics);

if (len == -2L) {
entity.setChunked(true);
entity.setContentLength(-1L);
} else if (len == -1L) {
entity.setChunked(false);
entity.setContentLength(-1L);
} else {
entity.setChunked(false);
entity.setContentLength(len);
} 

Header contentTypeHeader = message.getFirstHeader("Content-Type");
if (contentTypeHeader != null) {
entity.setContentType(contentTypeHeader);
}
Header contentEncodingHeader = message.getFirstHeader("Content-Encoding");
if (contentEncodingHeader != null) {
entity.setContentEncoding(contentEncodingHeader);
}
return (HttpEntity)entity;
}

protected ContentDecoder createContentDecoder(long len, ReadableByteChannel channel, SessionInputBuffer buffer, HttpTransportMetricsImpl metrics) {
if (len == -2L)
return (ContentDecoder)new ChunkDecoder(channel, buffer, this.constraints, metrics); 
if (len == -1L) {
return (ContentDecoder)new IdentityDecoder(channel, buffer, metrics);
}
return (ContentDecoder)new LengthDelimitedDecoder(channel, buffer, metrics, len);
}

protected void prepareEncoder(HttpMessage message) throws HttpException {
long len = this.outgoingContentStrategy.determineLength(message);
this.contentEncoder = createContentEncoder(len, this.session.channel(), (SessionOutputBuffer)this.outbuf, this.outTransportMetrics);
}

protected ContentEncoder createContentEncoder(long len, WritableByteChannel channel, SessionOutputBuffer buffer, HttpTransportMetricsImpl metrics) {
if (len == -2L)
return (ContentEncoder)new ChunkEncoder(channel, buffer, metrics, this.fragmentSizeHint); 
if (len == -1L) {
return (ContentEncoder)new IdentityEncoder(channel, buffer, metrics, this.fragmentSizeHint);
}
return (ContentEncoder)new LengthDelimitedEncoder(channel, buffer, metrics, len, this.fragmentSizeHint);
}

public boolean hasBufferedInput() {
return this.hasBufferedInput;
}

public boolean hasBufferedOutput() {
return this.hasBufferedOutput;
}

protected void assertNotClosed() throws ConnectionClosedException {
if (this.status != 0) {
throw new ConnectionClosedException("Connection is closed");
}
}

public void close() throws IOException {
if (this.status != 0) {
return;
}
this.status = 1;
if (this.outbuf.hasData()) {
this.session.setEvent(4);
} else {
this.session.close();
this.status = 2;
} 
}

public boolean isOpen() {
return (this.status == 0 && !this.session.isClosed());
}

public boolean isStale() {
return this.session.isClosed();
}

public InetAddress getLocalAddress() {
SocketAddress address = this.session.getLocalAddress();
if (address instanceof InetSocketAddress) {
return ((InetSocketAddress)address).getAddress();
}
return null;
}

public int getLocalPort() {
SocketAddress address = this.session.getLocalAddress();
if (address instanceof InetSocketAddress) {
return ((InetSocketAddress)address).getPort();
}
return -1;
}

public InetAddress getRemoteAddress() {
SocketAddress address = this.session.getRemoteAddress();
if (address instanceof InetSocketAddress) {
return ((InetSocketAddress)address).getAddress();
}
return null;
}

public int getRemotePort() {
SocketAddress address = this.session.getRemoteAddress();
if (address instanceof InetSocketAddress) {
return ((InetSocketAddress)address).getPort();
}
return -1;
}

public void setSocketTimeout(int timeout) {
this.session.setSocketTimeout(timeout);
}

public int getSocketTimeout() {
return this.session.getSocketTimeout();
}

public void shutdown() throws IOException {
this.status = 2;
this.session.shutdown();
}

public HttpConnectionMetrics getMetrics() {
return (HttpConnectionMetrics)this.connMetrics;
}

public String toString() {
SocketAddress remoteAddress = this.session.getRemoteAddress();
SocketAddress localAddress = this.session.getLocalAddress();
if (remoteAddress != null && localAddress != null) {
StringBuilder buffer = new StringBuilder();
NetUtils.formatAddress(buffer, localAddress);
buffer.append("<->");
NetUtils.formatAddress(buffer, remoteAddress);
return buffer.toString();
} 
return "[Not bound]";
}

public Socket getSocket() {
if (this.session instanceof SocketAccessor) {
return ((SocketAccessor)this.session).getSocket();
}
return null;
}
}


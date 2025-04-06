/*     */ package org.apache.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import org.apache.http.ConnectionClosedException;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpConnectionMetrics;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpInetConnection;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.config.MessageConstraints;
/*     */ import org.apache.http.entity.BasicHttpEntity;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.impl.HttpConnectionMetricsImpl;
/*     */ import org.apache.http.impl.entity.LaxContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.StrictContentLengthStrategy;
/*     */ import org.apache.http.impl.io.HttpTransportMetricsImpl;
/*     */ import org.apache.http.impl.nio.codecs.ChunkDecoder;
/*     */ import org.apache.http.impl.nio.codecs.ChunkEncoder;
/*     */ import org.apache.http.impl.nio.codecs.IdentityDecoder;
/*     */ import org.apache.http.impl.nio.codecs.IdentityEncoder;
/*     */ import org.apache.http.impl.nio.codecs.LengthDelimitedDecoder;
/*     */ import org.apache.http.impl.nio.codecs.LengthDelimitedEncoder;
/*     */ import org.apache.http.impl.nio.reactor.SessionInputBufferImpl;
/*     */ import org.apache.http.impl.nio.reactor.SessionOutputBufferImpl;
/*     */ import org.apache.http.io.HttpTransportMetrics;
/*     */ import org.apache.http.nio.ContentDecoder;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.NHttpConnection;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.reactor.SessionBufferStatus;
/*     */ import org.apache.http.nio.reactor.SessionInputBuffer;
/*     */ import org.apache.http.nio.reactor.SessionOutputBuffer;
/*     */ import org.apache.http.nio.reactor.SocketAccessor;
/*     */ import org.apache.http.nio.util.ByteBufferAllocator;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.CharsetUtils;
/*     */ import org.apache.http.util.NetUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class NHttpConnectionBase
/*     */   implements NHttpConnection, HttpInetConnection, SessionBufferStatus, SocketAccessor
/*     */ {
/*     */   protected final ContentLengthStrategy incomingContentStrategy;
/*     */   protected final ContentLengthStrategy outgoingContentStrategy;
/*     */   protected final SessionInputBufferImpl inbuf;
/*     */   protected final SessionOutputBufferImpl outbuf;
/*     */   private final int fragmentSizeHint;
/*     */   private final MessageConstraints constraints;
/*     */   protected final HttpTransportMetricsImpl inTransportMetrics;
/*     */   protected final HttpTransportMetricsImpl outTransportMetrics;
/*     */   protected final HttpConnectionMetricsImpl connMetrics;
/*     */   protected HttpContext context;
/*     */   protected IOSession session;
/*     */   protected SocketAddress remote;
/*     */   protected volatile ContentDecoder contentDecoder;
/*     */   protected volatile boolean hasBufferedInput;
/*     */   protected volatile ContentEncoder contentEncoder;
/*     */   protected volatile boolean hasBufferedOutput;
/*     */   protected volatile HttpRequest request;
/*     */   protected volatile HttpResponse response;
/*     */   protected volatile int status;
/*     */   
/*     */   @Deprecated
/*     */   public NHttpConnectionBase(IOSession session, ByteBufferAllocator allocator, HttpParams params) {
/* 140 */     Args.notNull(session, "I/O session");
/* 141 */     Args.notNull(params, "HTTP params");
/*     */     
/* 143 */     int buffersize = params.getIntParameter("http.socket.buffer-size", -1);
/* 144 */     if (buffersize <= 0) {
/* 145 */       buffersize = 4096;
/*     */     }
/* 147 */     int linebuffersize = buffersize;
/* 148 */     if (linebuffersize > 512) {
/* 149 */       linebuffersize = 512;
/*     */     }
/*     */     
/* 152 */     CharsetDecoder decoder = null;
/* 153 */     CharsetEncoder encoder = null;
/* 154 */     Charset charset = CharsetUtils.lookup((String)params.getParameter("http.protocol.element-charset"));
/*     */     
/* 156 */     if (charset != null) {
/* 157 */       charset = Consts.ASCII;
/* 158 */       decoder = charset.newDecoder();
/* 159 */       encoder = charset.newEncoder();
/* 160 */       CodingErrorAction malformedCharAction = (CodingErrorAction)params.getParameter("http.malformed.input.action");
/*     */       
/* 162 */       CodingErrorAction unmappableCharAction = (CodingErrorAction)params.getParameter("http.unmappable.input.action");
/*     */       
/* 164 */       decoder.onMalformedInput(malformedCharAction).onUnmappableCharacter(unmappableCharAction);
/* 165 */       encoder.onMalformedInput(malformedCharAction).onUnmappableCharacter(unmappableCharAction);
/*     */     } 
/* 167 */     this.inbuf = new SessionInputBufferImpl(buffersize, linebuffersize, decoder, allocator);
/* 168 */     this.outbuf = new SessionOutputBufferImpl(buffersize, linebuffersize, encoder, allocator);
/* 169 */     this.fragmentSizeHint = buffersize;
/* 170 */     this.constraints = MessageConstraints.DEFAULT;
/*     */     
/* 172 */     this.incomingContentStrategy = createIncomingContentStrategy();
/* 173 */     this.outgoingContentStrategy = createOutgoingContentStrategy();
/*     */     
/* 175 */     this.inTransportMetrics = createTransportMetrics();
/* 176 */     this.outTransportMetrics = createTransportMetrics();
/* 177 */     this.connMetrics = createConnectionMetrics((HttpTransportMetrics)this.inTransportMetrics, (HttpTransportMetrics)this.outTransportMetrics);
/*     */ 
/*     */ 
/*     */     
/* 181 */     setSession(session);
/* 182 */     this.status = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected NHttpConnectionBase(IOSession session, int buffersize, int fragmentSizeHint, ByteBufferAllocator allocator, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy) {
/* 217 */     Args.notNull(session, "I/O session");
/* 218 */     Args.positive(buffersize, "Buffer size");
/* 219 */     int linebuffersize = buffersize;
/* 220 */     if (linebuffersize > 512) {
/* 221 */       linebuffersize = 512;
/*     */     }
/* 223 */     this.inbuf = new SessionInputBufferImpl(buffersize, linebuffersize, chardecoder, allocator);
/* 224 */     this.outbuf = new SessionOutputBufferImpl(buffersize, linebuffersize, charencoder, allocator);
/* 225 */     this.fragmentSizeHint = (fragmentSizeHint >= 0) ? fragmentSizeHint : buffersize;
/*     */     
/* 227 */     this.inTransportMetrics = new HttpTransportMetricsImpl();
/* 228 */     this.outTransportMetrics = new HttpTransportMetricsImpl();
/* 229 */     this.connMetrics = new HttpConnectionMetricsImpl((HttpTransportMetrics)this.inTransportMetrics, (HttpTransportMetrics)this.outTransportMetrics);
/* 230 */     this.constraints = (constraints != null) ? constraints : MessageConstraints.DEFAULT;
/* 231 */     this.incomingContentStrategy = (incomingContentStrategy != null) ? incomingContentStrategy : (ContentLengthStrategy)LaxContentLengthStrategy.INSTANCE;
/*     */     
/* 233 */     this.outgoingContentStrategy = (outgoingContentStrategy != null) ? outgoingContentStrategy : (ContentLengthStrategy)StrictContentLengthStrategy.INSTANCE;
/*     */ 
/*     */     
/* 236 */     setSession(session);
/* 237 */     this.status = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected NHttpConnectionBase(IOSession session, int buffersize, int fragmentSizeHint, ByteBufferAllocator allocator, CharsetDecoder chardecoder, CharsetEncoder charencoder, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy) {
/* 269 */     this(session, buffersize, fragmentSizeHint, allocator, chardecoder, charencoder, null, incomingContentStrategy, outgoingContentStrategy);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setSession(IOSession session) {
/* 274 */     this.session = session;
/* 275 */     this.context = new SessionHttpContext(this.session);
/* 276 */     this.session.setBufferStatus(this);
/* 277 */     this.remote = this.session.getRemoteAddress();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void bind(IOSession session) {
/* 287 */     Args.notNull(session, "I/O session");
/* 288 */     setSession(session);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected ContentLengthStrategy createIncomingContentStrategy() {
/* 298 */     return (ContentLengthStrategy)new LaxContentLengthStrategy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected ContentLengthStrategy createOutgoingContentStrategy() {
/* 308 */     return (ContentLengthStrategy)new StrictContentLengthStrategy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected HttpTransportMetricsImpl createTransportMetrics() {
/* 318 */     return new HttpTransportMetricsImpl();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected HttpConnectionMetricsImpl createConnectionMetrics(HttpTransportMetrics inTransportMetric, HttpTransportMetrics outTransportMetric) {
/* 330 */     return new HttpConnectionMetricsImpl(inTransportMetric, outTransportMetric);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStatus() {
/* 335 */     return this.status;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpContext getContext() {
/* 340 */     return this.context;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpRequest getHttpRequest() {
/* 345 */     return this.request;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpResponse getHttpResponse() {
/* 350 */     return this.response;
/*     */   }
/*     */ 
/*     */   
/*     */   public void requestInput() {
/* 355 */     this.session.setEvent(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void requestOutput() {
/* 360 */     this.session.setEvent(4);
/*     */   }
/*     */ 
/*     */   
/*     */   public void suspendInput() {
/* 365 */     this.session.clearEvent(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void suspendOutput() {
/* 370 */     this.session.clearEvent(4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpEntity prepareDecoder(HttpMessage message) throws HttpException {
/* 383 */     BasicHttpEntity entity = new BasicHttpEntity();
/* 384 */     long len = this.incomingContentStrategy.determineLength(message);
/* 385 */     this.contentDecoder = createContentDecoder(len, this.session.channel(), (SessionInputBuffer)this.inbuf, this.inTransportMetrics);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 390 */     if (len == -2L) {
/* 391 */       entity.setChunked(true);
/* 392 */       entity.setContentLength(-1L);
/* 393 */     } else if (len == -1L) {
/* 394 */       entity.setChunked(false);
/* 395 */       entity.setContentLength(-1L);
/*     */     } else {
/* 397 */       entity.setChunked(false);
/* 398 */       entity.setContentLength(len);
/*     */     } 
/*     */     
/* 401 */     Header contentTypeHeader = message.getFirstHeader("Content-Type");
/* 402 */     if (contentTypeHeader != null) {
/* 403 */       entity.setContentType(contentTypeHeader);
/*     */     }
/* 405 */     Header contentEncodingHeader = message.getFirstHeader("Content-Encoding");
/* 406 */     if (contentEncodingHeader != null) {
/* 407 */       entity.setContentEncoding(contentEncodingHeader);
/*     */     }
/* 409 */     return (HttpEntity)entity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ContentDecoder createContentDecoder(long len, ReadableByteChannel channel, SessionInputBuffer buffer, HttpTransportMetricsImpl metrics) {
/* 430 */     if (len == -2L)
/* 431 */       return (ContentDecoder)new ChunkDecoder(channel, buffer, this.constraints, metrics); 
/* 432 */     if (len == -1L) {
/* 433 */       return (ContentDecoder)new IdentityDecoder(channel, buffer, metrics);
/*     */     }
/* 435 */     return (ContentDecoder)new LengthDelimitedDecoder(channel, buffer, metrics, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void prepareEncoder(HttpMessage message) throws HttpException {
/* 447 */     long len = this.outgoingContentStrategy.determineLength(message);
/* 448 */     this.contentEncoder = createContentEncoder(len, this.session.channel(), (SessionOutputBuffer)this.outbuf, this.outTransportMetrics);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ContentEncoder createContentEncoder(long len, WritableByteChannel channel, SessionOutputBuffer buffer, HttpTransportMetricsImpl metrics) {
/* 473 */     if (len == -2L)
/* 474 */       return (ContentEncoder)new ChunkEncoder(channel, buffer, metrics, this.fragmentSizeHint); 
/* 475 */     if (len == -1L) {
/* 476 */       return (ContentEncoder)new IdentityEncoder(channel, buffer, metrics, this.fragmentSizeHint);
/*     */     }
/* 478 */     return (ContentEncoder)new LengthDelimitedEncoder(channel, buffer, metrics, len, this.fragmentSizeHint);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasBufferedInput() {
/* 484 */     return this.hasBufferedInput;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasBufferedOutput() {
/* 489 */     return this.hasBufferedOutput;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void assertNotClosed() throws ConnectionClosedException {
/* 499 */     if (this.status != 0) {
/* 500 */       throw new ConnectionClosedException("Connection is closed");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 506 */     if (this.status != 0) {
/*     */       return;
/*     */     }
/* 509 */     this.status = 1;
/* 510 */     if (this.outbuf.hasData()) {
/* 511 */       this.session.setEvent(4);
/*     */     } else {
/* 513 */       this.session.close();
/* 514 */       this.status = 2;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 520 */     return (this.status == 0 && !this.session.isClosed());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStale() {
/* 525 */     return this.session.isClosed();
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 530 */     SocketAddress address = this.session.getLocalAddress();
/* 531 */     if (address instanceof InetSocketAddress) {
/* 532 */       return ((InetSocketAddress)address).getAddress();
/*     */     }
/* 534 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLocalPort() {
/* 540 */     SocketAddress address = this.session.getLocalAddress();
/* 541 */     if (address instanceof InetSocketAddress) {
/* 542 */       return ((InetSocketAddress)address).getPort();
/*     */     }
/* 544 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InetAddress getRemoteAddress() {
/* 550 */     SocketAddress address = this.session.getRemoteAddress();
/* 551 */     if (address instanceof InetSocketAddress) {
/* 552 */       return ((InetSocketAddress)address).getAddress();
/*     */     }
/* 554 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRemotePort() {
/* 560 */     SocketAddress address = this.session.getRemoteAddress();
/* 561 */     if (address instanceof InetSocketAddress) {
/* 562 */       return ((InetSocketAddress)address).getPort();
/*     */     }
/* 564 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(int timeout) {
/* 570 */     this.session.setSocketTimeout(timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSocketTimeout() {
/* 575 */     return this.session.getSocketTimeout();
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() throws IOException {
/* 580 */     this.status = 2;
/* 581 */     this.session.shutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpConnectionMetrics getMetrics() {
/* 586 */     return (HttpConnectionMetrics)this.connMetrics;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 591 */     SocketAddress remoteAddress = this.session.getRemoteAddress();
/* 592 */     SocketAddress localAddress = this.session.getLocalAddress();
/* 593 */     if (remoteAddress != null && localAddress != null) {
/* 594 */       StringBuilder buffer = new StringBuilder();
/* 595 */       NetUtils.formatAddress(buffer, localAddress);
/* 596 */       buffer.append("<->");
/* 597 */       NetUtils.formatAddress(buffer, remoteAddress);
/* 598 */       return buffer.toString();
/*     */     } 
/* 600 */     return "[Not bound]";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket getSocket() {
/* 606 */     if (this.session instanceof SocketAccessor) {
/* 607 */       return ((SocketAccessor)this.session).getSocket();
/*     */     }
/* 609 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/NHttpConnectionBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
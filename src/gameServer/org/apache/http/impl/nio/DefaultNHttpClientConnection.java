/*     */ package org.apache.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.config.MessageConstraints;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.impl.nio.codecs.DefaultHttpRequestWriter;
/*     */ import org.apache.http.impl.nio.codecs.DefaultHttpRequestWriterFactory;
/*     */ import org.apache.http.impl.nio.codecs.DefaultHttpResponseParser;
/*     */ import org.apache.http.impl.nio.codecs.DefaultHttpResponseParserFactory;
/*     */ import org.apache.http.nio.NHttpClientConnection;
/*     */ import org.apache.http.nio.NHttpClientEventHandler;
/*     */ import org.apache.http.nio.NHttpClientHandler;
/*     */ import org.apache.http.nio.NHttpClientIOTarget;
/*     */ import org.apache.http.nio.NHttpMessageParser;
/*     */ import org.apache.http.nio.NHttpMessageParserFactory;
/*     */ import org.apache.http.nio.NHttpMessageWriter;
/*     */ import org.apache.http.nio.NHttpMessageWriterFactory;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.reactor.SessionInputBuffer;
/*     */ import org.apache.http.nio.reactor.SessionOutputBuffer;
/*     */ import org.apache.http.nio.util.ByteBufferAllocator;
/*     */ import org.apache.http.params.HttpParamConfig;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.util.Args;
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
/*     */ public class DefaultNHttpClientConnection
/*     */   extends NHttpConnectionBase
/*     */   implements NHttpClientIOTarget
/*     */ {
/*     */   protected final NHttpMessageParser<HttpResponse> responseParser;
/*     */   protected final NHttpMessageWriter<HttpRequest> requestWriter;
/*     */   
/*     */   @Deprecated
/*     */   public DefaultNHttpClientConnection(IOSession session, HttpResponseFactory responseFactory, ByteBufferAllocator allocator, HttpParams params) {
/*  97 */     super(session, allocator, params);
/*  98 */     Args.notNull(responseFactory, "Response factory");
/*  99 */     this.responseParser = createResponseParser((SessionInputBuffer)this.inbuf, responseFactory, params);
/* 100 */     this.requestWriter = createRequestWriter((SessionOutputBuffer)this.outbuf, params);
/* 101 */     this.hasBufferedInput = false;
/* 102 */     this.hasBufferedOutput = false;
/* 103 */     this.session.setBufferStatus(this);
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
/*     */ 
/*     */   
/*     */   public DefaultNHttpClientConnection(IOSession session, int buffersize, int fragmentSizeHint, ByteBufferAllocator allocator, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, NHttpMessageWriterFactory<HttpRequest> requestWriterFactory, NHttpMessageParserFactory<HttpResponse> responseParserFactory) {
/* 140 */     super(session, buffersize, fragmentSizeHint, allocator, chardecoder, charencoder, constraints, incomingContentStrategy, outgoingContentStrategy);
/*     */     
/* 142 */     this.requestWriter = ((requestWriterFactory != null) ? requestWriterFactory : DefaultHttpRequestWriterFactory.INSTANCE).create((SessionOutputBuffer)this.outbuf);
/*     */     
/* 144 */     this.responseParser = ((responseParserFactory != null) ? responseParserFactory : DefaultHttpResponseParserFactory.INSTANCE).create((SessionInputBuffer)this.inbuf, constraints);
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
/*     */   public DefaultNHttpClientConnection(IOSession session, int buffersize, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints) {
/* 157 */     this(session, buffersize, buffersize, (ByteBufferAllocator)null, chardecoder, charencoder, constraints, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (NHttpMessageWriterFactory<HttpRequest>)null, (NHttpMessageParserFactory<HttpResponse>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultNHttpClientConnection(IOSession session, int buffersize) {
/* 165 */     this(session, buffersize, buffersize, (ByteBufferAllocator)null, (CharsetDecoder)null, (CharsetEncoder)null, (MessageConstraints)null, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (NHttpMessageWriterFactory<HttpRequest>)null, (NHttpMessageParserFactory<HttpResponse>)null);
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
/*     */   @Deprecated
/*     */   protected NHttpMessageParser<HttpResponse> createResponseParser(SessionInputBuffer buffer, HttpResponseFactory responseFactory, HttpParams params) {
/* 185 */     MessageConstraints constraints = HttpParamConfig.getMessageConstraints(params);
/* 186 */     return (NHttpMessageParser<HttpResponse>)new DefaultHttpResponseParser(buffer, null, responseFactory, constraints);
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
/*     */   @Deprecated
/*     */   protected NHttpMessageWriter<HttpRequest> createRequestWriter(SessionOutputBuffer buffer, HttpParams params) {
/* 205 */     return (NHttpMessageWriter<HttpRequest>)new DefaultHttpRequestWriter(buffer, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onResponseReceived(HttpResponse response) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onRequestSubmitted(HttpRequest request) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetInput() {
/* 222 */     this.response = null;
/* 223 */     this.contentDecoder = null;
/* 224 */     this.responseParser.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetOutput() {
/* 229 */     this.request = null;
/* 230 */     this.contentEncoder = null;
/* 231 */     this.requestWriter.reset();
/*     */   }
/*     */   
/*     */   public void consumeInput(NHttpClientEventHandler handler) {
/* 235 */     if (this.status != 0) {
/* 236 */       this.session.clearEvent(1);
/*     */       return;
/*     */     } 
/*     */     try {
/* 240 */       if (this.response == null) {
/*     */         int bytesRead;
/*     */         do {
/* 243 */           bytesRead = this.responseParser.fillBuffer(this.session.channel());
/* 244 */           if (bytesRead > 0) {
/* 245 */             this.inTransportMetrics.incrementBytesTransferred(bytesRead);
/*     */           }
/* 247 */           this.response = (HttpResponse)this.responseParser.parse();
/* 248 */         } while (bytesRead > 0 && this.response == null);
/* 249 */         if (this.response != null) {
/* 250 */           if (this.response.getStatusLine().getStatusCode() >= 200) {
/* 251 */             HttpEntity entity = prepareDecoder((HttpMessage)this.response);
/* 252 */             this.response.setEntity(entity);
/* 253 */             this.connMetrics.incrementResponseCount();
/*     */           } 
/* 255 */           this.hasBufferedInput = this.inbuf.hasData();
/* 256 */           onResponseReceived(this.response);
/* 257 */           handler.responseReceived((NHttpClientConnection)this);
/* 258 */           if (this.contentDecoder == null) {
/* 259 */             resetInput();
/*     */           }
/*     */         } 
/* 262 */         if (bytesRead == -1 && !this.inbuf.hasData()) {
/* 263 */           handler.endOfInput((NHttpClientConnection)this);
/*     */         }
/*     */       } 
/* 266 */       if (this.contentDecoder != null && (this.session.getEventMask() & 0x1) > 0) {
/* 267 */         handler.inputReady((NHttpClientConnection)this, this.contentDecoder);
/* 268 */         if (this.contentDecoder.isCompleted())
/*     */         {
/*     */           
/* 271 */           resetInput();
/*     */         }
/*     */       } 
/* 274 */     } catch (HttpException ex) {
/* 275 */       resetInput();
/* 276 */       handler.exception((NHttpClientConnection)this, (Exception)ex);
/* 277 */     } catch (Exception ex) {
/* 278 */       handler.exception((NHttpClientConnection)this, ex);
/*     */     } finally {
/*     */       
/* 281 */       this.hasBufferedInput = this.inbuf.hasData();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void produceOutput(NHttpClientEventHandler handler) {
/*     */     try {
/* 287 */       if (this.status == 0) {
/* 288 */         if (this.contentEncoder == null) {
/* 289 */           handler.requestReady((NHttpClientConnection)this);
/*     */         }
/* 291 */         if (this.contentEncoder != null) {
/* 292 */           handler.outputReady((NHttpClientConnection)this, this.contentEncoder);
/* 293 */           if (this.contentEncoder.isCompleted()) {
/* 294 */             resetOutput();
/*     */           }
/*     */         } 
/*     */       } 
/* 298 */       if (this.outbuf.hasData()) {
/* 299 */         int bytesWritten = this.outbuf.flush(this.session.channel());
/* 300 */         if (bytesWritten > 0) {
/* 301 */           this.outTransportMetrics.incrementBytesTransferred(bytesWritten);
/*     */         }
/*     */       } 
/* 304 */       if (!this.outbuf.hasData()) {
/* 305 */         if (this.status == 1) {
/* 306 */           this.session.close();
/* 307 */           this.status = 2;
/* 308 */           resetOutput();
/*     */         } 
/* 310 */         if (this.contentEncoder == null && this.status != 2) {
/* 311 */           this.session.clearEvent(4);
/*     */         }
/*     */       } 
/* 314 */     } catch (Exception ex) {
/* 315 */       handler.exception((NHttpClientConnection)this, ex);
/*     */     } finally {
/*     */       
/* 318 */       this.hasBufferedOutput = this.outbuf.hasData();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitRequest(HttpRequest request) throws IOException, HttpException {
/* 324 */     Args.notNull(request, "HTTP request");
/* 325 */     assertNotClosed();
/* 326 */     if (this.request != null) {
/* 327 */       throw new HttpException("Request already submitted");
/*     */     }
/* 329 */     onRequestSubmitted(request);
/* 330 */     this.requestWriter.write((HttpMessage)request);
/* 331 */     this.hasBufferedOutput = this.outbuf.hasData();
/*     */     
/* 333 */     if (request instanceof HttpEntityEnclosingRequest && ((HttpEntityEnclosingRequest)request).getEntity() != null) {
/*     */       
/* 335 */       prepareEncoder((HttpMessage)request);
/* 336 */       this.request = request;
/*     */     } 
/* 338 */     this.connMetrics.incrementRequestCount();
/* 339 */     this.session.setEvent(4);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRequestSubmitted() {
/* 344 */     return (this.request != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumeInput(NHttpClientHandler handler) {
/* 349 */     consumeInput(new NHttpClientEventHandlerAdaptor(handler));
/*     */   }
/*     */ 
/*     */   
/*     */   public void produceOutput(NHttpClientHandler handler) {
/* 354 */     produceOutput(new NHttpClientEventHandlerAdaptor(handler));
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/DefaultNHttpClientConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
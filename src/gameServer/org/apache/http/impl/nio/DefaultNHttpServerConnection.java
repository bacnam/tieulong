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
/*     */ import org.apache.http.HttpRequestFactory;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.config.MessageConstraints;
/*     */ import org.apache.http.entity.ContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.DisallowIdentityContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.LaxContentLengthStrategy;
/*     */ import org.apache.http.impl.entity.StrictContentLengthStrategy;
/*     */ import org.apache.http.impl.nio.codecs.DefaultHttpRequestParser;
/*     */ import org.apache.http.impl.nio.codecs.DefaultHttpRequestParserFactory;
/*     */ import org.apache.http.impl.nio.codecs.DefaultHttpResponseWriter;
/*     */ import org.apache.http.impl.nio.codecs.DefaultHttpResponseWriterFactory;
/*     */ import org.apache.http.nio.NHttpMessageParser;
/*     */ import org.apache.http.nio.NHttpMessageParserFactory;
/*     */ import org.apache.http.nio.NHttpMessageWriter;
/*     */ import org.apache.http.nio.NHttpMessageWriterFactory;
/*     */ import org.apache.http.nio.NHttpServerConnection;
/*     */ import org.apache.http.nio.NHttpServerEventHandler;
/*     */ import org.apache.http.nio.NHttpServerIOTarget;
/*     */ import org.apache.http.nio.NHttpServiceHandler;
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
/*     */ public class DefaultNHttpServerConnection
/*     */   extends NHttpConnectionBase
/*     */   implements NHttpServerIOTarget
/*     */ {
/*     */   protected final NHttpMessageParser<HttpRequest> requestParser;
/*     */   protected final NHttpMessageWriter<HttpResponse> responseWriter;
/*     */   
/*     */   @Deprecated
/*     */   public DefaultNHttpServerConnection(IOSession session, HttpRequestFactory requestFactory, ByteBufferAllocator allocator, HttpParams params) {
/* 100 */     super(session, allocator, params);
/* 101 */     Args.notNull(requestFactory, "Request factory");
/* 102 */     this.requestParser = createRequestParser((SessionInputBuffer)this.inbuf, requestFactory, params);
/* 103 */     this.responseWriter = createResponseWriter((SessionOutputBuffer)this.outbuf, params);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultNHttpServerConnection(IOSession session, int buffersize, int fragmentSizeHint, ByteBufferAllocator allocator, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, NHttpMessageParserFactory<HttpRequest> requestParserFactory, NHttpMessageWriterFactory<HttpResponse> responseWriterFactory) {
/* 144 */     super(session, buffersize, fragmentSizeHint, allocator, chardecoder, charencoder, constraints, (incomingContentStrategy != null) ? incomingContentStrategy : (ContentLengthStrategy)DisallowIdentityContentLengthStrategy.INSTANCE, (outgoingContentStrategy != null) ? outgoingContentStrategy : (ContentLengthStrategy)StrictContentLengthStrategy.INSTANCE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     this.requestParser = ((requestParserFactory != null) ? requestParserFactory : DefaultHttpRequestParserFactory.INSTANCE).create((SessionInputBuffer)this.inbuf, constraints);
/*     */     
/* 152 */     this.responseWriter = ((responseWriterFactory != null) ? responseWriterFactory : DefaultHttpResponseWriterFactory.INSTANCE).create((SessionOutputBuffer)this.outbuf);
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
/*     */   public DefaultNHttpServerConnection(IOSession session, int buffersize, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints) {
/* 165 */     this(session, buffersize, buffersize, (ByteBufferAllocator)null, chardecoder, charencoder, constraints, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (NHttpMessageParserFactory<HttpRequest>)null, (NHttpMessageWriterFactory<HttpResponse>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultNHttpServerConnection(IOSession session, int buffersize) {
/* 173 */     this(session, buffersize, buffersize, (ByteBufferAllocator)null, (CharsetDecoder)null, (CharsetEncoder)null, (MessageConstraints)null, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (NHttpMessageParserFactory<HttpRequest>)null, (NHttpMessageWriterFactory<HttpResponse>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected ContentLengthStrategy createIncomingContentStrategy() {
/* 182 */     return (ContentLengthStrategy)new DisallowIdentityContentLengthStrategy((ContentLengthStrategy)new LaxContentLengthStrategy(0));
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
/*     */   protected NHttpMessageParser<HttpRequest> createRequestParser(SessionInputBuffer buffer, HttpRequestFactory requestFactory, HttpParams params) {
/* 201 */     MessageConstraints constraints = HttpParamConfig.getMessageConstraints(params);
/* 202 */     return (NHttpMessageParser<HttpRequest>)new DefaultHttpRequestParser(buffer, null, requestFactory, constraints);
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
/*     */   protected NHttpMessageWriter<HttpResponse> createResponseWriter(SessionOutputBuffer buffer, HttpParams params) {
/* 222 */     return (NHttpMessageWriter<HttpResponse>)new DefaultHttpResponseWriter(buffer, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onRequestReceived(HttpRequest request) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onResponseSubmitted(HttpResponse response) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetInput() {
/* 239 */     this.request = null;
/* 240 */     this.contentDecoder = null;
/* 241 */     this.requestParser.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetOutput() {
/* 246 */     this.response = null;
/* 247 */     this.contentEncoder = null;
/* 248 */     this.responseWriter.reset();
/*     */   }
/*     */   
/*     */   public void consumeInput(NHttpServerEventHandler handler) {
/* 252 */     if (this.status != 0) {
/* 253 */       this.session.clearEvent(1);
/*     */       return;
/*     */     } 
/*     */     try {
/* 257 */       if (this.request == null) {
/*     */         int bytesRead;
/*     */         do {
/* 260 */           bytesRead = this.requestParser.fillBuffer(this.session.channel());
/* 261 */           if (bytesRead > 0) {
/* 262 */             this.inTransportMetrics.incrementBytesTransferred(bytesRead);
/*     */           }
/* 264 */           this.request = (HttpRequest)this.requestParser.parse();
/* 265 */         } while (bytesRead > 0 && this.request == null);
/* 266 */         if (this.request != null) {
/* 267 */           if (this.request instanceof HttpEntityEnclosingRequest) {
/*     */             
/* 269 */             HttpEntity entity = prepareDecoder((HttpMessage)this.request);
/* 270 */             ((HttpEntityEnclosingRequest)this.request).setEntity(entity);
/*     */           } 
/* 272 */           this.connMetrics.incrementRequestCount();
/* 273 */           this.hasBufferedInput = this.inbuf.hasData();
/* 274 */           onRequestReceived(this.request);
/* 275 */           handler.requestReceived((NHttpServerConnection)this);
/* 276 */           if (this.contentDecoder == null)
/*     */           {
/*     */             
/* 279 */             resetInput();
/*     */           }
/*     */         } 
/* 282 */         if (bytesRead == -1 && !this.inbuf.hasData()) {
/* 283 */           handler.endOfInput((NHttpServerConnection)this);
/*     */         }
/*     */       } 
/* 286 */       if (this.contentDecoder != null && (this.session.getEventMask() & 0x1) > 0) {
/* 287 */         handler.inputReady((NHttpServerConnection)this, this.contentDecoder);
/* 288 */         if (this.contentDecoder.isCompleted())
/*     */         {
/*     */           
/* 291 */           resetInput();
/*     */         }
/*     */       } 
/* 294 */     } catch (HttpException ex) {
/* 295 */       resetInput();
/* 296 */       handler.exception((NHttpServerConnection)this, (Exception)ex);
/* 297 */     } catch (Exception ex) {
/* 298 */       handler.exception((NHttpServerConnection)this, ex);
/*     */     } finally {
/*     */       
/* 301 */       this.hasBufferedInput = this.inbuf.hasData();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void produceOutput(NHttpServerEventHandler handler) {
/*     */     try {
/* 307 */       if (this.status == 0) {
/* 308 */         if (this.contentEncoder == null) {
/* 309 */           handler.responseReady((NHttpServerConnection)this);
/*     */         }
/* 311 */         if (this.contentEncoder != null) {
/* 312 */           handler.outputReady((NHttpServerConnection)this, this.contentEncoder);
/* 313 */           if (this.contentEncoder.isCompleted()) {
/* 314 */             resetOutput();
/*     */           }
/*     */         } 
/*     */       } 
/* 318 */       if (this.outbuf.hasData()) {
/* 319 */         int bytesWritten = this.outbuf.flush(this.session.channel());
/* 320 */         if (bytesWritten > 0) {
/* 321 */           this.outTransportMetrics.incrementBytesTransferred(bytesWritten);
/*     */         }
/*     */       } 
/* 324 */       if (!this.outbuf.hasData() && 
/* 325 */         this.status == 1) {
/* 326 */         this.session.close();
/* 327 */         this.status = 2;
/* 328 */         resetOutput();
/*     */       }
/*     */     
/* 331 */     } catch (Exception ex) {
/* 332 */       handler.exception((NHttpServerConnection)this, ex);
/*     */     } finally {
/*     */       
/* 335 */       this.hasBufferedOutput = this.outbuf.hasData();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void submitResponse(HttpResponse response) throws IOException, HttpException {
/* 341 */     Args.notNull(response, "HTTP response");
/* 342 */     assertNotClosed();
/* 343 */     if (this.response != null) {
/* 344 */       throw new HttpException("Response already submitted");
/*     */     }
/* 346 */     onResponseSubmitted(response);
/* 347 */     this.responseWriter.write((HttpMessage)response);
/* 348 */     this.hasBufferedOutput = this.outbuf.hasData();
/*     */     
/* 350 */     if (response.getStatusLine().getStatusCode() >= 200) {
/* 351 */       this.connMetrics.incrementResponseCount();
/* 352 */       if (response.getEntity() != null) {
/* 353 */         this.response = response;
/* 354 */         prepareEncoder((HttpMessage)response);
/*     */       } 
/*     */     } 
/*     */     
/* 358 */     this.session.setEvent(4);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isResponseSubmitted() {
/* 363 */     return (this.response != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumeInput(NHttpServiceHandler handler) {
/* 368 */     consumeInput(new NHttpServerEventHandlerAdaptor(handler));
/*     */   }
/*     */ 
/*     */   
/*     */   public void produceOutput(NHttpServiceHandler handler) {
/* 373 */     produceOutput(new NHttpServerEventHandlerAdaptor(handler));
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/DefaultNHttpServerConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
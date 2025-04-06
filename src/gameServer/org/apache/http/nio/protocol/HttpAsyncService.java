/*      */ package org.apache.http.nio.protocol;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.net.SocketTimeoutException;
/*      */ import java.util.Queue;
/*      */ import java.util.concurrent.ConcurrentLinkedQueue;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import org.apache.http.ConnectionReuseStrategy;
/*      */ import org.apache.http.ExceptionLogger;
/*      */ import org.apache.http.HttpEntity;
/*      */ import org.apache.http.HttpEntityEnclosingRequest;
/*      */ import org.apache.http.HttpException;
/*      */ import org.apache.http.HttpRequest;
/*      */ import org.apache.http.HttpResponse;
/*      */ import org.apache.http.HttpResponseFactory;
/*      */ import org.apache.http.HttpVersion;
/*      */ import org.apache.http.ProtocolVersion;
/*      */ import org.apache.http.annotation.Immutable;
/*      */ import org.apache.http.concurrent.Cancellable;
/*      */ import org.apache.http.entity.ContentType;
/*      */ import org.apache.http.impl.DefaultConnectionReuseStrategy;
/*      */ import org.apache.http.impl.DefaultHttpResponseFactory;
/*      */ import org.apache.http.nio.ContentDecoder;
/*      */ import org.apache.http.nio.ContentEncoder;
/*      */ import org.apache.http.nio.IOControl;
/*      */ import org.apache.http.nio.NHttpConnection;
/*      */ import org.apache.http.nio.NHttpServerConnection;
/*      */ import org.apache.http.nio.NHttpServerEventHandler;
/*      */ import org.apache.http.nio.entity.NStringEntity;
/*      */ import org.apache.http.nio.reactor.SessionBufferStatus;
/*      */ import org.apache.http.params.HttpParams;
/*      */ import org.apache.http.protocol.BasicHttpContext;
/*      */ import org.apache.http.protocol.HttpContext;
/*      */ import org.apache.http.protocol.HttpProcessor;
/*      */ import org.apache.http.util.Args;
/*      */ import org.apache.http.util.Asserts;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Immutable
/*      */ public class HttpAsyncService
/*      */   implements NHttpServerEventHandler
/*      */ {
/*      */   static final String HTTP_EXCHANGE_STATE = "http.nio.http-exchange-state";
/*      */   private final HttpProcessor httpProcessor;
/*      */   private final ConnectionReuseStrategy connStrategy;
/*      */   private final HttpResponseFactory responseFactory;
/*      */   private final HttpAsyncRequestHandlerMapper handlerMapper;
/*      */   private final HttpAsyncExpectationVerifier expectationVerifier;
/*      */   private final ExceptionLogger exceptionLogger;
/*      */   
/*      */   @Deprecated
/*      */   public HttpAsyncService(HttpProcessor httpProcessor, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory, HttpAsyncRequestHandlerResolver handlerResolver, HttpAsyncExpectationVerifier expectationVerifier, HttpParams params) {
/*  135 */     this(httpProcessor, connStrategy, responseFactory, new HttpAsyncRequestHandlerResolverAdapter(handlerResolver), expectationVerifier);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public HttpAsyncService(HttpProcessor httpProcessor, ConnectionReuseStrategy connStrategy, HttpAsyncRequestHandlerResolver handlerResolver, HttpParams params) {
/*  160 */     this(httpProcessor, connStrategy, (HttpResponseFactory)DefaultHttpResponseFactory.INSTANCE, new HttpAsyncRequestHandlerResolverAdapter(handlerResolver), null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpAsyncService(HttpProcessor httpProcessor, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory, HttpAsyncRequestHandlerMapper handlerMapper, HttpAsyncExpectationVerifier expectationVerifier) {
/*  186 */     this(httpProcessor, connStrategy, responseFactory, handlerMapper, expectationVerifier, (ExceptionLogger)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpAsyncService(HttpProcessor httpProcessor, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory, HttpAsyncRequestHandlerMapper handlerMapper, HttpAsyncExpectationVerifier expectationVerifier, ExceptionLogger exceptionLogger) {
/*  214 */     this.httpProcessor = (HttpProcessor)Args.notNull(httpProcessor, "HTTP processor");
/*  215 */     this.connStrategy = (connStrategy != null) ? connStrategy : (ConnectionReuseStrategy)DefaultConnectionReuseStrategy.INSTANCE;
/*      */     
/*  217 */     this.responseFactory = (responseFactory != null) ? responseFactory : (HttpResponseFactory)DefaultHttpResponseFactory.INSTANCE;
/*      */     
/*  219 */     this.handlerMapper = handlerMapper;
/*  220 */     this.expectationVerifier = expectationVerifier;
/*  221 */     this.exceptionLogger = (exceptionLogger != null) ? exceptionLogger : ExceptionLogger.NO_OP;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpAsyncService(HttpProcessor httpProcessor, HttpAsyncRequestHandlerMapper handlerMapper) {
/*  235 */     this(httpProcessor, null, null, handlerMapper, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpAsyncService(HttpProcessor httpProcessor, HttpAsyncRequestHandlerMapper handlerMapper, ExceptionLogger exceptionLogger) {
/*  254 */     this(httpProcessor, (ConnectionReuseStrategy)null, (HttpResponseFactory)null, handlerMapper, (HttpAsyncExpectationVerifier)null, exceptionLogger);
/*      */   }
/*      */ 
/*      */   
/*      */   public void connected(NHttpServerConnection conn) {
/*  259 */     State state = new State();
/*  260 */     conn.getContext().setAttribute("http.nio.http-exchange-state", state);
/*      */   }
/*      */ 
/*      */   
/*      */   public void closed(NHttpServerConnection conn) {
/*  265 */     State state = (State)conn.getContext().removeAttribute("http.nio.http-exchange-state");
/*  266 */     if (state != null) {
/*  267 */       state.setTerminated();
/*  268 */       closeHandlers(state);
/*  269 */       Cancellable cancellable = state.getCancellable();
/*  270 */       if (cancellable != null) {
/*  271 */         cancellable.cancel();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void exception(NHttpServerConnection conn, Exception cause) {
/*  279 */     State state = getState((NHttpConnection)conn);
/*  280 */     if (state == null) {
/*  281 */       shutdownConnection((NHttpConnection)conn);
/*  282 */       log(cause);
/*      */       return;
/*      */     } 
/*  285 */     state.setTerminated();
/*  286 */     closeHandlers(state, cause);
/*  287 */     Cancellable cancellable = state.getCancellable();
/*  288 */     if (cancellable != null) {
/*  289 */       cancellable.cancel();
/*      */     }
/*  291 */     Queue<PipelineEntry> pipeline = state.getPipeline();
/*  292 */     if (!pipeline.isEmpty() || conn.isResponseSubmitted() || state.getResponseState().compareTo(MessageState.INIT) > 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  297 */       shutdownConnection((NHttpConnection)conn);
/*      */     } else {
/*      */       try {
/*  300 */         Incoming incoming = state.getIncoming();
/*  301 */         HttpRequest request = (incoming != null) ? incoming.getRequest() : null;
/*  302 */         HttpContext context = (incoming != null) ? incoming.getContext() : (HttpContext)new BasicHttpContext();
/*  303 */         HttpAsyncResponseProducer responseProducer = handleException(cause, context);
/*  304 */         HttpResponse response = responseProducer.generateResponse();
/*  305 */         Outgoing outgoing = new Outgoing(request, response, responseProducer, context);
/*  306 */         state.setResponseState(MessageState.INIT);
/*  307 */         state.setOutgoing(outgoing);
/*  308 */         commitFinalResponse(conn, state);
/*  309 */       } catch (Exception ex) {
/*  310 */         shutdownConnection((NHttpConnection)conn);
/*  311 */         closeHandlers(state);
/*  312 */         if (ex instanceof RuntimeException) {
/*  313 */           throw (RuntimeException)ex;
/*      */         }
/*  315 */         log(ex);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void requestReceived(NHttpServerConnection conn) throws IOException, HttpException {
/*  324 */     State state = getState((NHttpConnection)conn);
/*  325 */     Asserts.notNull(state, "Connection state");
/*  326 */     Asserts.check((state.getRequestState() == MessageState.READY), "Unexpected request state %s", state.getRequestState());
/*      */ 
/*      */     
/*  329 */     HttpRequest request = conn.getHttpRequest();
/*  330 */     BasicHttpContext basicHttpContext = new BasicHttpContext();
/*      */     
/*  332 */     basicHttpContext.setAttribute("http.request", request);
/*  333 */     basicHttpContext.setAttribute("http.connection", conn);
/*  334 */     this.httpProcessor.process(request, (HttpContext)basicHttpContext);
/*      */     
/*  336 */     HttpAsyncRequestHandler<Object> requestHandler = getRequestHandler(request);
/*  337 */     HttpAsyncRequestConsumer<Object> consumer = requestHandler.processRequest(request, (HttpContext)basicHttpContext);
/*  338 */     consumer.requestReceived(request);
/*      */     
/*  340 */     Incoming incoming = new Incoming(request, requestHandler, consumer, (HttpContext)basicHttpContext);
/*  341 */     state.setIncoming(incoming);
/*      */     
/*  343 */     if (request instanceof HttpEntityEnclosingRequest) {
/*      */ 
/*      */ 
/*      */       
/*  347 */       if (((HttpEntityEnclosingRequest)request).expectContinue() && state.getResponseState() == MessageState.READY && state.getPipeline().isEmpty() && (!(conn instanceof SessionBufferStatus) || !((SessionBufferStatus)conn).hasBufferedInput())) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  352 */         state.setRequestState(MessageState.ACK_EXPECTED);
/*  353 */         HttpResponse ack = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_1, 100, (HttpContext)basicHttpContext);
/*      */         
/*  355 */         if (this.expectationVerifier != null) {
/*  356 */           conn.suspendInput();
/*  357 */           conn.suspendOutput();
/*  358 */           HttpAsyncExchange httpAsyncExchange = new HttpAsyncExchangeImpl(request, ack, state, conn, (HttpContext)basicHttpContext);
/*      */           
/*  360 */           this.expectationVerifier.verify(httpAsyncExchange, (HttpContext)basicHttpContext);
/*      */         } else {
/*  362 */           conn.submitResponse(ack);
/*  363 */           state.setRequestState(MessageState.BODY_STREAM);
/*      */         } 
/*      */       } else {
/*  366 */         state.setRequestState(MessageState.BODY_STREAM);
/*      */       } 
/*      */     } else {
/*      */       
/*  370 */       completeRequest(incoming, conn, state);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void inputReady(NHttpServerConnection conn, ContentDecoder decoder) throws IOException, HttpException {
/*  378 */     State state = getState((NHttpConnection)conn);
/*  379 */     Asserts.notNull(state, "Connection state");
/*  380 */     Asserts.check((state.getRequestState() == MessageState.BODY_STREAM), "Unexpected request state %s", state.getRequestState());
/*      */ 
/*      */     
/*  383 */     Incoming incoming = state.getIncoming();
/*  384 */     Asserts.notNull(incoming, "Incoming request");
/*  385 */     HttpAsyncRequestConsumer<?> consumer = incoming.getConsumer();
/*  386 */     consumer.consumeContent(decoder, (IOControl)conn);
/*  387 */     if (decoder.isCompleted()) {
/*  388 */       completeRequest(incoming, conn, state);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void responseReady(NHttpServerConnection conn) throws IOException, HttpException {
/*  395 */     State state = getState((NHttpConnection)conn);
/*  396 */     Asserts.notNull(state, "Connection state");
/*  397 */     Asserts.check((state.getResponseState() == MessageState.READY || state.getResponseState() == MessageState.INIT), "Unexpected response state %s", state.getResponseState());
/*      */ 
/*      */ 
/*      */     
/*  401 */     if (state.getRequestState() == MessageState.ACK_EXPECTED) {
/*  402 */       Outgoing outgoing = state.getOutgoing();
/*  403 */       Asserts.notNull(outgoing, "Outgoing response");
/*      */       
/*  405 */       HttpResponse response = outgoing.getResponse();
/*  406 */       int status = response.getStatusLine().getStatusCode();
/*  407 */       if (status == 100) {
/*  408 */         HttpContext context = outgoing.getContext();
/*  409 */         HttpAsyncResponseProducer responseProducer = outgoing.getProducer();
/*      */         
/*      */         try {
/*  412 */           response.setEntity(null);
/*  413 */           conn.requestInput();
/*  414 */           state.setRequestState(MessageState.BODY_STREAM);
/*  415 */           state.setOutgoing(null);
/*  416 */           conn.submitResponse(response);
/*  417 */           responseProducer.responseCompleted(context);
/*      */         } finally {
/*  419 */           responseProducer.close();
/*      */         } 
/*  421 */       } else if (status >= 400) {
/*  422 */         conn.resetInput();
/*  423 */         state.setRequestState(MessageState.READY);
/*  424 */         commitFinalResponse(conn, state);
/*      */       } else {
/*  426 */         throw new HttpException("Invalid response: " + response.getStatusLine());
/*      */       } 
/*      */     } else {
/*  429 */       if (state.getResponseState() == MessageState.READY) {
/*  430 */         Queue<PipelineEntry> pipeline = state.getPipeline();
/*  431 */         PipelineEntry pipelineEntry = pipeline.poll();
/*  432 */         if (pipelineEntry == null) {
/*  433 */           conn.suspendOutput();
/*      */           return;
/*      */         } 
/*  436 */         state.setResponseState(MessageState.INIT);
/*  437 */         Object result = pipelineEntry.getResult();
/*  438 */         HttpRequest request = pipelineEntry.getRequest();
/*  439 */         HttpContext context = pipelineEntry.getContext();
/*  440 */         if (result != null) {
/*  441 */           HttpResponse response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_1, 200, context);
/*      */           
/*  443 */           HttpAsyncExchangeImpl httpExchange = new HttpAsyncExchangeImpl(request, response, state, conn, context);
/*      */           
/*  445 */           HttpAsyncRequestHandler<Object> handler = pipelineEntry.getHandler();
/*  446 */           conn.suspendOutput();
/*      */           try {
/*  448 */             handler.handle(result, httpExchange, context);
/*  449 */           } catch (RuntimeException ex) {
/*  450 */             throw ex;
/*  451 */           } catch (Exception ex) {
/*  452 */             pipeline.add(new PipelineEntry(request, null, ex, handler, context));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  458 */             state.setResponseState(MessageState.READY);
/*  459 */             responseReady(conn);
/*      */             return;
/*      */           } 
/*      */         } else {
/*  463 */           Exception exception = pipelineEntry.getException();
/*  464 */           HttpAsyncResponseProducer responseProducer = handleException((exception != null) ? exception : (Exception)new HttpException("Internal error processing request"), context);
/*      */ 
/*      */           
/*  467 */           HttpResponse error = responseProducer.generateResponse();
/*  468 */           state.setOutgoing(new Outgoing(request, error, responseProducer, context));
/*      */         } 
/*      */       } 
/*  471 */       if (state.getResponseState() == MessageState.INIT) {
/*      */         Outgoing outgoing;
/*  473 */         synchronized (state) {
/*  474 */           outgoing = state.getOutgoing();
/*  475 */           if (outgoing == null) {
/*  476 */             conn.suspendOutput();
/*      */             return;
/*      */           } 
/*      */         } 
/*  480 */         HttpResponse response = outgoing.getResponse();
/*  481 */         int status = response.getStatusLine().getStatusCode();
/*  482 */         if (status >= 200) {
/*  483 */           commitFinalResponse(conn, state);
/*      */         } else {
/*  485 */           throw new HttpException("Invalid response: " + response.getStatusLine());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void outputReady(NHttpServerConnection conn, ContentEncoder encoder) throws HttpException, IOException {
/*  495 */     State state = getState((NHttpConnection)conn);
/*  496 */     Asserts.notNull(state, "Connection state");
/*  497 */     Asserts.check((state.getResponseState() == MessageState.BODY_STREAM), "Unexpected response state %s", state.getResponseState());
/*      */ 
/*      */     
/*  500 */     Outgoing outgoing = state.getOutgoing();
/*  501 */     Asserts.notNull(outgoing, "Outgoing response");
/*  502 */     HttpAsyncResponseProducer responseProducer = outgoing.getProducer();
/*      */     
/*  504 */     responseProducer.produceContent(encoder, (IOControl)conn);
/*      */     
/*  506 */     if (encoder.isCompleted()) {
/*  507 */       completeResponse(outgoing, conn, state);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endOfInput(NHttpServerConnection conn) throws IOException {
/*  517 */     if (conn.getSocketTimeout() <= 0) {
/*  518 */       conn.setSocketTimeout(1000);
/*      */     }
/*  520 */     conn.close();
/*      */   }
/*      */ 
/*      */   
/*      */   public void timeout(NHttpServerConnection conn) throws IOException {
/*  525 */     State state = getState((NHttpConnection)conn);
/*  526 */     if (state != null) {
/*  527 */       closeHandlers(state, new SocketTimeoutException());
/*      */     }
/*  529 */     if (conn.getStatus() == 0) {
/*  530 */       conn.close();
/*  531 */       if (conn.getStatus() == 1)
/*      */       {
/*      */         
/*  534 */         conn.setSocketTimeout(250);
/*      */       }
/*      */     } else {
/*  537 */       conn.shutdown();
/*      */     } 
/*      */   }
/*      */   
/*      */   private State getState(NHttpConnection conn) {
/*  542 */     return (State)conn.getContext().getAttribute("http.nio.http-exchange-state");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void log(Exception ex) {
/*  553 */     this.exceptionLogger.log(ex);
/*      */   }
/*      */   
/*      */   private void shutdownConnection(NHttpConnection conn) {
/*      */     try {
/*  558 */       conn.shutdown();
/*  559 */     } catch (IOException ex) {
/*  560 */       log(ex);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void closeHandlers(State state, Exception ex) {
/*  565 */     HttpAsyncRequestConsumer<Object> consumer = (state.getIncoming() != null) ? state.getIncoming().getConsumer() : null;
/*      */     
/*  567 */     if (consumer != null) {
/*      */       try {
/*  569 */         consumer.failed(ex);
/*      */       } finally {
/*      */         try {
/*  572 */           consumer.close();
/*  573 */         } catch (IOException ioex) {
/*  574 */           log(ioex);
/*      */         } 
/*      */       } 
/*      */     }
/*  578 */     HttpAsyncResponseProducer producer = (state.getOutgoing() != null) ? state.getOutgoing().getProducer() : null;
/*      */     
/*  580 */     if (producer != null) {
/*      */       try {
/*  582 */         producer.failed(ex);
/*      */       } finally {
/*      */         try {
/*  585 */           producer.close();
/*  586 */         } catch (IOException ioex) {
/*  587 */           log(ioex);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private void closeHandlers(State state) {
/*  594 */     HttpAsyncRequestConsumer<Object> consumer = (state.getIncoming() != null) ? state.getIncoming().getConsumer() : null;
/*      */     
/*  596 */     if (consumer != null) {
/*      */       try {
/*  598 */         consumer.close();
/*  599 */       } catch (IOException ioex) {
/*  600 */         log(ioex);
/*      */       } 
/*      */     }
/*  603 */     HttpAsyncResponseProducer producer = (state.getOutgoing() != null) ? state.getOutgoing().getProducer() : null;
/*      */     
/*  605 */     if (producer != null) {
/*      */       try {
/*  607 */         producer.close();
/*  608 */       } catch (IOException ioex) {
/*  609 */         log(ioex);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected HttpAsyncResponseProducer handleException(Exception ex, HttpContext context) {
/*      */     int code;
/*  617 */     if (ex instanceof org.apache.http.MethodNotSupportedException) {
/*  618 */       code = 501;
/*  619 */     } else if (ex instanceof org.apache.http.UnsupportedHttpVersionException) {
/*  620 */       code = 505;
/*  621 */     } else if (ex instanceof org.apache.http.ProtocolException) {
/*  622 */       code = 400;
/*      */     } else {
/*  624 */       code = 500;
/*      */     } 
/*  626 */     String message = ex.getMessage();
/*  627 */     if (message == null) {
/*  628 */       message = ex.toString();
/*      */     }
/*  630 */     HttpResponse response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_1, code, context);
/*      */     
/*  632 */     return new ErrorResponseProducer(response, (HttpEntity)new NStringEntity(message, ContentType.DEFAULT_TEXT), false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleAlreadySubmittedResponse(Cancellable cancellable, HttpContext context) {
/*  647 */     throw new IllegalStateException("Response already submitted");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleAlreadySubmittedResponse(HttpAsyncResponseProducer responseProducer, HttpContext context) {
/*  661 */     throw new IllegalStateException("Response already submitted");
/*      */   }
/*      */   
/*      */   private boolean canResponseHaveBody(HttpRequest request, HttpResponse response) {
/*  665 */     if (request != null && "HEAD".equalsIgnoreCase(request.getRequestLine().getMethod())) {
/*  666 */       return false;
/*      */     }
/*  668 */     int status = response.getStatusLine().getStatusCode();
/*  669 */     return (status >= 200 && status != 204 && status != 304 && status != 205);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void completeRequest(Incoming incoming, NHttpServerConnection conn, State state) throws IOException, HttpException {
/*      */     PipelineEntry pipelineEntry;
/*  679 */     state.setRequestState(MessageState.READY);
/*  680 */     state.setIncoming(null);
/*      */ 
/*      */     
/*  683 */     HttpAsyncRequestConsumer<?> consumer = incoming.getConsumer();
/*      */     try {
/*  685 */       HttpContext context = incoming.getContext();
/*  686 */       consumer.requestCompleted(context);
/*  687 */       pipelineEntry = new PipelineEntry(incoming.getRequest(), consumer.getResult(), consumer.getException(), incoming.getHandler(), context);
/*      */ 
/*      */     
/*      */     }
/*      */     finally {
/*      */ 
/*      */       
/*  694 */       consumer.close();
/*      */     } 
/*  696 */     Queue<PipelineEntry> pipeline = state.getPipeline();
/*  697 */     pipeline.add(pipelineEntry);
/*  698 */     if (state.getResponseState() == MessageState.READY) {
/*  699 */       conn.requestOutput();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void commitFinalResponse(NHttpServerConnection conn, State state) throws IOException, HttpException {
/*  706 */     Outgoing outgoing = state.getOutgoing();
/*  707 */     Asserts.notNull(outgoing, "Outgoing response");
/*  708 */     HttpRequest request = outgoing.getRequest();
/*  709 */     HttpResponse response = outgoing.getResponse();
/*  710 */     HttpContext context = outgoing.getContext();
/*      */     
/*  712 */     context.setAttribute("http.response", response);
/*  713 */     this.httpProcessor.process(response, context);
/*      */     
/*  715 */     HttpEntity entity = response.getEntity();
/*  716 */     if (entity != null && !canResponseHaveBody(request, response)) {
/*  717 */       response.setEntity(null);
/*  718 */       entity = null;
/*      */     } 
/*      */     
/*  721 */     conn.submitResponse(response);
/*      */     
/*  723 */     if (entity == null) {
/*  724 */       completeResponse(outgoing, conn, state);
/*      */     } else {
/*  726 */       state.setResponseState(MessageState.BODY_STREAM);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void completeResponse(Outgoing outgoing, NHttpServerConnection conn, State state) throws IOException, HttpException {
/*  734 */     HttpContext context = outgoing.getContext();
/*  735 */     HttpResponse response = outgoing.getResponse();
/*  736 */     HttpAsyncResponseProducer responseProducer = outgoing.getProducer();
/*      */     try {
/*  738 */       responseProducer.responseCompleted(context);
/*  739 */       state.setOutgoing(null);
/*  740 */       state.setCancellable(null);
/*  741 */       state.setResponseState(MessageState.READY);
/*      */     } finally {
/*  743 */       responseProducer.close();
/*      */     } 
/*  745 */     if (!this.connStrategy.keepAlive(response, context)) {
/*  746 */       conn.close();
/*      */     } else {
/*      */       
/*  749 */       Queue<PipelineEntry> pipeline = state.getPipeline();
/*  750 */       if (pipeline.isEmpty()) {
/*  751 */         conn.suspendOutput();
/*      */       }
/*  753 */       conn.requestInput();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private HttpAsyncRequestHandler<Object> getRequestHandler(HttpRequest request) {
/*  759 */     HttpAsyncRequestHandler<Object> handler = null;
/*  760 */     if (this.handlerMapper != null) {
/*  761 */       handler = (HttpAsyncRequestHandler)this.handlerMapper.lookup(request);
/*      */     }
/*  763 */     if (handler == null) {
/*  764 */       handler = new NullRequestHandler();
/*      */     }
/*  766 */     return handler;
/*      */   }
/*      */ 
/*      */   
/*      */   static class Incoming
/*      */   {
/*      */     private final HttpRequest request;
/*      */     
/*      */     private final HttpAsyncRequestHandler<Object> handler;
/*      */     
/*      */     private final HttpAsyncRequestConsumer<Object> consumer;
/*      */     
/*      */     private final HttpContext context;
/*      */     
/*      */     Incoming(HttpRequest request, HttpAsyncRequestHandler<Object> handler, HttpAsyncRequestConsumer<Object> consumer, HttpContext context) {
/*  781 */       this.request = request;
/*  782 */       this.handler = handler;
/*  783 */       this.consumer = consumer;
/*  784 */       this.context = context;
/*      */     }
/*      */     
/*      */     public HttpRequest getRequest() {
/*  788 */       return this.request;
/*      */     }
/*      */     
/*      */     public HttpAsyncRequestHandler<Object> getHandler() {
/*  792 */       return this.handler;
/*      */     }
/*      */     
/*      */     public HttpAsyncRequestConsumer<Object> getConsumer() {
/*  796 */       return this.consumer;
/*      */     }
/*      */     
/*      */     public HttpContext getContext() {
/*  800 */       return this.context;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class Outgoing
/*      */   {
/*      */     private final HttpRequest request;
/*      */     
/*      */     private final HttpResponse response;
/*      */     
/*      */     private final HttpAsyncResponseProducer producer;
/*      */     
/*      */     private final HttpContext context;
/*      */     
/*      */     Outgoing(HttpRequest request, HttpResponse response, HttpAsyncResponseProducer producer, HttpContext context) {
/*  816 */       this.request = request;
/*  817 */       this.response = response;
/*  818 */       this.producer = producer;
/*  819 */       this.context = context;
/*      */     }
/*      */     
/*      */     public HttpRequest getRequest() {
/*  823 */       return this.request;
/*      */     }
/*      */     
/*      */     public HttpResponse getResponse() {
/*  827 */       return this.response;
/*      */     }
/*      */     
/*      */     public HttpAsyncResponseProducer getProducer() {
/*  831 */       return this.producer;
/*      */     }
/*      */     
/*      */     public HttpContext getContext() {
/*  835 */       return this.context;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class PipelineEntry
/*      */   {
/*      */     private final HttpRequest request;
/*      */     
/*      */     private final Object result;
/*      */     
/*      */     private final Exception exception;
/*      */     
/*      */     private final HttpAsyncRequestHandler<Object> handler;
/*      */     
/*      */     private final HttpContext context;
/*      */     
/*      */     PipelineEntry(HttpRequest request, Object result, Exception exception, HttpAsyncRequestHandler<Object> handler, HttpContext context) {
/*  853 */       this.request = request;
/*  854 */       this.result = result;
/*  855 */       this.exception = exception;
/*  856 */       this.handler = handler;
/*  857 */       this.context = context;
/*      */     }
/*      */     
/*      */     public HttpRequest getRequest() {
/*  861 */       return this.request;
/*      */     }
/*      */     
/*      */     public Object getResult() {
/*  865 */       return this.result;
/*      */     }
/*      */     
/*      */     public Exception getException() {
/*  869 */       return this.exception;
/*      */     }
/*      */     
/*      */     public HttpAsyncRequestHandler<Object> getHandler() {
/*  873 */       return this.handler;
/*      */     }
/*      */     
/*      */     public HttpContext getContext() {
/*  877 */       return this.context;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class State
/*      */   {
/*  894 */     private final Queue<HttpAsyncService.PipelineEntry> pipeline = new ConcurrentLinkedQueue<HttpAsyncService.PipelineEntry>(); private volatile boolean terminated;
/*  895 */     private volatile MessageState requestState = MessageState.READY;
/*  896 */     private volatile MessageState responseState = MessageState.READY;
/*      */     private volatile HttpAsyncService.Incoming incoming;
/*      */     
/*      */     public boolean isTerminated() {
/*  900 */       return this.terminated;
/*      */     }
/*      */     private volatile HttpAsyncService.Outgoing outgoing; private volatile Cancellable cancellable;
/*      */     public void setTerminated() {
/*  904 */       this.terminated = true;
/*      */     }
/*      */     
/*      */     public MessageState getRequestState() {
/*  908 */       return this.requestState;
/*      */     }
/*      */     
/*      */     public void setRequestState(MessageState state) {
/*  912 */       this.requestState = state;
/*      */     }
/*      */     
/*      */     public MessageState getResponseState() {
/*  916 */       return this.responseState;
/*      */     }
/*      */     
/*      */     public void setResponseState(MessageState state) {
/*  920 */       this.responseState = state;
/*      */     }
/*      */     
/*      */     public HttpAsyncService.Incoming getIncoming() {
/*  924 */       return this.incoming;
/*      */     }
/*      */     
/*      */     public void setIncoming(HttpAsyncService.Incoming incoming) {
/*  928 */       this.incoming = incoming;
/*      */     }
/*      */     
/*      */     public HttpAsyncService.Outgoing getOutgoing() {
/*  932 */       return this.outgoing;
/*      */     }
/*      */     
/*      */     public void setOutgoing(HttpAsyncService.Outgoing outgoing) {
/*  936 */       this.outgoing = outgoing;
/*      */     }
/*      */     
/*      */     public Cancellable getCancellable() {
/*  940 */       return this.cancellable;
/*      */     }
/*      */     
/*      */     public void setCancellable(Cancellable cancellable) {
/*  944 */       this.cancellable = cancellable;
/*      */     }
/*      */     
/*      */     public Queue<HttpAsyncService.PipelineEntry> getPipeline() {
/*  948 */       return this.pipeline;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  953 */       StringBuilder buf = new StringBuilder();
/*  954 */       buf.append("[incoming ");
/*  955 */       buf.append(this.requestState);
/*  956 */       if (this.incoming != null) {
/*  957 */         buf.append(" ");
/*  958 */         buf.append(this.incoming.getRequest().getRequestLine());
/*      */       } 
/*  960 */       buf.append("; outgoing ");
/*  961 */       buf.append(this.responseState);
/*  962 */       if (this.outgoing != null) {
/*  963 */         buf.append(" ");
/*  964 */         buf.append(this.outgoing.getResponse().getStatusLine());
/*      */       } 
/*  966 */       buf.append("]");
/*  967 */       return buf.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   class HttpAsyncExchangeImpl
/*      */     implements HttpAsyncExchange
/*      */   {
/*  974 */     private final AtomicBoolean completed = new AtomicBoolean();
/*      */     
/*      */     private final HttpRequest request;
/*      */     
/*      */     private final HttpResponse response;
/*      */     
/*      */     private final HttpAsyncService.State state;
/*      */     
/*      */     private final NHttpServerConnection conn;
/*      */     
/*      */     private final HttpContext context;
/*      */ 
/*      */     
/*      */     public HttpAsyncExchangeImpl(HttpRequest request, HttpResponse response, HttpAsyncService.State state, NHttpServerConnection conn, HttpContext context) {
/*  988 */       this.request = request;
/*  989 */       this.response = response;
/*  990 */       this.state = state;
/*  991 */       this.conn = conn;
/*  992 */       this.context = context;
/*      */     }
/*      */ 
/*      */     
/*      */     public HttpRequest getRequest() {
/*  997 */       return this.request;
/*      */     }
/*      */ 
/*      */     
/*      */     public HttpResponse getResponse() {
/* 1002 */       return this.response;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setCallback(Cancellable cancellable) {
/* 1007 */       if (this.completed.get()) {
/* 1008 */         HttpAsyncService.this.handleAlreadySubmittedResponse(cancellable, this.context);
/* 1009 */       } else if (this.state.isTerminated() && cancellable != null) {
/* 1010 */         cancellable.cancel();
/*      */       } else {
/* 1012 */         this.state.setCancellable(cancellable);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void submitResponse(HttpAsyncResponseProducer responseProducer) {
/* 1018 */       Args.notNull(responseProducer, "Response producer");
/* 1019 */       if (this.completed.getAndSet(true)) {
/* 1020 */         HttpAsyncService.this.handleAlreadySubmittedResponse(responseProducer, this.context);
/* 1021 */       } else if (!this.state.isTerminated()) {
/* 1022 */         HttpResponse response = responseProducer.generateResponse();
/* 1023 */         HttpAsyncService.Outgoing outgoing = new HttpAsyncService.Outgoing(this.request, response, responseProducer, this.context);
/*      */ 
/*      */         
/* 1026 */         synchronized (this.state) {
/* 1027 */           this.state.setOutgoing(outgoing);
/* 1028 */           this.state.setCancellable(null);
/* 1029 */           this.conn.requestOutput();
/*      */         } 
/*      */       } else {
/*      */         
/*      */         try {
/* 1034 */           responseProducer.close();
/* 1035 */         } catch (IOException ex) {
/* 1036 */           HttpAsyncService.this.log(ex);
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void submitResponse() {
/* 1043 */       submitResponse(new BasicAsyncResponseProducer(this.response));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isCompleted() {
/* 1048 */       return this.completed.get();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setTimeout(int timeout) {
/* 1053 */       this.conn.setSocketTimeout(timeout);
/*      */     }
/*      */ 
/*      */     
/*      */     public int getTimeout() {
/* 1058 */       return this.conn.getSocketTimeout();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   private static class HttpAsyncRequestHandlerResolverAdapter
/*      */     implements HttpAsyncRequestHandlerMapper
/*      */   {
/*      */     private final HttpAsyncRequestHandlerResolver resolver;
/*      */ 
/*      */     
/*      */     public HttpAsyncRequestHandlerResolverAdapter(HttpAsyncRequestHandlerResolver resolver) {
/* 1072 */       this.resolver = resolver;
/*      */     }
/*      */ 
/*      */     
/*      */     public HttpAsyncRequestHandler<?> lookup(HttpRequest request) {
/* 1077 */       return this.resolver.lookup(request.getRequestLine().getUri());
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/protocol/HttpAsyncService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
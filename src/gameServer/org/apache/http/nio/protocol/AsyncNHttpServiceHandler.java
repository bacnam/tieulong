/*     */ package org.apache.http.nio.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.nio.ContentDecoder;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.IOControl;
/*     */ import org.apache.http.nio.NHttpConnection;
/*     */ import org.apache.http.nio.NHttpServerConnection;
/*     */ import org.apache.http.nio.NHttpServiceHandler;
/*     */ import org.apache.http.nio.entity.ConsumingNHttpEntity;
/*     */ import org.apache.http.nio.entity.NByteArrayEntity;
/*     */ import org.apache.http.nio.entity.NHttpEntityWrapper;
/*     */ import org.apache.http.nio.entity.ProducingNHttpEntity;
/*     */ import org.apache.http.nio.util.ByteBufferAllocator;
/*     */ import org.apache.http.nio.util.HeapByteBufferAllocator;
/*     */ import org.apache.http.params.DefaultedHttpParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.protocol.HttpExpectationVerifier;
/*     */ import org.apache.http.protocol.HttpProcessor;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
/*     */ import org.apache.http.util.EncodingUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public class AsyncNHttpServiceHandler
/*     */   extends NHttpHandlerBase
/*     */   implements NHttpServiceHandler
/*     */ {
/*     */   protected final HttpResponseFactory responseFactory;
/*     */   protected NHttpRequestHandlerResolver handlerResolver;
/*     */   protected HttpExpectationVerifier expectationVerifier;
/*     */   
/*     */   public AsyncNHttpServiceHandler(HttpProcessor httpProcessor, HttpResponseFactory responseFactory, ConnectionReuseStrategy connStrategy, ByteBufferAllocator allocator, HttpParams params) {
/* 118 */     super(httpProcessor, connStrategy, allocator, params);
/* 119 */     Args.notNull(responseFactory, "Response factory");
/* 120 */     this.responseFactory = responseFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncNHttpServiceHandler(HttpProcessor httpProcessor, HttpResponseFactory responseFactory, ConnectionReuseStrategy connStrategy, HttpParams params) {
/* 128 */     this(httpProcessor, responseFactory, connStrategy, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE, params);
/*     */   }
/*     */   
/*     */   public void setExpectationVerifier(HttpExpectationVerifier expectationVerifier) {
/* 132 */     this.expectationVerifier = expectationVerifier;
/*     */   }
/*     */   
/*     */   public void setHandlerResolver(NHttpRequestHandlerResolver handlerResolver) {
/* 136 */     this.handlerResolver = handlerResolver;
/*     */   }
/*     */   
/*     */   public void connected(NHttpServerConnection conn) {
/* 140 */     HttpContext context = conn.getContext();
/*     */     
/* 142 */     ServerConnState connState = new ServerConnState();
/* 143 */     context.setAttribute("http.nio.conn-state", connState);
/* 144 */     context.setAttribute("http.connection", conn);
/*     */     
/* 146 */     if (this.eventListener != null)
/* 147 */       this.eventListener.connectionOpen((NHttpConnection)conn); 
/*     */   }
/*     */   
/*     */   public void requestReceived(NHttpServerConnection conn) {
/*     */     HttpVersion httpVersion;
/* 152 */     HttpContext context = conn.getContext();
/*     */     
/* 154 */     ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");
/*     */     
/* 156 */     HttpRequest request = conn.getHttpRequest();
/* 157 */     request.setParams((HttpParams)new DefaultedHttpParams(request.getParams(), this.params));
/*     */     
/* 159 */     connState.setRequest(request);
/*     */     
/* 161 */     NHttpRequestHandler requestHandler = getRequestHandler(request);
/* 162 */     connState.setRequestHandler(requestHandler);
/*     */     
/* 164 */     ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
/* 165 */     if (!ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_1))
/*     */     {
/* 167 */       httpVersion = HttpVersion.HTTP_1_1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 174 */       if (request instanceof HttpEntityEnclosingRequest) {
/* 175 */         HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest)request;
/* 176 */         if (entityRequest.expectContinue()) {
/* 177 */           HttpResponse response = this.responseFactory.newHttpResponse((ProtocolVersion)httpVersion, 100, context);
/*     */           
/* 179 */           response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));
/*     */ 
/*     */           
/* 182 */           if (this.expectationVerifier != null) {
/*     */             try {
/* 184 */               this.expectationVerifier.verify(request, response, context);
/* 185 */             } catch (HttpException ex) {
/* 186 */               response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_0, 500, context);
/*     */ 
/*     */ 
/*     */               
/* 190 */               response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));
/*     */               
/* 192 */               handleException(ex, response);
/*     */             } 
/*     */           }
/*     */           
/* 196 */           if (response.getStatusLine().getStatusCode() < 200) {
/*     */ 
/*     */             
/* 199 */             conn.submitResponse(response);
/*     */           } else {
/* 201 */             conn.resetInput();
/* 202 */             sendResponse(conn, request, response);
/*     */           } 
/*     */         } 
/*     */         
/* 206 */         ConsumingNHttpEntity consumingEntity = null;
/*     */ 
/*     */         
/* 209 */         if (requestHandler != null) {
/* 210 */           consumingEntity = requestHandler.entityRequest(entityRequest, context);
/*     */         }
/* 212 */         if (consumingEntity == null) {
/* 213 */           consumingEntity = new NullNHttpEntity(entityRequest.getEntity());
/*     */         }
/* 215 */         entityRequest.setEntity((HttpEntity)consumingEntity);
/* 216 */         connState.setConsumingEntity(consumingEntity);
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 221 */         conn.suspendInput();
/* 222 */         processRequest(conn, request);
/*     */       }
/*     */     
/* 225 */     } catch (IOException ex) {
/* 226 */       shutdownConnection((NHttpConnection)conn, ex);
/* 227 */       if (this.eventListener != null) {
/* 228 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/* 230 */     } catch (HttpException ex) {
/* 231 */       closeConnection((NHttpConnection)conn, (Throwable)ex);
/* 232 */       if (this.eventListener != null) {
/* 233 */         this.eventListener.fatalProtocolException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void closed(NHttpServerConnection conn) {
/* 240 */     HttpContext context = conn.getContext();
/*     */     
/* 242 */     ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");
/*     */     try {
/* 244 */       connState.reset();
/* 245 */     } catch (IOException ex) {
/* 246 */       if (this.eventListener != null) {
/* 247 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/* 250 */     if (this.eventListener != null) {
/* 251 */       this.eventListener.connectionClosed((NHttpConnection)conn);
/*     */     }
/*     */   }
/*     */   
/*     */   public void exception(NHttpServerConnection conn, HttpException httpex) {
/* 256 */     if (conn.isResponseSubmitted()) {
/*     */ 
/*     */       
/* 259 */       closeConnection((NHttpConnection)conn, (Throwable)httpex);
/* 260 */       if (this.eventListener != null) {
/* 261 */         this.eventListener.fatalProtocolException(httpex, (NHttpConnection)conn);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 266 */     HttpContext context = conn.getContext();
/*     */     try {
/* 268 */       HttpResponse response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_0, 500, context);
/*     */       
/* 270 */       response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));
/*     */       
/* 272 */       handleException(httpex, response);
/* 273 */       response.setEntity(null);
/* 274 */       sendResponse(conn, (HttpRequest)null, response);
/*     */     }
/* 276 */     catch (IOException ex) {
/* 277 */       shutdownConnection((NHttpConnection)conn, ex);
/* 278 */       if (this.eventListener != null) {
/* 279 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/* 281 */     } catch (HttpException ex) {
/* 282 */       closeConnection((NHttpConnection)conn, (Throwable)ex);
/* 283 */       if (this.eventListener != null) {
/* 284 */         this.eventListener.fatalProtocolException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void exception(NHttpServerConnection conn, IOException ex) {
/* 290 */     shutdownConnection((NHttpConnection)conn, ex);
/*     */     
/* 292 */     if (this.eventListener != null) {
/* 293 */       this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */     }
/*     */   }
/*     */   
/*     */   public void timeout(NHttpServerConnection conn) {
/* 298 */     handleTimeout((NHttpConnection)conn);
/*     */   }
/*     */   
/*     */   public void inputReady(NHttpServerConnection conn, ContentDecoder decoder) {
/* 302 */     HttpContext context = conn.getContext();
/* 303 */     ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");
/*     */     
/* 305 */     HttpRequest request = connState.getRequest();
/* 306 */     ConsumingNHttpEntity consumingEntity = connState.getConsumingEntity();
/*     */ 
/*     */     
/*     */     try {
/* 310 */       consumingEntity.consumeContent(decoder, (IOControl)conn);
/* 311 */       if (decoder.isCompleted()) {
/* 312 */         conn.suspendInput();
/* 313 */         processRequest(conn, request);
/*     */       }
/*     */     
/* 316 */     } catch (IOException ex) {
/* 317 */       shutdownConnection((NHttpConnection)conn, ex);
/* 318 */       if (this.eventListener != null) {
/* 319 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/* 321 */     } catch (HttpException ex) {
/* 322 */       closeConnection((NHttpConnection)conn, (Throwable)ex);
/* 323 */       if (this.eventListener != null) {
/* 324 */         this.eventListener.fatalProtocolException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void responseReady(NHttpServerConnection conn) {
/* 330 */     HttpContext context = conn.getContext();
/* 331 */     ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");
/*     */     
/* 333 */     if (connState.isHandled()) {
/*     */       return;
/*     */     }
/*     */     
/* 337 */     HttpRequest request = connState.getRequest();
/*     */ 
/*     */     
/*     */     try {
/* 341 */       IOException ioex = connState.getIOException();
/* 342 */       if (ioex != null) {
/* 343 */         throw ioex;
/*     */       }
/*     */       
/* 346 */       HttpException httpex = connState.getHttpException();
/* 347 */       if (httpex != null) {
/* 348 */         HttpResponse httpResponse = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_0, 500, context);
/*     */         
/* 350 */         httpResponse.setParams((HttpParams)new DefaultedHttpParams(httpResponse.getParams(), this.params));
/*     */         
/* 352 */         handleException(httpex, httpResponse);
/* 353 */         connState.setResponse(httpResponse);
/*     */       } 
/*     */       
/* 356 */       HttpResponse response = connState.getResponse();
/* 357 */       if (response != null) {
/* 358 */         connState.setHandled(true);
/* 359 */         sendResponse(conn, request, response);
/*     */       }
/*     */     
/* 362 */     } catch (IOException ex) {
/* 363 */       shutdownConnection((NHttpConnection)conn, ex);
/* 364 */       if (this.eventListener != null) {
/* 365 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/* 367 */     } catch (HttpException ex) {
/* 368 */       closeConnection((NHttpConnection)conn, (Throwable)ex);
/* 369 */       if (this.eventListener != null) {
/* 370 */         this.eventListener.fatalProtocolException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void outputReady(NHttpServerConnection conn, ContentEncoder encoder) {
/* 376 */     HttpContext context = conn.getContext();
/* 377 */     ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");
/*     */     
/* 379 */     HttpResponse response = conn.getHttpResponse();
/*     */     
/*     */     try {
/* 382 */       ProducingNHttpEntity entity = connState.getProducingEntity();
/* 383 */       entity.produceContent(encoder, (IOControl)conn);
/*     */       
/* 385 */       if (encoder.isCompleted()) {
/* 386 */         connState.finishOutput();
/* 387 */         if (!this.connStrategy.keepAlive(response, context)) {
/* 388 */           conn.close();
/*     */         } else {
/*     */           
/* 391 */           connState.reset();
/* 392 */           conn.requestInput();
/*     */         } 
/* 394 */         responseComplete(response, context);
/*     */       }
/*     */     
/* 397 */     } catch (IOException ex) {
/* 398 */       shutdownConnection((NHttpConnection)conn, ex);
/* 399 */       if (this.eventListener != null) {
/* 400 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleException(HttpException ex, HttpResponse response) {
/* 406 */     int code = 500;
/* 407 */     if (ex instanceof org.apache.http.MethodNotSupportedException) {
/* 408 */       code = 501;
/* 409 */     } else if (ex instanceof org.apache.http.UnsupportedHttpVersionException) {
/* 410 */       code = 505;
/* 411 */     } else if (ex instanceof org.apache.http.ProtocolException) {
/* 412 */       code = 400;
/*     */     } 
/* 414 */     response.setStatusCode(code);
/*     */     
/* 416 */     byte[] msg = EncodingUtils.getAsciiBytes(ex.getMessage());
/* 417 */     NByteArrayEntity entity = new NByteArrayEntity(msg);
/* 418 */     entity.setContentType("text/plain; charset=US-ASCII");
/* 419 */     response.setEntity((HttpEntity)entity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processRequest(NHttpServerConnection conn, HttpRequest request) throws IOException, HttpException {
/*     */     HttpVersion httpVersion;
/* 429 */     HttpContext context = conn.getContext();
/* 430 */     ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");
/*     */     
/* 432 */     ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
/*     */     
/* 434 */     if (!ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_1))
/*     */     {
/* 436 */       httpVersion = HttpVersion.HTTP_1_1;
/*     */     }
/*     */     
/* 439 */     NHttpResponseTrigger trigger = new ResponseTriggerImpl(connState, (IOControl)conn);
/*     */     try {
/* 441 */       this.httpProcessor.process(request, context);
/*     */       
/* 443 */       NHttpRequestHandler handler = connState.getRequestHandler();
/* 444 */       if (handler != null) {
/* 445 */         HttpResponse response = this.responseFactory.newHttpResponse((ProtocolVersion)httpVersion, 200, context);
/*     */         
/* 447 */         response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));
/*     */ 
/*     */         
/* 450 */         handler.handle(request, response, trigger, context);
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 456 */         HttpResponse response = this.responseFactory.newHttpResponse((ProtocolVersion)httpVersion, 501, context);
/*     */         
/* 458 */         response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));
/*     */         
/* 460 */         trigger.submitResponse(response);
/*     */       }
/*     */     
/* 463 */     } catch (HttpException ex) {
/* 464 */       trigger.handleException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendResponse(NHttpServerConnection conn, HttpRequest request, HttpResponse response) throws IOException, HttpException {
/* 472 */     HttpContext context = conn.getContext();
/* 473 */     ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");
/*     */ 
/*     */     
/* 476 */     connState.finishInput();
/*     */ 
/*     */     
/* 479 */     context.setAttribute("http.request", request);
/* 480 */     this.httpProcessor.process(response, context);
/* 481 */     context.setAttribute("http.request", null);
/*     */     
/* 483 */     if (response.getEntity() != null && !canResponseHaveBody(request, response)) {
/* 484 */       response.setEntity(null);
/*     */     }
/*     */     
/* 487 */     HttpEntity entity = response.getEntity();
/* 488 */     if (entity != null) {
/* 489 */       if (entity instanceof ProducingNHttpEntity) {
/* 490 */         connState.setProducingEntity((ProducingNHttpEntity)entity);
/*     */       } else {
/* 492 */         connState.setProducingEntity((ProducingNHttpEntity)new NHttpEntityWrapper(entity));
/*     */       } 
/*     */     }
/*     */     
/* 496 */     conn.submitResponse(response);
/*     */     
/* 498 */     if (entity == null) {
/* 499 */       if (!this.connStrategy.keepAlive(response, context)) {
/* 500 */         conn.close();
/*     */       } else {
/*     */         
/* 503 */         connState.reset();
/* 504 */         conn.requestInput();
/*     */       } 
/* 506 */       responseComplete(response, context);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void responseComplete(HttpResponse response, HttpContext context) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NHttpRequestHandler getRequestHandler(HttpRequest request) {
/* 520 */     NHttpRequestHandler handler = null;
/* 521 */     if (this.handlerResolver != null) {
/* 522 */       String requestURI = request.getRequestLine().getUri();
/* 523 */       handler = this.handlerResolver.lookup(requestURI);
/*     */     } 
/*     */     
/* 526 */     return handler;
/*     */   }
/*     */   
/*     */   protected static class ServerConnState
/*     */   {
/*     */     private volatile NHttpRequestHandler requestHandler;
/*     */     private volatile HttpRequest request;
/*     */     private volatile ConsumingNHttpEntity consumingEntity;
/*     */     private volatile HttpResponse response;
/*     */     private volatile ProducingNHttpEntity producingEntity;
/*     */     private volatile IOException ioex;
/*     */     private volatile HttpException httpex;
/*     */     private volatile boolean handled;
/*     */     
/*     */     public void finishInput() throws IOException {
/* 541 */       if (this.consumingEntity != null) {
/* 542 */         this.consumingEntity.finish();
/* 543 */         this.consumingEntity = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void finishOutput() throws IOException {
/* 548 */       if (this.producingEntity != null) {
/* 549 */         this.producingEntity.finish();
/* 550 */         this.producingEntity = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void reset() throws IOException {
/* 555 */       finishInput();
/* 556 */       this.request = null;
/* 557 */       finishOutput();
/* 558 */       this.handled = false;
/* 559 */       this.response = null;
/* 560 */       this.ioex = null;
/* 561 */       this.httpex = null;
/* 562 */       this.requestHandler = null;
/*     */     }
/*     */     
/*     */     public NHttpRequestHandler getRequestHandler() {
/* 566 */       return this.requestHandler;
/*     */     }
/*     */     
/*     */     public void setRequestHandler(NHttpRequestHandler requestHandler) {
/* 570 */       this.requestHandler = requestHandler;
/*     */     }
/*     */     
/*     */     public HttpRequest getRequest() {
/* 574 */       return this.request;
/*     */     }
/*     */     
/*     */     public void setRequest(HttpRequest request) {
/* 578 */       this.request = request;
/*     */     }
/*     */     
/*     */     public ConsumingNHttpEntity getConsumingEntity() {
/* 582 */       return this.consumingEntity;
/*     */     }
/*     */     
/*     */     public void setConsumingEntity(ConsumingNHttpEntity consumingEntity) {
/* 586 */       this.consumingEntity = consumingEntity;
/*     */     }
/*     */     
/*     */     public HttpResponse getResponse() {
/* 590 */       return this.response;
/*     */     }
/*     */     
/*     */     public void setResponse(HttpResponse response) {
/* 594 */       this.response = response;
/*     */     }
/*     */     
/*     */     public ProducingNHttpEntity getProducingEntity() {
/* 598 */       return this.producingEntity;
/*     */     }
/*     */     
/*     */     public void setProducingEntity(ProducingNHttpEntity producingEntity) {
/* 602 */       this.producingEntity = producingEntity;
/*     */     }
/*     */     
/*     */     public IOException getIOException() {
/* 606 */       return this.ioex;
/*     */     }
/*     */     
/*     */     public IOException getIOExepction() {
/* 610 */       return this.ioex;
/*     */     }
/*     */     
/*     */     public void setIOException(IOException ex) {
/* 614 */       this.ioex = ex;
/*     */     }
/*     */     
/*     */     public void setIOExepction(IOException ex) {
/* 618 */       this.ioex = ex;
/*     */     }
/*     */     
/*     */     public HttpException getHttpException() {
/* 622 */       return this.httpex;
/*     */     }
/*     */     
/*     */     public HttpException getHttpExepction() {
/* 626 */       return this.httpex;
/*     */     }
/*     */     
/*     */     public void setHttpException(HttpException ex) {
/* 630 */       this.httpex = ex;
/*     */     }
/*     */     
/*     */     public void setHttpExepction(HttpException ex) {
/* 634 */       this.httpex = ex;
/*     */     }
/*     */     
/*     */     public boolean isHandled() {
/* 638 */       return this.handled;
/*     */     }
/*     */     
/*     */     public void setHandled(boolean handled) {
/* 642 */       this.handled = handled;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ResponseTriggerImpl
/*     */     implements NHttpResponseTrigger
/*     */   {
/*     */     private final AsyncNHttpServiceHandler.ServerConnState connState;
/*     */     
/*     */     private final IOControl iocontrol;
/*     */     private volatile boolean triggered;
/*     */     
/*     */     public ResponseTriggerImpl(AsyncNHttpServiceHandler.ServerConnState connState, IOControl iocontrol) {
/* 656 */       this.connState = connState;
/* 657 */       this.iocontrol = iocontrol;
/*     */     }
/*     */     
/*     */     public void submitResponse(HttpResponse response) {
/* 661 */       Args.notNull(response, "Response");
/* 662 */       Asserts.check(!this.triggered, "Response already triggered");
/* 663 */       this.triggered = true;
/* 664 */       this.connState.setResponse(response);
/* 665 */       this.iocontrol.requestOutput();
/*     */     }
/*     */     
/*     */     public void handleException(HttpException ex) {
/* 669 */       Asserts.check(!this.triggered, "Response already triggered");
/* 670 */       this.triggered = true;
/* 671 */       this.connState.setHttpException(ex);
/* 672 */       this.iocontrol.requestOutput();
/*     */     }
/*     */     
/*     */     public void handleException(IOException ex) {
/* 676 */       Asserts.check(!this.triggered, "Response already triggered");
/* 677 */       this.triggered = true;
/* 678 */       this.connState.setIOException(ex);
/* 679 */       this.iocontrol.requestOutput();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/protocol/AsyncNHttpServiceHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
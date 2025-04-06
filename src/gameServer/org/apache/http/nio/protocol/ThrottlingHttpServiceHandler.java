/*     */ package org.apache.http.nio.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.concurrent.Executor;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.entity.ByteArrayEntity;
/*     */ import org.apache.http.nio.ContentDecoder;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.IOControl;
/*     */ import org.apache.http.nio.NHttpConnection;
/*     */ import org.apache.http.nio.NHttpServerConnection;
/*     */ import org.apache.http.nio.NHttpServiceHandler;
/*     */ import org.apache.http.nio.entity.ContentBufferEntity;
/*     */ import org.apache.http.nio.entity.ContentOutputStream;
/*     */ import org.apache.http.nio.util.ByteBufferAllocator;
/*     */ import org.apache.http.nio.util.ContentInputBuffer;
/*     */ import org.apache.http.nio.util.ContentOutputBuffer;
/*     */ import org.apache.http.nio.util.DirectByteBufferAllocator;
/*     */ import org.apache.http.nio.util.SharedInputBuffer;
/*     */ import org.apache.http.nio.util.SharedOutputBuffer;
/*     */ import org.apache.http.params.DefaultedHttpParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.protocol.HttpExpectationVerifier;
/*     */ import org.apache.http.protocol.HttpProcessor;
/*     */ import org.apache.http.protocol.HttpRequestHandler;
/*     */ import org.apache.http.protocol.HttpRequestHandlerResolver;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.EncodingUtils;
/*     */ import org.apache.http.util.EntityUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @ThreadSafe
/*     */ public class ThrottlingHttpServiceHandler
/*     */   extends NHttpHandlerBase
/*     */   implements NHttpServiceHandler
/*     */ {
/*     */   protected final HttpResponseFactory responseFactory;
/*     */   protected final Executor executor;
/*     */   protected HttpRequestHandlerResolver handlerResolver;
/*     */   protected HttpExpectationVerifier expectationVerifier;
/*     */   private final int bufsize;
/*     */   
/*     */   public ThrottlingHttpServiceHandler(HttpProcessor httpProcessor, HttpResponseFactory responseFactory, ConnectionReuseStrategy connStrategy, ByteBufferAllocator allocator, Executor executor, HttpParams params) {
/* 131 */     super(httpProcessor, connStrategy, allocator, params);
/* 132 */     Args.notNull(responseFactory, "Response factory");
/* 133 */     Args.notNull(executor, "Executor");
/* 134 */     this.responseFactory = responseFactory;
/* 135 */     this.executor = executor;
/* 136 */     this.bufsize = this.params.getIntParameter("http.nio.content-buffer-size", 20480);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThrottlingHttpServiceHandler(HttpProcessor httpProcessor, HttpResponseFactory responseFactory, ConnectionReuseStrategy connStrategy, Executor executor, HttpParams params) {
/* 145 */     this(httpProcessor, responseFactory, connStrategy, (ByteBufferAllocator)DirectByteBufferAllocator.INSTANCE, executor, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHandlerResolver(HttpRequestHandlerResolver handlerResolver) {
/* 150 */     this.handlerResolver = handlerResolver;
/*     */   }
/*     */   
/*     */   public void setExpectationVerifier(HttpExpectationVerifier expectationVerifier) {
/* 154 */     this.expectationVerifier = expectationVerifier;
/*     */   }
/*     */   
/*     */   public void connected(NHttpServerConnection conn) {
/* 158 */     HttpContext context = conn.getContext();
/*     */     
/* 160 */     ServerConnState connState = new ServerConnState(this.bufsize, (IOControl)conn, this.allocator);
/* 161 */     context.setAttribute("http.nio.conn-state", connState);
/*     */     
/* 163 */     if (this.eventListener != null) {
/* 164 */       this.eventListener.connectionOpen((NHttpConnection)conn);
/*     */     }
/*     */   }
/*     */   
/*     */   public void closed(NHttpServerConnection conn) {
/* 169 */     HttpContext context = conn.getContext();
/* 170 */     ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");
/*     */     
/* 172 */     if (connState != null) {
/* 173 */       synchronized (connState) {
/* 174 */         connState.close();
/* 175 */         connState.notifyAll();
/*     */       } 
/*     */     }
/*     */     
/* 179 */     if (this.eventListener != null) {
/* 180 */       this.eventListener.connectionClosed((NHttpConnection)conn);
/*     */     }
/*     */   }
/*     */   
/*     */   public void exception(NHttpServerConnection conn, HttpException httpex) {
/* 185 */     if (conn.isResponseSubmitted()) {
/* 186 */       if (this.eventListener != null) {
/* 187 */         this.eventListener.fatalProtocolException(httpex, (NHttpConnection)conn);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 192 */     HttpContext context = conn.getContext();
/*     */     
/* 194 */     ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");
/*     */ 
/*     */     
/*     */     try {
/* 198 */       HttpResponse response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_0, 500, context);
/*     */ 
/*     */ 
/*     */       
/* 202 */       response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));
/*     */       
/* 204 */       handleException(httpex, response);
/* 205 */       response.setEntity(null);
/*     */       
/* 207 */       this.httpProcessor.process(response, context);
/*     */       
/* 209 */       synchronized (connState) {
/* 210 */         connState.setResponse(response);
/*     */         
/* 212 */         conn.requestOutput();
/*     */       }
/*     */     
/* 215 */     } catch (IOException ex) {
/* 216 */       shutdownConnection((NHttpConnection)conn, ex);
/* 217 */       if (this.eventListener != null) {
/* 218 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/* 220 */     } catch (HttpException ex) {
/* 221 */       closeConnection((NHttpConnection)conn, (Throwable)ex);
/* 222 */       if (this.eventListener != null) {
/* 223 */         this.eventListener.fatalProtocolException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void exception(NHttpServerConnection conn, IOException ex) {
/* 229 */     shutdownConnection((NHttpConnection)conn, ex);
/*     */     
/* 231 */     if (this.eventListener != null) {
/* 232 */       this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */     }
/*     */   }
/*     */   
/*     */   public void timeout(NHttpServerConnection conn) {
/* 237 */     handleTimeout((NHttpConnection)conn);
/*     */   }
/*     */   
/*     */   public void requestReceived(final NHttpServerConnection conn) {
/* 241 */     HttpContext context = conn.getContext();
/*     */     
/* 243 */     final HttpRequest request = conn.getHttpRequest();
/* 244 */     final ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");
/*     */     
/* 246 */     synchronized (connState) {
/* 247 */       boolean contentExpected = false;
/* 248 */       if (request instanceof HttpEntityEnclosingRequest) {
/* 249 */         HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
/* 250 */         if (entity != null) {
/* 251 */           contentExpected = true;
/*     */         }
/*     */       } 
/*     */       
/* 255 */       if (!contentExpected) {
/* 256 */         conn.suspendInput();
/*     */       }
/*     */       
/* 259 */       this.executor.execute(new Runnable()
/*     */           {
/*     */             public void run()
/*     */             {
/*     */               try {
/* 264 */                 ThrottlingHttpServiceHandler.this.handleRequest(request, connState, conn);
/*     */               }
/* 266 */               catch (IOException ex) {
/* 267 */                 ThrottlingHttpServiceHandler.this.shutdownConnection((NHttpConnection)conn, ex);
/* 268 */                 if (ThrottlingHttpServiceHandler.this.eventListener != null) {
/* 269 */                   ThrottlingHttpServiceHandler.this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */                 }
/* 271 */               } catch (HttpException ex) {
/* 272 */                 ThrottlingHttpServiceHandler.this.shutdownConnection((NHttpConnection)conn, (Throwable)ex);
/* 273 */                 if (ThrottlingHttpServiceHandler.this.eventListener != null) {
/* 274 */                   ThrottlingHttpServiceHandler.this.eventListener.fatalProtocolException(ex, (NHttpConnection)conn);
/*     */                 }
/*     */               } 
/*     */             }
/*     */           });
/*     */ 
/*     */       
/* 281 */       connState.notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void inputReady(NHttpServerConnection conn, ContentDecoder decoder) {
/* 287 */     HttpContext context = conn.getContext();
/*     */     
/* 289 */     ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");
/*     */ 
/*     */     
/*     */     try {
/* 293 */       synchronized (connState) {
/* 294 */         ContentInputBuffer buffer = connState.getInbuffer();
/*     */         
/* 296 */         buffer.consumeContent(decoder);
/* 297 */         if (decoder.isCompleted()) {
/* 298 */           connState.setInputState(4);
/*     */         } else {
/* 300 */           connState.setInputState(2);
/*     */         } 
/*     */         
/* 303 */         connState.notifyAll();
/*     */       }
/*     */     
/* 306 */     } catch (IOException ex) {
/* 307 */       shutdownConnection((NHttpConnection)conn, ex);
/* 308 */       if (this.eventListener != null) {
/* 309 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void responseReady(NHttpServerConnection conn) {
/* 316 */     HttpContext context = conn.getContext();
/*     */     
/* 318 */     ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");
/*     */ 
/*     */     
/*     */     try {
/* 322 */       synchronized (connState) {
/* 323 */         if (connState.isExpectationFailed()) {
/*     */ 
/*     */ 
/*     */           
/* 327 */           conn.resetInput();
/* 328 */           connState.setExpectationFailed(false);
/*     */         } 
/*     */         
/* 331 */         HttpResponse response = connState.getResponse();
/* 332 */         if (connState.getOutputState() == 0 && response != null && !conn.isResponseSubmitted()) {
/*     */ 
/*     */ 
/*     */           
/* 336 */           conn.submitResponse(response);
/* 337 */           int statusCode = response.getStatusLine().getStatusCode();
/* 338 */           HttpEntity entity = response.getEntity();
/*     */           
/* 340 */           if (statusCode >= 200 && entity == null) {
/* 341 */             connState.setOutputState(32);
/*     */             
/* 343 */             if (!this.connStrategy.keepAlive(response, context)) {
/* 344 */               conn.close();
/*     */             }
/*     */           } else {
/* 347 */             connState.setOutputState(8);
/*     */           } 
/*     */         } 
/*     */         
/* 351 */         connState.notifyAll();
/*     */       }
/*     */     
/* 354 */     } catch (IOException ex) {
/* 355 */       shutdownConnection((NHttpConnection)conn, ex);
/* 356 */       if (this.eventListener != null) {
/* 357 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/* 359 */     } catch (HttpException ex) {
/* 360 */       closeConnection((NHttpConnection)conn, (Throwable)ex);
/* 361 */       if (this.eventListener != null) {
/* 362 */         this.eventListener.fatalProtocolException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void outputReady(NHttpServerConnection conn, ContentEncoder encoder) {
/* 368 */     HttpContext context = conn.getContext();
/*     */     
/* 370 */     ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");
/*     */ 
/*     */     
/*     */     try {
/* 374 */       synchronized (connState) {
/* 375 */         HttpResponse response = connState.getResponse();
/* 376 */         ContentOutputBuffer buffer = connState.getOutbuffer();
/*     */         
/* 378 */         buffer.produceContent(encoder);
/* 379 */         if (encoder.isCompleted()) {
/* 380 */           connState.setOutputState(32);
/*     */           
/* 382 */           if (!this.connStrategy.keepAlive(response, context)) {
/* 383 */             conn.close();
/*     */           }
/*     */         } else {
/* 386 */           connState.setOutputState(16);
/*     */         } 
/*     */         
/* 389 */         connState.notifyAll();
/*     */       }
/*     */     
/* 392 */     } catch (IOException ex) {
/* 393 */       shutdownConnection((NHttpConnection)conn, ex);
/* 394 */       if (this.eventListener != null) {
/* 395 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void handleException(HttpException ex, HttpResponse response) {
/* 401 */     if (ex instanceof org.apache.http.MethodNotSupportedException) {
/* 402 */       response.setStatusCode(501);
/* 403 */     } else if (ex instanceof org.apache.http.UnsupportedHttpVersionException) {
/* 404 */       response.setStatusCode(505);
/* 405 */     } else if (ex instanceof org.apache.http.ProtocolException) {
/* 406 */       response.setStatusCode(400);
/*     */     } else {
/* 408 */       response.setStatusCode(500);
/*     */     } 
/* 410 */     byte[] msg = EncodingUtils.getAsciiBytes(ex.getMessage());
/* 411 */     ByteArrayEntity entity = new ByteArrayEntity(msg);
/* 412 */     entity.setContentType("text/plain; charset=US-ASCII");
/* 413 */     response.setEntity((HttpEntity)entity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleRequest(HttpRequest request, ServerConnState connState, NHttpServerConnection conn) throws HttpException, IOException {
/*     */     HttpVersion httpVersion;
/* 421 */     HttpContext context = conn.getContext();
/*     */ 
/*     */ 
/*     */     
/* 425 */     synchronized (connState) {
/*     */       while (true) {
/*     */         try {
/* 428 */           int currentState = connState.getOutputState();
/* 429 */           if (currentState == 0) {
/*     */             break;
/*     */           }
/* 432 */           if (currentState == -1) {
/*     */             return;
/*     */           }
/* 435 */           connState.wait();
/*     */         }
/* 437 */         catch (InterruptedException ex) {
/* 438 */           connState.shutdown(); return;
/*     */         } 
/*     */       } 
/* 441 */       connState.setInputState(1);
/* 442 */       connState.setRequest(request);
/*     */     } 
/*     */     
/* 445 */     request.setParams((HttpParams)new DefaultedHttpParams(request.getParams(), this.params));
/*     */     
/* 447 */     context.setAttribute("http.connection", conn);
/* 448 */     context.setAttribute("http.request", request);
/*     */     
/* 450 */     ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
/*     */     
/* 452 */     if (!ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_1))
/*     */     {
/* 454 */       httpVersion = HttpVersion.HTTP_1_1;
/*     */     }
/*     */     
/* 457 */     HttpResponse response = null;
/*     */     
/* 459 */     if (request instanceof HttpEntityEnclosingRequest) {
/* 460 */       HttpEntityEnclosingRequest eeRequest = (HttpEntityEnclosingRequest)request;
/*     */       
/* 462 */       if (eeRequest.expectContinue()) {
/* 463 */         response = this.responseFactory.newHttpResponse((ProtocolVersion)httpVersion, 100, context);
/*     */ 
/*     */ 
/*     */         
/* 467 */         response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));
/*     */         
/* 469 */         if (this.expectationVerifier != null) {
/*     */           try {
/* 471 */             this.expectationVerifier.verify(request, response, context);
/* 472 */           } catch (HttpException ex) {
/* 473 */             response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_0, 500, context);
/*     */             
/* 475 */             response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));
/*     */             
/* 477 */             handleException(ex, response);
/*     */           } 
/*     */         }
/*     */         
/* 481 */         synchronized (connState) {
/* 482 */           if (response.getStatusLine().getStatusCode() < 200) {
/*     */ 
/*     */             
/* 485 */             connState.setResponse(response);
/* 486 */             conn.requestOutput();
/*     */ 
/*     */             
/*     */             try {
/*     */               while (true) {
/* 491 */                 int currentState = connState.getOutputState();
/* 492 */                 if (currentState == 8) {
/*     */                   break;
/*     */                 }
/* 495 */                 if (currentState == -1) {
/*     */                   return;
/*     */                 }
/* 498 */                 connState.wait();
/*     */               } 
/* 500 */             } catch (InterruptedException ex) {
/* 501 */               connState.shutdown();
/*     */               return;
/*     */             } 
/* 504 */             connState.resetOutput();
/* 505 */             response = null;
/*     */           } else {
/*     */             
/* 508 */             eeRequest.setEntity(null);
/* 509 */             conn.suspendInput();
/* 510 */             connState.setExpectationFailed(true);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 516 */       if (eeRequest.getEntity() != null) {
/* 517 */         eeRequest.setEntity((HttpEntity)new ContentBufferEntity(eeRequest.getEntity(), connState.getInbuffer()));
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 524 */     if (response == null) {
/* 525 */       response = this.responseFactory.newHttpResponse((ProtocolVersion)httpVersion, 200, context);
/*     */ 
/*     */ 
/*     */       
/* 529 */       response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));
/*     */ 
/*     */       
/* 532 */       context.setAttribute("http.response", response);
/*     */ 
/*     */       
/*     */       try {
/* 536 */         this.httpProcessor.process(request, context);
/*     */         
/* 538 */         HttpRequestHandler handler = null;
/* 539 */         if (this.handlerResolver != null) {
/* 540 */           String requestURI = request.getRequestLine().getUri();
/* 541 */           handler = this.handlerResolver.lookup(requestURI);
/*     */         } 
/* 543 */         if (handler != null) {
/* 544 */           handler.handle(request, response, context);
/*     */         } else {
/* 546 */           response.setStatusCode(501);
/*     */         }
/*     */       
/* 549 */       } catch (HttpException ex) {
/* 550 */         response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_0, 500, context);
/*     */         
/* 552 */         response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));
/*     */         
/* 554 */         handleException(ex, response);
/*     */       } 
/*     */     } 
/*     */     
/* 558 */     if (request instanceof HttpEntityEnclosingRequest) {
/* 559 */       HttpEntityEnclosingRequest eeRequest = (HttpEntityEnclosingRequest)request;
/* 560 */       HttpEntity entity = eeRequest.getEntity();
/* 561 */       EntityUtils.consume(entity);
/*     */     } 
/*     */ 
/*     */     
/* 565 */     connState.resetInput();
/*     */     
/* 567 */     this.httpProcessor.process(response, context);
/*     */     
/* 569 */     if (!canResponseHaveBody(request, response)) {
/* 570 */       response.setEntity(null);
/*     */     }
/*     */     
/* 573 */     connState.setResponse(response);
/*     */     
/* 575 */     conn.requestOutput();
/*     */     
/* 577 */     if (response.getEntity() != null) {
/* 578 */       ContentOutputBuffer buffer = connState.getOutbuffer();
/* 579 */       ContentOutputStream contentOutputStream = new ContentOutputStream(buffer);
/*     */       
/* 581 */       HttpEntity entity = response.getEntity();
/* 582 */       entity.writeTo((OutputStream)contentOutputStream);
/* 583 */       contentOutputStream.flush();
/* 584 */       contentOutputStream.close();
/*     */     } 
/*     */     
/* 587 */     synchronized (connState) {
/*     */       while (true) {
/*     */         try {
/* 590 */           int currentState = connState.getOutputState();
/* 591 */           if (currentState == 32) {
/*     */             break;
/*     */           }
/* 594 */           if (currentState == -1) {
/*     */             return;
/*     */           }
/* 597 */           connState.wait();
/*     */         }
/* 599 */         catch (InterruptedException ex) {
/* 600 */           connState.shutdown(); return;
/*     */         } 
/*     */       } 
/* 603 */       connState.resetOutput();
/* 604 */       conn.requestInput();
/* 605 */       connState.notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void shutdownConnection(NHttpConnection conn, Throwable cause) {
/* 611 */     HttpContext context = conn.getContext();
/*     */     
/* 613 */     ServerConnState connState = (ServerConnState)context.getAttribute("http.nio.conn-state");
/*     */     
/* 615 */     super.shutdownConnection(conn, cause);
/*     */     
/* 617 */     if (connState != null) {
/* 618 */       connState.shutdown();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class ServerConnState
/*     */   {
/*     */     public static final int SHUTDOWN = -1;
/*     */     
/*     */     public static final int READY = 0;
/*     */     
/*     */     public static final int REQUEST_RECEIVED = 1;
/*     */     
/*     */     public static final int REQUEST_BODY_STREAM = 2;
/*     */     
/*     */     public static final int REQUEST_BODY_DONE = 4;
/*     */     
/*     */     public static final int RESPONSE_SENT = 8;
/*     */     
/*     */     public static final int RESPONSE_BODY_STREAM = 16;
/*     */     
/*     */     public static final int RESPONSE_BODY_DONE = 32;
/*     */     public static final int RESPONSE_DONE = 32;
/*     */     private final SharedInputBuffer inbuffer;
/*     */     private final SharedOutputBuffer outbuffer;
/*     */     private volatile int inputState;
/*     */     private volatile int outputState;
/*     */     private volatile HttpRequest request;
/*     */     private volatile HttpResponse response;
/*     */     private volatile boolean expectationFailure;
/*     */     
/*     */     public ServerConnState(int bufsize, IOControl ioControl, ByteBufferAllocator allocator) {
/* 650 */       this.inbuffer = new SharedInputBuffer(bufsize, ioControl, allocator);
/* 651 */       this.outbuffer = new SharedOutputBuffer(bufsize, ioControl, allocator);
/* 652 */       this.inputState = 0;
/* 653 */       this.outputState = 0;
/*     */     }
/*     */     
/*     */     public ContentInputBuffer getInbuffer() {
/* 657 */       return (ContentInputBuffer)this.inbuffer;
/*     */     }
/*     */     
/*     */     public ContentOutputBuffer getOutbuffer() {
/* 661 */       return (ContentOutputBuffer)this.outbuffer;
/*     */     }
/*     */     
/*     */     public int getInputState() {
/* 665 */       return this.inputState;
/*     */     }
/*     */     
/*     */     public void setInputState(int inputState) {
/* 669 */       this.inputState = inputState;
/*     */     }
/*     */     
/*     */     public int getOutputState() {
/* 673 */       return this.outputState;
/*     */     }
/*     */     
/*     */     public void setOutputState(int outputState) {
/* 677 */       this.outputState = outputState;
/*     */     }
/*     */     
/*     */     public HttpRequest getRequest() {
/* 681 */       return this.request;
/*     */     }
/*     */     
/*     */     public void setRequest(HttpRequest request) {
/* 685 */       this.request = request;
/*     */     }
/*     */     
/*     */     public HttpResponse getResponse() {
/* 689 */       return this.response;
/*     */     }
/*     */     
/*     */     public void setResponse(HttpResponse response) {
/* 693 */       this.response = response;
/*     */     }
/*     */     
/*     */     public boolean isExpectationFailed() {
/* 697 */       return this.expectationFailure;
/*     */     }
/*     */     
/*     */     public void setExpectationFailed(boolean b) {
/* 701 */       this.expectationFailure = b;
/*     */     }
/*     */     
/*     */     public void close() {
/* 705 */       this.inbuffer.close();
/* 706 */       this.outbuffer.close();
/* 707 */       this.inputState = -1;
/* 708 */       this.outputState = -1;
/*     */     }
/*     */     
/*     */     public void shutdown() {
/* 712 */       this.inbuffer.shutdown();
/* 713 */       this.outbuffer.shutdown();
/* 714 */       this.inputState = -1;
/* 715 */       this.outputState = -1;
/*     */     }
/*     */     
/*     */     public void resetInput() {
/* 719 */       this.inbuffer.reset();
/* 720 */       this.request = null;
/* 721 */       this.inputState = 0;
/*     */     }
/*     */     
/*     */     public void resetOutput() {
/* 725 */       this.outbuffer.reset();
/* 726 */       this.response = null;
/* 727 */       this.outputState = 0;
/* 728 */       this.expectationFailure = false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/protocol/ThrottlingHttpServiceHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
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
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.nio.ContentDecoder;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.IOControl;
/*     */ import org.apache.http.nio.NHttpClientConnection;
/*     */ import org.apache.http.nio.NHttpClientHandler;
/*     */ import org.apache.http.nio.NHttpConnection;
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
/*     */ import org.apache.http.protocol.HttpProcessor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class ThrottlingHttpClientHandler
/*     */   extends NHttpHandlerBase
/*     */   implements NHttpClientHandler
/*     */ {
/*     */   protected HttpRequestExecutionHandler execHandler;
/*     */   protected final Executor executor;
/*     */   private final int bufsize;
/*     */   
/*     */   public ThrottlingHttpClientHandler(HttpProcessor httpProcessor, HttpRequestExecutionHandler execHandler, ConnectionReuseStrategy connStrategy, ByteBufferAllocator allocator, Executor executor, HttpParams params) {
/* 117 */     super(httpProcessor, connStrategy, allocator, params);
/* 118 */     Args.notNull(execHandler, "HTTP request execution handler");
/* 119 */     Args.notNull(executor, "Executor");
/* 120 */     this.execHandler = execHandler;
/* 121 */     this.executor = executor;
/* 122 */     this.bufsize = this.params.getIntParameter("http.nio.content-buffer-size", 20480);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThrottlingHttpClientHandler(HttpProcessor httpProcessor, HttpRequestExecutionHandler execHandler, ConnectionReuseStrategy connStrategy, Executor executor, HttpParams params) {
/* 131 */     this(httpProcessor, execHandler, connStrategy, (ByteBufferAllocator)DirectByteBufferAllocator.INSTANCE, executor, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public void connected(NHttpClientConnection conn, Object attachment) {
/* 136 */     HttpContext context = conn.getContext();
/*     */     
/* 138 */     initialize(conn, attachment);
/*     */     
/* 140 */     ClientConnState connState = new ClientConnState(this.bufsize, (IOControl)conn, this.allocator);
/* 141 */     context.setAttribute("http.nio.conn-state", connState);
/*     */     
/* 143 */     if (this.eventListener != null) {
/* 144 */       this.eventListener.connectionOpen((NHttpConnection)conn);
/*     */     }
/*     */     
/* 147 */     requestReady(conn);
/*     */   }
/*     */   
/*     */   public void closed(NHttpClientConnection conn) {
/* 151 */     HttpContext context = conn.getContext();
/* 152 */     ClientConnState connState = (ClientConnState)context.getAttribute("http.nio.conn-state");
/*     */     
/* 154 */     if (connState != null) {
/* 155 */       synchronized (connState) {
/* 156 */         connState.close();
/* 157 */         connState.notifyAll();
/*     */       } 
/*     */     }
/* 160 */     this.execHandler.finalizeContext(context);
/*     */     
/* 162 */     if (this.eventListener != null) {
/* 163 */       this.eventListener.connectionClosed((NHttpConnection)conn);
/*     */     }
/*     */   }
/*     */   
/*     */   public void exception(NHttpClientConnection conn, HttpException ex) {
/* 168 */     closeConnection((NHttpConnection)conn, (Throwable)ex);
/* 169 */     if (this.eventListener != null) {
/* 170 */       this.eventListener.fatalProtocolException(ex, (NHttpConnection)conn);
/*     */     }
/*     */   }
/*     */   
/*     */   public void exception(NHttpClientConnection conn, IOException ex) {
/* 175 */     shutdownConnection((NHttpConnection)conn, ex);
/* 176 */     if (this.eventListener != null) {
/* 177 */       this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void requestReady(NHttpClientConnection conn) {
/* 183 */     HttpContext context = conn.getContext();
/*     */     
/* 185 */     ClientConnState connState = (ClientConnState)context.getAttribute("http.nio.conn-state");
/*     */ 
/*     */     
/*     */     try {
/* 189 */       synchronized (connState) {
/* 190 */         if (connState.getOutputState() != 0) {
/*     */           return;
/*     */         }
/*     */         
/* 194 */         HttpRequest request = this.execHandler.submitRequest(context);
/* 195 */         if (request == null) {
/*     */           return;
/*     */         }
/*     */         
/* 199 */         request.setParams((HttpParams)new DefaultedHttpParams(request.getParams(), this.params));
/*     */ 
/*     */         
/* 202 */         context.setAttribute("http.request", request);
/* 203 */         this.httpProcessor.process(request, context);
/* 204 */         connState.setRequest(request);
/* 205 */         conn.submitRequest(request);
/* 206 */         connState.setOutputState(1);
/*     */         
/* 208 */         conn.requestInput();
/*     */         
/* 210 */         if (request instanceof HttpEntityEnclosingRequest) {
/* 211 */           if (((HttpEntityEnclosingRequest)request).expectContinue()) {
/* 212 */             int timeout = conn.getSocketTimeout();
/* 213 */             connState.setTimeout(timeout);
/* 214 */             timeout = this.params.getIntParameter("http.protocol.wait-for-continue", 3000);
/*     */             
/* 216 */             conn.setSocketTimeout(timeout);
/* 217 */             connState.setOutputState(2);
/*     */           } else {
/* 219 */             sendRequestBody((HttpEntityEnclosingRequest)request, connState, conn);
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 226 */         connState.notifyAll();
/*     */       }
/*     */     
/* 229 */     } catch (IOException ex) {
/* 230 */       shutdownConnection((NHttpConnection)conn, ex);
/* 231 */       if (this.eventListener != null) {
/* 232 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/* 234 */     } catch (HttpException ex) {
/* 235 */       closeConnection((NHttpConnection)conn, (Throwable)ex);
/* 236 */       if (this.eventListener != null) {
/* 237 */         this.eventListener.fatalProtocolException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void outputReady(NHttpClientConnection conn, ContentEncoder encoder) {
/* 243 */     HttpContext context = conn.getContext();
/*     */     
/* 245 */     ClientConnState connState = (ClientConnState)context.getAttribute("http.nio.conn-state");
/*     */ 
/*     */     
/*     */     try {
/* 249 */       synchronized (connState) {
/* 250 */         if (connState.getOutputState() == 2) {
/* 251 */           conn.suspendOutput();
/*     */           return;
/*     */         } 
/* 254 */         ContentOutputBuffer buffer = connState.getOutbuffer();
/* 255 */         buffer.produceContent(encoder);
/* 256 */         if (encoder.isCompleted()) {
/* 257 */           connState.setInputState(8);
/*     */         } else {
/* 259 */           connState.setInputState(4);
/*     */         } 
/*     */         
/* 262 */         connState.notifyAll();
/*     */       }
/*     */     
/* 265 */     } catch (IOException ex) {
/* 266 */       shutdownConnection((NHttpConnection)conn, ex);
/* 267 */       if (this.eventListener != null) {
/* 268 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void responseReceived(NHttpClientConnection conn) {
/* 274 */     HttpContext context = conn.getContext();
/* 275 */     ClientConnState connState = (ClientConnState)context.getAttribute("http.nio.conn-state");
/*     */ 
/*     */     
/*     */     try {
/* 279 */       synchronized (connState) {
/* 280 */         HttpResponse response = conn.getHttpResponse();
/* 281 */         response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));
/*     */ 
/*     */         
/* 284 */         HttpRequest request = connState.getRequest();
/*     */         
/* 286 */         int statusCode = response.getStatusLine().getStatusCode();
/* 287 */         if (statusCode < 200) {
/*     */           
/* 289 */           if (statusCode == 100 && connState.getOutputState() == 2) {
/*     */             
/* 291 */             connState.setOutputState(1);
/* 292 */             continueRequest(conn, connState);
/*     */           } 
/*     */           return;
/*     */         } 
/* 296 */         connState.setResponse(response);
/* 297 */         connState.setInputState(16);
/*     */         
/* 299 */         if (connState.getOutputState() == 2) {
/* 300 */           int timeout = connState.getTimeout();
/* 301 */           conn.setSocketTimeout(timeout);
/* 302 */           conn.resetOutput();
/*     */         } 
/*     */ 
/*     */         
/* 306 */         if (!canResponseHaveBody(request, response)) {
/* 307 */           conn.resetInput();
/* 308 */           response.setEntity(null);
/* 309 */           connState.setInputState(64);
/*     */           
/* 311 */           if (!this.connStrategy.keepAlive(response, context)) {
/* 312 */             conn.close();
/*     */           }
/*     */         } 
/*     */         
/* 316 */         if (response.getEntity() != null) {
/* 317 */           response.setEntity((HttpEntity)new ContentBufferEntity(response.getEntity(), connState.getInbuffer()));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 322 */         context.setAttribute("http.response", response);
/*     */         
/* 324 */         this.httpProcessor.process(response, context);
/*     */         
/* 326 */         handleResponse(response, connState, conn);
/*     */         
/* 328 */         connState.notifyAll();
/*     */       }
/*     */     
/* 331 */     } catch (IOException ex) {
/* 332 */       shutdownConnection((NHttpConnection)conn, ex);
/* 333 */       if (this.eventListener != null) {
/* 334 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/* 336 */     } catch (HttpException ex) {
/* 337 */       closeConnection((NHttpConnection)conn, (Throwable)ex);
/* 338 */       if (this.eventListener != null) {
/* 339 */         this.eventListener.fatalProtocolException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void inputReady(NHttpClientConnection conn, ContentDecoder decoder) {
/* 345 */     HttpContext context = conn.getContext();
/*     */     
/* 347 */     ClientConnState connState = (ClientConnState)context.getAttribute("http.nio.conn-state");
/*     */     
/*     */     try {
/* 350 */       synchronized (connState) {
/* 351 */         HttpResponse response = connState.getResponse();
/* 352 */         ContentInputBuffer buffer = connState.getInbuffer();
/*     */         
/* 354 */         buffer.consumeContent(decoder);
/* 355 */         if (decoder.isCompleted()) {
/* 356 */           connState.setInputState(64);
/*     */           
/* 358 */           if (!this.connStrategy.keepAlive(response, context)) {
/* 359 */             conn.close();
/*     */           }
/*     */         } else {
/* 362 */           connState.setInputState(32);
/*     */         } 
/*     */         
/* 365 */         connState.notifyAll();
/*     */       }
/*     */     
/* 368 */     } catch (IOException ex) {
/* 369 */       shutdownConnection((NHttpConnection)conn, ex);
/* 370 */       if (this.eventListener != null) {
/* 371 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void timeout(NHttpClientConnection conn) {
/* 377 */     HttpContext context = conn.getContext();
/* 378 */     ClientConnState connState = (ClientConnState)context.getAttribute("http.nio.conn-state");
/*     */ 
/*     */     
/*     */     try {
/* 382 */       synchronized (connState) {
/* 383 */         if (connState.getOutputState() == 2) {
/* 384 */           connState.setOutputState(1);
/* 385 */           continueRequest(conn, connState);
/*     */           
/* 387 */           connState.notifyAll();
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/* 392 */     } catch (IOException ex) {
/* 393 */       shutdownConnection((NHttpConnection)conn, ex);
/* 394 */       if (this.eventListener != null) {
/* 395 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */     
/* 399 */     handleTimeout((NHttpConnection)conn);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize(NHttpClientConnection conn, Object attachment) {
/* 405 */     HttpContext context = conn.getContext();
/*     */     
/* 407 */     context.setAttribute("http.connection", conn);
/* 408 */     this.execHandler.initalizeContext(context, attachment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void continueRequest(NHttpClientConnection conn, ClientConnState connState) throws IOException {
/* 415 */     HttpRequest request = connState.getRequest();
/*     */     
/* 417 */     int timeout = connState.getTimeout();
/* 418 */     conn.setSocketTimeout(timeout);
/*     */     
/* 420 */     sendRequestBody((HttpEntityEnclosingRequest)request, connState, conn);
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
/*     */   private void sendRequestBody(final HttpEntityEnclosingRequest request, final ClientConnState connState, final NHttpClientConnection conn) throws IOException {
/* 433 */     HttpEntity entity = request.getEntity();
/* 434 */     if (entity != null)
/*     */     {
/* 436 */       this.executor.execute(new Runnable()
/*     */           {
/*     */ 
/*     */             
/*     */             public void run()
/*     */             {
/*     */               try {
/* 443 */                 synchronized (connState) {
/*     */                   while (true) {
/*     */                     try {
/* 446 */                       int currentState = connState.getOutputState();
/* 447 */                       if (!connState.isWorkerRunning()) {
/*     */                         break;
/*     */                       }
/* 450 */                       if (currentState == -1) {
/*     */                         return;
/*     */                       }
/* 453 */                       connState.wait();
/*     */                     }
/* 455 */                     catch (InterruptedException ex) {
/* 456 */                       connState.shutdown(); return;
/*     */                     } 
/*     */                   } 
/* 459 */                   connState.setWorkerRunning(true);
/*     */                 } 
/*     */                 
/* 462 */                 ContentOutputStream contentOutputStream = new ContentOutputStream(connState.getOutbuffer());
/*     */                 
/* 464 */                 request.getEntity().writeTo((OutputStream)contentOutputStream);
/* 465 */                 contentOutputStream.flush();
/* 466 */                 contentOutputStream.close();
/*     */                 
/* 468 */                 synchronized (connState) {
/* 469 */                   connState.setWorkerRunning(false);
/* 470 */                   connState.notifyAll();
/*     */                 }
/*     */               
/* 473 */               } catch (IOException ex) {
/* 474 */                 ThrottlingHttpClientHandler.this.shutdownConnection((NHttpConnection)conn, ex);
/* 475 */                 if (ThrottlingHttpClientHandler.this.eventListener != null) {
/* 476 */                   ThrottlingHttpClientHandler.this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */                 }
/*     */               } 
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleResponse(final HttpResponse response, final ClientConnState connState, final NHttpClientConnection conn) {
/* 490 */     final HttpContext context = conn.getContext();
/*     */     
/* 492 */     this.executor.execute(new Runnable()
/*     */         {
/*     */ 
/*     */           
/*     */           public void run()
/*     */           {
/*     */             try {
/* 499 */               synchronized (connState) {
/*     */                 while (true) {
/*     */                   try {
/* 502 */                     int currentState = connState.getOutputState();
/* 503 */                     if (!connState.isWorkerRunning()) {
/*     */                       break;
/*     */                     }
/* 506 */                     if (currentState == -1) {
/*     */                       return;
/*     */                     }
/* 509 */                     connState.wait();
/*     */                   }
/* 511 */                   catch (InterruptedException ex) {
/* 512 */                     connState.shutdown(); return;
/*     */                   } 
/*     */                 } 
/* 515 */                 connState.setWorkerRunning(true);
/*     */               } 
/*     */               
/* 518 */               ThrottlingHttpClientHandler.this.execHandler.handleResponse(response, context);
/*     */               
/* 520 */               synchronized (connState)
/*     */               {
/*     */                 while (true) {
/*     */                   try {
/* 524 */                     int currentState = connState.getInputState();
/* 525 */                     if (currentState == 64) {
/*     */                       break;
/*     */                     }
/* 528 */                     if (currentState == -1) {
/*     */                       return;
/*     */                     }
/* 531 */                     connState.wait();
/*     */                   }
/* 533 */                   catch (InterruptedException ex) {
/* 534 */                     connState.shutdown(); break;
/*     */                   } 
/*     */                 } 
/* 537 */                 connState.resetInput();
/* 538 */                 connState.resetOutput();
/* 539 */                 if (conn.isOpen()) {
/* 540 */                   conn.requestOutput();
/*     */                 }
/* 542 */                 connState.setWorkerRunning(false);
/* 543 */                 connState.notifyAll();
/*     */               }
/*     */             
/* 546 */             } catch (IOException ex) {
/* 547 */               ThrottlingHttpClientHandler.this.shutdownConnection((NHttpConnection)conn, ex);
/* 548 */               if (ThrottlingHttpClientHandler.this.eventListener != null) {
/* 549 */                 ThrottlingHttpClientHandler.this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */               }
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   static class ClientConnState
/*     */   {
/*     */     public static final int SHUTDOWN = -1;
/*     */     
/*     */     public static final int READY = 0;
/*     */     
/*     */     public static final int REQUEST_SENT = 1;
/*     */     
/*     */     public static final int EXPECT_CONTINUE = 2;
/*     */     
/*     */     public static final int REQUEST_BODY_STREAM = 4;
/*     */     
/*     */     public static final int REQUEST_BODY_DONE = 8;
/*     */     
/*     */     public static final int RESPONSE_RECEIVED = 16;
/*     */     
/*     */     public static final int RESPONSE_BODY_STREAM = 32;
/*     */     
/*     */     public static final int RESPONSE_BODY_DONE = 64;
/*     */     
/*     */     public static final int RESPONSE_DONE = 64;
/*     */     
/*     */     private final SharedInputBuffer inbuffer;
/*     */     private final SharedOutputBuffer outbuffer;
/*     */     private volatile int inputState;
/*     */     private volatile int outputState;
/*     */     private volatile HttpRequest request;
/*     */     private volatile HttpResponse response;
/*     */     private volatile int timeout;
/*     */     private volatile boolean workerRunning;
/*     */     
/*     */     public ClientConnState(int bufsize, IOControl ioControl, ByteBufferAllocator allocator) {
/* 589 */       this.inbuffer = new SharedInputBuffer(bufsize, ioControl, allocator);
/* 590 */       this.outbuffer = new SharedOutputBuffer(bufsize, ioControl, allocator);
/* 591 */       this.inputState = 0;
/* 592 */       this.outputState = 0;
/*     */     }
/*     */     
/*     */     public ContentInputBuffer getInbuffer() {
/* 596 */       return (ContentInputBuffer)this.inbuffer;
/*     */     }
/*     */     
/*     */     public ContentOutputBuffer getOutbuffer() {
/* 600 */       return (ContentOutputBuffer)this.outbuffer;
/*     */     }
/*     */     
/*     */     public int getInputState() {
/* 604 */       return this.inputState;
/*     */     }
/*     */     
/*     */     public void setInputState(int inputState) {
/* 608 */       this.inputState = inputState;
/*     */     }
/*     */     
/*     */     public int getOutputState() {
/* 612 */       return this.outputState;
/*     */     }
/*     */     
/*     */     public void setOutputState(int outputState) {
/* 616 */       this.outputState = outputState;
/*     */     }
/*     */     
/*     */     public HttpRequest getRequest() {
/* 620 */       return this.request;
/*     */     }
/*     */     
/*     */     public void setRequest(HttpRequest request) {
/* 624 */       this.request = request;
/*     */     }
/*     */     
/*     */     public HttpResponse getResponse() {
/* 628 */       return this.response;
/*     */     }
/*     */     
/*     */     public void setResponse(HttpResponse response) {
/* 632 */       this.response = response;
/*     */     }
/*     */     
/*     */     public int getTimeout() {
/* 636 */       return this.timeout;
/*     */     }
/*     */     
/*     */     public void setTimeout(int timeout) {
/* 640 */       this.timeout = timeout;
/*     */     }
/*     */     
/*     */     public boolean isWorkerRunning() {
/* 644 */       return this.workerRunning;
/*     */     }
/*     */     
/*     */     public void setWorkerRunning(boolean b) {
/* 648 */       this.workerRunning = b;
/*     */     }
/*     */     
/*     */     public void close() {
/* 652 */       this.inbuffer.close();
/* 653 */       this.outbuffer.close();
/* 654 */       this.inputState = -1;
/* 655 */       this.outputState = -1;
/*     */     }
/*     */     
/*     */     public void shutdown() {
/* 659 */       this.inbuffer.shutdown();
/* 660 */       this.outbuffer.shutdown();
/* 661 */       this.inputState = -1;
/* 662 */       this.outputState = -1;
/*     */     }
/*     */     
/*     */     public void resetInput() {
/* 666 */       this.inbuffer.reset();
/* 667 */       this.request = null;
/* 668 */       this.inputState = 0;
/*     */     }
/*     */     
/*     */     public void resetOutput() {
/* 672 */       this.outbuffer.reset();
/* 673 */       this.response = null;
/* 674 */       this.outputState = 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/protocol/ThrottlingHttpClientHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
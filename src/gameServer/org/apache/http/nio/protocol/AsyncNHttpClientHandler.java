/*     */ package org.apache.http.nio.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.nio.ContentDecoder;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.IOControl;
/*     */ import org.apache.http.nio.NHttpClientConnection;
/*     */ import org.apache.http.nio.NHttpClientHandler;
/*     */ import org.apache.http.nio.NHttpConnection;
/*     */ import org.apache.http.nio.entity.ConsumingNHttpEntity;
/*     */ import org.apache.http.nio.entity.NHttpEntityWrapper;
/*     */ import org.apache.http.nio.entity.ProducingNHttpEntity;
/*     */ import org.apache.http.nio.util.ByteBufferAllocator;
/*     */ import org.apache.http.nio.util.HeapByteBufferAllocator;
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
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public class AsyncNHttpClientHandler
/*     */   extends NHttpHandlerBase
/*     */   implements NHttpClientHandler
/*     */ {
/*     */   protected NHttpRequestExecutionHandler execHandler;
/*     */   
/*     */   public AsyncNHttpClientHandler(HttpProcessor httpProcessor, NHttpRequestExecutionHandler execHandler, ConnectionReuseStrategy connStrategy, ByteBufferAllocator allocator, HttpParams params) {
/* 106 */     super(httpProcessor, connStrategy, allocator, params);
/* 107 */     this.execHandler = (NHttpRequestExecutionHandler)Args.notNull(execHandler, "HTTP request execution handler");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncNHttpClientHandler(HttpProcessor httpProcessor, NHttpRequestExecutionHandler execHandler, ConnectionReuseStrategy connStrategy, HttpParams params) {
/* 115 */     this(httpProcessor, execHandler, connStrategy, (ByteBufferAllocator)HeapByteBufferAllocator.INSTANCE, params);
/*     */   }
/*     */   
/*     */   public void connected(NHttpClientConnection conn, Object attachment) {
/* 119 */     HttpContext context = conn.getContext();
/*     */     
/* 121 */     initialize(conn, attachment);
/*     */     
/* 123 */     ClientConnState connState = new ClientConnState();
/* 124 */     context.setAttribute("http.nio.conn-state", connState);
/*     */     
/* 126 */     if (this.eventListener != null) {
/* 127 */       this.eventListener.connectionOpen((NHttpConnection)conn);
/*     */     }
/*     */     
/* 130 */     requestReady(conn);
/*     */   }
/*     */   
/*     */   public void closed(NHttpClientConnection conn) {
/* 134 */     HttpContext context = conn.getContext();
/*     */     
/* 136 */     ClientConnState connState = (ClientConnState)context.getAttribute("http.nio.conn-state");
/*     */     try {
/* 138 */       connState.reset();
/* 139 */     } catch (IOException ex) {
/* 140 */       if (this.eventListener != null) {
/* 141 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */     
/* 145 */     this.execHandler.finalizeContext(context);
/*     */     
/* 147 */     if (this.eventListener != null) {
/* 148 */       this.eventListener.connectionClosed((NHttpConnection)conn);
/*     */     }
/*     */   }
/*     */   
/*     */   public void exception(NHttpClientConnection conn, HttpException ex) {
/* 153 */     closeConnection((NHttpConnection)conn, (Throwable)ex);
/* 154 */     if (this.eventListener != null) {
/* 155 */       this.eventListener.fatalProtocolException(ex, (NHttpConnection)conn);
/*     */     }
/*     */   }
/*     */   
/*     */   public void exception(NHttpClientConnection conn, IOException ex) {
/* 160 */     shutdownConnection((NHttpConnection)conn, ex);
/* 161 */     if (this.eventListener != null) {
/* 162 */       this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */     }
/*     */   }
/*     */   
/*     */   public void requestReady(NHttpClientConnection conn) {
/* 167 */     HttpContext context = conn.getContext();
/*     */     
/* 169 */     ClientConnState connState = (ClientConnState)context.getAttribute("http.nio.conn-state");
/* 170 */     if (connState.getOutputState() != 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 176 */       HttpRequest request = this.execHandler.submitRequest(context);
/* 177 */       if (request == null) {
/*     */         return;
/*     */       }
/*     */       
/* 181 */       request.setParams((HttpParams)new DefaultedHttpParams(request.getParams(), this.params));
/*     */ 
/*     */       
/* 184 */       context.setAttribute("http.request", request);
/* 185 */       this.httpProcessor.process(request, context);
/*     */       
/* 187 */       HttpEntityEnclosingRequest entityReq = null;
/* 188 */       HttpEntity entity = null;
/*     */       
/* 190 */       if (request instanceof HttpEntityEnclosingRequest) {
/* 191 */         entityReq = (HttpEntityEnclosingRequest)request;
/* 192 */         entity = entityReq.getEntity();
/*     */       } 
/*     */       
/* 195 */       if (entity instanceof ProducingNHttpEntity) {
/* 196 */         connState.setProducingEntity((ProducingNHttpEntity)entity);
/* 197 */       } else if (entity != null) {
/* 198 */         connState.setProducingEntity((ProducingNHttpEntity)new NHttpEntityWrapper(entity));
/*     */       } 
/*     */       
/* 201 */       connState.setRequest(request);
/* 202 */       conn.submitRequest(request);
/* 203 */       connState.setOutputState(1);
/*     */       
/* 205 */       if (entityReq != null && entityReq.expectContinue()) {
/* 206 */         int timeout = conn.getSocketTimeout();
/* 207 */         connState.setTimeout(timeout);
/* 208 */         timeout = this.params.getIntParameter("http.protocol.wait-for-continue", 3000);
/*     */         
/* 210 */         conn.setSocketTimeout(timeout);
/* 211 */         connState.setOutputState(2);
/* 212 */       } else if (connState.getProducingEntity() != null) {
/* 213 */         connState.setOutputState(4);
/*     */       }
/*     */     
/* 216 */     } catch (IOException ex) {
/* 217 */       shutdownConnection((NHttpConnection)conn, ex);
/* 218 */       if (this.eventListener != null) {
/* 219 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/* 221 */     } catch (HttpException ex) {
/* 222 */       closeConnection((NHttpConnection)conn, (Throwable)ex);
/* 223 */       if (this.eventListener != null) {
/* 224 */         this.eventListener.fatalProtocolException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void inputReady(NHttpClientConnection conn, ContentDecoder decoder) {
/* 230 */     HttpContext context = conn.getContext();
/*     */     
/* 232 */     ClientConnState connState = (ClientConnState)context.getAttribute("http.nio.conn-state");
/*     */     
/* 234 */     ConsumingNHttpEntity consumingEntity = connState.getConsumingEntity();
/*     */     
/*     */     try {
/* 237 */       consumingEntity.consumeContent(decoder, (IOControl)conn);
/* 238 */       if (decoder.isCompleted()) {
/* 239 */         processResponse(conn, connState);
/*     */       }
/*     */     }
/* 242 */     catch (IOException ex) {
/* 243 */       shutdownConnection((NHttpConnection)conn, ex);
/* 244 */       if (this.eventListener != null) {
/* 245 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/* 247 */     } catch (HttpException ex) {
/* 248 */       closeConnection((NHttpConnection)conn, (Throwable)ex);
/* 249 */       if (this.eventListener != null) {
/* 250 */         this.eventListener.fatalProtocolException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void outputReady(NHttpClientConnection conn, ContentEncoder encoder) {
/* 256 */     HttpContext context = conn.getContext();
/* 257 */     ClientConnState connState = (ClientConnState)context.getAttribute("http.nio.conn-state");
/*     */     
/*     */     try {
/* 260 */       if (connState.getOutputState() == 2) {
/* 261 */         conn.suspendOutput();
/*     */         
/*     */         return;
/*     */       } 
/* 265 */       ProducingNHttpEntity entity = connState.getProducingEntity();
/*     */       
/* 267 */       entity.produceContent(encoder, (IOControl)conn);
/* 268 */       if (encoder.isCompleted()) {
/* 269 */         connState.setOutputState(8);
/*     */       }
/* 271 */     } catch (IOException ex) {
/* 272 */       shutdownConnection((NHttpConnection)conn, ex);
/* 273 */       if (this.eventListener != null) {
/* 274 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void responseReceived(NHttpClientConnection conn) {
/* 280 */     HttpContext context = conn.getContext();
/* 281 */     ClientConnState connState = (ClientConnState)context.getAttribute("http.nio.conn-state");
/*     */     
/* 283 */     HttpResponse response = conn.getHttpResponse();
/* 284 */     response.setParams((HttpParams)new DefaultedHttpParams(response.getParams(), this.params));
/*     */ 
/*     */     
/* 287 */     HttpRequest request = connState.getRequest();
/*     */     
/*     */     try {
/* 290 */       int statusCode = response.getStatusLine().getStatusCode();
/* 291 */       if (statusCode < 200) {
/*     */         
/* 293 */         if (statusCode == 100 && connState.getOutputState() == 2)
/*     */         {
/* 295 */           continueRequest(conn, connState);
/*     */         }
/*     */         return;
/*     */       } 
/* 299 */       connState.setResponse(response);
/* 300 */       if (connState.getOutputState() == 2) {
/* 301 */         cancelRequest(conn, connState);
/* 302 */       } else if (connState.getOutputState() == 4) {
/*     */         
/* 304 */         cancelRequest(conn, connState);
/* 305 */         connState.invalidate();
/* 306 */         conn.suspendOutput();
/*     */       } 
/*     */ 
/*     */       
/* 310 */       context.setAttribute("http.response", response);
/*     */       
/* 312 */       if (!canResponseHaveBody(request, response)) {
/* 313 */         conn.resetInput();
/* 314 */         response.setEntity(null);
/* 315 */         this.httpProcessor.process(response, context);
/* 316 */         processResponse(conn, connState);
/*     */       } else {
/* 318 */         HttpEntity entity = response.getEntity();
/* 319 */         if (entity != null) {
/* 320 */           ConsumingNHttpEntity consumingEntity = this.execHandler.responseEntity(response, context);
/*     */           
/* 322 */           if (consumingEntity == null) {
/* 323 */             consumingEntity = new NullNHttpEntity(entity);
/*     */           }
/* 325 */           response.setEntity((HttpEntity)consumingEntity);
/* 326 */           connState.setConsumingEntity(consumingEntity);
/* 327 */           this.httpProcessor.process(response, context);
/*     */         }
/*     */       
/*     */       }
/*     */     
/* 332 */     } catch (IOException ex) {
/* 333 */       shutdownConnection((NHttpConnection)conn, ex);
/* 334 */       if (this.eventListener != null) {
/* 335 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/* 337 */     } catch (HttpException ex) {
/* 338 */       closeConnection((NHttpConnection)conn, (Throwable)ex);
/* 339 */       if (this.eventListener != null) {
/* 340 */         this.eventListener.fatalProtocolException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void timeout(NHttpClientConnection conn) {
/* 346 */     HttpContext context = conn.getContext();
/* 347 */     ClientConnState connState = (ClientConnState)context.getAttribute("http.nio.conn-state");
/*     */ 
/*     */     
/*     */     try {
/* 351 */       if (connState.getOutputState() == 2) {
/* 352 */         continueRequest(conn, connState);
/*     */         
/*     */         return;
/*     */       } 
/* 356 */     } catch (IOException ex) {
/* 357 */       shutdownConnection((NHttpConnection)conn, ex);
/* 358 */       if (this.eventListener != null) {
/* 359 */         this.eventListener.fatalIOException(ex, (NHttpConnection)conn);
/*     */       }
/*     */     } 
/*     */     
/* 363 */     handleTimeout((NHttpConnection)conn);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void initialize(NHttpClientConnection conn, Object attachment) {
/* 369 */     HttpContext context = conn.getContext();
/*     */     
/* 371 */     context.setAttribute("http.connection", conn);
/* 372 */     this.execHandler.initalizeContext(context, attachment);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void continueRequest(NHttpClientConnection conn, ClientConnState connState) throws IOException {
/* 382 */     int timeout = connState.getTimeout();
/* 383 */     conn.setSocketTimeout(timeout);
/*     */     
/* 385 */     conn.requestOutput();
/* 386 */     connState.setOutputState(4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cancelRequest(NHttpClientConnection conn, ClientConnState connState) throws IOException {
/* 393 */     int timeout = connState.getTimeout();
/* 394 */     conn.setSocketTimeout(timeout);
/*     */     
/* 396 */     conn.resetOutput();
/* 397 */     connState.resetOutput();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processResponse(NHttpClientConnection conn, ClientConnState connState) throws IOException, HttpException {
/* 407 */     if (!connState.isValid()) {
/* 408 */       conn.close();
/*     */     }
/*     */     
/* 411 */     HttpContext context = conn.getContext();
/* 412 */     HttpResponse response = connState.getResponse();
/* 413 */     this.execHandler.handleResponse(response, context);
/* 414 */     if (!this.connStrategy.keepAlive(response, context)) {
/* 415 */       conn.close();
/*     */     }
/*     */     
/* 418 */     if (conn.isOpen()) {
/*     */       
/* 420 */       connState.resetInput();
/* 421 */       connState.resetOutput();
/* 422 */       conn.requestOutput();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected static class ClientConnState
/*     */   {
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
/*     */     public static final int RESPONSE_BODY_DONE = 64;
/*     */     private int outputState;
/*     */     private HttpRequest request;
/*     */     private HttpResponse response;
/*     */     private ConsumingNHttpEntity consumingEntity;
/*     */     private ProducingNHttpEntity producingEntity;
/*     */     private boolean valid = true;
/*     */     private int timeout;
/*     */     
/*     */     public void setConsumingEntity(ConsumingNHttpEntity consumingEntity) {
/* 452 */       this.consumingEntity = consumingEntity;
/*     */     }
/*     */     
/*     */     public void setProducingEntity(ProducingNHttpEntity producingEntity) {
/* 456 */       this.producingEntity = producingEntity;
/*     */     }
/*     */     
/*     */     public ProducingNHttpEntity getProducingEntity() {
/* 460 */       return this.producingEntity;
/*     */     }
/*     */     
/*     */     public ConsumingNHttpEntity getConsumingEntity() {
/* 464 */       return this.consumingEntity;
/*     */     }
/*     */     
/*     */     public int getOutputState() {
/* 468 */       return this.outputState;
/*     */     }
/*     */     
/*     */     public void setOutputState(int outputState) {
/* 472 */       this.outputState = outputState;
/*     */     }
/*     */     
/*     */     public HttpRequest getRequest() {
/* 476 */       return this.request;
/*     */     }
/*     */     
/*     */     public void setRequest(HttpRequest request) {
/* 480 */       this.request = request;
/*     */     }
/*     */     
/*     */     public HttpResponse getResponse() {
/* 484 */       return this.response;
/*     */     }
/*     */     
/*     */     public void setResponse(HttpResponse response) {
/* 488 */       this.response = response;
/*     */     }
/*     */     
/*     */     public int getTimeout() {
/* 492 */       return this.timeout;
/*     */     }
/*     */     
/*     */     public void setTimeout(int timeout) {
/* 496 */       this.timeout = timeout;
/*     */     }
/*     */     
/*     */     public void resetInput() throws IOException {
/* 500 */       this.response = null;
/* 501 */       if (this.consumingEntity != null) {
/* 502 */         this.consumingEntity.finish();
/* 503 */         this.consumingEntity = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void resetOutput() throws IOException {
/* 508 */       this.request = null;
/* 509 */       if (this.producingEntity != null) {
/* 510 */         this.producingEntity.finish();
/* 511 */         this.producingEntity = null;
/*     */       } 
/* 513 */       this.outputState = 0;
/*     */     }
/*     */     
/*     */     public void reset() throws IOException {
/* 517 */       resetInput();
/* 518 */       resetOutput();
/*     */     }
/*     */     
/*     */     public boolean isValid() {
/* 522 */       return this.valid;
/*     */     }
/*     */     
/*     */     public void invalidate() {
/* 526 */       this.valid = false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/protocol/AsyncNHttpClientHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
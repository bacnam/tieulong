/*     */ package org.apache.http.nio.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketTimeoutException;
/*     */ import org.apache.http.ConnectionClosedException;
/*     */ import org.apache.http.ExceptionLogger;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.nio.ContentDecoder;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.IOControl;
/*     */ import org.apache.http.nio.NHttpClientConnection;
/*     */ import org.apache.http.nio.NHttpClientEventHandler;
/*     */ import org.apache.http.nio.NHttpConnection;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class HttpAsyncRequestExecutor
/*     */   implements NHttpClientEventHandler
/*     */ {
/*     */   public static final int DEFAULT_WAIT_FOR_CONTINUE = 3000;
/*     */   public static final String HTTP_HANDLER = "http.nio.exchange-handler";
/*     */   private final int waitForContinue;
/*     */   private final ExceptionLogger exceptionLogger;
/*     */   static final String HTTP_EXCHANGE_STATE = "http.nio.http-exchange-state";
/*     */   
/*     */   public HttpAsyncRequestExecutor(int waitForContinue, ExceptionLogger exceptionLogger) {
/* 103 */     this.waitForContinue = Args.positive(waitForContinue, "Wait for continue time");
/* 104 */     this.exceptionLogger = (exceptionLogger != null) ? exceptionLogger : ExceptionLogger.NO_OP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpAsyncRequestExecutor(int waitForContinue) {
/* 113 */     this(waitForContinue, null);
/*     */   }
/*     */   
/*     */   public HttpAsyncRequestExecutor() {
/* 117 */     this(3000, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connected(NHttpClientConnection conn, Object attachment) throws IOException, HttpException {
/* 124 */     State state = new State();
/* 125 */     HttpContext context = conn.getContext();
/* 126 */     context.setAttribute("http.nio.http-exchange-state", state);
/* 127 */     requestReady(conn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void closed(NHttpClientConnection conn) {
/* 132 */     State state = getState((NHttpConnection)conn);
/* 133 */     HttpAsyncClientExchangeHandler handler = getHandler((NHttpConnection)conn);
/* 134 */     if (state != null && (
/* 135 */       state.getRequestState() != MessageState.READY || state.getResponseState() != MessageState.READY) && 
/* 136 */       handler != null) {
/* 137 */       handler.failed((Exception)new ConnectionClosedException("Connection closed unexpectedly"));
/*     */     }
/*     */ 
/*     */     
/* 141 */     if (state == null || (handler != null && handler.isDone())) {
/* 142 */       closeHandler(handler);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void exception(NHttpClientConnection conn, Exception cause) {
/* 149 */     shutdownConnection((NHttpConnection)conn);
/* 150 */     HttpAsyncClientExchangeHandler handler = getHandler((NHttpConnection)conn);
/* 151 */     if (handler != null) {
/* 152 */       handler.failed(cause);
/*     */     } else {
/* 154 */       log(cause);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void requestReady(NHttpClientConnection conn) throws IOException, HttpException {
/* 161 */     State state = getState((NHttpConnection)conn);
/* 162 */     Asserts.notNull(state, "Connection state");
/* 163 */     Asserts.check((state.getRequestState() == MessageState.READY || state.getRequestState() == MessageState.COMPLETED), "Unexpected request state %s", state.getRequestState());
/*     */ 
/*     */ 
/*     */     
/* 167 */     if (state.getRequestState() != MessageState.READY) {
/*     */       return;
/*     */     }
/* 170 */     HttpAsyncClientExchangeHandler handler = getHandler((NHttpConnection)conn);
/* 171 */     if (handler == null || handler.isDone()) {
/*     */       return;
/*     */     }
/* 174 */     boolean pipelined = (handler.getClass().getAnnotation(Pipelined.class) != null);
/*     */     
/* 176 */     HttpRequest request = handler.generateRequest();
/* 177 */     if (request == null) {
/*     */       return;
/*     */     }
/* 180 */     ProtocolVersion version = request.getRequestLine().getProtocolVersion();
/* 181 */     if (pipelined && version.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
/* 182 */       throw new ProtocolException(version + " cannot be used with request pipelining");
/*     */     }
/* 184 */     state.setRequest(request);
/*     */     
/* 186 */     if (request instanceof HttpEntityEnclosingRequest) {
/* 187 */       boolean expectContinue = ((HttpEntityEnclosingRequest)request).expectContinue();
/* 188 */       if (expectContinue && pipelined) {
/* 189 */         throw new ProtocolException("Expect-continue handshake cannot be used with request pipelining");
/*     */       }
/* 191 */       conn.submitRequest(request);
/* 192 */       if (expectContinue) {
/* 193 */         int timeout = conn.getSocketTimeout();
/* 194 */         state.setTimeout(timeout);
/* 195 */         conn.setSocketTimeout(this.waitForContinue);
/* 196 */         state.setRequestState(MessageState.ACK_EXPECTED);
/*     */       } else {
/* 198 */         HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
/* 199 */         if (entity != null) {
/* 200 */           state.setRequestState(MessageState.BODY_STREAM);
/*     */         } else {
/* 202 */           handler.requestCompleted();
/* 203 */           state.setRequestState(pipelined ? MessageState.READY : MessageState.COMPLETED);
/*     */         } 
/*     */       } 
/*     */     } else {
/* 207 */       conn.submitRequest(request);
/* 208 */       handler.requestCompleted();
/* 209 */       state.setRequestState(pipelined ? MessageState.READY : MessageState.COMPLETED);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void outputReady(NHttpClientConnection conn, ContentEncoder encoder) throws IOException, HttpException {
/* 217 */     State state = getState((NHttpConnection)conn);
/* 218 */     Asserts.notNull(state, "Connection state");
/* 219 */     Asserts.check((state.getRequestState() == MessageState.BODY_STREAM || state.getRequestState() == MessageState.ACK_EXPECTED), "Unexpected request state %s", state.getRequestState());
/*     */ 
/*     */ 
/*     */     
/* 223 */     HttpAsyncClientExchangeHandler handler = getHandler((NHttpConnection)conn);
/* 224 */     Asserts.notNull(handler, "Client exchange handler");
/* 225 */     if (state.getRequestState() == MessageState.ACK_EXPECTED) {
/* 226 */       conn.suspendOutput();
/*     */       return;
/*     */     } 
/* 229 */     handler.produceContent(encoder, (IOControl)conn);
/* 230 */     if (encoder.isCompleted()) {
/* 231 */       handler.requestCompleted();
/* 232 */       boolean pipelined = (handler.getClass().getAnnotation(Pipelined.class) != null);
/* 233 */       state.setRequestState(pipelined ? MessageState.READY : MessageState.COMPLETED);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void responseReceived(NHttpClientConnection conn) throws HttpException, IOException {
/* 240 */     State state = getState((NHttpConnection)conn);
/* 241 */     Asserts.notNull(state, "Connection state");
/* 242 */     Asserts.check((state.getResponseState() == MessageState.READY), "Unexpected request state %s", state.getResponseState());
/*     */ 
/*     */     
/* 245 */     HttpRequest request = state.getRequest();
/* 246 */     if (request == null) {
/* 247 */       throw new HttpException("Out of sequence response");
/*     */     }
/* 249 */     HttpAsyncClientExchangeHandler handler = getHandler((NHttpConnection)conn);
/* 250 */     Asserts.notNull(handler, "Client exchange handler");
/* 251 */     HttpResponse response = conn.getHttpResponse();
/*     */     
/* 253 */     int statusCode = response.getStatusLine().getStatusCode();
/* 254 */     if (statusCode < 200) {
/*     */       
/* 256 */       if (statusCode != 100) {
/* 257 */         throw new ProtocolException("Unexpected response: " + response.getStatusLine());
/*     */       }
/*     */       
/* 260 */       if (state.getRequestState() == MessageState.ACK_EXPECTED) {
/* 261 */         int timeout = state.getTimeout();
/* 262 */         conn.setSocketTimeout(timeout);
/* 263 */         conn.requestOutput();
/* 264 */         state.setRequestState(MessageState.BODY_STREAM);
/*     */       } 
/*     */       return;
/*     */     } 
/* 268 */     state.setResponse(response);
/* 269 */     if (state.getRequestState() == MessageState.ACK_EXPECTED) {
/* 270 */       int timeout = state.getTimeout();
/* 271 */       conn.setSocketTimeout(timeout);
/* 272 */       conn.resetOutput();
/* 273 */       state.setRequestState(MessageState.COMPLETED);
/* 274 */     } else if (state.getRequestState() == MessageState.BODY_STREAM) {
/*     */       
/* 276 */       conn.resetOutput();
/* 277 */       conn.suspendOutput();
/* 278 */       state.setRequestState(MessageState.COMPLETED);
/* 279 */       state.invalidate();
/*     */     } 
/*     */     
/* 282 */     handler.responseReceived(response);
/*     */     
/* 284 */     state.setResponseState(MessageState.BODY_STREAM);
/* 285 */     if (!canResponseHaveBody(request, response)) {
/* 286 */       response.setEntity(null);
/* 287 */       conn.resetInput();
/* 288 */       processResponse(conn, state, handler);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void inputReady(NHttpClientConnection conn, ContentDecoder decoder) throws IOException, HttpException {
/* 296 */     State state = getState((NHttpConnection)conn);
/* 297 */     Asserts.notNull(state, "Connection state");
/* 298 */     Asserts.check((state.getResponseState() == MessageState.BODY_STREAM), "Unexpected request state %s", state.getResponseState());
/*     */ 
/*     */     
/* 301 */     HttpAsyncClientExchangeHandler handler = getHandler((NHttpConnection)conn);
/* 302 */     Asserts.notNull(handler, "Client exchange handler");
/* 303 */     handler.consumeContent(decoder, (IOControl)conn);
/* 304 */     if (decoder.isCompleted()) {
/* 305 */       processResponse(conn, state, handler);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void endOfInput(NHttpClientConnection conn) throws IOException {
/* 311 */     State state = getState((NHttpConnection)conn);
/* 312 */     if (state != null) {
/* 313 */       if (state.getRequestState().compareTo(MessageState.READY) != 0) {
/* 314 */         state.invalidate();
/*     */       }
/* 316 */       HttpAsyncClientExchangeHandler handler = getHandler((NHttpConnection)conn);
/* 317 */       if (handler != null) {
/* 318 */         if (state.isValid()) {
/* 319 */           handler.inputTerminated();
/*     */         } else {
/* 321 */           handler.failed((Exception)new ConnectionClosedException("Connection closed"));
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 329 */     if (conn.getSocketTimeout() <= 0) {
/* 330 */       conn.setSocketTimeout(1000);
/*     */     }
/* 332 */     conn.close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void timeout(NHttpClientConnection conn) throws IOException {
/* 338 */     State state = getState((NHttpConnection)conn);
/* 339 */     if (state != null) {
/* 340 */       if (state.getRequestState() == MessageState.ACK_EXPECTED) {
/* 341 */         int timeout = state.getTimeout();
/* 342 */         conn.setSocketTimeout(timeout);
/* 343 */         conn.requestOutput();
/* 344 */         state.setRequestState(MessageState.BODY_STREAM);
/* 345 */         state.setTimeout(0);
/*     */         return;
/*     */       } 
/* 348 */       state.invalidate();
/* 349 */       HttpAsyncClientExchangeHandler handler = getHandler((NHttpConnection)conn);
/* 350 */       if (handler != null) {
/* 351 */         handler.failed(new SocketTimeoutException());
/* 352 */         handler.close();
/*     */       } 
/*     */     } 
/*     */     
/* 356 */     if (conn.getStatus() == 0) {
/* 357 */       conn.close();
/* 358 */       if (conn.getStatus() == 1)
/*     */       {
/*     */         
/* 361 */         conn.setSocketTimeout(250);
/*     */       }
/*     */     } else {
/* 364 */       conn.shutdown();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void log(Exception ex) {
/* 376 */     this.exceptionLogger.log(ex);
/*     */   }
/*     */   
/*     */   private State getState(NHttpConnection conn) {
/* 380 */     return (State)conn.getContext().getAttribute("http.nio.http-exchange-state");
/*     */   }
/*     */   
/*     */   private HttpAsyncClientExchangeHandler getHandler(NHttpConnection conn) {
/* 384 */     return (HttpAsyncClientExchangeHandler)conn.getContext().getAttribute("http.nio.exchange-handler");
/*     */   }
/*     */   
/*     */   private void shutdownConnection(NHttpConnection conn) {
/*     */     try {
/* 389 */       conn.shutdown();
/* 390 */     } catch (IOException ex) {
/* 391 */       log(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void closeHandler(HttpAsyncClientExchangeHandler handler) {
/* 396 */     if (handler != null) {
/*     */       try {
/* 398 */         handler.close();
/* 399 */       } catch (IOException ioex) {
/* 400 */         log(ioex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processResponse(NHttpClientConnection conn, State state, HttpAsyncClientExchangeHandler handler) throws IOException, HttpException {
/* 409 */     if (!state.isValid()) {
/* 410 */       conn.close();
/*     */     }
/* 412 */     handler.responseCompleted();
/*     */     
/* 414 */     boolean pipelined = (handler.getClass().getAnnotation(Pipelined.class) != null);
/* 415 */     if (!pipelined) {
/* 416 */       state.setRequestState(MessageState.READY);
/* 417 */       state.setRequest(null);
/*     */     } 
/* 419 */     state.setResponseState(MessageState.READY);
/* 420 */     state.setResponse(null);
/* 421 */     if (!handler.isDone() && conn.isOpen()) {
/* 422 */       conn.requestOutput();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canResponseHaveBody(HttpRequest request, HttpResponse response) {
/* 428 */     String method = request.getRequestLine().getMethod();
/* 429 */     int status = response.getStatusLine().getStatusCode();
/*     */     
/* 431 */     if (method.equalsIgnoreCase("HEAD")) {
/* 432 */       return false;
/*     */     }
/* 434 */     if (method.equalsIgnoreCase("CONNECT") && status < 300) {
/* 435 */       return false;
/*     */     }
/* 437 */     return (status >= 200 && status != 204 && status != 304 && status != 205);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class State
/*     */   {
/*     */     private volatile boolean valid = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 457 */     private volatile MessageState requestState = MessageState.READY;
/* 458 */     private volatile MessageState responseState = MessageState.READY;
/*     */     private volatile HttpRequest request;
/*     */     
/*     */     public MessageState getRequestState() {
/* 462 */       return this.requestState;
/*     */     }
/*     */     private volatile HttpResponse response; private volatile int timeout;
/*     */     public void setRequestState(MessageState state) {
/* 466 */       this.requestState = state;
/*     */     }
/*     */     
/*     */     public MessageState getResponseState() {
/* 470 */       return this.responseState;
/*     */     }
/*     */     
/*     */     public void setResponseState(MessageState state) {
/* 474 */       this.responseState = state;
/*     */     }
/*     */     
/*     */     public HttpRequest getRequest() {
/* 478 */       return this.request;
/*     */     }
/*     */     
/*     */     public void setRequest(HttpRequest request) {
/* 482 */       this.request = request;
/*     */     }
/*     */     
/*     */     public HttpResponse getResponse() {
/* 486 */       return this.response;
/*     */     }
/*     */     
/*     */     public void setResponse(HttpResponse response) {
/* 490 */       this.response = response;
/*     */     }
/*     */     
/*     */     public int getTimeout() {
/* 494 */       return this.timeout;
/*     */     }
/*     */     
/*     */     public void setTimeout(int timeout) {
/* 498 */       this.timeout = timeout;
/*     */     }
/*     */     
/*     */     public boolean isValid() {
/* 502 */       return this.valid;
/*     */     }
/*     */     
/*     */     public void invalidate() {
/* 506 */       this.valid = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 511 */       StringBuilder buf = new StringBuilder();
/* 512 */       buf.append("request state: ");
/* 513 */       buf.append(this.requestState);
/* 514 */       buf.append("; request: ");
/* 515 */       if (this.request != null) {
/* 516 */         buf.append(this.request.getRequestLine());
/*     */       }
/* 518 */       buf.append("; response state: ");
/* 519 */       buf.append(this.responseState);
/* 520 */       buf.append("; response: ");
/* 521 */       if (this.response != null) {
/* 522 */         buf.append(this.response.getStatusLine());
/*     */       }
/* 524 */       buf.append("; valid: ");
/* 525 */       buf.append(this.valid);
/* 526 */       buf.append(";");
/* 527 */       return buf.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/protocol/HttpAsyncRequestExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
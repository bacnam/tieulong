/*     */ package org.apache.http.impl.nio.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.http.ConnectionClosedException;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.auth.AuthProtocolState;
/*     */ import org.apache.http.auth.AuthScheme;
/*     */ import org.apache.http.auth.AuthState;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.UsernamePasswordCredentials;
/*     */ import org.apache.http.client.AuthenticationStrategy;
/*     */ import org.apache.http.client.CredentialsProvider;
/*     */ import org.apache.http.client.NonRepeatableRequestException;
/*     */ import org.apache.http.client.RedirectException;
/*     */ import org.apache.http.client.RedirectStrategy;
/*     */ import org.apache.http.client.UserTokenHandler;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.AbortableHttpRequest;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.params.HttpClientParams;
/*     */ import org.apache.http.client.utils.URIUtils;
/*     */ import org.apache.http.concurrent.FutureCallback;
/*     */ import org.apache.http.conn.ConnectionKeepAliveStrategy;
/*     */ import org.apache.http.conn.ConnectionReleaseTrigger;
/*     */ import org.apache.http.conn.routing.BasicRouteDirector;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.routing.HttpRouteDirector;
/*     */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*     */ import org.apache.http.conn.routing.RouteInfo;
/*     */ import org.apache.http.impl.auth.BasicScheme;
/*     */ import org.apache.http.impl.client.ClientParamsStack;
/*     */ import org.apache.http.impl.client.EntityEnclosingRequestWrapper;
/*     */ import org.apache.http.impl.client.HttpAuthenticator;
/*     */ import org.apache.http.impl.client.RequestWrapper;
/*     */ import org.apache.http.impl.client.RoutedRequest;
/*     */ import org.apache.http.message.BasicHttpRequest;
/*     */ import org.apache.http.nio.ContentDecoder;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.IOControl;
/*     */ import org.apache.http.nio.conn.ClientAsyncConnectionManager;
/*     */ import org.apache.http.nio.conn.ManagedClientAsyncConnection;
/*     */ import org.apache.http.nio.conn.scheme.AsyncScheme;
/*     */ import org.apache.http.nio.conn.scheme.AsyncSchemeRegistry;
/*     */ import org.apache.http.nio.protocol.HttpAsyncRequestExecutionHandler;
/*     */ import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
/*     */ import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
/*     */ import org.apache.http.params.HttpConnectionParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.params.HttpProtocolParams;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.protocol.HttpProcessor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ class DefaultAsyncRequestDirector<T>
/*     */   implements HttpAsyncRequestExecutionHandler<T>
/*     */ {
/*  99 */   private static final AtomicLong COUNTER = new AtomicLong(1L);
/*     */   
/*     */   private final Log log;
/*     */   
/*     */   private final HttpAsyncRequestProducer requestProducer;
/*     */   
/*     */   private final HttpAsyncResponseConsumer<T> responseConsumer;
/*     */   
/*     */   private final HttpContext localContext;
/*     */   
/*     */   private final ResultCallback<T> resultCallback;
/*     */   
/*     */   private final ClientAsyncConnectionManager connmgr;
/*     */   
/*     */   private final HttpProcessor httppocessor;
/*     */   
/*     */   private final HttpRoutePlanner routePlanner;
/*     */   
/*     */   private final HttpRouteDirector routeDirector;
/*     */   
/*     */   private final ConnectionReuseStrategy reuseStrategy;
/*     */   
/*     */   private final ConnectionKeepAliveStrategy keepaliveStrategy;
/*     */   
/*     */   private final RedirectStrategy redirectStrategy;
/*     */   
/*     */   private final AuthenticationStrategy targetAuthStrategy;
/*     */   
/*     */   private final AuthenticationStrategy proxyAuthStrategy;
/*     */   
/*     */   private final UserTokenHandler userTokenHandler;
/*     */   
/*     */   private final AuthState targetAuthState;
/*     */   
/*     */   private final AuthState proxyAuthState;
/*     */   
/*     */   private final HttpAuthenticator authenticator;
/*     */   
/*     */   private final HttpParams clientParams;
/*     */   
/*     */   private final long id;
/*     */   
/*     */   private volatile boolean closed;
/*     */   private volatile InternalFutureCallback connRequestCallback;
/*     */   private volatile ManagedClientAsyncConnection managedConn;
/*     */   private RoutedRequest mainRequest;
/*     */   private RoutedRequest followup;
/*     */   private HttpResponse finalResponse;
/*     */   private ClientParamsStack params;
/*     */   private RequestWrapper currentRequest;
/*     */   private HttpResponse currentResponse;
/*     */   private boolean routeEstablished;
/*     */   private int redirectCount;
/*     */   private ByteBuffer tmpbuf;
/*     */   private boolean requestContentProduced;
/*     */   private boolean requestSent;
/*     */   private int execCount;
/*     */   
/*     */   public DefaultAsyncRequestDirector(Log log, HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, HttpContext localContext, ResultCallback<T> callback, ClientAsyncConnectionManager connmgr, HttpProcessor httppocessor, HttpRoutePlanner routePlanner, ConnectionReuseStrategy reuseStrategy, ConnectionKeepAliveStrategy keepaliveStrategy, RedirectStrategy redirectStrategy, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler, HttpParams clientParams) {
/* 158 */     this.log = log;
/* 159 */     this.requestProducer = requestProducer;
/* 160 */     this.responseConsumer = responseConsumer;
/* 161 */     this.localContext = localContext;
/* 162 */     this.resultCallback = callback;
/* 163 */     this.connmgr = connmgr;
/* 164 */     this.httppocessor = httppocessor;
/* 165 */     this.routePlanner = routePlanner;
/* 166 */     this.reuseStrategy = reuseStrategy;
/* 167 */     this.keepaliveStrategy = keepaliveStrategy;
/* 168 */     this.redirectStrategy = redirectStrategy;
/* 169 */     this.routeDirector = (HttpRouteDirector)new BasicRouteDirector();
/* 170 */     this.targetAuthStrategy = targetAuthStrategy;
/* 171 */     this.proxyAuthStrategy = proxyAuthStrategy;
/* 172 */     this.userTokenHandler = userTokenHandler;
/* 173 */     this.targetAuthState = new AuthState();
/* 174 */     this.proxyAuthState = new AuthState();
/* 175 */     this.authenticator = new HttpAuthenticator(log);
/* 176 */     this.clientParams = clientParams;
/* 177 */     this.id = COUNTER.getAndIncrement();
/*     */   }
/*     */   
/*     */   public void close() {
/* 181 */     if (this.closed) {
/*     */       return;
/*     */     }
/* 184 */     this.closed = true;
/* 185 */     ManagedClientAsyncConnection localConn = this.managedConn;
/* 186 */     if (localConn != null) {
/* 187 */       if (this.log.isDebugEnabled()) {
/* 188 */         this.log.debug("[exchange: " + this.id + "] aborting connection " + localConn);
/*     */       }
/*     */       try {
/* 191 */         localConn.abortConnection();
/* 192 */       } catch (IOException ioex) {
/* 193 */         this.log.debug("I/O error releasing connection", ioex);
/*     */       } 
/*     */     } 
/*     */     try {
/* 197 */       this.requestProducer.close();
/* 198 */     } catch (IOException ex) {
/* 199 */       this.log.debug("I/O error closing request producer", ex);
/*     */     } 
/*     */     try {
/* 202 */       this.responseConsumer.close();
/* 203 */     } catch (IOException ex) {
/* 204 */       this.log.debug("I/O error closing response consumer", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void start() {
/*     */     try {
/* 210 */       if (this.log.isDebugEnabled()) {
/* 211 */         this.log.debug("[exchange: " + this.id + "] start execution");
/*     */       }
/* 213 */       this.localContext.setAttribute("http.auth.target-scope", this.targetAuthState);
/* 214 */       this.localContext.setAttribute("http.auth.proxy-scope", this.proxyAuthState);
/*     */       
/* 216 */       HttpHost target = this.requestProducer.getTarget();
/* 217 */       HttpRequest request = this.requestProducer.generateRequest();
/* 218 */       if (request instanceof AbortableHttpRequest) {
/* 219 */         ((AbortableHttpRequest)request).setReleaseTrigger(new ConnectionReleaseTrigger()
/*     */             {
/*     */               public void releaseConnection() throws IOException {}
/*     */ 
/*     */               
/*     */               public void abortConnection() throws IOException {
/* 225 */                 DefaultAsyncRequestDirector.this.cancel();
/*     */               }
/*     */             });
/*     */       }
/*     */       
/* 230 */       this.params = new ClientParamsStack(null, this.clientParams, request.getParams(), null);
/* 231 */       RequestWrapper wrapper = wrapRequest(request);
/* 232 */       wrapper.setParams((HttpParams)this.params);
/* 233 */       HttpRoute route = determineRoute(target, (HttpRequest)wrapper, this.localContext);
/* 234 */       this.mainRequest = new RoutedRequest(wrapper, route);
/* 235 */       RequestConfig config = ParamConfig.getRequestConfig((HttpParams)this.params);
/* 236 */       this.localContext.setAttribute("http.request-config", config);
/* 237 */       this.requestContentProduced = false;
/* 238 */       requestConnection();
/* 239 */     } catch (Exception ex) {
/* 240 */       failed(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public HttpHost getTarget() {
/* 245 */     return this.requestProducer.getTarget();
/*     */   }
/*     */   
/*     */   public synchronized HttpRequest generateRequest() throws IOException, HttpException {
/* 249 */     HttpRoute route = this.mainRequest.getRoute();
/* 250 */     if (!this.routeEstablished) {
/*     */       int step; do {
/*     */         HttpRequest connect;
/* 253 */         HttpRoute fact = this.managedConn.getRoute();
/* 254 */         step = this.routeDirector.nextStep((RouteInfo)route, (RouteInfo)fact);
/* 255 */         switch (step) {
/*     */           case 1:
/*     */           case 2:
/*     */             break;
/*     */           case 3:
/* 260 */             if (this.log.isDebugEnabled()) {
/* 261 */               this.log.debug("[exchange: " + this.id + "] Tunnel required");
/*     */             }
/* 263 */             connect = createConnectRequest(route);
/* 264 */             this.currentRequest = wrapRequest(connect);
/* 265 */             this.currentRequest.setParams((HttpParams)this.params);
/*     */             break;
/*     */           case 4:
/* 268 */             throw new HttpException("Proxy chains are not supported");
/*     */           case 5:
/* 270 */             this.managedConn.layerProtocol(this.localContext, (HttpParams)this.params);
/*     */             break;
/*     */           case -1:
/* 273 */             throw new HttpException("Unable to establish route: planned = " + route + "; current = " + fact);
/*     */           
/*     */           case 0:
/* 276 */             this.routeEstablished = true;
/*     */             break;
/*     */           default:
/* 279 */             throw new IllegalStateException("Unknown step indicator " + step + " from RouteDirector.");
/*     */         } 
/*     */       
/* 282 */       } while (step > 0 && this.currentRequest == null);
/*     */     } 
/*     */     
/* 285 */     HttpHost target = (HttpHost)this.params.getParameter("http.virtual-host");
/* 286 */     if (target == null) {
/* 287 */       target = route.getTargetHost();
/*     */     }
/* 289 */     HttpHost proxy = route.getProxyHost();
/* 290 */     this.localContext.setAttribute("http.target_host", target);
/* 291 */     this.localContext.setAttribute("http.proxy_host", proxy);
/* 292 */     this.localContext.setAttribute("http.connection", this.managedConn);
/* 293 */     this.localContext.setAttribute("http.route", route);
/*     */     
/* 295 */     if (this.currentRequest == null) {
/* 296 */       this.currentRequest = this.mainRequest.getRequest();
/*     */       
/* 298 */       String userinfo = this.currentRequest.getURI().getUserInfo();
/* 299 */       if (userinfo != null) {
/* 300 */         this.targetAuthState.update((AuthScheme)new BasicScheme(), (Credentials)new UsernamePasswordCredentials(userinfo));
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 305 */       rewriteRequestURI(this.currentRequest, route);
/*     */     } 
/*     */     
/* 308 */     this.currentRequest.resetHeaders();
/*     */     
/* 310 */     this.currentRequest.incrementExecCount();
/* 311 */     if (this.currentRequest.getExecCount() > 1 && !this.requestProducer.isRepeatable() && this.requestContentProduced)
/*     */     {
/*     */       
/* 314 */       throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.");
/*     */     }
/*     */     
/* 317 */     this.execCount++;
/* 318 */     if (this.log.isDebugEnabled()) {
/* 319 */       this.log.debug("[exchange: " + this.id + "] Attempt " + this.execCount + " to execute request");
/*     */     }
/* 321 */     return (HttpRequest)this.currentRequest;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void produceContent(ContentEncoder encoder, IOControl ioctrl) throws IOException {
/* 326 */     if (this.log.isDebugEnabled()) {
/* 327 */       this.log.debug("[exchange: " + this.id + "] produce content");
/*     */     }
/* 329 */     this.requestContentProduced = true;
/* 330 */     this.requestProducer.produceContent(encoder, ioctrl);
/* 331 */     if (encoder.isCompleted()) {
/* 332 */       this.requestProducer.resetRequest();
/*     */     }
/*     */   }
/*     */   
/*     */   public void requestCompleted(HttpContext context) {
/* 337 */     if (this.log.isDebugEnabled()) {
/* 338 */       this.log.debug("[exchange: " + this.id + "] Request completed");
/*     */     }
/* 340 */     this.requestSent = true;
/* 341 */     this.requestProducer.requestCompleted(context);
/*     */   }
/*     */   
/*     */   public boolean isRepeatable() {
/* 345 */     return this.requestProducer.isRepeatable();
/*     */   }
/*     */   
/*     */   public void resetRequest() throws IOException {
/* 349 */     this.requestSent = false;
/* 350 */     this.requestProducer.resetRequest();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void responseReceived(HttpResponse response) throws IOException, HttpException {
/* 355 */     if (this.log.isDebugEnabled()) {
/* 356 */       this.log.debug("[exchange: " + this.id + "] Response received " + response.getStatusLine());
/*     */     }
/* 358 */     this.currentResponse = response;
/* 359 */     this.currentResponse.setParams((HttpParams)this.params);
/*     */     
/* 361 */     int status = this.currentResponse.getStatusLine().getStatusCode();
/*     */     
/* 363 */     if (!this.routeEstablished) {
/* 364 */       String method = this.currentRequest.getMethod();
/* 365 */       if (method.equalsIgnoreCase("CONNECT") && status == 200) {
/* 366 */         this.managedConn.tunnelTarget((HttpParams)this.params);
/*     */       } else {
/* 368 */         this.followup = handleConnectResponse();
/* 369 */         if (this.followup == null) {
/* 370 */           this.finalResponse = response;
/*     */         }
/*     */       } 
/*     */     } else {
/* 374 */       this.followup = handleResponse();
/* 375 */       if (this.followup == null) {
/* 376 */         this.finalResponse = response;
/*     */       }
/*     */       
/* 379 */       Object userToken = this.localContext.getAttribute("http.user-token");
/* 380 */       if (this.managedConn != null) {
/* 381 */         if (userToken == null) {
/* 382 */           userToken = this.userTokenHandler.getUserToken(this.localContext);
/* 383 */           this.localContext.setAttribute("http.user-token", userToken);
/*     */         } 
/* 385 */         if (userToken != null) {
/* 386 */           this.managedConn.setState(userToken);
/*     */         }
/*     */       } 
/*     */     } 
/* 390 */     if (this.finalResponse != null) {
/* 391 */       this.responseConsumer.responseReceived(response);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void consumeContent(ContentDecoder decoder, IOControl ioctrl) throws IOException {
/* 397 */     if (this.log.isDebugEnabled()) {
/* 398 */       this.log.debug("[exchange: " + this.id + "] Consume content");
/*     */     }
/* 400 */     if (this.finalResponse != null) {
/* 401 */       this.responseConsumer.consumeContent(decoder, ioctrl);
/*     */     } else {
/* 403 */       if (this.tmpbuf == null) {
/* 404 */         this.tmpbuf = ByteBuffer.allocate(2048);
/*     */       }
/* 406 */       this.tmpbuf.clear();
/* 407 */       decoder.read(this.tmpbuf);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void releaseConnection() {
/* 412 */     if (this.managedConn != null) {
/* 413 */       if (this.log.isDebugEnabled()) {
/* 414 */         this.log.debug("[exchange: " + this.id + "] releasing connection " + this.managedConn);
/*     */       }
/*     */       try {
/* 417 */         this.managedConn.getContext().removeAttribute("http.nio.exchange-handler");
/* 418 */         this.managedConn.releaseConnection();
/* 419 */       } catch (IOException ioex) {
/* 420 */         this.log.debug("I/O error releasing connection", ioex);
/*     */       } 
/* 422 */       this.managedConn = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void failed(Exception ex) {
/*     */     try {
/* 428 */       if (!this.requestSent) {
/* 429 */         this.requestProducer.failed(ex);
/*     */       }
/* 431 */       this.responseConsumer.failed(ex);
/*     */     } finally {
/*     */       try {
/* 434 */         this.resultCallback.failed(ex, this);
/*     */       } finally {
/* 436 */         close();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void responseCompleted(HttpContext context) {
/* 442 */     if (this.log.isDebugEnabled()) {
/* 443 */       this.log.debug("[exchange: " + this.id + "] Response fully read");
/*     */     }
/*     */     try {
/* 446 */       if (this.resultCallback.isDone()) {
/*     */         return;
/*     */       }
/* 449 */       if (this.managedConn.isOpen()) {
/* 450 */         long duration = this.keepaliveStrategy.getKeepAliveDuration(this.currentResponse, this.localContext);
/*     */         
/* 452 */         if (this.log.isDebugEnabled()) {
/*     */           String s;
/* 454 */           if (duration > 0L) {
/* 455 */             s = "for " + duration + " " + TimeUnit.MILLISECONDS;
/*     */           } else {
/* 457 */             s = "indefinitely";
/*     */           } 
/* 459 */           this.log.debug("[exchange: " + this.id + "] Connection can be kept alive " + s);
/*     */         } 
/* 461 */         this.managedConn.setIdleDuration(duration, TimeUnit.MILLISECONDS);
/*     */       } else {
/* 463 */         if (this.log.isDebugEnabled()) {
/* 464 */           this.log.debug("[exchange: " + this.id + "] Connection cannot be kept alive");
/*     */         }
/* 466 */         this.managedConn.unmarkReusable();
/* 467 */         if (this.proxyAuthState.getState() == AuthProtocolState.SUCCESS && this.proxyAuthState.getAuthScheme() != null && this.proxyAuthState.getAuthScheme().isConnectionBased()) {
/*     */ 
/*     */           
/* 470 */           if (this.log.isDebugEnabled()) {
/* 471 */             this.log.debug("[exchange: " + this.id + "] Resetting proxy auth state");
/*     */           }
/* 473 */           this.proxyAuthState.reset();
/*     */         } 
/* 475 */         if (this.targetAuthState.getState() == AuthProtocolState.SUCCESS && this.targetAuthState.getAuthScheme() != null && this.targetAuthState.getAuthScheme().isConnectionBased()) {
/*     */ 
/*     */           
/* 478 */           if (this.log.isDebugEnabled()) {
/* 479 */             this.log.debug("[exchange: " + this.id + "] Resetting target auth state");
/*     */           }
/* 481 */           this.targetAuthState.reset();
/*     */         } 
/*     */       } 
/*     */       
/* 485 */       if (this.finalResponse != null) {
/* 486 */         this.responseConsumer.responseCompleted(this.localContext);
/* 487 */         if (this.log.isDebugEnabled()) {
/* 488 */           this.log.debug("[exchange: " + this.id + "] Response processed");
/*     */         }
/* 490 */         releaseConnection();
/* 491 */         T result = (T)this.responseConsumer.getResult();
/* 492 */         Exception ex = this.responseConsumer.getException();
/* 493 */         if (ex == null) {
/* 494 */           this.resultCallback.completed(result, this);
/*     */         } else {
/* 496 */           this.resultCallback.failed(ex, this);
/*     */         } 
/*     */       } else {
/* 499 */         if (this.followup != null) {
/* 500 */           HttpRoute actualRoute = this.mainRequest.getRoute();
/* 501 */           HttpRoute newRoute = this.followup.getRoute();
/* 502 */           if (!actualRoute.equals(newRoute)) {
/* 503 */             releaseConnection();
/*     */           }
/* 505 */           this.mainRequest = this.followup;
/*     */         } 
/* 507 */         if (this.managedConn != null && !this.managedConn.isOpen()) {
/* 508 */           releaseConnection();
/*     */         }
/* 510 */         if (this.managedConn != null) {
/* 511 */           this.managedConn.requestOutput();
/*     */         } else {
/* 513 */           requestConnection();
/*     */         } 
/*     */       } 
/* 516 */       this.followup = null;
/* 517 */       this.currentRequest = null;
/* 518 */       this.currentResponse = null;
/* 519 */     } catch (RuntimeException runex) {
/* 520 */       failed(runex);
/* 521 */       throw runex;
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized boolean cancel() {
/* 526 */     if (this.log.isDebugEnabled()) {
/* 527 */       this.log.debug("[exchange: " + this.id + "] Cancelled");
/*     */     }
/*     */     try {
/* 530 */       boolean cancelled = this.responseConsumer.cancel();
/*     */       
/* 532 */       T result = (T)this.responseConsumer.getResult();
/* 533 */       Exception ex = this.responseConsumer.getException();
/* 534 */       if (ex != null) {
/* 535 */         this.resultCallback.failed(ex, this);
/* 536 */       } else if (result != null) {
/* 537 */         this.resultCallback.completed(result, this);
/*     */       } else {
/* 539 */         this.resultCallback.cancelled(this);
/*     */       } 
/* 541 */       return cancelled;
/* 542 */     } catch (RuntimeException runex) {
/* 543 */       this.resultCallback.failed(runex, this);
/* 544 */       throw runex;
/*     */     } finally {
/* 546 */       close();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/* 551 */     return this.resultCallback.isDone();
/*     */   }
/*     */   
/*     */   public T getResult() {
/* 555 */     return (T)this.responseConsumer.getResult();
/*     */   }
/*     */   
/*     */   public Exception getException() {
/* 559 */     return this.responseConsumer.getException();
/*     */   }
/*     */   
/*     */   private synchronized void connectionRequestCompleted(ManagedClientAsyncConnection conn) {
/* 563 */     if (this.log.isDebugEnabled()) {
/* 564 */       this.log.debug("[exchange: " + this.id + "] Connection allocated: " + conn);
/*     */     }
/* 566 */     this.connRequestCallback = null;
/*     */     try {
/* 568 */       this.managedConn = conn;
/* 569 */       if (this.closed) {
/* 570 */         conn.releaseConnection();
/*     */         return;
/*     */       } 
/* 573 */       HttpRoute route = this.mainRequest.getRoute();
/* 574 */       if (!conn.isOpen()) {
/* 575 */         conn.open(route, this.localContext, (HttpParams)this.params);
/*     */       }
/* 577 */       conn.getContext().setAttribute("http.nio.exchange-handler", this);
/* 578 */       conn.requestOutput();
/* 579 */       this.routeEstablished = route.equals(conn.getRoute());
/* 580 */       if (!conn.isOpen()) {
/* 581 */         throw new ConnectionClosedException("Connection closed");
/*     */       }
/* 583 */     } catch (IOException ex) {
/* 584 */       failed(ex);
/* 585 */     } catch (RuntimeException runex) {
/* 586 */       failed(runex);
/* 587 */       throw runex;
/*     */     } 
/*     */   }
/*     */   
/*     */   private synchronized void connectionRequestFailed(Exception ex) {
/* 592 */     if (this.log.isDebugEnabled()) {
/* 593 */       this.log.debug("[exchange: " + this.id + "] connection request failed");
/*     */     }
/* 595 */     this.connRequestCallback = null;
/*     */     try {
/* 597 */       this.resultCallback.failed(ex, this);
/*     */     } finally {
/* 599 */       close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private synchronized void connectionRequestCancelled() {
/* 604 */     if (this.log.isDebugEnabled()) {
/* 605 */       this.log.debug("[exchange: " + this.id + "] Connection request cancelled");
/*     */     }
/* 607 */     this.connRequestCallback = null;
/*     */     try {
/* 609 */       this.resultCallback.cancelled(this);
/*     */     } finally {
/* 611 */       close();
/*     */     } 
/*     */   }
/*     */   
/*     */   class InternalFutureCallback
/*     */     implements FutureCallback<ManagedClientAsyncConnection> {
/*     */     public void completed(ManagedClientAsyncConnection session) {
/* 618 */       DefaultAsyncRequestDirector.this.connectionRequestCompleted(session);
/*     */     }
/*     */     
/*     */     public void failed(Exception ex) {
/* 622 */       DefaultAsyncRequestDirector.this.connectionRequestFailed(ex);
/*     */     }
/*     */     
/*     */     public void cancelled() {
/* 626 */       DefaultAsyncRequestDirector.this.connectionRequestCancelled();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void requestConnection() {
/* 632 */     HttpRoute route = this.mainRequest.getRoute();
/* 633 */     if (this.log.isDebugEnabled()) {
/* 634 */       this.log.debug("[exchange: " + this.id + "] Request connection for " + route);
/*     */     }
/* 636 */     long connectTimeout = HttpConnectionParams.getConnectionTimeout((HttpParams)this.params);
/* 637 */     Object userToken = this.localContext.getAttribute("http.user-token");
/* 638 */     this.connRequestCallback = new InternalFutureCallback();
/* 639 */     this.connmgr.leaseConnection(route, userToken, connectTimeout, TimeUnit.MILLISECONDS, this.connRequestCallback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void endOfStream() {
/* 646 */     if (this.managedConn != null) {
/* 647 */       if (this.log.isDebugEnabled()) {
/* 648 */         this.log.debug("[exchange: " + this.id + "] Unexpected end of data stream");
/*     */       }
/* 650 */       releaseConnection();
/* 651 */       if (this.connRequestCallback == null) {
/* 652 */         requestConnection();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
/* 661 */     HttpHost t = (target != null) ? target : (HttpHost)request.getParams().getParameter("http.default-host");
/*     */     
/* 663 */     if (t == null) {
/* 664 */       throw new IllegalStateException("Target host could not be resolved");
/*     */     }
/* 666 */     return this.routePlanner.determineRoute(t, request, context);
/*     */   }
/*     */   
/*     */   private RequestWrapper wrapRequest(HttpRequest request) throws ProtocolException {
/* 670 */     if (request instanceof HttpEntityEnclosingRequest) {
/* 671 */       return (RequestWrapper)new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)request);
/*     */     }
/* 673 */     return new RequestWrapper(request);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void rewriteRequestURI(RequestWrapper request, HttpRoute route) throws ProtocolException {
/*     */     try {
/* 680 */       URI uri = request.getURI();
/* 681 */       if (route.getProxyHost() != null && !route.isTunnelled()) {
/*     */         
/* 683 */         if (!uri.isAbsolute()) {
/* 684 */           HttpHost target = route.getTargetHost();
/* 685 */           uri = URIUtils.rewriteURI(uri, target);
/* 686 */           request.setURI(uri);
/*     */         }
/*     */       
/*     */       }
/* 690 */       else if (uri.isAbsolute()) {
/* 691 */         uri = URIUtils.rewriteURI(uri, null);
/* 692 */         request.setURI(uri);
/*     */       }
/*     */     
/* 695 */     } catch (URISyntaxException ex) {
/* 696 */       throw new ProtocolException("Invalid URI: " + request.getRequestLine().getUri(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private AsyncSchemeRegistry getSchemeRegistry(HttpContext context) {
/* 701 */     AsyncSchemeRegistry reg = (AsyncSchemeRegistry)context.getAttribute("http.scheme-registry");
/*     */     
/* 703 */     if (reg == null) {
/* 704 */       reg = this.connmgr.getSchemeRegistry();
/*     */     }
/* 706 */     return reg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HttpRequest createConnectRequest(HttpRoute route) {
/* 713 */     HttpHost target = route.getTargetHost();
/* 714 */     String host = target.getHostName();
/* 715 */     int port = target.getPort();
/* 716 */     if (port < 0) {
/* 717 */       AsyncSchemeRegistry registry = getSchemeRegistry(this.localContext);
/* 718 */       AsyncScheme scheme = registry.getScheme(target.getSchemeName());
/* 719 */       port = scheme.getDefaultPort();
/*     */     } 
/* 721 */     StringBuilder buffer = new StringBuilder(host.length() + 6);
/* 722 */     buffer.append(host);
/* 723 */     buffer.append(':');
/* 724 */     buffer.append(Integer.toString(port));
/* 725 */     ProtocolVersion ver = HttpProtocolParams.getVersion((HttpParams)this.params);
/* 726 */     return (HttpRequest)new BasicHttpRequest("CONNECT", buffer.toString(), ver);
/*     */   }
/*     */ 
/*     */   
/*     */   private RoutedRequest handleResponse() throws HttpException {
/* 731 */     RoutedRequest followup = null;
/* 732 */     if (HttpClientParams.isAuthenticating((HttpParams)this.params)) {
/* 733 */       CredentialsProvider credsProvider = (CredentialsProvider)this.localContext.getAttribute("http.auth.credentials-provider");
/*     */       
/* 735 */       if (credsProvider != null) {
/* 736 */         followup = handleTargetChallenge(credsProvider);
/* 737 */         if (followup != null) {
/* 738 */           return followup;
/*     */         }
/* 740 */         followup = handleProxyChallenge(credsProvider);
/* 741 */         if (followup != null) {
/* 742 */           return followup;
/*     */         }
/*     */       } 
/*     */     } 
/* 746 */     if (HttpClientParams.isRedirecting((HttpParams)this.params)) {
/* 747 */       followup = handleRedirect();
/* 748 */       if (followup != null) {
/* 749 */         return followup;
/*     */       }
/*     */     } 
/* 752 */     return null;
/*     */   }
/*     */   
/*     */   private RoutedRequest handleConnectResponse() throws HttpException {
/* 756 */     RoutedRequest followup = null;
/* 757 */     if (HttpClientParams.isAuthenticating((HttpParams)this.params)) {
/* 758 */       CredentialsProvider credsProvider = (CredentialsProvider)this.localContext.getAttribute("http.auth.credentials-provider");
/*     */       
/* 760 */       if (credsProvider != null) {
/* 761 */         followup = handleProxyChallenge(credsProvider);
/* 762 */         if (followup != null) {
/* 763 */           return followup;
/*     */         }
/*     */       } 
/*     */     } 
/* 767 */     return null;
/*     */   }
/*     */   
/*     */   private RoutedRequest handleRedirect() throws HttpException {
/* 771 */     if (this.redirectStrategy.isRedirected((HttpRequest)this.currentRequest, this.currentResponse, this.localContext)) {
/*     */ 
/*     */       
/* 774 */       HttpRoute route = this.mainRequest.getRoute();
/* 775 */       RequestWrapper request = this.mainRequest.getRequest();
/*     */       
/* 777 */       int maxRedirects = this.params.getIntParameter("http.protocol.max-redirects", 100);
/* 778 */       if (this.redirectCount >= maxRedirects) {
/* 779 */         throw new RedirectException("Maximum redirects (" + maxRedirects + ") exceeded");
/*     */       }
/*     */       
/* 782 */       this.redirectCount++;
/*     */       
/* 784 */       HttpUriRequest redirect = this.redirectStrategy.getRedirect((HttpRequest)this.currentRequest, this.currentResponse, this.localContext);
/*     */       
/* 786 */       HttpRequest orig = request.getOriginal();
/* 787 */       redirect.setHeaders(orig.getAllHeaders());
/*     */       
/* 789 */       URI uri = redirect.getURI();
/* 790 */       if (uri.getHost() == null) {
/* 791 */         throw new ProtocolException("Redirect URI does not specify a valid host name: " + uri);
/*     */       }
/* 793 */       HttpHost newTarget = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
/*     */ 
/*     */       
/* 796 */       if (!route.getTargetHost().equals(newTarget)) {
/* 797 */         if (this.log.isDebugEnabled()) {
/* 798 */           this.log.debug("[exchange: " + this.id + "] Resetting target auth state");
/*     */         }
/* 800 */         this.targetAuthState.reset();
/* 801 */         AuthScheme authScheme = this.proxyAuthState.getAuthScheme();
/* 802 */         if (authScheme != null && authScheme.isConnectionBased()) {
/* 803 */           if (this.log.isDebugEnabled()) {
/* 804 */             this.log.debug("[exchange: " + this.id + "] Resetting proxy auth state");
/*     */           }
/* 806 */           this.proxyAuthState.reset();
/*     */         } 
/*     */       } 
/*     */       
/* 810 */       RequestWrapper newRequest = wrapRequest((HttpRequest)redirect);
/* 811 */       newRequest.setParams((HttpParams)this.params);
/*     */       
/* 813 */       HttpRoute newRoute = determineRoute(newTarget, (HttpRequest)newRequest, this.localContext);
/*     */       
/* 815 */       if (this.log.isDebugEnabled()) {
/* 816 */         this.log.debug("[exchange: " + this.id + "] Redirecting to '" + uri + "' via " + newRoute);
/*     */       }
/* 818 */       return new RoutedRequest(newRequest, newRoute);
/*     */     } 
/* 820 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private RoutedRequest handleTargetChallenge(CredentialsProvider credsProvider) throws HttpException {
/* 825 */     HttpRoute route = this.mainRequest.getRoute();
/* 826 */     HttpHost target = (HttpHost)this.localContext.getAttribute("http.target_host");
/*     */     
/* 828 */     if (target == null) {
/* 829 */       target = route.getTargetHost();
/*     */     }
/* 831 */     if (this.authenticator.isAuthenticationRequested(target, this.currentResponse, this.targetAuthStrategy, this.targetAuthState, this.localContext)) {
/*     */       
/* 833 */       if (this.authenticator.authenticate(target, this.currentResponse, this.targetAuthStrategy, this.targetAuthState, this.localContext))
/*     */       {
/*     */         
/* 836 */         return this.mainRequest;
/*     */       }
/* 838 */       return null;
/*     */     } 
/*     */     
/* 841 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private RoutedRequest handleProxyChallenge(CredentialsProvider credsProvider) throws HttpException {
/* 846 */     HttpRoute route = this.mainRequest.getRoute();
/* 847 */     HttpHost proxy = route.getProxyHost();
/* 848 */     if (this.authenticator.isAuthenticationRequested(proxy, this.currentResponse, this.proxyAuthStrategy, this.proxyAuthState, this.localContext)) {
/*     */       
/* 850 */       if (this.authenticator.authenticate(proxy, this.currentResponse, this.proxyAuthStrategy, this.proxyAuthState, this.localContext))
/*     */       {
/*     */         
/* 853 */         return this.mainRequest;
/*     */       }
/* 855 */       return null;
/*     */     } 
/*     */     
/* 858 */     return null;
/*     */   }
/*     */   
/*     */   public HttpContext getContext() {
/* 862 */     return this.localContext;
/*     */   }
/*     */   
/*     */   public HttpProcessor getHttpProcessor() {
/* 866 */     return this.httppocessor;
/*     */   }
/*     */   
/*     */   public ConnectionReuseStrategy getConnectionReuseStrategy() {
/* 870 */     return this.reuseStrategy;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/DefaultAsyncRequestDirector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
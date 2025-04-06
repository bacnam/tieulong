/*      */ package org.apache.http.impl.client;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InterruptedIOException;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.apache.http.ConnectionReuseStrategy;
/*      */ import org.apache.http.HttpClientConnection;
/*      */ import org.apache.http.HttpEntity;
/*      */ import org.apache.http.HttpEntityEnclosingRequest;
/*      */ import org.apache.http.HttpException;
/*      */ import org.apache.http.HttpHost;
/*      */ import org.apache.http.HttpRequest;
/*      */ import org.apache.http.HttpResponse;
/*      */ import org.apache.http.NoHttpResponseException;
/*      */ import org.apache.http.ProtocolException;
/*      */ import org.apache.http.ProtocolVersion;
/*      */ import org.apache.http.annotation.NotThreadSafe;
/*      */ import org.apache.http.auth.AuthProtocolState;
/*      */ import org.apache.http.auth.AuthScheme;
/*      */ import org.apache.http.auth.AuthState;
/*      */ import org.apache.http.auth.Credentials;
/*      */ import org.apache.http.auth.UsernamePasswordCredentials;
/*      */ import org.apache.http.client.AuthenticationHandler;
/*      */ import org.apache.http.client.AuthenticationStrategy;
/*      */ import org.apache.http.client.HttpRequestRetryHandler;
/*      */ import org.apache.http.client.NonRepeatableRequestException;
/*      */ import org.apache.http.client.RedirectException;
/*      */ import org.apache.http.client.RedirectHandler;
/*      */ import org.apache.http.client.RedirectStrategy;
/*      */ import org.apache.http.client.RequestDirector;
/*      */ import org.apache.http.client.UserTokenHandler;
/*      */ import org.apache.http.client.methods.AbortableHttpRequest;
/*      */ import org.apache.http.client.methods.HttpUriRequest;
/*      */ import org.apache.http.client.params.HttpClientParams;
/*      */ import org.apache.http.client.utils.URIUtils;
/*      */ import org.apache.http.conn.BasicManagedEntity;
/*      */ import org.apache.http.conn.ClientConnectionManager;
/*      */ import org.apache.http.conn.ClientConnectionRequest;
/*      */ import org.apache.http.conn.ConnectionKeepAliveStrategy;
/*      */ import org.apache.http.conn.ConnectionReleaseTrigger;
/*      */ import org.apache.http.conn.ManagedClientConnection;
/*      */ import org.apache.http.conn.routing.BasicRouteDirector;
/*      */ import org.apache.http.conn.routing.HttpRoute;
/*      */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*      */ import org.apache.http.conn.routing.RouteInfo;
/*      */ import org.apache.http.conn.scheme.Scheme;
/*      */ import org.apache.http.entity.BufferedHttpEntity;
/*      */ import org.apache.http.impl.auth.BasicScheme;
/*      */ import org.apache.http.impl.conn.ConnectionShutdownException;
/*      */ import org.apache.http.message.BasicHttpRequest;
/*      */ import org.apache.http.params.HttpConnectionParams;
/*      */ import org.apache.http.params.HttpParams;
/*      */ import org.apache.http.params.HttpProtocolParams;
/*      */ import org.apache.http.protocol.HttpContext;
/*      */ import org.apache.http.protocol.HttpProcessor;
/*      */ import org.apache.http.protocol.HttpRequestExecutor;
/*      */ import org.apache.http.util.Args;
/*      */ import org.apache.http.util.EntityUtils;
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
/*      */ @Deprecated
/*      */ @NotThreadSafe
/*      */ public class DefaultRequestDirector
/*      */   implements RequestDirector
/*      */ {
/*      */   private final Log log;
/*      */   protected final ClientConnectionManager connManager;
/*      */   protected final HttpRoutePlanner routePlanner;
/*      */   protected final ConnectionReuseStrategy reuseStrategy;
/*      */   protected final ConnectionKeepAliveStrategy keepAliveStrategy;
/*      */   protected final HttpRequestExecutor requestExec;
/*      */   protected final HttpProcessor httpProcessor;
/*      */   protected final HttpRequestRetryHandler retryHandler;
/*      */   @Deprecated
/*      */   protected final RedirectHandler redirectHandler;
/*      */   protected final RedirectStrategy redirectStrategy;
/*      */   @Deprecated
/*      */   protected final AuthenticationHandler targetAuthHandler;
/*      */   protected final AuthenticationStrategy targetAuthStrategy;
/*      */   @Deprecated
/*      */   protected final AuthenticationHandler proxyAuthHandler;
/*      */   protected final AuthenticationStrategy proxyAuthStrategy;
/*      */   protected final UserTokenHandler userTokenHandler;
/*      */   protected final HttpParams params;
/*      */   protected ManagedClientConnection managedConn;
/*      */   protected final AuthState targetAuthState;
/*      */   protected final AuthState proxyAuthState;
/*      */   private final HttpAuthenticator authenticator;
/*      */   private int execCount;
/*      */   private int redirectCount;
/*      */   private final int maxRedirects;
/*      */   private HttpHost virtualHost;
/*      */   
/*      */   @Deprecated
/*      */   public DefaultRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectHandler redirectHandler, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params) {
/*  219 */     this(LogFactory.getLog(DefaultRequestDirector.class), requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, new DefaultRedirectStrategyAdaptor(redirectHandler), new AuthenticationStrategyAdaptor(targetAuthHandler), new AuthenticationStrategyAdaptor(proxyAuthHandler), userTokenHandler, params);
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
/*      */   public DefaultRequestDirector(Log log, HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params) {
/*  244 */     this(LogFactory.getLog(DefaultRequestDirector.class), requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, redirectStrategy, new AuthenticationStrategyAdaptor(targetAuthHandler), new AuthenticationStrategyAdaptor(proxyAuthHandler), userTokenHandler, params);
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
/*      */   public DefaultRequestDirector(Log log, HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler, HttpParams params) {
/*  271 */     Args.notNull(log, "Log");
/*  272 */     Args.notNull(requestExec, "Request executor");
/*  273 */     Args.notNull(conman, "Client connection manager");
/*  274 */     Args.notNull(reustrat, "Connection reuse strategy");
/*  275 */     Args.notNull(kastrat, "Connection keep alive strategy");
/*  276 */     Args.notNull(rouplan, "Route planner");
/*  277 */     Args.notNull(httpProcessor, "HTTP protocol processor");
/*  278 */     Args.notNull(retryHandler, "HTTP request retry handler");
/*  279 */     Args.notNull(redirectStrategy, "Redirect strategy");
/*  280 */     Args.notNull(targetAuthStrategy, "Target authentication strategy");
/*  281 */     Args.notNull(proxyAuthStrategy, "Proxy authentication strategy");
/*  282 */     Args.notNull(userTokenHandler, "User token handler");
/*  283 */     Args.notNull(params, "HTTP parameters");
/*  284 */     this.log = log;
/*  285 */     this.authenticator = new HttpAuthenticator(log);
/*  286 */     this.requestExec = requestExec;
/*  287 */     this.connManager = conman;
/*  288 */     this.reuseStrategy = reustrat;
/*  289 */     this.keepAliveStrategy = kastrat;
/*  290 */     this.routePlanner = rouplan;
/*  291 */     this.httpProcessor = httpProcessor;
/*  292 */     this.retryHandler = retryHandler;
/*  293 */     this.redirectStrategy = redirectStrategy;
/*  294 */     this.targetAuthStrategy = targetAuthStrategy;
/*  295 */     this.proxyAuthStrategy = proxyAuthStrategy;
/*  296 */     this.userTokenHandler = userTokenHandler;
/*  297 */     this.params = params;
/*      */     
/*  299 */     if (redirectStrategy instanceof DefaultRedirectStrategyAdaptor) {
/*  300 */       this.redirectHandler = ((DefaultRedirectStrategyAdaptor)redirectStrategy).getHandler();
/*      */     } else {
/*  302 */       this.redirectHandler = null;
/*      */     } 
/*  304 */     if (targetAuthStrategy instanceof AuthenticationStrategyAdaptor) {
/*  305 */       this.targetAuthHandler = ((AuthenticationStrategyAdaptor)targetAuthStrategy).getHandler();
/*      */     } else {
/*  307 */       this.targetAuthHandler = null;
/*      */     } 
/*  309 */     if (proxyAuthStrategy instanceof AuthenticationStrategyAdaptor) {
/*  310 */       this.proxyAuthHandler = ((AuthenticationStrategyAdaptor)proxyAuthStrategy).getHandler();
/*      */     } else {
/*  312 */       this.proxyAuthHandler = null;
/*      */     } 
/*      */     
/*  315 */     this.managedConn = null;
/*      */     
/*  317 */     this.execCount = 0;
/*  318 */     this.redirectCount = 0;
/*  319 */     this.targetAuthState = new AuthState();
/*  320 */     this.proxyAuthState = new AuthState();
/*  321 */     this.maxRedirects = this.params.getIntParameter("http.protocol.max-redirects", 100);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private RequestWrapper wrapRequest(HttpRequest request) throws ProtocolException {
/*  327 */     if (request instanceof HttpEntityEnclosingRequest) {
/*  328 */       return new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)request);
/*      */     }
/*      */     
/*  331 */     return new RequestWrapper(request);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void rewriteRequestURI(RequestWrapper request, HttpRoute route) throws ProtocolException {
/*      */     try {
/*  342 */       URI uri = request.getURI();
/*  343 */       if (route.getProxyHost() != null && !route.isTunnelled()) {
/*      */         
/*  345 */         if (!uri.isAbsolute()) {
/*  346 */           HttpHost target = route.getTargetHost();
/*  347 */           uri = URIUtils.rewriteURI(uri, target, true);
/*      */         } else {
/*  349 */           uri = URIUtils.rewriteURI(uri);
/*      */         }
/*      */       
/*      */       }
/*  353 */       else if (uri.isAbsolute()) {
/*  354 */         uri = URIUtils.rewriteURI(uri, null, true);
/*      */       } else {
/*  356 */         uri = URIUtils.rewriteURI(uri);
/*      */       } 
/*      */       
/*  359 */       request.setURI(uri);
/*      */     }
/*  361 */     catch (URISyntaxException ex) {
/*  362 */       throw new ProtocolException("Invalid URI: " + request.getRequestLine().getUri(), ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpResponse execute(HttpHost targetHost, HttpRequest request, HttpContext context) throws HttpException, IOException {
/*  373 */     context.setAttribute("http.auth.target-scope", this.targetAuthState);
/*  374 */     context.setAttribute("http.auth.proxy-scope", this.proxyAuthState);
/*      */     
/*  376 */     HttpHost target = targetHost;
/*      */     
/*  378 */     HttpRequest orig = request;
/*  379 */     RequestWrapper origWrapper = wrapRequest(orig);
/*  380 */     origWrapper.setParams(this.params);
/*  381 */     HttpRoute origRoute = determineRoute(target, (HttpRequest)origWrapper, context);
/*      */     
/*  383 */     this.virtualHost = (HttpHost)origWrapper.getParams().getParameter("http.virtual-host");
/*      */ 
/*      */     
/*  386 */     if (this.virtualHost != null && this.virtualHost.getPort() == -1) {
/*  387 */       HttpHost host = (target != null) ? target : origRoute.getTargetHost();
/*  388 */       int port = host.getPort();
/*  389 */       if (port != -1) {
/*  390 */         this.virtualHost = new HttpHost(this.virtualHost.getHostName(), port, this.virtualHost.getSchemeName());
/*      */       }
/*      */     } 
/*      */     
/*  394 */     RoutedRequest roureq = new RoutedRequest(origWrapper, origRoute);
/*      */     
/*  396 */     boolean reuse = false;
/*  397 */     boolean done = false;
/*      */     try {
/*  399 */       HttpResponse response = null;
/*  400 */       while (!done) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  406 */         RequestWrapper wrapper = roureq.getRequest();
/*  407 */         HttpRoute route = roureq.getRoute();
/*  408 */         response = null;
/*      */ 
/*      */         
/*  411 */         Object userToken = context.getAttribute("http.user-token");
/*      */ 
/*      */         
/*  414 */         if (this.managedConn == null) {
/*  415 */           ClientConnectionRequest connRequest = this.connManager.requestConnection(route, userToken);
/*      */           
/*  417 */           if (orig instanceof AbortableHttpRequest) {
/*  418 */             ((AbortableHttpRequest)orig).setConnectionRequest(connRequest);
/*      */           }
/*      */           
/*  421 */           long timeout = HttpClientParams.getConnectionManagerTimeout(this.params);
/*      */           try {
/*  423 */             this.managedConn = connRequest.getConnection(timeout, TimeUnit.MILLISECONDS);
/*  424 */           } catch (InterruptedException interrupted) {
/*  425 */             Thread.currentThread().interrupt();
/*  426 */             throw new InterruptedIOException();
/*      */           } 
/*      */           
/*  429 */           if (HttpConnectionParams.isStaleCheckingEnabled(this.params))
/*      */           {
/*  431 */             if (this.managedConn.isOpen()) {
/*  432 */               this.log.debug("Stale connection check");
/*  433 */               if (this.managedConn.isStale()) {
/*  434 */                 this.log.debug("Stale connection detected");
/*  435 */                 this.managedConn.close();
/*      */               } 
/*      */             } 
/*      */           }
/*      */         } 
/*      */         
/*  441 */         if (orig instanceof AbortableHttpRequest) {
/*  442 */           ((AbortableHttpRequest)orig).setReleaseTrigger((ConnectionReleaseTrigger)this.managedConn);
/*      */         }
/*      */         
/*      */         try {
/*  446 */           tryConnect(roureq, context);
/*  447 */         } catch (TunnelRefusedException ex) {
/*  448 */           if (this.log.isDebugEnabled()) {
/*  449 */             this.log.debug(ex.getMessage());
/*      */           }
/*  451 */           response = ex.getResponse();
/*      */           
/*      */           break;
/*      */         } 
/*  455 */         String userinfo = wrapper.getURI().getUserInfo();
/*  456 */         if (userinfo != null) {
/*  457 */           this.targetAuthState.update((AuthScheme)new BasicScheme(), (Credentials)new UsernamePasswordCredentials(userinfo));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  462 */         if (this.virtualHost != null) {
/*  463 */           target = this.virtualHost;
/*      */         } else {
/*  465 */           URI requestURI = wrapper.getURI();
/*  466 */           if (requestURI.isAbsolute()) {
/*  467 */             target = URIUtils.extractHost(requestURI);
/*      */           }
/*      */         } 
/*  470 */         if (target == null) {
/*  471 */           target = route.getTargetHost();
/*      */         }
/*      */ 
/*      */         
/*  475 */         wrapper.resetHeaders();
/*      */         
/*  477 */         rewriteRequestURI(wrapper, route);
/*      */ 
/*      */         
/*  480 */         context.setAttribute("http.target_host", target);
/*  481 */         context.setAttribute("http.route", route);
/*  482 */         context.setAttribute("http.connection", this.managedConn);
/*      */ 
/*      */         
/*  485 */         this.requestExec.preProcess((HttpRequest)wrapper, this.httpProcessor, context);
/*      */         
/*  487 */         response = tryExecute(roureq, context);
/*  488 */         if (response == null) {
/*      */           continue;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  494 */         response.setParams(this.params);
/*  495 */         this.requestExec.postProcess(response, this.httpProcessor, context);
/*      */ 
/*      */ 
/*      */         
/*  499 */         reuse = this.reuseStrategy.keepAlive(response, context);
/*  500 */         if (reuse) {
/*      */           
/*  502 */           long duration = this.keepAliveStrategy.getKeepAliveDuration(response, context);
/*  503 */           if (this.log.isDebugEnabled()) {
/*      */             String s;
/*  505 */             if (duration > 0L) {
/*  506 */               s = "for " + duration + " " + TimeUnit.MILLISECONDS;
/*      */             } else {
/*  508 */               s = "indefinitely";
/*      */             } 
/*  510 */             this.log.debug("Connection can be kept alive " + s);
/*      */           } 
/*  512 */           this.managedConn.setIdleDuration(duration, TimeUnit.MILLISECONDS);
/*      */         } 
/*      */         
/*  515 */         RoutedRequest followup = handleResponse(roureq, response, context);
/*  516 */         if (followup == null) {
/*  517 */           done = true;
/*      */         } else {
/*  519 */           if (reuse) {
/*      */             
/*  521 */             HttpEntity entity = response.getEntity();
/*  522 */             EntityUtils.consume(entity);
/*      */ 
/*      */             
/*  525 */             this.managedConn.markReusable();
/*      */           } else {
/*  527 */             this.managedConn.close();
/*  528 */             if (this.proxyAuthState.getState().compareTo((Enum)AuthProtocolState.CHALLENGED) > 0 && this.proxyAuthState.getAuthScheme() != null && this.proxyAuthState.getAuthScheme().isConnectionBased()) {
/*      */ 
/*      */               
/*  531 */               this.log.debug("Resetting proxy auth state");
/*  532 */               this.proxyAuthState.reset();
/*      */             } 
/*  534 */             if (this.targetAuthState.getState().compareTo((Enum)AuthProtocolState.CHALLENGED) > 0 && this.targetAuthState.getAuthScheme() != null && this.targetAuthState.getAuthScheme().isConnectionBased()) {
/*      */ 
/*      */               
/*  537 */               this.log.debug("Resetting target auth state");
/*  538 */               this.targetAuthState.reset();
/*      */             } 
/*      */           } 
/*      */           
/*  542 */           if (!followup.getRoute().equals(roureq.getRoute())) {
/*  543 */             releaseConnection();
/*      */           }
/*  545 */           roureq = followup;
/*      */         } 
/*      */         
/*  548 */         if (this.managedConn != null) {
/*  549 */           if (userToken == null) {
/*  550 */             userToken = this.userTokenHandler.getUserToken(context);
/*  551 */             context.setAttribute("http.user-token", userToken);
/*      */           } 
/*  553 */           if (userToken != null) {
/*  554 */             this.managedConn.setState(userToken);
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  562 */       if (response == null || response.getEntity() == null || !response.getEntity().isStreaming()) {
/*      */ 
/*      */         
/*  565 */         if (reuse) {
/*  566 */           this.managedConn.markReusable();
/*      */         }
/*  568 */         releaseConnection();
/*      */       } else {
/*      */         
/*  571 */         HttpEntity entity = response.getEntity();
/*  572 */         BasicManagedEntity basicManagedEntity = new BasicManagedEntity(entity, this.managedConn, reuse);
/*  573 */         response.setEntity((HttpEntity)basicManagedEntity);
/*      */       } 
/*      */       
/*  576 */       return response;
/*      */     }
/*  578 */     catch (ConnectionShutdownException ex) {
/*  579 */       InterruptedIOException ioex = new InterruptedIOException("Connection has been shut down");
/*      */       
/*  581 */       ioex.initCause((Throwable)ex);
/*  582 */       throw ioex;
/*  583 */     } catch (HttpException ex) {
/*  584 */       abortConnection();
/*  585 */       throw ex;
/*  586 */     } catch (IOException ex) {
/*  587 */       abortConnection();
/*  588 */       throw ex;
/*  589 */     } catch (RuntimeException ex) {
/*  590 */       abortConnection();
/*  591 */       throw ex;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void tryConnect(RoutedRequest req, HttpContext context) throws HttpException, IOException {
/*  601 */     HttpRoute route = req.getRoute();
/*  602 */     RequestWrapper requestWrapper = req.getRequest();
/*      */     
/*  604 */     int connectCount = 0;
/*      */     while (true) {
/*  606 */       context.setAttribute("http.request", requestWrapper);
/*      */       
/*  608 */       connectCount++;
/*      */       try {
/*  610 */         if (!this.managedConn.isOpen()) {
/*  611 */           this.managedConn.open(route, context, this.params);
/*      */         } else {
/*  613 */           this.managedConn.setSocketTimeout(HttpConnectionParams.getSoTimeout(this.params));
/*      */         } 
/*  615 */         establishRoute(route, context);
/*      */       }
/*  617 */       catch (IOException ex) {
/*      */         try {
/*  619 */           this.managedConn.close();
/*  620 */         } catch (IOException ignore) {}
/*      */         
/*  622 */         if (this.retryHandler.retryRequest(ex, connectCount, context)) {
/*  623 */           if (this.log.isInfoEnabled()) {
/*  624 */             this.log.info("I/O exception (" + ex.getClass().getName() + ") caught when connecting to " + route + ": " + ex.getMessage());
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  629 */             if (this.log.isDebugEnabled()) {
/*  630 */               this.log.debug(ex.getMessage(), ex);
/*      */             }
/*  632 */             this.log.info("Retrying connect to " + route);
/*      */           }  continue;
/*      */         } 
/*  635 */         throw ex;
/*      */       } 
/*      */       break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private HttpResponse tryExecute(RoutedRequest req, HttpContext context) throws HttpException, IOException {
/*  646 */     RequestWrapper wrapper = req.getRequest();
/*  647 */     HttpRoute route = req.getRoute();
/*  648 */     HttpResponse response = null;
/*      */     
/*  650 */     Exception retryReason = null;
/*      */     
/*      */     while (true) {
/*  653 */       this.execCount++;
/*      */       
/*  655 */       wrapper.incrementExecCount();
/*  656 */       if (!wrapper.isRepeatable()) {
/*  657 */         this.log.debug("Cannot retry non-repeatable request");
/*  658 */         if (retryReason != null) {
/*  659 */           throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.  The cause lists the reason the original request failed.", retryReason);
/*      */         }
/*      */ 
/*      */         
/*  663 */         throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.");
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  669 */         if (!this.managedConn.isOpen())
/*      */         {
/*      */           
/*  672 */           if (!route.isTunnelled()) {
/*  673 */             this.log.debug("Reopening the direct connection.");
/*  674 */             this.managedConn.open(route, context, this.params);
/*      */           } else {
/*      */             
/*  677 */             this.log.debug("Proxied connection. Need to start over.");
/*      */             
/*      */             break;
/*      */           } 
/*      */         }
/*  682 */         if (this.log.isDebugEnabled()) {
/*  683 */           this.log.debug("Attempt " + this.execCount + " to execute request");
/*      */         }
/*  685 */         response = this.requestExec.execute((HttpRequest)wrapper, (HttpClientConnection)this.managedConn, context);
/*      */       
/*      */       }
/*  688 */       catch (IOException ex) {
/*  689 */         this.log.debug("Closing the connection.");
/*      */         try {
/*  691 */           this.managedConn.close();
/*  692 */         } catch (IOException ignore) {}
/*      */         
/*  694 */         if (this.retryHandler.retryRequest(ex, wrapper.getExecCount(), context)) {
/*  695 */           if (this.log.isInfoEnabled()) {
/*  696 */             this.log.info("I/O exception (" + ex.getClass().getName() + ") caught when processing request to " + route + ": " + ex.getMessage());
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  702 */           if (this.log.isDebugEnabled()) {
/*  703 */             this.log.debug(ex.getMessage(), ex);
/*      */           }
/*  705 */           if (this.log.isInfoEnabled()) {
/*  706 */             this.log.info("Retrying request to " + route);
/*      */           }
/*  708 */           retryReason = ex; continue;
/*      */         } 
/*  710 */         if (ex instanceof NoHttpResponseException) {
/*  711 */           NoHttpResponseException updatedex = new NoHttpResponseException(route.getTargetHost().toHostString() + " failed to respond");
/*      */           
/*  713 */           updatedex.setStackTrace(ex.getStackTrace());
/*  714 */           throw updatedex;
/*      */         } 
/*  716 */         throw ex;
/*      */       } 
/*      */       
/*      */       break;
/*      */     } 
/*  721 */     return response;
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
/*      */   protected void releaseConnection() {
/*      */     try {
/*  734 */       this.managedConn.releaseConnection();
/*  735 */     } catch (IOException ignored) {
/*  736 */       this.log.debug("IOException releasing connection", ignored);
/*      */     } 
/*  738 */     this.managedConn = null;
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
/*      */   protected HttpRoute determineRoute(HttpHost targetHost, HttpRequest request, HttpContext context) throws HttpException {
/*  762 */     return this.routePlanner.determineRoute((targetHost != null) ? targetHost : (HttpHost)request.getParams().getParameter("http.default-host"), request, context);
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
/*      */   protected void establishRoute(HttpRoute route, HttpContext context) throws HttpException, IOException {
/*      */     int step;
/*  781 */     BasicRouteDirector basicRouteDirector = new BasicRouteDirector(); do {
/*      */       boolean secure; int hop;
/*      */       boolean bool1;
/*  784 */       HttpRoute fact = this.managedConn.getRoute();
/*  785 */       step = basicRouteDirector.nextStep((RouteInfo)route, (RouteInfo)fact);
/*      */       
/*  787 */       switch (step) {
/*      */         
/*      */         case 1:
/*      */         case 2:
/*  791 */           this.managedConn.open(route, context, this.params);
/*      */           break;
/*      */         
/*      */         case 3:
/*  795 */           secure = createTunnelToTarget(route, context);
/*  796 */           this.log.debug("Tunnel to target created.");
/*  797 */           this.managedConn.tunnelTarget(secure, this.params);
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 4:
/*  805 */           hop = fact.getHopCount() - 1;
/*  806 */           bool1 = createTunnelToProxy(route, hop, context);
/*  807 */           this.log.debug("Tunnel to proxy created.");
/*  808 */           this.managedConn.tunnelProxy(route.getHopTarget(hop), bool1, this.params);
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 5:
/*  814 */           this.managedConn.layerProtocol(context, this.params);
/*      */           break;
/*      */         
/*      */         case -1:
/*  818 */           throw new HttpException("Unable to establish route: planned = " + route + "; current = " + fact);
/*      */         
/*      */         case 0:
/*      */           break;
/*      */         
/*      */         default:
/*  824 */           throw new IllegalStateException("Unknown step indicator " + step + " from RouteDirector.");
/*      */       } 
/*      */ 
/*      */     
/*  828 */     } while (step > 0);
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
/*      */   protected boolean createTunnelToTarget(HttpRoute route, HttpContext context) throws HttpException, IOException {
/*  856 */     HttpHost proxy = route.getProxyHost();
/*  857 */     HttpHost target = route.getTargetHost();
/*  858 */     HttpResponse response = null;
/*      */     
/*      */     while (true) {
/*  861 */       if (!this.managedConn.isOpen()) {
/*  862 */         this.managedConn.open(route, context, this.params);
/*      */       }
/*      */       
/*  865 */       HttpRequest connect = createConnectRequest(route, context);
/*  866 */       connect.setParams(this.params);
/*      */ 
/*      */       
/*  869 */       context.setAttribute("http.target_host", target);
/*  870 */       context.setAttribute("http.route", route);
/*  871 */       context.setAttribute("http.proxy_host", proxy);
/*  872 */       context.setAttribute("http.connection", this.managedConn);
/*  873 */       context.setAttribute("http.request", connect);
/*      */       
/*  875 */       this.requestExec.preProcess(connect, this.httpProcessor, context);
/*      */       
/*  877 */       response = this.requestExec.execute(connect, (HttpClientConnection)this.managedConn, context);
/*      */       
/*  879 */       response.setParams(this.params);
/*  880 */       this.requestExec.postProcess(response, this.httpProcessor, context);
/*      */       
/*  882 */       int i = response.getStatusLine().getStatusCode();
/*  883 */       if (i < 200) {
/*  884 */         throw new HttpException("Unexpected response to CONNECT request: " + response.getStatusLine());
/*      */       }
/*      */ 
/*      */       
/*  888 */       if (HttpClientParams.isAuthenticating(this.params)) {
/*  889 */         if (this.authenticator.isAuthenticationRequested(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, context))
/*      */         {
/*  891 */           if (this.authenticator.authenticate(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, context)) {
/*      */ 
/*      */             
/*  894 */             if (this.reuseStrategy.keepAlive(response, context)) {
/*  895 */               this.log.debug("Connection kept alive");
/*      */               
/*  897 */               HttpEntity entity = response.getEntity();
/*  898 */               EntityUtils.consume(entity); continue;
/*      */             } 
/*  900 */             this.managedConn.close();
/*      */ 
/*      */             
/*      */             continue;
/*      */           } 
/*      */         }
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/*  911 */     int status = response.getStatusLine().getStatusCode();
/*      */     
/*  913 */     if (status > 299) {
/*      */ 
/*      */       
/*  916 */       HttpEntity entity = response.getEntity();
/*  917 */       if (entity != null) {
/*  918 */         response.setEntity((HttpEntity)new BufferedHttpEntity(entity));
/*      */       }
/*      */       
/*  921 */       this.managedConn.close();
/*  922 */       throw new TunnelRefusedException("CONNECT refused by proxy: " + response.getStatusLine(), response);
/*      */     } 
/*      */ 
/*      */     
/*  926 */     this.managedConn.markReusable();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  932 */     return false;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean createTunnelToProxy(HttpRoute route, int hop, HttpContext context) throws HttpException, IOException {
/*  968 */     throw new HttpException("Proxy chains are not supported.");
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
/*      */   protected HttpRequest createConnectRequest(HttpRoute route, HttpContext context) {
/*  988 */     HttpHost target = route.getTargetHost();
/*      */     
/*  990 */     String host = target.getHostName();
/*  991 */     int port = target.getPort();
/*  992 */     if (port < 0) {
/*  993 */       Scheme scheme = this.connManager.getSchemeRegistry().getScheme(target.getSchemeName());
/*      */       
/*  995 */       port = scheme.getDefaultPort();
/*      */     } 
/*      */     
/*  998 */     StringBuilder buffer = new StringBuilder(host.length() + 6);
/*  999 */     buffer.append(host);
/* 1000 */     buffer.append(':');
/* 1001 */     buffer.append(Integer.toString(port));
/*      */     
/* 1003 */     String authority = buffer.toString();
/* 1004 */     ProtocolVersion ver = HttpProtocolParams.getVersion(this.params);
/* 1005 */     return (HttpRequest)new BasicHttpRequest("CONNECT", authority, ver);
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
/*      */   protected RoutedRequest handleResponse(RoutedRequest roureq, HttpResponse response, HttpContext context) throws HttpException, IOException {
/* 1030 */     HttpRoute route = roureq.getRoute();
/* 1031 */     RequestWrapper request = roureq.getRequest();
/*      */     
/* 1033 */     HttpParams params = request.getParams();
/*      */     
/* 1035 */     if (HttpClientParams.isAuthenticating(params)) {
/* 1036 */       HttpHost target = (HttpHost)context.getAttribute("http.target_host");
/* 1037 */       if (target == null) {
/* 1038 */         target = route.getTargetHost();
/*      */       }
/* 1040 */       if (target.getPort() < 0) {
/* 1041 */         Scheme scheme = this.connManager.getSchemeRegistry().getScheme(target);
/* 1042 */         target = new HttpHost(target.getHostName(), scheme.getDefaultPort(), target.getSchemeName());
/*      */       } 
/*      */       
/* 1045 */       boolean targetAuthRequested = this.authenticator.isAuthenticationRequested(target, response, this.targetAuthStrategy, this.targetAuthState, context);
/*      */ 
/*      */       
/* 1048 */       HttpHost proxy = route.getProxyHost();
/*      */       
/* 1050 */       if (proxy == null) {
/* 1051 */         proxy = route.getTargetHost();
/*      */       }
/* 1053 */       boolean proxyAuthRequested = this.authenticator.isAuthenticationRequested(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, context);
/*      */ 
/*      */       
/* 1056 */       if (targetAuthRequested && 
/* 1057 */         this.authenticator.authenticate(target, response, this.targetAuthStrategy, this.targetAuthState, context))
/*      */       {
/*      */         
/* 1060 */         return roureq;
/*      */       }
/*      */       
/* 1063 */       if (proxyAuthRequested && 
/* 1064 */         this.authenticator.authenticate(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, context))
/*      */       {
/*      */         
/* 1067 */         return roureq;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1072 */     if (HttpClientParams.isRedirecting(params) && this.redirectStrategy.isRedirected((HttpRequest)request, response, context)) {
/*      */ 
/*      */       
/* 1075 */       if (this.redirectCount >= this.maxRedirects) {
/* 1076 */         throw new RedirectException("Maximum redirects (" + this.maxRedirects + ") exceeded");
/*      */       }
/*      */       
/* 1079 */       this.redirectCount++;
/*      */ 
/*      */       
/* 1082 */       this.virtualHost = null;
/*      */       
/* 1084 */       HttpUriRequest redirect = this.redirectStrategy.getRedirect((HttpRequest)request, response, context);
/* 1085 */       HttpRequest orig = request.getOriginal();
/* 1086 */       redirect.setHeaders(orig.getAllHeaders());
/*      */       
/* 1088 */       URI uri = redirect.getURI();
/* 1089 */       HttpHost newTarget = URIUtils.extractHost(uri);
/* 1090 */       if (newTarget == null) {
/* 1091 */         throw new ProtocolException("Redirect URI does not specify a valid host name: " + uri);
/*      */       }
/*      */ 
/*      */       
/* 1095 */       if (!route.getTargetHost().equals(newTarget)) {
/* 1096 */         this.log.debug("Resetting target auth state");
/* 1097 */         this.targetAuthState.reset();
/* 1098 */         AuthScheme authScheme = this.proxyAuthState.getAuthScheme();
/* 1099 */         if (authScheme != null && authScheme.isConnectionBased()) {
/* 1100 */           this.log.debug("Resetting proxy auth state");
/* 1101 */           this.proxyAuthState.reset();
/*      */         } 
/*      */       } 
/*      */       
/* 1105 */       RequestWrapper wrapper = wrapRequest((HttpRequest)redirect);
/* 1106 */       wrapper.setParams(params);
/*      */       
/* 1108 */       HttpRoute newRoute = determineRoute(newTarget, (HttpRequest)wrapper, context);
/* 1109 */       RoutedRequest newRequest = new RoutedRequest(wrapper, newRoute);
/*      */       
/* 1111 */       if (this.log.isDebugEnabled()) {
/* 1112 */         this.log.debug("Redirecting to '" + uri + "' via " + newRoute);
/*      */       }
/*      */       
/* 1115 */       return newRequest;
/*      */     } 
/*      */     
/* 1118 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void abortConnection() {
/* 1128 */     ManagedClientConnection mcc = this.managedConn;
/* 1129 */     if (mcc != null) {
/*      */ 
/*      */       
/* 1132 */       this.managedConn = null;
/*      */       try {
/* 1134 */         mcc.abortConnection();
/* 1135 */       } catch (IOException ex) {
/* 1136 */         if (this.log.isDebugEnabled()) {
/* 1137 */           this.log.debug(ex.getMessage(), ex);
/*      */         }
/*      */       } 
/*      */       
/*      */       try {
/* 1142 */         mcc.releaseConnection();
/* 1143 */       } catch (IOException ignored) {
/* 1144 */         this.log.debug("Error releasing connection", ignored);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/client/DefaultRequestDirector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package org.apache.http.impl.nio.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.auth.AuthProtocolState;
/*     */ import org.apache.http.auth.AuthScheme;
/*     */ import org.apache.http.auth.AuthScope;
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
/*     */ import org.apache.http.client.methods.Configurable;
/*     */ import org.apache.http.client.methods.HttpRequestWrapper;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.client.protocol.RequestClientConnControl;
/*     */ import org.apache.http.client.utils.URIUtils;
/*     */ import org.apache.http.conn.ConnectionKeepAliveStrategy;
/*     */ import org.apache.http.conn.routing.BasicRouteDirector;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.routing.HttpRouteDirector;
/*     */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*     */ import org.apache.http.conn.routing.RouteInfo;
/*     */ import org.apache.http.conn.routing.RouteTracker;
/*     */ import org.apache.http.impl.auth.HttpAuthenticator;
/*     */ import org.apache.http.message.BasicHttpRequest;
/*     */ import org.apache.http.nio.ContentDecoder;
/*     */ import org.apache.http.nio.ContentEncoder;
/*     */ import org.apache.http.nio.IOControl;
/*     */ import org.apache.http.nio.NHttpClientConnection;
/*     */ import org.apache.http.nio.conn.NHttpClientConnectionManager;
/*     */ import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
/*     */ import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.protocol.HttpProcessor;
/*     */ import org.apache.http.protocol.ImmutableHttpProcessor;
/*     */ import org.apache.http.protocol.RequestTargetHost;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MainClientExec
/*     */   implements InternalClientExec
/*     */ {
/*  87 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final NHttpClientConnectionManager connmgr;
/*     */   
/*     */   private final HttpProcessor httpProcessor;
/*     */   
/*     */   private final HttpProcessor proxyHttpProcessor;
/*     */   
/*     */   private final HttpRoutePlanner routePlanner;
/*     */   
/*     */   private final ConnectionReuseStrategy connReuseStrategy;
/*     */   
/*     */   private final ConnectionKeepAliveStrategy keepaliveStrategy;
/*     */   
/*     */   private final AuthenticationStrategy targetAuthStrategy;
/*     */   
/*     */   private final AuthenticationStrategy proxyAuthStrategy;
/*     */   
/*     */   private final UserTokenHandler userTokenHandler;
/*     */   
/*     */   private final RedirectStrategy redirectStrategy;
/*     */   
/*     */   private final HttpRouteDirector routeDirector;
/*     */   private final HttpAuthenticator authenticator;
/*     */   
/*     */   public MainClientExec(NHttpClientConnectionManager connmgr, HttpProcessor httpProcessor, HttpRoutePlanner routePlanner, ConnectionReuseStrategy connReuseStrategy, ConnectionKeepAliveStrategy keepaliveStrategy, RedirectStrategy redirectStrategy, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler) {
/* 113 */     this.connmgr = connmgr;
/* 114 */     this.httpProcessor = httpProcessor;
/* 115 */     this.proxyHttpProcessor = (HttpProcessor)new ImmutableHttpProcessor(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestTargetHost(), (HttpRequestInterceptor)new RequestClientConnControl() });
/*     */     
/* 117 */     this.routePlanner = routePlanner;
/* 118 */     this.connReuseStrategy = connReuseStrategy;
/* 119 */     this.keepaliveStrategy = keepaliveStrategy;
/* 120 */     this.redirectStrategy = redirectStrategy;
/* 121 */     this.targetAuthStrategy = targetAuthStrategy;
/* 122 */     this.proxyAuthStrategy = proxyAuthStrategy;
/* 123 */     this.userTokenHandler = userTokenHandler;
/* 124 */     this.routeDirector = (HttpRouteDirector)new BasicRouteDirector();
/* 125 */     this.authenticator = new HttpAuthenticator(this.log);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare(InternalState state, HttpHost target, HttpRequest original) throws HttpException, IOException {
/* 132 */     if (this.log.isDebugEnabled()) {
/* 133 */       this.log.debug("[exchange: " + state.getId() + "] start execution");
/*     */     }
/*     */     
/* 136 */     HttpClientContext localContext = state.getLocalContext();
/*     */     
/* 138 */     if (original instanceof Configurable) {
/* 139 */       RequestConfig config = ((Configurable)original).getConfig();
/* 140 */       if (config != null) {
/* 141 */         localContext.setRequestConfig(config);
/*     */       }
/*     */     } 
/*     */     
/* 145 */     List<URI> redirectLocations = localContext.getRedirectLocations();
/* 146 */     if (redirectLocations != null) {
/* 147 */       redirectLocations.clear();
/*     */     }
/*     */     
/* 150 */     HttpRequestWrapper request = HttpRequestWrapper.wrap(original);
/* 151 */     HttpRoute route = this.routePlanner.determineRoute(target, (HttpRequest)request, (HttpContext)localContext);
/* 152 */     state.setRoute(route);
/* 153 */     state.setMainRequest(request);
/* 154 */     state.setCurrentRequest(request);
/*     */     
/* 156 */     prepareRequest(state);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRequest generateRequest(InternalState state, InternalConnManager connManager) throws IOException, HttpException {
/* 162 */     HttpClientContext localContext = state.getLocalContext();
/* 163 */     HttpRoute route = state.getRoute();
/* 164 */     NHttpClientConnection managedConn = connManager.getConnection();
/* 165 */     if (!state.isRouteEstablished() && state.getRouteTracker() == null) {
/* 166 */       state.setRouteEstablished(this.connmgr.isRouteComplete(managedConn));
/* 167 */       if (!state.isRouteEstablished()) {
/* 168 */         this.log.debug("Start connection routing");
/* 169 */         state.setRouteTracker(new RouteTracker(route));
/*     */       } else {
/* 171 */         this.log.debug("Connection route already established");
/*     */       } 
/*     */     } 
/*     */     
/* 175 */     if (!state.isRouteEstablished()) {
/* 176 */       int step; RouteTracker routeTracker = state.getRouteTracker();
/*     */       do {
/*     */         HttpHost proxy;
/*     */         HttpRequest connect;
/* 180 */         HttpRoute fact = routeTracker.toRoute();
/* 181 */         step = this.routeDirector.nextStep((RouteInfo)route, (RouteInfo)fact);
/* 182 */         switch (step) {
/*     */           case 1:
/* 184 */             this.connmgr.startRoute(managedConn, route, (HttpContext)localContext);
/* 185 */             routeTracker.connectTarget(route.isSecure());
/*     */             break;
/*     */           case 2:
/* 188 */             this.connmgr.startRoute(managedConn, route, (HttpContext)localContext);
/* 189 */             proxy = route.getProxyHost();
/* 190 */             routeTracker.connectProxy(proxy, false);
/*     */             break;
/*     */           case 3:
/* 193 */             if (this.log.isDebugEnabled()) {
/* 194 */               this.log.debug("[exchange: " + state.getId() + "] Tunnel required");
/*     */             }
/* 196 */             connect = createConnectRequest(route, state);
/* 197 */             state.setCurrentRequest(HttpRequestWrapper.wrap(connect));
/*     */             break;
/*     */           case 4:
/* 200 */             throw new HttpException("Proxy chains are not supported");
/*     */           case 5:
/* 202 */             this.connmgr.upgrade(managedConn, route, (HttpContext)localContext);
/* 203 */             routeTracker.layerProtocol(route.isSecure());
/*     */             break;
/*     */           case -1:
/* 206 */             throw new HttpException("Unable to establish route: planned = " + route + "; current = " + fact);
/*     */           
/*     */           case 0:
/* 209 */             this.connmgr.routeComplete(managedConn, route, (HttpContext)localContext);
/* 210 */             state.setRouteEstablished(true);
/* 211 */             state.setRouteTracker(null);
/* 212 */             this.log.debug("Connection route established");
/*     */             break;
/*     */           default:
/* 215 */             throw new IllegalStateException("Unknown step indicator " + step + " from RouteDirector.");
/*     */         } 
/*     */       
/* 218 */       } while (step > 0);
/*     */     } 
/*     */     
/* 221 */     HttpRequestWrapper currentRequest = state.getCurrentRequest();
/* 222 */     if (currentRequest == null) {
/* 223 */       currentRequest = state.getMainRequest();
/* 224 */       state.setCurrentRequest(currentRequest);
/*     */     } 
/*     */     
/* 227 */     if (state.isRouteEstablished()) {
/* 228 */       state.incrementExecCount();
/* 229 */       if (state.getExecCount() > 1) {
/* 230 */         HttpAsyncRequestProducer requestProducer = state.getRequestProducer();
/* 231 */         if (!requestProducer.isRepeatable() && state.isRequestContentProduced()) {
/* 232 */           throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.");
/*     */         }
/*     */         
/* 235 */         requestProducer.resetRequest();
/*     */       } 
/* 237 */       if (this.log.isDebugEnabled()) {
/* 238 */         this.log.debug("[exchange: " + state.getId() + "] Attempt " + state.getExecCount() + " to execute request");
/*     */       }
/*     */ 
/*     */       
/* 242 */       if (!currentRequest.containsHeader("Authorization")) {
/* 243 */         AuthState targetAuthState = localContext.getTargetAuthState();
/* 244 */         if (this.log.isDebugEnabled()) {
/* 245 */           this.log.debug("Target auth state: " + targetAuthState.getState());
/*     */         }
/* 247 */         this.authenticator.generateAuthResponse((HttpRequest)currentRequest, targetAuthState, (HttpContext)localContext);
/*     */       } 
/* 249 */       if (!currentRequest.containsHeader("Proxy-Authorization") && !route.isTunnelled()) {
/* 250 */         AuthState proxyAuthState = localContext.getProxyAuthState();
/* 251 */         if (this.log.isDebugEnabled()) {
/* 252 */           this.log.debug("Proxy auth state: " + proxyAuthState.getState());
/*     */         }
/* 254 */         this.authenticator.generateAuthResponse((HttpRequest)currentRequest, proxyAuthState, (HttpContext)localContext);
/*     */       }
/*     */     
/* 257 */     } else if (!currentRequest.containsHeader("Proxy-Authorization")) {
/* 258 */       AuthState proxyAuthState = localContext.getProxyAuthState();
/* 259 */       if (this.log.isDebugEnabled()) {
/* 260 */         this.log.debug("Proxy auth state: " + proxyAuthState.getState());
/*     */       }
/* 262 */       this.authenticator.generateAuthResponse((HttpRequest)currentRequest, proxyAuthState, (HttpContext)localContext);
/*     */     } 
/*     */ 
/*     */     
/* 266 */     localContext.setAttribute("http.connection", managedConn);
/* 267 */     RequestConfig config = localContext.getRequestConfig();
/* 268 */     if (config.getSocketTimeout() > 0) {
/* 269 */       managedConn.setSocketTimeout(config.getSocketTimeout());
/*     */     }
/* 271 */     return (HttpRequest)currentRequest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void produceContent(InternalState state, ContentEncoder encoder, IOControl ioctrl) throws IOException {
/* 278 */     if (this.log.isDebugEnabled()) {
/* 279 */       this.log.debug("[exchange: " + state.getId() + "] produce content");
/*     */     }
/* 281 */     HttpAsyncRequestProducer requestProducer = state.getRequestProducer();
/* 282 */     state.setRequestContentProduced();
/* 283 */     requestProducer.produceContent(encoder, ioctrl);
/* 284 */     if (encoder.isCompleted()) {
/* 285 */       requestProducer.resetRequest();
/*     */     }
/*     */   }
/*     */   
/*     */   public void requestCompleted(InternalState state) {
/* 290 */     if (this.log.isDebugEnabled()) {
/* 291 */       this.log.debug("[exchange: " + state.getId() + "] Request completed");
/*     */     }
/* 293 */     HttpClientContext localContext = state.getLocalContext();
/* 294 */     HttpAsyncRequestProducer requestProducer = state.getRequestProducer();
/* 295 */     requestProducer.requestCompleted((HttpContext)localContext);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void responseReceived(InternalState state, HttpResponse response) throws IOException, HttpException {
/* 301 */     if (this.log.isDebugEnabled()) {
/* 302 */       this.log.debug("[exchange: " + state.getId() + "] Response received " + response.getStatusLine());
/*     */     }
/* 304 */     HttpClientContext context = state.getLocalContext();
/* 305 */     context.setAttribute("http.response", response);
/* 306 */     this.httpProcessor.process(response, (HttpContext)context);
/*     */     
/* 308 */     state.setCurrentResponse(response);
/*     */     
/* 310 */     if (!state.isRouteEstablished()) {
/* 311 */       int status = response.getStatusLine().getStatusCode();
/* 312 */       if (status < 200) {
/* 313 */         throw new HttpException("Unexpected response to CONNECT request: " + response.getStatusLine());
/*     */       }
/*     */       
/* 316 */       if (status == 200) {
/* 317 */         RouteTracker routeTracker = state.getRouteTracker();
/* 318 */         routeTracker.tunnelTarget(false);
/* 319 */         state.setCurrentRequest(null);
/*     */       }
/* 321 */       else if (!handleConnectResponse(state)) {
/* 322 */         state.setFinalResponse(response);
/*     */       }
/*     */     
/*     */     }
/* 326 */     else if (!handleResponse(state)) {
/* 327 */       state.setFinalResponse(response);
/*     */     } 
/*     */     
/* 330 */     if (state.getFinalResponse() != null) {
/* 331 */       HttpAsyncResponseConsumer<?> responseConsumer = state.getResponseConsumer();
/* 332 */       responseConsumer.responseReceived(response);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void consumeContent(InternalState state, ContentDecoder decoder, IOControl ioctrl) throws IOException {
/* 340 */     if (this.log.isDebugEnabled()) {
/* 341 */       this.log.debug("[exchange: " + state.getId() + "] Consume content");
/*     */     }
/* 343 */     if (state.getFinalResponse() != null) {
/* 344 */       HttpAsyncResponseConsumer<?> responseConsumer = state.getResponseConsumer();
/* 345 */       responseConsumer.consumeContent(decoder, ioctrl);
/*     */     } else {
/* 347 */       ByteBuffer tmpbuf = state.getTmpbuf();
/* 348 */       tmpbuf.clear();
/* 349 */       decoder.read(tmpbuf);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void responseCompleted(InternalState state, InternalConnManager connManager) throws IOException, HttpException {
/* 356 */     HttpClientContext localContext = state.getLocalContext();
/* 357 */     HttpResponse currentResponse = state.getCurrentResponse();
/*     */     
/* 359 */     if (!state.isRouteEstablished()) {
/* 360 */       int status = currentResponse.getStatusLine().getStatusCode();
/* 361 */       if (status == 200) {
/* 362 */         state.setCurrentResponse(null);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 367 */     NHttpClientConnection managedConn = connManager.getConnection();
/* 368 */     if (managedConn.isOpen() && this.connReuseStrategy.keepAlive(currentResponse, (HttpContext)localContext)) {
/* 369 */       long validDuration = this.keepaliveStrategy.getKeepAliveDuration(currentResponse, (HttpContext)localContext);
/*     */       
/* 371 */       if (this.log.isDebugEnabled()) {
/*     */         String s;
/* 373 */         if (validDuration > 0L) {
/* 374 */           s = "for " + validDuration + " " + TimeUnit.MILLISECONDS;
/*     */         } else {
/* 376 */           s = "indefinitely";
/*     */         } 
/* 378 */         this.log.debug("[exchange: " + state.getId() + "] Connection can be kept alive " + s);
/*     */       } 
/* 380 */       state.setValidDuration(validDuration);
/* 381 */       state.setReusable();
/*     */     } else {
/* 383 */       if (this.log.isDebugEnabled() && 
/* 384 */         managedConn.isOpen()) {
/* 385 */         this.log.debug("[exchange: " + state.getId() + "] Connection cannot be kept alive");
/*     */       }
/*     */       
/* 388 */       state.setNonReusable();
/* 389 */       connManager.releaseConnection();
/* 390 */       AuthState proxyAuthState = localContext.getProxyAuthState();
/* 391 */       if (proxyAuthState.getState() == AuthProtocolState.SUCCESS && proxyAuthState.getAuthScheme() != null && proxyAuthState.getAuthScheme().isConnectionBased()) {
/*     */ 
/*     */         
/* 394 */         if (this.log.isDebugEnabled()) {
/* 395 */           this.log.debug("[exchange: " + state.getId() + "] Resetting proxy auth state");
/*     */         }
/* 397 */         proxyAuthState.reset();
/*     */       } 
/* 399 */       AuthState targetAuthState = localContext.getTargetAuthState();
/* 400 */       if (targetAuthState.getState() == AuthProtocolState.SUCCESS && targetAuthState.getAuthScheme() != null && targetAuthState.getAuthScheme().isConnectionBased()) {
/*     */ 
/*     */         
/* 403 */         if (this.log.isDebugEnabled()) {
/* 404 */           this.log.debug("[exchange: " + state.getId() + "] Resetting target auth state");
/*     */         }
/* 406 */         targetAuthState.reset();
/*     */       } 
/*     */     } 
/*     */     
/* 410 */     Object userToken = localContext.getUserToken();
/* 411 */     if (userToken == null) {
/* 412 */       userToken = this.userTokenHandler.getUserToken((HttpContext)localContext);
/* 413 */       localContext.setAttribute("http.user-token", userToken);
/*     */     } 
/*     */     
/* 416 */     if (state.getFinalResponse() != null) {
/* 417 */       HttpAsyncResponseConsumer<?> responseConsumer = state.getResponseConsumer();
/* 418 */       responseConsumer.responseCompleted((HttpContext)localContext);
/* 419 */       if (this.log.isDebugEnabled()) {
/* 420 */         this.log.debug("[exchange: " + state.getId() + "] Response processed");
/*     */       }
/* 422 */       connManager.releaseConnection();
/*     */     }
/* 424 */     else if (state.getRedirect() != null) {
/* 425 */       HttpUriRequest redirect = state.getRedirect();
/* 426 */       URI uri = redirect.getURI();
/* 427 */       if (this.log.isDebugEnabled()) {
/* 428 */         this.log.debug("[exchange: " + state.getId() + "] Redirecting to '" + uri + "'");
/*     */       }
/* 430 */       state.setRedirect(null);
/*     */       
/* 432 */       HttpHost newTarget = URIUtils.extractHost(uri);
/* 433 */       if (newTarget == null) {
/* 434 */         throw new ProtocolException("Redirect URI does not specify a valid host name: " + uri);
/*     */       }
/*     */ 
/*     */       
/* 438 */       HttpRoute route = state.getRoute();
/* 439 */       if (!route.getTargetHost().equals(newTarget)) {
/* 440 */         AuthState targetAuthState = localContext.getTargetAuthState();
/* 441 */         if (this.log.isDebugEnabled()) {
/* 442 */           this.log.debug("[exchange: " + state.getId() + "] Resetting target auth state");
/*     */         }
/* 444 */         targetAuthState.reset();
/* 445 */         AuthState proxyAuthState = localContext.getProxyAuthState();
/* 446 */         AuthScheme authScheme = proxyAuthState.getAuthScheme();
/* 447 */         if (authScheme != null && authScheme.isConnectionBased()) {
/* 448 */           if (this.log.isDebugEnabled()) {
/* 449 */             this.log.debug("[exchange: " + state.getId() + "] Resetting proxy auth state");
/*     */           }
/* 451 */           proxyAuthState.reset();
/*     */         } 
/*     */       } 
/*     */       
/* 455 */       if (!redirect.headerIterator().hasNext()) {
/* 456 */         HttpRequest original = state.getMainRequest().getOriginal();
/* 457 */         redirect.setHeaders(original.getAllHeaders());
/*     */       } 
/*     */       
/* 460 */       HttpRequestWrapper newRequest = HttpRequestWrapper.wrap((HttpRequest)redirect);
/* 461 */       HttpRoute newRoute = this.routePlanner.determineRoute(newTarget, (HttpRequest)newRequest, (HttpContext)localContext);
/*     */       
/* 463 */       state.setRoute(newRoute);
/* 464 */       state.setMainRequest(newRequest);
/* 465 */       state.setCurrentRequest(newRequest);
/* 466 */       if (!route.equals(newRoute)) {
/* 467 */         connManager.releaseConnection();
/*     */       }
/* 469 */       prepareRequest(state);
/*     */     } 
/*     */     
/* 472 */     state.setCurrentResponse(null);
/*     */   }
/*     */   
/*     */   private void rewriteRequestURI(InternalState state) throws ProtocolException {
/* 476 */     HttpRequestWrapper request = state.getCurrentRequest();
/* 477 */     HttpRoute route = state.getRoute();
/*     */     try {
/* 479 */       URI uri = request.getURI();
/* 480 */       if (uri != null) {
/* 481 */         if (route.getProxyHost() != null && !route.isTunnelled()) {
/*     */           
/* 483 */           if (!uri.isAbsolute()) {
/* 484 */             HttpHost target = route.getTargetHost();
/* 485 */             uri = URIUtils.rewriteURI(uri, target, true);
/*     */           } else {
/* 487 */             uri = URIUtils.rewriteURI(uri);
/*     */           }
/*     */         
/*     */         }
/* 491 */         else if (uri.isAbsolute()) {
/* 492 */           uri = URIUtils.rewriteURI(uri, null, true);
/*     */         } else {
/* 494 */           uri = URIUtils.rewriteURI(uri);
/*     */         } 
/*     */         
/* 497 */         request.setURI(uri);
/*     */       } 
/* 499 */     } catch (URISyntaxException ex) {
/* 500 */       throw new ProtocolException("Invalid URI: " + request.getRequestLine().getUri(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void prepareRequest(InternalState state) throws IOException, HttpException {
/* 506 */     HttpClientContext localContext = state.getLocalContext();
/* 507 */     HttpRequestWrapper currentRequest = state.getCurrentRequest();
/* 508 */     HttpRoute route = state.getRoute();
/*     */     
/* 510 */     HttpRequest original = currentRequest.getOriginal();
/* 511 */     URI uri = null;
/* 512 */     if (original instanceof HttpUriRequest) {
/* 513 */       uri = ((HttpUriRequest)original).getURI();
/*     */     } else {
/* 515 */       String uriString = original.getRequestLine().getUri();
/*     */       try {
/* 517 */         uri = URI.create(uriString);
/* 518 */       } catch (IllegalArgumentException ex) {
/* 519 */         if (this.log.isDebugEnabled()) {
/* 520 */           this.log.debug("Unable to parse '" + uriString + "' as a valid URI; " + "request URI and Host header may be inconsistent", ex);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 526 */     currentRequest.setURI(uri);
/*     */ 
/*     */     
/* 529 */     rewriteRequestURI(state);
/*     */     
/* 531 */     HttpHost target = null;
/* 532 */     if (uri != null && uri.isAbsolute() && uri.getHost() != null) {
/* 533 */       target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
/*     */     }
/* 535 */     if (target == null) {
/* 536 */       target = route.getTargetHost();
/*     */     }
/*     */ 
/*     */     
/* 540 */     if (uri != null) {
/* 541 */       String userinfo = uri.getUserInfo();
/* 542 */       if (userinfo != null) {
/* 543 */         CredentialsProvider credsProvider = localContext.getCredentialsProvider();
/* 544 */         credsProvider.setCredentials(new AuthScope(target), (Credentials)new UsernamePasswordCredentials(userinfo));
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 550 */     localContext.setAttribute("http.request", currentRequest);
/* 551 */     localContext.setAttribute("http.target_host", target);
/* 552 */     localContext.setAttribute("http.route", route);
/* 553 */     this.httpProcessor.process((HttpRequest)currentRequest, (HttpContext)localContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HttpRequest createConnectRequest(HttpRoute route, InternalState state) throws IOException, HttpException {
/* 561 */     HttpHost target = route.getTargetHost();
/* 562 */     String host = target.getHostName();
/* 563 */     int port = target.getPort();
/* 564 */     StringBuilder buffer = new StringBuilder(host.length() + 6);
/* 565 */     buffer.append(host);
/* 566 */     buffer.append(':');
/* 567 */     buffer.append(Integer.toString(port));
/* 568 */     BasicHttpRequest basicHttpRequest = new BasicHttpRequest("CONNECT", buffer.toString(), (ProtocolVersion)HttpVersion.HTTP_1_1);
/* 569 */     HttpClientContext localContext = state.getLocalContext();
/* 570 */     this.proxyHttpProcessor.process((HttpRequest)basicHttpRequest, (HttpContext)localContext);
/* 571 */     return (HttpRequest)basicHttpRequest;
/*     */   }
/*     */   
/*     */   private boolean handleConnectResponse(InternalState state) throws HttpException {
/* 575 */     HttpClientContext localContext = state.getLocalContext();
/* 576 */     RequestConfig config = localContext.getRequestConfig();
/* 577 */     if (config.isAuthenticationEnabled()) {
/* 578 */       CredentialsProvider credsProvider = localContext.getCredentialsProvider();
/* 579 */       if (credsProvider != null) {
/* 580 */         HttpRoute route = state.getRoute();
/* 581 */         HttpHost proxy = route.getProxyHost();
/* 582 */         HttpResponse currentResponse = state.getCurrentResponse();
/* 583 */         AuthState proxyAuthState = localContext.getProxyAuthState();
/* 584 */         if (this.authenticator.isAuthenticationRequested(proxy, currentResponse, this.proxyAuthStrategy, proxyAuthState, (HttpContext)localContext))
/*     */         {
/* 586 */           return this.authenticator.handleAuthChallenge(proxy, currentResponse, this.proxyAuthStrategy, proxyAuthState, (HttpContext)localContext);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 591 */     return false;
/*     */   }
/*     */   
/*     */   private boolean handleResponse(InternalState state) throws HttpException {
/* 595 */     HttpClientContext localContext = state.getLocalContext();
/* 596 */     RequestConfig config = localContext.getRequestConfig();
/* 597 */     if (config.isAuthenticationEnabled() && 
/* 598 */       needAuthentication(state)) {
/*     */       
/* 600 */       HttpRequestWrapper currentRequest = state.getCurrentRequest();
/* 601 */       HttpRequest original = currentRequest.getOriginal();
/* 602 */       if (!original.containsHeader("Authorization")) {
/* 603 */         currentRequest.removeHeaders("Authorization");
/*     */       }
/* 605 */       if (!original.containsHeader("Proxy-Authorization")) {
/* 606 */         currentRequest.removeHeaders("Proxy-Authorization");
/*     */       }
/* 608 */       return true;
/*     */     } 
/*     */     
/* 611 */     if (config.isRedirectsEnabled()) {
/* 612 */       HttpRequestWrapper httpRequestWrapper = state.getCurrentRequest();
/* 613 */       HttpResponse currentResponse = state.getCurrentResponse();
/* 614 */       if (this.redirectStrategy.isRedirected((HttpRequest)httpRequestWrapper, currentResponse, (HttpContext)localContext)) {
/* 615 */         int maxRedirects = (config.getMaxRedirects() >= 0) ? config.getMaxRedirects() : 100;
/* 616 */         if (state.getRedirectCount() >= maxRedirects) {
/* 617 */           throw new RedirectException("Maximum redirects (" + maxRedirects + ") exceeded");
/*     */         }
/* 619 */         state.incrementRedirectCount();
/* 620 */         HttpUriRequest redirect = this.redirectStrategy.getRedirect((HttpRequest)httpRequestWrapper, currentResponse, (HttpContext)localContext);
/*     */         
/* 622 */         state.setRedirect(redirect);
/* 623 */         return true;
/*     */       } 
/*     */     } 
/* 626 */     return false;
/*     */   }
/*     */   
/*     */   private boolean needAuthentication(InternalState state) throws HttpException {
/* 630 */     HttpClientContext localContext = state.getLocalContext();
/* 631 */     CredentialsProvider credsProvider = localContext.getCredentialsProvider();
/* 632 */     if (credsProvider != null) {
/* 633 */       HttpRoute route = state.getRoute();
/* 634 */       HttpResponse currentResponse = state.getCurrentResponse();
/* 635 */       HttpHost target = localContext.getTargetHost();
/* 636 */       if (target == null) {
/* 637 */         target = route.getTargetHost();
/*     */       }
/* 639 */       if (target.getPort() < 0) {
/* 640 */         target = new HttpHost(target.getHostName(), route.getTargetHost().getPort(), target.getSchemeName());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 645 */       AuthState targetAuthState = localContext.getTargetAuthState();
/* 646 */       AuthState proxyAuthState = localContext.getProxyAuthState();
/*     */       
/* 648 */       boolean targetAuthRequested = this.authenticator.isAuthenticationRequested(target, currentResponse, this.targetAuthStrategy, targetAuthState, (HttpContext)localContext);
/*     */ 
/*     */       
/* 651 */       HttpHost proxy = route.getProxyHost();
/*     */       
/* 653 */       if (proxy == null) {
/* 654 */         proxy = route.getTargetHost();
/*     */       }
/* 656 */       boolean proxyAuthRequested = this.authenticator.isAuthenticationRequested(proxy, currentResponse, this.proxyAuthStrategy, proxyAuthState, (HttpContext)localContext);
/*     */ 
/*     */       
/* 659 */       if (targetAuthRequested) {
/* 660 */         return this.authenticator.handleAuthChallenge(target, currentResponse, this.targetAuthStrategy, targetAuthState, (HttpContext)localContext);
/*     */       }
/*     */       
/* 663 */       if (proxyAuthRequested) {
/* 664 */         return this.authenticator.handleAuthChallenge(proxy, currentResponse, this.proxyAuthStrategy, proxyAuthState, (HttpContext)localContext);
/*     */       }
/*     */     } 
/*     */     
/* 668 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/MainClientExec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
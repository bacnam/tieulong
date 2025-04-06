/*     */ package org.apache.http.impl.nio.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.auth.AuthSchemeFactory;
/*     */ import org.apache.http.auth.AuthSchemeRegistry;
/*     */ import org.apache.http.client.AuthenticationStrategy;
/*     */ import org.apache.http.client.ClientProtocolException;
/*     */ import org.apache.http.client.CookieStore;
/*     */ import org.apache.http.client.CredentialsProvider;
/*     */ import org.apache.http.client.RedirectStrategy;
/*     */ import org.apache.http.client.UserTokenHandler;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.utils.URIUtils;
/*     */ import org.apache.http.concurrent.BasicFuture;
/*     */ import org.apache.http.concurrent.FutureCallback;
/*     */ import org.apache.http.conn.ConnectionKeepAliveStrategy;
/*     */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*     */ import org.apache.http.cookie.CookieSpecFactory;
/*     */ import org.apache.http.cookie.CookieSpecRegistry;
/*     */ import org.apache.http.impl.DefaultConnectionReuseStrategy;
/*     */ import org.apache.http.impl.auth.BasicSchemeFactory;
/*     */ import org.apache.http.impl.auth.DigestSchemeFactory;
/*     */ import org.apache.http.impl.auth.KerberosSchemeFactory;
/*     */ import org.apache.http.impl.auth.NTLMSchemeFactory;
/*     */ import org.apache.http.impl.auth.SPNegoSchemeFactory;
/*     */ import org.apache.http.impl.client.BasicCookieStore;
/*     */ import org.apache.http.impl.client.BasicCredentialsProvider;
/*     */ import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
/*     */ import org.apache.http.impl.client.DefaultRedirectStrategy;
/*     */ import org.apache.http.impl.client.DefaultUserTokenHandler;
/*     */ import org.apache.http.impl.client.ProxyAuthenticationStrategy;
/*     */ import org.apache.http.impl.client.TargetAuthenticationStrategy;
/*     */ import org.apache.http.impl.cookie.BestMatchSpecFactory;
/*     */ import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
/*     */ import org.apache.http.impl.cookie.IgnoreSpecFactory;
/*     */ import org.apache.http.impl.cookie.NetscapeDraftSpecFactory;
/*     */ import org.apache.http.impl.cookie.RFC2109SpecFactory;
/*     */ import org.apache.http.impl.cookie.RFC2965SpecFactory;
/*     */ import org.apache.http.impl.nio.DefaultHttpClientIODispatch;
/*     */ import org.apache.http.impl.nio.conn.DefaultHttpAsyncRoutePlanner;
/*     */ import org.apache.http.impl.nio.conn.PoolingClientAsyncConnectionManager;
/*     */ import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
/*     */ import org.apache.http.impl.nio.reactor.IOReactorConfig;
/*     */ import org.apache.http.nio.NHttpClientEventHandler;
/*     */ import org.apache.http.nio.client.HttpAsyncClient;
/*     */ import org.apache.http.nio.client.methods.HttpAsyncMethods;
/*     */ import org.apache.http.nio.conn.ClientAsyncConnectionManager;
/*     */ import org.apache.http.nio.protocol.HttpAsyncRequestExecutionHandler;
/*     */ import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
/*     */ import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
/*     */ import org.apache.http.nio.reactor.ConnectingIOReactor;
/*     */ import org.apache.http.nio.reactor.IOEventDispatch;
/*     */ import org.apache.http.nio.reactor.IOReactorException;
/*     */ import org.apache.http.nio.reactor.IOReactorStatus;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.BasicHttpContext;
/*     */ import org.apache.http.protocol.BasicHttpProcessor;
/*     */ import org.apache.http.protocol.DefaultedHttpContext;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.protocol.HttpProcessor;
/*     */ import org.apache.http.protocol.ImmutableHttpProcessor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AbstractHttpAsyncClient
/*     */   implements HttpAsyncClient
/*     */ {
/* 104 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final ClientAsyncConnectionManager connmgr;
/*     */   
/*     */   private final Queue<HttpAsyncRequestExecutionHandler<?>> queue;
/*     */   
/*     */   private Thread reactorThread;
/*     */   private BasicHttpProcessor mutableProcessor;
/*     */   private ImmutableHttpProcessor protocolProcessor;
/*     */   private ConnectionReuseStrategy reuseStrategy;
/*     */   private ConnectionKeepAliveStrategy keepAliveStrategy;
/*     */   private RedirectStrategy redirectStrategy;
/*     */   private CookieSpecRegistry supportedCookieSpecs;
/*     */   private CookieStore cookieStore;
/*     */   private AuthSchemeRegistry supportedAuthSchemes;
/*     */   private AuthenticationStrategy targetAuthStrategy;
/*     */   private AuthenticationStrategy proxyAuthStrategy;
/*     */   private CredentialsProvider credsProvider;
/*     */   private HttpRoutePlanner routePlanner;
/*     */   private UserTokenHandler userTokenHandler;
/*     */   private HttpParams params;
/*     */   private volatile boolean terminated;
/*     */   
/*     */   protected AbstractHttpAsyncClient(ClientAsyncConnectionManager connmgr) {
/* 128 */     this.connmgr = connmgr;
/* 129 */     this.queue = new ConcurrentLinkedQueue<HttpAsyncRequestExecutionHandler<?>>();
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractHttpAsyncClient(IOReactorConfig config) throws IOReactorException {
/* 134 */     DefaultConnectingIOReactor defaultioreactor = new DefaultConnectingIOReactor(config);
/* 135 */     defaultioreactor.setExceptionHandler(new InternalIOReactorExceptionHandler(this.log));
/* 136 */     this.connmgr = (ClientAsyncConnectionManager)new PoolingClientAsyncConnectionManager((ConnectingIOReactor)defaultioreactor);
/* 137 */     this.queue = new ConcurrentLinkedQueue<HttpAsyncRequestExecutionHandler<?>>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpContext createHttpContext() {
/* 145 */     BasicHttpContext basicHttpContext = new BasicHttpContext();
/* 146 */     basicHttpContext.setAttribute("http.scheme-registry", getConnectionManager().getSchemeRegistry());
/*     */ 
/*     */     
/* 149 */     basicHttpContext.setAttribute("http.authscheme-registry", getAuthSchemes());
/*     */ 
/*     */     
/* 152 */     basicHttpContext.setAttribute("http.cookiespec-registry", getCookieSpecs());
/*     */ 
/*     */     
/* 155 */     basicHttpContext.setAttribute("http.cookie-store", getCookieStore());
/*     */ 
/*     */     
/* 158 */     basicHttpContext.setAttribute("http.auth.credentials-provider", getCredentialsProvider());
/*     */ 
/*     */     
/* 161 */     return (HttpContext)basicHttpContext;
/*     */   }
/*     */   
/*     */   protected ConnectionReuseStrategy createConnectionReuseStrategy() {
/* 165 */     return (ConnectionReuseStrategy)new DefaultConnectionReuseStrategy();
/*     */   }
/*     */   
/*     */   protected ConnectionKeepAliveStrategy createConnectionKeepAliveStrategy() {
/* 169 */     return (ConnectionKeepAliveStrategy)new DefaultConnectionKeepAliveStrategy();
/*     */   }
/*     */   
/*     */   protected AuthSchemeRegistry createAuthSchemeRegistry() {
/* 173 */     AuthSchemeRegistry registry = new AuthSchemeRegistry();
/* 174 */     registry.register("Basic", (AuthSchemeFactory)new BasicSchemeFactory());
/*     */ 
/*     */     
/* 177 */     registry.register("Digest", (AuthSchemeFactory)new DigestSchemeFactory());
/*     */ 
/*     */     
/* 180 */     registry.register("NTLM", (AuthSchemeFactory)new NTLMSchemeFactory());
/*     */ 
/*     */     
/* 183 */     registry.register("negotiate", (AuthSchemeFactory)new SPNegoSchemeFactory());
/*     */ 
/*     */     
/* 186 */     registry.register("Kerberos", (AuthSchemeFactory)new KerberosSchemeFactory());
/*     */ 
/*     */     
/* 189 */     return registry;
/*     */   }
/*     */   
/*     */   protected CookieSpecRegistry createCookieSpecRegistry() {
/* 193 */     CookieSpecRegistry registry = new CookieSpecRegistry();
/* 194 */     registry.register("best-match", (CookieSpecFactory)new BestMatchSpecFactory());
/*     */ 
/*     */     
/* 197 */     registry.register("compatibility", (CookieSpecFactory)new BrowserCompatSpecFactory());
/*     */ 
/*     */     
/* 200 */     registry.register("netscape", (CookieSpecFactory)new NetscapeDraftSpecFactory());
/*     */ 
/*     */     
/* 203 */     registry.register("rfc2109", (CookieSpecFactory)new RFC2109SpecFactory());
/*     */ 
/*     */     
/* 206 */     registry.register("rfc2965", (CookieSpecFactory)new RFC2965SpecFactory());
/*     */ 
/*     */     
/* 209 */     registry.register("ignoreCookies", (CookieSpecFactory)new IgnoreSpecFactory());
/*     */ 
/*     */     
/* 212 */     return registry;
/*     */   }
/*     */   
/*     */   protected AuthenticationStrategy createTargetAuthenticationStrategy() {
/* 216 */     return (AuthenticationStrategy)new TargetAuthenticationStrategy();
/*     */   }
/*     */   
/*     */   protected AuthenticationStrategy createProxyAuthenticationStrategy() {
/* 220 */     return (AuthenticationStrategy)new ProxyAuthenticationStrategy();
/*     */   }
/*     */   
/*     */   protected CookieStore createCookieStore() {
/* 224 */     return (CookieStore)new BasicCookieStore();
/*     */   }
/*     */   
/*     */   protected CredentialsProvider createCredentialsProvider() {
/* 228 */     return (CredentialsProvider)new BasicCredentialsProvider();
/*     */   }
/*     */   
/*     */   protected HttpRoutePlanner createHttpRoutePlanner() {
/* 232 */     return (HttpRoutePlanner)new DefaultHttpAsyncRoutePlanner(getConnectionManager().getSchemeRegistry());
/*     */   }
/*     */   
/*     */   protected UserTokenHandler createUserTokenHandler() {
/* 236 */     return (UserTokenHandler)new DefaultUserTokenHandler();
/*     */   }
/*     */   
/*     */   public final synchronized HttpParams getParams() {
/* 240 */     if (this.params == null) {
/* 241 */       this.params = createHttpParams();
/*     */     }
/* 243 */     return this.params;
/*     */   }
/*     */   
/*     */   public synchronized void setParams(HttpParams params) {
/* 247 */     this.params = params;
/*     */   }
/*     */   
/*     */   public synchronized ClientAsyncConnectionManager getConnectionManager() {
/* 251 */     return this.connmgr;
/*     */   }
/*     */   
/*     */   public final synchronized ConnectionReuseStrategy getConnectionReuseStrategy() {
/* 255 */     if (this.reuseStrategy == null) {
/* 256 */       this.reuseStrategy = createConnectionReuseStrategy();
/*     */     }
/* 258 */     return this.reuseStrategy;
/*     */   }
/*     */   
/*     */   public synchronized void setReuseStrategy(ConnectionReuseStrategy reuseStrategy) {
/* 262 */     this.reuseStrategy = reuseStrategy;
/*     */   }
/*     */   
/*     */   public final synchronized ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy() {
/* 266 */     if (this.keepAliveStrategy == null) {
/* 267 */       this.keepAliveStrategy = createConnectionKeepAliveStrategy();
/*     */     }
/* 269 */     return this.keepAliveStrategy;
/*     */   }
/*     */   
/*     */   public synchronized void setKeepAliveStrategy(ConnectionKeepAliveStrategy keepAliveStrategy) {
/* 273 */     this.keepAliveStrategy = keepAliveStrategy;
/*     */   }
/*     */   
/*     */   public final synchronized RedirectStrategy getRedirectStrategy() {
/* 277 */     if (this.redirectStrategy == null) {
/* 278 */       this.redirectStrategy = (RedirectStrategy)new DefaultRedirectStrategy();
/*     */     }
/* 280 */     return this.redirectStrategy;
/*     */   }
/*     */   
/*     */   public synchronized void setRedirectStrategy(RedirectStrategy redirectStrategy) {
/* 284 */     this.redirectStrategy = redirectStrategy;
/*     */   }
/*     */   
/*     */   public final synchronized AuthSchemeRegistry getAuthSchemes() {
/* 288 */     if (this.supportedAuthSchemes == null) {
/* 289 */       this.supportedAuthSchemes = createAuthSchemeRegistry();
/*     */     }
/* 291 */     return this.supportedAuthSchemes;
/*     */   }
/*     */   
/*     */   public synchronized void setAuthSchemes(AuthSchemeRegistry authSchemeRegistry) {
/* 295 */     this.supportedAuthSchemes = authSchemeRegistry;
/*     */   }
/*     */   
/*     */   public final synchronized CookieSpecRegistry getCookieSpecs() {
/* 299 */     if (this.supportedCookieSpecs == null) {
/* 300 */       this.supportedCookieSpecs = createCookieSpecRegistry();
/*     */     }
/* 302 */     return this.supportedCookieSpecs;
/*     */   }
/*     */   
/*     */   public synchronized void setCookieSpecs(CookieSpecRegistry cookieSpecRegistry) {
/* 306 */     this.supportedCookieSpecs = cookieSpecRegistry;
/*     */   }
/*     */   
/*     */   public final synchronized AuthenticationStrategy getTargetAuthenticationStrategy() {
/* 310 */     if (this.targetAuthStrategy == null) {
/* 311 */       this.targetAuthStrategy = createTargetAuthenticationStrategy();
/*     */     }
/* 313 */     return this.targetAuthStrategy;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setTargetAuthenticationStrategy(AuthenticationStrategy targetAuthStrategy) {
/* 318 */     this.targetAuthStrategy = targetAuthStrategy;
/*     */   }
/*     */   
/*     */   public final synchronized AuthenticationStrategy getProxyAuthenticationStrategy() {
/* 322 */     if (this.proxyAuthStrategy == null) {
/* 323 */       this.proxyAuthStrategy = createProxyAuthenticationStrategy();
/*     */     }
/* 325 */     return this.proxyAuthStrategy;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setProxyAuthenticationStrategy(AuthenticationStrategy proxyAuthStrategy) {
/* 330 */     this.proxyAuthStrategy = proxyAuthStrategy;
/*     */   }
/*     */   
/*     */   public final synchronized CookieStore getCookieStore() {
/* 334 */     if (this.cookieStore == null) {
/* 335 */       this.cookieStore = createCookieStore();
/*     */     }
/* 337 */     return this.cookieStore;
/*     */   }
/*     */   
/*     */   public synchronized void setCookieStore(CookieStore cookieStore) {
/* 341 */     this.cookieStore = cookieStore;
/*     */   }
/*     */   
/*     */   public final synchronized CredentialsProvider getCredentialsProvider() {
/* 345 */     if (this.credsProvider == null) {
/* 346 */       this.credsProvider = createCredentialsProvider();
/*     */     }
/* 348 */     return this.credsProvider;
/*     */   }
/*     */   
/*     */   public synchronized void setCredentialsProvider(CredentialsProvider credsProvider) {
/* 352 */     this.credsProvider = credsProvider;
/*     */   }
/*     */   
/*     */   public final synchronized HttpRoutePlanner getRoutePlanner() {
/* 356 */     if (this.routePlanner == null) {
/* 357 */       this.routePlanner = createHttpRoutePlanner();
/*     */     }
/* 359 */     return this.routePlanner;
/*     */   }
/*     */   
/*     */   public synchronized void setRoutePlanner(HttpRoutePlanner routePlanner) {
/* 363 */     this.routePlanner = routePlanner;
/*     */   }
/*     */   
/*     */   public final synchronized UserTokenHandler getUserTokenHandler() {
/* 367 */     if (this.userTokenHandler == null) {
/* 368 */       this.userTokenHandler = createUserTokenHandler();
/*     */     }
/* 370 */     return this.userTokenHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setUserTokenHandler(UserTokenHandler userTokenHandler) {
/* 375 */     this.userTokenHandler = userTokenHandler;
/*     */   }
/*     */   
/*     */   protected final synchronized BasicHttpProcessor getHttpProcessor() {
/* 379 */     if (this.mutableProcessor == null) {
/* 380 */       this.mutableProcessor = createHttpProcessor();
/*     */     }
/* 382 */     return this.mutableProcessor;
/*     */   }
/*     */   
/*     */   private final synchronized HttpProcessor getProtocolProcessor() {
/* 386 */     if (this.protocolProcessor == null) {
/*     */       
/* 388 */       BasicHttpProcessor proc = getHttpProcessor();
/*     */       
/* 390 */       int reqc = proc.getRequestInterceptorCount();
/* 391 */       HttpRequestInterceptor[] reqinterceptors = new HttpRequestInterceptor[reqc];
/* 392 */       for (int i = 0; i < reqc; i++) {
/* 393 */         reqinterceptors[i] = proc.getRequestInterceptor(i);
/*     */       }
/* 395 */       int resc = proc.getResponseInterceptorCount();
/* 396 */       HttpResponseInterceptor[] resinterceptors = new HttpResponseInterceptor[resc];
/* 397 */       for (int j = 0; j < resc; j++) {
/* 398 */         resinterceptors[j] = proc.getResponseInterceptor(j);
/*     */       }
/* 400 */       this.protocolProcessor = new ImmutableHttpProcessor(reqinterceptors, resinterceptors);
/*     */     } 
/* 402 */     return (HttpProcessor)this.protocolProcessor;
/*     */   }
/*     */   
/*     */   public synchronized int getResponseInterceptorCount() {
/* 406 */     return getHttpProcessor().getResponseInterceptorCount();
/*     */   }
/*     */   
/*     */   public synchronized HttpResponseInterceptor getResponseInterceptor(int index) {
/* 410 */     return getHttpProcessor().getResponseInterceptor(index);
/*     */   }
/*     */   
/*     */   public synchronized HttpRequestInterceptor getRequestInterceptor(int index) {
/* 414 */     return getHttpProcessor().getRequestInterceptor(index);
/*     */   }
/*     */   
/*     */   public synchronized int getRequestInterceptorCount() {
/* 418 */     return getHttpProcessor().getRequestInterceptorCount();
/*     */   }
/*     */   
/*     */   public synchronized void addResponseInterceptor(HttpResponseInterceptor itcp) {
/* 422 */     getHttpProcessor().addInterceptor(itcp);
/* 423 */     this.protocolProcessor = null;
/*     */   }
/*     */   
/*     */   public synchronized void addResponseInterceptor(HttpResponseInterceptor itcp, int index) {
/* 427 */     getHttpProcessor().addInterceptor(itcp, index);
/* 428 */     this.protocolProcessor = null;
/*     */   }
/*     */   
/*     */   public synchronized void clearResponseInterceptors() {
/* 432 */     getHttpProcessor().clearResponseInterceptors();
/* 433 */     this.protocolProcessor = null;
/*     */   }
/*     */   
/*     */   public synchronized void removeResponseInterceptorByClass(Class<? extends HttpResponseInterceptor> clazz) {
/* 437 */     getHttpProcessor().removeResponseInterceptorByClass(clazz);
/* 438 */     this.protocolProcessor = null;
/*     */   }
/*     */   
/*     */   public synchronized void addRequestInterceptor(HttpRequestInterceptor itcp) {
/* 442 */     getHttpProcessor().addInterceptor(itcp);
/* 443 */     this.protocolProcessor = null;
/*     */   }
/*     */   
/*     */   public synchronized void addRequestInterceptor(HttpRequestInterceptor itcp, int index) {
/* 447 */     getHttpProcessor().addInterceptor(itcp, index);
/* 448 */     this.protocolProcessor = null;
/*     */   }
/*     */   
/*     */   public synchronized void clearRequestInterceptors() {
/* 452 */     getHttpProcessor().clearRequestInterceptors();
/* 453 */     this.protocolProcessor = null;
/*     */   }
/*     */   
/*     */   public synchronized void removeRequestInterceptorByClass(Class<? extends HttpRequestInterceptor> clazz) {
/* 457 */     getHttpProcessor().removeRequestInterceptorByClass(clazz);
/* 458 */     this.protocolProcessor = null;
/*     */   }
/*     */   
/*     */   private void doExecute() {
/* 462 */     LoggingAsyncRequestExecutor handler = new LoggingAsyncRequestExecutor();
/*     */     try {
/* 464 */       DefaultHttpClientIODispatch defaultHttpClientIODispatch = new DefaultHttpClientIODispatch((NHttpClientEventHandler)handler, getParams());
/* 465 */       this.connmgr.execute((IOEventDispatch)defaultHttpClientIODispatch);
/* 466 */     } catch (Exception ex) {
/* 467 */       this.log.error("I/O reactor terminated abnormally", ex);
/*     */     } finally {
/* 469 */       this.terminated = true;
/* 470 */       while (!this.queue.isEmpty()) {
/* 471 */         HttpAsyncRequestExecutionHandler<?> exchangeHandler = this.queue.remove();
/* 472 */         exchangeHandler.cancel();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public IOReactorStatus getStatus() {
/* 478 */     return this.connmgr.getStatus();
/*     */   }
/*     */   
/*     */   public synchronized void start() {
/* 482 */     this.reactorThread = new Thread()
/*     */       {
/*     */         public void run()
/*     */         {
/* 486 */           AbstractHttpAsyncClient.this.doExecute();
/*     */         }
/*     */       };
/*     */     
/* 490 */     this.reactorThread.start();
/*     */   }
/*     */   
/*     */   public void shutdown() throws InterruptedException {
/*     */     try {
/* 495 */       this.connmgr.shutdown(5000L);
/* 496 */     } catch (IOException ex) {
/* 497 */       this.log.error("I/O error shutting down", ex);
/*     */     } 
/* 499 */     if (this.reactorThread != null) {
/* 500 */       this.reactorThread.join();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, HttpContext context, FutureCallback<T> callback) {
/*     */     DefaultAsyncRequestDirector<T> httpexchange;
/* 509 */     if (this.terminated) {
/* 510 */       throw new IllegalStateException("Client has been shut down");
/*     */     }
/* 512 */     BasicFuture<T> future = new BasicFuture(callback);
/* 513 */     ResultCallback<T> resultCallback = new DefaultResultCallback<T>(future, this.queue);
/*     */     
/* 515 */     synchronized (this) {
/* 516 */       DefaultedHttpContext defaultedHttpContext; HttpContext defaultContext = createHttpContext();
/*     */       
/* 518 */       if (context == null) {
/* 519 */         HttpContext execContext = defaultContext;
/*     */       } else {
/* 521 */         defaultedHttpContext = new DefaultedHttpContext(context, defaultContext);
/*     */       } 
/* 523 */       httpexchange = new DefaultAsyncRequestDirector<T>(this.log, requestProducer, responseConsumer, (HttpContext)defaultedHttpContext, resultCallback, this.connmgr, getProtocolProcessor(), getRoutePlanner(), getConnectionReuseStrategy(), getConnectionKeepAliveStrategy(), getRedirectStrategy(), getTargetAuthenticationStrategy(), getProxyAuthenticationStrategy(), getUserTokenHandler(), getParams());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 540 */     this.queue.add(httpexchange);
/* 541 */     httpexchange.start();
/* 542 */     return (Future<T>)future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, FutureCallback<T> callback) {
/* 549 */     return execute(requestProducer, responseConsumer, (HttpContext)new BasicHttpContext(), callback);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<HttpResponse> execute(HttpHost target, HttpRequest request, HttpContext context, FutureCallback<HttpResponse> callback) {
/* 555 */     return execute(HttpAsyncMethods.create(target, request), HttpAsyncMethods.createConsumer(), context, callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<HttpResponse> execute(HttpHost target, HttpRequest request, FutureCallback<HttpResponse> callback) {
/* 564 */     return execute(target, request, (HttpContext)new BasicHttpContext(), callback);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<HttpResponse> execute(HttpUriRequest request, FutureCallback<HttpResponse> callback) {
/* 570 */     return execute(request, (HttpContext)new BasicHttpContext(), callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<HttpResponse> execute(HttpUriRequest request, HttpContext context, FutureCallback<HttpResponse> callback) {
/*     */     HttpHost target;
/*     */     try {
/* 579 */       target = determineTarget(request);
/* 580 */     } catch (ClientProtocolException ex) {
/* 581 */       BasicFuture<HttpResponse> future = new BasicFuture(callback);
/* 582 */       future.failed((Exception)ex);
/* 583 */       return (Future<HttpResponse>)future;
/*     */     } 
/* 585 */     return execute(target, (HttpRequest)request, context, callback);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private HttpHost determineTarget(HttpUriRequest request) throws ClientProtocolException {
/* 591 */     HttpHost target = null;
/*     */     
/* 593 */     URI requestURI = request.getURI();
/* 594 */     if (requestURI.isAbsolute()) {
/* 595 */       target = URIUtils.extractHost(requestURI);
/* 596 */       if (target == null) {
/* 597 */         throw new ClientProtocolException("URI does not specify a valid host name: " + requestURI);
/*     */       }
/*     */     } 
/*     */     
/* 601 */     return target;
/*     */   }
/*     */   
/*     */   protected abstract HttpParams createHttpParams();
/*     */   
/*     */   protected abstract BasicHttpProcessor createHttpProcessor();
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/AbstractHttpAsyncClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*      */ package org.apache.http.impl.client;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.UndeclaredThrowableException;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.apache.http.ConnectionReuseStrategy;
/*      */ import org.apache.http.HttpException;
/*      */ import org.apache.http.HttpHost;
/*      */ import org.apache.http.HttpRequest;
/*      */ import org.apache.http.HttpRequestInterceptor;
/*      */ import org.apache.http.HttpResponse;
/*      */ import org.apache.http.HttpResponseInterceptor;
/*      */ import org.apache.http.annotation.GuardedBy;
/*      */ import org.apache.http.annotation.ThreadSafe;
/*      */ import org.apache.http.auth.AuthSchemeFactory;
/*      */ import org.apache.http.auth.AuthSchemeRegistry;
/*      */ import org.apache.http.client.AuthenticationHandler;
/*      */ import org.apache.http.client.AuthenticationStrategy;
/*      */ import org.apache.http.client.BackoffManager;
/*      */ import org.apache.http.client.ClientProtocolException;
/*      */ import org.apache.http.client.ConnectionBackoffStrategy;
/*      */ import org.apache.http.client.CookieStore;
/*      */ import org.apache.http.client.CredentialsProvider;
/*      */ import org.apache.http.client.HttpRequestRetryHandler;
/*      */ import org.apache.http.client.RedirectHandler;
/*      */ import org.apache.http.client.RedirectStrategy;
/*      */ import org.apache.http.client.RequestDirector;
/*      */ import org.apache.http.client.UserTokenHandler;
/*      */ import org.apache.http.client.config.RequestConfig;
/*      */ import org.apache.http.client.methods.CloseableHttpResponse;
/*      */ import org.apache.http.client.params.HttpClientParamConfig;
/*      */ import org.apache.http.conn.ClientConnectionManager;
/*      */ import org.apache.http.conn.ClientConnectionManagerFactory;
/*      */ import org.apache.http.conn.ConnectionKeepAliveStrategy;
/*      */ import org.apache.http.conn.routing.HttpRoute;
/*      */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*      */ import org.apache.http.conn.scheme.SchemeRegistry;
/*      */ import org.apache.http.cookie.CookieSpecFactory;
/*      */ import org.apache.http.cookie.CookieSpecRegistry;
/*      */ import org.apache.http.impl.DefaultConnectionReuseStrategy;
/*      */ import org.apache.http.impl.auth.BasicSchemeFactory;
/*      */ import org.apache.http.impl.auth.DigestSchemeFactory;
/*      */ import org.apache.http.impl.auth.KerberosSchemeFactory;
/*      */ import org.apache.http.impl.auth.NTLMSchemeFactory;
/*      */ import org.apache.http.impl.auth.SPNegoSchemeFactory;
/*      */ import org.apache.http.impl.conn.BasicClientConnectionManager;
/*      */ import org.apache.http.impl.conn.DefaultHttpRoutePlanner;
/*      */ import org.apache.http.impl.conn.SchemeRegistryFactory;
/*      */ import org.apache.http.impl.cookie.BestMatchSpecFactory;
/*      */ import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
/*      */ import org.apache.http.impl.cookie.IgnoreSpecFactory;
/*      */ import org.apache.http.impl.cookie.NetscapeDraftSpecFactory;
/*      */ import org.apache.http.impl.cookie.RFC2109SpecFactory;
/*      */ import org.apache.http.impl.cookie.RFC2965SpecFactory;
/*      */ import org.apache.http.params.HttpParams;
/*      */ import org.apache.http.protocol.BasicHttpContext;
/*      */ import org.apache.http.protocol.BasicHttpProcessor;
/*      */ import org.apache.http.protocol.DefaultedHttpContext;
/*      */ import org.apache.http.protocol.HttpContext;
/*      */ import org.apache.http.protocol.HttpProcessor;
/*      */ import org.apache.http.protocol.HttpRequestExecutor;
/*      */ import org.apache.http.protocol.ImmutableHttpProcessor;
/*      */ import org.apache.http.util.Args;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */ @ThreadSafe
/*      */ public abstract class AbstractHttpClient
/*      */   extends CloseableHttpClient
/*      */ {
/*  201 */   private final Log log = LogFactory.getLog(getClass());
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private HttpParams defaultParams;
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private HttpRequestExecutor requestExec;
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private ClientConnectionManager connManager;
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private ConnectionReuseStrategy reuseStrategy;
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private ConnectionKeepAliveStrategy keepAliveStrategy;
/*      */ 
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private CookieSpecRegistry supportedCookieSpecs;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private AuthSchemeRegistry supportedAuthSchemes;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private BasicHttpProcessor mutableProcessor;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private ImmutableHttpProcessor protocolProcessor;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private HttpRequestRetryHandler retryHandler;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private RedirectStrategy redirectStrategy;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private AuthenticationStrategy targetAuthStrategy;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private AuthenticationStrategy proxyAuthStrategy;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private CookieStore cookieStore;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private CredentialsProvider credsProvider;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private HttpRoutePlanner routePlanner;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private UserTokenHandler userTokenHandler;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private ConnectionBackoffStrategy connectionBackoffStrategy;
/*      */ 
/*      */   
/*      */   @GuardedBy("this")
/*      */   private BackoffManager backoffManager;
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractHttpClient(ClientConnectionManager conman, HttpParams params) {
/*  288 */     this.defaultParams = params;
/*  289 */     this.connManager = conman;
/*      */   }
/*      */ 
/*      */   
/*      */   protected abstract HttpParams createHttpParams();
/*      */ 
/*      */   
/*      */   protected abstract BasicHttpProcessor createHttpProcessor();
/*      */ 
/*      */   
/*      */   protected HttpContext createHttpContext() {
/*  300 */     BasicHttpContext basicHttpContext = new BasicHttpContext();
/*  301 */     basicHttpContext.setAttribute("http.scheme-registry", getConnectionManager().getSchemeRegistry());
/*      */ 
/*      */     
/*  304 */     basicHttpContext.setAttribute("http.authscheme-registry", getAuthSchemes());
/*      */ 
/*      */     
/*  307 */     basicHttpContext.setAttribute("http.cookiespec-registry", getCookieSpecs());
/*      */ 
/*      */     
/*  310 */     basicHttpContext.setAttribute("http.cookie-store", getCookieStore());
/*      */ 
/*      */     
/*  313 */     basicHttpContext.setAttribute("http.auth.credentials-provider", getCredentialsProvider());
/*      */ 
/*      */     
/*  316 */     return (HttpContext)basicHttpContext;
/*      */   }
/*      */   
/*      */   protected ClientConnectionManager createClientConnectionManager() {
/*      */     BasicClientConnectionManager basicClientConnectionManager;
/*  321 */     SchemeRegistry registry = SchemeRegistryFactory.createDefault();
/*      */     
/*  323 */     ClientConnectionManager connManager = null;
/*  324 */     HttpParams params = getParams();
/*      */     
/*  326 */     ClientConnectionManagerFactory factory = null;
/*      */     
/*  328 */     String className = (String)params.getParameter("http.connection-manager.factory-class-name");
/*      */     
/*  330 */     if (className != null) {
/*      */       try {
/*  332 */         Class<?> clazz = Class.forName(className);
/*  333 */         factory = (ClientConnectionManagerFactory)clazz.newInstance();
/*  334 */       } catch (ClassNotFoundException ex) {
/*  335 */         throw new IllegalStateException("Invalid class name: " + className);
/*  336 */       } catch (IllegalAccessException ex) {
/*  337 */         throw new IllegalAccessError(ex.getMessage());
/*  338 */       } catch (InstantiationException ex) {
/*  339 */         throw new InstantiationError(ex.getMessage());
/*      */       } 
/*      */     }
/*  342 */     if (factory != null) {
/*  343 */       connManager = factory.newInstance(params, registry);
/*      */     } else {
/*  345 */       basicClientConnectionManager = new BasicClientConnectionManager(registry);
/*      */     } 
/*      */     
/*  348 */     return (ClientConnectionManager)basicClientConnectionManager;
/*      */   }
/*      */ 
/*      */   
/*      */   protected AuthSchemeRegistry createAuthSchemeRegistry() {
/*  353 */     AuthSchemeRegistry registry = new AuthSchemeRegistry();
/*  354 */     registry.register("Basic", (AuthSchemeFactory)new BasicSchemeFactory());
/*      */ 
/*      */     
/*  357 */     registry.register("Digest", (AuthSchemeFactory)new DigestSchemeFactory());
/*      */ 
/*      */     
/*  360 */     registry.register("NTLM", (AuthSchemeFactory)new NTLMSchemeFactory());
/*      */ 
/*      */     
/*  363 */     registry.register("Negotiate", (AuthSchemeFactory)new SPNegoSchemeFactory());
/*      */ 
/*      */     
/*  366 */     registry.register("Kerberos", (AuthSchemeFactory)new KerberosSchemeFactory());
/*      */ 
/*      */     
/*  369 */     return registry;
/*      */   }
/*      */ 
/*      */   
/*      */   protected CookieSpecRegistry createCookieSpecRegistry() {
/*  374 */     CookieSpecRegistry registry = new CookieSpecRegistry();
/*  375 */     registry.register("default", (CookieSpecFactory)new BestMatchSpecFactory());
/*      */ 
/*      */     
/*  378 */     registry.register("best-match", (CookieSpecFactory)new BestMatchSpecFactory());
/*      */ 
/*      */     
/*  381 */     registry.register("compatibility", (CookieSpecFactory)new BrowserCompatSpecFactory());
/*      */ 
/*      */     
/*  384 */     registry.register("netscape", (CookieSpecFactory)new NetscapeDraftSpecFactory());
/*      */ 
/*      */     
/*  387 */     registry.register("rfc2109", (CookieSpecFactory)new RFC2109SpecFactory());
/*      */ 
/*      */     
/*  390 */     registry.register("rfc2965", (CookieSpecFactory)new RFC2965SpecFactory());
/*      */ 
/*      */     
/*  393 */     registry.register("ignoreCookies", (CookieSpecFactory)new IgnoreSpecFactory());
/*      */ 
/*      */     
/*  396 */     return registry;
/*      */   }
/*      */   
/*      */   protected HttpRequestExecutor createRequestExecutor() {
/*  400 */     return new HttpRequestExecutor();
/*      */   }
/*      */   
/*      */   protected ConnectionReuseStrategy createConnectionReuseStrategy() {
/*  404 */     return (ConnectionReuseStrategy)new DefaultConnectionReuseStrategy();
/*      */   }
/*      */   
/*      */   protected ConnectionKeepAliveStrategy createConnectionKeepAliveStrategy() {
/*  408 */     return new DefaultConnectionKeepAliveStrategy();
/*      */   }
/*      */   
/*      */   protected HttpRequestRetryHandler createHttpRequestRetryHandler() {
/*  412 */     return new DefaultHttpRequestRetryHandler();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected RedirectHandler createRedirectHandler() {
/*  420 */     return new DefaultRedirectHandler();
/*      */   }
/*      */   
/*      */   protected AuthenticationStrategy createTargetAuthenticationStrategy() {
/*  424 */     return new TargetAuthenticationStrategy();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected AuthenticationHandler createTargetAuthenticationHandler() {
/*  432 */     return new DefaultTargetAuthenticationHandler();
/*      */   }
/*      */   
/*      */   protected AuthenticationStrategy createProxyAuthenticationStrategy() {
/*  436 */     return new ProxyAuthenticationStrategy();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected AuthenticationHandler createProxyAuthenticationHandler() {
/*  444 */     return new DefaultProxyAuthenticationHandler();
/*      */   }
/*      */   
/*      */   protected CookieStore createCookieStore() {
/*  448 */     return new BasicCookieStore();
/*      */   }
/*      */   
/*      */   protected CredentialsProvider createCredentialsProvider() {
/*  452 */     return new BasicCredentialsProvider();
/*      */   }
/*      */   
/*      */   protected HttpRoutePlanner createHttpRoutePlanner() {
/*  456 */     return (HttpRoutePlanner)new DefaultHttpRoutePlanner(getConnectionManager().getSchemeRegistry());
/*      */   }
/*      */   
/*      */   protected UserTokenHandler createUserTokenHandler() {
/*  460 */     return new DefaultUserTokenHandler();
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized HttpParams getParams() {
/*  465 */     if (this.defaultParams == null) {
/*  466 */       this.defaultParams = createHttpParams();
/*      */     }
/*  468 */     return this.defaultParams;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setParams(HttpParams params) {
/*  478 */     this.defaultParams = params;
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized ClientConnectionManager getConnectionManager() {
/*  483 */     if (this.connManager == null) {
/*  484 */       this.connManager = createClientConnectionManager();
/*      */     }
/*  486 */     return this.connManager;
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized HttpRequestExecutor getRequestExecutor() {
/*  491 */     if (this.requestExec == null) {
/*  492 */       this.requestExec = createRequestExecutor();
/*      */     }
/*  494 */     return this.requestExec;
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized AuthSchemeRegistry getAuthSchemes() {
/*  499 */     if (this.supportedAuthSchemes == null) {
/*  500 */       this.supportedAuthSchemes = createAuthSchemeRegistry();
/*      */     }
/*  502 */     return this.supportedAuthSchemes;
/*      */   }
/*      */   
/*      */   public synchronized void setAuthSchemes(AuthSchemeRegistry registry) {
/*  506 */     this.supportedAuthSchemes = registry;
/*      */   }
/*      */   
/*      */   public final synchronized ConnectionBackoffStrategy getConnectionBackoffStrategy() {
/*  510 */     return this.connectionBackoffStrategy;
/*      */   }
/*      */   
/*      */   public synchronized void setConnectionBackoffStrategy(ConnectionBackoffStrategy strategy) {
/*  514 */     this.connectionBackoffStrategy = strategy;
/*      */   }
/*      */   
/*      */   public final synchronized CookieSpecRegistry getCookieSpecs() {
/*  518 */     if (this.supportedCookieSpecs == null) {
/*  519 */       this.supportedCookieSpecs = createCookieSpecRegistry();
/*      */     }
/*  521 */     return this.supportedCookieSpecs;
/*      */   }
/*      */   
/*      */   public final synchronized BackoffManager getBackoffManager() {
/*  525 */     return this.backoffManager;
/*      */   }
/*      */   
/*      */   public synchronized void setBackoffManager(BackoffManager manager) {
/*  529 */     this.backoffManager = manager;
/*      */   }
/*      */   
/*      */   public synchronized void setCookieSpecs(CookieSpecRegistry registry) {
/*  533 */     this.supportedCookieSpecs = registry;
/*      */   }
/*      */   
/*      */   public final synchronized ConnectionReuseStrategy getConnectionReuseStrategy() {
/*  537 */     if (this.reuseStrategy == null) {
/*  538 */       this.reuseStrategy = createConnectionReuseStrategy();
/*      */     }
/*  540 */     return this.reuseStrategy;
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setReuseStrategy(ConnectionReuseStrategy strategy) {
/*  545 */     this.reuseStrategy = strategy;
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy() {
/*  550 */     if (this.keepAliveStrategy == null) {
/*  551 */       this.keepAliveStrategy = createConnectionKeepAliveStrategy();
/*      */     }
/*  553 */     return this.keepAliveStrategy;
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setKeepAliveStrategy(ConnectionKeepAliveStrategy strategy) {
/*  558 */     this.keepAliveStrategy = strategy;
/*      */   }
/*      */ 
/*      */   
/*      */   public final synchronized HttpRequestRetryHandler getHttpRequestRetryHandler() {
/*  563 */     if (this.retryHandler == null) {
/*  564 */       this.retryHandler = createHttpRequestRetryHandler();
/*      */     }
/*  566 */     return this.retryHandler;
/*      */   }
/*      */   
/*      */   public synchronized void setHttpRequestRetryHandler(HttpRequestRetryHandler handler) {
/*  570 */     this.retryHandler = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final synchronized RedirectHandler getRedirectHandler() {
/*  578 */     return createRedirectHandler();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public synchronized void setRedirectHandler(RedirectHandler handler) {
/*  586 */     this.redirectStrategy = new DefaultRedirectStrategyAdaptor(handler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized RedirectStrategy getRedirectStrategy() {
/*  593 */     if (this.redirectStrategy == null) {
/*  594 */       this.redirectStrategy = new DefaultRedirectStrategy();
/*      */     }
/*  596 */     return this.redirectStrategy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setRedirectStrategy(RedirectStrategy strategy) {
/*  603 */     this.redirectStrategy = strategy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final synchronized AuthenticationHandler getTargetAuthenticationHandler() {
/*  611 */     return createTargetAuthenticationHandler();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public synchronized void setTargetAuthenticationHandler(AuthenticationHandler handler) {
/*  619 */     this.targetAuthStrategy = new AuthenticationStrategyAdaptor(handler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized AuthenticationStrategy getTargetAuthenticationStrategy() {
/*  626 */     if (this.targetAuthStrategy == null) {
/*  627 */       this.targetAuthStrategy = createTargetAuthenticationStrategy();
/*      */     }
/*  629 */     return this.targetAuthStrategy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setTargetAuthenticationStrategy(AuthenticationStrategy strategy) {
/*  636 */     this.targetAuthStrategy = strategy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final synchronized AuthenticationHandler getProxyAuthenticationHandler() {
/*  644 */     return createProxyAuthenticationHandler();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public synchronized void setProxyAuthenticationHandler(AuthenticationHandler handler) {
/*  652 */     this.proxyAuthStrategy = new AuthenticationStrategyAdaptor(handler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final synchronized AuthenticationStrategy getProxyAuthenticationStrategy() {
/*  659 */     if (this.proxyAuthStrategy == null) {
/*  660 */       this.proxyAuthStrategy = createProxyAuthenticationStrategy();
/*      */     }
/*  662 */     return this.proxyAuthStrategy;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void setProxyAuthenticationStrategy(AuthenticationStrategy strategy) {
/*  669 */     this.proxyAuthStrategy = strategy;
/*      */   }
/*      */   
/*      */   public final synchronized CookieStore getCookieStore() {
/*  673 */     if (this.cookieStore == null) {
/*  674 */       this.cookieStore = createCookieStore();
/*      */     }
/*  676 */     return this.cookieStore;
/*      */   }
/*      */   
/*      */   public synchronized void setCookieStore(CookieStore cookieStore) {
/*  680 */     this.cookieStore = cookieStore;
/*      */   }
/*      */   
/*      */   public final synchronized CredentialsProvider getCredentialsProvider() {
/*  684 */     if (this.credsProvider == null) {
/*  685 */       this.credsProvider = createCredentialsProvider();
/*      */     }
/*  687 */     return this.credsProvider;
/*      */   }
/*      */   
/*      */   public synchronized void setCredentialsProvider(CredentialsProvider credsProvider) {
/*  691 */     this.credsProvider = credsProvider;
/*      */   }
/*      */   
/*      */   public final synchronized HttpRoutePlanner getRoutePlanner() {
/*  695 */     if (this.routePlanner == null) {
/*  696 */       this.routePlanner = createHttpRoutePlanner();
/*      */     }
/*  698 */     return this.routePlanner;
/*      */   }
/*      */   
/*      */   public synchronized void setRoutePlanner(HttpRoutePlanner routePlanner) {
/*  702 */     this.routePlanner = routePlanner;
/*      */   }
/*      */   
/*      */   public final synchronized UserTokenHandler getUserTokenHandler() {
/*  706 */     if (this.userTokenHandler == null) {
/*  707 */       this.userTokenHandler = createUserTokenHandler();
/*      */     }
/*  709 */     return this.userTokenHandler;
/*      */   }
/*      */   
/*      */   public synchronized void setUserTokenHandler(UserTokenHandler handler) {
/*  713 */     this.userTokenHandler = handler;
/*      */   }
/*      */   
/*      */   protected final synchronized BasicHttpProcessor getHttpProcessor() {
/*  717 */     if (this.mutableProcessor == null) {
/*  718 */       this.mutableProcessor = createHttpProcessor();
/*      */     }
/*  720 */     return this.mutableProcessor;
/*      */   }
/*      */   
/*      */   private synchronized HttpProcessor getProtocolProcessor() {
/*  724 */     if (this.protocolProcessor == null) {
/*      */       
/*  726 */       BasicHttpProcessor proc = getHttpProcessor();
/*      */       
/*  728 */       int reqc = proc.getRequestInterceptorCount();
/*  729 */       HttpRequestInterceptor[] reqinterceptors = new HttpRequestInterceptor[reqc];
/*  730 */       for (int i = 0; i < reqc; i++) {
/*  731 */         reqinterceptors[i] = proc.getRequestInterceptor(i);
/*      */       }
/*  733 */       int resc = proc.getResponseInterceptorCount();
/*  734 */       HttpResponseInterceptor[] resinterceptors = new HttpResponseInterceptor[resc];
/*  735 */       for (int j = 0; j < resc; j++) {
/*  736 */         resinterceptors[j] = proc.getResponseInterceptor(j);
/*      */       }
/*  738 */       this.protocolProcessor = new ImmutableHttpProcessor(reqinterceptors, resinterceptors);
/*      */     } 
/*  740 */     return (HttpProcessor)this.protocolProcessor;
/*      */   }
/*      */   
/*      */   public synchronized int getResponseInterceptorCount() {
/*  744 */     return getHttpProcessor().getResponseInterceptorCount();
/*      */   }
/*      */   
/*      */   public synchronized HttpResponseInterceptor getResponseInterceptor(int index) {
/*  748 */     return getHttpProcessor().getResponseInterceptor(index);
/*      */   }
/*      */   
/*      */   public synchronized HttpRequestInterceptor getRequestInterceptor(int index) {
/*  752 */     return getHttpProcessor().getRequestInterceptor(index);
/*      */   }
/*      */   
/*      */   public synchronized int getRequestInterceptorCount() {
/*  756 */     return getHttpProcessor().getRequestInterceptorCount();
/*      */   }
/*      */   
/*      */   public synchronized void addResponseInterceptor(HttpResponseInterceptor itcp) {
/*  760 */     getHttpProcessor().addInterceptor(itcp);
/*  761 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void addResponseInterceptor(HttpResponseInterceptor itcp, int index) {
/*  765 */     getHttpProcessor().addInterceptor(itcp, index);
/*  766 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void clearResponseInterceptors() {
/*  770 */     getHttpProcessor().clearResponseInterceptors();
/*  771 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void removeResponseInterceptorByClass(Class<? extends HttpResponseInterceptor> clazz) {
/*  775 */     getHttpProcessor().removeResponseInterceptorByClass(clazz);
/*  776 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void addRequestInterceptor(HttpRequestInterceptor itcp) {
/*  780 */     getHttpProcessor().addInterceptor(itcp);
/*  781 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void addRequestInterceptor(HttpRequestInterceptor itcp, int index) {
/*  785 */     getHttpProcessor().addInterceptor(itcp, index);
/*  786 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void clearRequestInterceptors() {
/*  790 */     getHttpProcessor().clearRequestInterceptors();
/*  791 */     this.protocolProcessor = null;
/*      */   }
/*      */   
/*      */   public synchronized void removeRequestInterceptorByClass(Class<? extends HttpRequestInterceptor> clazz) {
/*  795 */     getHttpProcessor().removeRequestInterceptorByClass(clazz);
/*  796 */     this.protocolProcessor = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final CloseableHttpResponse doExecute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
/*      */     DefaultedHttpContext defaultedHttpContext;
/*  804 */     Args.notNull(request, "HTTP request");
/*      */ 
/*      */ 
/*      */     
/*  808 */     HttpContext execContext = null;
/*  809 */     RequestDirector director = null;
/*  810 */     HttpRoutePlanner routePlanner = null;
/*  811 */     ConnectionBackoffStrategy connectionBackoffStrategy = null;
/*  812 */     BackoffManager backoffManager = null;
/*      */ 
/*      */ 
/*      */     
/*  816 */     synchronized (this) {
/*      */       
/*  818 */       HttpContext defaultContext = createHttpContext();
/*  819 */       if (context == null) {
/*  820 */         execContext = defaultContext;
/*      */       } else {
/*  822 */         defaultedHttpContext = new DefaultedHttpContext(context, defaultContext);
/*      */       } 
/*  824 */       HttpParams params = determineParams(request);
/*  825 */       RequestConfig config = HttpClientParamConfig.getRequestConfig(params);
/*  826 */       defaultedHttpContext.setAttribute("http.request-config", config);
/*      */ 
/*      */       
/*  829 */       director = createClientRequestDirector(getRequestExecutor(), getConnectionManager(), getConnectionReuseStrategy(), getConnectionKeepAliveStrategy(), getRoutePlanner(), getProtocolProcessor(), getHttpRequestRetryHandler(), getRedirectStrategy(), getTargetAuthenticationStrategy(), getProxyAuthenticationStrategy(), getUserTokenHandler(), params);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  842 */       routePlanner = getRoutePlanner();
/*  843 */       connectionBackoffStrategy = getConnectionBackoffStrategy();
/*  844 */       backoffManager = getBackoffManager();
/*      */     } 
/*      */     
/*      */     try {
/*  848 */       if (connectionBackoffStrategy != null && backoffManager != null) {
/*  849 */         CloseableHttpResponse out; HttpHost targetForRoute = (target != null) ? target : (HttpHost)determineParams(request).getParameter("http.default-host");
/*      */ 
/*      */         
/*  852 */         HttpRoute route = routePlanner.determineRoute(targetForRoute, request, (HttpContext)defaultedHttpContext);
/*      */ 
/*      */         
/*      */         try {
/*  856 */           out = CloseableHttpResponseProxy.newProxy(director.execute(target, request, (HttpContext)defaultedHttpContext));
/*      */         }
/*  858 */         catch (RuntimeException re) {
/*  859 */           if (connectionBackoffStrategy.shouldBackoff(re)) {
/*  860 */             backoffManager.backOff(route);
/*      */           }
/*  862 */           throw re;
/*  863 */         } catch (Exception e) {
/*  864 */           if (connectionBackoffStrategy.shouldBackoff(e)) {
/*  865 */             backoffManager.backOff(route);
/*      */           }
/*  867 */           if (e instanceof HttpException) {
/*  868 */             throw (HttpException)e;
/*      */           }
/*  870 */           if (e instanceof IOException) {
/*  871 */             throw (IOException)e;
/*      */           }
/*  873 */           throw new UndeclaredThrowableException(e);
/*      */         } 
/*  875 */         if (connectionBackoffStrategy.shouldBackoff((HttpResponse)out)) {
/*  876 */           backoffManager.backOff(route);
/*      */         } else {
/*  878 */           backoffManager.probe(route);
/*      */         } 
/*  880 */         return out;
/*      */       } 
/*  882 */       return CloseableHttpResponseProxy.newProxy(director.execute(target, request, (HttpContext)defaultedHttpContext));
/*      */     
/*      */     }
/*  885 */     catch (HttpException httpException) {
/*  886 */       throw new ClientProtocolException(httpException);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   protected RequestDirector createClientRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectHandler redirectHandler, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params) {
/*  907 */     return new DefaultRequestDirector(requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, redirectHandler, targetAuthHandler, proxyAuthHandler, userTokenHandler, params);
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
/*      */   @Deprecated
/*      */   protected RequestDirector createClientRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params) {
/*  939 */     return new DefaultRequestDirector(this.log, requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, redirectStrategy, targetAuthHandler, proxyAuthHandler, userTokenHandler, params);
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
/*      */   protected RequestDirector createClientRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler, HttpParams params) {
/*  972 */     return new DefaultRequestDirector(this.log, requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, redirectStrategy, targetAuthStrategy, proxyAuthStrategy, userTokenHandler, params);
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
/*      */   protected HttpParams determineParams(HttpRequest req) {
/* 1005 */     return (HttpParams)new ClientParamsStack(null, getParams(), req.getParams(), null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() {
/* 1011 */     getConnectionManager().shutdown();
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/client/AbstractHttpClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
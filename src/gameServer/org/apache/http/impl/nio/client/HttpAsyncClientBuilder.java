/*     */ package org.apache.http.impl.nio.client;
/*     */ 
/*     */ import java.net.ProxySelector;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.auth.AuthSchemeProvider;
/*     */ import org.apache.http.client.AuthenticationStrategy;
/*     */ import org.apache.http.client.CookieStore;
/*     */ import org.apache.http.client.CredentialsProvider;
/*     */ import org.apache.http.client.RedirectStrategy;
/*     */ import org.apache.http.client.UserTokenHandler;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.protocol.RequestAddCookies;
/*     */ import org.apache.http.client.protocol.RequestAuthCache;
/*     */ import org.apache.http.client.protocol.RequestClientConnControl;
/*     */ import org.apache.http.client.protocol.RequestDefaultHeaders;
/*     */ import org.apache.http.client.protocol.RequestExpectContinue;
/*     */ import org.apache.http.client.protocol.ResponseProcessCookies;
/*     */ import org.apache.http.config.ConnectionConfig;
/*     */ import org.apache.http.config.Lookup;
/*     */ import org.apache.http.config.Registry;
/*     */ import org.apache.http.config.RegistryBuilder;
/*     */ import org.apache.http.conn.ConnectionKeepAliveStrategy;
/*     */ import org.apache.http.conn.SchemePortResolver;
/*     */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*     */ import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
/*     */ import org.apache.http.conn.ssl.SSLContexts;
/*     */ import org.apache.http.conn.ssl.X509HostnameVerifier;
/*     */ import org.apache.http.cookie.CookieSpecProvider;
/*     */ import org.apache.http.impl.DefaultConnectionReuseStrategy;
/*     */ import org.apache.http.impl.NoConnectionReuseStrategy;
/*     */ import org.apache.http.impl.auth.BasicSchemeFactory;
/*     */ import org.apache.http.impl.auth.DigestSchemeFactory;
/*     */ import org.apache.http.impl.auth.KerberosSchemeFactory;
/*     */ import org.apache.http.impl.auth.NTLMSchemeFactory;
/*     */ import org.apache.http.impl.auth.SPNegoSchemeFactory;
/*     */ import org.apache.http.impl.client.BasicCookieStore;
/*     */ import org.apache.http.impl.client.BasicCredentialsProvider;
/*     */ import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
/*     */ import org.apache.http.impl.client.DefaultRedirectStrategy;
/*     */ import org.apache.http.impl.client.NoopUserTokenHandler;
/*     */ import org.apache.http.impl.client.ProxyAuthenticationStrategy;
/*     */ import org.apache.http.impl.client.TargetAuthenticationStrategy;
/*     */ import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
/*     */ import org.apache.http.impl.conn.DefaultRoutePlanner;
/*     */ import org.apache.http.impl.conn.DefaultSchemePortResolver;
/*     */ import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
/*     */ import org.apache.http.impl.cookie.BestMatchSpecFactory;
/*     */ import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
/*     */ import org.apache.http.impl.cookie.IgnoreSpecFactory;
/*     */ import org.apache.http.impl.cookie.NetscapeDraftSpecFactory;
/*     */ import org.apache.http.impl.cookie.RFC2109SpecFactory;
/*     */ import org.apache.http.impl.cookie.RFC2965SpecFactory;
/*     */ import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
/*     */ import org.apache.http.impl.nio.reactor.IOReactorConfig;
/*     */ import org.apache.http.nio.conn.NHttpClientConnectionManager;
/*     */ import org.apache.http.nio.conn.NoopIOSessionStrategy;
/*     */ import org.apache.http.nio.conn.SchemeIOSessionStrategy;
/*     */ import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
/*     */ import org.apache.http.nio.reactor.ConnectingIOReactor;
/*     */ import org.apache.http.protocol.HttpProcessor;
/*     */ import org.apache.http.protocol.HttpProcessorBuilder;
/*     */ import org.apache.http.protocol.RequestContent;
/*     */ import org.apache.http.protocol.RequestTargetHost;
/*     */ import org.apache.http.protocol.RequestUserAgent;
/*     */ import org.apache.http.util.TextUtils;
/*     */ import org.apache.http.util.VersionInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class HttpAsyncClientBuilder
/*     */ {
/*     */   static final String DEFAULT_USER_AGENT;
/*     */   private NHttpClientConnectionManager connManager;
/*     */   private SchemePortResolver schemePortResolver;
/*     */   private SchemeIOSessionStrategy sslStrategy;
/*     */   private X509HostnameVerifier hostnameVerifier;
/*     */   private SSLContext sslcontext;
/*     */   private ConnectionReuseStrategy reuseStrategy;
/*     */   private ConnectionKeepAliveStrategy keepAliveStrategy;
/*     */   private AuthenticationStrategy targetAuthStrategy;
/*     */   private AuthenticationStrategy proxyAuthStrategy;
/*     */   private UserTokenHandler userTokenHandler;
/*     */   private HttpProcessor httpprocessor;
/*     */   private LinkedList<HttpRequestInterceptor> requestFirst;
/*     */   private LinkedList<HttpRequestInterceptor> requestLast;
/*     */   private LinkedList<HttpResponseInterceptor> responseFirst;
/*     */   private LinkedList<HttpResponseInterceptor> responseLast;
/*     */   private HttpRoutePlanner routePlanner;
/*     */   private RedirectStrategy redirectStrategy;
/*     */   private Lookup<AuthSchemeProvider> authSchemeRegistry;
/*     */   private Lookup<CookieSpecProvider> cookieSpecRegistry;
/*     */   private CookieStore cookieStore;
/*     */   private CredentialsProvider credentialsProvider;
/*     */   private String userAgent;
/*     */   private HttpHost proxy;
/*     */   private Collection<? extends Header> defaultHeaders;
/*     */   private IOReactorConfig defaultIOReactorConfig;
/*     */   private ConnectionConfig defaultConnectionConfig;
/*     */   private RequestConfig defaultRequestConfig;
/*     */   private ThreadFactory threadFactory;
/*     */   private boolean systemProperties;
/*     */   private boolean cookieManagementDisabled;
/*     */   private boolean authCachingDisabled;
/*     */   private boolean connectionStateDisabled;
/*     */   
/*     */   static {
/* 147 */     VersionInfo vi = VersionInfo.loadVersionInfo("org.apache.http.nio.client", HttpAsyncClientBuilder.class.getClassLoader());
/*     */     
/* 149 */     String release = (vi != null) ? vi.getRelease() : "UNAVAILABLE";
/* 150 */     DEFAULT_USER_AGENT = "Apache-HttpAsyncClient/" + release + " (java 1.5)";
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 190 */   private int maxConnTotal = 0;
/* 191 */   private int maxConnPerRoute = 0;
/*     */   
/*     */   public static HttpAsyncClientBuilder create() {
/* 194 */     return new HttpAsyncClientBuilder();
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
/*     */   public final HttpAsyncClientBuilder setConnectionManager(NHttpClientConnectionManager connManager) {
/* 206 */     this.connManager = connManager;
/* 207 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setSchemePortResolver(SchemePortResolver schemePortResolver) {
/* 215 */     this.schemePortResolver = schemePortResolver;
/* 216 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setMaxConnTotal(int maxConnTotal) {
/* 226 */     this.maxConnTotal = maxConnTotal;
/* 227 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setMaxConnPerRoute(int maxConnPerRoute) {
/* 237 */     this.maxConnPerRoute = maxConnPerRoute;
/* 238 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setConnectionReuseStrategy(ConnectionReuseStrategy reuseStrategy) {
/* 246 */     this.reuseStrategy = reuseStrategy;
/* 247 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setKeepAliveStrategy(ConnectionKeepAliveStrategy keepAliveStrategy) {
/* 255 */     this.keepAliveStrategy = keepAliveStrategy;
/* 256 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setUserTokenHandler(UserTokenHandler userTokenHandler) {
/* 266 */     this.userTokenHandler = userTokenHandler;
/* 267 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setTargetAuthenticationStrategy(AuthenticationStrategy targetAuthStrategy) {
/* 276 */     this.targetAuthStrategy = targetAuthStrategy;
/* 277 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setProxyAuthenticationStrategy(AuthenticationStrategy proxyAuthStrategy) {
/* 286 */     this.proxyAuthStrategy = proxyAuthStrategy;
/* 287 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setHttpProcessor(HttpProcessor httpprocessor) {
/* 294 */     this.httpprocessor = httpprocessor;
/* 295 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder addInterceptorFirst(HttpResponseInterceptor itcp) {
/* 305 */     if (itcp == null) {
/* 306 */       return this;
/*     */     }
/* 308 */     if (this.responseFirst == null) {
/* 309 */       this.responseFirst = new LinkedList<HttpResponseInterceptor>();
/*     */     }
/* 311 */     this.responseFirst.addFirst(itcp);
/* 312 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder addInterceptorLast(HttpResponseInterceptor itcp) {
/* 322 */     if (itcp == null) {
/* 323 */       return this;
/*     */     }
/* 325 */     if (this.responseLast == null) {
/* 326 */       this.responseLast = new LinkedList<HttpResponseInterceptor>();
/*     */     }
/* 328 */     this.responseLast.addLast(itcp);
/* 329 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder addInterceptorFirst(HttpRequestInterceptor itcp) {
/* 339 */     if (itcp == null) {
/* 340 */       return this;
/*     */     }
/* 342 */     if (this.requestFirst == null) {
/* 343 */       this.requestFirst = new LinkedList<HttpRequestInterceptor>();
/*     */     }
/* 345 */     this.requestFirst.addFirst(itcp);
/* 346 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder addInterceptorLast(HttpRequestInterceptor itcp) {
/* 356 */     if (itcp == null) {
/* 357 */       return this;
/*     */     }
/* 359 */     if (this.requestLast == null) {
/* 360 */       this.requestLast = new LinkedList<HttpRequestInterceptor>();
/*     */     }
/* 362 */     this.requestLast.addLast(itcp);
/* 363 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setRoutePlanner(HttpRoutePlanner routePlanner) {
/* 370 */     this.routePlanner = routePlanner;
/* 371 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setRedirectStrategy(RedirectStrategy redirectStrategy) {
/* 378 */     this.redirectStrategy = redirectStrategy;
/* 379 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setDefaultCookieStore(CookieStore cookieStore) {
/* 387 */     this.cookieStore = cookieStore;
/* 388 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setDefaultCredentialsProvider(CredentialsProvider credentialsProvider) {
/* 398 */     this.credentialsProvider = credentialsProvider;
/* 399 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setDefaultAuthSchemeRegistry(Lookup<AuthSchemeProvider> authSchemeRegistry) {
/* 410 */     this.authSchemeRegistry = authSchemeRegistry;
/* 411 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setDefaultCookieSpecRegistry(Lookup<CookieSpecProvider> cookieSpecRegistry) {
/* 421 */     this.cookieSpecRegistry = cookieSpecRegistry;
/* 422 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setUserAgent(String userAgent) {
/* 432 */     this.userAgent = userAgent;
/* 433 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setProxy(HttpHost proxy) {
/* 443 */     this.proxy = proxy;
/* 444 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setSSLStrategy(SchemeIOSessionStrategy strategy) {
/* 454 */     this.sslStrategy = strategy;
/* 455 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setSSLContext(SSLContext sslcontext) {
/* 466 */     this.sslcontext = sslcontext;
/* 467 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setHostnameVerifier(X509HostnameVerifier hostnameVerifier) {
/* 478 */     this.hostnameVerifier = hostnameVerifier;
/* 479 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setDefaultHeaders(Collection<? extends Header> defaultHeaders) {
/* 489 */     this.defaultHeaders = defaultHeaders;
/* 490 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setDefaultIOReactorConfig(IOReactorConfig config) {
/* 500 */     this.defaultIOReactorConfig = config;
/* 501 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setDefaultConnectionConfig(ConnectionConfig config) {
/* 511 */     this.defaultConnectionConfig = config;
/* 512 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setDefaultRequestConfig(RequestConfig config) {
/* 521 */     this.defaultRequestConfig = config;
/* 522 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder setThreadFactory(ThreadFactory threadFactory) {
/* 529 */     this.threadFactory = threadFactory;
/* 530 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder disableConnectionState() {
/* 537 */     this.connectionStateDisabled = true;
/* 538 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder disableCookieManagement() {
/* 548 */     this.cookieManagementDisabled = true;
/* 549 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder disableAuthCaching() {
/* 559 */     this.authCachingDisabled = true;
/* 560 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpAsyncClientBuilder useSystemProperties() {
/* 568 */     this.systemProperties = true;
/* 569 */     return this;
/*     */   }
/*     */   
/*     */   private static String[] split(String s) {
/* 573 */     if (TextUtils.isBlank(s)) {
/* 574 */       return null;
/*     */     }
/* 576 */     return s.split(" *, *"); } public CloseableHttpAsyncClient build() { PoolingNHttpClientConnectionManager poolingNHttpClientConnectionManager; DefaultConnectionReuseStrategy defaultConnectionReuseStrategy; DefaultConnectionKeepAliveStrategy defaultConnectionKeepAliveStrategy; TargetAuthenticationStrategy targetAuthenticationStrategy; ProxyAuthenticationStrategy proxyAuthenticationStrategy; NoopUserTokenHandler noopUserTokenHandler; DefaultSchemePortResolver defaultSchemePortResolver; DefaultRoutePlanner defaultRoutePlanner; Registry registry1, registry2;
/*     */     BasicCookieStore basicCookieStore;
/*     */     BasicCredentialsProvider basicCredentialsProvider;
/*     */     DefaultRedirectStrategy defaultRedirectStrategy;
/* 580 */     NHttpClientConnectionManager connManager = this.connManager;
/* 581 */     if (connManager == null) {
/* 582 */       SSLIOSessionStrategy sSLIOSessionStrategy; SchemeIOSessionStrategy sslStrategy = this.sslStrategy;
/* 583 */       if (sslStrategy == null) {
/* 584 */         SSLContext sslcontext = this.sslcontext;
/* 585 */         if (sslcontext == null) {
/* 586 */           if (this.systemProperties) {
/* 587 */             sslcontext = SSLContexts.createDefault();
/*     */           } else {
/* 589 */             sslcontext = SSLContexts.createSystemDefault();
/*     */           } 
/*     */         }
/* 592 */         String[] supportedProtocols = this.systemProperties ? split(System.getProperty("https.protocols")) : null;
/*     */         
/* 594 */         String[] supportedCipherSuites = this.systemProperties ? split(System.getProperty("https.cipherSuites")) : null;
/*     */         
/* 596 */         X509HostnameVerifier hostnameVerifier = this.hostnameVerifier;
/* 597 */         if (hostnameVerifier == null) {
/* 598 */           hostnameVerifier = SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
/*     */         }
/* 600 */         sSLIOSessionStrategy = new SSLIOSessionStrategy(sslcontext, supportedProtocols, supportedCipherSuites, hostnameVerifier);
/*     */       } 
/*     */       
/* 603 */       ConnectingIOReactor ioreactor = IOReactorUtils.create((this.defaultIOReactorConfig != null) ? this.defaultIOReactorConfig : IOReactorConfig.DEFAULT);
/*     */       
/* 605 */       PoolingNHttpClientConnectionManager poolingmgr = new PoolingNHttpClientConnectionManager(ioreactor, RegistryBuilder.create().register("http", NoopIOSessionStrategy.INSTANCE).register("https", sSLIOSessionStrategy).build());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 611 */       if (this.defaultConnectionConfig != null) {
/* 612 */         poolingmgr.setDefaultConnectionConfig(this.defaultConnectionConfig);
/*     */       }
/* 614 */       if (this.systemProperties) {
/* 615 */         String s = System.getProperty("http.keepAlive", "true");
/* 616 */         if ("true".equalsIgnoreCase(s)) {
/* 617 */           s = System.getProperty("http.maxConnections", "5");
/* 618 */           int max = Integer.parseInt(s);
/* 619 */           poolingmgr.setDefaultMaxPerRoute(max);
/* 620 */           poolingmgr.setMaxTotal(2 * max);
/*     */         } 
/*     */       } else {
/* 623 */         if (this.maxConnTotal > 0) {
/* 624 */           poolingmgr.setMaxTotal(this.maxConnTotal);
/*     */         }
/* 626 */         if (this.maxConnPerRoute > 0) {
/* 627 */           poolingmgr.setDefaultMaxPerRoute(this.maxConnPerRoute);
/*     */         }
/*     */       } 
/* 630 */       poolingNHttpClientConnectionManager = poolingmgr;
/*     */     } 
/* 632 */     ConnectionReuseStrategy reuseStrategy = this.reuseStrategy;
/* 633 */     if (reuseStrategy == null) {
/* 634 */       if (this.systemProperties) {
/* 635 */         String s = System.getProperty("http.keepAlive", "true");
/* 636 */         if ("true".equalsIgnoreCase(s)) {
/* 637 */           defaultConnectionReuseStrategy = DefaultConnectionReuseStrategy.INSTANCE;
/*     */         } else {
/* 639 */           NoConnectionReuseStrategy noConnectionReuseStrategy = NoConnectionReuseStrategy.INSTANCE;
/*     */         } 
/*     */       } else {
/* 642 */         defaultConnectionReuseStrategy = DefaultConnectionReuseStrategy.INSTANCE;
/*     */       } 
/*     */     }
/* 645 */     ConnectionKeepAliveStrategy keepAliveStrategy = this.keepAliveStrategy;
/* 646 */     if (keepAliveStrategy == null) {
/* 647 */       defaultConnectionKeepAliveStrategy = DefaultConnectionKeepAliveStrategy.INSTANCE;
/*     */     }
/* 649 */     AuthenticationStrategy targetAuthStrategy = this.targetAuthStrategy;
/* 650 */     if (targetAuthStrategy == null) {
/* 651 */       targetAuthenticationStrategy = TargetAuthenticationStrategy.INSTANCE;
/*     */     }
/* 653 */     AuthenticationStrategy proxyAuthStrategy = this.proxyAuthStrategy;
/* 654 */     if (proxyAuthStrategy == null) {
/* 655 */       proxyAuthenticationStrategy = ProxyAuthenticationStrategy.INSTANCE;
/*     */     }
/* 657 */     UserTokenHandler userTokenHandler = this.userTokenHandler;
/* 658 */     if (userTokenHandler == null) {
/* 659 */       if (!this.connectionStateDisabled) {
/* 660 */         userTokenHandler = DefaultAsyncUserTokenHandler.INSTANCE;
/*     */       } else {
/* 662 */         noopUserTokenHandler = NoopUserTokenHandler.INSTANCE;
/*     */       } 
/*     */     }
/* 665 */     SchemePortResolver schemePortResolver = this.schemePortResolver;
/* 666 */     if (schemePortResolver == null) {
/* 667 */       defaultSchemePortResolver = DefaultSchemePortResolver.INSTANCE;
/*     */     }
/*     */     
/* 670 */     HttpProcessor httpprocessor = this.httpprocessor;
/* 671 */     if (httpprocessor == null) {
/*     */       
/* 673 */       String userAgent = this.userAgent;
/* 674 */       if (userAgent == null) {
/* 675 */         if (this.systemProperties) {
/* 676 */           userAgent = System.getProperty("http.agent");
/*     */         }
/* 678 */         if (userAgent == null) {
/* 679 */           userAgent = DEFAULT_USER_AGENT;
/*     */         }
/*     */       } 
/*     */       
/* 683 */       HttpProcessorBuilder b = HttpProcessorBuilder.create();
/* 684 */       if (this.requestFirst != null) {
/* 685 */         for (HttpRequestInterceptor i : this.requestFirst) {
/* 686 */           b.addFirst(i);
/*     */         }
/*     */       }
/* 689 */       if (this.responseFirst != null) {
/* 690 */         for (HttpResponseInterceptor i : this.responseFirst) {
/* 691 */           b.addFirst(i);
/*     */         }
/*     */       }
/* 694 */       b.addAll(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestDefaultHeaders(this.defaultHeaders), (HttpRequestInterceptor)new RequestContent(), (HttpRequestInterceptor)new RequestTargetHost(), (HttpRequestInterceptor)new RequestClientConnControl(), (HttpRequestInterceptor)new RequestUserAgent(userAgent), (HttpRequestInterceptor)new RequestExpectContinue() });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 701 */       if (!this.cookieManagementDisabled) {
/* 702 */         b.add((HttpRequestInterceptor)new RequestAddCookies());
/*     */       }
/* 704 */       if (!this.authCachingDisabled) {
/* 705 */         b.add((HttpRequestInterceptor)new RequestAuthCache());
/*     */       }
/* 707 */       if (!this.cookieManagementDisabled) {
/* 708 */         b.add((HttpResponseInterceptor)new ResponseProcessCookies());
/*     */       }
/* 710 */       if (this.requestLast != null) {
/* 711 */         for (HttpRequestInterceptor i : this.requestLast) {
/* 712 */           b.addLast(i);
/*     */         }
/*     */       }
/* 715 */       if (this.responseLast != null) {
/* 716 */         for (HttpResponseInterceptor i : this.responseLast) {
/* 717 */           b.addLast(i);
/*     */         }
/*     */       }
/* 720 */       httpprocessor = b.build();
/*     */     } 
/*     */     
/* 723 */     HttpRoutePlanner routePlanner = this.routePlanner;
/* 724 */     if (routePlanner == null) {
/* 725 */       if (this.proxy != null) {
/* 726 */         DefaultProxyRoutePlanner defaultProxyRoutePlanner = new DefaultProxyRoutePlanner(this.proxy, (SchemePortResolver)defaultSchemePortResolver);
/* 727 */       } else if (this.systemProperties) {
/* 728 */         SystemDefaultRoutePlanner systemDefaultRoutePlanner = new SystemDefaultRoutePlanner((SchemePortResolver)defaultSchemePortResolver, ProxySelector.getDefault());
/*     */       } else {
/*     */         
/* 731 */         defaultRoutePlanner = new DefaultRoutePlanner((SchemePortResolver)defaultSchemePortResolver);
/*     */       } 
/*     */     }
/* 734 */     Lookup<AuthSchemeProvider> authSchemeRegistry = this.authSchemeRegistry;
/* 735 */     if (authSchemeRegistry == null) {
/* 736 */       registry1 = RegistryBuilder.create().register("Basic", new BasicSchemeFactory()).register("Digest", new DigestSchemeFactory()).register("NTLM", new NTLMSchemeFactory()).register("negotiate", new SPNegoSchemeFactory()).register("Kerberos", new KerberosSchemeFactory()).build();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 744 */     Lookup<CookieSpecProvider> cookieSpecRegistry = this.cookieSpecRegistry;
/* 745 */     if (cookieSpecRegistry == null) {
/* 746 */       registry2 = RegistryBuilder.create().register("best-match", new BestMatchSpecFactory()).register("standard", new RFC2965SpecFactory()).register("compatibility", new BrowserCompatSpecFactory()).register("netscape", new NetscapeDraftSpecFactory()).register("ignoreCookies", new IgnoreSpecFactory()).register("rfc2109", new RFC2109SpecFactory()).register("rfc2965", new RFC2965SpecFactory()).build();
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
/* 757 */     CookieStore defaultCookieStore = this.cookieStore;
/* 758 */     if (defaultCookieStore == null) {
/* 759 */       basicCookieStore = new BasicCookieStore();
/*     */     }
/*     */     
/* 762 */     CredentialsProvider defaultCredentialsProvider = this.credentialsProvider;
/* 763 */     if (defaultCredentialsProvider == null) {
/* 764 */       basicCredentialsProvider = new BasicCredentialsProvider();
/*     */     }
/*     */     
/* 767 */     RedirectStrategy redirectStrategy = this.redirectStrategy;
/* 768 */     if (redirectStrategy == null) {
/* 769 */       defaultRedirectStrategy = DefaultRedirectStrategy.INSTANCE;
/*     */     }
/*     */     
/* 772 */     RequestConfig defaultRequestConfig = this.defaultRequestConfig;
/* 773 */     if (defaultRequestConfig == null) {
/* 774 */       defaultRequestConfig = RequestConfig.DEFAULT;
/*     */     }
/*     */     
/* 777 */     ThreadFactory threadFactory = this.threadFactory;
/* 778 */     if (threadFactory == null) {
/* 779 */       threadFactory = Executors.defaultThreadFactory();
/*     */     }
/*     */     
/* 782 */     MainClientExec exec = new MainClientExec((NHttpClientConnectionManager)poolingNHttpClientConnectionManager, httpprocessor, (HttpRoutePlanner)defaultRoutePlanner, (ConnectionReuseStrategy)defaultConnectionReuseStrategy, (ConnectionKeepAliveStrategy)defaultConnectionKeepAliveStrategy, (RedirectStrategy)defaultRedirectStrategy, (AuthenticationStrategy)targetAuthenticationStrategy, (AuthenticationStrategy)proxyAuthenticationStrategy, (UserTokenHandler)noopUserTokenHandler);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 793 */     return new InternalHttpAsyncClient((NHttpClientConnectionManager)poolingNHttpClientConnectionManager, exec, (Lookup<CookieSpecProvider>)registry2, (Lookup<AuthSchemeProvider>)registry1, (CookieStore)basicCookieStore, (CredentialsProvider)basicCredentialsProvider, defaultRequestConfig, threadFactory); }
/*     */ 
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/HttpAsyncClientBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*      */ package org.apache.http.impl.client;
/*      */ 
/*      */ import java.io.Closeable;
/*      */ import java.io.IOException;
/*      */ import java.net.ProxySelector;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import javax.net.ssl.HostnameVerifier;
/*      */ import javax.net.ssl.SSLContext;
/*      */ import javax.net.ssl.SSLSocketFactory;
/*      */ import org.apache.http.ConnectionReuseStrategy;
/*      */ import org.apache.http.Header;
/*      */ import org.apache.http.HttpHost;
/*      */ import org.apache.http.HttpRequestInterceptor;
/*      */ import org.apache.http.HttpResponseInterceptor;
/*      */ import org.apache.http.annotation.NotThreadSafe;
/*      */ import org.apache.http.auth.AuthSchemeProvider;
/*      */ import org.apache.http.client.AuthenticationStrategy;
/*      */ import org.apache.http.client.BackoffManager;
/*      */ import org.apache.http.client.ConnectionBackoffStrategy;
/*      */ import org.apache.http.client.CookieStore;
/*      */ import org.apache.http.client.CredentialsProvider;
/*      */ import org.apache.http.client.HttpRequestRetryHandler;
/*      */ import org.apache.http.client.RedirectStrategy;
/*      */ import org.apache.http.client.ServiceUnavailableRetryStrategy;
/*      */ import org.apache.http.client.UserTokenHandler;
/*      */ import org.apache.http.client.config.RequestConfig;
/*      */ import org.apache.http.client.entity.InputStreamFactory;
/*      */ import org.apache.http.client.protocol.RequestAcceptEncoding;
/*      */ import org.apache.http.client.protocol.RequestAddCookies;
/*      */ import org.apache.http.client.protocol.RequestAuthCache;
/*      */ import org.apache.http.client.protocol.RequestClientConnControl;
/*      */ import org.apache.http.client.protocol.RequestDefaultHeaders;
/*      */ import org.apache.http.client.protocol.RequestExpectContinue;
/*      */ import org.apache.http.client.protocol.ResponseContentEncoding;
/*      */ import org.apache.http.client.protocol.ResponseProcessCookies;
/*      */ import org.apache.http.config.ConnectionConfig;
/*      */ import org.apache.http.config.Lookup;
/*      */ import org.apache.http.config.Registry;
/*      */ import org.apache.http.config.RegistryBuilder;
/*      */ import org.apache.http.config.SocketConfig;
/*      */ import org.apache.http.conn.ConnectionKeepAliveStrategy;
/*      */ import org.apache.http.conn.HttpClientConnectionManager;
/*      */ import org.apache.http.conn.SchemePortResolver;
/*      */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*      */ import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
/*      */ import org.apache.http.conn.socket.PlainConnectionSocketFactory;
/*      */ import org.apache.http.conn.ssl.DefaultHostnameVerifier;
/*      */ import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
/*      */ import org.apache.http.conn.ssl.X509HostnameVerifier;
/*      */ import org.apache.http.conn.util.PublicSuffixMatcher;
/*      */ import org.apache.http.conn.util.PublicSuffixMatcherLoader;
/*      */ import org.apache.http.cookie.CookieSpecProvider;
/*      */ import org.apache.http.impl.DefaultConnectionReuseStrategy;
/*      */ import org.apache.http.impl.NoConnectionReuseStrategy;
/*      */ import org.apache.http.impl.auth.BasicSchemeFactory;
/*      */ import org.apache.http.impl.auth.DigestSchemeFactory;
/*      */ import org.apache.http.impl.auth.KerberosSchemeFactory;
/*      */ import org.apache.http.impl.auth.NTLMSchemeFactory;
/*      */ import org.apache.http.impl.auth.SPNegoSchemeFactory;
/*      */ import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
/*      */ import org.apache.http.impl.conn.DefaultRoutePlanner;
/*      */ import org.apache.http.impl.conn.DefaultSchemePortResolver;
/*      */ import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
/*      */ import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
/*      */ import org.apache.http.impl.cookie.DefaultCookieSpecProvider;
/*      */ import org.apache.http.impl.cookie.IgnoreSpecProvider;
/*      */ import org.apache.http.impl.cookie.NetscapeDraftSpecProvider;
/*      */ import org.apache.http.impl.cookie.RFC6265CookieSpecProvider;
/*      */ import org.apache.http.impl.execchain.BackoffStrategyExec;
/*      */ import org.apache.http.impl.execchain.ClientExecChain;
/*      */ import org.apache.http.impl.execchain.MainClientExec;
/*      */ import org.apache.http.impl.execchain.ProtocolExec;
/*      */ import org.apache.http.impl.execchain.RedirectExec;
/*      */ import org.apache.http.impl.execchain.RetryExec;
/*      */ import org.apache.http.impl.execchain.ServiceUnavailableRetryExec;
/*      */ import org.apache.http.protocol.HttpProcessor;
/*      */ import org.apache.http.protocol.HttpProcessorBuilder;
/*      */ import org.apache.http.protocol.HttpRequestExecutor;
/*      */ import org.apache.http.protocol.ImmutableHttpProcessor;
/*      */ import org.apache.http.protocol.RequestContent;
/*      */ import org.apache.http.protocol.RequestTargetHost;
/*      */ import org.apache.http.protocol.RequestUserAgent;
/*      */ import org.apache.http.ssl.SSLContexts;
/*      */ import org.apache.http.util.TextUtils;
/*      */ import org.apache.http.util.VersionInfo;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @NotThreadSafe
/*      */ public class HttpClientBuilder
/*      */ {
/*      */   private HttpRequestExecutor requestExec;
/*      */   private HostnameVerifier hostnameVerifier;
/*      */   private LayeredConnectionSocketFactory sslSocketFactory;
/*      */   private SSLContext sslcontext;
/*      */   private HttpClientConnectionManager connManager;
/*      */   private boolean connManagerShared;
/*      */   private SchemePortResolver schemePortResolver;
/*      */   private ConnectionReuseStrategy reuseStrategy;
/*      */   private ConnectionKeepAliveStrategy keepAliveStrategy;
/*      */   private AuthenticationStrategy targetAuthStrategy;
/*      */   private AuthenticationStrategy proxyAuthStrategy;
/*      */   private UserTokenHandler userTokenHandler;
/*      */   private HttpProcessor httpprocessor;
/*      */   private LinkedList<HttpRequestInterceptor> requestFirst;
/*      */   private LinkedList<HttpRequestInterceptor> requestLast;
/*      */   private LinkedList<HttpResponseInterceptor> responseFirst;
/*      */   private LinkedList<HttpResponseInterceptor> responseLast;
/*      */   private HttpRequestRetryHandler retryHandler;
/*      */   private HttpRoutePlanner routePlanner;
/*      */   private RedirectStrategy redirectStrategy;
/*      */   private ConnectionBackoffStrategy connectionBackoffStrategy;
/*      */   private BackoffManager backoffManager;
/*      */   private ServiceUnavailableRetryStrategy serviceUnavailStrategy;
/*      */   private Lookup<AuthSchemeProvider> authSchemeRegistry;
/*      */   private Lookup<CookieSpecProvider> cookieSpecRegistry;
/*      */   private Map<String, InputStreamFactory> contentDecoderMap;
/*      */   private CookieStore cookieStore;
/*      */   private CredentialsProvider credentialsProvider;
/*      */   private String userAgent;
/*      */   private HttpHost proxy;
/*      */   private Collection<? extends Header> defaultHeaders;
/*      */   private SocketConfig defaultSocketConfig;
/*      */   private ConnectionConfig defaultConnectionConfig;
/*      */   private RequestConfig defaultRequestConfig;
/*      */   private boolean evictExpiredConnections;
/*      */   private boolean evictIdleConnections;
/*      */   private long maxIdleTime;
/*      */   private TimeUnit maxIdleTimeUnit;
/*      */   private boolean systemProperties;
/*      */   private boolean redirectHandlingDisabled;
/*      */   private boolean automaticRetriesDisabled;
/*      */   private boolean contentCompressionDisabled;
/*      */   private boolean cookieManagementDisabled;
/*      */   private boolean authCachingDisabled;
/*      */   private boolean connectionStateDisabled;
/*  213 */   private int maxConnTotal = 0;
/*  214 */   private int maxConnPerRoute = 0;
/*      */   
/*  216 */   private long connTimeToLive = -1L;
/*  217 */   private TimeUnit connTimeToLiveTimeUnit = TimeUnit.MILLISECONDS;
/*      */   
/*      */   private List<Closeable> closeables;
/*      */   
/*      */   private PublicSuffixMatcher publicSuffixMatcher;
/*      */   
/*      */   public static HttpClientBuilder create() {
/*  224 */     return new HttpClientBuilder();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setRequestExecutor(HttpRequestExecutor requestExec) {
/*  235 */     this.requestExec = requestExec;
/*  236 */     return this;
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
/*      */   @Deprecated
/*      */   public final HttpClientBuilder setHostnameVerifier(X509HostnameVerifier hostnameVerifier) {
/*  251 */     this.hostnameVerifier = (HostnameVerifier)hostnameVerifier;
/*  252 */     return this;
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
/*      */   public final HttpClientBuilder setSSLHostnameVerifier(HostnameVerifier hostnameVerifier) {
/*  266 */     this.hostnameVerifier = hostnameVerifier;
/*  267 */     return this;
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
/*      */   public final HttpClientBuilder setPublicSuffixMatcher(PublicSuffixMatcher publicSuffixMatcher) {
/*  280 */     this.publicSuffixMatcher = publicSuffixMatcher;
/*  281 */     return this;
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
/*      */   public final HttpClientBuilder setSslcontext(SSLContext sslcontext) {
/*  293 */     this.sslcontext = sslcontext;
/*  294 */     return this;
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
/*      */   public final HttpClientBuilder setSSLSocketFactory(LayeredConnectionSocketFactory sslSocketFactory) {
/*  306 */     this.sslSocketFactory = sslSocketFactory;
/*  307 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setMaxConnTotal(int maxConnTotal) {
/*  318 */     this.maxConnTotal = maxConnTotal;
/*  319 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setMaxConnPerRoute(int maxConnPerRoute) {
/*  330 */     this.maxConnPerRoute = maxConnPerRoute;
/*  331 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setDefaultSocketConfig(SocketConfig config) {
/*  342 */     this.defaultSocketConfig = config;
/*  343 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setDefaultConnectionConfig(ConnectionConfig config) {
/*  354 */     this.defaultConnectionConfig = config;
/*  355 */     return this;
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
/*      */   public final HttpClientBuilder setConnectionTimeToLive(long connTimeToLive, TimeUnit connTimeToLiveTimeUnit) {
/*  368 */     this.connTimeToLive = connTimeToLive;
/*  369 */     this.connTimeToLiveTimeUnit = connTimeToLiveTimeUnit;
/*  370 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setConnectionManager(HttpClientConnectionManager connManager) {
/*  378 */     this.connManager = connManager;
/*  379 */     return this;
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
/*      */   public final HttpClientBuilder setConnectionManagerShared(boolean shared) {
/*  398 */     this.connManagerShared = shared;
/*  399 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setConnectionReuseStrategy(ConnectionReuseStrategy reuseStrategy) {
/*  407 */     this.reuseStrategy = reuseStrategy;
/*  408 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setKeepAliveStrategy(ConnectionKeepAliveStrategy keepAliveStrategy) {
/*  416 */     this.keepAliveStrategy = keepAliveStrategy;
/*  417 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setTargetAuthenticationStrategy(AuthenticationStrategy targetAuthStrategy) {
/*  426 */     this.targetAuthStrategy = targetAuthStrategy;
/*  427 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setProxyAuthenticationStrategy(AuthenticationStrategy proxyAuthStrategy) {
/*  436 */     this.proxyAuthStrategy = proxyAuthStrategy;
/*  437 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setUserTokenHandler(UserTokenHandler userTokenHandler) {
/*  448 */     this.userTokenHandler = userTokenHandler;
/*  449 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder disableConnectionState() {
/*  456 */     this.connectionStateDisabled = true;
/*  457 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setSchemePortResolver(SchemePortResolver schemePortResolver) {
/*  465 */     this.schemePortResolver = schemePortResolver;
/*  466 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setUserAgent(String userAgent) {
/*  477 */     this.userAgent = userAgent;
/*  478 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setDefaultHeaders(Collection<? extends Header> defaultHeaders) {
/*  489 */     this.defaultHeaders = defaultHeaders;
/*  490 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder addInterceptorFirst(HttpResponseInterceptor itcp) {
/*  501 */     if (itcp == null) {
/*  502 */       return this;
/*      */     }
/*  504 */     if (this.responseFirst == null) {
/*  505 */       this.responseFirst = new LinkedList<HttpResponseInterceptor>();
/*      */     }
/*  507 */     this.responseFirst.addFirst(itcp);
/*  508 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder addInterceptorLast(HttpResponseInterceptor itcp) {
/*  519 */     if (itcp == null) {
/*  520 */       return this;
/*      */     }
/*  522 */     if (this.responseLast == null) {
/*  523 */       this.responseLast = new LinkedList<HttpResponseInterceptor>();
/*      */     }
/*  525 */     this.responseLast.addLast(itcp);
/*  526 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder addInterceptorFirst(HttpRequestInterceptor itcp) {
/*  536 */     if (itcp == null) {
/*  537 */       return this;
/*      */     }
/*  539 */     if (this.requestFirst == null) {
/*  540 */       this.requestFirst = new LinkedList<HttpRequestInterceptor>();
/*      */     }
/*  542 */     this.requestFirst.addFirst(itcp);
/*  543 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder addInterceptorLast(HttpRequestInterceptor itcp) {
/*  553 */     if (itcp == null) {
/*  554 */       return this;
/*      */     }
/*  556 */     if (this.requestLast == null) {
/*  557 */       this.requestLast = new LinkedList<HttpRequestInterceptor>();
/*      */     }
/*  559 */     this.requestLast.addLast(itcp);
/*  560 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder disableCookieManagement() {
/*  570 */     this.cookieManagementDisabled = true;
/*  571 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder disableContentCompression() {
/*  581 */     this.contentCompressionDisabled = true;
/*  582 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder disableAuthCaching() {
/*  592 */     this.authCachingDisabled = true;
/*  593 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setHttpProcessor(HttpProcessor httpprocessor) {
/*  600 */     this.httpprocessor = httpprocessor;
/*  601 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setRetryHandler(HttpRequestRetryHandler retryHandler) {
/*  611 */     this.retryHandler = retryHandler;
/*  612 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder disableAutomaticRetries() {
/*  619 */     this.automaticRetriesDisabled = true;
/*  620 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setProxy(HttpHost proxy) {
/*  630 */     this.proxy = proxy;
/*  631 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setRoutePlanner(HttpRoutePlanner routePlanner) {
/*  638 */     this.routePlanner = routePlanner;
/*  639 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setRedirectStrategy(RedirectStrategy redirectStrategy) {
/*  650 */     this.redirectStrategy = redirectStrategy;
/*  651 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder disableRedirectHandling() {
/*  658 */     this.redirectHandlingDisabled = true;
/*  659 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setConnectionBackoffStrategy(ConnectionBackoffStrategy connectionBackoffStrategy) {
/*  667 */     this.connectionBackoffStrategy = connectionBackoffStrategy;
/*  668 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setBackoffManager(BackoffManager backoffManager) {
/*  675 */     this.backoffManager = backoffManager;
/*  676 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setServiceUnavailableRetryStrategy(ServiceUnavailableRetryStrategy serviceUnavailStrategy) {
/*  684 */     this.serviceUnavailStrategy = serviceUnavailStrategy;
/*  685 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setDefaultCookieStore(CookieStore cookieStore) {
/*  693 */     this.cookieStore = cookieStore;
/*  694 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setDefaultCredentialsProvider(CredentialsProvider credentialsProvider) {
/*  704 */     this.credentialsProvider = credentialsProvider;
/*  705 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setDefaultAuthSchemeRegistry(Lookup<AuthSchemeProvider> authSchemeRegistry) {
/*  715 */     this.authSchemeRegistry = authSchemeRegistry;
/*  716 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setDefaultCookieSpecRegistry(Lookup<CookieSpecProvider> cookieSpecRegistry) {
/*  726 */     this.cookieSpecRegistry = cookieSpecRegistry;
/*  727 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setContentDecoderRegistry(Map<String, InputStreamFactory> contentDecoderMap) {
/*  737 */     this.contentDecoderMap = contentDecoderMap;
/*  738 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder setDefaultRequestConfig(RequestConfig config) {
/*  747 */     this.defaultRequestConfig = config;
/*  748 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpClientBuilder useSystemProperties() {
/*  756 */     this.systemProperties = true;
/*  757 */     return this;
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
/*      */   public final HttpClientBuilder evictExpiredConnections() {
/*  779 */     this.evictExpiredConnections = true;
/*  780 */     return this;
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
/*      */   public final HttpClientBuilder evictIdleConnections(Long maxIdleTime, TimeUnit maxIdleTimeUnit) {
/*  807 */     this.evictIdleConnections = true;
/*  808 */     this.maxIdleTime = maxIdleTime.longValue();
/*  809 */     this.maxIdleTimeUnit = maxIdleTimeUnit;
/*  810 */     return this;
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
/*      */   protected ClientExecChain createMainExec(HttpRequestExecutor requestExec, HttpClientConnectionManager connManager, ConnectionReuseStrategy reuseStrategy, ConnectionKeepAliveStrategy keepAliveStrategy, HttpProcessor proxyHttpProcessor, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler) {
/*  834 */     return (ClientExecChain)new MainClientExec(requestExec, connManager, reuseStrategy, keepAliveStrategy, proxyHttpProcessor, targetAuthStrategy, proxyAuthStrategy, userTokenHandler);
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
/*      */   protected ClientExecChain decorateMainExec(ClientExecChain mainExec) {
/*  849 */     return mainExec;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ClientExecChain decorateProtocolExec(ClientExecChain protocolExec) {
/*  856 */     return protocolExec;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void addCloseable(Closeable closeable) {
/*  863 */     if (closeable == null) {
/*      */       return;
/*      */     }
/*  866 */     if (this.closeables == null) {
/*  867 */       this.closeables = new ArrayList<Closeable>();
/*      */     }
/*  869 */     this.closeables.add(closeable);
/*      */   }
/*      */   
/*      */   private static String[] split(String s) {
/*  873 */     if (TextUtils.isBlank(s)) {
/*  874 */       return null;
/*      */     }
/*  876 */     return s.split(" *, *"); } public CloseableHttpClient build() { PoolingHttpClientConnectionManager poolingHttpClientConnectionManager; DefaultConnectionReuseStrategy defaultConnectionReuseStrategy; RetryExec retryExec;
/*      */     RedirectExec redirectExec;
/*      */     ServiceUnavailableRetryExec serviceUnavailableRetryExec;
/*      */     BackoffStrategyExec backoffStrategyExec;
/*      */     DefaultRoutePlanner defaultRoutePlanner;
/*      */     Registry registry1, registry2;
/*  882 */     PublicSuffixMatcher publicSuffixMatcherCopy = this.publicSuffixMatcher;
/*  883 */     if (publicSuffixMatcherCopy == null) {
/*  884 */       publicSuffixMatcherCopy = PublicSuffixMatcherLoader.getDefault();
/*      */     }
/*      */     
/*  887 */     HttpRequestExecutor requestExecCopy = this.requestExec;
/*  888 */     if (requestExecCopy == null) {
/*  889 */       requestExecCopy = new HttpRequestExecutor();
/*      */     }
/*  891 */     HttpClientConnectionManager connManagerCopy = this.connManager;
/*  892 */     if (connManagerCopy == null) {
/*  893 */       SSLConnectionSocketFactory sSLConnectionSocketFactory; LayeredConnectionSocketFactory sslSocketFactoryCopy = this.sslSocketFactory;
/*  894 */       if (sslSocketFactoryCopy == null) {
/*  895 */         DefaultHostnameVerifier defaultHostnameVerifier; String[] supportedProtocols = this.systemProperties ? split(System.getProperty("https.protocols")) : null;
/*      */         
/*  897 */         String[] supportedCipherSuites = this.systemProperties ? split(System.getProperty("https.cipherSuites")) : null;
/*      */         
/*  899 */         HostnameVerifier hostnameVerifierCopy = this.hostnameVerifier;
/*  900 */         if (hostnameVerifierCopy == null) {
/*  901 */           defaultHostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcherCopy);
/*      */         }
/*  903 */         if (this.sslcontext != null) {
/*  904 */           sSLConnectionSocketFactory = new SSLConnectionSocketFactory(this.sslcontext, supportedProtocols, supportedCipherSuites, (HostnameVerifier)defaultHostnameVerifier);
/*      */         
/*      */         }
/*  907 */         else if (this.systemProperties) {
/*  908 */           sSLConnectionSocketFactory = new SSLConnectionSocketFactory((SSLSocketFactory)SSLSocketFactory.getDefault(), supportedProtocols, supportedCipherSuites, (HostnameVerifier)defaultHostnameVerifier);
/*      */         }
/*      */         else {
/*      */           
/*  912 */           sSLConnectionSocketFactory = new SSLConnectionSocketFactory(SSLContexts.createDefault(), (HostnameVerifier)defaultHostnameVerifier);
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  919 */       PoolingHttpClientConnectionManager poolingmgr = new PoolingHttpClientConnectionManager(RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sSLConnectionSocketFactory).build(), null, null, null, this.connTimeToLive, (this.connTimeToLiveTimeUnit != null) ? this.connTimeToLiveTimeUnit : TimeUnit.MILLISECONDS);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  929 */       if (this.defaultSocketConfig != null) {
/*  930 */         poolingmgr.setDefaultSocketConfig(this.defaultSocketConfig);
/*      */       }
/*  932 */       if (this.defaultConnectionConfig != null) {
/*  933 */         poolingmgr.setDefaultConnectionConfig(this.defaultConnectionConfig);
/*      */       }
/*  935 */       if (this.systemProperties) {
/*  936 */         String s = System.getProperty("http.keepAlive", "true");
/*  937 */         if ("true".equalsIgnoreCase(s)) {
/*  938 */           s = System.getProperty("http.maxConnections", "5");
/*  939 */           int max = Integer.parseInt(s);
/*  940 */           poolingmgr.setDefaultMaxPerRoute(max);
/*  941 */           poolingmgr.setMaxTotal(2 * max);
/*      */         } 
/*      */       } 
/*  944 */       if (this.maxConnTotal > 0) {
/*  945 */         poolingmgr.setMaxTotal(this.maxConnTotal);
/*      */       }
/*  947 */       if (this.maxConnPerRoute > 0) {
/*  948 */         poolingmgr.setDefaultMaxPerRoute(this.maxConnPerRoute);
/*      */       }
/*  950 */       poolingHttpClientConnectionManager = poolingmgr;
/*      */     } 
/*  952 */     ConnectionReuseStrategy reuseStrategyCopy = this.reuseStrategy;
/*  953 */     if (reuseStrategyCopy == null) {
/*  954 */       if (this.systemProperties) {
/*  955 */         String s = System.getProperty("http.keepAlive", "true");
/*  956 */         if ("true".equalsIgnoreCase(s)) {
/*  957 */           defaultConnectionReuseStrategy = DefaultConnectionReuseStrategy.INSTANCE;
/*      */         } else {
/*  959 */           NoConnectionReuseStrategy noConnectionReuseStrategy = NoConnectionReuseStrategy.INSTANCE;
/*      */         } 
/*      */       } else {
/*  962 */         defaultConnectionReuseStrategy = DefaultConnectionReuseStrategy.INSTANCE;
/*      */       } 
/*      */     }
/*  965 */     ConnectionKeepAliveStrategy keepAliveStrategyCopy = this.keepAliveStrategy;
/*  966 */     if (keepAliveStrategyCopy == null) {
/*  967 */       keepAliveStrategyCopy = DefaultConnectionKeepAliveStrategy.INSTANCE;
/*      */     }
/*  969 */     AuthenticationStrategy targetAuthStrategyCopy = this.targetAuthStrategy;
/*  970 */     if (targetAuthStrategyCopy == null) {
/*  971 */       targetAuthStrategyCopy = TargetAuthenticationStrategy.INSTANCE;
/*      */     }
/*  973 */     AuthenticationStrategy proxyAuthStrategyCopy = this.proxyAuthStrategy;
/*  974 */     if (proxyAuthStrategyCopy == null) {
/*  975 */       proxyAuthStrategyCopy = ProxyAuthenticationStrategy.INSTANCE;
/*      */     }
/*  977 */     UserTokenHandler userTokenHandlerCopy = this.userTokenHandler;
/*  978 */     if (userTokenHandlerCopy == null) {
/*  979 */       if (!this.connectionStateDisabled) {
/*  980 */         userTokenHandlerCopy = DefaultUserTokenHandler.INSTANCE;
/*      */       } else {
/*  982 */         userTokenHandlerCopy = NoopUserTokenHandler.INSTANCE;
/*      */       } 
/*      */     }
/*      */     
/*  986 */     String userAgentCopy = this.userAgent;
/*  987 */     if (userAgentCopy == null) {
/*  988 */       if (this.systemProperties) {
/*  989 */         userAgentCopy = System.getProperty("http.agent");
/*      */       }
/*  991 */       if (userAgentCopy == null) {
/*  992 */         userAgentCopy = VersionInfo.getUserAgent("Apache-HttpClient", "org.apache.http.client", getClass());
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  997 */     ClientExecChain execChain = createMainExec(requestExecCopy, (HttpClientConnectionManager)poolingHttpClientConnectionManager, (ConnectionReuseStrategy)defaultConnectionReuseStrategy, keepAliveStrategyCopy, (HttpProcessor)new ImmutableHttpProcessor(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestTargetHost(), (HttpRequestInterceptor)new RequestUserAgent(userAgentCopy) }, ), targetAuthStrategyCopy, proxyAuthStrategyCopy, userTokenHandlerCopy);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1007 */     execChain = decorateMainExec(execChain);
/*      */     
/* 1009 */     HttpProcessor httpprocessorCopy = this.httpprocessor;
/* 1010 */     if (httpprocessorCopy == null) {
/*      */       
/* 1012 */       HttpProcessorBuilder b = HttpProcessorBuilder.create();
/* 1013 */       if (this.requestFirst != null) {
/* 1014 */         for (HttpRequestInterceptor i : this.requestFirst) {
/* 1015 */           b.addFirst(i);
/*      */         }
/*      */       }
/* 1018 */       if (this.responseFirst != null) {
/* 1019 */         for (HttpResponseInterceptor i : this.responseFirst) {
/* 1020 */           b.addFirst(i);
/*      */         }
/*      */       }
/* 1023 */       b.addAll(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestDefaultHeaders(this.defaultHeaders), (HttpRequestInterceptor)new RequestContent(), (HttpRequestInterceptor)new RequestTargetHost(), (HttpRequestInterceptor)new RequestClientConnControl(), (HttpRequestInterceptor)new RequestUserAgent(userAgentCopy), (HttpRequestInterceptor)new RequestExpectContinue() });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1030 */       if (!this.cookieManagementDisabled) {
/* 1031 */         b.add((HttpRequestInterceptor)new RequestAddCookies());
/*      */       }
/* 1033 */       if (!this.contentCompressionDisabled) {
/* 1034 */         if (this.contentDecoderMap != null) {
/* 1035 */           List<String> encodings = new ArrayList<String>(this.contentDecoderMap.keySet());
/* 1036 */           Collections.sort(encodings);
/* 1037 */           b.add((HttpRequestInterceptor)new RequestAcceptEncoding(encodings));
/*      */         } else {
/* 1039 */           b.add((HttpRequestInterceptor)new RequestAcceptEncoding());
/*      */         } 
/*      */       }
/* 1042 */       if (!this.authCachingDisabled) {
/* 1043 */         b.add((HttpRequestInterceptor)new RequestAuthCache());
/*      */       }
/* 1045 */       if (!this.cookieManagementDisabled) {
/* 1046 */         b.add((HttpResponseInterceptor)new ResponseProcessCookies());
/*      */       }
/* 1048 */       if (!this.contentCompressionDisabled) {
/* 1049 */         if (this.contentDecoderMap != null) {
/* 1050 */           RegistryBuilder<InputStreamFactory> b2 = RegistryBuilder.create();
/* 1051 */           for (Map.Entry<String, InputStreamFactory> entry : this.contentDecoderMap.entrySet()) {
/* 1052 */             b2.register(entry.getKey(), entry.getValue());
/*      */           }
/* 1054 */           b.add((HttpResponseInterceptor)new ResponseContentEncoding((Lookup)b2.build()));
/*      */         } else {
/* 1056 */           b.add((HttpResponseInterceptor)new ResponseContentEncoding());
/*      */         } 
/*      */       }
/* 1059 */       if (this.requestLast != null) {
/* 1060 */         for (HttpRequestInterceptor i : this.requestLast) {
/* 1061 */           b.addLast(i);
/*      */         }
/*      */       }
/* 1064 */       if (this.responseLast != null) {
/* 1065 */         for (HttpResponseInterceptor i : this.responseLast) {
/* 1066 */           b.addLast(i);
/*      */         }
/*      */       }
/* 1069 */       httpprocessorCopy = b.build();
/*      */     } 
/* 1071 */     ProtocolExec protocolExec = new ProtocolExec(execChain, httpprocessorCopy);
/*      */     
/* 1073 */     ClientExecChain clientExecChain1 = decorateProtocolExec((ClientExecChain)protocolExec);
/*      */ 
/*      */     
/* 1076 */     if (!this.automaticRetriesDisabled) {
/* 1077 */       HttpRequestRetryHandler retryHandlerCopy = this.retryHandler;
/* 1078 */       if (retryHandlerCopy == null) {
/* 1079 */         retryHandlerCopy = DefaultHttpRequestRetryHandler.INSTANCE;
/*      */       }
/* 1081 */       retryExec = new RetryExec(clientExecChain1, retryHandlerCopy);
/*      */     } 
/*      */     
/* 1084 */     HttpRoutePlanner routePlannerCopy = this.routePlanner;
/* 1085 */     if (routePlannerCopy == null) {
/* 1086 */       DefaultSchemePortResolver defaultSchemePortResolver; SchemePortResolver schemePortResolverCopy = this.schemePortResolver;
/* 1087 */       if (schemePortResolverCopy == null) {
/* 1088 */         defaultSchemePortResolver = DefaultSchemePortResolver.INSTANCE;
/*      */       }
/* 1090 */       if (this.proxy != null) {
/* 1091 */         DefaultProxyRoutePlanner defaultProxyRoutePlanner = new DefaultProxyRoutePlanner(this.proxy, (SchemePortResolver)defaultSchemePortResolver);
/* 1092 */       } else if (this.systemProperties) {
/* 1093 */         SystemDefaultRoutePlanner systemDefaultRoutePlanner = new SystemDefaultRoutePlanner((SchemePortResolver)defaultSchemePortResolver, ProxySelector.getDefault());
/*      */       } else {
/*      */         
/* 1096 */         defaultRoutePlanner = new DefaultRoutePlanner((SchemePortResolver)defaultSchemePortResolver);
/*      */       } 
/*      */     } 
/*      */     
/* 1100 */     if (!this.redirectHandlingDisabled) {
/* 1101 */       RedirectStrategy redirectStrategyCopy = this.redirectStrategy;
/* 1102 */       if (redirectStrategyCopy == null) {
/* 1103 */         redirectStrategyCopy = DefaultRedirectStrategy.INSTANCE;
/*      */       }
/* 1105 */       redirectExec = new RedirectExec((ClientExecChain)retryExec, (HttpRoutePlanner)defaultRoutePlanner, redirectStrategyCopy);
/*      */     } 
/*      */ 
/*      */     
/* 1109 */     ServiceUnavailableRetryStrategy serviceUnavailStrategyCopy = this.serviceUnavailStrategy;
/* 1110 */     if (serviceUnavailStrategyCopy != null) {
/* 1111 */       serviceUnavailableRetryExec = new ServiceUnavailableRetryExec((ClientExecChain)redirectExec, serviceUnavailStrategyCopy);
/*      */     }
/*      */     
/* 1114 */     if (this.backoffManager != null && this.connectionBackoffStrategy != null) {
/* 1115 */       backoffStrategyExec = new BackoffStrategyExec((ClientExecChain)serviceUnavailableRetryExec, this.connectionBackoffStrategy, this.backoffManager);
/*      */     }
/*      */     
/* 1118 */     Lookup<AuthSchemeProvider> authSchemeRegistryCopy = this.authSchemeRegistry;
/* 1119 */     if (authSchemeRegistryCopy == null) {
/* 1120 */       registry1 = RegistryBuilder.create().register("Basic", new BasicSchemeFactory()).register("Digest", new DigestSchemeFactory()).register("NTLM", new NTLMSchemeFactory()).register("Negotiate", new SPNegoSchemeFactory()).register("Kerberos", new KerberosSchemeFactory()).build();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1128 */     Lookup<CookieSpecProvider> cookieSpecRegistryCopy = this.cookieSpecRegistry;
/* 1129 */     if (cookieSpecRegistryCopy == null) {
/* 1130 */       DefaultCookieSpecProvider defaultCookieSpecProvider = new DefaultCookieSpecProvider(publicSuffixMatcherCopy);
/* 1131 */       RFC6265CookieSpecProvider rFC6265CookieSpecProvider1 = new RFC6265CookieSpecProvider(RFC6265CookieSpecProvider.CompatibilityLevel.RELAXED, publicSuffixMatcherCopy);
/*      */       
/* 1133 */       RFC6265CookieSpecProvider rFC6265CookieSpecProvider2 = new RFC6265CookieSpecProvider(RFC6265CookieSpecProvider.CompatibilityLevel.STRICT, publicSuffixMatcherCopy);
/*      */       
/* 1135 */       registry2 = RegistryBuilder.create().register("default", defaultCookieSpecProvider).register("best-match", defaultCookieSpecProvider).register("compatibility", defaultCookieSpecProvider).register("standard", rFC6265CookieSpecProvider1).register("standard-strict", rFC6265CookieSpecProvider2).register("netscape", new NetscapeDraftSpecProvider()).register("ignoreCookies", new IgnoreSpecProvider()).build();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1146 */     CookieStore defaultCookieStore = this.cookieStore;
/* 1147 */     if (defaultCookieStore == null) {
/* 1148 */       defaultCookieStore = new BasicCookieStore();
/*      */     }
/*      */     
/* 1151 */     CredentialsProvider defaultCredentialsProvider = this.credentialsProvider;
/* 1152 */     if (defaultCredentialsProvider == null) {
/* 1153 */       if (this.systemProperties) {
/* 1154 */         defaultCredentialsProvider = new SystemDefaultCredentialsProvider();
/*      */       } else {
/* 1156 */         defaultCredentialsProvider = new BasicCredentialsProvider();
/*      */       } 
/*      */     }
/*      */     
/* 1160 */     List<Closeable> closeablesCopy = (this.closeables != null) ? new ArrayList<Closeable>(this.closeables) : null;
/* 1161 */     if (!this.connManagerShared) {
/* 1162 */       if (closeablesCopy == null) {
/* 1163 */         closeablesCopy = new ArrayList<Closeable>(1);
/*      */       }
/* 1165 */       final PoolingHttpClientConnectionManager cm = poolingHttpClientConnectionManager;
/*      */       
/* 1167 */       if (this.evictExpiredConnections || this.evictIdleConnections) {
/* 1168 */         final IdleConnectionEvictor connectionEvictor = new IdleConnectionEvictor((HttpClientConnectionManager)poolingHttpClientConnectionManager1, (this.maxIdleTime > 0L) ? this.maxIdleTime : 10L, (this.maxIdleTimeUnit != null) ? this.maxIdleTimeUnit : TimeUnit.SECONDS);
/*      */         
/* 1170 */         closeablesCopy.add(new Closeable()
/*      */             {
/*      */               public void close() throws IOException
/*      */               {
/* 1174 */                 connectionEvictor.shutdown();
/*      */               }
/*      */             });
/*      */         
/* 1178 */         connectionEvictor.start();
/*      */       } 
/* 1180 */       closeablesCopy.add(new Closeable()
/*      */           {
/*      */             public void close() throws IOException
/*      */             {
/* 1184 */               cm.shutdown();
/*      */             }
/*      */           });
/*      */     } 
/*      */ 
/*      */     
/* 1190 */     return new InternalHttpClient((ClientExecChain)backoffStrategyExec, (HttpClientConnectionManager)poolingHttpClientConnectionManager, (HttpRoutePlanner)defaultRoutePlanner, (Lookup<CookieSpecProvider>)registry2, (Lookup<AuthSchemeProvider>)registry1, defaultCookieStore, defaultCredentialsProvider, (this.defaultRequestConfig != null) ? this.defaultRequestConfig : RequestConfig.DEFAULT, closeablesCopy); }
/*      */ 
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/client/HttpClientBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
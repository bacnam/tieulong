/*     */ package org.apache.http.client.config;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.util.Collection;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class RequestConfig
/*     */   implements Cloneable
/*     */ {
/*  44 */   public static final RequestConfig DEFAULT = (new Builder()).build();
/*     */ 
/*     */   
/*     */   private final boolean expectContinueEnabled;
/*     */   
/*     */   private final HttpHost proxy;
/*     */   
/*     */   private final InetAddress localAddress;
/*     */   
/*     */   private final boolean staleConnectionCheckEnabled;
/*     */   
/*     */   private final String cookieSpec;
/*     */   
/*     */   private final boolean redirectsEnabled;
/*     */   
/*     */   private final boolean relativeRedirectsAllowed;
/*     */   
/*     */   private final boolean circularRedirectsAllowed;
/*     */   
/*     */   private final int maxRedirects;
/*     */   
/*     */   private final boolean authenticationEnabled;
/*     */   
/*     */   private final Collection<String> targetPreferredAuthSchemes;
/*     */   
/*     */   private final Collection<String> proxyPreferredAuthSchemes;
/*     */   
/*     */   private final int connectionRequestTimeout;
/*     */   
/*     */   private final int connectTimeout;
/*     */   
/*     */   private final int socketTimeout;
/*     */   
/*     */   private final boolean decompressionEnabled;
/*     */ 
/*     */   
/*     */   RequestConfig(boolean expectContinueEnabled, HttpHost proxy, InetAddress localAddress, boolean staleConnectionCheckEnabled, String cookieSpec, boolean redirectsEnabled, boolean relativeRedirectsAllowed, boolean circularRedirectsAllowed, int maxRedirects, boolean authenticationEnabled, Collection<String> targetPreferredAuthSchemes, Collection<String> proxyPreferredAuthSchemes, int connectionRequestTimeout, int connectTimeout, int socketTimeout, boolean decompressionEnabled) {
/*  81 */     this.expectContinueEnabled = expectContinueEnabled;
/*  82 */     this.proxy = proxy;
/*  83 */     this.localAddress = localAddress;
/*  84 */     this.staleConnectionCheckEnabled = staleConnectionCheckEnabled;
/*  85 */     this.cookieSpec = cookieSpec;
/*  86 */     this.redirectsEnabled = redirectsEnabled;
/*  87 */     this.relativeRedirectsAllowed = relativeRedirectsAllowed;
/*  88 */     this.circularRedirectsAllowed = circularRedirectsAllowed;
/*  89 */     this.maxRedirects = maxRedirects;
/*  90 */     this.authenticationEnabled = authenticationEnabled;
/*  91 */     this.targetPreferredAuthSchemes = targetPreferredAuthSchemes;
/*  92 */     this.proxyPreferredAuthSchemes = proxyPreferredAuthSchemes;
/*  93 */     this.connectionRequestTimeout = connectionRequestTimeout;
/*  94 */     this.connectTimeout = connectTimeout;
/*  95 */     this.socketTimeout = socketTimeout;
/*  96 */     this.decompressionEnabled = decompressionEnabled;
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
/*     */   public boolean isExpectContinueEnabled() {
/* 122 */     return this.expectContinueEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpHost getProxy() {
/* 132 */     return this.proxy;
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
/*     */   public InetAddress getLocalAddress() {
/* 147 */     return this.localAddress;
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
/*     */   @Deprecated
/*     */   public boolean isStaleConnectionCheckEnabled() {
/* 164 */     return this.staleConnectionCheckEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCookieSpec() {
/* 175 */     return this.cookieSpec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRedirectsEnabled() {
/* 185 */     return this.redirectsEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRelativeRedirectsAllowed() {
/* 196 */     return this.relativeRedirectsAllowed;
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
/*     */   public boolean isCircularRedirectsAllowed() {
/* 208 */     return this.circularRedirectsAllowed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxRedirects() {
/* 219 */     return this.maxRedirects;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAuthenticationEnabled() {
/* 229 */     return this.authenticationEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> getTargetPreferredAuthSchemes() {
/* 240 */     return this.targetPreferredAuthSchemes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<String> getProxyPreferredAuthSchemes() {
/* 251 */     return this.proxyPreferredAuthSchemes;
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
/*     */   public int getConnectionRequestTimeout() {
/* 267 */     return this.connectionRequestTimeout;
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
/*     */   public int getConnectTimeout() {
/* 282 */     return this.connectTimeout;
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
/*     */   public int getSocketTimeout() {
/* 298 */     return this.socketTimeout;
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
/*     */   public boolean isDecompressionEnabled() {
/* 310 */     return this.decompressionEnabled;
/*     */   }
/*     */ 
/*     */   
/*     */   protected RequestConfig clone() throws CloneNotSupportedException {
/* 315 */     return (RequestConfig)super.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 320 */     StringBuilder builder = new StringBuilder();
/* 321 */     builder.append("[");
/* 322 */     builder.append("expectContinueEnabled=").append(this.expectContinueEnabled);
/* 323 */     builder.append(", proxy=").append(this.proxy);
/* 324 */     builder.append(", localAddress=").append(this.localAddress);
/* 325 */     builder.append(", cookieSpec=").append(this.cookieSpec);
/* 326 */     builder.append(", redirectsEnabled=").append(this.redirectsEnabled);
/* 327 */     builder.append(", relativeRedirectsAllowed=").append(this.relativeRedirectsAllowed);
/* 328 */     builder.append(", maxRedirects=").append(this.maxRedirects);
/* 329 */     builder.append(", circularRedirectsAllowed=").append(this.circularRedirectsAllowed);
/* 330 */     builder.append(", authenticationEnabled=").append(this.authenticationEnabled);
/* 331 */     builder.append(", targetPreferredAuthSchemes=").append(this.targetPreferredAuthSchemes);
/* 332 */     builder.append(", proxyPreferredAuthSchemes=").append(this.proxyPreferredAuthSchemes);
/* 333 */     builder.append(", connectionRequestTimeout=").append(this.connectionRequestTimeout);
/* 334 */     builder.append(", connectTimeout=").append(this.connectTimeout);
/* 335 */     builder.append(", socketTimeout=").append(this.socketTimeout);
/* 336 */     builder.append(", decompressionEnabled=").append(this.decompressionEnabled);
/* 337 */     builder.append("]");
/* 338 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static Builder custom() {
/* 342 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   public static Builder copy(RequestConfig config) {
/* 347 */     return (new Builder()).setExpectContinueEnabled(config.isExpectContinueEnabled()).setProxy(config.getProxy()).setLocalAddress(config.getLocalAddress()).setStaleConnectionCheckEnabled(config.isStaleConnectionCheckEnabled()).setCookieSpec(config.getCookieSpec()).setRedirectsEnabled(config.isRedirectsEnabled()).setRelativeRedirectsAllowed(config.isRelativeRedirectsAllowed()).setCircularRedirectsAllowed(config.isCircularRedirectsAllowed()).setMaxRedirects(config.getMaxRedirects()).setAuthenticationEnabled(config.isAuthenticationEnabled()).setTargetPreferredAuthSchemes(config.getTargetPreferredAuthSchemes()).setProxyPreferredAuthSchemes(config.getProxyPreferredAuthSchemes()).setConnectionRequestTimeout(config.getConnectionRequestTimeout()).setConnectTimeout(config.getConnectTimeout()).setSocketTimeout(config.getSocketTimeout()).setDecompressionEnabled(config.isDecompressionEnabled());
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
/*     */   public static class Builder
/*     */   {
/*     */     private boolean staleConnectionCheckEnabled = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean redirectsEnabled = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 389 */     private int maxRedirects = 50;
/*     */     private boolean relativeRedirectsAllowed = true;
/*     */     private boolean authenticationEnabled = true;
/* 392 */     private int connectionRequestTimeout = -1;
/* 393 */     private int connectTimeout = -1;
/* 394 */     private int socketTimeout = -1; private boolean decompressionEnabled = true; private boolean expectContinueEnabled;
/*     */     private HttpHost proxy;
/*     */     private InetAddress localAddress;
/*     */     
/*     */     public Builder setExpectContinueEnabled(boolean expectContinueEnabled) {
/* 399 */       this.expectContinueEnabled = expectContinueEnabled;
/* 400 */       return this;
/*     */     }
/*     */     private String cookieSpec; private boolean circularRedirectsAllowed; private Collection<String> targetPreferredAuthSchemes; private Collection<String> proxyPreferredAuthSchemes;
/*     */     public Builder setProxy(HttpHost proxy) {
/* 404 */       this.proxy = proxy;
/* 405 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setLocalAddress(InetAddress localAddress) {
/* 409 */       this.localAddress = localAddress;
/* 410 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Builder setStaleConnectionCheckEnabled(boolean staleConnectionCheckEnabled) {
/* 419 */       this.staleConnectionCheckEnabled = staleConnectionCheckEnabled;
/* 420 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setCookieSpec(String cookieSpec) {
/* 424 */       this.cookieSpec = cookieSpec;
/* 425 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setRedirectsEnabled(boolean redirectsEnabled) {
/* 429 */       this.redirectsEnabled = redirectsEnabled;
/* 430 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setRelativeRedirectsAllowed(boolean relativeRedirectsAllowed) {
/* 434 */       this.relativeRedirectsAllowed = relativeRedirectsAllowed;
/* 435 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setCircularRedirectsAllowed(boolean circularRedirectsAllowed) {
/* 439 */       this.circularRedirectsAllowed = circularRedirectsAllowed;
/* 440 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setMaxRedirects(int maxRedirects) {
/* 444 */       this.maxRedirects = maxRedirects;
/* 445 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setAuthenticationEnabled(boolean authenticationEnabled) {
/* 449 */       this.authenticationEnabled = authenticationEnabled;
/* 450 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setTargetPreferredAuthSchemes(Collection<String> targetPreferredAuthSchemes) {
/* 454 */       this.targetPreferredAuthSchemes = targetPreferredAuthSchemes;
/* 455 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setProxyPreferredAuthSchemes(Collection<String> proxyPreferredAuthSchemes) {
/* 459 */       this.proxyPreferredAuthSchemes = proxyPreferredAuthSchemes;
/* 460 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setConnectionRequestTimeout(int connectionRequestTimeout) {
/* 464 */       this.connectionRequestTimeout = connectionRequestTimeout;
/* 465 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setConnectTimeout(int connectTimeout) {
/* 469 */       this.connectTimeout = connectTimeout;
/* 470 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSocketTimeout(int socketTimeout) {
/* 474 */       this.socketTimeout = socketTimeout;
/* 475 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDecompressionEnabled(boolean decompressionEnabled) {
/* 479 */       this.decompressionEnabled = decompressionEnabled;
/* 480 */       return this;
/*     */     }
/*     */     
/*     */     public RequestConfig build() {
/* 484 */       return new RequestConfig(this.expectContinueEnabled, this.proxy, this.localAddress, this.staleConnectionCheckEnabled, this.cookieSpec, this.redirectsEnabled, this.relativeRedirectsAllowed, this.circularRedirectsAllowed, this.maxRedirects, this.authenticationEnabled, this.targetPreferredAuthSchemes, this.proxyPreferredAuthSchemes, this.connectionRequestTimeout, this.connectTimeout, this.socketTimeout, this.decompressionEnabled);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/client/config/RequestConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
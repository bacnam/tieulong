/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Proxy;
/*     */ import java.net.ProxySelector;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.List;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.conn.params.ConnRouteParams;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*     */ import org.apache.http.conn.scheme.Scheme;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
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
/*     */ @Deprecated
/*     */ @NotThreadSafe
/*     */ public class ProxySelectorRoutePlanner
/*     */   implements HttpRoutePlanner
/*     */ {
/*     */   protected final SchemeRegistry schemeRegistry;
/*     */   protected ProxySelector proxySelector;
/*     */   
/*     */   public ProxySelectorRoutePlanner(SchemeRegistry schreg, ProxySelector prosel) {
/*  93 */     Args.notNull(schreg, "SchemeRegistry");
/*  94 */     this.schemeRegistry = schreg;
/*  95 */     this.proxySelector = prosel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxySelector getProxySelector() {
/* 104 */     return this.proxySelector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxySelector(ProxySelector prosel) {
/* 114 */     this.proxySelector = prosel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
/* 122 */     Args.notNull(request, "HTTP request");
/*     */ 
/*     */     
/* 125 */     HttpRoute route = ConnRouteParams.getForcedRoute(request.getParams());
/*     */     
/* 127 */     if (route != null) {
/* 128 */       return route;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 134 */     Asserts.notNull(target, "Target host");
/*     */     
/* 136 */     InetAddress local = ConnRouteParams.getLocalAddress(request.getParams());
/*     */     
/* 138 */     HttpHost proxy = determineProxy(target, request, context);
/*     */     
/* 140 */     Scheme schm = this.schemeRegistry.getScheme(target.getSchemeName());
/*     */ 
/*     */ 
/*     */     
/* 144 */     boolean secure = schm.isLayered();
/*     */     
/* 146 */     if (proxy == null) {
/* 147 */       route = new HttpRoute(target, local, secure);
/*     */     } else {
/* 149 */       route = new HttpRoute(target, local, proxy, secure);
/*     */     } 
/* 151 */     return route;
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
/*     */   protected HttpHost determineProxy(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
/* 172 */     ProxySelector psel = this.proxySelector;
/* 173 */     if (psel == null) {
/* 174 */       psel = ProxySelector.getDefault();
/*     */     }
/* 176 */     if (psel == null) {
/* 177 */       return null;
/*     */     }
/*     */     
/* 180 */     URI targetURI = null;
/*     */     try {
/* 182 */       targetURI = new URI(target.toURI());
/* 183 */     } catch (URISyntaxException usx) {
/* 184 */       throw new HttpException("Cannot convert host to URI: " + target, usx);
/*     */     } 
/*     */     
/* 187 */     List<Proxy> proxies = psel.select(targetURI);
/*     */     
/* 189 */     Proxy p = chooseProxy(proxies, target, request, context);
/*     */     
/* 191 */     HttpHost result = null;
/* 192 */     if (p.type() == Proxy.Type.HTTP) {
/*     */       
/* 194 */       if (!(p.address() instanceof InetSocketAddress)) {
/* 195 */         throw new HttpException("Unable to handle non-Inet proxy address: " + p.address());
/*     */       }
/*     */       
/* 198 */       InetSocketAddress isa = (InetSocketAddress)p.address();
/*     */       
/* 200 */       result = new HttpHost(getHost(isa), isa.getPort());
/*     */     } 
/*     */     
/* 203 */     return result;
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
/*     */   protected String getHost(InetSocketAddress isa) {
/* 222 */     return isa.isUnresolved() ? isa.getHostName() : isa.getAddress().getHostAddress();
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
/*     */   protected Proxy chooseProxy(List<Proxy> proxies, HttpHost target, HttpRequest request, HttpContext context) {
/* 247 */     Args.notEmpty(proxies, "List of proxies");
/*     */     
/* 249 */     Proxy result = null;
/*     */ 
/*     */     
/* 252 */     for (int i = 0; result == null && i < proxies.size(); i++) {
/*     */       
/* 254 */       Proxy p = proxies.get(i);
/* 255 */       switch (p.type()) {
/*     */         
/*     */         case DIRECT:
/*     */         case HTTP:
/* 259 */           result = p;
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/* 269 */     if (result == null)
/*     */     {
/*     */ 
/*     */       
/* 273 */       result = Proxy.NO_PROXY;
/*     */     }
/*     */     
/* 276 */     return result;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/conn/ProxySelectorRoutePlanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
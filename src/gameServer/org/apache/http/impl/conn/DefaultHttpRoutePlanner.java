/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.ThreadSafe;
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
/*     */ @Deprecated
/*     */ @ThreadSafe
/*     */ public class DefaultHttpRoutePlanner
/*     */   implements HttpRoutePlanner
/*     */ {
/*     */   protected final SchemeRegistry schemeRegistry;
/*     */   
/*     */   public DefaultHttpRoutePlanner(SchemeRegistry schreg) {
/*  77 */     Args.notNull(schreg, "Scheme registry");
/*  78 */     this.schemeRegistry = schreg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
/*     */     Scheme schm;
/*  86 */     Args.notNull(request, "HTTP request");
/*     */ 
/*     */     
/*  89 */     HttpRoute route = ConnRouteParams.getForcedRoute(request.getParams());
/*     */     
/*  91 */     if (route != null) {
/*  92 */       return route;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     Asserts.notNull(target, "Target host");
/*     */     
/* 100 */     InetAddress local = ConnRouteParams.getLocalAddress(request.getParams());
/*     */     
/* 102 */     HttpHost proxy = ConnRouteParams.getDefaultProxy(request.getParams());
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 107 */       schm = this.schemeRegistry.getScheme(target.getSchemeName());
/* 108 */     } catch (IllegalStateException ex) {
/* 109 */       throw new HttpException(ex.getMessage());
/*     */     } 
/*     */ 
/*     */     
/* 113 */     boolean secure = schm.isLayered();
/*     */     
/* 115 */     if (proxy == null) {
/* 116 */       route = new HttpRoute(target, local, secure);
/*     */     } else {
/* 118 */       route = new HttpRoute(target, local, proxy, secure);
/*     */     } 
/* 120 */     return route;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/conn/DefaultHttpRoutePlanner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
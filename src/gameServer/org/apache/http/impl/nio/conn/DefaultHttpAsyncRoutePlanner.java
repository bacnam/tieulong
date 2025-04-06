/*    */ package org.apache.http.impl.nio.conn;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpHost;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.conn.params.ConnRouteParams;
/*    */ import org.apache.http.conn.routing.HttpRoute;
/*    */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*    */ import org.apache.http.nio.conn.scheme.AsyncScheme;
/*    */ import org.apache.http.nio.conn.scheme.AsyncSchemeRegistry;
/*    */ import org.apache.http.nio.conn.scheme.LayeringStrategy;
/*    */ import org.apache.http.protocol.HttpContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class DefaultHttpAsyncRoutePlanner
/*    */   implements HttpRoutePlanner
/*    */ {
/*    */   private final AsyncSchemeRegistry schemeRegistry;
/*    */   
/*    */   public DefaultHttpAsyncRoutePlanner(AsyncSchemeRegistry schemeRegistry) {
/* 51 */     this.schemeRegistry = schemeRegistry;
/*    */   }
/*    */   
/*    */   private AsyncSchemeRegistry getSchemeRegistry(HttpContext context) {
/* 55 */     AsyncSchemeRegistry reg = (AsyncSchemeRegistry)context.getAttribute("http.scheme-registry");
/*    */     
/* 57 */     if (reg == null) {
/* 58 */       reg = this.schemeRegistry;
/*    */     }
/* 60 */     return reg;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
/*    */     AsyncScheme scheme;
/* 67 */     if (request == null) {
/* 68 */       throw new IllegalStateException("Request may not be null");
/*    */     }
/* 70 */     HttpRoute route = ConnRouteParams.getForcedRoute(request.getParams());
/* 71 */     if (route != null) {
/* 72 */       return route;
/*    */     }
/* 74 */     if (target == null) {
/* 75 */       throw new IllegalStateException("Target host may be null");
/*    */     }
/* 77 */     InetAddress local = ConnRouteParams.getLocalAddress(request.getParams());
/* 78 */     HttpHost proxy = ConnRouteParams.getDefaultProxy(request.getParams());
/*    */     
/*    */     try {
/* 81 */       AsyncSchemeRegistry registry = getSchemeRegistry(context);
/* 82 */       scheme = registry.getScheme(target);
/* 83 */     } catch (IllegalStateException ex) {
/* 84 */       throw new HttpException(ex.getMessage());
/*    */     } 
/* 86 */     LayeringStrategy layeringStrategy = scheme.getLayeringStrategy();
/* 87 */     boolean secure = (layeringStrategy != null && layeringStrategy.isSecure());
/* 88 */     if (proxy == null) {
/* 89 */       route = new HttpRoute(target, local, secure);
/*    */     } else {
/* 91 */       route = new HttpRoute(target, local, proxy, secure);
/*    */     } 
/* 93 */     return route;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/conn/DefaultHttpAsyncRoutePlanner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
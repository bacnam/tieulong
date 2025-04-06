/*     */ package org.apache.http.conn.params;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class ConnPerRouteBean
/*     */   implements ConnPerRoute
/*     */ {
/*     */   public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 2;
/*     */   private final ConcurrentHashMap<HttpRoute, Integer> maxPerHostMap;
/*     */   private volatile int defaultMax;
/*     */   
/*     */   public ConnPerRouteBean(int defaultMax) {
/*  59 */     this.maxPerHostMap = new ConcurrentHashMap<HttpRoute, Integer>();
/*  60 */     setDefaultMaxPerRoute(defaultMax);
/*     */   }
/*     */   
/*     */   public ConnPerRouteBean() {
/*  64 */     this(2);
/*     */   }
/*     */   
/*     */   public int getDefaultMax() {
/*  68 */     return this.defaultMax;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/*  75 */     return this.defaultMax;
/*     */   }
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/*  79 */     Args.positive(max, "Default max per route");
/*  80 */     this.defaultMax = max;
/*     */   }
/*     */   
/*     */   public void setMaxForRoute(HttpRoute route, int max) {
/*  84 */     Args.notNull(route, "HTTP route");
/*  85 */     Args.positive(max, "Max per route");
/*  86 */     this.maxPerHostMap.put(route, Integer.valueOf(max));
/*     */   }
/*     */   
/*     */   public int getMaxForRoute(HttpRoute route) {
/*  90 */     Args.notNull(route, "HTTP route");
/*  91 */     Integer max = this.maxPerHostMap.get(route);
/*  92 */     if (max != null) {
/*  93 */       return max.intValue();
/*     */     }
/*  95 */     return this.defaultMax;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxForRoutes(Map<HttpRoute, Integer> map) {
/* 100 */     if (map == null) {
/*     */       return;
/*     */     }
/* 103 */     this.maxPerHostMap.clear();
/* 104 */     this.maxPerHostMap.putAll(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 109 */     return this.maxPerHostMap.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/conn/params/ConnPerRouteBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package org.apache.http.nio.conn.scheme;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.http.HttpHost;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class AsyncSchemeRegistry
/*     */ {
/*  52 */   private final Map<String, AsyncScheme> registeredSchemes = new ConcurrentHashMap<String, AsyncScheme>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncScheme getScheme(String name) {
/*  66 */     AsyncScheme found = get(name);
/*  67 */     if (found == null) {
/*  68 */       throw new IllegalStateException("Scheme '" + name + "' not registered.");
/*     */     }
/*     */     
/*  71 */     return found;
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
/*     */   public final AsyncScheme getScheme(HttpHost host) {
/*  86 */     if (host == null) {
/*  87 */       throw new IllegalArgumentException("Host must not be null.");
/*     */     }
/*  89 */     return getScheme(host.getSchemeName());
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
/*     */   public final AsyncScheme get(String name) {
/* 101 */     if (name == null) {
/* 102 */       throw new IllegalArgumentException("Name must not be null.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 107 */     AsyncScheme found = this.registeredSchemes.get(name);
/* 108 */     return found;
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
/*     */   public final AsyncScheme register(AsyncScheme sch) {
/* 122 */     if (sch == null) {
/* 123 */       throw new IllegalArgumentException("Scheme must not be null.");
/*     */     }
/*     */     
/* 126 */     AsyncScheme old = this.registeredSchemes.put(sch.getName(), sch);
/* 127 */     return old;
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
/*     */   public final AsyncScheme unregister(String name) {
/* 139 */     if (name == null) {
/* 140 */       throw new IllegalArgumentException("Name must not be null.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 145 */     AsyncScheme gone = this.registeredSchemes.remove(name);
/* 146 */     return gone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final List<String> getSchemeNames() {
/* 155 */     return new ArrayList<String>(this.registeredSchemes.keySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItems(Map<String, AsyncScheme> map) {
/* 165 */     if (map == null) {
/*     */       return;
/*     */     }
/* 168 */     this.registeredSchemes.clear();
/* 169 */     this.registeredSchemes.putAll(map);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/conn/scheme/AsyncSchemeRegistry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
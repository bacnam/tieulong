/*     */ package org.apache.http.client.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.auth.AuthProtocolState;
/*     */ import org.apache.http.auth.AuthScheme;
/*     */ import org.apache.http.auth.AuthState;
/*     */ import org.apache.http.client.AuthCache;
/*     */ import org.apache.http.conn.scheme.Scheme;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.impl.client.BasicAuthCache;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public class ResponseAuthCache
/*     */   implements HttpResponseInterceptor
/*     */ {
/*  64 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
/*     */     BasicAuthCache basicAuthCache;
/*  72 */     Args.notNull(response, "HTTP request");
/*  73 */     Args.notNull(context, "HTTP context");
/*  74 */     AuthCache authCache = (AuthCache)context.getAttribute("http.auth.auth-cache");
/*     */     
/*  76 */     HttpHost target = (HttpHost)context.getAttribute("http.target_host");
/*  77 */     AuthState targetState = (AuthState)context.getAttribute("http.auth.target-scope");
/*  78 */     if (target != null && targetState != null) {
/*  79 */       if (this.log.isDebugEnabled()) {
/*  80 */         this.log.debug("Target auth state: " + targetState.getState());
/*     */       }
/*  82 */       if (isCachable(targetState)) {
/*  83 */         SchemeRegistry schemeRegistry = (SchemeRegistry)context.getAttribute("http.scheme-registry");
/*     */         
/*  85 */         if (target.getPort() < 0) {
/*  86 */           Scheme scheme = schemeRegistry.getScheme(target);
/*  87 */           target = new HttpHost(target.getHostName(), scheme.resolvePort(target.getPort()), target.getSchemeName());
/*     */         } 
/*     */         
/*  90 */         if (authCache == null) {
/*  91 */           basicAuthCache = new BasicAuthCache();
/*  92 */           context.setAttribute("http.auth.auth-cache", basicAuthCache);
/*     */         } 
/*  94 */         switch (targetState.getState()) {
/*     */           case CHALLENGED:
/*  96 */             cache((AuthCache)basicAuthCache, target, targetState.getAuthScheme());
/*     */             break;
/*     */           case FAILURE:
/*  99 */             uncache((AuthCache)basicAuthCache, target, targetState.getAuthScheme());
/*     */             break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 104 */     HttpHost proxy = (HttpHost)context.getAttribute("http.proxy_host");
/* 105 */     AuthState proxyState = (AuthState)context.getAttribute("http.auth.proxy-scope");
/* 106 */     if (proxy != null && proxyState != null) {
/* 107 */       if (this.log.isDebugEnabled()) {
/* 108 */         this.log.debug("Proxy auth state: " + proxyState.getState());
/*     */       }
/* 110 */       if (isCachable(proxyState)) {
/* 111 */         if (basicAuthCache == null) {
/* 112 */           basicAuthCache = new BasicAuthCache();
/* 113 */           context.setAttribute("http.auth.auth-cache", basicAuthCache);
/*     */         } 
/* 115 */         switch (proxyState.getState()) {
/*     */           case CHALLENGED:
/* 117 */             cache((AuthCache)basicAuthCache, proxy, proxyState.getAuthScheme());
/*     */             break;
/*     */           case FAILURE:
/* 120 */             uncache((AuthCache)basicAuthCache, proxy, proxyState.getAuthScheme());
/*     */             break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private boolean isCachable(AuthState authState) {
/* 127 */     AuthScheme authScheme = authState.getAuthScheme();
/* 128 */     if (authScheme == null || !authScheme.isComplete()) {
/* 129 */       return false;
/*     */     }
/* 131 */     String schemeName = authScheme.getSchemeName();
/* 132 */     return (schemeName.equalsIgnoreCase("Basic") || schemeName.equalsIgnoreCase("Digest"));
/*     */   }
/*     */ 
/*     */   
/*     */   private void cache(AuthCache authCache, HttpHost host, AuthScheme authScheme) {
/* 137 */     if (this.log.isDebugEnabled()) {
/* 138 */       this.log.debug("Caching '" + authScheme.getSchemeName() + "' auth scheme for " + host);
/*     */     }
/*     */     
/* 141 */     authCache.put(host, authScheme);
/*     */   }
/*     */   
/*     */   private void uncache(AuthCache authCache, HttpHost host, AuthScheme authScheme) {
/* 145 */     if (this.log.isDebugEnabled()) {
/* 146 */       this.log.debug("Removing from cache '" + authScheme.getSchemeName() + "' auth scheme for " + host);
/*     */     }
/*     */     
/* 149 */     authCache.remove(host);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/client/protocol/ResponseAuthCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
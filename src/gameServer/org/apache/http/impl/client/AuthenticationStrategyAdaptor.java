/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.auth.AuthOption;
/*     */ import org.apache.http.auth.AuthScheme;
/*     */ import org.apache.http.auth.AuthScope;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.client.AuthCache;
/*     */ import org.apache.http.client.AuthenticationHandler;
/*     */ import org.apache.http.client.AuthenticationStrategy;
/*     */ import org.apache.http.client.CredentialsProvider;
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
/*     */ @Deprecated
/*     */ @Immutable
/*     */ class AuthenticationStrategyAdaptor
/*     */   implements AuthenticationStrategy
/*     */ {
/*  63 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final AuthenticationHandler handler;
/*     */ 
/*     */   
/*     */   public AuthenticationStrategyAdaptor(AuthenticationHandler handler) {
/*  69 */     this.handler = handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAuthenticationRequested(HttpHost authhost, HttpResponse response, HttpContext context) {
/*  76 */     return this.handler.isAuthenticationRequested(response, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Header> getChallenges(HttpHost authhost, HttpResponse response, HttpContext context) throws MalformedChallengeException {
/*  83 */     return this.handler.getChallenges(response, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Queue<AuthOption> select(Map<String, Header> challenges, HttpHost authhost, HttpResponse response, HttpContext context) throws MalformedChallengeException {
/*     */     AuthScheme authScheme;
/*  91 */     Args.notNull(challenges, "Map of auth challenges");
/*  92 */     Args.notNull(authhost, "Host");
/*  93 */     Args.notNull(response, "HTTP response");
/*  94 */     Args.notNull(context, "HTTP context");
/*     */     
/*  96 */     Queue<AuthOption> options = new LinkedList<AuthOption>();
/*  97 */     CredentialsProvider credsProvider = (CredentialsProvider)context.getAttribute("http.auth.credentials-provider");
/*     */     
/*  99 */     if (credsProvider == null) {
/* 100 */       this.log.debug("Credentials provider not set in the context");
/* 101 */       return options;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 106 */       authScheme = this.handler.selectScheme(challenges, response, context);
/* 107 */     } catch (AuthenticationException ex) {
/* 108 */       if (this.log.isWarnEnabled()) {
/* 109 */         this.log.warn(ex.getMessage(), (Throwable)ex);
/*     */       }
/* 111 */       return options;
/*     */     } 
/* 113 */     String id = authScheme.getSchemeName();
/* 114 */     Header challenge = challenges.get(id.toLowerCase(Locale.ROOT));
/* 115 */     authScheme.processChallenge(challenge);
/*     */     
/* 117 */     AuthScope authScope = new AuthScope(authhost.getHostName(), authhost.getPort(), authScheme.getRealm(), authScheme.getSchemeName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     Credentials credentials = credsProvider.getCredentials(authScope);
/* 124 */     if (credentials != null) {
/* 125 */       options.add(new AuthOption(authScheme, credentials));
/*     */     }
/* 127 */     return options;
/*     */   }
/*     */ 
/*     */   
/*     */   public void authSucceeded(HttpHost authhost, AuthScheme authScheme, HttpContext context) {
/* 132 */     AuthCache authCache = (AuthCache)context.getAttribute("http.auth.auth-cache");
/* 133 */     if (isCachable(authScheme)) {
/* 134 */       if (authCache == null) {
/* 135 */         authCache = new BasicAuthCache();
/* 136 */         context.setAttribute("http.auth.auth-cache", authCache);
/*     */       } 
/* 138 */       if (this.log.isDebugEnabled()) {
/* 139 */         this.log.debug("Caching '" + authScheme.getSchemeName() + "' auth scheme for " + authhost);
/*     */       }
/*     */       
/* 142 */       authCache.put(authhost, authScheme);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void authFailed(HttpHost authhost, AuthScheme authScheme, HttpContext context) {
/* 148 */     AuthCache authCache = (AuthCache)context.getAttribute("http.auth.auth-cache");
/* 149 */     if (authCache == null) {
/*     */       return;
/*     */     }
/* 152 */     if (this.log.isDebugEnabled()) {
/* 153 */       this.log.debug("Removing from cache '" + authScheme.getSchemeName() + "' auth scheme for " + authhost);
/*     */     }
/*     */     
/* 156 */     authCache.remove(authhost);
/*     */   }
/*     */   
/*     */   private boolean isCachable(AuthScheme authScheme) {
/* 160 */     if (authScheme == null || !authScheme.isComplete()) {
/* 161 */       return false;
/*     */     }
/* 163 */     String schemeName = authScheme.getSchemeName();
/* 164 */     return (schemeName.equalsIgnoreCase("Basic") || schemeName.equalsIgnoreCase("Digest"));
/*     */   }
/*     */ 
/*     */   
/*     */   public AuthenticationHandler getHandler() {
/* 169 */     return this.handler;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/client/AuthenticationStrategyAdaptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package org.apache.http.nio.protocol;
/*     */ 
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.protocol.UriPatternMatcher;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ public class UriHttpAsyncRequestHandlerMapper
/*     */   implements HttpAsyncRequestHandlerMapper
/*     */ {
/*     */   private final UriPatternMatcher<HttpAsyncRequestHandler<?>> matcher;
/*     */   
/*     */   protected UriHttpAsyncRequestHandlerMapper(UriPatternMatcher<HttpAsyncRequestHandler<?>> matcher) {
/*  58 */     this.matcher = (UriPatternMatcher<HttpAsyncRequestHandler<?>>)Args.notNull(matcher, "Pattern matcher");
/*     */   }
/*     */   
/*     */   public UriHttpAsyncRequestHandlerMapper() {
/*  62 */     this(new UriPatternMatcher());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(String pattern, HttpAsyncRequestHandler<?> handler) {
/*  73 */     this.matcher.register(pattern, handler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(String pattern) {
/*  82 */     this.matcher.unregister(pattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getRequestPath(HttpRequest request) {
/*  89 */     String uriPath = request.getRequestLine().getUri();
/*  90 */     int index = uriPath.indexOf("?");
/*  91 */     if (index != -1) {
/*  92 */       uriPath = uriPath.substring(0, index);
/*     */     } else {
/*  94 */       index = uriPath.indexOf("#");
/*  95 */       if (index != -1) {
/*  96 */         uriPath = uriPath.substring(0, index);
/*     */       }
/*     */     } 
/*  99 */     return uriPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpAsyncRequestHandler<?> lookup(HttpRequest request) {
/* 110 */     return (HttpAsyncRequestHandler)this.matcher.lookup(getRequestPath(request));
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/protocol/UriHttpAsyncRequestHandlerMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
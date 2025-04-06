/*    */ package org.apache.http.nio.protocol;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.apache.http.annotation.ThreadSafe;
/*    */ import org.apache.http.protocol.UriPatternMatcher;
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
/*    */ @ThreadSafe
/*    */ public class HttpAsyncRequestHandlerRegistry
/*    */   implements HttpAsyncRequestHandlerResolver
/*    */ {
/* 59 */   private final UriPatternMatcher<HttpAsyncRequestHandler<?>> matcher = new UriPatternMatcher();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void register(String pattern, HttpAsyncRequestHandler<?> handler) {
/* 70 */     this.matcher.register(pattern, handler);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void unregister(String pattern) {
/* 79 */     this.matcher.unregister(pattern);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setHandlers(Map<String, HttpAsyncRequestHandler<?>> map) {
/* 87 */     this.matcher.setObjects(map);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, HttpAsyncRequestHandler<?>> getHandlers() {
/* 95 */     return this.matcher.getObjects();
/*    */   }
/*    */   
/*    */   public HttpAsyncRequestHandler<?> lookup(String requestURI) {
/* 99 */     return (HttpAsyncRequestHandler)this.matcher.lookup(requestURI);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/protocol/HttpAsyncRequestHandlerRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
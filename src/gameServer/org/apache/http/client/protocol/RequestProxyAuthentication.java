/*    */ package org.apache.http.client.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.auth.AuthState;
/*    */ import org.apache.http.conn.HttpRoutedConnection;
/*    */ import org.apache.http.conn.routing.HttpRoute;
/*    */ import org.apache.http.protocol.HttpContext;
/*    */ import org.apache.http.util.Args;
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
/*    */ @Immutable
/*    */ public class RequestProxyAuthentication
/*    */   extends RequestAuthenticationBase
/*    */ {
/*    */   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
/* 61 */     Args.notNull(request, "HTTP request");
/* 62 */     Args.notNull(context, "HTTP context");
/*    */     
/* 64 */     if (request.containsHeader("Proxy-Authorization")) {
/*    */       return;
/*    */     }
/*    */     
/* 68 */     HttpRoutedConnection conn = (HttpRoutedConnection)context.getAttribute("http.connection");
/*    */     
/* 70 */     if (conn == null) {
/* 71 */       this.log.debug("HTTP connection not set in the context");
/*    */       return;
/*    */     } 
/* 74 */     HttpRoute route = conn.getRoute();
/* 75 */     if (route.isTunnelled()) {
/*    */       return;
/*    */     }
/*    */ 
/*    */     
/* 80 */     AuthState authState = (AuthState)context.getAttribute("http.auth.proxy-scope");
/*    */     
/* 82 */     if (authState == null) {
/* 83 */       this.log.debug("Proxy auth state not set in the context");
/*    */       return;
/*    */     } 
/* 86 */     if (this.log.isDebugEnabled()) {
/* 87 */       this.log.debug("Proxy auth state: " + authState.getState());
/*    */     }
/* 89 */     process(authState, request, context);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/client/protocol/RequestProxyAuthentication.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
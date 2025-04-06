/*    */ package org.apache.http.impl.nio.client;
/*    */ 
/*    */ import java.security.Principal;
/*    */ import javax.net.ssl.SSLSession;
/*    */ import org.apache.http.HttpConnection;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.auth.AuthScheme;
/*    */ import org.apache.http.auth.AuthState;
/*    */ import org.apache.http.auth.Credentials;
/*    */ import org.apache.http.client.UserTokenHandler;
/*    */ import org.apache.http.client.protocol.HttpClientContext;
/*    */ import org.apache.http.nio.conn.ManagedNHttpClientConnection;
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
/*    */ @Immutable
/*    */ public class DefaultAsyncUserTokenHandler
/*    */   implements UserTokenHandler
/*    */ {
/* 61 */   public static final DefaultAsyncUserTokenHandler INSTANCE = new DefaultAsyncUserTokenHandler();
/*    */ 
/*    */   
/*    */   public Object getUserToken(HttpContext context) {
/* 65 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/*    */     
/* 67 */     Principal userPrincipal = null;
/*    */     
/* 69 */     AuthState targetAuthState = clientContext.getTargetAuthState();
/* 70 */     if (targetAuthState != null) {
/* 71 */       userPrincipal = getAuthPrincipal(targetAuthState);
/* 72 */       if (userPrincipal == null) {
/* 73 */         AuthState proxyAuthState = clientContext.getProxyAuthState();
/* 74 */         userPrincipal = getAuthPrincipal(proxyAuthState);
/*    */       } 
/*    */     } 
/*    */     
/* 78 */     if (userPrincipal == null) {
/* 79 */       HttpConnection conn = clientContext.getConnection();
/* 80 */       if (conn.isOpen() && conn instanceof ManagedNHttpClientConnection) {
/* 81 */         SSLSession sslsession = ((ManagedNHttpClientConnection)conn).getSSLSession();
/* 82 */         if (sslsession != null) {
/* 83 */           userPrincipal = sslsession.getLocalPrincipal();
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 88 */     return userPrincipal;
/*    */   }
/*    */   
/*    */   private static Principal getAuthPrincipal(AuthState authState) {
/* 92 */     AuthScheme scheme = authState.getAuthScheme();
/* 93 */     if (scheme != null && scheme.isComplete() && scheme.isConnectionBased()) {
/* 94 */       Credentials creds = authState.getCredentials();
/* 95 */       if (creds != null) {
/* 96 */         return creds.getUserPrincipal();
/*    */       }
/*    */     } 
/* 99 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/DefaultAsyncUserTokenHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
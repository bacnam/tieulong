/*    */ package org.apache.mina.proxy.handlers.http;
/*    */ 
/*    */ import org.apache.mina.proxy.ProxyAuthException;
/*    */ import org.apache.mina.proxy.handlers.http.basic.HttpBasicAuthLogicHandler;
/*    */ import org.apache.mina.proxy.handlers.http.basic.HttpNoAuthLogicHandler;
/*    */ import org.apache.mina.proxy.handlers.http.digest.HttpDigestAuthLogicHandler;
/*    */ import org.apache.mina.proxy.handlers.http.ntlm.HttpNTLMAuthLogicHandler;
/*    */ import org.apache.mina.proxy.session.ProxyIoSession;
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
/*    */ public enum HttpAuthenticationMethods
/*    */ {
/* 37 */   NO_AUTH(1), BASIC(2), NTLM(3), DIGEST(4);
/*    */   
/*    */   private final int id;
/*    */   
/*    */   HttpAuthenticationMethods(int id) {
/* 42 */     this.id = id;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getId() {
/* 50 */     return this.id;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AbstractAuthLogicHandler getNewHandler(ProxyIoSession proxyIoSession) throws ProxyAuthException {
/* 60 */     return getNewHandler(this.id, proxyIoSession);
/*    */   }
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
/*    */   public static AbstractAuthLogicHandler getNewHandler(int method, ProxyIoSession proxyIoSession) throws ProxyAuthException {
/* 73 */     if (method == BASIC.id)
/* 74 */       return (AbstractAuthLogicHandler)new HttpBasicAuthLogicHandler(proxyIoSession); 
/* 75 */     if (method == DIGEST.id)
/* 76 */       return (AbstractAuthLogicHandler)new HttpDigestAuthLogicHandler(proxyIoSession); 
/* 77 */     if (method == NTLM.id)
/* 78 */       return (AbstractAuthLogicHandler)new HttpNTLMAuthLogicHandler(proxyIoSession); 
/* 79 */     if (method == NO_AUTH.id) {
/* 80 */       return (AbstractAuthLogicHandler)new HttpNoAuthLogicHandler(proxyIoSession);
/*    */     }
/* 82 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/handlers/http/HttpAuthenticationMethods.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
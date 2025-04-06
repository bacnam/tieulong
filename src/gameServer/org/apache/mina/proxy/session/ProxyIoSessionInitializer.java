/*    */ package org.apache.mina.proxy.session;
/*    */ 
/*    */ import org.apache.mina.core.future.ConnectFuture;
/*    */ import org.apache.mina.core.future.IoFuture;
/*    */ import org.apache.mina.core.session.IoSession;
/*    */ import org.apache.mina.core.session.IoSessionInitializer;
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
/*    */ public class ProxyIoSessionInitializer<T extends ConnectFuture>
/*    */   implements IoSessionInitializer<T>
/*    */ {
/*    */   private final IoSessionInitializer<T> wrappedSessionInitializer;
/*    */   private final ProxyIoSession proxyIoSession;
/*    */   
/*    */   public ProxyIoSessionInitializer(IoSessionInitializer<T> wrappedSessionInitializer, ProxyIoSession proxyIoSession) {
/* 41 */     this.wrappedSessionInitializer = wrappedSessionInitializer;
/* 42 */     this.proxyIoSession = proxyIoSession;
/*    */   }
/*    */   
/*    */   public ProxyIoSession getProxySession() {
/* 46 */     return this.proxyIoSession;
/*    */   }
/*    */   
/*    */   public void initializeSession(IoSession session, T future) {
/* 50 */     if (this.wrappedSessionInitializer != null) {
/* 51 */       this.wrappedSessionInitializer.initializeSession(session, (IoFuture)future);
/*    */     }
/*    */     
/* 54 */     if (this.proxyIoSession != null) {
/* 55 */       this.proxyIoSession.setSession(session);
/* 56 */       session.setAttribute(ProxyIoSession.PROXY_SESSION, this.proxyIoSession);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/session/ProxyIoSessionInitializer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
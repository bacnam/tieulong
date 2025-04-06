/*    */ package org.apache.mina.proxy;
/*    */ 
/*    */ import org.apache.mina.core.service.IoHandlerAdapter;
/*    */ import org.apache.mina.core.session.IoSession;
/*    */ import org.apache.mina.proxy.session.ProxyIoSession;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ public abstract class AbstractProxyIoHandler
/*    */   extends IoHandlerAdapter
/*    */ {
/* 37 */   private static final Logger logger = LoggerFactory.getLogger(AbstractProxyIoHandler.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract void proxySessionOpened(IoSession paramIoSession) throws Exception;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void sessionOpened(IoSession session) throws Exception {
/* 53 */     ProxyIoSession proxyIoSession = (ProxyIoSession)session.getAttribute(ProxyIoSession.PROXY_SESSION);
/*    */     
/* 55 */     if (proxyIoSession.getRequest() instanceof org.apache.mina.proxy.handlers.socks.SocksProxyRequest || proxyIoSession.isAuthenticationFailed() || proxyIoSession.getHandler().isHandshakeComplete()) {
/*    */       
/* 57 */       proxySessionOpened(session);
/*    */     } else {
/* 59 */       logger.debug("Filtered session opened event !");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/AbstractProxyIoHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
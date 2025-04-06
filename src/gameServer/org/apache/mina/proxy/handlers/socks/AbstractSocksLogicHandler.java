/*    */ package org.apache.mina.proxy.handlers.socks;
/*    */ 
/*    */ import org.apache.mina.proxy.AbstractProxyLogicHandler;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractSocksLogicHandler
/*    */   extends AbstractProxyLogicHandler
/*    */ {
/*    */   protected final SocksProxyRequest request;
/*    */   
/*    */   public AbstractSocksLogicHandler(ProxyIoSession proxyIoSession) {
/* 45 */     super(proxyIoSession);
/* 46 */     this.request = (SocksProxyRequest)proxyIoSession.getRequest();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/handlers/socks/AbstractSocksLogicHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
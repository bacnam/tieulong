/*    */ package org.apache.mina.proxy.handlers.http.basic;
/*    */ 
/*    */ import org.apache.mina.core.filterchain.IoFilter;
/*    */ import org.apache.mina.proxy.ProxyAuthException;
/*    */ import org.apache.mina.proxy.handlers.http.AbstractAuthLogicHandler;
/*    */ import org.apache.mina.proxy.handlers.http.HttpProxyRequest;
/*    */ import org.apache.mina.proxy.handlers.http.HttpProxyResponse;
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
/*    */ public class HttpNoAuthLogicHandler
/*    */   extends AbstractAuthLogicHandler
/*    */ {
/* 38 */   private static final Logger logger = LoggerFactory.getLogger(HttpNoAuthLogicHandler.class);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpNoAuthLogicHandler(ProxyIoSession proxyIoSession) throws ProxyAuthException {
/* 44 */     super(proxyIoSession);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void doHandshake(IoFilter.NextFilter nextFilter) throws ProxyAuthException {
/* 52 */     logger.debug(" doHandshake()");
/*    */ 
/*    */     
/* 55 */     writeRequest(nextFilter, (HttpProxyRequest)this.request);
/* 56 */     this.step++;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handleResponse(HttpProxyResponse response) throws ProxyAuthException {
/* 65 */     throw new ProxyAuthException("Received error response code (" + response.getStatusLine() + ").");
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/handlers/http/basic/HttpNoAuthLogicHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
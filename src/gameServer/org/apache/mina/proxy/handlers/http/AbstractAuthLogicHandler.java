/*     */ package org.apache.mina.proxy.handlers.http;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.proxy.ProxyAuthException;
/*     */ import org.apache.mina.proxy.handlers.ProxyRequest;
/*     */ import org.apache.mina.proxy.session.ProxyIoSession;
/*     */ import org.apache.mina.proxy.utils.StringUtilities;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public abstract class AbstractAuthLogicHandler
/*     */ {
/*  41 */   private static final Logger logger = LoggerFactory.getLogger(AbstractAuthLogicHandler.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ProxyRequest request;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ProxyIoSession proxyIoSession;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   protected int step = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractAuthLogicHandler(ProxyIoSession proxyIoSession) throws ProxyAuthException {
/*  65 */     this.proxyIoSession = proxyIoSession;
/*  66 */     this.request = proxyIoSession.getRequest();
/*     */     
/*  68 */     if (this.request == null || !(this.request instanceof HttpProxyRequest)) {
/*  69 */       throw new IllegalArgumentException("request parameter should be a non null HttpProxyRequest instance");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void doHandshake(IoFilter.NextFilter paramNextFilter) throws ProxyAuthException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void handleResponse(HttpProxyResponse paramHttpProxyResponse) throws ProxyAuthException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeRequest(IoFilter.NextFilter nextFilter, HttpProxyRequest request) throws ProxyAuthException {
/*  97 */     logger.debug("  sending HTTP request");
/*     */     
/*  99 */     ((AbstractHttpLogicHandler)this.proxyIoSession.getHandler()).writeRequest(nextFilter, request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addKeepAliveHeaders(Map<String, List<String>> headers) {
/* 108 */     StringUtilities.addValueToHeader(headers, "Keep-Alive", "300", true);
/* 109 */     StringUtilities.addValueToHeader(headers, "Proxy-Connection", "keep-Alive", true);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/handlers/http/AbstractAuthLogicHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
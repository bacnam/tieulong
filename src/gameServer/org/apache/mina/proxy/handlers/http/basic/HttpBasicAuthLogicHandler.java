/*     */ package org.apache.mina.proxy.handlers.http.basic;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.proxy.ProxyAuthException;
/*     */ import org.apache.mina.proxy.handlers.http.AbstractAuthLogicHandler;
/*     */ import org.apache.mina.proxy.handlers.http.HttpProxyRequest;
/*     */ import org.apache.mina.proxy.handlers.http.HttpProxyResponse;
/*     */ import org.apache.mina.proxy.session.ProxyIoSession;
/*     */ import org.apache.mina.proxy.utils.StringUtilities;
/*     */ import org.apache.mina.util.Base64;
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
/*     */ public class HttpBasicAuthLogicHandler
/*     */   extends AbstractAuthLogicHandler
/*     */ {
/*  45 */   private static final Logger logger = LoggerFactory.getLogger(HttpBasicAuthLogicHandler.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpBasicAuthLogicHandler(ProxyIoSession proxyIoSession) throws ProxyAuthException {
/*  51 */     super(proxyIoSession);
/*     */     
/*  53 */     ((HttpProxyRequest)this.request).checkRequiredProperties(new String[] { "USER", "PWD" });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doHandshake(IoFilter.NextFilter nextFilter) throws ProxyAuthException {
/*  62 */     logger.debug(" doHandshake()");
/*     */     
/*  64 */     if (this.step > 0) {
/*  65 */       throw new ProxyAuthException("Authentication request already sent");
/*     */     }
/*     */ 
/*     */     
/*  69 */     HttpProxyRequest req = (HttpProxyRequest)this.request;
/*  70 */     Map<String, List<String>> headers = (req.getHeaders() != null) ? req.getHeaders() : new HashMap<String, List<String>>();
/*     */ 
/*     */     
/*  73 */     String username = (String)req.getProperties().get("USER");
/*  74 */     String password = (String)req.getProperties().get("PWD");
/*     */     
/*  76 */     StringUtilities.addValueToHeader(headers, "Proxy-Authorization", "Basic " + createAuthorization(username, password), true);
/*     */ 
/*     */     
/*  79 */     addKeepAliveHeaders(headers);
/*  80 */     req.setHeaders(headers);
/*     */     
/*  82 */     writeRequest(nextFilter, req);
/*  83 */     this.step++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String createAuthorization(String username, String password) {
/*  94 */     return new String(Base64.encodeBase64((username + ":" + password).getBytes()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleResponse(HttpProxyResponse response) throws ProxyAuthException {
/* 102 */     if (response.getStatusCode() != 407)
/* 103 */       throw new ProxyAuthException("Received error response code (" + response.getStatusLine() + ")."); 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/handlers/http/basic/HttpBasicAuthLogicHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
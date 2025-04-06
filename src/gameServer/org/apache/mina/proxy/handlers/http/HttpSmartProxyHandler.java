/*     */ package org.apache.mina.proxy.handlers.http;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.proxy.ProxyAuthException;
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
/*     */ public class HttpSmartProxyHandler
/*     */   extends AbstractHttpLogicHandler
/*     */ {
/*  41 */   private static final Logger logger = LoggerFactory.getLogger(HttpSmartProxyHandler.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean requestSent = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private AbstractAuthLogicHandler authHandler;
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpSmartProxyHandler(ProxyIoSession proxyIoSession) {
/*  54 */     super(proxyIoSession);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doHandshake(IoFilter.NextFilter nextFilter) throws ProxyAuthException {
/*  63 */     logger.debug(" doHandshake()");
/*     */     
/*  65 */     if (this.authHandler != null) {
/*  66 */       this.authHandler.doHandshake(nextFilter);
/*     */     } else {
/*  68 */       if (this.requestSent)
/*     */       {
/*  70 */         throw new ProxyAuthException("Authentication request already sent");
/*     */       }
/*     */       
/*  73 */       logger.debug("  sending HTTP request");
/*     */ 
/*     */       
/*  76 */       HttpProxyRequest req = (HttpProxyRequest)getProxyIoSession().getRequest();
/*  77 */       Map<String, List<String>> headers = (req.getHeaders() != null) ? req.getHeaders() : new HashMap<String, List<String>>();
/*     */ 
/*     */       
/*  80 */       AbstractAuthLogicHandler.addKeepAliveHeaders(headers);
/*  81 */       req.setHeaders(headers);
/*     */ 
/*     */       
/*  84 */       writeRequest(nextFilter, req);
/*  85 */       this.requestSent = true;
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
/*     */   
/*     */   private void autoSelectAuthHandler(HttpProxyResponse response) throws ProxyAuthException {
/*  98 */     List<String> values = response.getHeaders().get("Proxy-Authenticate");
/*  99 */     ProxyIoSession proxyIoSession = getProxyIoSession();
/*     */     
/* 101 */     if (values == null || values.size() == 0) {
/* 102 */       this.authHandler = HttpAuthenticationMethods.NO_AUTH.getNewHandler(proxyIoSession);
/*     */     }
/* 104 */     else if (getProxyIoSession().getPreferedOrder() == null) {
/*     */       
/* 106 */       int method = -1;
/*     */ 
/*     */ 
/*     */       
/* 110 */       for (String proxyAuthHeader : values) {
/* 111 */         proxyAuthHeader = proxyAuthHeader.toLowerCase();
/*     */         
/* 113 */         if (proxyAuthHeader.contains("ntlm")) {
/* 114 */           method = HttpAuthenticationMethods.NTLM.getId(); break;
/*     */         } 
/* 116 */         if (proxyAuthHeader.contains("digest") && method != HttpAuthenticationMethods.NTLM.getId()) {
/* 117 */           method = HttpAuthenticationMethods.DIGEST.getId(); continue;
/* 118 */         }  if (proxyAuthHeader.contains("basic") && method == -1) {
/* 119 */           method = HttpAuthenticationMethods.BASIC.getId();
/*     */         }
/*     */       } 
/*     */       
/* 123 */       if (method != -1) {
/*     */         try {
/* 125 */           this.authHandler = HttpAuthenticationMethods.getNewHandler(method, proxyIoSession);
/* 126 */         } catch (Exception ex) {
/* 127 */           logger.debug("Following exception occured:", ex);
/*     */         } 
/*     */       }
/*     */       
/* 131 */       if (this.authHandler == null) {
/* 132 */         this.authHandler = HttpAuthenticationMethods.NO_AUTH.getNewHandler(proxyIoSession);
/*     */       }
/*     */     } else {
/*     */       
/* 136 */       for (HttpAuthenticationMethods method : proxyIoSession.getPreferedOrder()) {
/* 137 */         if (this.authHandler != null) {
/*     */           break;
/*     */         }
/*     */         
/* 141 */         if (method == HttpAuthenticationMethods.NO_AUTH) {
/* 142 */           this.authHandler = HttpAuthenticationMethods.NO_AUTH.getNewHandler(proxyIoSession);
/*     */           
/*     */           break;
/*     */         } 
/* 146 */         for (String proxyAuthHeader : values) {
/* 147 */           proxyAuthHeader = proxyAuthHeader.toLowerCase();
/*     */ 
/*     */           
/*     */           try {
/* 151 */             if (proxyAuthHeader.contains("basic") && method == HttpAuthenticationMethods.BASIC) {
/* 152 */               this.authHandler = HttpAuthenticationMethods.BASIC.getNewHandler(proxyIoSession); break;
/*     */             } 
/* 154 */             if (proxyAuthHeader.contains("digest") && method == HttpAuthenticationMethods.DIGEST) {
/* 155 */               this.authHandler = HttpAuthenticationMethods.DIGEST.getNewHandler(proxyIoSession); break;
/*     */             } 
/* 157 */             if (proxyAuthHeader.contains("ntlm") && method == HttpAuthenticationMethods.NTLM) {
/* 158 */               this.authHandler = HttpAuthenticationMethods.NTLM.getNewHandler(proxyIoSession);
/*     */               break;
/*     */             } 
/* 161 */           } catch (Exception ex) {
/* 162 */             logger.debug("Following exception occured:", ex);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 169 */     if (this.authHandler == null) {
/* 170 */       throw new ProxyAuthException("Unknown authentication mechanism(s): " + values);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleResponse(HttpProxyResponse response) throws ProxyAuthException {
/* 181 */     if (!isHandshakeComplete() && ("close".equalsIgnoreCase(StringUtilities.getSingleValuedHeader(response.getHeaders(), "Proxy-Connection")) || "close".equalsIgnoreCase(StringUtilities.getSingleValuedHeader(response.getHeaders(), "Connection"))))
/*     */     {
/*     */ 
/*     */       
/* 185 */       getProxyIoSession().setReconnectionNeeded(true);
/*     */     }
/*     */     
/* 188 */     if (response.getStatusCode() == 407) {
/* 189 */       if (this.authHandler == null) {
/* 190 */         autoSelectAuthHandler(response);
/*     */       }
/* 192 */       this.authHandler.handleResponse(response);
/*     */     } else {
/* 194 */       throw new ProxyAuthException("Error: unexpected response code " + response.getStatusLine() + " received from proxy.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/handlers/http/HttpSmartProxyHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
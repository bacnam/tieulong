/*     */ package org.apache.mina.proxy.handlers.http.ntlm;
/*     */ 
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ public class HttpNTLMAuthLogicHandler
/*     */   extends AbstractAuthLogicHandler
/*     */ {
/*  47 */   private static final Logger LOGGER = LoggerFactory.getLogger(HttpNTLMAuthLogicHandler.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   private byte[] challengePacket = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpNTLMAuthLogicHandler(ProxyIoSession proxyIoSession) throws ProxyAuthException {
/*  58 */     super(proxyIoSession);
/*     */     
/*  60 */     ((HttpProxyRequest)this.request).checkRequiredProperties(new String[] { "USER", "PWD", "DOMAIN", "WORKSTATION" });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doHandshake(IoFilter.NextFilter nextFilter) throws ProxyAuthException {
/*  70 */     LOGGER.debug(" doHandshake()");
/*     */     
/*  72 */     if (this.step > 0 && this.challengePacket == null) {
/*  73 */       throw new IllegalStateException("NTLM Challenge packet not received");
/*     */     }
/*     */     
/*  76 */     HttpProxyRequest req = (HttpProxyRequest)this.request;
/*  77 */     Map<String, List<String>> headers = (req.getHeaders() != null) ? req.getHeaders() : new HashMap<String, List<String>>();
/*     */ 
/*     */     
/*  80 */     String domain = (String)req.getProperties().get("DOMAIN");
/*  81 */     String workstation = (String)req.getProperties().get("WORKSTATION");
/*     */     
/*  83 */     if (this.step > 0) {
/*  84 */       LOGGER.debug("  sending NTLM challenge response");
/*     */       
/*  86 */       byte[] challenge = NTLMUtilities.extractChallengeFromType2Message(this.challengePacket);
/*  87 */       int serverFlags = NTLMUtilities.extractFlagsFromType2Message(this.challengePacket);
/*     */       
/*  89 */       String username = (String)req.getProperties().get("USER");
/*  90 */       String password = (String)req.getProperties().get("PWD");
/*     */       
/*  92 */       byte[] authenticationPacket = NTLMUtilities.createType3Message(username, password, challenge, domain, workstation, Integer.valueOf(serverFlags), null);
/*     */ 
/*     */       
/*  95 */       StringUtilities.addValueToHeader(headers, "Proxy-Authorization", "NTLM " + new String(Base64.encodeBase64(authenticationPacket)), true);
/*     */     }
/*     */     else {
/*     */       
/*  99 */       LOGGER.debug("  sending NTLM negotiation packet");
/*     */       
/* 101 */       byte[] negotiationPacket = NTLMUtilities.createType1Message(workstation, domain, null, null);
/* 102 */       StringUtilities.addValueToHeader(headers, "Proxy-Authorization", "NTLM " + new String(Base64.encodeBase64(negotiationPacket)), true);
/*     */     } 
/*     */ 
/*     */     
/* 106 */     addKeepAliveHeaders(headers);
/* 107 */     req.setHeaders(headers);
/*     */     
/* 109 */     writeRequest(nextFilter, req);
/* 110 */     this.step++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getNTLMHeader(HttpProxyResponse response) {
/* 119 */     List<String> values = (List<String>)response.getHeaders().get("Proxy-Authenticate");
/*     */     
/* 121 */     for (String s : values) {
/* 122 */       if (s.startsWith("NTLM")) {
/* 123 */         return s;
/*     */       }
/*     */     } 
/*     */     
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleResponse(HttpProxyResponse response) throws ProxyAuthException {
/* 135 */     if (this.step == 0) {
/* 136 */       String challengeResponse = getNTLMHeader(response);
/* 137 */       this.step = 1;
/*     */       
/* 139 */       if (challengeResponse == null || challengeResponse.length() < 5) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 148 */     if (this.step == 1) {
/*     */ 
/*     */       
/* 151 */       String challengeResponse = getNTLMHeader(response);
/*     */       
/* 153 */       if (challengeResponse == null || challengeResponse.length() < 5) {
/* 154 */         throw new ProxyAuthException("Unexpected error while reading server challenge !");
/*     */       }
/*     */       
/*     */       try {
/* 158 */         this.challengePacket = Base64.decodeBase64(challengeResponse.substring(5).getBytes(this.proxyIoSession.getCharsetName()));
/*     */       }
/* 160 */       catch (IOException e) {
/* 161 */         throw new ProxyAuthException("Unable to decode the base64 encoded NTLM challenge", e);
/*     */       } 
/* 163 */       this.step = 2;
/*     */     } else {
/* 165 */       throw new ProxyAuthException("Received unexpected response code (" + response.getStatusLine() + ").");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/handlers/http/ntlm/HttpNTLMAuthLogicHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package org.apache.mina.proxy.handlers.http.digest;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
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
/*     */ public class HttpDigestAuthLogicHandler
/*     */   extends AbstractAuthLogicHandler
/*     */ {
/*  51 */   private static final Logger logger = LoggerFactory.getLogger(HttpDigestAuthLogicHandler.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   private HashMap<String, String> directives = null;
/*     */ 
/*     */   
/*     */   private HttpProxyResponse response;
/*     */ 
/*     */   
/*     */   private static SecureRandom rnd;
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  68 */       rnd = SecureRandom.getInstance("SHA1PRNG");
/*  69 */     } catch (NoSuchAlgorithmException e) {
/*  70 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public HttpDigestAuthLogicHandler(ProxyIoSession proxyIoSession) throws ProxyAuthException {
/*  75 */     super(proxyIoSession);
/*     */     
/*  77 */     ((HttpProxyRequest)this.request).checkRequiredProperties(new String[] { "USER", "PWD" });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doHandshake(IoFilter.NextFilter nextFilter) throws ProxyAuthException {
/*  83 */     logger.debug(" doHandshake()");
/*     */     
/*  85 */     if (this.step > 0 && this.directives == null) {
/*  86 */       throw new ProxyAuthException("Authentication challenge not received");
/*     */     }
/*     */     
/*  89 */     HttpProxyRequest req = (HttpProxyRequest)this.request;
/*  90 */     Map<String, List<String>> headers = (req.getHeaders() != null) ? req.getHeaders() : new HashMap<String, List<String>>();
/*     */ 
/*     */     
/*  93 */     if (this.step > 0) {
/*  94 */       logger.debug("  sending DIGEST challenge response");
/*     */ 
/*     */       
/*  97 */       HashMap<String, String> map = new HashMap<String, String>();
/*  98 */       map.put("username", (String)req.getProperties().get("USER"));
/*  99 */       StringUtilities.copyDirective(this.directives, map, "realm");
/* 100 */       StringUtilities.copyDirective(this.directives, map, "uri");
/* 101 */       StringUtilities.copyDirective(this.directives, map, "opaque");
/* 102 */       StringUtilities.copyDirective(this.directives, map, "nonce");
/* 103 */       String algorithm = StringUtilities.copyDirective(this.directives, map, "algorithm");
/*     */ 
/*     */       
/* 106 */       if (algorithm != null && !"md5".equalsIgnoreCase(algorithm) && !"md5-sess".equalsIgnoreCase(algorithm)) {
/* 107 */         throw new ProxyAuthException("Unknown algorithm required by server");
/*     */       }
/*     */ 
/*     */       
/* 111 */       String qop = this.directives.get("qop");
/* 112 */       if (qop != null) {
/* 113 */         StringTokenizer st = new StringTokenizer(qop, ",");
/* 114 */         String token = null;
/*     */         
/* 116 */         while (st.hasMoreTokens()) {
/* 117 */           String tk = st.nextToken();
/* 118 */           if ("auth".equalsIgnoreCase(token)) {
/*     */             break;
/*     */           }
/*     */           
/* 122 */           int pos = Arrays.binarySearch((Object[])DigestUtilities.SUPPORTED_QOPS, tk);
/* 123 */           if (pos > -1) {
/* 124 */             token = tk;
/*     */           }
/*     */         } 
/*     */         
/* 128 */         if (token != null) {
/* 129 */           map.put("qop", token);
/*     */           
/* 131 */           byte[] nonce = new byte[8];
/* 132 */           rnd.nextBytes(nonce);
/*     */           
/*     */           try {
/* 135 */             String cnonce = new String(Base64.encodeBase64(nonce), this.proxyIoSession.getCharsetName());
/* 136 */             map.put("cnonce", cnonce);
/* 137 */           } catch (UnsupportedEncodingException e) {
/* 138 */             throw new ProxyAuthException("Unable to encode cnonce", e);
/*     */           } 
/*     */         } else {
/* 141 */           throw new ProxyAuthException("No supported qop option available");
/*     */         } 
/*     */       } 
/*     */       
/* 145 */       map.put("nc", "00000001");
/* 146 */       map.put("uri", req.getHttpURI());
/*     */ 
/*     */       
/*     */       try {
/* 150 */         map.put("response", DigestUtilities.computeResponseValue(this.proxyIoSession.getSession(), map, req.getHttpVerb().toUpperCase(), (String)req.getProperties().get("PWD"), this.proxyIoSession.getCharsetName(), this.response.getBody()));
/*     */ 
/*     */       
/*     */       }
/* 154 */       catch (Exception e) {
/* 155 */         throw new ProxyAuthException("Digest response computing failed", e);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 160 */       StringBuilder sb = new StringBuilder("Digest ");
/* 161 */       boolean addSeparator = false;
/*     */       
/* 163 */       for (String key : map.keySet()) {
/*     */         
/* 165 */         if (addSeparator) {
/* 166 */           sb.append(", ");
/*     */         } else {
/* 168 */           addSeparator = true;
/*     */         } 
/*     */         
/* 171 */         boolean quotedValue = (!"qop".equals(key) && !"nc".equals(key));
/* 172 */         sb.append(key);
/* 173 */         if (quotedValue) {
/* 174 */           sb.append("=\"").append(map.get(key)).append('"'); continue;
/*     */         } 
/* 176 */         sb.append('=').append(map.get(key));
/*     */       } 
/*     */ 
/*     */       
/* 180 */       StringUtilities.addValueToHeader(headers, "Proxy-Authorization", sb.toString(), true);
/*     */     } 
/*     */     
/* 183 */     addKeepAliveHeaders(headers);
/* 184 */     req.setHeaders(headers);
/*     */     
/* 186 */     writeRequest(nextFilter, req);
/* 187 */     this.step++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleResponse(HttpProxyResponse response) throws ProxyAuthException {
/* 192 */     this.response = response;
/*     */     
/* 194 */     if (this.step == 0) {
/* 195 */       if (response.getStatusCode() != 401 && response.getStatusCode() != 407) {
/* 196 */         throw new ProxyAuthException("Received unexpected response code (" + response.getStatusLine() + ").");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 201 */       List<String> values = (List<String>)response.getHeaders().get("Proxy-Authenticate");
/* 202 */       String challengeResponse = null;
/*     */       
/* 204 */       for (String s : values) {
/* 205 */         if (s.startsWith("Digest")) {
/* 206 */           challengeResponse = s;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 211 */       if (challengeResponse == null) {
/* 212 */         throw new ProxyAuthException("Server doesn't support digest authentication method !");
/*     */       }
/*     */       
/*     */       try {
/* 216 */         this.directives = StringUtilities.parseDirectives(challengeResponse.substring(7).getBytes(this.proxyIoSession.getCharsetName()));
/*     */       }
/* 218 */       catch (Exception e) {
/* 219 */         throw new ProxyAuthException("Parsing of server digest directives failed", e);
/*     */       } 
/* 221 */       this.step = 1;
/*     */     } else {
/* 223 */       throw new ProxyAuthException("Received unexpected response code (" + response.getStatusLine() + ").");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/handlers/http/digest/HttpDigestAuthLogicHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
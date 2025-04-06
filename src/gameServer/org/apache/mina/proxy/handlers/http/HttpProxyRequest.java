/*     */ package org.apache.mina.proxy.handlers.http;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.mina.proxy.ProxyAuthException;
/*     */ import org.apache.mina.proxy.handlers.ProxyRequest;
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
/*     */ public class HttpProxyRequest
/*     */   extends ProxyRequest
/*     */ {
/*  40 */   private static final Logger logger = LoggerFactory.getLogger(HttpProxyRequest.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String httpVerb;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String httpURI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String httpVersion;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String host;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, List<String>> headers;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient Map<String, String> properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpProxyRequest(InetSocketAddress endpointAddress) {
/*  80 */     this(endpointAddress, "HTTP/1.0", (Map<String, List<String>>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpProxyRequest(InetSocketAddress endpointAddress, String httpVersion) {
/*  91 */     this(endpointAddress, httpVersion, (Map<String, List<String>>)null);
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
/*     */   
/*     */   public HttpProxyRequest(InetSocketAddress endpointAddress, String httpVersion, Map<String, List<String>> headers) {
/* 104 */     this.httpVerb = "CONNECT";
/* 105 */     if (!endpointAddress.isUnresolved()) {
/* 106 */       this.httpURI = endpointAddress.getHostName() + ":" + endpointAddress.getPort();
/*     */     } else {
/* 108 */       this.httpURI = endpointAddress.getAddress().getHostAddress() + ":" + endpointAddress.getPort();
/*     */     } 
/*     */     
/* 111 */     this.httpVersion = httpVersion;
/* 112 */     this.headers = headers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpProxyRequest(String httpURI) {
/* 122 */     this("GET", httpURI, "HTTP/1.0", null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpProxyRequest(String httpURI, String httpVersion) {
/* 133 */     this("GET", httpURI, httpVersion, null);
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
/*     */   public HttpProxyRequest(String httpVerb, String httpURI, String httpVersion) {
/* 145 */     this(httpVerb, httpURI, httpVersion, null);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpProxyRequest(String httpVerb, String httpURI, String httpVersion, Map<String, List<String>> headers) {
/* 160 */     this.httpVerb = httpVerb;
/* 161 */     this.httpURI = httpURI;
/* 162 */     this.httpVersion = httpVersion;
/* 163 */     this.headers = headers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getHttpVerb() {
/* 170 */     return this.httpVerb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHttpVersion() {
/* 177 */     return this.httpVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHttpVersion(String httpVersion) {
/* 186 */     this.httpVersion = httpVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized String getHost() {
/* 193 */     if (this.host == null) {
/* 194 */       if (getEndpointAddress() != null && !getEndpointAddress().isUnresolved()) {
/* 195 */         this.host = getEndpointAddress().getHostName();
/*     */       }
/*     */       
/* 198 */       if (this.host == null && this.httpURI != null) {
/*     */         try {
/* 200 */           this.host = (new URL(this.httpURI)).getHost();
/* 201 */         } catch (MalformedURLException e) {
/* 202 */           logger.debug("Malformed URL", e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 207 */     return this.host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getHttpURI() {
/* 214 */     return this.httpURI;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Map<String, List<String>> getHeaders() {
/* 221 */     return this.headers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setHeaders(Map<String, List<String>> headers) {
/* 228 */     this.headers = headers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> getProperties() {
/* 235 */     return this.properties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperties(Map<String, String> properties) {
/* 242 */     this.properties = properties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkRequiredProperties(String... propNames) throws ProxyAuthException {
/* 250 */     StringBuilder sb = new StringBuilder();
/* 251 */     for (String propertyName : propNames) {
/* 252 */       if (this.properties.get(propertyName) == null) {
/* 253 */         sb.append(propertyName).append(' ');
/*     */       }
/*     */     } 
/* 256 */     if (sb.length() > 0) {
/* 257 */       sb.append("property(ies) missing in request");
/* 258 */       throw new ProxyAuthException(sb.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toHttpString() {
/* 266 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 268 */     sb.append(getHttpVerb()).append(' ').append(getHttpURI()).append(' ').append(getHttpVersion()).append("\r\n");
/*     */ 
/*     */     
/* 271 */     boolean hostHeaderFound = false;
/*     */     
/* 273 */     if (getHeaders() != null) {
/* 274 */       for (Map.Entry<String, List<String>> header : getHeaders().entrySet()) {
/* 275 */         if (!hostHeaderFound) {
/* 276 */           hostHeaderFound = ((String)header.getKey()).equalsIgnoreCase("host");
/*     */         }
/*     */         
/* 279 */         for (String value : header.getValue()) {
/* 280 */           sb.append(header.getKey()).append(": ").append(value).append("\r\n");
/*     */         }
/*     */       } 
/*     */       
/* 284 */       if (!hostHeaderFound && getHttpVersion() == "HTTP/1.1") {
/* 285 */         sb.append("Host: ").append(getHost()).append("\r\n");
/*     */       }
/*     */     } 
/*     */     
/* 289 */     sb.append("\r\n");
/*     */     
/* 291 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/handlers/http/HttpProxyRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
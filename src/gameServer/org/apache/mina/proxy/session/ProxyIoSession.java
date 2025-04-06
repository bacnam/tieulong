/*     */ package org.apache.mina.proxy.session;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.List;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.proxy.ProxyConnector;
/*     */ import org.apache.mina.proxy.ProxyLogicHandler;
/*     */ import org.apache.mina.proxy.event.IoSessionEventQueue;
/*     */ import org.apache.mina.proxy.filter.ProxyFilter;
/*     */ import org.apache.mina.proxy.handlers.ProxyRequest;
/*     */ import org.apache.mina.proxy.handlers.http.HttpAuthenticationMethods;
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
/*     */ 
/*     */ 
/*     */ public class ProxyIoSession
/*     */ {
/*  44 */   public static final String PROXY_SESSION = ProxyConnector.class.getName() + ".ProxySession";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String DEFAULT_ENCODING = "ISO-8859-1";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<HttpAuthenticationMethods> preferedOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ProxyRequest request;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ProxyLogicHandler handler;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ProxyFilter proxyFilter;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private IoSession session;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ProxyConnector connector;
/*     */ 
/*     */ 
/*     */   
/*  83 */   private InetSocketAddress proxyAddress = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean reconnectionNeeded = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String charsetName;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   private IoSessionEventQueue eventQueue = new IoSessionEventQueue(this);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean authenticationFailed;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxyIoSession(InetSocketAddress proxyAddress, ProxyRequest request) {
/* 114 */     setProxyAddress(proxyAddress);
/* 115 */     setRequest(request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoSessionEventQueue getEventQueue() {
/* 122 */     return this.eventQueue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<HttpAuthenticationMethods> getPreferedOrder() {
/* 132 */     return this.preferedOrder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreferedOrder(List<HttpAuthenticationMethods> preferedOrder) {
/* 141 */     this.preferedOrder = preferedOrder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxyLogicHandler getHandler() {
/* 148 */     return this.handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandler(ProxyLogicHandler handler) {
/* 157 */     this.handler = handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxyFilter getProxyFilter() {
/* 164 */     return this.proxyFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProxyFilter(ProxyFilter proxyFilter) {
/* 175 */     this.proxyFilter = proxyFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxyRequest getRequest() {
/* 182 */     return this.request;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setRequest(ProxyRequest request) {
/* 191 */     if (request == null) {
/* 192 */       throw new IllegalArgumentException("request cannot be null");
/*     */     }
/*     */     
/* 195 */     this.request = request;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoSession getSession() {
/* 202 */     return this.session;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSession(IoSession session) {
/* 213 */     this.session = session;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxyConnector getConnector() {
/* 220 */     return this.connector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnector(ProxyConnector connector) {
/* 231 */     this.connector = connector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InetSocketAddress getProxyAddress() {
/* 238 */     return this.proxyAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setProxyAddress(InetSocketAddress proxyAddress) {
/* 247 */     if (proxyAddress == null) {
/* 248 */       throw new IllegalArgumentException("proxyAddress object cannot be null");
/*     */     }
/*     */     
/* 251 */     this.proxyAddress = proxyAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReconnectionNeeded() {
/* 259 */     return this.reconnectionNeeded;
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
/*     */   public void setReconnectionNeeded(boolean reconnectionNeeded) {
/* 273 */     this.reconnectionNeeded = reconnectionNeeded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getCharset() {
/* 280 */     return Charset.forName(getCharsetName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCharsetName() {
/* 287 */     if (this.charsetName == null) {
/* 288 */       this.charsetName = "ISO-8859-1";
/*     */     }
/*     */     
/* 291 */     return this.charsetName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharsetName(String charsetName) {
/* 300 */     this.charsetName = charsetName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAuthenticationFailed() {
/* 307 */     return this.authenticationFailed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAuthenticationFailed(boolean authenticationFailed) {
/* 316 */     this.authenticationFailed = authenticationFailed;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/session/ProxyIoSession.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
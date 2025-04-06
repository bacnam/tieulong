/*     */ package org.apache.mina.proxy.filter;
/*     */ 
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.filterchain.IoFilterAdapter;
/*     */ import org.apache.mina.core.filterchain.IoFilterChain;
/*     */ import org.apache.mina.core.session.IdleStatus;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.write.WriteRequest;
/*     */ import org.apache.mina.proxy.ProxyAuthException;
/*     */ import org.apache.mina.proxy.ProxyLogicHandler;
/*     */ import org.apache.mina.proxy.event.IoSessionEvent;
/*     */ import org.apache.mina.proxy.event.IoSessionEventType;
/*     */ import org.apache.mina.proxy.handlers.ProxyRequest;
/*     */ import org.apache.mina.proxy.handlers.http.HttpSmartProxyHandler;
/*     */ import org.apache.mina.proxy.handlers.socks.Socks4LogicHandler;
/*     */ import org.apache.mina.proxy.handlers.socks.Socks5LogicHandler;
/*     */ import org.apache.mina.proxy.handlers.socks.SocksProxyRequest;
/*     */ import org.apache.mina.proxy.session.ProxyIoSession;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProxyFilter
/*     */   extends IoFilterAdapter
/*     */ {
/*  58 */   private static final Logger LOGGER = LoggerFactory.getLogger(ProxyFilter.class);
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
/*     */   public void onPreAdd(IoFilterChain chain, String name, IoFilter.NextFilter nextFilter) {
/*  79 */     if (chain.contains(ProxyFilter.class)) {
/*  80 */       throw new IllegalStateException("A filter chain cannot contain more than one ProxyFilter.");
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
/*     */   
/*     */   public void onPreRemove(IoFilterChain chain, String name, IoFilter.NextFilter nextFilter) {
/*  94 */     IoSession session = chain.getSession();
/*  95 */     session.removeAttribute(ProxyIoSession.PROXY_SESSION);
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
/*     */   public void exceptionCaught(IoFilter.NextFilter nextFilter, IoSession session, Throwable cause) throws Exception {
/* 109 */     ProxyIoSession proxyIoSession = (ProxyIoSession)session.getAttribute(ProxyIoSession.PROXY_SESSION);
/* 110 */     proxyIoSession.setAuthenticationFailed(true);
/* 111 */     super.exceptionCaught(nextFilter, session, cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ProxyLogicHandler getProxyHandler(IoSession session) {
/* 121 */     ProxyLogicHandler handler = ((ProxyIoSession)session.getAttribute(ProxyIoSession.PROXY_SESSION)).getHandler();
/*     */     
/* 123 */     if (handler == null) {
/* 124 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/* 128 */     if (handler.getProxyIoSession().getProxyFilter() != this) {
/* 129 */       throw new IllegalArgumentException("Not managed by this filter.");
/*     */     }
/*     */     
/* 132 */     return handler;
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
/*     */   public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) throws ProxyAuthException {
/* 146 */     ProxyLogicHandler handler = getProxyHandler(session);
/*     */     
/* 148 */     synchronized (handler) {
/* 149 */       IoBuffer buf = (IoBuffer)message;
/*     */       
/* 151 */       if (handler.isHandshakeComplete()) {
/*     */         
/* 153 */         nextFilter.messageReceived(session, buf);
/*     */       } else {
/*     */         
/* 156 */         LOGGER.debug(" Data Read: {} ({})", handler, buf);
/*     */ 
/*     */ 
/*     */         
/* 160 */         while (buf.hasRemaining() && !handler.isHandshakeComplete()) {
/* 161 */           LOGGER.debug(" Pre-handshake - passing to handler");
/*     */           
/* 163 */           int pos = buf.position();
/* 164 */           handler.messageReceived(nextFilter, buf);
/*     */ 
/*     */           
/* 167 */           if (buf.position() == pos || session.isClosing()) {
/*     */             return;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 173 */         if (buf.hasRemaining()) {
/* 174 */           LOGGER.debug(" Passing remaining data to next filter");
/*     */           
/* 176 */           nextFilter.messageReceived(session, buf);
/*     */         } 
/*     */       } 
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
/*     */   
/*     */   public void filterWrite(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) {
/* 192 */     writeData(nextFilter, session, writeRequest, false);
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
/*     */   public void writeData(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest, boolean isHandshakeData) {
/* 206 */     ProxyLogicHandler handler = getProxyHandler(session);
/*     */     
/* 208 */     synchronized (handler) {
/* 209 */       if (handler.isHandshakeComplete()) {
/*     */         
/* 211 */         nextFilter.filterWrite(session, writeRequest);
/* 212 */       } else if (isHandshakeData) {
/* 213 */         LOGGER.debug("   handshake data: {}", writeRequest.getMessage());
/*     */ 
/*     */         
/* 216 */         nextFilter.filterWrite(session, writeRequest);
/*     */       
/*     */       }
/* 219 */       else if (!session.isConnected()) {
/*     */         
/* 221 */         LOGGER.debug(" Write request on closed session. Request ignored.");
/*     */       } else {
/*     */         
/* 224 */         LOGGER.debug(" Handshaking is not complete yet. Buffering write request.");
/* 225 */         handler.enqueueWriteRequest(nextFilter, writeRequest);
/*     */       } 
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/* 242 */     if (writeRequest.getMessage() != null && writeRequest.getMessage() instanceof ProxyHandshakeIoBuffer) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 247 */     nextFilter.messageSent(session, writeRequest);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionCreated(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 266 */     LOGGER.debug("Session created: " + session);
/* 267 */     ProxyIoSession proxyIoSession = (ProxyIoSession)session.getAttribute(ProxyIoSession.PROXY_SESSION);
/* 268 */     LOGGER.debug("  get proxyIoSession: " + proxyIoSession);
/* 269 */     proxyIoSession.setProxyFilter(this);
/*     */ 
/*     */     
/* 272 */     ProxyLogicHandler handler = proxyIoSession.getHandler();
/*     */ 
/*     */ 
/*     */     
/* 276 */     if (handler == null) {
/* 277 */       HttpSmartProxyHandler httpSmartProxyHandler; ProxyRequest request = proxyIoSession.getRequest();
/*     */       
/* 279 */       if (request instanceof SocksProxyRequest) {
/* 280 */         SocksProxyRequest req = (SocksProxyRequest)request;
/* 281 */         if (req.getProtocolVersion() == 4) {
/* 282 */           Socks4LogicHandler socks4LogicHandler = new Socks4LogicHandler(proxyIoSession);
/*     */         } else {
/* 284 */           Socks5LogicHandler socks5LogicHandler = new Socks5LogicHandler(proxyIoSession);
/*     */         } 
/*     */       } else {
/* 287 */         httpSmartProxyHandler = new HttpSmartProxyHandler(proxyIoSession);
/*     */       } 
/*     */       
/* 290 */       proxyIoSession.setHandler((ProxyLogicHandler)httpSmartProxyHandler);
/* 291 */       httpSmartProxyHandler.doHandshake(nextFilter);
/*     */     } 
/*     */     
/* 294 */     proxyIoSession.getEventQueue().enqueueEventIfNecessary(new IoSessionEvent(nextFilter, session, IoSessionEventType.CREATED));
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
/*     */   public void sessionOpened(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 308 */     ProxyIoSession proxyIoSession = (ProxyIoSession)session.getAttribute(ProxyIoSession.PROXY_SESSION);
/* 309 */     proxyIoSession.getEventQueue().enqueueEventIfNecessary(new IoSessionEvent(nextFilter, session, IoSessionEventType.OPENED));
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
/*     */   public void sessionIdle(IoFilter.NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception {
/* 323 */     ProxyIoSession proxyIoSession = (ProxyIoSession)session.getAttribute(ProxyIoSession.PROXY_SESSION);
/* 324 */     proxyIoSession.getEventQueue().enqueueEventIfNecessary(new IoSessionEvent(nextFilter, session, status));
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
/*     */   public void sessionClosed(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 337 */     ProxyIoSession proxyIoSession = (ProxyIoSession)session.getAttribute(ProxyIoSession.PROXY_SESSION);
/* 338 */     proxyIoSession.getEventQueue().enqueueEventIfNecessary(new IoSessionEvent(nextFilter, session, IoSessionEventType.CLOSED));
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/filter/ProxyFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
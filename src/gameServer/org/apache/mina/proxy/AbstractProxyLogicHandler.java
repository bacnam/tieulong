/*     */ package org.apache.mina.proxy;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.future.DefaultWriteFuture;
/*     */ import org.apache.mina.core.future.WriteFuture;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.write.DefaultWriteRequest;
/*     */ import org.apache.mina.core.write.WriteRequest;
/*     */ import org.apache.mina.proxy.filter.ProxyFilter;
/*     */ import org.apache.mina.proxy.filter.ProxyHandshakeIoBuffer;
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
/*     */ public abstract class AbstractProxyLogicHandler
/*     */   implements ProxyLogicHandler
/*     */ {
/*  49 */   private static final Logger LOGGER = LoggerFactory.getLogger(AbstractProxyLogicHandler.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ProxyIoSession proxyIoSession;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private Queue<Event> writeRequestQueue = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean handshakeComplete = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractProxyLogicHandler(ProxyIoSession proxyIoSession) {
/*  72 */     this.proxyIoSession = proxyIoSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ProxyFilter getProxyFilter() {
/*  79 */     return this.proxyIoSession.getProxyFilter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected IoSession getSession() {
/*  86 */     return this.proxyIoSession.getSession();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxyIoSession getProxyIoSession() {
/*  93 */     return this.proxyIoSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected WriteFuture writeData(IoFilter.NextFilter nextFilter, IoBuffer data) {
/* 104 */     ProxyHandshakeIoBuffer writeBuffer = new ProxyHandshakeIoBuffer(data);
/*     */     
/* 106 */     LOGGER.debug("   session write: {}", writeBuffer);
/*     */     
/* 108 */     DefaultWriteFuture defaultWriteFuture = new DefaultWriteFuture(getSession());
/* 109 */     getProxyFilter().writeData(nextFilter, getSession(), (WriteRequest)new DefaultWriteRequest(writeBuffer, (WriteFuture)defaultWriteFuture), true);
/*     */     
/* 111 */     return (WriteFuture)defaultWriteFuture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHandshakeComplete() {
/* 119 */     synchronized (this) {
/* 120 */       return this.handshakeComplete;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void setHandshakeComplete() {
/* 128 */     synchronized (this) {
/* 129 */       this.handshakeComplete = true;
/*     */     } 
/*     */     
/* 132 */     ProxyIoSession proxyIoSession = getProxyIoSession();
/* 133 */     proxyIoSession.getConnector().fireConnected(proxyIoSession.getSession()).awaitUninterruptibly();
/*     */     
/* 135 */     LOGGER.debug("  handshake completed");
/*     */ 
/*     */     
/*     */     try {
/* 139 */       proxyIoSession.getEventQueue().flushPendingSessionEvents();
/* 140 */       flushPendingWriteRequests();
/* 141 */     } catch (Exception ex) {
/* 142 */       LOGGER.error("Unable to flush pending write requests", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void flushPendingWriteRequests() throws Exception {
/* 150 */     LOGGER.debug(" flushPendingWriteRequests()");
/*     */     
/* 152 */     if (this.writeRequestQueue == null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     Event scheduledWrite;
/* 157 */     while ((scheduledWrite = this.writeRequestQueue.poll()) != null) {
/* 158 */       LOGGER.debug(" Flushing buffered write request: {}", scheduledWrite.data);
/*     */       
/* 160 */       getProxyFilter().filterWrite(scheduledWrite.nextFilter, getSession(), (WriteRequest)scheduledWrite.data);
/*     */     } 
/*     */ 
/*     */     
/* 164 */     this.writeRequestQueue = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void enqueueWriteRequest(IoFilter.NextFilter nextFilter, WriteRequest writeRequest) {
/* 171 */     if (this.writeRequestQueue == null) {
/* 172 */       this.writeRequestQueue = new LinkedList<Event>();
/*     */     }
/*     */     
/* 175 */     this.writeRequestQueue.offer(new Event(nextFilter, writeRequest));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeSession(String message, Throwable t) {
/* 185 */     if (t != null) {
/* 186 */       LOGGER.error(message, t);
/* 187 */       this.proxyIoSession.setAuthenticationFailed(true);
/*     */     } else {
/* 189 */       LOGGER.error(message);
/*     */     } 
/*     */     
/* 192 */     getSession().close(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeSession(String message) {
/* 201 */     closeSession(message, null);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class Event
/*     */   {
/*     */     private final IoFilter.NextFilter nextFilter;
/*     */     
/*     */     private final Object data;
/*     */ 
/*     */     
/*     */     Event(IoFilter.NextFilter nextFilter, Object data) {
/* 213 */       this.nextFilter = nextFilter;
/* 214 */       this.data = data;
/*     */     }
/*     */     
/*     */     public Object getData() {
/* 218 */       return this.data;
/*     */     }
/*     */     
/*     */     public IoFilter.NextFilter getNextFilter() {
/* 222 */       return this.nextFilter;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/AbstractProxyLogicHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
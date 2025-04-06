/*     */ package org.apache.mina.proxy.event;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
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
/*     */ public class IoSessionEventQueue
/*     */ {
/*  38 */   private static final Logger logger = LoggerFactory.getLogger(IoSessionEventQueue.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ProxyIoSession proxyIoSession;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   private Queue<IoSessionEvent> sessionEventsQueue = new LinkedList<IoSessionEvent>();
/*     */   
/*     */   public IoSessionEventQueue(ProxyIoSession proxyIoSession) {
/*  51 */     this.proxyIoSession = proxyIoSession;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void discardSessionQueueEvents() {
/*  58 */     synchronized (this.sessionEventsQueue) {
/*     */       
/*  60 */       this.sessionEventsQueue.clear();
/*  61 */       logger.debug("Event queue CLEARED");
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
/*     */   public void enqueueEventIfNecessary(IoSessionEvent evt) {
/*  77 */     logger.debug("??? >> Enqueue {}", evt);
/*     */     
/*  79 */     if (this.proxyIoSession.getRequest() instanceof org.apache.mina.proxy.handlers.socks.SocksProxyRequest) {
/*     */       
/*  81 */       evt.deliverEvent();
/*     */       
/*     */       return;
/*     */     } 
/*  85 */     if (this.proxyIoSession.getHandler().isHandshakeComplete()) {
/*  86 */       evt.deliverEvent();
/*     */     }
/*  88 */     else if (evt.getType() == IoSessionEventType.CLOSED) {
/*  89 */       if (this.proxyIoSession.isAuthenticationFailed()) {
/*  90 */         this.proxyIoSession.getConnector().cancelConnectFuture();
/*  91 */         discardSessionQueueEvents();
/*  92 */         evt.deliverEvent();
/*     */       } else {
/*  94 */         discardSessionQueueEvents();
/*     */       } 
/*  96 */     } else if (evt.getType() == IoSessionEventType.OPENED) {
/*     */ 
/*     */       
/*  99 */       enqueueSessionEvent(evt);
/* 100 */       evt.deliverEvent();
/*     */     } else {
/* 102 */       enqueueSessionEvent(evt);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flushPendingSessionEvents() throws Exception {
/* 113 */     synchronized (this.sessionEventsQueue) {
/*     */       IoSessionEvent evt;
/*     */       
/* 116 */       while ((evt = this.sessionEventsQueue.poll()) != null) {
/* 117 */         logger.debug(" Flushing buffered event: {}", evt);
/* 118 */         evt.deliverEvent();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void enqueueSessionEvent(IoSessionEvent evt) {
/* 129 */     synchronized (this.sessionEventsQueue) {
/* 130 */       logger.debug("Enqueuing event: {}", evt);
/* 131 */       this.sessionEventsQueue.offer(evt);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/event/IoSessionEventQueue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
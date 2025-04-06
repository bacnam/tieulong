/*     */ package org.apache.mina.filter.firewall;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.filterchain.IoFilterAdapter;
/*     */ import org.apache.mina.core.session.IoSession;
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
/*     */ public class ConnectionThrottleFilter
/*     */   extends IoFilterAdapter
/*     */ {
/*  44 */   private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionThrottleFilter.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long DEFAULT_TIME = 1000L;
/*     */ 
/*     */ 
/*     */   
/*     */   private long allowedInterval;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Map<String, Long> clients;
/*     */ 
/*     */   
/*  59 */   private Lock lock = new ReentrantLock();
/*     */ 
/*     */   
/*     */   private class ExpiredSessionThread
/*     */     extends Thread
/*     */   {
/*     */     private ExpiredSessionThread() {}
/*     */     
/*     */     public void run() {
/*     */       try {
/*  69 */         Thread.sleep(ConnectionThrottleFilter.this.allowedInterval);
/*  70 */       } catch (InterruptedException e) {
/*     */         return;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  77 */       long currentTime = System.currentTimeMillis();
/*     */       
/*  79 */       ConnectionThrottleFilter.this.lock.lock();
/*     */       
/*     */       try {
/*  82 */         Iterator<String> sessions = ConnectionThrottleFilter.this.clients.keySet().iterator();
/*     */         
/*  84 */         while (sessions.hasNext()) {
/*  85 */           String session = sessions.next();
/*  86 */           long creationTime = ((Long)ConnectionThrottleFilter.this.clients.get(session)).longValue();
/*     */           
/*  88 */           if (creationTime + ConnectionThrottleFilter.this.allowedInterval < currentTime) {
/*  89 */             ConnectionThrottleFilter.this.clients.remove(session);
/*     */           }
/*     */         } 
/*     */       } finally {
/*  93 */         ConnectionThrottleFilter.this.lock.unlock();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConnectionThrottleFilter() {
/* 102 */     this(1000L);
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
/*     */   public ConnectionThrottleFilter(long allowedInterval) {
/* 114 */     this.allowedInterval = allowedInterval;
/* 115 */     this.clients = new ConcurrentHashMap<String, Long>();
/*     */ 
/*     */     
/* 118 */     ExpiredSessionThread cleanupThread = new ExpiredSessionThread();
/*     */ 
/*     */     
/* 121 */     cleanupThread.setDaemon(true);
/*     */ 
/*     */     
/* 124 */     cleanupThread.start();
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
/*     */   public void setAllowedInterval(long allowedInterval) {
/* 136 */     this.lock.lock();
/*     */     
/*     */     try {
/* 139 */       this.allowedInterval = allowedInterval;
/*     */     } finally {
/* 141 */       this.lock.unlock();
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
/*     */   protected boolean isConnectionOk(IoSession session) {
/* 155 */     SocketAddress remoteAddress = session.getRemoteAddress();
/*     */     
/* 157 */     if (remoteAddress instanceof InetSocketAddress) {
/* 158 */       InetSocketAddress addr = (InetSocketAddress)remoteAddress;
/* 159 */       long now = System.currentTimeMillis();
/*     */       
/* 161 */       this.lock.lock();
/*     */       
/*     */       try {
/* 164 */         if (this.clients.containsKey(addr.getAddress().getHostAddress())) {
/*     */           
/* 166 */           LOGGER.debug("This is not a new client");
/* 167 */           Long lastConnTime = this.clients.get(addr.getAddress().getHostAddress());
/*     */           
/* 169 */           this.clients.put(addr.getAddress().getHostAddress(), Long.valueOf(now));
/*     */ 
/*     */ 
/*     */           
/* 173 */           if (now - lastConnTime.longValue() < this.allowedInterval) {
/* 174 */             LOGGER.warn("Session connection interval too short");
/* 175 */             return false;
/*     */           } 
/*     */           
/* 178 */           return true;
/*     */         } 
/*     */         
/* 181 */         this.clients.put(addr.getAddress().getHostAddress(), Long.valueOf(now));
/*     */       } finally {
/* 183 */         this.lock.unlock();
/*     */       } 
/*     */       
/* 186 */       return true;
/*     */     } 
/*     */     
/* 189 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void sessionCreated(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 194 */     if (!isConnectionOk(session)) {
/* 195 */       LOGGER.warn("Connections coming in too fast; closing.");
/* 196 */       session.close(true);
/*     */     } 
/*     */     
/* 199 */     nextFilter.sessionCreated(session);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/firewall/ConnectionThrottleFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
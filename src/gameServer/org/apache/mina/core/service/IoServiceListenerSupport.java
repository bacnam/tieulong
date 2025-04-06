/*     */ package org.apache.mina.core.service;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.mina.core.filterchain.IoFilterChain;
/*     */ import org.apache.mina.core.future.IoFuture;
/*     */ import org.apache.mina.core.future.IoFutureListener;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.util.ExceptionMonitor;
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
/*     */ public class IoServiceListenerSupport
/*     */ {
/*     */   private final IoService service;
/*  47 */   private final List<IoServiceListener> listeners = new CopyOnWriteArrayList<IoServiceListener>();
/*     */ 
/*     */   
/*  50 */   private final ConcurrentMap<Long, IoSession> managedSessions = new ConcurrentHashMap<Long, IoSession>();
/*     */ 
/*     */   
/*  53 */   private final Map<Long, IoSession> readOnlyManagedSessions = Collections.unmodifiableMap(this.managedSessions);
/*     */   
/*  55 */   private final AtomicBoolean activated = new AtomicBoolean();
/*     */ 
/*     */   
/*     */   private volatile long activationTime;
/*     */ 
/*     */   
/*  61 */   private volatile int largestManagedSessionCount = 0;
/*     */ 
/*     */   
/*  64 */   private volatile long cumulativeManagedSessionCount = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoServiceListenerSupport(IoService service) {
/*  72 */     if (service == null) {
/*  73 */       throw new IllegalArgumentException("service");
/*     */     }
/*     */     
/*  76 */     this.service = service;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(IoServiceListener listener) {
/*  85 */     if (listener != null) {
/*  86 */       this.listeners.add(listener);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(IoServiceListener listener) {
/*  96 */     if (listener != null) {
/*  97 */       this.listeners.remove(listener);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getActivationTime() {
/* 105 */     return this.activationTime;
/*     */   }
/*     */   
/*     */   public Map<Long, IoSession> getManagedSessions() {
/* 109 */     return this.readOnlyManagedSessions;
/*     */   }
/*     */   
/*     */   public int getManagedSessionCount() {
/* 113 */     return this.managedSessions.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLargestManagedSessionCount() {
/* 121 */     return this.largestManagedSessionCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCumulativeManagedSessionCount() {
/* 129 */     return this.cumulativeManagedSessionCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isActive() {
/* 136 */     return this.activated.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fireServiceActivated() {
/* 144 */     if (!this.activated.compareAndSet(false, true)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 149 */     this.activationTime = System.currentTimeMillis();
/*     */ 
/*     */     
/* 152 */     for (IoServiceListener listener : this.listeners) {
/*     */       try {
/* 154 */         listener.serviceActivated(this.service);
/* 155 */       } catch (Exception e) {
/* 156 */         ExceptionMonitor.getInstance().exceptionCaught(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fireServiceDeactivated() {
/* 166 */     if (!this.activated.compareAndSet(true, false)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 173 */       for (IoServiceListener listener : this.listeners) {
/*     */         try {
/* 175 */           listener.serviceDeactivated(this.service);
/* 176 */         } catch (Exception e) {
/* 177 */           ExceptionMonitor.getInstance().exceptionCaught(e);
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 181 */       disconnectSessions();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fireSessionCreated(IoSession session) {
/* 191 */     boolean firstSession = false;
/*     */     
/* 193 */     if (session.getService() instanceof IoConnector) {
/* 194 */       synchronized (this.managedSessions) {
/* 195 */         firstSession = this.managedSessions.isEmpty();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 200 */     if (this.managedSessions.putIfAbsent(Long.valueOf(session.getId()), session) != null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 205 */     if (firstSession) {
/* 206 */       fireServiceActivated();
/*     */     }
/*     */ 
/*     */     
/* 210 */     IoFilterChain filterChain = session.getFilterChain();
/* 211 */     filterChain.fireSessionCreated();
/* 212 */     filterChain.fireSessionOpened();
/*     */     
/* 214 */     int managedSessionCount = this.managedSessions.size();
/*     */     
/* 216 */     if (managedSessionCount > this.largestManagedSessionCount) {
/* 217 */       this.largestManagedSessionCount = managedSessionCount;
/*     */     }
/*     */     
/* 220 */     this.cumulativeManagedSessionCount++;
/*     */ 
/*     */     
/* 223 */     for (IoServiceListener l : this.listeners) {
/*     */       try {
/* 225 */         l.sessionCreated(session);
/* 226 */       } catch (Exception e) {
/* 227 */         ExceptionMonitor.getInstance().exceptionCaught(e);
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
/*     */   public void fireSessionDestroyed(IoSession session) {
/* 239 */     if (this.managedSessions.remove(Long.valueOf(session.getId())) == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 244 */     session.getFilterChain().fireSessionClosed();
/*     */ 
/*     */     
/*     */     try {
/* 248 */       for (IoServiceListener l : this.listeners) {
/*     */         try {
/* 250 */           l.sessionDestroyed(session);
/* 251 */         } catch (Exception e) {
/* 252 */           ExceptionMonitor.getInstance().exceptionCaught(e);
/*     */         } 
/*     */       } 
/*     */     } finally {
/*     */       
/* 257 */       if (session.getService() instanceof IoConnector) {
/* 258 */         boolean lastSession = false;
/*     */         
/* 260 */         synchronized (this.managedSessions) {
/* 261 */           lastSession = this.managedSessions.isEmpty();
/*     */         } 
/*     */         
/* 264 */         if (lastSession) {
/* 265 */           fireServiceDeactivated();
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
/*     */   private void disconnectSessions() {
/* 277 */     if (!(this.service instanceof IoAcceptor)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 282 */     if (!((IoAcceptor)this.service).isCloseOnDeactivation()) {
/*     */       return;
/*     */     }
/*     */     
/* 286 */     Object lock = new Object();
/* 287 */     IoFutureListener<IoFuture> listener = new LockNotifyingListener(lock);
/*     */     
/* 289 */     for (IoSession s : this.managedSessions.values()) {
/* 290 */       s.close(true).addListener(listener);
/*     */     }
/*     */     
/*     */     try {
/* 294 */       synchronized (lock) {
/* 295 */         while (!this.managedSessions.isEmpty()) {
/* 296 */           lock.wait(500L);
/*     */         }
/*     */       } 
/* 299 */     } catch (InterruptedException ie) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class LockNotifyingListener
/*     */     implements IoFutureListener<IoFuture>
/*     */   {
/*     */     private final Object lock;
/*     */ 
/*     */     
/*     */     public LockNotifyingListener(Object lock) {
/* 311 */       this.lock = lock;
/*     */     }
/*     */     
/*     */     public void operationComplete(IoFuture future) {
/* 315 */       synchronized (this.lock) {
/* 316 */         this.lock.notifyAll();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/service/IoServiceListenerSupport.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
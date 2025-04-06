/*     */ package org.apache.mina.core.future;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.mina.core.polling.AbstractPollingIoProcessor;
/*     */ import org.apache.mina.core.service.IoProcessor;
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
/*     */ public class DefaultIoFuture
/*     */   implements IoFuture
/*     */ {
/*     */   private static final long DEAD_LOCK_CHECK_INTERVAL = 5000L;
/*     */   private final IoSession session;
/*     */   private final Object lock;
/*     */   private IoFutureListener<?> firstListener;
/*     */   private List<IoFutureListener<?>> otherListeners;
/*     */   private Object result;
/*     */   private boolean ready;
/*     */   private int waiters;
/*     */   
/*     */   public DefaultIoFuture(IoSession session) {
/*  64 */     this.session = session;
/*  65 */     this.lock = this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoSession getSession() {
/*  72 */     return this.session;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void join() {
/*  80 */     awaitUninterruptibly();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean join(long timeoutMillis) {
/*  88 */     return awaitUninterruptibly(timeoutMillis);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoFuture await() throws InterruptedException {
/*  95 */     synchronized (this.lock) {
/*  96 */       while (!this.ready) {
/*  97 */         this.waiters++;
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 102 */           this.lock.wait(5000L);
/*     */         } finally {
/* 104 */           this.waiters--;
/* 105 */           if (!this.ready) {
/* 106 */             checkDeadLock();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 111 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
/* 118 */     return await(unit.toMillis(timeout));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean await(long timeoutMillis) throws InterruptedException {
/* 125 */     return await0(timeoutMillis, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoFuture awaitUninterruptibly() {
/*     */     try {
/* 133 */       await0(Long.MAX_VALUE, false);
/* 134 */     } catch (InterruptedException ie) {}
/*     */ 
/*     */ 
/*     */     
/* 138 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
/* 145 */     return awaitUninterruptibly(unit.toMillis(timeout));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean awaitUninterruptibly(long timeoutMillis) {
/*     */     try {
/* 153 */       return await0(timeoutMillis, false);
/* 154 */     } catch (InterruptedException e) {
/* 155 */       throw new InternalError();
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
/*     */ 
/*     */   
/*     */   private boolean await0(long timeoutMillis, boolean interruptable) throws InterruptedException {
/* 173 */     long endTime = System.currentTimeMillis() + timeoutMillis;
/*     */     
/* 175 */     if (endTime < 0L) {
/* 176 */       endTime = Long.MAX_VALUE;
/*     */     }
/*     */     
/* 179 */     synchronized (this.lock) {
/* 180 */       if (this.ready)
/* 181 */         return this.ready; 
/* 182 */       if (timeoutMillis <= 0L) {
/* 183 */         return this.ready;
/*     */       }
/*     */       
/* 186 */       this.waiters++;
/*     */       
/*     */       try {
/*     */         while (true) {
/*     */           try {
/* 191 */             long timeOut = Math.min(timeoutMillis, 5000L);
/* 192 */             this.lock.wait(timeOut);
/* 193 */           } catch (InterruptedException e) {
/* 194 */             if (interruptable) {
/* 195 */               throw e;
/*     */             }
/*     */           } 
/*     */           
/* 199 */           if (this.ready) {
/* 200 */             return true;
/*     */           }
/*     */           
/* 203 */           if (endTime < System.currentTimeMillis()) {
/* 204 */             return this.ready;
/*     */           }
/*     */         } 
/*     */       } finally {
/* 208 */         this.waiters--;
/* 209 */         if (!this.ready) {
/* 210 */           checkDeadLock();
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
/*     */   private void checkDeadLock() {
/* 223 */     if (!(this instanceof CloseFuture) && !(this instanceof WriteFuture) && !(this instanceof ReadFuture) && !(this instanceof ConnectFuture)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
/*     */ 
/*     */     
/* 236 */     for (StackTraceElement s : stackTrace) {
/* 237 */       if (AbstractPollingIoProcessor.class.getName().equals(s.getClassName())) {
/* 238 */         IllegalStateException e = new IllegalStateException("t");
/* 239 */         e.getStackTrace();
/* 240 */         throw new IllegalStateException("DEAD LOCK: " + IoFuture.class.getSimpleName() + ".await() was invoked from an I/O processor thread.  " + "Please use " + IoFutureListener.class.getSimpleName() + " or configure a proper thread model alternatively.");
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 247 */     for (StackTraceElement s : stackTrace) {
/*     */       try {
/* 249 */         Class<?> cls = DefaultIoFuture.class.getClassLoader().loadClass(s.getClassName());
/* 250 */         if (IoProcessor.class.isAssignableFrom(cls)) {
/* 251 */           throw new IllegalStateException("DEAD LOCK: " + IoFuture.class.getSimpleName() + ".await() was invoked from an I/O processor thread.  " + "Please use " + IoFutureListener.class.getSimpleName() + " or configure a proper thread model alternatively.");
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 256 */       catch (Exception cnfe) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/* 266 */     synchronized (this.lock) {
/* 267 */       return this.ready;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(Object newValue) {
/* 275 */     synchronized (this.lock) {
/*     */       
/* 277 */       if (this.ready) {
/*     */         return;
/*     */       }
/*     */       
/* 281 */       this.result = newValue;
/* 282 */       this.ready = true;
/* 283 */       if (this.waiters > 0) {
/* 284 */         this.lock.notifyAll();
/*     */       }
/*     */     } 
/*     */     
/* 288 */     notifyListeners();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getValue() {
/* 295 */     synchronized (this.lock) {
/* 296 */       return this.result;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoFuture addListener(IoFutureListener<?> listener) {
/* 304 */     if (listener == null) {
/* 305 */       throw new IllegalArgumentException("listener");
/*     */     }
/*     */     
/* 308 */     boolean notifyNow = false;
/* 309 */     synchronized (this.lock) {
/* 310 */       if (this.ready) {
/* 311 */         notifyNow = true;
/*     */       }
/* 313 */       else if (this.firstListener == null) {
/* 314 */         this.firstListener = listener;
/*     */       } else {
/* 316 */         if (this.otherListeners == null) {
/* 317 */           this.otherListeners = new ArrayList<IoFutureListener<?>>(1);
/*     */         }
/* 319 */         this.otherListeners.add(listener);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 324 */     if (notifyNow) {
/* 325 */       notifyListener(listener);
/*     */     }
/* 327 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IoFuture removeListener(IoFutureListener<?> listener) {
/* 334 */     if (listener == null) {
/* 335 */       throw new IllegalArgumentException("listener");
/*     */     }
/*     */     
/* 338 */     synchronized (this.lock) {
/* 339 */       if (!this.ready) {
/* 340 */         if (listener == this.firstListener) {
/* 341 */           if (this.otherListeners != null && !this.otherListeners.isEmpty()) {
/* 342 */             this.firstListener = this.otherListeners.remove(0);
/*     */           } else {
/* 344 */             this.firstListener = null;
/*     */           } 
/* 346 */         } else if (this.otherListeners != null) {
/* 347 */           this.otherListeners.remove(listener);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 352 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void notifyListeners() {
/* 359 */     if (this.firstListener != null) {
/* 360 */       notifyListener(this.firstListener);
/* 361 */       this.firstListener = null;
/*     */       
/* 363 */       if (this.otherListeners != null) {
/* 364 */         for (IoFutureListener<?> l : this.otherListeners) {
/* 365 */           notifyListener(l);
/*     */         }
/* 367 */         this.otherListeners = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void notifyListener(IoFutureListener<DefaultIoFuture> l) {
/*     */     try {
/* 375 */       l.operationComplete(this);
/* 376 */     } catch (Exception e) {
/* 377 */       ExceptionMonitor.getInstance().exceptionCaught(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/future/DefaultIoFuture.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
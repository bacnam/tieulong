/*     */ package com.mchange.v1.util;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public abstract class AbstractResourcePool
/*     */ {
/*     */   private static final boolean TRACE = true;
/*     */   private static final boolean DEBUG = true;
/*  53 */   private static RunnableQueue sharedQueue = new SimpleRunnableQueue();
/*     */   
/*  55 */   Set managed = new HashSet();
/*  56 */   List unused = new LinkedList();
/*     */   
/*     */   int start;
/*     */   
/*     */   int max;
/*     */   int inc;
/*  62 */   int num_acq_attempts = Integer.MAX_VALUE;
/*  63 */   int acq_attempt_delay = 50;
/*     */   
/*     */   RunnableQueue rq;
/*     */   
/*     */   boolean initted = false;
/*     */   
/*     */   boolean broken = false;
/*     */   
/*     */   protected AbstractResourcePool(int paramInt1, int paramInt2, int paramInt3) {
/*  72 */     this(paramInt1, paramInt2, paramInt3, sharedQueue);
/*     */   }
/*     */   
/*     */   protected AbstractResourcePool(int paramInt1, int paramInt2, int paramInt3, RunnableQueue paramRunnableQueue) {
/*  76 */     this.start = paramInt1;
/*  77 */     this.max = paramInt2;
/*  78 */     this.inc = paramInt3;
/*  79 */     this.rq = paramRunnableQueue;
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
/*     */   
/*     */   protected synchronized void init() throws Exception {
/*  99 */     for (byte b = 0; b < this.start; ) { assimilateResource(); b++; }
/*     */     
/* 101 */     this.initted = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object checkoutResource() throws BrokenObjectException, InterruptedException, Exception {
/* 107 */     return checkoutResource(0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized Object checkoutResource(long paramLong) throws BrokenObjectException, InterruptedException, TimeoutException, Exception {
/* 114 */     if (!this.initted) init(); 
/* 115 */     ensureNotBroken();
/*     */     
/* 117 */     int i = this.unused.size();
/*     */     
/* 119 */     if (i == 0) {
/*     */       
/* 121 */       int j = this.managed.size();
/* 122 */       if (j < this.max)
/* 123 */         postAcquireMore(); 
/* 124 */       awaitAvailable(paramLong);
/*     */     } 
/* 126 */     Object object = this.unused.get(0);
/* 127 */     this.unused.remove(0);
/*     */     
/*     */     try {
/* 130 */       refurbishResource(object);
/*     */     }
/* 132 */     catch (Exception exception) {
/*     */ 
/*     */       
/* 135 */       exception.printStackTrace();
/* 136 */       removeResource(object);
/* 137 */       return checkoutResource(paramLong);
/*     */     } 
/* 139 */     trace();
/* 140 */     return object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void checkinResource(Object paramObject) throws BrokenObjectException {
/* 147 */     if (!this.managed.contains(paramObject))
/* 148 */       throw new IllegalArgumentException("ResourcePool: Tried to check-in a foreign resource!"); 
/* 149 */     this.unused.add(paramObject);
/* 150 */     notifyAll();
/* 151 */     trace();
/*     */   }
/*     */   
/*     */   protected synchronized void markBad(Object paramObject) throws Exception {
/* 155 */     removeResource(paramObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void close() throws Exception {
/* 162 */     this.broken = true;
/* 163 */     for (Iterator iterator = this.managed.iterator(); iterator.hasNext();) {
/*     */       
/*     */       try {
/* 166 */         removeResource(iterator.next());
/* 167 */       } catch (Exception exception) {
/* 168 */         exception.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void postAcquireMore() throws InterruptedException {
/* 179 */     this.rq.postRunnable(new AcquireTask());
/*     */   }
/*     */ 
/*     */   
/*     */   private void awaitAvailable(long paramLong) throws InterruptedException, TimeoutException {
/*     */     int i;
/* 185 */     for (; (i = this.unused.size()) == 0; wait(paramLong));
/* 186 */     if (i == 0) {
/* 187 */       throw new TimeoutException();
/*     */     }
/*     */   }
/*     */   
/*     */   private void acquireMore() throws Exception {
/* 192 */     int i = this.managed.size();
/* 193 */     for (byte b = 0; b < Math.min(this.inc, this.max - i); b++) {
/* 194 */       assimilateResource();
/*     */     }
/*     */   }
/*     */   
/*     */   private void assimilateResource() throws Exception {
/* 199 */     Object object = acquireResource();
/* 200 */     this.managed.add(object);
/* 201 */     this.unused.add(object);
/*     */     
/* 203 */     notifyAll();
/* 204 */     trace();
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeResource(Object paramObject) throws Exception {
/* 209 */     this.managed.remove(paramObject);
/* 210 */     this.unused.remove(paramObject);
/* 211 */     destroyResource(paramObject);
/* 212 */     trace();
/*     */   }
/*     */   
/*     */   private void ensureNotBroken() throws BrokenObjectException {
/* 216 */     if (this.broken) throw new BrokenObjectException(this);
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized void unexpectedBreak() {
/* 222 */     this.broken = true;
/* 223 */     for (Iterator iterator = this.unused.iterator(); iterator.hasNext();) {
/*     */       
/*     */       try {
/* 226 */         removeResource(iterator.next());
/* 227 */       } catch (Exception exception) {
/* 228 */         exception.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void trace() {
/* 234 */     System.err.println(this + "  [managed: " + this.managed.size() + ", " + "unused: " + this.unused.size() + ']');
/*     */   }
/*     */   
/*     */   protected abstract Object acquireResource() throws Exception;
/*     */   
/*     */   protected abstract void refurbishResource(Object paramObject) throws BrokenObjectException;
/*     */   
/*     */   protected abstract void destroyResource(Object paramObject) throws Exception;
/*     */   
/*     */   class AcquireTask implements Runnable { public void run() {
/* 244 */       for (byte b = 0; !this.success && b < AbstractResourcePool.this.num_acq_attempts; b++) {
/*     */ 
/*     */         
/*     */         try {
/* 248 */           if (b > 0)
/* 249 */             Thread.sleep(AbstractResourcePool.this.acq_attempt_delay); 
/* 250 */           synchronized (AbstractResourcePool.this) {
/* 251 */             AbstractResourcePool.this.acquireMore();
/* 252 */           }  this.success = true;
/*     */         }
/* 254 */         catch (Exception exception) {
/* 255 */           exception.printStackTrace();
/*     */         } 
/* 257 */       }  if (!this.success) AbstractResourcePool.this.unexpectedBreak(); 
/*     */     }
/*     */     
/*     */     boolean success = false; }
/*     */ 
/*     */   
/*     */   protected class TimeoutException extends Exception {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/AbstractResourcePool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
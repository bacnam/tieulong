/*     */ package org.apache.http.impl.conn.tsccm;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ConnectionPoolTimeoutException;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.params.ConnManagerParams;
/*     */ import org.apache.http.conn.params.ConnPerRoute;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class ConnPoolByRoute
/*     */   extends AbstractConnPool
/*     */ {
/*  73 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private final Lock poolLock;
/*     */ 
/*     */   
/*     */   protected final ClientConnectionOperator operator;
/*     */ 
/*     */   
/*     */   protected final ConnPerRoute connPerRoute;
/*     */ 
/*     */   
/*     */   protected final Set<BasicPoolEntry> leasedConnections;
/*     */ 
/*     */   
/*     */   protected final Queue<BasicPoolEntry> freeConnections;
/*     */ 
/*     */   
/*     */   protected final Queue<WaitingThread> waitingThreads;
/*     */ 
/*     */   
/*     */   protected final Map<HttpRoute, RouteSpecificPool> routeToPool;
/*     */ 
/*     */   
/*     */   private final long connTTL;
/*     */ 
/*     */   
/*     */   private final TimeUnit connTTLTimeUnit;
/*     */ 
/*     */   
/*     */   protected volatile boolean shutdown;
/*     */ 
/*     */   
/*     */   protected volatile int maxTotalConnections;
/*     */ 
/*     */   
/*     */   protected volatile int numConnections;
/*     */ 
/*     */ 
/*     */   
/*     */   public ConnPoolByRoute(ClientConnectionOperator operator, ConnPerRoute connPerRoute, int maxTotalConnections) {
/* 114 */     this(operator, connPerRoute, maxTotalConnections, -1L, TimeUnit.MILLISECONDS);
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
/*     */   public ConnPoolByRoute(ClientConnectionOperator operator, ConnPerRoute connPerRoute, int maxTotalConnections, long connTTL, TimeUnit connTTLTimeUnit) {
/* 127 */     Args.notNull(operator, "Connection operator");
/* 128 */     Args.notNull(connPerRoute, "Connections per route");
/* 129 */     this.poolLock = super.poolLock;
/* 130 */     this.leasedConnections = super.leasedConnections;
/* 131 */     this.operator = operator;
/* 132 */     this.connPerRoute = connPerRoute;
/* 133 */     this.maxTotalConnections = maxTotalConnections;
/* 134 */     this.freeConnections = createFreeConnQueue();
/* 135 */     this.waitingThreads = createWaitingThreadQueue();
/* 136 */     this.routeToPool = createRouteToPoolMap();
/* 137 */     this.connTTL = connTTL;
/* 138 */     this.connTTLTimeUnit = connTTLTimeUnit;
/*     */   }
/*     */   
/*     */   protected Lock getLock() {
/* 142 */     return this.poolLock;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ConnPoolByRoute(ClientConnectionOperator operator, HttpParams params) {
/* 152 */     this(operator, ConnManagerParams.getMaxConnectionsPerRoute(params), ConnManagerParams.getMaxTotalConnections(params));
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
/*     */   protected Queue<BasicPoolEntry> createFreeConnQueue() {
/* 164 */     return new LinkedList<BasicPoolEntry>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Queue<WaitingThread> createWaitingThreadQueue() {
/* 174 */     return new LinkedList<WaitingThread>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<HttpRoute, RouteSpecificPool> createRouteToPoolMap() {
/* 184 */     return new HashMap<HttpRoute, RouteSpecificPool>();
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
/*     */   protected RouteSpecificPool newRouteSpecificPool(HttpRoute route) {
/* 197 */     return new RouteSpecificPool(route, this.connPerRoute);
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
/*     */   protected WaitingThread newWaitingThread(Condition cond, RouteSpecificPool rospl) {
/* 212 */     return new WaitingThread(cond, rospl);
/*     */   }
/*     */   
/*     */   private void closeConnection(BasicPoolEntry entry) {
/* 216 */     OperatedClientConnection conn = entry.getConnection();
/* 217 */     if (conn != null) {
/*     */       try {
/* 219 */         conn.close();
/* 220 */       } catch (IOException ex) {
/* 221 */         this.log.debug("I/O error closing connection", ex);
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
/*     */   protected RouteSpecificPool getRoutePool(HttpRoute route, boolean create) {
/* 237 */     RouteSpecificPool rospl = null;
/* 238 */     this.poolLock.lock();
/*     */     
/*     */     try {
/* 241 */       rospl = this.routeToPool.get(route);
/* 242 */       if (rospl == null && create) {
/*     */         
/* 244 */         rospl = newRouteSpecificPool(route);
/* 245 */         this.routeToPool.put(route, rospl);
/*     */       } 
/*     */     } finally {
/*     */       
/* 249 */       this.poolLock.unlock();
/*     */     } 
/*     */     
/* 252 */     return rospl;
/*     */   }
/*     */   
/*     */   public int getConnectionsInPool(HttpRoute route) {
/* 256 */     this.poolLock.lock();
/*     */     
/*     */     try {
/* 259 */       RouteSpecificPool rospl = getRoutePool(route, false);
/* 260 */       return (rospl != null) ? rospl.getEntryCount() : 0;
/*     */     } finally {
/*     */       
/* 263 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getConnectionsInPool() {
/* 268 */     this.poolLock.lock();
/*     */     try {
/* 270 */       return this.numConnections;
/*     */     } finally {
/* 272 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolEntryRequest requestPoolEntry(final HttpRoute route, final Object state) {
/* 281 */     final WaitingThreadAborter aborter = new WaitingThreadAborter();
/*     */     
/* 283 */     return new PoolEntryRequest()
/*     */       {
/*     */         public void abortRequest() {
/* 286 */           ConnPoolByRoute.this.poolLock.lock();
/*     */           try {
/* 288 */             aborter.abort();
/*     */           } finally {
/* 290 */             ConnPoolByRoute.this.poolLock.unlock();
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public BasicPoolEntry getPoolEntry(long timeout, TimeUnit tunit) throws InterruptedException, ConnectionPoolTimeoutException {
/* 298 */           return ConnPoolByRoute.this.getEntryBlocking(route, state, timeout, tunit, aborter);
/*     */         }
/*     */       };
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BasicPoolEntry getEntryBlocking(HttpRoute route, Object state, long timeout, TimeUnit tunit, WaitingThreadAborter aborter) throws ConnectionPoolTimeoutException, InterruptedException {
/* 328 */     Date deadline = null;
/* 329 */     if (timeout > 0L) {
/* 330 */       deadline = new Date(System.currentTimeMillis() + tunit.toMillis(timeout));
/*     */     }
/*     */ 
/*     */     
/* 334 */     BasicPoolEntry entry = null;
/* 335 */     this.poolLock.lock();
/*     */     
/*     */     try {
/* 338 */       RouteSpecificPool rospl = getRoutePool(route, true);
/* 339 */       WaitingThread waitingThread = null;
/*     */       
/* 341 */       while (entry == null) {
/* 342 */         Asserts.check(!this.shutdown, "Connection pool shut down");
/*     */         
/* 344 */         if (this.log.isDebugEnabled()) {
/* 345 */           this.log.debug("[" + route + "] total kept alive: " + this.freeConnections.size() + ", total issued: " + this.leasedConnections.size() + ", total allocated: " + this.numConnections + " out of " + this.maxTotalConnections);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 356 */         entry = getFreeEntry(rospl, state);
/* 357 */         if (entry != null) {
/*     */           break;
/*     */         }
/*     */         
/* 361 */         boolean hasCapacity = (rospl.getCapacity() > 0);
/*     */         
/* 363 */         if (this.log.isDebugEnabled()) {
/* 364 */           this.log.debug("Available capacity: " + rospl.getCapacity() + " out of " + rospl.getMaxEntries() + " [" + route + "][" + state + "]");
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 369 */         if (hasCapacity && this.numConnections < this.maxTotalConnections) {
/*     */           
/* 371 */           entry = createEntry(rospl, this.operator); continue;
/*     */         } 
/* 373 */         if (hasCapacity && !this.freeConnections.isEmpty()) {
/*     */           
/* 375 */           deleteLeastUsedEntry();
/*     */ 
/*     */           
/* 378 */           rospl = getRoutePool(route, true);
/* 379 */           entry = createEntry(rospl, this.operator);
/*     */           
/*     */           continue;
/*     */         } 
/* 383 */         if (this.log.isDebugEnabled()) {
/* 384 */           this.log.debug("Need to wait for connection [" + route + "][" + state + "]");
/*     */         }
/*     */ 
/*     */         
/* 388 */         if (waitingThread == null) {
/* 389 */           waitingThread = newWaitingThread(this.poolLock.newCondition(), rospl);
/*     */           
/* 391 */           aborter.setWaitingThread(waitingThread);
/*     */         } 
/*     */         
/* 394 */         boolean success = false;
/*     */         try {
/* 396 */           rospl.queueThread(waitingThread);
/* 397 */           this.waitingThreads.add(waitingThread);
/* 398 */           success = waitingThread.await(deadline);
/*     */ 
/*     */         
/*     */         }
/*     */         finally {
/*     */ 
/*     */           
/* 405 */           rospl.removeThread(waitingThread);
/* 406 */           this.waitingThreads.remove(waitingThread);
/*     */         } 
/*     */ 
/*     */         
/* 410 */         if (!success && deadline != null && deadline.getTime() <= System.currentTimeMillis())
/*     */         {
/* 412 */           throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
/*     */         }
/*     */       }
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 419 */       this.poolLock.unlock();
/*     */     } 
/* 421 */     return entry;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void freeEntry(BasicPoolEntry entry, boolean reusable, long validDuration, TimeUnit timeUnit) {
/* 427 */     HttpRoute route = entry.getPlannedRoute();
/* 428 */     if (this.log.isDebugEnabled()) {
/* 429 */       this.log.debug("Releasing connection [" + route + "][" + entry.getState() + "]");
/*     */     }
/*     */ 
/*     */     
/* 433 */     this.poolLock.lock();
/*     */     try {
/* 435 */       if (this.shutdown) {
/*     */ 
/*     */         
/* 438 */         closeConnection(entry);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 443 */       this.leasedConnections.remove(entry);
/*     */       
/* 445 */       RouteSpecificPool rospl = getRoutePool(route, true);
/*     */       
/* 447 */       if (reusable && rospl.getCapacity() >= 0) {
/* 448 */         if (this.log.isDebugEnabled()) {
/*     */           String s;
/* 450 */           if (validDuration > 0L) {
/* 451 */             s = "for " + validDuration + " " + timeUnit;
/*     */           } else {
/* 453 */             s = "indefinitely";
/*     */           } 
/* 455 */           this.log.debug("Pooling connection [" + route + "][" + entry.getState() + "]; keep alive " + s);
/*     */         } 
/*     */         
/* 458 */         rospl.freeEntry(entry);
/* 459 */         entry.updateExpiry(validDuration, timeUnit);
/* 460 */         this.freeConnections.add(entry);
/*     */       } else {
/* 462 */         closeConnection(entry);
/* 463 */         rospl.dropEntry();
/* 464 */         this.numConnections--;
/*     */       } 
/*     */       
/* 467 */       notifyWaitingThread(rospl);
/*     */     } finally {
/*     */       
/* 470 */       this.poolLock.unlock();
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
/*     */   protected BasicPoolEntry getFreeEntry(RouteSpecificPool rospl, Object state) {
/* 484 */     BasicPoolEntry entry = null;
/* 485 */     this.poolLock.lock();
/*     */     try {
/* 487 */       boolean done = false;
/* 488 */       while (!done)
/*     */       {
/* 490 */         entry = rospl.allocEntry(state);
/*     */         
/* 492 */         if (entry != null) {
/* 493 */           if (this.log.isDebugEnabled()) {
/* 494 */             this.log.debug("Getting free connection [" + rospl.getRoute() + "][" + state + "]");
/*     */           }
/*     */ 
/*     */           
/* 498 */           this.freeConnections.remove(entry);
/* 499 */           if (entry.isExpired(System.currentTimeMillis())) {
/*     */ 
/*     */             
/* 502 */             if (this.log.isDebugEnabled()) {
/* 503 */               this.log.debug("Closing expired free connection [" + rospl.getRoute() + "][" + state + "]");
/*     */             }
/*     */             
/* 506 */             closeConnection(entry);
/*     */ 
/*     */ 
/*     */             
/* 510 */             rospl.dropEntry();
/* 511 */             this.numConnections--; continue;
/*     */           } 
/* 513 */           this.leasedConnections.add(entry);
/* 514 */           done = true;
/*     */           
/*     */           continue;
/*     */         } 
/* 518 */         done = true;
/* 519 */         if (this.log.isDebugEnabled()) {
/* 520 */           this.log.debug("No free connections [" + rospl.getRoute() + "][" + state + "]");
/*     */         }
/*     */       }
/*     */     
/*     */     } finally {
/*     */       
/* 526 */       this.poolLock.unlock();
/*     */     } 
/* 528 */     return entry;
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
/*     */   protected BasicPoolEntry createEntry(RouteSpecificPool rospl, ClientConnectionOperator op) {
/* 545 */     if (this.log.isDebugEnabled()) {
/* 546 */       this.log.debug("Creating new connection [" + rospl.getRoute() + "]");
/*     */     }
/*     */ 
/*     */     
/* 550 */     BasicPoolEntry entry = new BasicPoolEntry(op, rospl.getRoute(), this.connTTL, this.connTTLTimeUnit);
/*     */     
/* 552 */     this.poolLock.lock();
/*     */     try {
/* 554 */       rospl.createdEntry(entry);
/* 555 */       this.numConnections++;
/* 556 */       this.leasedConnections.add(entry);
/*     */     } finally {
/* 558 */       this.poolLock.unlock();
/*     */     } 
/*     */     
/* 561 */     return entry;
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
/*     */   protected void deleteEntry(BasicPoolEntry entry) {
/* 578 */     HttpRoute route = entry.getPlannedRoute();
/*     */     
/* 580 */     if (this.log.isDebugEnabled()) {
/* 581 */       this.log.debug("Deleting connection [" + route + "][" + entry.getState() + "]");
/*     */     }
/*     */ 
/*     */     
/* 585 */     this.poolLock.lock();
/*     */     
/*     */     try {
/* 588 */       closeConnection(entry);
/*     */       
/* 590 */       RouteSpecificPool rospl = getRoutePool(route, true);
/* 591 */       rospl.deleteEntry(entry);
/* 592 */       this.numConnections--;
/* 593 */       if (rospl.isUnused()) {
/* 594 */         this.routeToPool.remove(route);
/*     */       }
/*     */     } finally {
/*     */       
/* 598 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void deleteLeastUsedEntry() {
/* 608 */     this.poolLock.lock();
/*     */     
/*     */     try {
/* 611 */       BasicPoolEntry entry = this.freeConnections.remove();
/*     */       
/* 613 */       if (entry != null) {
/* 614 */         deleteEntry(entry);
/* 615 */       } else if (this.log.isDebugEnabled()) {
/* 616 */         this.log.debug("No free connection to delete");
/*     */       } 
/*     */     } finally {
/*     */       
/* 620 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleLostEntry(HttpRoute route) {
/* 627 */     this.poolLock.lock();
/*     */     
/*     */     try {
/* 630 */       RouteSpecificPool rospl = getRoutePool(route, true);
/* 631 */       rospl.dropEntry();
/* 632 */       if (rospl.isUnused()) {
/* 633 */         this.routeToPool.remove(route);
/*     */       }
/*     */       
/* 636 */       this.numConnections--;
/* 637 */       notifyWaitingThread(rospl);
/*     */     } finally {
/*     */       
/* 640 */       this.poolLock.unlock();
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
/*     */   
/*     */   protected void notifyWaitingThread(RouteSpecificPool rospl) {
/* 659 */     WaitingThread waitingThread = null;
/*     */     
/* 661 */     this.poolLock.lock();
/*     */     
/*     */     try {
/* 664 */       if (rospl != null && rospl.hasThread()) {
/* 665 */         if (this.log.isDebugEnabled()) {
/* 666 */           this.log.debug("Notifying thread waiting on pool [" + rospl.getRoute() + "]");
/*     */         }
/*     */         
/* 669 */         waitingThread = rospl.nextThread();
/* 670 */       } else if (!this.waitingThreads.isEmpty()) {
/* 671 */         if (this.log.isDebugEnabled()) {
/* 672 */           this.log.debug("Notifying thread waiting on any pool");
/*     */         }
/* 674 */         waitingThread = this.waitingThreads.remove();
/* 675 */       } else if (this.log.isDebugEnabled()) {
/* 676 */         this.log.debug("Notifying no-one, there are no waiting threads");
/*     */       } 
/*     */       
/* 679 */       if (waitingThread != null) {
/* 680 */         waitingThread.wakeup();
/*     */       }
/*     */     } finally {
/*     */       
/* 684 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteClosedConnections() {
/* 691 */     this.poolLock.lock();
/*     */     try {
/* 693 */       Iterator<BasicPoolEntry> iter = this.freeConnections.iterator();
/* 694 */       while (iter.hasNext()) {
/* 695 */         BasicPoolEntry entry = iter.next();
/* 696 */         if (!entry.getConnection().isOpen()) {
/* 697 */           iter.remove();
/* 698 */           deleteEntry(entry);
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 702 */       this.poolLock.unlock();
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
/*     */   public void closeIdleConnections(long idletime, TimeUnit tunit) {
/* 715 */     Args.notNull(tunit, "Time unit");
/* 716 */     long t = (idletime > 0L) ? idletime : 0L;
/* 717 */     if (this.log.isDebugEnabled()) {
/* 718 */       this.log.debug("Closing connections idle longer than " + t + " " + tunit);
/*     */     }
/*     */     
/* 721 */     long deadline = System.currentTimeMillis() - tunit.toMillis(t);
/* 722 */     this.poolLock.lock();
/*     */     try {
/* 724 */       Iterator<BasicPoolEntry> iter = this.freeConnections.iterator();
/* 725 */       while (iter.hasNext()) {
/* 726 */         BasicPoolEntry entry = iter.next();
/* 727 */         if (entry.getUpdated() <= deadline) {
/* 728 */           if (this.log.isDebugEnabled()) {
/* 729 */             this.log.debug("Closing connection last used @ " + new Date(entry.getUpdated()));
/*     */           }
/* 731 */           iter.remove();
/* 732 */           deleteEntry(entry);
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 736 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeExpiredConnections() {
/* 742 */     this.log.debug("Closing expired connections");
/* 743 */     long now = System.currentTimeMillis();
/*     */     
/* 745 */     this.poolLock.lock();
/*     */     try {
/* 747 */       Iterator<BasicPoolEntry> iter = this.freeConnections.iterator();
/* 748 */       while (iter.hasNext()) {
/* 749 */         BasicPoolEntry entry = iter.next();
/* 750 */         if (entry.isExpired(now)) {
/* 751 */           if (this.log.isDebugEnabled()) {
/* 752 */             this.log.debug("Closing connection expired @ " + new Date(entry.getExpiry()));
/*     */           }
/* 754 */           iter.remove();
/* 755 */           deleteEntry(entry);
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 759 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 765 */     this.poolLock.lock();
/*     */     try {
/* 767 */       if (this.shutdown) {
/*     */         return;
/*     */       }
/* 770 */       this.shutdown = true;
/*     */ 
/*     */       
/* 773 */       Iterator<BasicPoolEntry> iter1 = this.leasedConnections.iterator();
/* 774 */       while (iter1.hasNext()) {
/* 775 */         BasicPoolEntry entry = iter1.next();
/* 776 */         iter1.remove();
/* 777 */         closeConnection(entry);
/*     */       } 
/*     */ 
/*     */       
/* 781 */       Iterator<BasicPoolEntry> iter2 = this.freeConnections.iterator();
/* 782 */       while (iter2.hasNext()) {
/* 783 */         BasicPoolEntry entry = iter2.next();
/* 784 */         iter2.remove();
/*     */         
/* 786 */         if (this.log.isDebugEnabled()) {
/* 787 */           this.log.debug("Closing connection [" + entry.getPlannedRoute() + "][" + entry.getState() + "]");
/*     */         }
/*     */         
/* 790 */         closeConnection(entry);
/*     */       } 
/*     */ 
/*     */       
/* 794 */       Iterator<WaitingThread> iwth = this.waitingThreads.iterator();
/* 795 */       while (iwth.hasNext()) {
/* 796 */         WaitingThread waiter = iwth.next();
/* 797 */         iwth.remove();
/* 798 */         waiter.wakeup();
/*     */       } 
/*     */       
/* 801 */       this.routeToPool.clear();
/*     */     } finally {
/*     */       
/* 804 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxTotalConnections(int max) {
/* 812 */     this.poolLock.lock();
/*     */     try {
/* 814 */       this.maxTotalConnections = max;
/*     */     } finally {
/* 816 */       this.poolLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxTotalConnections() {
/* 825 */     return this.maxTotalConnections;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/conn/tsccm/ConnPoolByRoute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
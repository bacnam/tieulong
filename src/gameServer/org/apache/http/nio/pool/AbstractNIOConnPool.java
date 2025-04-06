/*     */ package org.apache.http.nio.pool;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.concurrent.BasicFuture;
/*     */ import org.apache.http.concurrent.FutureCallback;
/*     */ import org.apache.http.nio.reactor.ConnectingIOReactor;
/*     */ import org.apache.http.nio.reactor.IOReactorStatus;
/*     */ import org.apache.http.nio.reactor.IOSession;
/*     */ import org.apache.http.nio.reactor.SessionRequest;
/*     */ import org.apache.http.nio.reactor.SessionRequestCallback;
/*     */ import org.apache.http.pool.ConnPool;
/*     */ import org.apache.http.pool.ConnPoolControl;
/*     */ import org.apache.http.pool.PoolEntry;
/*     */ import org.apache.http.pool.PoolEntryCallback;
/*     */ import org.apache.http.pool.PoolStats;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ public abstract class AbstractNIOConnPool<T, C, E extends PoolEntry<T, C>>
/*     */   implements ConnPool<T, E>, ConnPoolControl<T>
/*     */ {
/*     */   private final ConnectingIOReactor ioreactor;
/*     */   private final NIOConnFactory<T, C> connFactory;
/*     */   private final SocketAddressResolver<T> addressResolver;
/*     */   private final SessionRequestCallback sessionRequestCallback;
/*     */   private final Map<T, RouteSpecificPool<T, C, E>> routeToPool;
/*     */   private final LinkedList<LeaseRequest<T, C, E>> leasingRequests;
/*     */   private final Set<SessionRequest> pending;
/*     */   private final Set<E> leased;
/*     */   private final LinkedList<E> available;
/*     */   private final ConcurrentLinkedQueue<LeaseRequest<T, C, E>> completedRequests;
/*     */   private final Map<T, Integer> maxPerRoute;
/*     */   private final Lock lock;
/*     */   private final AtomicBoolean isShutDown;
/*     */   private volatile int defaultMaxPerRoute;
/*     */   private volatile int maxTotal;
/*     */   
/*     */   @Deprecated
/*     */   public AbstractNIOConnPool(ConnectingIOReactor ioreactor, NIOConnFactory<T, C> connFactory, int defaultMaxPerRoute, int maxTotal) {
/* 103 */     Args.notNull(ioreactor, "I/O reactor");
/* 104 */     Args.notNull(connFactory, "Connection factory");
/* 105 */     Args.positive(defaultMaxPerRoute, "Max per route value");
/* 106 */     Args.positive(maxTotal, "Max total value");
/* 107 */     this.ioreactor = ioreactor;
/* 108 */     this.connFactory = connFactory;
/* 109 */     this.addressResolver = new SocketAddressResolver<T>()
/*     */       {
/*     */         public SocketAddress resolveLocalAddress(T route) throws IOException
/*     */         {
/* 113 */           return AbstractNIOConnPool.this.resolveLocalAddress(route);
/*     */         }
/*     */ 
/*     */         
/*     */         public SocketAddress resolveRemoteAddress(T route) throws IOException {
/* 118 */           return AbstractNIOConnPool.this.resolveRemoteAddress(route);
/*     */         }
/*     */       };
/*     */     
/* 122 */     this.sessionRequestCallback = new InternalSessionRequestCallback();
/* 123 */     this.routeToPool = new HashMap<T, RouteSpecificPool<T, C, E>>();
/* 124 */     this.leasingRequests = new LinkedList<LeaseRequest<T, C, E>>();
/* 125 */     this.pending = new HashSet<SessionRequest>();
/* 126 */     this.leased = new HashSet<E>();
/* 127 */     this.available = new LinkedList<E>();
/* 128 */     this.maxPerRoute = new HashMap<T, Integer>();
/* 129 */     this.completedRequests = new ConcurrentLinkedQueue<LeaseRequest<T, C, E>>();
/* 130 */     this.lock = new ReentrantLock();
/* 131 */     this.isShutDown = new AtomicBoolean(false);
/* 132 */     this.defaultMaxPerRoute = defaultMaxPerRoute;
/* 133 */     this.maxTotal = maxTotal;
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
/*     */   public AbstractNIOConnPool(ConnectingIOReactor ioreactor, NIOConnFactory<T, C> connFactory, SocketAddressResolver<T> addressResolver, int defaultMaxPerRoute, int maxTotal) {
/* 146 */     Args.notNull(ioreactor, "I/O reactor");
/* 147 */     Args.notNull(connFactory, "Connection factory");
/* 148 */     Args.notNull(addressResolver, "Address resolver");
/* 149 */     Args.positive(defaultMaxPerRoute, "Max per route value");
/* 150 */     Args.positive(maxTotal, "Max total value");
/* 151 */     this.ioreactor = ioreactor;
/* 152 */     this.connFactory = connFactory;
/* 153 */     this.addressResolver = addressResolver;
/* 154 */     this.sessionRequestCallback = new InternalSessionRequestCallback();
/* 155 */     this.routeToPool = new HashMap<T, RouteSpecificPool<T, C, E>>();
/* 156 */     this.leasingRequests = new LinkedList<LeaseRequest<T, C, E>>();
/* 157 */     this.pending = new HashSet<SessionRequest>();
/* 158 */     this.leased = new HashSet<E>();
/* 159 */     this.available = new LinkedList<E>();
/* 160 */     this.completedRequests = new ConcurrentLinkedQueue<LeaseRequest<T, C, E>>();
/* 161 */     this.maxPerRoute = new HashMap<T, Integer>();
/* 162 */     this.lock = new ReentrantLock();
/* 163 */     this.isShutDown = new AtomicBoolean(false);
/* 164 */     this.defaultMaxPerRoute = defaultMaxPerRoute;
/* 165 */     this.maxTotal = maxTotal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected SocketAddress resolveRemoteAddress(T route) {
/* 173 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected SocketAddress resolveLocalAddress(T route) {
/* 181 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onLease(E entry) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onRelease(E entry) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onReuse(E entry) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isShutdown() {
/* 205 */     return this.isShutDown.get();
/*     */   }
/*     */   
/*     */   public void shutdown(long waitMs) throws IOException {
/* 209 */     if (this.isShutDown.compareAndSet(false, true)) {
/* 210 */       fireCallbacks();
/* 211 */       this.lock.lock();
/*     */       try {
/* 213 */         for (SessionRequest sessionRequest : this.pending) {
/* 214 */           sessionRequest.cancel();
/*     */         }
/* 216 */         for (PoolEntry poolEntry : this.available) {
/* 217 */           poolEntry.close();
/*     */         }
/* 219 */         for (PoolEntry poolEntry : this.leased) {
/* 220 */           poolEntry.close();
/*     */         }
/* 222 */         for (RouteSpecificPool<T, C, E> pool : this.routeToPool.values()) {
/* 223 */           pool.shutdown();
/*     */         }
/* 225 */         this.routeToPool.clear();
/* 226 */         this.leased.clear();
/* 227 */         this.pending.clear();
/* 228 */         this.available.clear();
/* 229 */         this.leasingRequests.clear();
/* 230 */         this.ioreactor.shutdown(waitMs);
/*     */       } finally {
/* 232 */         this.lock.unlock();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private RouteSpecificPool<T, C, E> getPool(T route) {
/* 238 */     RouteSpecificPool<T, C, E> pool = this.routeToPool.get(route);
/* 239 */     if (pool == null) {
/* 240 */       pool = new RouteSpecificPool<T, C, E>(route)
/*     */         {
/*     */           protected E createEntry(T route, C conn)
/*     */           {
/* 244 */             return AbstractNIOConnPool.this.createEntry(route, conn);
/*     */           }
/*     */         };
/*     */       
/* 248 */       this.routeToPool.put(route, pool);
/*     */     } 
/* 250 */     return pool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<E> lease(T route, Object state, long connectTimeout, TimeUnit tunit, FutureCallback<E> callback) {
/* 257 */     return lease(route, state, connectTimeout, connectTimeout, tunit, callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<E> lease(T route, Object state, long connectTimeout, long leaseTimeout, TimeUnit tunit, FutureCallback<E> callback) {
/* 267 */     Args.notNull(route, "Route");
/* 268 */     Args.notNull(tunit, "Time unit");
/* 269 */     Asserts.check(!this.isShutDown.get(), "Connection pool shut down");
/* 270 */     BasicFuture<E> future = new BasicFuture(callback);
/* 271 */     this.lock.lock();
/*     */     try {
/* 273 */       long timeout = (connectTimeout > 0L) ? tunit.toMillis(connectTimeout) : 0L;
/* 274 */       LeaseRequest<T, C, E> request = new LeaseRequest<T, C, E>(route, state, timeout, leaseTimeout, future);
/* 275 */       boolean completed = processPendingRequest(request);
/* 276 */       if (!request.isDone() && !completed) {
/* 277 */         this.leasingRequests.add(request);
/*     */       }
/* 279 */       if (request.isDone()) {
/* 280 */         this.completedRequests.add(request);
/*     */       }
/*     */     } finally {
/* 283 */       this.lock.unlock();
/*     */     } 
/* 285 */     fireCallbacks();
/* 286 */     return (Future<E>)future;
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<E> lease(T route, Object state, FutureCallback<E> callback) {
/* 291 */     return lease(route, state, -1L, TimeUnit.MICROSECONDS, callback);
/*     */   }
/*     */   
/*     */   public Future<E> lease(T route, Object state) {
/* 295 */     return lease(route, state, -1L, TimeUnit.MICROSECONDS, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void release(E entry, boolean reusable) {
/* 300 */     if (entry == null) {
/*     */       return;
/*     */     }
/* 303 */     if (this.isShutDown.get()) {
/*     */       return;
/*     */     }
/* 306 */     this.lock.lock();
/*     */     try {
/* 308 */       if (this.leased.remove(entry)) {
/* 309 */         RouteSpecificPool<T, C, E> pool = getPool((T)entry.getRoute());
/* 310 */         pool.free(entry, reusable);
/* 311 */         if (reusable) {
/* 312 */           this.available.addFirst(entry);
/* 313 */           onRelease(entry);
/*     */         } else {
/* 315 */           entry.close();
/*     */         } 
/* 317 */         processNextPendingRequest();
/*     */       } 
/*     */     } finally {
/* 320 */       this.lock.unlock();
/*     */     } 
/* 322 */     fireCallbacks();
/*     */   }
/*     */   
/*     */   private void processPendingRequests() {
/* 326 */     ListIterator<LeaseRequest<T, C, E>> it = this.leasingRequests.listIterator();
/* 327 */     while (it.hasNext()) {
/* 328 */       LeaseRequest<T, C, E> request = it.next();
/* 329 */       boolean completed = processPendingRequest(request);
/* 330 */       if (request.isDone() || completed) {
/* 331 */         it.remove();
/*     */       }
/* 333 */       if (request.isDone()) {
/* 334 */         this.completedRequests.add(request);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processNextPendingRequest() {
/* 340 */     ListIterator<LeaseRequest<T, C, E>> it = this.leasingRequests.listIterator();
/* 341 */     while (it.hasNext()) {
/* 342 */       LeaseRequest<T, C, E> request = it.next();
/* 343 */       boolean completed = processPendingRequest(request);
/* 344 */       if (request.isDone() || completed) {
/* 345 */         it.remove();
/*     */       }
/* 347 */       if (request.isDone()) {
/* 348 */         this.completedRequests.add(request);
/*     */       }
/* 350 */       if (completed)
/*     */         return; 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean processPendingRequest(LeaseRequest<T, C, E> request) {
/*     */     E entry;
/* 357 */     T route = request.getRoute();
/* 358 */     Object state = request.getState();
/* 359 */     long deadline = request.getDeadline();
/*     */     
/* 361 */     long now = System.currentTimeMillis();
/* 362 */     if (now > deadline) {
/* 363 */       request.failed(new TimeoutException());
/* 364 */       return false;
/*     */     } 
/*     */     
/* 367 */     RouteSpecificPool<T, C, E> pool = getPool(route);
/*     */     
/*     */     while (true) {
/* 370 */       entry = pool.getFree(state);
/* 371 */       if (entry == null) {
/*     */         break;
/*     */       }
/* 374 */       if (entry.isClosed() || entry.isExpired(System.currentTimeMillis())) {
/* 375 */         entry.close();
/* 376 */         this.available.remove(entry);
/* 377 */         pool.free(entry, false);
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 382 */     if (entry != null) {
/* 383 */       this.available.remove(entry);
/* 384 */       this.leased.add(entry);
/* 385 */       request.completed(entry);
/* 386 */       onReuse(entry);
/* 387 */       onLease(entry);
/* 388 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 392 */     int maxPerRoute = getMax(route);
/*     */     
/* 394 */     int excess = Math.max(0, pool.getAllocatedCount() + 1 - maxPerRoute);
/* 395 */     if (excess > 0) {
/* 396 */       for (int i = 0; i < excess; i++) {
/* 397 */         E lastUsed = pool.getLastUsed();
/* 398 */         if (lastUsed == null) {
/*     */           break;
/*     */         }
/* 401 */         lastUsed.close();
/* 402 */         this.available.remove(lastUsed);
/* 403 */         pool.remove(lastUsed);
/*     */       } 
/*     */     }
/*     */     
/* 407 */     if (pool.getAllocatedCount() < maxPerRoute) {
/* 408 */       SocketAddress localAddress, remoteAddress; int totalUsed = this.pending.size() + this.leased.size();
/* 409 */       int freeCapacity = Math.max(this.maxTotal - totalUsed, 0);
/* 410 */       if (freeCapacity == 0) {
/* 411 */         return false;
/*     */       }
/* 413 */       int totalAvailable = this.available.size();
/* 414 */       if (totalAvailable > freeCapacity - 1 && 
/* 415 */         !this.available.isEmpty()) {
/* 416 */         PoolEntry poolEntry = (PoolEntry)this.available.removeLast();
/* 417 */         poolEntry.close();
/* 418 */         RouteSpecificPool<T, C, E> otherpool = getPool((T)poolEntry.getRoute());
/* 419 */         otherpool.remove((E)poolEntry);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 426 */         remoteAddress = this.addressResolver.resolveRemoteAddress(route);
/* 427 */         localAddress = this.addressResolver.resolveLocalAddress(route);
/* 428 */       } catch (IOException ex) {
/* 429 */         request.failed(ex);
/* 430 */         return false;
/*     */       } 
/*     */       
/* 433 */       SessionRequest sessionRequest = this.ioreactor.connect(remoteAddress, localAddress, route, this.sessionRequestCallback);
/*     */       
/* 435 */       int timout = (request.getConnectTimeout() < 2147483647L) ? (int)request.getConnectTimeout() : Integer.MAX_VALUE;
/*     */       
/* 437 */       sessionRequest.setConnectTimeout(timout);
/* 438 */       this.pending.add(sessionRequest);
/* 439 */       pool.addPending(sessionRequest, request.getFuture());
/* 440 */       return true;
/*     */     } 
/* 442 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void fireCallbacks() {
/*     */     LeaseRequest<T, C, E> request;
/* 448 */     while ((request = (LeaseRequest<T, C, E>)this.completedRequests.poll()) != null) {
/* 449 */       BasicFuture<E> future = request.getFuture();
/* 450 */       Exception ex = request.getException();
/* 451 */       E result = request.getResult();
/* 452 */       if (ex != null) {
/* 453 */         future.failed(ex); continue;
/* 454 */       }  if (result != null) {
/* 455 */         future.completed(result); continue;
/*     */       } 
/* 457 */       future.cancel();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void validatePendingRequests() {
/* 463 */     this.lock.lock();
/*     */     try {
/* 465 */       long now = System.currentTimeMillis();
/* 466 */       ListIterator<LeaseRequest<T, C, E>> it = this.leasingRequests.listIterator();
/* 467 */       while (it.hasNext()) {
/* 468 */         LeaseRequest<T, C, E> request = it.next();
/* 469 */         long deadline = request.getDeadline();
/* 470 */         if (now > deadline) {
/* 471 */           it.remove();
/* 472 */           request.failed(new TimeoutException());
/* 473 */           this.completedRequests.add(request);
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 477 */       this.lock.unlock();
/*     */     } 
/* 479 */     fireCallbacks();
/*     */   }
/*     */   
/*     */   protected void requestCompleted(SessionRequest request) {
/* 483 */     if (this.isShutDown.get()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 488 */     T route = (T)request.getAttachment();
/* 489 */     this.lock.lock();
/*     */     try {
/* 491 */       this.pending.remove(request);
/* 492 */       RouteSpecificPool<T, C, E> pool = getPool(route);
/* 493 */       IOSession session = request.getSession();
/*     */       try {
/* 495 */         C conn = this.connFactory.create(route, session);
/* 496 */         E entry = pool.createEntry(request, conn);
/* 497 */         this.leased.add(entry);
/* 498 */         pool.completed(request, entry);
/* 499 */         onLease(entry);
/* 500 */       } catch (IOException ex) {
/* 501 */         pool.failed(request, ex);
/*     */       } 
/*     */     } finally {
/* 504 */       this.lock.unlock();
/*     */     } 
/* 506 */     fireCallbacks();
/*     */   }
/*     */   
/*     */   protected void requestCancelled(SessionRequest request) {
/* 510 */     if (this.isShutDown.get()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 515 */     T route = (T)request.getAttachment();
/* 516 */     this.lock.lock();
/*     */     try {
/* 518 */       this.pending.remove(request);
/* 519 */       RouteSpecificPool<T, C, E> pool = getPool(route);
/* 520 */       pool.cancelled(request);
/* 521 */       if (this.ioreactor.getStatus().compareTo((Enum)IOReactorStatus.ACTIVE) <= 0) {
/* 522 */         processNextPendingRequest();
/*     */       }
/*     */     } finally {
/* 525 */       this.lock.unlock();
/*     */     } 
/* 527 */     fireCallbacks();
/*     */   }
/*     */   
/*     */   protected void requestFailed(SessionRequest request) {
/* 531 */     if (this.isShutDown.get()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 536 */     T route = (T)request.getAttachment();
/* 537 */     this.lock.lock();
/*     */     try {
/* 539 */       this.pending.remove(request);
/* 540 */       RouteSpecificPool<T, C, E> pool = getPool(route);
/* 541 */       pool.failed(request, request.getException());
/* 542 */       processNextPendingRequest();
/*     */     } finally {
/* 544 */       this.lock.unlock();
/*     */     } 
/* 546 */     fireCallbacks();
/*     */   }
/*     */   
/*     */   protected void requestTimeout(SessionRequest request) {
/* 550 */     if (this.isShutDown.get()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 555 */     T route = (T)request.getAttachment();
/* 556 */     this.lock.lock();
/*     */     try {
/* 558 */       this.pending.remove(request);
/* 559 */       RouteSpecificPool<T, C, E> pool = getPool(route);
/* 560 */       pool.timeout(request);
/* 561 */       processNextPendingRequest();
/*     */     } finally {
/* 563 */       this.lock.unlock();
/*     */     } 
/* 565 */     fireCallbacks();
/*     */   }
/*     */   
/*     */   private int getMax(T route) {
/* 569 */     Integer v = this.maxPerRoute.get(route);
/* 570 */     if (v != null) {
/* 571 */       return v.intValue();
/*     */     }
/* 573 */     return this.defaultMaxPerRoute;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxTotal(int max) {
/* 579 */     Args.positive(max, "Max value");
/* 580 */     this.lock.lock();
/*     */     try {
/* 582 */       this.maxTotal = max;
/*     */     } finally {
/* 584 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxTotal() {
/* 590 */     this.lock.lock();
/*     */     try {
/* 592 */       return this.maxTotal;
/*     */     } finally {
/* 594 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/* 600 */     Args.positive(max, "Max value");
/* 601 */     this.lock.lock();
/*     */     try {
/* 603 */       this.defaultMaxPerRoute = max;
/*     */     } finally {
/* 605 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/* 611 */     this.lock.lock();
/*     */     try {
/* 613 */       return this.defaultMaxPerRoute;
/*     */     } finally {
/* 615 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxPerRoute(T route, int max) {
/* 621 */     Args.notNull(route, "Route");
/* 622 */     Args.positive(max, "Max value");
/* 623 */     this.lock.lock();
/*     */     try {
/* 625 */       this.maxPerRoute.put(route, Integer.valueOf(max));
/*     */     } finally {
/* 627 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxPerRoute(T route) {
/* 633 */     Args.notNull(route, "Route");
/* 634 */     this.lock.lock();
/*     */     try {
/* 636 */       return getMax(route);
/*     */     } finally {
/* 638 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolStats getTotalStats() {
/* 644 */     this.lock.lock();
/*     */     try {
/* 646 */       return new PoolStats(this.leased.size(), this.pending.size(), this.available.size(), this.maxTotal);
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */       
/* 652 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolStats getStats(T route) {
/* 658 */     Args.notNull(route, "Route");
/* 659 */     this.lock.lock();
/*     */     try {
/* 661 */       RouteSpecificPool<T, C, E> pool = getPool(route);
/* 662 */       return new PoolStats(pool.getLeasedCount(), pool.getPendingCount(), pool.getAvailableCount(), getMax(route));
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */       
/* 668 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<T> getRoutes() {
/* 678 */     this.lock.lock();
/*     */     try {
/* 680 */       return new HashSet(this.routeToPool.keySet());
/*     */     } finally {
/* 682 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void enumAvailable(PoolEntryCallback<T, C> callback) {
/* 692 */     this.lock.lock();
/*     */     try {
/* 694 */       Iterator<E> it = this.available.iterator();
/* 695 */       while (it.hasNext()) {
/* 696 */         PoolEntry poolEntry = (PoolEntry)it.next();
/* 697 */         callback.process(poolEntry);
/* 698 */         if (poolEntry.isClosed()) {
/* 699 */           RouteSpecificPool<T, C, E> pool = getPool((T)poolEntry.getRoute());
/* 700 */           pool.remove((E)poolEntry);
/* 701 */           it.remove();
/*     */         } 
/*     */       } 
/* 704 */       processPendingRequests();
/* 705 */       purgePoolMap();
/*     */     } finally {
/* 707 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void enumLeased(PoolEntryCallback<T, C> callback) {
/* 717 */     this.lock.lock();
/*     */     try {
/* 719 */       Iterator<E> it = this.leased.iterator();
/* 720 */       while (it.hasNext()) {
/* 721 */         PoolEntry poolEntry = (PoolEntry)it.next();
/* 722 */         callback.process(poolEntry);
/*     */       } 
/* 724 */       processPendingRequests();
/*     */     } finally {
/* 726 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected void enumEntries(Iterator<E> it, PoolEntryCallback<T, C> callback) {
/* 738 */     while (it.hasNext()) {
/* 739 */       PoolEntry poolEntry = (PoolEntry)it.next();
/* 740 */       callback.process(poolEntry);
/*     */     } 
/* 742 */     processPendingRequests();
/*     */   }
/*     */   
/*     */   private void purgePoolMap() {
/* 746 */     Iterator<Map.Entry<T, RouteSpecificPool<T, C, E>>> it = this.routeToPool.entrySet().iterator();
/* 747 */     while (it.hasNext()) {
/* 748 */       Map.Entry<T, RouteSpecificPool<T, C, E>> entry = it.next();
/* 749 */       RouteSpecificPool<T, C, E> pool = entry.getValue();
/* 750 */       if (pool.getAllocatedCount() == 0) {
/* 751 */         it.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void closeIdle(long idletime, TimeUnit tunit) {
/* 757 */     Args.notNull(tunit, "Time unit");
/* 758 */     long time = tunit.toMillis(idletime);
/* 759 */     if (time < 0L) {
/* 760 */       time = 0L;
/*     */     }
/* 762 */     final long deadline = System.currentTimeMillis() - time;
/* 763 */     enumAvailable(new PoolEntryCallback<T, C>()
/*     */         {
/*     */           public void process(PoolEntry<T, C> entry)
/*     */           {
/* 767 */             if (entry.getUpdated() <= deadline) {
/* 768 */               entry.close();
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeExpired() {
/* 776 */     final long now = System.currentTimeMillis();
/* 777 */     enumAvailable(new PoolEntryCallback<T, C>()
/*     */         {
/*     */           public void process(PoolEntry<T, C> entry)
/*     */           {
/* 781 */             if (entry.isExpired(now)) {
/* 782 */               entry.close();
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 791 */     StringBuilder buffer = new StringBuilder();
/* 792 */     buffer.append("[leased: ");
/* 793 */     buffer.append(this.leased);
/* 794 */     buffer.append("][available: ");
/* 795 */     buffer.append(this.available);
/* 796 */     buffer.append("][pending: ");
/* 797 */     buffer.append(this.pending);
/* 798 */     buffer.append("]");
/* 799 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   protected abstract E createEntry(T paramT, C paramC);
/*     */   
/*     */   class InternalSessionRequestCallback implements SessionRequestCallback {
/*     */     public void completed(SessionRequest request) {
/* 806 */       AbstractNIOConnPool.this.requestCompleted(request);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancelled(SessionRequest request) {
/* 811 */       AbstractNIOConnPool.this.requestCancelled(request);
/*     */     }
/*     */ 
/*     */     
/*     */     public void failed(SessionRequest request) {
/* 816 */       AbstractNIOConnPool.this.requestFailed(request);
/*     */     }
/*     */ 
/*     */     
/*     */     public void timeout(SessionRequest request) {
/* 821 */       AbstractNIOConnPool.this.requestTimeout(request);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/pool/AbstractNIOConnPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
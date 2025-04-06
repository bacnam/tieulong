/*     */ package org.apache.http.nio.pool;
/*     */ 
/*     */ import java.net.ConnectException;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.concurrent.BasicFuture;
/*     */ import org.apache.http.nio.reactor.SessionRequest;
/*     */ import org.apache.http.pool.PoolEntry;
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
/*     */ @NotThreadSafe
/*     */ abstract class RouteSpecificPool<T, C, E extends PoolEntry<T, C>>
/*     */ {
/*     */   private final T route;
/*     */   private final Set<E> leased;
/*     */   private final LinkedList<E> available;
/*     */   private final Map<SessionRequest, BasicFuture<E>> pending;
/*     */   
/*     */   RouteSpecificPool(T route) {
/*  54 */     this.route = route;
/*  55 */     this.leased = new HashSet<E>();
/*  56 */     this.available = new LinkedList<E>();
/*  57 */     this.pending = new HashMap<SessionRequest, BasicFuture<E>>();
/*     */   }
/*     */   
/*     */   public T getRoute() {
/*  61 */     return this.route;
/*     */   }
/*     */   
/*     */   protected abstract E createEntry(T paramT, C paramC);
/*     */   
/*     */   public int getLeasedCount() {
/*  67 */     return this.leased.size();
/*     */   }
/*     */   
/*     */   public int getPendingCount() {
/*  71 */     return this.pending.size();
/*     */   }
/*     */   
/*     */   public int getAvailableCount() {
/*  75 */     return this.available.size();
/*     */   }
/*     */   
/*     */   public int getAllocatedCount() {
/*  79 */     return this.available.size() + this.leased.size() + this.pending.size();
/*     */   }
/*     */   
/*     */   public E getFree(Object state) {
/*  83 */     if (!this.available.isEmpty()) {
/*  84 */       if (state != null) {
/*  85 */         Iterator<E> iterator = this.available.iterator();
/*  86 */         while (iterator.hasNext()) {
/*  87 */           PoolEntry poolEntry = (PoolEntry)iterator.next();
/*  88 */           if (state.equals(poolEntry.getState())) {
/*  89 */             iterator.remove();
/*  90 */             this.leased.add((E)poolEntry);
/*  91 */             return (E)poolEntry;
/*     */           } 
/*     */         } 
/*     */       } 
/*  95 */       Iterator<E> it = this.available.iterator();
/*  96 */       while (it.hasNext()) {
/*  97 */         PoolEntry poolEntry = (PoolEntry)it.next();
/*  98 */         if (poolEntry.getState() == null) {
/*  99 */           it.remove();
/* 100 */           this.leased.add((E)poolEntry);
/* 101 */           return (E)poolEntry;
/*     */         } 
/*     */       } 
/*     */     } 
/* 105 */     return null;
/*     */   }
/*     */   
/*     */   public E getLastUsed() {
/* 109 */     if (!this.available.isEmpty()) {
/* 110 */       return this.available.getLast();
/*     */     }
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(E entry) {
/* 117 */     Args.notNull(entry, "Pool entry");
/* 118 */     if (!this.available.remove(entry) && 
/* 119 */       !this.leased.remove(entry)) {
/* 120 */       return false;
/*     */     }
/*     */     
/* 123 */     return true;
/*     */   }
/*     */   
/*     */   public void free(E entry, boolean reusable) {
/* 127 */     Args.notNull(entry, "Pool entry");
/* 128 */     boolean found = this.leased.remove(entry);
/* 129 */     Asserts.check(found, "Entry %s has not been leased from this pool", entry);
/* 130 */     if (reusable) {
/* 131 */       this.available.addFirst(entry);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPending(SessionRequest sessionRequest, BasicFuture<E> future) {
/* 138 */     this.pending.put(sessionRequest, future);
/*     */   }
/*     */   
/*     */   private BasicFuture<E> removeRequest(SessionRequest request) {
/* 142 */     BasicFuture<E> future = this.pending.remove(request);
/* 143 */     Asserts.notNull(future, "Session request future");
/* 144 */     return future;
/*     */   }
/*     */   
/*     */   public E createEntry(SessionRequest request, C conn) {
/* 148 */     E entry = createEntry(this.route, conn);
/* 149 */     this.leased.add(entry);
/* 150 */     return entry;
/*     */   }
/*     */   
/*     */   public void completed(SessionRequest request, E entry) {
/* 154 */     BasicFuture<E> future = removeRequest(request);
/* 155 */     future.completed(entry);
/*     */   }
/*     */   
/*     */   public void cancelled(SessionRequest request) {
/* 159 */     BasicFuture<E> future = removeRequest(request);
/* 160 */     future.cancel(true);
/*     */   }
/*     */   
/*     */   public void failed(SessionRequest request, Exception ex) {
/* 164 */     BasicFuture<E> future = removeRequest(request);
/* 165 */     future.failed(ex);
/*     */   }
/*     */   
/*     */   public void timeout(SessionRequest request) {
/* 169 */     BasicFuture<E> future = removeRequest(request);
/* 170 */     future.failed(new ConnectException());
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 174 */     for (SessionRequest sessionRequest : this.pending.keySet()) {
/* 175 */       sessionRequest.cancel();
/*     */     }
/* 177 */     this.pending.clear();
/* 178 */     for (PoolEntry poolEntry : this.available) {
/* 179 */       poolEntry.close();
/*     */     }
/* 181 */     this.available.clear();
/* 182 */     for (PoolEntry poolEntry : this.leased) {
/* 183 */       poolEntry.close();
/*     */     }
/* 185 */     this.leased.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 190 */     StringBuilder buffer = new StringBuilder();
/* 191 */     buffer.append("[route: ");
/* 192 */     buffer.append(this.route);
/* 193 */     buffer.append("][leased: ");
/* 194 */     buffer.append(this.leased.size());
/* 195 */     buffer.append("][available: ");
/* 196 */     buffer.append(this.available.size());
/* 197 */     buffer.append("][pending: ");
/* 198 */     buffer.append(this.pending.size());
/* 199 */     buffer.append("]");
/* 200 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/nio/pool/RouteSpecificPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
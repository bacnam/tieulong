/*     */ package org.apache.http.impl.conn.tsccm;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Queue;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.params.ConnPerRoute;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
/*     */ import org.apache.http.util.LangUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class RouteSpecificPool
/*     */ {
/*  56 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */   
/*     */   protected final HttpRoute route;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int maxEntries;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ConnPerRoute connPerRoute;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final LinkedList<BasicPoolEntry> freeEntries;
/*     */ 
/*     */   
/*     */   protected final Queue<WaitingThread> waitingThreads;
/*     */ 
/*     */   
/*     */   protected int numEntries;
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public RouteSpecificPool(HttpRoute route, int maxEntries) {
/*  84 */     this.route = route;
/*  85 */     this.maxEntries = maxEntries;
/*  86 */     this.connPerRoute = new ConnPerRoute() {
/*     */         public int getMaxForRoute(HttpRoute unused) {
/*  88 */           return RouteSpecificPool.this.maxEntries;
/*     */         }
/*     */       };
/*  91 */     this.freeEntries = new LinkedList<BasicPoolEntry>();
/*  92 */     this.waitingThreads = new LinkedList<WaitingThread>();
/*  93 */     this.numEntries = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RouteSpecificPool(HttpRoute route, ConnPerRoute connPerRoute) {
/* 104 */     this.route = route;
/* 105 */     this.connPerRoute = connPerRoute;
/* 106 */     this.maxEntries = connPerRoute.getMaxForRoute(route);
/* 107 */     this.freeEntries = new LinkedList<BasicPoolEntry>();
/* 108 */     this.waitingThreads = new LinkedList<WaitingThread>();
/* 109 */     this.numEntries = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpRoute getRoute() {
/* 119 */     return this.route;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getMaxEntries() {
/* 129 */     return this.maxEntries;
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
/*     */   public boolean isUnused() {
/* 142 */     return (this.numEntries < 1 && this.waitingThreads.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCapacity() {
/* 152 */     return this.connPerRoute.getMaxForRoute(this.route) - this.numEntries;
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
/*     */   public final int getEntryCount() {
/* 164 */     return this.numEntries;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicPoolEntry allocEntry(Object state) {
/* 174 */     if (!this.freeEntries.isEmpty()) {
/* 175 */       ListIterator<BasicPoolEntry> it = this.freeEntries.listIterator(this.freeEntries.size());
/* 176 */       while (it.hasPrevious()) {
/* 177 */         BasicPoolEntry entry = it.previous();
/* 178 */         if (entry.getState() == null || LangUtils.equals(state, entry.getState())) {
/* 179 */           it.remove();
/* 180 */           return entry;
/*     */         } 
/*     */       } 
/*     */     } 
/* 184 */     if (getCapacity() == 0 && !this.freeEntries.isEmpty()) {
/* 185 */       BasicPoolEntry entry = this.freeEntries.remove();
/* 186 */       entry.shutdownEntry();
/* 187 */       OperatedClientConnection conn = entry.getConnection();
/*     */       try {
/* 189 */         conn.close();
/* 190 */       } catch (IOException ex) {
/* 191 */         this.log.debug("I/O error closing connection", ex);
/*     */       } 
/* 193 */       return entry;
/*     */     } 
/* 195 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void freeEntry(BasicPoolEntry entry) {
/* 206 */     if (this.numEntries < 1) {
/* 207 */       throw new IllegalStateException("No entry created for this pool. " + this.route);
/*     */     }
/*     */     
/* 210 */     if (this.numEntries <= this.freeEntries.size()) {
/* 211 */       throw new IllegalStateException("No entry allocated from this pool. " + this.route);
/*     */     }
/*     */     
/* 214 */     this.freeEntries.add(entry);
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
/*     */   public void createdEntry(BasicPoolEntry entry) {
/* 227 */     Args.check(this.route.equals(entry.getPlannedRoute()), "Entry not planned for this pool");
/* 228 */     this.numEntries++;
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
/*     */   public boolean deleteEntry(BasicPoolEntry entry) {
/* 244 */     boolean found = this.freeEntries.remove(entry);
/* 245 */     if (found) {
/* 246 */       this.numEntries--;
/*     */     }
/* 248 */     return found;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dropEntry() {
/* 259 */     Asserts.check((this.numEntries > 0), "There is no entry that could be dropped");
/* 260 */     this.numEntries--;
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
/*     */   public void queueThread(WaitingThread wt) {
/* 273 */     Args.notNull(wt, "Waiting thread");
/* 274 */     this.waitingThreads.add(wt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasThread() {
/* 285 */     return !this.waitingThreads.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WaitingThread nextThread() {
/* 295 */     return this.waitingThreads.peek();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeThread(WaitingThread wt) {
/* 305 */     if (wt == null) {
/*     */       return;
/*     */     }
/*     */     
/* 309 */     this.waitingThreads.remove(wt);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/conn/tsccm/RouteSpecificPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.jolbox.bonecp;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Statistics
/*     */   implements StatisticsMBean
/*     */ {
/*  27 */   private final AtomicLong cacheHits = new AtomicLong(0L);
/*     */   
/*  29 */   private final AtomicLong cacheMiss = new AtomicLong(0L);
/*     */   
/*  31 */   private final AtomicLong statementsCached = new AtomicLong(0L);
/*     */   
/*  33 */   private final AtomicLong connectionsRequested = new AtomicLong(0L);
/*     */   
/*  35 */   private final AtomicLong cumulativeConnectionWaitTime = new AtomicLong(0L);
/*     */   
/*  37 */   private final AtomicLong cumulativeStatementExecuteTime = new AtomicLong(0L);
/*     */   
/*  39 */   private final AtomicLong cumulativeStatementPrepareTime = new AtomicLong(0L);
/*     */   
/*  41 */   private final AtomicLong statementsExecuted = new AtomicLong(0L);
/*     */   
/*  43 */   private final AtomicLong statementsPrepared = new AtomicLong(0L);
/*     */ 
/*     */ 
/*     */   
/*     */   private BoneCP pool;
/*     */ 
/*     */ 
/*     */   
/*     */   public Statistics(BoneCP pool) {
/*  52 */     this.pool = pool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetStats() {
/*  59 */     this.cacheHits.set(0L);
/*  60 */     this.cacheMiss.set(0L);
/*  61 */     this.statementsCached.set(0L);
/*  62 */     this.connectionsRequested.set(0L);
/*  63 */     this.cumulativeConnectionWaitTime.set(0L);
/*  64 */     this.cumulativeStatementExecuteTime.set(0L);
/*  65 */     this.cumulativeStatementPrepareTime.set(0L);
/*  66 */     this.statementsExecuted.set(0L);
/*  67 */     this.statementsPrepared.set(0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getConnectionWaitTimeAvg() {
/*  74 */     return (this.connectionsRequested.get() == 0L) ? 0.0D : (this.cumulativeConnectionWaitTime.get() / 1.0D * this.connectionsRequested.get() / 1000000.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getStatementExecuteTimeAvg() {
/*  81 */     return (this.statementsExecuted.get() == 0L) ? 0.0D : (this.cumulativeStatementExecuteTime.get() / 1.0D * this.statementsExecuted.get() / 1000000.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getStatementPrepareTimeAvg() {
/*  88 */     return (this.statementsPrepared.get() == 0L) ? 0.0D : (this.statementsPrepared.get() / 1.0D * this.cumulativeStatementPrepareTime.get() / 1000000.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalLeased() {
/*  96 */     return this.pool.getTotalLeased();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalFree() {
/* 103 */     return this.pool.getTotalFree();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalCreatedConnections() {
/* 110 */     return this.pool.getTotalCreatedConnections();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCacheHits() {
/* 117 */     return this.cacheHits.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCacheMiss() {
/* 124 */     return this.cacheMiss.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getStatementsCached() {
/* 132 */     return this.statementsCached.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getConnectionsRequested() {
/* 139 */     return this.connectionsRequested.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCumulativeConnectionWaitTime() {
/* 146 */     return this.cumulativeConnectionWaitTime.get() / 1000000L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addCumulativeConnectionWaitTime(long increment) {
/* 153 */     this.cumulativeConnectionWaitTime.addAndGet(increment);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void incrementStatementsExecuted() {
/* 159 */     this.statementsExecuted.incrementAndGet();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void incrementStatementsPrepared() {
/* 165 */     this.statementsPrepared.incrementAndGet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void incrementStatementsCached() {
/* 172 */     this.statementsCached.incrementAndGet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void incrementCacheMiss() {
/* 179 */     this.cacheMiss.incrementAndGet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void incrementCacheHits() {
/* 187 */     this.cacheHits.incrementAndGet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void incrementConnectionsRequested() {
/* 194 */     this.connectionsRequested.incrementAndGet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getCacheHitRatio() {
/* 201 */     return (this.cacheHits.get() + this.cacheMiss.get() == 0L) ? 0.0D : (this.cacheHits.get() / (1.0D * this.cacheHits.get() + this.cacheMiss.get()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getStatementsExecuted() {
/* 208 */     return this.statementsExecuted.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCumulativeStatementExecutionTime() {
/* 215 */     return this.cumulativeStatementExecuteTime.get() / 1000000L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addStatementExecuteTime(long time) {
/* 223 */     this.cumulativeStatementExecuteTime.addAndGet(time);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addStatementPrepareTime(long time) {
/* 231 */     this.cumulativeStatementPrepareTime.addAndGet(time);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCumulativeStatementPrepareTime() {
/* 238 */     return this.cumulativeStatementPrepareTime.get() / 1000000L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getStatementsPrepared() {
/* 245 */     return this.statementsPrepared.get();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/Statistics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
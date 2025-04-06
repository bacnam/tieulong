/*     */ package com.jolbox.bonecp;
/*     */ 
/*     */ import java.sql.SQLException;
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
/*     */ public class PoolWatchThread
/*     */   implements Runnable
/*     */ {
/*     */   private ConnectionPartition partition;
/*     */   private BoneCP pool;
/*     */   private boolean signalled;
/*  37 */   private long acquireRetryDelayInMs = 1000L;
/*     */   
/*     */   private boolean lazyInit;
/*     */   
/*     */   private int poolAvailabilityThreshold;
/*     */   
/*  43 */   private static Logger logger = LoggerFactory.getLogger(PoolWatchThread.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolWatchThread(ConnectionPartition connectionPartition, BoneCP pool) {
/*  51 */     this.partition = connectionPartition;
/*  52 */     this.pool = pool;
/*  53 */     this.lazyInit = this.pool.getConfig().isLazyInit();
/*  54 */     this.acquireRetryDelayInMs = this.pool.getConfig().getAcquireRetryDelayInMs();
/*  55 */     this.poolAvailabilityThreshold = this.pool.getConfig().getPoolAvailabilityThreshold();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*  61 */     while (!this.signalled) {
/*  62 */       int maxNewConnections = 0;
/*     */       
/*     */       try {
/*  65 */         if (this.lazyInit) {
/*  66 */           this.lazyInit = false;
/*  67 */           this.partition.getPoolWatchThreadSignalQueue().take();
/*     */         } 
/*     */ 
/*     */         
/*  71 */         maxNewConnections = this.partition.getMaxConnections() - this.partition.getCreatedConnections();
/*     */         
/*  73 */         while (maxNewConnections == 0 || this.partition.getAvailableConnections() * 100 / this.partition.getMaxConnections() > this.poolAvailabilityThreshold) {
/*  74 */           if (maxNewConnections == 0) {
/*  75 */             this.partition.setUnableToCreateMoreTransactions(true);
/*     */           }
/*  77 */           this.partition.getPoolWatchThreadSignalQueue().take();
/*  78 */           maxNewConnections = this.partition.getMaxConnections() - this.partition.getCreatedConnections();
/*     */         } 
/*     */         
/*  81 */         if (maxNewConnections > 0 && !this.lazyInit) {
/*  82 */           fillConnections(Math.min(maxNewConnections, this.partition.getAcquireIncrement()));
/*     */         
/*     */         }
/*     */       }
/*  86 */       catch (InterruptedException e) {
/*     */         return;
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
/*     */   private void fillConnections(int connectionsToCreate) throws InterruptedException {
/*     */     try {
/* 100 */       for (int i = 0; i < connectionsToCreate; i++) {
/* 101 */         this.partition.addFreeConnection(new ConnectionHandle(this.partition.getUrl(), this.partition.getUsername(), this.partition.getPassword(), this.pool));
/*     */       }
/* 103 */     } catch (SQLException e) {
/* 104 */       logger.error("Error in trying to obtain a connection. Retrying in " + this.acquireRetryDelayInMs + "ms", e);
/* 105 */       Thread.sleep(this.acquireRetryDelayInMs);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/PoolWatchThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
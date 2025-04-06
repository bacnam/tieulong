/*     */ package com.jolbox.bonecp;
/*     */ 
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConnectionMaxAgeThread
/*     */   implements Runnable
/*     */ {
/*     */   private long maxAgeInMs;
/*     */   private ConnectionPartition partition;
/*     */   private ScheduledExecutorService scheduler;
/*     */   private BoneCP pool;
/*     */   private boolean lifoMode;
/*  44 */   protected static Logger logger = LoggerFactory.getLogger(ConnectionTesterThread.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConnectionMaxAgeThread(ConnectionPartition connectionPartition, ScheduledExecutorService scheduler, BoneCP pool, long maxAgeInMs, boolean lifoMode) {
/*  55 */     this.partition = connectionPartition;
/*  56 */     this.scheduler = scheduler;
/*  57 */     this.maxAgeInMs = maxAgeInMs;
/*  58 */     this.pool = pool;
/*  59 */     this.lifoMode = lifoMode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*  65 */     ConnectionHandle connection = null;
/*     */     
/*  67 */     long nextCheckInMs = this.maxAgeInMs;
/*     */     
/*  69 */     int partitionSize = this.partition.getAvailableConnections();
/*  70 */     long currentTime = System.currentTimeMillis();
/*  71 */     for (int i = 0; i < partitionSize; i++) {
/*     */       try {
/*  73 */         connection = (ConnectionHandle)this.partition.getFreeConnections().poll();
/*     */         
/*  75 */         if (connection != null) {
/*  76 */           connection.setOriginatingPartition(this.partition);
/*     */           
/*  78 */           long tmp = this.maxAgeInMs - currentTime - connection.getConnectionCreationTimeInMs();
/*     */           
/*  80 */           if (tmp < nextCheckInMs) {
/*  81 */             nextCheckInMs = tmp;
/*     */           }
/*     */           
/*  84 */           if (connection.isExpired(currentTime)) {
/*     */             
/*  86 */             closeConnection(connection);
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */             
/*  92 */             if (this.lifoMode) {
/*     */               
/*  94 */               if (!((LIFOQueue<ConnectionHandle>)connection.getOriginatingPartition().getFreeConnections()).offerLast(connection)) {
/*  95 */                 connection.internalClose();
/*     */               }
/*     */             } else {
/*  98 */               this.pool.putConnectionBackInPartition(connection);
/*     */             } 
/*     */ 
/*     */             
/* 102 */             Thread.sleep(20L);
/*     */           } 
/*     */         } 
/* 105 */       } catch (Exception e) {
/* 106 */         if (this.scheduler.isShutdown()) {
/* 107 */           logger.debug("Shutting down connection max age thread.");
/*     */           break;
/*     */         } 
/* 110 */         logger.error("Connection max age thread exception.", e);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 115 */     if (!this.scheduler.isShutdown()) {
/* 116 */       this.scheduler.schedule(this, nextCheckInMs, TimeUnit.MILLISECONDS);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeConnection(ConnectionHandle connection) {
/* 126 */     if (connection != null)
/*     */       try {
/* 128 */         connection.internalClose();
/* 129 */       } catch (Throwable t) {
/* 130 */         logger.error("Destroy connection exception", t);
/*     */       } finally {
/* 132 */         this.pool.postDestroyConnection(connection);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/ConnectionMaxAgeThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
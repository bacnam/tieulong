/*     */ package com.jolbox.bonecp;
/*     */ 
/*     */ import java.sql.SQLException;
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
/*     */ 
/*     */ public class ConnectionTesterThread
/*     */   implements Runnable
/*     */ {
/*     */   private long idleConnectionTestPeriodInMs;
/*     */   private long idleMaxAgeInMs;
/*     */   private ConnectionPartition partition;
/*     */   private ScheduledExecutorService scheduler;
/*     */   private BoneCP pool;
/*     */   private boolean lifoMode;
/*  47 */   private static Logger logger = LoggerFactory.getLogger(ConnectionTesterThread.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConnectionTesterThread(ConnectionPartition connectionPartition, ScheduledExecutorService scheduler, BoneCP pool, long idleMaxAgeInMs, long idleConnectionTestPeriodInMs, boolean lifoMode) {
/*  59 */     this.partition = connectionPartition;
/*  60 */     this.scheduler = scheduler;
/*  61 */     this.idleMaxAgeInMs = idleMaxAgeInMs;
/*  62 */     this.idleConnectionTestPeriodInMs = idleConnectionTestPeriodInMs;
/*  63 */     this.pool = pool;
/*  64 */     this.lifoMode = lifoMode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*  70 */     ConnectionHandle connection = null;
/*     */     
/*     */     try {
/*  73 */       long nextCheckInMs = this.idleConnectionTestPeriodInMs;
/*  74 */       if (this.idleMaxAgeInMs > 0L) {
/*  75 */         if (this.idleConnectionTestPeriodInMs == 0L) {
/*  76 */           nextCheckInMs = this.idleMaxAgeInMs;
/*     */         } else {
/*  78 */           nextCheckInMs = Math.min(nextCheckInMs, this.idleMaxAgeInMs);
/*     */         } 
/*     */       }
/*     */       
/*  82 */       int partitionSize = this.partition.getAvailableConnections();
/*  83 */       long currentTimeInMs = System.currentTimeMillis();
/*     */       
/*  85 */       int i = 0; while (true) { if (i < partitionSize) {
/*     */           
/*  87 */           connection = (ConnectionHandle)this.partition.getFreeConnections().poll();
/*  88 */           if (connection != null) {
/*  89 */             connection.setOriginatingPartition(this.partition);
/*     */ 
/*     */             
/*  92 */             if (connection.isPossiblyBroken() || (this.idleMaxAgeInMs > 0L && this.partition.getAvailableConnections() >= this.partition.getMinConnections() && System.currentTimeMillis() - connection.getConnectionLastUsedInMs() > this.idleMaxAgeInMs))
/*     */             
/*     */             { 
/*  95 */               closeConnection(connection);
/*     */ 
/*     */               
/*     */                }
/*     */             
/* 100 */             else if (this.idleConnectionTestPeriodInMs > 0L && currentTimeInMs - connection.getConnectionLastUsedInMs() > this.idleConnectionTestPeriodInMs && currentTimeInMs - connection.getConnectionLastResetInMs() >= this.idleConnectionTestPeriodInMs)
/*     */             
/*     */             { 
/* 103 */               if (!this.pool.isConnectionHandleAlive(connection))
/* 104 */               { closeConnection(connection); }
/*     */               
/*     */               else
/*     */               
/* 108 */               { long tmp = this.idleConnectionTestPeriodInMs;
/* 109 */                 if (this.idleMaxAgeInMs > 0L) {
/* 110 */                   tmp = Math.min(tmp, this.idleMaxAgeInMs);
/*     */                 }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 121 */                 if (tmp < nextCheckInMs)
/* 122 */                   nextCheckInMs = tmp;  }  } else { long tmp = this.idleConnectionTestPeriodInMs - currentTimeInMs - connection.getConnectionLastResetInMs(); long tmp2 = this.idleMaxAgeInMs - currentTimeInMs - connection.getConnectionLastUsedInMs(); if (this.idleMaxAgeInMs > 0L) tmp = Math.min(tmp, tmp2);  if (tmp < nextCheckInMs) nextCheckInMs = tmp;
/*     */                }
/*     */           
/*     */           } 
/*     */         } else {
/*     */           break;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         i++; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 138 */       this.scheduler.schedule(this, nextCheckInMs, TimeUnit.MILLISECONDS);
/* 139 */     } catch (Exception e) {
/* 140 */       if (this.scheduler.isShutdown()) {
/* 141 */         logger.debug("Shutting down connection tester thread.");
/*     */       } else {
/* 143 */         logger.error("Connection tester thread interrupted", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void closeConnection(ConnectionHandle connection) {
/* 153 */     if (connection != null)
/*     */       try {
/* 155 */         connection.internalClose();
/* 156 */       } catch (SQLException e) {
/* 157 */         logger.error("Destroy connection exception", e);
/*     */       } finally {
/* 159 */         this.pool.postDestroyConnection(connection);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/ConnectionTesterThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
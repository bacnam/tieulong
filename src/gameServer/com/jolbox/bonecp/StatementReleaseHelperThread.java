/*    */ package com.jolbox.bonecp;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StatementReleaseHelperThread
/*    */   implements Runnable
/*    */ {
/*    */   private BlockingQueue<StatementHandle> queue;
/*    */   private BoneCP pool;
/* 39 */   private static Logger logger = LoggerFactory.getLogger(StatementReleaseHelperThread.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StatementReleaseHelperThread(BlockingQueue<StatementHandle> queue, BoneCP pool) {
/* 48 */     this.queue = queue;
/* 49 */     this.pool = pool;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 57 */     boolean interrupted = false;
/* 58 */     while (!interrupted) {
/*    */       try {
/* 60 */         StatementHandle statement = this.queue.take();
/*    */         
/* 62 */         statement.closeStatement();
/* 63 */       } catch (InterruptedException e) {
/* 64 */         if (this.pool.poolShuttingDown) {
/*    */           StatementHandle statement;
/*    */           
/* 67 */           while ((statement = this.queue.poll()) != null) {
/*    */             try {
/* 69 */               statement.closeStatement();
/* 70 */             } catch (SQLException e1) {}
/*    */           } 
/*    */         } 
/*    */ 
/*    */ 
/*    */         
/* 76 */         interrupted = true;
/*    */       }
/* 78 */       catch (Exception e) {
/* 79 */         logger.error("Could not close statement.", e);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/StatementReleaseHelperThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
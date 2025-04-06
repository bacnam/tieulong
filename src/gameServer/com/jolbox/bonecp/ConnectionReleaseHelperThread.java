/*    */ package com.jolbox.bonecp;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import java.util.concurrent.BlockingQueue;
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
/*    */ public class ConnectionReleaseHelperThread
/*    */   implements Runnable
/*    */ {
/*    */   private BlockingQueue<ConnectionHandle> queue;
/*    */   private BoneCP pool;
/*    */   
/*    */   public ConnectionReleaseHelperThread(BlockingQueue<ConnectionHandle> queue, BoneCP pool) {
/* 63 */     this.queue = queue;
/* 64 */     this.pool = pool;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 73 */     boolean interrupted = false;
/* 74 */     while (!interrupted) {
/*    */       try {
/* 76 */         ConnectionHandle connection = this.queue.take();
/* 77 */         this.pool.internalReleaseConnection(connection);
/* 78 */       } catch (SQLException e) {
/* 79 */         interrupted = true;
/* 80 */       } catch (InterruptedException e) {
/* 81 */         if (this.pool.poolShuttingDown) {
/*    */           ConnectionHandle connection;
/*    */           
/* 84 */           while ((connection = this.queue.poll()) != null) {
/*    */             try {
/* 86 */               this.pool.internalReleaseConnection(connection);
/* 87 */             } catch (Exception e1) {}
/*    */           } 
/*    */         } 
/*    */ 
/*    */ 
/*    */         
/* 93 */         interrupted = true;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/ConnectionReleaseHelperThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
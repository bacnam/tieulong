/*    */ package com.jolbox.bonecp;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CloseThreadMonitor
/*    */   implements Runnable
/*    */ {
/*    */   private ConnectionHandle connectionHandle;
/*    */   private String stackTrace;
/*    */   private Thread threadToMonitor;
/*    */   private long closeConnectionWatchTimeout;
/* 42 */   private static Logger logger = LoggerFactory.getLogger(CloseThreadMonitor.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CloseThreadMonitor(Thread threadToMonitor, ConnectionHandle connectionHandle, String stackTrace, long closeConnectionWatchTimeout) {
/* 52 */     this.connectionHandle = connectionHandle;
/* 53 */     this.stackTrace = stackTrace;
/* 54 */     this.threadToMonitor = threadToMonitor;
/* 55 */     this.closeConnectionWatchTimeout = closeConnectionWatchTimeout;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/*    */     try {
/* 64 */       this.connectionHandle.setThreadWatch(Thread.currentThread());
/*    */       
/* 66 */       this.threadToMonitor.join(this.closeConnectionWatchTimeout);
/* 67 */       if (!this.connectionHandle.isClosed() && this.threadToMonitor.equals(this.connectionHandle.getThreadUsingConnection()))
/*    */       {
/*    */         
/* 70 */         logger.error(this.stackTrace);
/*    */       }
/* 72 */     } catch (Exception e) {
/*    */       
/* 74 */       if (this.connectionHandle != null)
/* 75 */         this.connectionHandle.setThreadWatch(null); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/CloseThreadMonitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.jolbox.bonecp;
/*    */ 
/*    */ import java.util.concurrent.ThreadFactory;
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
/*    */ public class CustomThreadFactory
/*    */   implements ThreadFactory, Thread.UncaughtExceptionHandler
/*    */ {
/*    */   private boolean daemon;
/*    */   private String threadName;
/* 59 */   private static Logger logger = LoggerFactory.getLogger(CustomThreadFactory.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CustomThreadFactory(String threadName, boolean daemon) {
/* 68 */     this.threadName = threadName;
/* 69 */     this.daemon = daemon;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Thread newThread(Runnable r) {
/* 78 */     Thread t = new Thread(r, this.threadName);
/* 79 */     t.setDaemon(this.daemon);
/* 80 */     t.setUncaughtExceptionHandler(this);
/* 81 */     return t;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void uncaughtException(Thread thread, Throwable throwable) {
/* 89 */     logger.error("Uncaught Exception in thread " + thread.getName(), throwable);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/CustomThreadFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
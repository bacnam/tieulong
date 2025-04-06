/*    */ package org.apache.mina.util;
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
/*    */ public class NamePreservingRunnable
/*    */   implements Runnable
/*    */ {
/* 31 */   private static final Logger LOGGER = LoggerFactory.getLogger(NamePreservingRunnable.class);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private final String newName;
/*    */ 
/*    */ 
/*    */   
/*    */   private final Runnable runnable;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NamePreservingRunnable(Runnable runnable, String newName) {
/* 46 */     this.runnable = runnable;
/* 47 */     this.newName = newName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 56 */     Thread currentThread = Thread.currentThread();
/* 57 */     String oldName = currentThread.getName();
/*    */     
/* 59 */     if (this.newName != null) {
/* 60 */       setName(currentThread, this.newName);
/*    */     }
/*    */     
/*    */     try {
/* 64 */       this.runnable.run();
/*    */     } finally {
/* 66 */       setName(currentThread, oldName);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void setName(Thread thread, String name) {
/*    */     try {
/* 76 */       thread.setName(name);
/* 77 */     } catch (SecurityException se) {
/* 78 */       if (LOGGER.isWarnEnabled())
/* 79 */         LOGGER.warn("Failed to set the thread name.", se); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/NamePreservingRunnable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
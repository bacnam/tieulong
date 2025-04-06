/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.lang.ref.Reference;
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
/*    */ public class AbandonedConnectionCleanupThread
/*    */   extends Thread
/*    */ {
/*    */   private static boolean running = true;
/* 30 */   private static Thread threadRef = null;
/*    */   
/*    */   public AbandonedConnectionCleanupThread() {
/* 33 */     super("Abandoned connection cleanup thread");
/*    */   }
/*    */   
/*    */   public void run() {
/* 37 */     threadRef = this;
/* 38 */     threadRef.setContextClassLoader(null);
/* 39 */     while (running) {
/*    */       try {
/* 41 */         Reference<? extends ConnectionImpl> ref = NonRegisteringDriver.refQueue.remove(100L);
/* 42 */         if (ref != null) {
/*    */           try {
/* 44 */             ((NonRegisteringDriver.ConnectionPhantomReference)ref).cleanup();
/*    */           } finally {
/* 46 */             NonRegisteringDriver.connectionPhantomRefs.remove(ref);
/*    */           }
/*    */         
/*    */         }
/* 50 */       } catch (Exception ex) {}
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void shutdown() throws InterruptedException {
/* 57 */     running = false;
/* 58 */     if (threadRef != null) {
/* 59 */       threadRef.interrupt();
/* 60 */       threadRef.join();
/* 61 */       threadRef = null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mysql/jdbc/AbandonedConnectionCleanupThread.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
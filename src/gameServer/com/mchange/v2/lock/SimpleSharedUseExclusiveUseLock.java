/*    */ package com.mchange.v2.lock;
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
/*    */ public class SimpleSharedUseExclusiveUseLock
/*    */   implements SharedUseExclusiveUseLock
/*    */ {
/* 46 */   private int waiting_readers = 0;
/* 47 */   private int active_readers = 0;
/*    */   
/* 49 */   private int waiting_writers = 0;
/*    */   
/*    */   private boolean writer_active = false;
/*    */ 
/*    */   
/*    */   public synchronized void acquireShared() throws InterruptedException {
/*    */     try {
/* 56 */       this.waiting_readers++;
/* 57 */       while (!okayToRead())
/* 58 */         wait(); 
/* 59 */       this.active_readers++;
/*    */     }
/*    */     finally {
/*    */       
/* 63 */       this.waiting_readers--;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void relinquishShared() {
/* 69 */     this.active_readers--;
/* 70 */     notifyAll();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void acquireExclusive() throws InterruptedException {
/*    */     try {
/* 77 */       this.waiting_writers++;
/* 78 */       while (!okayToWrite())
/* 79 */         wait(); 
/* 80 */       this.writer_active = true;
/*    */     }
/*    */     finally {
/*    */       
/* 84 */       this.waiting_writers--;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void relinquishExclusive() {
/* 90 */     this.writer_active = false;
/* 91 */     notifyAll();
/*    */   }
/*    */   
/*    */   private boolean okayToRead() {
/* 95 */     return (!this.writer_active && this.waiting_writers == 0);
/*    */   }
/*    */   private boolean okayToWrite() {
/* 98 */     return (this.active_readers == 0 && !this.writer_active);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/lock/SimpleSharedUseExclusiveUseLock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.jolbox.bonecp;
/*    */ 
/*    */ import java.util.concurrent.LinkedBlockingDeque;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import jsr166y.TransferQueue;
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
/*    */ public class LIFOQueue<E>
/*    */   extends LinkedBlockingDeque<E>
/*    */   implements TransferQueue<E>
/*    */ {
/*    */   private static final long serialVersionUID = -3503791017846313243L;
/*    */   
/*    */   public LIFOQueue(int capacity) {
/* 50 */     super(capacity);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LIFOQueue() {}
/*    */ 
/*    */   
/*    */   public boolean tryTransfer(E e) {
/* 59 */     return offerFirst(e);
/*    */   }
/*    */ 
/*    */   
/*    */   public void transfer(E e) throws InterruptedException {
/* 64 */     putFirst(e);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean tryTransfer(E e, long timeout, TimeUnit unit) throws InterruptedException {
/* 69 */     return offerFirst(e, timeout, unit);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasWaitingConsumer() {
/* 74 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getWaitingConsumerCount() {
/* 79 */     return 0;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean offer(E e) {
/* 85 */     return offerFirst(e);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/LIFOQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.mchange.util.impl;
/*    */ 
/*    */ import com.mchange.util.Queue;
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
/*    */ public class CircularListQueue
/*    */   implements Queue, Cloneable
/*    */ {
/*    */   CircularList list;
/*    */   
/*    */   public int size() {
/* 44 */     return this.list.size();
/* 45 */   } public boolean hasMoreElements() { return (this.list.size() > 0); }
/* 46 */   public void enqueue(Object paramObject) { this.list.appendElement(paramObject); } public Object peek() {
/* 47 */     return this.list.getFirstElement();
/*    */   }
/*    */   public Object dequeue() {
/* 50 */     Object object = this.list.getFirstElement();
/* 51 */     this.list.removeFirstElement();
/* 52 */     return object;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object clone() {
/* 60 */     return new CircularListQueue((CircularList)this.list.clone());
/*    */   }
/*    */   public CircularListQueue() {
/* 63 */     this.list = new CircularList();
/*    */   }
/*    */   private CircularListQueue(CircularList paramCircularList) {
/* 66 */     this.list = paramCircularList;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/CircularListQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
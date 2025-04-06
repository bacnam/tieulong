/*    */ package com.mchange.v1.util;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
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
/*    */ public class JoinedIterator
/*    */   implements Iterator
/*    */ {
/*    */   Iterator[] its;
/* 43 */   Iterator removeIterator = null;
/*    */   boolean permit_removes;
/* 45 */   int cur = 0;
/*    */ 
/*    */   
/*    */   public JoinedIterator(Iterator[] paramArrayOfIterator, boolean paramBoolean) {
/* 49 */     this.its = paramArrayOfIterator;
/* 50 */     this.permit_removes = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasNext() {
/* 55 */     if (this.cur == this.its.length)
/* 56 */       return false; 
/* 57 */     if (this.its[this.cur].hasNext()) {
/* 58 */       return true;
/*    */     }
/*    */     
/* 61 */     this.cur++;
/* 62 */     return hasNext();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object next() {
/* 68 */     if (!hasNext()) {
/* 69 */       throw new NoSuchElementException();
/*    */     }
/* 71 */     this.removeIterator = this.its[this.cur];
/* 72 */     return this.removeIterator.next();
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove() {
/* 77 */     if (this.permit_removes) {
/*    */       
/* 79 */       if (this.removeIterator != null) {
/*    */         
/* 81 */         this.removeIterator.remove();
/* 82 */         this.removeIterator = null;
/*    */       } else {
/*    */         
/* 85 */         throw new IllegalStateException("next() not called, or element already removed.");
/*    */       } 
/*    */     } else {
/* 88 */       throw new UnsupportedOperationException();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/JoinedIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.sql.SQLException;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class IterateBlock<T>
/*    */ {
/*    */   DatabaseMetaData.IteratorWithCleanup<T> iteratorWithCleanup;
/*    */   Iterator<T> javaIterator;
/*    */   boolean stopIterating = false;
/*    */   
/*    */   IterateBlock(DatabaseMetaData.IteratorWithCleanup<T> i) {
/* 38 */     this.iteratorWithCleanup = i;
/* 39 */     this.javaIterator = null;
/*    */   }
/*    */   
/*    */   IterateBlock(Iterator<T> i) {
/* 43 */     this.javaIterator = i;
/* 44 */     this.iteratorWithCleanup = null;
/*    */   }
/*    */   
/*    */   public void doForAll() throws SQLException {
/* 48 */     if (this.iteratorWithCleanup != null) {
/*    */       try {
/* 50 */         while (this.iteratorWithCleanup.hasNext()) {
/* 51 */           forEach(this.iteratorWithCleanup.next());
/*    */           
/* 53 */           if (this.stopIterating) {
/*    */             break;
/*    */           }
/*    */         } 
/*    */       } finally {
/* 58 */         this.iteratorWithCleanup.close();
/*    */       } 
/*    */     } else {
/* 61 */       while (this.javaIterator.hasNext()) {
/* 62 */         forEach(this.javaIterator.next());
/*    */         
/* 64 */         if (this.stopIterating) {
/*    */           break;
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   abstract void forEach(T paramT) throws SQLException;
/*    */   
/*    */   public final boolean fullIteration() {
/* 74 */     return !this.stopIterating;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mysql/jdbc/IterateBlock.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
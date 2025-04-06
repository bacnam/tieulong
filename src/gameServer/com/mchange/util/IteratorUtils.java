/*    */ package com.mchange.util;
/*    */ 
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
/*    */ public class IteratorUtils
/*    */ {
/*    */   public static Iterator unmodifiableIterator(final Iterator ii) {
/* 44 */     return new Iterator()
/*    */       {
/*    */         public boolean hasNext() {
/* 47 */           return ii.hasNext();
/*    */         }
/*    */         public Object next() {
/* 50 */           return ii.next();
/*    */         }
/*    */         public void remove() {
/* 53 */           throw new UnsupportedOperationException("This Iterator does not support the remove operation.");
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/IteratorUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
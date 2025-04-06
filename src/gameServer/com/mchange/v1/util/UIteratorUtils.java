/*    */ package com.mchange.v1.util;
/*    */ 
/*    */ import java.util.Collection;
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
/*    */ public class UIteratorUtils
/*    */ {
/*    */   public static void addToCollection(Collection<Object> paramCollection, UIterator paramUIterator) throws Exception {
/* 45 */     while (paramUIterator.hasNext()) {
/* 46 */       paramCollection.add(paramUIterator.next());
/*    */     }
/*    */   }
/*    */   
/*    */   public static UIterator uiteratorFromIterator(final Iterator ii) {
/* 51 */     return new UIterator()
/*    */       {
/*    */         public boolean hasNext() {
/* 54 */           return ii.hasNext();
/*    */         }
/*    */         public Object next() {
/* 57 */           return ii.next();
/*    */         }
/*    */         public void remove() {
/* 60 */           ii.remove();
/*    */         }
/*    */         
/*    */         public void close() {}
/*    */       };
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/UIteratorUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
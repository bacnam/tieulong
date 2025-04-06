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
/*    */ 
/*    */ public class UnreliableIteratorUtils
/*    */ {
/*    */   public static void addToCollection(Collection<Object> paramCollection, UnreliableIterator paramUnreliableIterator) throws UnreliableIteratorException {
/* 46 */     while (paramUnreliableIterator.hasNext()) {
/* 47 */       paramCollection.add(paramUnreliableIterator.next());
/*    */     }
/*    */   }
/*    */   
/*    */   public static UnreliableIterator unreliableIteratorFromIterator(final Iterator ii) {
/* 52 */     return new UnreliableIterator()
/*    */       {
/*    */         public boolean hasNext() {
/* 55 */           return ii.hasNext();
/*    */         }
/*    */         public Object next() {
/* 58 */           return ii.next();
/*    */         }
/*    */         public void remove() {
/* 61 */           ii.remove();
/*    */         }
/*    */         
/*    */         public void close() {}
/*    */       };
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/UnreliableIteratorUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.mchange.v1.util;
/*    */ 
/*    */ import java.util.AbstractList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
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
/*    */ public final class ListUtils
/*    */ {
/*    */   public static List oneElementUnmodifiableList(final Object elem) {
/* 44 */     return new AbstractList()
/*    */       {
/*    */         public Iterator iterator() {
/* 47 */           return IteratorUtils.oneElementUnmodifiableIterator(elem);
/*    */         } public int size() {
/* 49 */           return 1;
/*    */         }
/*    */         public boolean isEmpty() {
/* 52 */           return false;
/*    */         }
/*    */         public boolean contains(Object param1Object) {
/* 55 */           return (param1Object == elem);
/*    */         }
/*    */         
/*    */         public Object get(int param1Int) {
/* 59 */           if (param1Int != 0) {
/* 60 */             throw new IndexOutOfBoundsException("One element list has no element index " + param1Int);
/*    */           }
/*    */           
/* 63 */           return elem;
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean equivalent(List paramList1, List paramList2) {
/* 72 */     if (paramList1.size() != paramList2.size()) {
/* 73 */       return false;
/*    */     }
/*    */     
/* 76 */     Iterator iterator1 = paramList1.iterator();
/* 77 */     Iterator iterator2 = paramList2.iterator();
/* 78 */     return IteratorUtils.equivalent(iterator1, iterator2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int hashContents(List paramList) {
/* 90 */     int i = 0;
/* 91 */     byte b = 0;
/* 92 */     for (Object object : paramList) {
/*    */ 
/*    */       
/* 95 */       if (object != null) i ^= object.hashCode() ^ b;  b++;
/*    */     } 
/* 97 */     return i;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/ListUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.mchange.v1.util;
/*    */ 
/*    */ import java.util.AbstractSet;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
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
/*    */ public final class SetUtils
/*    */ {
/*    */   public static Set oneElementUnmodifiableSet(final Object elem) {
/* 47 */     return new AbstractSet()
/*    */       {
/*    */         public Iterator iterator() {
/* 50 */           return IteratorUtils.oneElementUnmodifiableIterator(elem);
/*    */         } public int size() {
/* 52 */           return 1;
/*    */         }
/*    */         public boolean isEmpty() {
/* 55 */           return false;
/*    */         }
/*    */         public boolean contains(Object param1Object) {
/* 58 */           return (param1Object == elem);
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public static Set setFromArray(Object[] paramArrayOfObject) {
/* 65 */     HashSet<Object> hashSet = new HashSet(); byte b; int i;
/* 66 */     for (b = 0, i = paramArrayOfObject.length; b < i; b++)
/* 67 */       hashSet.add(paramArrayOfObject[b]); 
/* 68 */     return hashSet;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean equivalentDisregardingSort(Set<?> paramSet1, Set<?> paramSet2) {
/* 73 */     return (paramSet1.containsAll(paramSet2) && paramSet2.containsAll(paramSet1));
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
/*    */   
/*    */   public static int hashContentsDisregardingSort(Set paramSet) {
/* 86 */     int i = 0;
/* 87 */     for (Object object : paramSet) {
/*    */ 
/*    */       
/* 90 */       if (object != null) i ^= object.hashCode(); 
/*    */     } 
/* 92 */     return i;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/SetUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
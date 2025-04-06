/*    */ package com.mchange.util.impl;
/*    */ 
/*    */ import com.mchange.util.IntEnumeration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class IntEnumerationHelperBase
/*    */   implements IntEnumeration
/*    */ {
/*    */   public abstract boolean hasMoreInts();
/*    */   
/*    */   public abstract int nextInt();
/*    */   
/*    */   public final boolean hasMoreElements() {
/* 46 */     return hasMoreInts();
/*    */   }
/*    */   public final Object nextElement() {
/* 49 */     return new Integer(nextInt());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/IntEnumerationHelperBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
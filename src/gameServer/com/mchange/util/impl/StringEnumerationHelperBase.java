/*    */ package com.mchange.util.impl;
/*    */ 
/*    */ import com.mchange.util.StringEnumeration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class StringEnumerationHelperBase
/*    */   implements StringEnumeration
/*    */ {
/*    */   public abstract boolean hasMoreStrings();
/*    */   
/*    */   public abstract String nextString();
/*    */   
/*    */   public final boolean hasMoreElements() {
/* 46 */     return hasMoreStrings();
/*    */   }
/*    */   public final Object nextElement() {
/* 49 */     return nextString();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/StringEnumerationHelperBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
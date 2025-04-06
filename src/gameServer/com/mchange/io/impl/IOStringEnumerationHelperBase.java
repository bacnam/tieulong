/*    */ package com.mchange.io.impl;
/*    */ 
/*    */ import com.mchange.io.IOStringEnumeration;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class IOStringEnumerationHelperBase
/*    */   implements IOStringEnumeration
/*    */ {
/*    */   public abstract boolean hasMoreStrings() throws IOException;
/*    */   
/*    */   public abstract String nextString() throws IOException;
/*    */   
/*    */   public final boolean hasMoreElements() throws IOException {
/* 47 */     return hasMoreStrings();
/*    */   }
/*    */   public final Object nextElement() throws IOException {
/* 50 */     return nextString();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/impl/IOStringEnumerationHelperBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
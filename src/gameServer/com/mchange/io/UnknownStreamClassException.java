/*    */ package com.mchange.io;
/*    */ 
/*    */ import java.io.InvalidClassException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnknownStreamClassException
/*    */   extends InvalidClassException
/*    */ {
/*    */   public UnknownStreamClassException(ClassNotFoundException paramClassNotFoundException) {
/* 43 */     super(paramClassNotFoundException.getMessage());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/io/UnknownStreamClassException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
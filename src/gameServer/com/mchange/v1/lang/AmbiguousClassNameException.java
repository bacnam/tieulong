/*    */ package com.mchange.v1.lang;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AmbiguousClassNameException
/*    */   extends Exception
/*    */ {
/*    */   AmbiguousClassNameException(String paramString, Class paramClass1, Class paramClass2) {
/* 42 */     super(paramString + " could refer either to " + paramClass1.getName() + " or " + paramClass2.getName());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/AmbiguousClassNameException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
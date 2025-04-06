/*    */ package com.notnoop.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ApnsException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -4756693306121825229L;
/*    */   
/*    */   public ApnsException() {}
/*    */   
/*    */   public ApnsException(String message) {
/* 41 */     super(message);
/* 42 */   } public ApnsException(Throwable cause) { super(cause); } public ApnsException(String m, Throwable c) {
/* 43 */     super(m, c);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/exceptions/ApnsException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
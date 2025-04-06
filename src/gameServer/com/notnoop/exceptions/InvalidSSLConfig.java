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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InvalidSSLConfig
/*    */   extends ApnsException
/*    */ {
/*    */   private static final long serialVersionUID = -7283168775864517167L;
/*    */   
/*    */   public InvalidSSLConfig() {}
/*    */   
/*    */   public InvalidSSLConfig(String message) {
/* 61 */     super(message);
/* 62 */   } public InvalidSSLConfig(Throwable cause) { super(cause); } public InvalidSSLConfig(String m, Throwable c) {
/* 63 */     super(m, c);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/exceptions/InvalidSSLConfig.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
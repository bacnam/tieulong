/*    */ package com.notnoop.exceptions;
/*    */ 
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
/*    */ 
/*    */ public class RuntimeIOException
/*    */   extends ApnsException
/*    */ {
/*    */   private static final long serialVersionUID = 8665285084049041306L;
/*    */   
/*    */   public RuntimeIOException() {}
/*    */   
/*    */   public RuntimeIOException(String message) {
/* 47 */     super(message);
/* 48 */   } public RuntimeIOException(IOException cause) { super(cause); } public RuntimeIOException(String m, IOException c) {
/* 49 */     super(m, c);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/notnoop/exceptions/RuntimeIOException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
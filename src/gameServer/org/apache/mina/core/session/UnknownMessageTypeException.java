/*    */ package org.apache.mina.core.session;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnknownMessageTypeException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 3257290227428047158L;
/*    */   
/*    */   public UnknownMessageTypeException() {}
/*    */   
/*    */   public UnknownMessageTypeException(String message, Throwable cause) {
/* 35 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public UnknownMessageTypeException(String message) {
/* 39 */     super(message);
/*    */   }
/*    */   
/*    */   public UnknownMessageTypeException(Throwable cause) {
/* 43 */     super(cause);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/session/UnknownMessageTypeException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
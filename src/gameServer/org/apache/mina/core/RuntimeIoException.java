/*    */ package org.apache.mina.core;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RuntimeIoException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 9029092241311939548L;
/*    */   
/*    */   public RuntimeIoException() {}
/*    */   
/*    */   public RuntimeIoException(String message) {
/* 41 */     super(message);
/*    */   }
/*    */   
/*    */   public RuntimeIoException(String message, Throwable cause) {
/* 45 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public RuntimeIoException(Throwable cause) {
/* 49 */     super(cause);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/RuntimeIoException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
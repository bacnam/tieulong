/*    */ package org.apache.mina.filter.keepalive;
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
/*    */ public class KeepAliveRequestTimeoutException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -1985092764656546558L;
/*    */   
/*    */   public KeepAliveRequestTimeoutException() {}
/*    */   
/*    */   public KeepAliveRequestTimeoutException(String message, Throwable cause) {
/* 37 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public KeepAliveRequestTimeoutException(String message) {
/* 41 */     super(message);
/*    */   }
/*    */   
/*    */   public KeepAliveRequestTimeoutException(Throwable cause) {
/* 45 */     super(cause);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/keepalive/KeepAliveRequestTimeoutException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
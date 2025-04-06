/*    */ package org.apache.mina.core.filterchain;
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
/*    */ public class IoFilterLifeCycleException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -5542098881633506449L;
/*    */   
/*    */   public IoFilterLifeCycleException() {}
/*    */   
/*    */   public IoFilterLifeCycleException(String message) {
/* 36 */     super(message);
/*    */   }
/*    */   
/*    */   public IoFilterLifeCycleException(String message, Throwable cause) {
/* 40 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public IoFilterLifeCycleException(Throwable cause) {
/* 44 */     super(cause);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/filterchain/IoFilterLifeCycleException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
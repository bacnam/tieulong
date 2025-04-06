/*    */ package org.apache.mina.util;
/*    */ 
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ public class DefaultExceptionMonitor
/*    */   extends ExceptionMonitor
/*    */ {
/* 36 */   private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionMonitor.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void exceptionCaught(Throwable cause) {
/* 43 */     if (cause instanceof Error) {
/* 44 */       throw (Error)cause;
/*    */     }
/*    */     
/* 47 */     LOGGER.warn("Unexpected exception.", cause);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/DefaultExceptionMonitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
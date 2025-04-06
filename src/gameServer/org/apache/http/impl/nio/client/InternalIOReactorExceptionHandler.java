/*    */ package org.apache.http.impl.nio.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.http.nio.reactor.IOReactorExceptionHandler;
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
/*    */ class InternalIOReactorExceptionHandler
/*    */   implements IOReactorExceptionHandler
/*    */ {
/*    */   private final Log log;
/*    */   
/*    */   InternalIOReactorExceptionHandler(Log log) {
/* 40 */     this.log = log;
/*    */   }
/*    */   
/*    */   public boolean handle(IOException ex) {
/* 44 */     this.log.error("Fatal I/O error", ex);
/* 45 */     return false;
/*    */   }
/*    */   
/*    */   public boolean handle(RuntimeException ex) {
/* 49 */     this.log.error("Fatal runtime error", ex);
/* 50 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/client/InternalIOReactorExceptionHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
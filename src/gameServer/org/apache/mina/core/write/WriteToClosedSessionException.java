/*    */ package org.apache.mina.core.write;
/*    */ 
/*    */ import java.util.Collection;
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
/*    */ public class WriteToClosedSessionException
/*    */   extends WriteException
/*    */ {
/*    */   private static final long serialVersionUID = 5550204573739301393L;
/*    */   
/*    */   public WriteToClosedSessionException(Collection<WriteRequest> requests, String message, Throwable cause) {
/* 35 */     super(requests, message, cause);
/*    */   }
/*    */   
/*    */   public WriteToClosedSessionException(Collection<WriteRequest> requests, String s) {
/* 39 */     super(requests, s);
/*    */   }
/*    */   
/*    */   public WriteToClosedSessionException(Collection<WriteRequest> requests, Throwable cause) {
/* 43 */     super(requests, cause);
/*    */   }
/*    */   
/*    */   public WriteToClosedSessionException(Collection<WriteRequest> requests) {
/* 47 */     super(requests);
/*    */   }
/*    */   
/*    */   public WriteToClosedSessionException(WriteRequest request, String message, Throwable cause) {
/* 51 */     super(request, message, cause);
/*    */   }
/*    */   
/*    */   public WriteToClosedSessionException(WriteRequest request, String s) {
/* 55 */     super(request, s);
/*    */   }
/*    */   
/*    */   public WriteToClosedSessionException(WriteRequest request, Throwable cause) {
/* 59 */     super(request, cause);
/*    */   }
/*    */   
/*    */   public WriteToClosedSessionException(WriteRequest request) {
/* 63 */     super(request);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/write/WriteToClosedSessionException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
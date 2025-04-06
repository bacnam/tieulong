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
/*    */ 
/*    */ public class WriteTimeoutException
/*    */   extends WriteException
/*    */ {
/*    */   private static final long serialVersionUID = 3906931157944579121L;
/*    */   
/*    */   public WriteTimeoutException(Collection<WriteRequest> requests, String message, Throwable cause) {
/* 36 */     super(requests, message, cause);
/*    */   }
/*    */   
/*    */   public WriteTimeoutException(Collection<WriteRequest> requests, String s) {
/* 40 */     super(requests, s);
/*    */   }
/*    */   
/*    */   public WriteTimeoutException(Collection<WriteRequest> requests, Throwable cause) {
/* 44 */     super(requests, cause);
/*    */   }
/*    */   
/*    */   public WriteTimeoutException(Collection<WriteRequest> requests) {
/* 48 */     super(requests);
/*    */   }
/*    */   
/*    */   public WriteTimeoutException(WriteRequest request, String message, Throwable cause) {
/* 52 */     super(request, message, cause);
/*    */   }
/*    */   
/*    */   public WriteTimeoutException(WriteRequest request, String s) {
/* 56 */     super(request, s);
/*    */   }
/*    */   
/*    */   public WriteTimeoutException(WriteRequest request, Throwable cause) {
/* 60 */     super(request, cause);
/*    */   }
/*    */   
/*    */   public WriteTimeoutException(WriteRequest request) {
/* 64 */     super(request);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/write/WriteTimeoutException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
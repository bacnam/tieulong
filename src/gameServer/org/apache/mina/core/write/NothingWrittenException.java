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
/*    */ public class NothingWrittenException
/*    */   extends WriteException
/*    */ {
/*    */   private static final long serialVersionUID = -6331979307737691005L;
/*    */   
/*    */   public NothingWrittenException(Collection<WriteRequest> requests, String message, Throwable cause) {
/* 35 */     super(requests, message, cause);
/*    */   }
/*    */   
/*    */   public NothingWrittenException(Collection<WriteRequest> requests, String s) {
/* 39 */     super(requests, s);
/*    */   }
/*    */   
/*    */   public NothingWrittenException(Collection<WriteRequest> requests, Throwable cause) {
/* 43 */     super(requests, cause);
/*    */   }
/*    */   
/*    */   public NothingWrittenException(Collection<WriteRequest> requests) {
/* 47 */     super(requests);
/*    */   }
/*    */   
/*    */   public NothingWrittenException(WriteRequest request, String message, Throwable cause) {
/* 51 */     super(request, message, cause);
/*    */   }
/*    */   
/*    */   public NothingWrittenException(WriteRequest request, String s) {
/* 55 */     super(request, s);
/*    */   }
/*    */   
/*    */   public NothingWrittenException(WriteRequest request, Throwable cause) {
/* 59 */     super(request, cause);
/*    */   }
/*    */   
/*    */   public NothingWrittenException(WriteRequest request) {
/* 63 */     super(request);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/write/NothingWrittenException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
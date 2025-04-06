/*    */ package org.apache.mina.core.write;
/*    */ 
/*    */ import java.net.SocketAddress;
/*    */ import org.apache.mina.core.future.WriteFuture;
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
/*    */ public class WriteRequestWrapper
/*    */   implements WriteRequest
/*    */ {
/*    */   private final WriteRequest parentRequest;
/*    */   
/*    */   public WriteRequestWrapper(WriteRequest parentRequest) {
/* 39 */     if (parentRequest == null) {
/* 40 */       throw new IllegalArgumentException("parentRequest");
/*    */     }
/* 42 */     this.parentRequest = parentRequest;
/*    */   }
/*    */   
/*    */   public SocketAddress getDestination() {
/* 46 */     return this.parentRequest.getDestination();
/*    */   }
/*    */   
/*    */   public WriteFuture getFuture() {
/* 50 */     return this.parentRequest.getFuture();
/*    */   }
/*    */   
/*    */   public Object getMessage() {
/* 54 */     return this.parentRequest.getMessage();
/*    */   }
/*    */   
/*    */   public WriteRequest getOriginalRequest() {
/* 58 */     return this.parentRequest.getOriginalRequest();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public WriteRequest getParentRequest() {
/* 65 */     return this.parentRequest;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 70 */     return "WR Wrapper" + this.parentRequest.toString();
/*    */   }
/*    */   
/*    */   public boolean isEncoded() {
/* 74 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/write/WriteRequestWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package org.apache.mina.filter.util;
/*    */ 
/*    */ import org.apache.mina.core.filterchain.IoFilter;
/*    */ import org.apache.mina.core.filterchain.IoFilterAdapter;
/*    */ import org.apache.mina.core.session.IoSession;
/*    */ import org.apache.mina.core.write.WriteRequest;
/*    */ import org.apache.mina.core.write.WriteRequestWrapper;
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
/*    */ public abstract class WriteRequestFilter
/*    */   extends IoFilterAdapter
/*    */ {
/*    */   public void filterWrite(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/* 39 */     Object filteredMessage = doFilterWrite(nextFilter, session, writeRequest);
/* 40 */     if (filteredMessage != null && filteredMessage != writeRequest.getMessage()) {
/* 41 */       nextFilter.filterWrite(session, (WriteRequest)new FilteredWriteRequest(filteredMessage, writeRequest));
/*    */     } else {
/* 43 */       nextFilter.filterWrite(session, writeRequest);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/* 49 */     if (writeRequest instanceof FilteredWriteRequest) {
/* 50 */       FilteredWriteRequest req = (FilteredWriteRequest)writeRequest;
/* 51 */       if (req.getParent() == this) {
/* 52 */         nextFilter.messageSent(session, req.getParentRequest());
/*    */         
/*    */         return;
/*    */       } 
/*    */     } 
/* 57 */     nextFilter.messageSent(session, writeRequest);
/*    */   }
/*    */   
/*    */   protected abstract Object doFilterWrite(IoFilter.NextFilter paramNextFilter, IoSession paramIoSession, WriteRequest paramWriteRequest) throws Exception;
/*    */   
/*    */   private class FilteredWriteRequest
/*    */     extends WriteRequestWrapper {
/*    */     private final Object filteredMessage;
/*    */     
/*    */     public FilteredWriteRequest(Object filteredMessage, WriteRequest writeRequest) {
/* 67 */       super(writeRequest);
/*    */       
/* 69 */       if (filteredMessage == null) {
/* 70 */         throw new IllegalArgumentException("filteredMessage");
/*    */       }
/* 72 */       this.filteredMessage = filteredMessage;
/*    */     }
/*    */     
/*    */     public WriteRequestFilter getParent() {
/* 76 */       return WriteRequestFilter.this;
/*    */     }
/*    */ 
/*    */     
/*    */     public Object getMessage() {
/* 81 */       return this.filteredMessage;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/util/WriteRequestFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
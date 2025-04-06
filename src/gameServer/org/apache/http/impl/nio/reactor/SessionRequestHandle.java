/*    */ package org.apache.http.impl.nio.reactor;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.util.Args;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class SessionRequestHandle
/*    */ {
/*    */   private final SessionRequestImpl sessionRequest;
/*    */   private final long requestTime;
/*    */   
/*    */   public SessionRequestHandle(SessionRequestImpl sessionRequest) {
/* 48 */     Args.notNull(sessionRequest, "Session request");
/* 49 */     this.sessionRequest = sessionRequest;
/* 50 */     this.requestTime = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   public SessionRequestImpl getSessionRequest() {
/* 54 */     return this.sessionRequest;
/*    */   }
/*    */   
/*    */   public long getRequestTime() {
/* 58 */     return this.requestTime;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/reactor/SessionRequestHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
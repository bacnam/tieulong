/*     */ package org.apache.mina.core.filterchain;
/*     */ 
/*     */ import org.apache.mina.core.session.IdleStatus;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.write.WriteRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IoFilterAdapter
/*     */   implements IoFilter
/*     */ {
/*     */   public void init() throws Exception {}
/*     */   
/*     */   public void destroy() throws Exception {}
/*     */   
/*     */   public void onPreAdd(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {}
/*     */   
/*     */   public void onPostAdd(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {}
/*     */   
/*     */   public void onPreRemove(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {}
/*     */   
/*     */   public void onPostRemove(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {}
/*     */   
/*     */   public void sessionCreated(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/*  74 */     nextFilter.sessionCreated(session);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionOpened(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/*  81 */     nextFilter.sessionOpened(session);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionClosed(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/*  88 */     nextFilter.sessionClosed(session);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sessionIdle(IoFilter.NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception {
/*  95 */     nextFilter.sessionIdle(session, status);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void exceptionCaught(IoFilter.NextFilter nextFilter, IoSession session, Throwable cause) throws Exception {
/* 102 */     nextFilter.exceptionCaught(session, cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) throws Exception {
/* 109 */     nextFilter.messageReceived(session, message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/* 116 */     nextFilter.messageSent(session, writeRequest);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void filterWrite(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/* 123 */     nextFilter.filterWrite(session, writeRequest);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void filterClose(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 130 */     nextFilter.filterClose(session);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void inputClosed(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 137 */     nextFilter.inputClosed(session);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 141 */     return getClass().getSimpleName();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/filterchain/IoFilterAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
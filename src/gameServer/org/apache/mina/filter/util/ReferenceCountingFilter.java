/*     */ package org.apache.mina.filter.util;
/*     */ 
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.filterchain.IoFilterAdapter;
/*     */ import org.apache.mina.core.filterchain.IoFilterChain;
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
/*     */ public class ReferenceCountingFilter
/*     */   extends IoFilterAdapter
/*     */ {
/*     */   private final IoFilter filter;
/*  39 */   private int count = 0;
/*     */   
/*     */   public ReferenceCountingFilter(IoFilter filter) {
/*  42 */     this.filter = filter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() throws Exception {}
/*     */ 
/*     */   
/*     */   public void destroy() throws Exception {}
/*     */ 
/*     */   
/*     */   public synchronized void onPreAdd(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {
/*  54 */     if (0 == this.count) {
/*  55 */       this.filter.init();
/*     */     }
/*     */     
/*  58 */     this.count++;
/*     */     
/*  60 */     this.filter.onPreAdd(parent, name, nextFilter);
/*     */   }
/*     */   
/*     */   public synchronized void onPostRemove(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {
/*  64 */     this.filter.onPostRemove(parent, name, nextFilter);
/*     */     
/*  66 */     this.count--;
/*     */     
/*  68 */     if (0 == this.count) {
/*  69 */       this.filter.destroy();
/*     */     }
/*     */   }
/*     */   
/*     */   public void exceptionCaught(IoFilter.NextFilter nextFilter, IoSession session, Throwable cause) throws Exception {
/*  74 */     this.filter.exceptionCaught(nextFilter, session, cause);
/*     */   }
/*     */   
/*     */   public void filterClose(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/*  78 */     this.filter.filterClose(nextFilter, session);
/*     */   }
/*     */   
/*     */   public void filterWrite(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/*  82 */     this.filter.filterWrite(nextFilter, session, writeRequest);
/*     */   }
/*     */   
/*     */   public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) throws Exception {
/*  86 */     this.filter.messageReceived(nextFilter, session, message);
/*     */   }
/*     */   
/*     */   public void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/*  90 */     this.filter.messageSent(nextFilter, session, writeRequest);
/*     */   }
/*     */   
/*     */   public void onPostAdd(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {
/*  94 */     this.filter.onPostAdd(parent, name, nextFilter);
/*     */   }
/*     */   
/*     */   public void onPreRemove(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {
/*  98 */     this.filter.onPreRemove(parent, name, nextFilter);
/*     */   }
/*     */   
/*     */   public void sessionClosed(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 102 */     this.filter.sessionClosed(nextFilter, session);
/*     */   }
/*     */   
/*     */   public void sessionCreated(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 106 */     this.filter.sessionCreated(nextFilter, session);
/*     */   }
/*     */   
/*     */   public void sessionIdle(IoFilter.NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception {
/* 110 */     this.filter.sessionIdle(nextFilter, session, status);
/*     */   }
/*     */   
/*     */   public void sessionOpened(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 114 */     this.filter.sessionOpened(nextFilter, session);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/util/ReferenceCountingFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
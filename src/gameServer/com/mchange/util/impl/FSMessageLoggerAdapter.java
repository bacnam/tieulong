/*    */ package com.mchange.util.impl;
/*    */ 
/*    */ import com.mchange.util.FailSuppressedMessageLogger;
/*    */ import com.mchange.util.MessageLogger;
/*    */ import java.io.IOException;
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
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
/*    */ public class FSMessageLoggerAdapter
/*    */   implements FailSuppressedMessageLogger
/*    */ {
/*    */   MessageLogger inner;
/* 48 */   List failures = null;
/*    */   
/*    */   public FSMessageLoggerAdapter(MessageLogger paramMessageLogger) {
/* 51 */     this.inner = paramMessageLogger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void log(String paramString) {
/*    */     try {
/* 57 */       this.inner.log(paramString);
/* 58 */     } catch (IOException iOException) {
/* 59 */       addFailure(iOException);
/*    */     } 
/*    */   }
/*    */   public void log(Throwable paramThrowable, String paramString) {
/*    */     try {
/* 64 */       this.inner.log(paramThrowable, paramString);
/* 65 */     } catch (IOException iOException) {
/* 66 */       addFailure(iOException);
/*    */     } 
/*    */   }
/*    */   
/*    */   public synchronized Iterator getFailures() {
/* 71 */     if (this.inner instanceof FailSuppressedMessageLogger)
/* 72 */       return ((FailSuppressedMessageLogger)this.inner).getFailures(); 
/* 73 */     return (this.failures != null) ? this.failures.iterator() : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void clearFailures() {
/* 78 */     if (this.inner instanceof FailSuppressedMessageLogger)
/* 79 */     { ((FailSuppressedMessageLogger)this.inner).clearFailures(); }
/* 80 */     else { this.failures = null; }
/*    */   
/*    */   }
/*    */   
/*    */   private synchronized void addFailure(IOException paramIOException) {
/* 85 */     if (this.failures == null)
/* 86 */       this.failures = new LinkedList(); 
/* 87 */     this.failures.add(paramIOException);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/FSMessageLoggerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
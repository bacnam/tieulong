/*    */ package org.apache.http.impl.nio.reactor;
/*    */ 
/*    */ import java.util.Date;
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ public class ExceptionEvent
/*    */ {
/*    */   private final Throwable ex;
/*    */   private final long time;
/*    */   
/*    */   public ExceptionEvent(Throwable ex, Date timestamp) {
/* 47 */     this.ex = ex;
/* 48 */     if (timestamp != null) {
/* 49 */       this.time = timestamp.getTime();
/*    */     } else {
/* 51 */       this.time = 0L;
/*    */     } 
/*    */   }
/*    */   
/*    */   public ExceptionEvent(Exception ex) {
/* 56 */     this(ex, new Date());
/*    */   }
/*    */   
/*    */   public Throwable getCause() {
/* 60 */     return this.ex;
/*    */   }
/*    */   
/*    */   public Date getTimestamp() {
/* 64 */     return new Date(this.time);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 69 */     StringBuilder buffer = new StringBuilder();
/* 70 */     buffer.append(new Date(this.time));
/* 71 */     buffer.append(" ");
/* 72 */     buffer.append(this.ex);
/* 73 */     return buffer.toString();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/nio/reactor/ExceptionEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package ch.qos.logback.core.util;
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
/*    */ public class FixedDelay
/*    */   implements DelayStrategy
/*    */ {
/*    */   private final long subsequentDelay;
/*    */   private long nextDelay;
/*    */   
/*    */   public FixedDelay(long initialDelay, long subsequentDelay) {
/* 35 */     String s = new String();
/* 36 */     this.nextDelay = initialDelay;
/* 37 */     this.subsequentDelay = subsequentDelay;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FixedDelay(int delay) {
/* 47 */     this(delay, delay);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long nextDelay() {
/* 54 */     long delay = this.nextDelay;
/* 55 */     this.nextDelay = this.subsequentDelay;
/* 56 */     return delay;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/util/FixedDelay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
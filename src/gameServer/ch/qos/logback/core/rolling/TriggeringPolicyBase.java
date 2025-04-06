/*    */ package ch.qos.logback.core.rolling;
/*    */ 
/*    */ import ch.qos.logback.core.spi.ContextAwareBase;
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
/*    */ public abstract class TriggeringPolicyBase<E>
/*    */   extends ContextAwareBase
/*    */   implements TriggeringPolicy<E>
/*    */ {
/*    */   private boolean start;
/*    */   
/*    */   public void start() {
/* 31 */     this.start = true;
/*    */   }
/*    */   
/*    */   public void stop() {
/* 35 */     this.start = false;
/*    */   }
/*    */   
/*    */   public boolean isStarted() {
/* 39 */     return this.start;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/rolling/TriggeringPolicyBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
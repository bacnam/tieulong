/*    */ package ch.qos.logback.core.boolex;
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
/*    */ public abstract class EventEvaluatorBase<E>
/*    */   extends ContextAwareBase
/*    */   implements EventEvaluator<E>
/*    */ {
/*    */   String name;
/*    */   boolean started;
/*    */   
/*    */   public String getName() {
/* 25 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 29 */     if (this.name != null) {
/* 30 */       throw new IllegalStateException("name has been already set");
/*    */     }
/* 32 */     this.name = name;
/*    */   }
/*    */   
/*    */   public boolean isStarted() {
/* 36 */     return this.started;
/*    */   }
/*    */   
/*    */   public void start() {
/* 40 */     this.started = true;
/*    */   }
/*    */   
/*    */   public void stop() {
/* 44 */     this.started = false;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/boolex/EventEvaluatorBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
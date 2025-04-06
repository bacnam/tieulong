/*    */ package ch.qos.logback.classic;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.AsyncAppenderBase;
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
/*    */ public class AsyncAppender
/*    */   extends AsyncAppenderBase<ILoggingEvent>
/*    */ {
/*    */   boolean includeCallerData = false;
/*    */   
/*    */   protected boolean isDiscardable(ILoggingEvent event) {
/* 40 */     Level level = event.getLevel();
/* 41 */     return (level.toInt() <= 20000);
/*    */   }
/*    */   
/*    */   protected void preprocess(ILoggingEvent eventObject) {
/* 45 */     eventObject.prepareForDeferredProcessing();
/* 46 */     if (this.includeCallerData)
/* 47 */       eventObject.getCallerData(); 
/*    */   }
/*    */   
/*    */   public boolean isIncludeCallerData() {
/* 51 */     return this.includeCallerData;
/*    */   }
/*    */   
/*    */   public void setIncludeCallerData(boolean includeCallerData) {
/* 55 */     this.includeCallerData = includeCallerData;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/classic/AsyncAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
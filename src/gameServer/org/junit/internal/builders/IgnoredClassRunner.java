/*    */ package org.junit.internal.builders;
/*    */ 
/*    */ import org.junit.runner.Description;
/*    */ import org.junit.runner.Runner;
/*    */ import org.junit.runner.notification.RunNotifier;
/*    */ 
/*    */ public class IgnoredClassRunner extends Runner {
/*    */   private final Class<?> clazz;
/*    */   
/*    */   public IgnoredClassRunner(Class<?> testClass) {
/* 11 */     this.clazz = testClass;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run(RunNotifier notifier) {
/* 16 */     notifier.fireTestIgnored(getDescription());
/*    */   }
/*    */ 
/*    */   
/*    */   public Description getDescription() {
/* 21 */     return Description.createSuiteDescription(this.clazz);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/internal/builders/IgnoredClassRunner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
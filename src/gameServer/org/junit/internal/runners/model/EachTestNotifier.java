/*    */ package org.junit.internal.runners.model;
/*    */ 
/*    */ import org.junit.internal.AssumptionViolatedException;
/*    */ import org.junit.runner.Description;
/*    */ import org.junit.runner.notification.Failure;
/*    */ import org.junit.runner.notification.RunNotifier;
/*    */ import org.junit.runners.model.MultipleFailureException;
/*    */ 
/*    */ public class EachTestNotifier
/*    */ {
/*    */   private final RunNotifier notifier;
/*    */   private final Description description;
/*    */   
/*    */   public EachTestNotifier(RunNotifier notifier, Description description) {
/* 15 */     this.notifier = notifier;
/* 16 */     this.description = description;
/*    */   }
/*    */   
/*    */   public void addFailure(Throwable targetException) {
/* 20 */     if (targetException instanceof MultipleFailureException) {
/* 21 */       addMultipleFailureException((MultipleFailureException)targetException);
/*    */     } else {
/* 23 */       this.notifier.fireTestFailure(new Failure(this.description, targetException));
/*    */     } 
/*    */   }
/*    */   
/*    */   private void addMultipleFailureException(MultipleFailureException mfe) {
/* 28 */     for (Throwable each : mfe.getFailures()) {
/* 29 */       addFailure(each);
/*    */     }
/*    */   }
/*    */   
/*    */   public void addFailedAssumption(AssumptionViolatedException e) {
/* 34 */     this.notifier.fireTestAssumptionFailed(new Failure(this.description, (Throwable)e));
/*    */   }
/*    */   
/*    */   public void fireTestFinished() {
/* 38 */     this.notifier.fireTestFinished(this.description);
/*    */   }
/*    */   
/*    */   public void fireTestStarted() {
/* 42 */     this.notifier.fireTestStarted(this.description);
/*    */   }
/*    */   
/*    */   public void fireTestIgnored() {
/* 46 */     this.notifier.fireTestIgnored(this.description);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/internal/runners/model/EachTestNotifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
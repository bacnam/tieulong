/*    */ package org.junit.rules;
/*    */ 
/*    */ import org.junit.internal.AssumptionViolatedException;
/*    */ import org.junit.runners.model.FrameworkMethod;
/*    */ import org.junit.runners.model.Statement;
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
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class TestWatchman
/*    */   implements MethodRule
/*    */ {
/*    */   public Statement apply(final Statement base, final FrameworkMethod method, Object target) {
/* 48 */     return new Statement()
/*    */       {
/*    */         public void evaluate() throws Throwable {
/* 51 */           TestWatchman.this.starting(method);
/*    */           try {
/* 53 */             base.evaluate();
/* 54 */             TestWatchman.this.succeeded(method);
/* 55 */           } catch (AssumptionViolatedException e) {
/* 56 */             throw e;
/* 57 */           } catch (Throwable e) {
/* 58 */             TestWatchman.this.failed(e, method);
/* 59 */             throw e;
/*    */           } finally {
/* 61 */             TestWatchman.this.finished(method);
/*    */           } 
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public void succeeded(FrameworkMethod method) {}
/*    */   
/*    */   public void failed(Throwable e, FrameworkMethod method) {}
/*    */   
/*    */   public void starting(FrameworkMethod method) {}
/*    */   
/*    */   public void finished(FrameworkMethod method) {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/rules/TestWatchman.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
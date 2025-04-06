/*    */ package org.junit.internal.builders;
/*    */ 
/*    */ import org.junit.internal.runners.SuiteMethod;
/*    */ import org.junit.runner.Runner;
/*    */ import org.junit.runners.model.RunnerBuilder;
/*    */ 
/*    */ public class SuiteMethodBuilder
/*    */   extends RunnerBuilder {
/*    */   public Runner runnerForClass(Class<?> each) throws Throwable {
/* 10 */     if (hasSuiteMethod(each)) {
/* 11 */       return (Runner)new SuiteMethod(each);
/*    */     }
/* 13 */     return null;
/*    */   }
/*    */   
/*    */   public boolean hasSuiteMethod(Class<?> testClass) {
/*    */     try {
/* 18 */       testClass.getMethod("suite", new Class[0]);
/* 19 */     } catch (NoSuchMethodException e) {
/* 20 */       return false;
/*    */     } 
/* 22 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/internal/builders/SuiteMethodBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
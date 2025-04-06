/*    */ package org.junit.runner;
/*    */ 
/*    */ import org.junit.runners.Suite;
/*    */ import org.junit.runners.model.InitializationError;
/*    */ import org.junit.runners.model.RunnerBuilder;
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
/*    */ public class Computer
/*    */ {
/*    */   public static Computer serial() {
/* 19 */     return new Computer();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Runner getSuite(final RunnerBuilder builder, Class<?>[] classes) throws InitializationError {
/* 28 */     return (Runner)new Suite(new RunnerBuilder()
/*    */         {
/*    */           public Runner runnerForClass(Class<?> testClass) throws Throwable {
/* 31 */             return Computer.this.getRunner(builder, testClass);
/*    */           }
/*    */         }classes);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Runner getRunner(RunnerBuilder builder, Class<?> testClass) throws Throwable {
/* 40 */     return builder.runnerForClass(testClass);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/runner/Computer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
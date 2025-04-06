/*    */ package org.junit.rules;
/*    */ 
/*    */ import org.junit.runner.Description;
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
/*    */ public abstract class Verifier
/*    */   implements TestRule
/*    */ {
/*    */   public Statement apply(final Statement base, Description description) {
/* 32 */     return new Statement()
/*    */       {
/*    */         public void evaluate() throws Throwable {
/* 35 */           base.evaluate();
/* 36 */           Verifier.this.verify();
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   protected void verify() throws Throwable {}
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/rules/Verifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
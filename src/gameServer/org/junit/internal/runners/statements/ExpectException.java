/*    */ package org.junit.internal.runners.statements;
/*    */ 
/*    */ import org.junit.internal.AssumptionViolatedException;
/*    */ import org.junit.runners.model.Statement;
/*    */ 
/*    */ public class ExpectException extends Statement {
/*    */   private final Statement next;
/*    */   private final Class<? extends Throwable> expected;
/*    */   
/*    */   public ExpectException(Statement next, Class<? extends Throwable> expected) {
/* 11 */     this.next = next;
/* 12 */     this.expected = expected;
/*    */   }
/*    */ 
/*    */   
/*    */   public void evaluate() throws Exception {
/* 17 */     boolean complete = false;
/*    */     try {
/* 19 */       this.next.evaluate();
/* 20 */       complete = true;
/* 21 */     } catch (AssumptionViolatedException e) {
/* 22 */       throw e;
/* 23 */     } catch (Throwable e) {
/* 24 */       if (!this.expected.isAssignableFrom(e.getClass())) {
/* 25 */         String message = "Unexpected exception, expected<" + this.expected.getName() + "> but was<" + e.getClass().getName() + ">";
/*    */ 
/*    */         
/* 28 */         throw new Exception(message, e);
/*    */       } 
/*    */     } 
/* 31 */     if (complete)
/* 32 */       throw new AssertionError("Expected exception: " + this.expected.getName()); 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/internal/runners/statements/ExpectException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
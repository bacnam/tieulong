/*    */ package org.junit.internal.runners.statements;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.junit.runners.model.FrameworkMethod;
/*    */ import org.junit.runners.model.MultipleFailureException;
/*    */ import org.junit.runners.model.Statement;
/*    */ 
/*    */ 
/*    */ public class RunAfters
/*    */   extends Statement
/*    */ {
/*    */   private final Statement next;
/*    */   private final Object target;
/*    */   private final List<FrameworkMethod> afters;
/*    */   
/*    */   public RunAfters(Statement next, List<FrameworkMethod> afters, Object target) {
/* 18 */     this.next = next;
/* 19 */     this.afters = afters;
/* 20 */     this.target = target;
/*    */   }
/*    */ 
/*    */   
/*    */   public void evaluate() throws Throwable {
/* 25 */     List<Throwable> errors = new ArrayList<Throwable>();
/*    */     try {
/* 27 */       this.next.evaluate();
/* 28 */     } catch (Throwable e) {
/* 29 */       errors.add(e);
/*    */     } finally {
/* 31 */       for (FrameworkMethod each : this.afters) {
/*    */         try {
/* 33 */           each.invokeExplosively(this.target, new Object[0]);
/* 34 */         } catch (Throwable e) {
/* 35 */           errors.add(e);
/*    */         } 
/*    */       } 
/*    */     } 
/* 39 */     MultipleFailureException.assertEmpty(errors);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/internal/runners/statements/RunAfters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
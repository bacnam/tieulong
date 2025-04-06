/*    */ package org.junit;
/*    */ 
/*    */ import org.hamcrest.Matcher;
/*    */ import org.junit.internal.AssumptionViolatedException;
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
/*    */ public class AssumptionViolatedException
/*    */   extends AssumptionViolatedException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public <T> AssumptionViolatedException(T actual, Matcher<T> matcher) {
/* 22 */     super(actual, matcher);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> AssumptionViolatedException(String message, T expected, Matcher<T> matcher) {
/* 30 */     super(message, expected, matcher);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AssumptionViolatedException(String message) {
/* 37 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AssumptionViolatedException(String assumption, Throwable t) {
/* 44 */     super(assumption, t);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/AssumptionViolatedException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
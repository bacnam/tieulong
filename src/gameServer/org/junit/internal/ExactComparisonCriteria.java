/*   */ package org.junit.internal;
/*   */ 
/*   */ import org.junit.Assert;
/*   */ 
/*   */ public class ExactComparisonCriteria
/*   */   extends ComparisonCriteria {
/*   */   protected void assertElementsEqual(Object expected, Object actual) {
/* 8 */     Assert.assertEquals(expected, actual);
/*   */   }
/*   */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/internal/ExactComparisonCriteria.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
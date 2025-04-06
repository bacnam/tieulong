/*    */ package org.junit.internal;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ import java.util.Arrays;
/*    */ import org.junit.Assert;
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
/*    */ public abstract class ComparisonCriteria
/*    */ {
/*    */   public void arrayEquals(String message, Object expecteds, Object actuals) throws ArrayComparisonFailure {
/* 28 */     if (expecteds == actuals || Arrays.deepEquals(new Object[] { expecteds }, new Object[] { actuals })) {
/*    */       return;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 35 */     String header = (message == null) ? "" : (message + ": ");
/*    */     
/* 37 */     int expectedsLength = assertArraysAreSameLength(expecteds, actuals, header);
/*    */ 
/*    */     
/* 40 */     for (int i = 0; i < expectedsLength; i++) {
/* 41 */       Object expected = Array.get(expecteds, i);
/* 42 */       Object actual = Array.get(actuals, i);
/*    */       
/* 44 */       if (isArray(expected) && isArray(actual)) {
/*    */         try {
/* 46 */           arrayEquals(message, expected, actual);
/* 47 */         } catch (ArrayComparisonFailure e) {
/* 48 */           e.addDimension(i);
/* 49 */           throw e;
/*    */         } 
/*    */       } else {
/*    */         try {
/* 53 */           assertElementsEqual(expected, actual);
/* 54 */         } catch (AssertionError e) {
/* 55 */           throw new ArrayComparisonFailure(header, e, i);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean isArray(Object expected) {
/* 62 */     return (expected != null && expected.getClass().isArray());
/*    */   }
/*    */ 
/*    */   
/*    */   private int assertArraysAreSameLength(Object expecteds, Object actuals, String header) {
/* 67 */     if (expecteds == null) {
/* 68 */       Assert.fail(header + "expected array was null");
/*    */     }
/* 70 */     if (actuals == null) {
/* 71 */       Assert.fail(header + "actual array was null");
/*    */     }
/* 73 */     int actualsLength = Array.getLength(actuals);
/* 74 */     int expectedsLength = Array.getLength(expecteds);
/* 75 */     if (actualsLength != expectedsLength) {
/* 76 */       Assert.fail(header + "array lengths differed, expected.length=" + expectedsLength + " actual.length=" + actualsLength);
/*    */     }
/*    */     
/* 79 */     return expectedsLength;
/*    */   }
/*    */   
/*    */   protected abstract void assertElementsEqual(Object paramObject1, Object paramObject2);
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/internal/ComparisonCriteria.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
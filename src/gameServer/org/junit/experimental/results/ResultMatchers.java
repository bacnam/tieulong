/*    */ package org.junit.experimental.results;
/*    */ 
/*    */ import org.hamcrest.BaseMatcher;
/*    */ import org.hamcrest.Description;
/*    */ import org.hamcrest.Matcher;
/*    */ import org.hamcrest.TypeSafeMatcher;
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
/*    */ public class ResultMatchers
/*    */ {
/*    */   public static Matcher<PrintableResult> isSuccessful() {
/* 21 */     return failureCountIs(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Matcher<PrintableResult> failureCountIs(final int count) {
/* 28 */     return (Matcher<PrintableResult>)new TypeSafeMatcher<PrintableResult>() {
/*    */         public void describeTo(Description description) {
/* 30 */           description.appendText("has " + count + " failures");
/*    */         }
/*    */ 
/*    */         
/*    */         public boolean matchesSafely(PrintableResult item) {
/* 35 */           return (item.failureCount() == count);
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Matcher<Object> hasSingleFailureContaining(final String string) {
/* 44 */     return (Matcher<Object>)new BaseMatcher<Object>() {
/*    */         public boolean matches(Object item) {
/* 46 */           return (item.toString().contains(string) && ResultMatchers.failureCountIs(1).matches(item));
/*    */         }
/*    */         
/*    */         public void describeTo(Description description) {
/* 50 */           description.appendText("has single failure containing " + string);
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Matcher<PrintableResult> hasFailureContaining(final String string) {
/* 60 */     return (Matcher<PrintableResult>)new BaseMatcher<PrintableResult>() {
/*    */         public boolean matches(Object item) {
/* 62 */           return item.toString().contains(string);
/*    */         }
/*    */         
/*    */         public void describeTo(Description description) {
/* 66 */           description.appendText("has failure containing " + string);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/experimental/results/ResultMatchers.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
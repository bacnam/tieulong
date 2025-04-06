/*    */ package org.junit.rules;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.hamcrest.CoreMatchers;
/*    */ import org.hamcrest.Matcher;
/*    */ import org.junit.matchers.JUnitMatchers;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ExpectedExceptionMatcherBuilder
/*    */ {
/* 16 */   private final List<Matcher<?>> matchers = new ArrayList<Matcher<?>>();
/*    */   
/*    */   void add(Matcher<?> matcher) {
/* 19 */     this.matchers.add(matcher);
/*    */   }
/*    */   
/*    */   boolean expectsThrowable() {
/* 23 */     return !this.matchers.isEmpty();
/*    */   }
/*    */   
/*    */   Matcher<Throwable> build() {
/* 27 */     return JUnitMatchers.isThrowable(allOfTheMatchers());
/*    */   }
/*    */   
/*    */   private Matcher<Throwable> allOfTheMatchers() {
/* 31 */     if (this.matchers.size() == 1) {
/* 32 */       return cast(this.matchers.get(0));
/*    */     }
/* 34 */     return CoreMatchers.allOf(castedMatchers());
/*    */   }
/*    */ 
/*    */   
/*    */   private List<Matcher<? super Throwable>> castedMatchers() {
/* 39 */     return (List)new ArrayList<Matcher<?>>(this.matchers);
/*    */   }
/*    */ 
/*    */   
/*    */   private Matcher<Throwable> cast(Matcher<?> singleMatcher) {
/* 44 */     return (Matcher)singleMatcher;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/rules/ExpectedExceptionMatcherBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
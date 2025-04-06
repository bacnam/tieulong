/*     */ package org.junit.rules;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.junit.AssumptionViolatedException;
/*     */ import org.junit.internal.AssumptionViolatedException;
/*     */ import org.junit.runner.Description;
/*     */ import org.junit.runners.model.MultipleFailureException;
/*     */ import org.junit.runners.model.Statement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TestWatcher
/*     */   implements TestRule
/*     */ {
/*     */   public Statement apply(final Statement base, final Description description) {
/*  48 */     return new Statement()
/*     */       {
/*     */         public void evaluate() throws Throwable {
/*  51 */           List<Throwable> errors = new ArrayList<Throwable>();
/*     */           
/*  53 */           TestWatcher.this.startingQuietly(description, errors);
/*     */           try {
/*  55 */             base.evaluate();
/*  56 */             TestWatcher.this.succeededQuietly(description, errors);
/*  57 */           } catch (AssumptionViolatedException e) {
/*  58 */             errors.add(e);
/*  59 */             TestWatcher.this.skippedQuietly(e, description, errors);
/*  60 */           } catch (Throwable e) {
/*  61 */             errors.add(e);
/*  62 */             TestWatcher.this.failedQuietly(e, description, errors);
/*     */           } finally {
/*  64 */             TestWatcher.this.finishedQuietly(description, errors);
/*     */           } 
/*     */           
/*  67 */           MultipleFailureException.assertEmpty(errors);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   private void succeededQuietly(Description description, List<Throwable> errors) {
/*     */     try {
/*  75 */       succeeded(description);
/*  76 */     } catch (Throwable e) {
/*  77 */       errors.add(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void failedQuietly(Throwable e, Description description, List<Throwable> errors) {
/*     */     try {
/*  84 */       failed(e, description);
/*  85 */     } catch (Throwable e1) {
/*  86 */       errors.add(e1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void skippedQuietly(AssumptionViolatedException e, Description description, List<Throwable> errors) {
/*     */     try {
/*  95 */       if (e instanceof AssumptionViolatedException) {
/*  96 */         skipped((AssumptionViolatedException)e, description);
/*     */       } else {
/*  98 */         skipped(e, description);
/*     */       } 
/* 100 */     } catch (Throwable e1) {
/* 101 */       errors.add(e1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void startingQuietly(Description description, List<Throwable> errors) {
/*     */     try {
/* 108 */       starting(description);
/* 109 */     } catch (Throwable e) {
/* 110 */       errors.add(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void finishedQuietly(Description description, List<Throwable> errors) {
/*     */     try {
/* 117 */       finished(description);
/* 118 */     } catch (Throwable e) {
/* 119 */       errors.add(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void succeeded(Description description) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void failed(Throwable e, Description description) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void skipped(AssumptionViolatedException e, Description description) {
/* 141 */     AssumptionViolatedException assumptionViolatedException = e;
/* 142 */     skipped((AssumptionViolatedException)assumptionViolatedException, description);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected void skipped(AssumptionViolatedException e, Description description) {}
/*     */   
/*     */   protected void starting(Description description) {}
/*     */   
/*     */   protected void finished(Description description) {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/rules/TestWatcher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
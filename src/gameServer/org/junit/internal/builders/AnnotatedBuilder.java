/*     */ package org.junit.internal.builders;
/*     */ 
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.junit.runner.RunWith;
/*     */ import org.junit.runner.Runner;
/*     */ import org.junit.runners.model.InitializationError;
/*     */ import org.junit.runners.model.RunnerBuilder;
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
/*     */ public class AnnotatedBuilder
/*     */   extends RunnerBuilder
/*     */ {
/*     */   private static final String CONSTRUCTOR_ERROR_FORMAT = "Custom runner class %s should have a public constructor with signature %s(Class testClass)";
/*     */   private final RunnerBuilder suiteBuilder;
/*     */   
/*     */   public AnnotatedBuilder(RunnerBuilder suiteBuilder) {
/*  77 */     this.suiteBuilder = suiteBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   public Runner runnerForClass(Class<?> testClass) throws Exception {
/*  82 */     for (Class<?> currentTestClass = testClass; currentTestClass != null; 
/*  83 */       currentTestClass = getEnclosingClassForNonStaticMemberClass(currentTestClass)) {
/*  84 */       RunWith annotation = currentTestClass.<RunWith>getAnnotation(RunWith.class);
/*  85 */       if (annotation != null) {
/*  86 */         return buildRunner(annotation.value(), testClass);
/*     */       }
/*     */     } 
/*     */     
/*  90 */     return null;
/*     */   }
/*     */   
/*     */   private Class<?> getEnclosingClassForNonStaticMemberClass(Class<?> currentTestClass) {
/*  94 */     if (currentTestClass.isMemberClass() && !Modifier.isStatic(currentTestClass.getModifiers())) {
/*  95 */       return currentTestClass.getEnclosingClass();
/*     */     }
/*  97 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Runner buildRunner(Class<? extends Runner> runnerClass, Class<?> testClass) throws Exception {
/*     */     try {
/* 104 */       return runnerClass.getConstructor(new Class[] { Class.class }).newInstance(new Object[] { testClass });
/* 105 */     } catch (NoSuchMethodException e) {
/*     */       try {
/* 107 */         return runnerClass.getConstructor(new Class[] { Class.class, RunnerBuilder.class }).newInstance(new Object[] { testClass, this.suiteBuilder });
/*     */       }
/* 109 */       catch (NoSuchMethodException e2) {
/* 110 */         String simpleName = runnerClass.getSimpleName();
/* 111 */         throw new InitializationError(String.format("Custom runner class %s should have a public constructor with signature %s(Class testClass)", new Object[] { simpleName, simpleName }));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/internal/builders/AnnotatedBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
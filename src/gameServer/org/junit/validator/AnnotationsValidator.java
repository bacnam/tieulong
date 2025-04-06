/*     */ package org.junit.validator;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.junit.runners.model.Annotatable;
/*     */ import org.junit.runners.model.FrameworkField;
/*     */ import org.junit.runners.model.FrameworkMethod;
/*     */ import org.junit.runners.model.TestClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AnnotationsValidator
/*     */   implements TestClassValidator
/*     */ {
/*  22 */   private static final List<AnnotatableValidator<?>> VALIDATORS = Arrays.asList((AnnotatableValidator<?>[])new AnnotatableValidator[] { new ClassValidator(), new MethodValidator(), new FieldValidator() });
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
/*     */   public List<Exception> validateTestClass(TestClass testClass) {
/*  34 */     List<Exception> validationErrors = new ArrayList<Exception>();
/*  35 */     for (AnnotatableValidator<?> validator : VALIDATORS) {
/*  36 */       List<Exception> additionalErrors = validator.validateTestClass(testClass);
/*     */       
/*  38 */       validationErrors.addAll(additionalErrors);
/*     */     } 
/*  40 */     return validationErrors;
/*     */   }
/*     */   private static abstract class AnnotatableValidator<T extends Annotatable> { private AnnotatableValidator() {}
/*     */     
/*  44 */     private static final AnnotationValidatorFactory ANNOTATION_VALIDATOR_FACTORY = new AnnotationValidatorFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<Exception> validateTestClass(TestClass testClass) {
/*  52 */       List<Exception> validationErrors = new ArrayList<Exception>();
/*  53 */       for (Annotatable annotatable : getAnnotatablesForTestClass(testClass)) {
/*  54 */         List<Exception> additionalErrors = validateAnnotatable((T)annotatable);
/*  55 */         validationErrors.addAll(additionalErrors);
/*     */       } 
/*  57 */       return validationErrors;
/*     */     }
/*     */     abstract Iterable<T> getAnnotatablesForTestClass(TestClass param1TestClass);
/*     */     private List<Exception> validateAnnotatable(T annotatable) {
/*  61 */       List<Exception> validationErrors = new ArrayList<Exception>();
/*  62 */       for (Annotation annotation : annotatable.getAnnotations()) {
/*  63 */         Class<? extends Annotation> annotationType = annotation.annotationType();
/*     */         
/*  65 */         ValidateWith validateWith = annotationType.<ValidateWith>getAnnotation(ValidateWith.class);
/*     */         
/*  67 */         if (validateWith != null) {
/*  68 */           AnnotationValidator annotationValidator = ANNOTATION_VALIDATOR_FACTORY.createAnnotationValidator(validateWith);
/*     */           
/*  70 */           List<Exception> errors = validateAnnotatable(annotationValidator, annotatable);
/*     */           
/*  72 */           validationErrors.addAll(errors);
/*     */         } 
/*     */       } 
/*  75 */       return validationErrors;
/*     */     }
/*     */     
/*     */     abstract List<Exception> validateAnnotatable(AnnotationValidator param1AnnotationValidator, T param1T); }
/*     */   
/*     */   private static class ClassValidator extends AnnotatableValidator<TestClass> {
/*     */     Iterable<TestClass> getAnnotatablesForTestClass(TestClass testClass) {
/*  82 */       return Collections.singletonList(testClass);
/*     */     }
/*     */     
/*     */     private ClassValidator() {}
/*     */     
/*     */     List<Exception> validateAnnotatable(AnnotationValidator validator, TestClass testClass) {
/*  88 */       return validator.validateAnnotatedClass(testClass);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class MethodValidator
/*     */     extends AnnotatableValidator<FrameworkMethod> {
/*     */     private MethodValidator() {}
/*     */     
/*     */     Iterable<FrameworkMethod> getAnnotatablesForTestClass(TestClass testClass) {
/*  97 */       return testClass.getAnnotatedMethods();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     List<Exception> validateAnnotatable(AnnotationValidator validator, FrameworkMethod method) {
/* 103 */       return validator.validateAnnotatedMethod(method);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FieldValidator extends AnnotatableValidator<FrameworkField> {
/*     */     private FieldValidator() {}
/*     */     
/*     */     Iterable<FrameworkField> getAnnotatablesForTestClass(TestClass testClass) {
/* 111 */       return testClass.getAnnotatedFields();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     List<Exception> validateAnnotatable(AnnotationValidator validator, FrameworkField field) {
/* 117 */       return validator.validateAnnotatedField(field);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/validator/AnnotationsValidator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package org.junit.validator;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnnotationValidatorFactory
/*    */ {
/* 11 */   private static final ConcurrentHashMap<ValidateWith, AnnotationValidator> VALIDATORS_FOR_ANNOTATION_TYPES = new ConcurrentHashMap<ValidateWith, AnnotationValidator>();
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
/*    */   public AnnotationValidator createAnnotationValidator(ValidateWith validateWithAnnotation) {
/* 24 */     AnnotationValidator validator = VALIDATORS_FOR_ANNOTATION_TYPES.get(validateWithAnnotation);
/* 25 */     if (validator != null) {
/* 26 */       return validator;
/*    */     }
/*    */     
/* 29 */     Class<? extends AnnotationValidator> clazz = validateWithAnnotation.value();
/* 30 */     if (clazz == null) {
/* 31 */       throw new IllegalArgumentException("Can't create validator, value is null in annotation " + validateWithAnnotation.getClass().getName());
/*    */     }
/*    */     try {
/* 34 */       AnnotationValidator annotationValidator = clazz.newInstance();
/* 35 */       VALIDATORS_FOR_ANNOTATION_TYPES.putIfAbsent(validateWithAnnotation, annotationValidator);
/* 36 */       return VALIDATORS_FOR_ANNOTATION_TYPES.get(validateWithAnnotation);
/* 37 */     } catch (Exception e) {
/* 38 */       throw new RuntimeException("Exception received when creating AnnotationValidator class " + clazz.getName(), e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/validator/AnnotationValidatorFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
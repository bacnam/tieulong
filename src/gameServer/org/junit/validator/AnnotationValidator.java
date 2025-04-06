/*    */ package org.junit.validator;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.junit.runners.model.FrameworkField;
/*    */ import org.junit.runners.model.FrameworkMethod;
/*    */ import org.junit.runners.model.TestClass;
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
/*    */ public abstract class AnnotationValidator
/*    */ {
/* 22 */   private static final List<Exception> NO_VALIDATION_ERRORS = Collections.emptyList();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Exception> validateAnnotatedClass(TestClass testClass) {
/* 33 */     return NO_VALIDATION_ERRORS;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Exception> validateAnnotatedField(FrameworkField field) {
/* 45 */     return NO_VALIDATION_ERRORS;
/*    */   }
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
/*    */   public List<Exception> validateAnnotatedMethod(FrameworkMethod method) {
/* 58 */     return NO_VALIDATION_ERRORS;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/validator/AnnotationValidator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
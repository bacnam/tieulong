/*    */ package org.slf4j.helpers;
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
/*    */ public class FormattingTuple
/*    */ {
/* 34 */   public static FormattingTuple NULL = new FormattingTuple(null);
/*    */   
/*    */   private String message;
/*    */   private Throwable throwable;
/*    */   private Object[] argArray;
/*    */   
/*    */   public FormattingTuple(String message) {
/* 41 */     this(message, null, null);
/*    */   }
/*    */   
/*    */   public FormattingTuple(String message, Object[] argArray, Throwable throwable) {
/* 45 */     this.message = message;
/* 46 */     this.throwable = throwable;
/* 47 */     if (throwable == null) {
/* 48 */       this.argArray = argArray;
/*    */     } else {
/* 50 */       this.argArray = trimmedCopy(argArray);
/*    */     } 
/*    */   }
/*    */   
/*    */   static Object[] trimmedCopy(Object[] argArray) {
/* 55 */     if (argArray == null || argArray.length == 0) {
/* 56 */       throw new IllegalStateException("non-sensical empty or null argument array");
/*    */     }
/* 58 */     int trimemdLen = argArray.length - 1;
/* 59 */     Object[] trimmed = new Object[trimemdLen];
/* 60 */     System.arraycopy(argArray, 0, trimmed, 0, trimemdLen);
/* 61 */     return trimmed;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 65 */     return this.message;
/*    */   }
/*    */   
/*    */   public Object[] getArgArray() {
/* 69 */     return this.argArray;
/*    */   }
/*    */   
/*    */   public Throwable getThrowable() {
/* 73 */     return this.throwable;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/slf4j/helpers/FormattingTuple.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
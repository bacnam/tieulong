/*    */ package org.junit.experimental.theories.internal;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public class ParameterizedAssertionError
/*    */   extends AssertionError {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public ParameterizedAssertionError(Throwable targetException, String methodName, Object... params) {
/* 12 */     super(String.format("%s(%s)", new Object[] { methodName, join(", ", params) }));
/* 13 */     initCause(targetException);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 18 */     return (obj instanceof ParameterizedAssertionError && toString().equals(obj.toString()));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 23 */     return toString().hashCode();
/*    */   }
/*    */   
/*    */   public static String join(String delimiter, Object... params) {
/* 27 */     return join(delimiter, Arrays.asList(params));
/*    */   }
/*    */   
/*    */   public static String join(String delimiter, Collection<Object> values) {
/* 31 */     StringBuilder sb = new StringBuilder();
/* 32 */     Iterator<Object> iter = values.iterator();
/* 33 */     while (iter.hasNext()) {
/* 34 */       Object next = iter.next();
/* 35 */       sb.append(stringValueOf(next));
/* 36 */       if (iter.hasNext()) {
/* 37 */         sb.append(delimiter);
/*    */       }
/*    */     } 
/* 40 */     return sb.toString();
/*    */   }
/*    */   
/*    */   private static String stringValueOf(Object next) {
/*    */     try {
/* 45 */       return String.valueOf(next);
/* 46 */     } catch (Throwable e) {
/* 47 */       return "[toString failed]";
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/experimental/theories/internal/ParameterizedAssertionError.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.mchange.v2.lang.reflect;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Comparator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MethodUtils
/*    */ {
/* 43 */   public static final Comparator METHOD_COMPARATOR = new Comparator()
/*    */     {
/*    */       public int compare(Object param1Object1, Object param1Object2)
/*    */       {
/* 47 */         Method method1 = (Method)param1Object1;
/* 48 */         Method method2 = (Method)param1Object2;
/* 49 */         String str1 = method1.getName();
/* 50 */         String str2 = method2.getName();
/* 51 */         int i = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
/* 52 */         if (i == 0)
/*    */         {
/* 54 */           if (str1.equals(str2)) {
/*    */             
/* 56 */             Class[] arrayOfClass1 = method1.getParameterTypes();
/* 57 */             Class[] arrayOfClass2 = method2.getParameterTypes();
/* 58 */             if (arrayOfClass1.length < arrayOfClass2.length) {
/* 59 */               i = -1;
/* 60 */             } else if (arrayOfClass1.length > arrayOfClass2.length) {
/* 61 */               i = 1;
/*    */             } else {
/*    */               byte b; int j;
/* 64 */               for (b = 0, j = arrayOfClass1.length; b < j; b++) {
/*    */                 
/* 66 */                 String str3 = arrayOfClass1[b].getName();
/* 67 */                 String str4 = arrayOfClass2[b].getName();
/* 68 */                 i = str3.compareTo(str4);
/* 69 */                 if (i != 0) {
/*    */                   break;
/*    */                 }
/*    */               } 
/*    */             } 
/*    */           } else {
/*    */             
/* 76 */             i = str1.compareTo(str2);
/*    */           } 
/*    */         }
/* 79 */         return i;
/*    */       }
/*    */     };
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/lang/reflect/MethodUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
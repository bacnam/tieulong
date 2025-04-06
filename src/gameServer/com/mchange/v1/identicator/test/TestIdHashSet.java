/*    */ package com.mchange.v1.identicator.test;
/*    */ 
/*    */ import com.mchange.v1.identicator.IdHashSet;
/*    */ import com.mchange.v1.identicator.Identicator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TestIdHashSet
/*    */ {
/*    */   public static void main(String[] paramArrayOfString) {
/* 49 */     Identicator identicator = new Identicator()
/*    */       {
/*    */         public boolean identical(Object param1Object1, Object param1Object2) {
/* 52 */           return (((String)param1Object1).charAt(0) == ((String)param1Object2).charAt(0));
/*    */         }
/*    */         public int hash(Object param1Object) {
/* 55 */           return ((String)param1Object).charAt(0);
/*    */         }
/*    */       };
/* 58 */     IdHashSet<String> idHashSet = new IdHashSet(identicator);
/* 59 */     System.out.println(idHashSet.add("hello"));
/* 60 */     System.out.println(idHashSet.add("world"));
/* 61 */     System.out.println(idHashSet.add("hi"));
/* 62 */     System.out.println(idHashSet.size());
/* 63 */     Object[] arrayOfObject = idHashSet.toArray();
/* 64 */     for (byte b = 0; b < arrayOfObject.length; b++)
/* 65 */       System.out.println(arrayOfObject[b]); 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/identicator/test/TestIdHashSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
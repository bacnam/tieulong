/*    */ package com.mchange.v2.c3p0.test;
/*    */ 
/*    */ import com.mchange.v2.c3p0.ComboPooledDataSource;
/*    */ import com.mchange.v2.c3p0.impl.C3P0JavaBeanObjectFactory;
/*    */ import com.mchange.v2.naming.JavaBeanObjectFactory;
/*    */ import javax.naming.Reference;
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
/*    */ public final class JavaBeanRefTest
/*    */ {
/*    */   public static void main(String[] argv) {
/*    */     try {
/* 49 */       ComboPooledDataSource cpds = new ComboPooledDataSource();
/* 50 */       Reference ref = cpds.getReference();
/* 51 */       ComboPooledDataSource cpdsJBOF = (ComboPooledDataSource)(new JavaBeanObjectFactory()).getObjectInstance(ref, null, null, null);
/* 52 */       ComboPooledDataSource cpdsCJBOF = (ComboPooledDataSource)(new C3P0JavaBeanObjectFactory()).getObjectInstance(ref, null, null, null);
/* 53 */       System.err.println("cpds: " + cpds);
/* 54 */       System.err.println("cpdsJBOF: " + cpdsJBOF);
/* 55 */       System.err.println("cpdsCJBOF: " + cpdsCJBOF);
/*    */     }
/* 57 */     catch (Exception e) {
/* 58 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/test/JavaBeanRefTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
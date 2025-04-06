/*    */ package com.mchange.v1.db.sql.schemarep;
/*    */ 
/*    */ import com.mchange.v1.identicator.Identicator;
/*    */ import java.util.Arrays;
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
/*    */ public class TypeRepIdenticator
/*    */   implements Identicator
/*    */ {
/* 43 */   private static final TypeRepIdenticator INSTANCE = new TypeRepIdenticator();
/*    */   
/*    */   public static TypeRepIdenticator getInstance() {
/* 46 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean identical(Object paramObject1, Object paramObject2) {
/* 53 */     if (paramObject1 == paramObject2) {
/* 54 */       return true;
/*    */     }
/* 56 */     TypeRep typeRep1 = (TypeRep)paramObject1;
/* 57 */     TypeRep typeRep2 = (TypeRep)paramObject2;
/*    */     
/* 59 */     return (typeRep1.getTypeCode() == typeRep2.getTypeCode() && Arrays.equals(typeRep1.getTypeSize(), typeRep2.getTypeSize()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hash(Object paramObject) {
/* 66 */     TypeRep typeRep = (TypeRep)paramObject;
/* 67 */     int i = typeRep.getTypeCode();
/*    */     
/* 69 */     int[] arrayOfInt = typeRep.getTypeSize();
/* 70 */     if (arrayOfInt != null) {
/*    */       
/* 72 */       int j = arrayOfInt.length;
/* 73 */       for (byte b = 0; b < j; b++)
/* 74 */         i ^= arrayOfInt[b]; 
/* 75 */       i ^= j;
/*    */     } 
/* 77 */     return i;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/schemarep/TypeRepIdenticator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
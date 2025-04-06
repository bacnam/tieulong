/*    */ package com.mchange.v1.db.sql.schemarep;
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
/*    */ public final class TypeRepImpl
/*    */   implements TypeRep
/*    */ {
/*    */   int type_code;
/*    */   int[] typeSize;
/*    */   
/*    */   public TypeRepImpl(int paramInt, int[] paramArrayOfint) {
/* 45 */     this.type_code = paramInt;
/* 46 */     this.typeSize = paramArrayOfint;
/*    */   }
/*    */   
/*    */   public int getTypeCode() {
/* 50 */     return this.type_code;
/*    */   }
/*    */   public int[] getTypeSize() {
/* 53 */     return this.typeSize;
/*    */   }
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 57 */     if (this == paramObject)
/* 58 */       return true; 
/* 59 */     if (paramObject instanceof TypeRep) {
/* 60 */       return TypeRepIdenticator.getInstance().identical(this, paramObject);
/*    */     }
/* 62 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 66 */     return TypeRepIdenticator.getInstance().hash(this);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/schemarep/TypeRepImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
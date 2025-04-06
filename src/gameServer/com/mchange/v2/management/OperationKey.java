/*    */ package com.mchange.v2.management;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ public final class OperationKey
/*    */ {
/*    */   String name;
/*    */   String[] signature;
/*    */   
/*    */   public OperationKey(String paramString, String[] paramArrayOfString) {
/* 47 */     this.name = paramString;
/* 48 */     this.signature = paramArrayOfString;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 53 */     if (paramObject instanceof OperationKey) {
/*    */       
/* 55 */       OperationKey operationKey = (OperationKey)paramObject;
/* 56 */       return (this.name.equals(operationKey.name) && Arrays.equals((Object[])this.signature, (Object[])operationKey.signature));
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 61 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 65 */     return this.name.hashCode() ^ Arrays.hashCode((Object[])this.signature);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/management/OperationKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
/*    */ package com.mchange.v2.codegen.bean;
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
/*    */ class SimplePropertyMask
/*    */   implements Property
/*    */ {
/*    */   Property p;
/*    */   
/*    */   SimplePropertyMask(Property paramProperty) {
/* 45 */     this.p = paramProperty;
/*    */   }
/*    */   public int getVariableModifiers() {
/* 48 */     return 2;
/*    */   }
/*    */   public String getName() {
/* 51 */     return this.p.getName();
/*    */   }
/*    */   public String getSimpleTypeName() {
/* 54 */     return this.p.getSimpleTypeName();
/*    */   }
/*    */   public String getDefensiveCopyExpression() {
/* 57 */     return null;
/*    */   }
/*    */   public String getDefaultValueExpression() {
/* 60 */     return this.p.getDefaultValueExpression();
/*    */   }
/*    */   public int getGetterModifiers() {
/* 63 */     return 1;
/*    */   }
/*    */   public int getSetterModifiers() {
/* 66 */     return 1;
/*    */   }
/*    */   public boolean isReadOnly() {
/* 69 */     return false;
/*    */   }
/*    */   public boolean isBound() {
/* 72 */     return false;
/*    */   }
/*    */   public boolean isConstrained() {
/* 75 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/SimplePropertyMask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
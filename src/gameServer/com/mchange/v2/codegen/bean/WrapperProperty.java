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
/*    */ public abstract class WrapperProperty
/*    */   implements Property
/*    */ {
/*    */   Property p;
/*    */   
/*    */   public WrapperProperty(Property paramProperty) {
/* 45 */     this.p = paramProperty;
/*    */   }
/*    */   protected Property getInner() {
/* 48 */     return this.p;
/*    */   }
/*    */   public int getVariableModifiers() {
/* 51 */     return this.p.getVariableModifiers();
/*    */   }
/*    */   public String getName() {
/* 54 */     return this.p.getName();
/*    */   }
/*    */   public String getSimpleTypeName() {
/* 57 */     return this.p.getSimpleTypeName();
/*    */   }
/*    */   public String getDefensiveCopyExpression() {
/* 60 */     return this.p.getDefensiveCopyExpression();
/*    */   }
/*    */   public String getDefaultValueExpression() {
/* 63 */     return this.p.getDefaultValueExpression();
/*    */   }
/*    */   public int getGetterModifiers() {
/* 66 */     return this.p.getGetterModifiers();
/*    */   }
/*    */   public int getSetterModifiers() {
/* 69 */     return this.p.getSetterModifiers();
/*    */   }
/*    */   public boolean isReadOnly() {
/* 72 */     return this.p.isReadOnly();
/*    */   }
/*    */   public boolean isBound() {
/* 75 */     return this.p.isBound();
/*    */   }
/*    */   public boolean isConstrained() {
/* 78 */     return this.p.isConstrained();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/WrapperProperty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
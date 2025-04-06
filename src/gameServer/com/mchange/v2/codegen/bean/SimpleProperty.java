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
/*    */ public class SimpleProperty
/*    */   implements Property
/*    */ {
/*    */   int variable_modifiers;
/*    */   String name;
/*    */   String simpleTypeName;
/*    */   String defensiveCopyExpression;
/*    */   String defaultValueExpression;
/*    */   int getter_modifiers;
/*    */   int setter_modifiers;
/*    */   boolean is_read_only;
/*    */   boolean is_bound;
/*    */   boolean is_constrained;
/*    */   
/*    */   public int getVariableModifiers() {
/* 53 */     return this.variable_modifiers;
/* 54 */   } public String getName() { return this.name; }
/* 55 */   public String getSimpleTypeName() { return this.simpleTypeName; }
/* 56 */   public String getDefensiveCopyExpression() { return this.defensiveCopyExpression; }
/* 57 */   public String getDefaultValueExpression() { return this.defaultValueExpression; }
/* 58 */   public int getGetterModifiers() { return this.getter_modifiers; }
/* 59 */   public int getSetterModifiers() { return this.setter_modifiers; }
/* 60 */   public boolean isReadOnly() { return this.is_read_only; }
/* 61 */   public boolean isBound() { return this.is_bound; } public boolean isConstrained() {
/* 62 */     return this.is_constrained;
/*    */   }
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
/*    */   public SimpleProperty(int paramInt1, String paramString1, String paramString2, String paramString3, String paramString4, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/* 75 */     this.variable_modifiers = paramInt1;
/* 76 */     this.name = paramString1;
/* 77 */     this.simpleTypeName = paramString2;
/* 78 */     this.defensiveCopyExpression = paramString3;
/* 79 */     this.defaultValueExpression = paramString4;
/* 80 */     this.getter_modifiers = paramInt2;
/* 81 */     this.setter_modifiers = paramInt3;
/* 82 */     this.is_read_only = paramBoolean1;
/* 83 */     this.is_bound = paramBoolean2;
/* 84 */     this.is_constrained = paramBoolean3;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SimpleProperty(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/* 95 */     this(2, paramString1, paramString2, paramString3, paramString4, 1, 1, paramBoolean1, paramBoolean2, paramBoolean3);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/SimpleProperty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
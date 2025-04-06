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
/*    */ public abstract class WrapperClassInfo
/*    */   implements ClassInfo
/*    */ {
/*    */   ClassInfo inner;
/*    */   
/*    */   public WrapperClassInfo(ClassInfo paramClassInfo) {
/* 43 */     this.inner = paramClassInfo;
/*    */   }
/* 45 */   public String getPackageName() { return this.inner.getPackageName(); }
/* 46 */   public int getModifiers() { return this.inner.getModifiers(); }
/* 47 */   public String getClassName() { return this.inner.getClassName(); }
/* 48 */   public String getSuperclassName() { return this.inner.getSuperclassName(); }
/* 49 */   public String[] getInterfaceNames() { return this.inner.getInterfaceNames(); }
/* 50 */   public String[] getGeneralImports() { return this.inner.getGeneralImports(); } public String[] getSpecificImports() {
/* 51 */     return this.inner.getSpecificImports();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/WrapperClassInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
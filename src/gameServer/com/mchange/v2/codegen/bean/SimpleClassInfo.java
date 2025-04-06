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
/*    */ public class SimpleClassInfo
/*    */   implements ClassInfo
/*    */ {
/*    */   String packageName;
/*    */   int modifiers;
/*    */   String className;
/*    */   String superclassName;
/*    */   String[] interfaceNames;
/*    */   String[] generalImports;
/*    */   String[] specificImports;
/*    */   
/*    */   public String getPackageName() {
/* 50 */     return this.packageName;
/* 51 */   } public int getModifiers() { return this.modifiers; }
/* 52 */   public String getClassName() { return this.className; }
/* 53 */   public String getSuperclassName() { return this.superclassName; }
/* 54 */   public String[] getInterfaceNames() { return this.interfaceNames; }
/* 55 */   public String[] getGeneralImports() { return this.generalImports; } public String[] getSpecificImports() {
/* 56 */     return this.specificImports;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SimpleClassInfo(String paramString1, int paramInt, String paramString2, String paramString3, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3) {
/* 66 */     this.packageName = paramString1;
/* 67 */     this.modifiers = paramInt;
/* 68 */     this.className = paramString2;
/* 69 */     this.superclassName = paramString3;
/* 70 */     this.interfaceNames = paramArrayOfString1;
/* 71 */     this.generalImports = paramArrayOfString2;
/* 72 */     this.specificImports = paramArrayOfString3;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/SimpleClassInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
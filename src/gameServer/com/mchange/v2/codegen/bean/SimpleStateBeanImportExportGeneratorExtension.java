/*     */ package com.mchange.v2.codegen.bean;
/*     */ 
/*     */ import com.mchange.v2.codegen.IndentedWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleStateBeanImportExportGeneratorExtension
/*     */   implements GeneratorExtension
/*     */ {
/*  46 */   int ctor_modifiers = 1;
/*     */   
/*     */   public Collection extraGeneralImports() {
/*  49 */     return Collections.EMPTY_SET;
/*     */   }
/*     */   public Collection extraSpecificImports() {
/*  52 */     return Collections.EMPTY_SET;
/*     */   }
/*     */   public Collection extraInterfaceNames() {
/*  55 */     return Collections.EMPTY_SET;
/*     */   }
/*     */   
/*     */   static class SimplePropertyMask implements Property {
/*     */     Property p;
/*     */     
/*     */     SimplePropertyMask(Property param1Property) {
/*  62 */       this.p = param1Property;
/*     */     }
/*     */     public int getVariableModifiers() {
/*  65 */       return 2;
/*     */     }
/*     */     public String getName() {
/*  68 */       return this.p.getName();
/*     */     }
/*     */     public String getSimpleTypeName() {
/*  71 */       return this.p.getSimpleTypeName();
/*     */     }
/*     */     public String getDefensiveCopyExpression() {
/*  74 */       return null;
/*     */     }
/*     */     public String getDefaultValueExpression() {
/*  77 */       return null;
/*     */     }
/*     */     public int getGetterModifiers() {
/*  80 */       return 1;
/*     */     }
/*     */     public int getSetterModifiers() {
/*  83 */       return 1;
/*     */     }
/*     */     public boolean isReadOnly() {
/*  86 */       return false;
/*     */     }
/*     */     public boolean isBound() {
/*  89 */       return false;
/*     */     }
/*     */     public boolean isConstrained() {
/*  92 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
/*  98 */     int i = paramArrayOfProperty.length;
/*  99 */     Property[] arrayOfProperty = new Property[i]; byte b;
/* 100 */     for (b = 0; b < i; b++) {
/* 101 */       arrayOfProperty[b] = new SimplePropertyMask(paramArrayOfProperty[b]);
/*     */     }
/* 103 */     paramIndentedWriter.println("protected static class SimpleStateBean implements ExportedState");
/* 104 */     paramIndentedWriter.println("{");
/* 105 */     paramIndentedWriter.upIndent();
/*     */     
/* 107 */     for (b = 0; b < i; b++) {
/*     */       
/* 109 */       arrayOfProperty[b] = new SimplePropertyMask(paramArrayOfProperty[b]);
/* 110 */       BeangenUtils.writePropertyMember(arrayOfProperty[b], paramIndentedWriter);
/* 111 */       paramIndentedWriter.println();
/* 112 */       BeangenUtils.writePropertyGetter(arrayOfProperty[b], paramIndentedWriter);
/* 113 */       paramIndentedWriter.println();
/* 114 */       BeangenUtils.writePropertySetter(arrayOfProperty[b], paramIndentedWriter);
/*     */     } 
/*     */     
/* 117 */     paramIndentedWriter.downIndent();
/* 118 */     paramIndentedWriter.println("}");
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/SimpleStateBeanImportExportGeneratorExtension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
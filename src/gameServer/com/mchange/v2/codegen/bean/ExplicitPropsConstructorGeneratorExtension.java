/*     */ package com.mchange.v2.codegen.bean;
/*     */ 
/*     */ import com.mchange.v2.codegen.CodegenUtils;
/*     */ import com.mchange.v2.codegen.IndentedWriter;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExplicitPropsConstructorGeneratorExtension
/*     */   implements GeneratorExtension
/*     */ {
/*  55 */   static final MLogger logger = MLog.getLogger(ExplicitPropsConstructorGeneratorExtension.class);
/*     */ 
/*     */   
/*     */   String[] propNames;
/*     */ 
/*     */   
/*     */   boolean skips_silently = false;
/*     */ 
/*     */   
/*     */   int ctor_modifiers;
/*     */ 
/*     */   
/*     */   public String[] getPropNames() {
/*  68 */     return (String[])this.propNames.clone();
/*     */   }
/*     */   public void setPropNames(String[] paramArrayOfString) {
/*  71 */     this.propNames = (String[])paramArrayOfString.clone();
/*     */   }
/*     */   public boolean isSkipsSilently() {
/*  74 */     return this.skips_silently;
/*     */   }
/*     */   public void setsSkipsSilently(boolean paramBoolean) {
/*  77 */     this.skips_silently = paramBoolean;
/*     */   }
/*  79 */   public ExplicitPropsConstructorGeneratorExtension() { this.ctor_modifiers = 1; } public ExplicitPropsConstructorGeneratorExtension(String[] paramArrayOfString) { this.ctor_modifiers = 1;
/*     */     this.propNames = paramArrayOfString; }
/*     */    public Collection extraGeneralImports() {
/*  82 */     return Collections.EMPTY_SET;
/*     */   }
/*     */   public Collection extraSpecificImports() {
/*  85 */     return Collections.EMPTY_SET;
/*     */   }
/*     */   public Collection extraInterfaceNames() {
/*  88 */     return Collections.EMPTY_SET;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
/*  93 */     HashMap<Object, Object> hashMap = new HashMap<Object, Object>(); int i;
/*  94 */     for (byte b = 0; b < i; b++) {
/*  95 */       hashMap.put(paramArrayOfProperty[b].getName(), paramArrayOfProperty[b]);
/*     */     }
/*  97 */     ArrayList<Property> arrayList = new ArrayList(this.propNames.length); int j;
/*  98 */     for (i = 0, j = this.propNames.length; i < j; i++) {
/*     */       
/* 100 */       Property property = (Property)hashMap.get(this.propNames[i]);
/* 101 */       if (property == null) {
/* 102 */         logger.warning("Could not include property '" + this.propNames[i] + "' in explicit-props-constructor generated for bean class '" + paramClassInfo.getClassName() + "' because the property is not defined for the bean. Skipping.");
/*     */       } else {
/*     */         
/* 105 */         arrayList.add(property);
/*     */       } 
/*     */     } 
/* 108 */     if (arrayList.size() > 0) {
/*     */       
/* 110 */       Property[] arrayOfProperty = arrayList.<Property>toArray(new Property[arrayList.size()]);
/*     */       
/* 112 */       paramIndentedWriter.print(CodegenUtils.getModifierString(this.ctor_modifiers));
/* 113 */       paramIndentedWriter.print(paramClassInfo.getClassName() + "( ");
/* 114 */       BeangenUtils.writeArgList(arrayOfProperty, true, paramIndentedWriter);
/* 115 */       paramIndentedWriter.println(" )");
/* 116 */       paramIndentedWriter.println("{");
/* 117 */       paramIndentedWriter.upIndent();
/*     */       int k;
/* 119 */       for (j = 0, k = arrayOfProperty.length; j < k; j++) {
/*     */         
/* 121 */         paramIndentedWriter.print("this." + arrayOfProperty[j].getName() + " = ");
/* 122 */         String str = arrayOfProperty[j].getDefensiveCopyExpression();
/* 123 */         if (str == null)
/* 124 */           str = arrayOfProperty[j].getName(); 
/* 125 */         paramIndentedWriter.println(str + ';');
/*     */       } 
/*     */       
/* 128 */       paramIndentedWriter.downIndent();
/* 129 */       paramIndentedWriter.println("}");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/ExplicitPropsConstructorGeneratorExtension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
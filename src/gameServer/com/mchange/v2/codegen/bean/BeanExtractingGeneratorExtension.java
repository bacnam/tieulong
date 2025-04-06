/*     */ package com.mchange.v2.codegen.bean;
/*     */ 
/*     */ import com.mchange.v2.codegen.CodegenUtils;
/*     */ import com.mchange.v2.codegen.IndentedWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
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
/*     */ public class BeanExtractingGeneratorExtension
/*     */   implements GeneratorExtension
/*     */ {
/*  46 */   int ctor_modifiers = 1;
/*  47 */   int method_modifiers = 2;
/*     */   
/*     */   public void setConstructorModifiers(int paramInt) {
/*  50 */     this.ctor_modifiers = paramInt;
/*     */   }
/*     */   public int getConstructorModifiers() {
/*  53 */     return this.ctor_modifiers;
/*     */   }
/*     */   public void setExtractMethodModifiers(int paramInt) {
/*  56 */     this.method_modifiers = paramInt;
/*     */   }
/*     */   public int getExtractMethodModifiers() {
/*  59 */     return this.method_modifiers;
/*     */   }
/*     */   public Collection extraGeneralImports() {
/*  62 */     return Collections.EMPTY_SET;
/*     */   }
/*     */   
/*     */   public Collection extraSpecificImports() {
/*  66 */     HashSet<String> hashSet = new HashSet();
/*  67 */     hashSet.add("java.beans.BeanInfo");
/*  68 */     hashSet.add("java.beans.PropertyDescriptor");
/*  69 */     hashSet.add("java.beans.Introspector");
/*  70 */     hashSet.add("java.beans.IntrospectionException");
/*  71 */     hashSet.add("java.lang.reflect.InvocationTargetException");
/*  72 */     return hashSet;
/*     */   }
/*     */   
/*     */   public Collection extraInterfaceNames() {
/*  76 */     return Collections.EMPTY_SET;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
/*  81 */     paramIndentedWriter.println("private static Class[] NOARGS = new Class[0];");
/*  82 */     paramIndentedWriter.println();
/*  83 */     paramIndentedWriter.print(CodegenUtils.getModifierString(this.method_modifiers));
/*  84 */     paramIndentedWriter.print(" void extractPropertiesFromBean( Object bean ) throws InvocationTargetException, IllegalAccessException, IntrospectionException");
/*  85 */     paramIndentedWriter.println("{");
/*  86 */     paramIndentedWriter.upIndent();
/*     */     
/*  88 */     paramIndentedWriter.println("BeanInfo bi = Introspector.getBeanInfo( bean.getClass() );");
/*  89 */     paramIndentedWriter.println("PropertyDescriptor[] pds = bi.getPropertyDescriptors();");
/*  90 */     paramIndentedWriter.println("for (int i = 0, len = pds.length; i < len; ++i)");
/*  91 */     paramIndentedWriter.println("{");
/*  92 */     paramIndentedWriter.upIndent(); byte b;
/*     */     int i;
/*  94 */     for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {
/*     */       
/*  96 */       paramIndentedWriter.println("if (\"" + paramArrayOfProperty[b].getName() + "\".equals( pds[i].getName() ) )");
/*  97 */       paramIndentedWriter.upIndent();
/*  98 */       paramIndentedWriter.println("this." + paramArrayOfProperty[b].getName() + " = " + extractorExpr(paramArrayOfProperty[b], paramArrayOfClass[b]) + ';');
/*  99 */       paramIndentedWriter.downIndent();
/*     */     } 
/* 101 */     paramIndentedWriter.println("}");
/*     */ 
/*     */     
/* 104 */     paramIndentedWriter.downIndent();
/* 105 */     paramIndentedWriter.println("}");
/* 106 */     paramIndentedWriter.println();
/* 107 */     paramIndentedWriter.print(CodegenUtils.getModifierString(this.ctor_modifiers));
/* 108 */     paramIndentedWriter.println(' ' + paramClassInfo.getClassName() + "( Object bean ) throws InvocationTargetException, IllegalAccessException, IntrospectionException");
/* 109 */     paramIndentedWriter.println("{");
/* 110 */     paramIndentedWriter.upIndent();
/* 111 */     paramIndentedWriter.println("extractPropertiesFromBean( bean );");
/* 112 */     paramIndentedWriter.downIndent();
/* 113 */     paramIndentedWriter.println("}");
/*     */   }
/*     */ 
/*     */   
/*     */   private String extractorExpr(Property paramProperty, Class<char> paramClass) {
/* 118 */     if (paramClass.isPrimitive()) {
/*     */       
/* 120 */       String str1 = BeangenUtils.capitalize(paramProperty.getSimpleTypeName());
/* 121 */       String str2 = paramProperty.getSimpleTypeName() + "Value()";
/*     */       
/* 123 */       if (paramClass == char.class) {
/* 124 */         str1 = "Character";
/* 125 */       } else if (paramClass == int.class) {
/* 126 */         str1 = "Integer";
/*     */       } 
/* 128 */       return "((" + str1 + ") pds[i].getReadMethod().invoke( bean, NOARGS ))." + str2;
/*     */     } 
/*     */     
/* 131 */     return "(" + paramProperty.getSimpleTypeName() + ") pds[i].getReadMethod().invoke( bean, NOARGS )";
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/BeanExtractingGeneratorExtension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
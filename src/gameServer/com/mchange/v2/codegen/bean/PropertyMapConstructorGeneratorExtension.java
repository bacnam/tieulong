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
/*     */ 
/*     */ public class PropertyMapConstructorGeneratorExtension
/*     */   implements GeneratorExtension
/*     */ {
/*  47 */   int ctor_modifiers = 1;
/*     */   
/*     */   public Collection extraGeneralImports() {
/*  50 */     return Collections.EMPTY_SET;
/*     */   }
/*     */   
/*     */   public Collection extraSpecificImports() {
/*  54 */     HashSet<String> hashSet = new HashSet();
/*  55 */     hashSet.add("java.util.Map");
/*  56 */     return hashSet;
/*     */   }
/*     */   
/*     */   public Collection extraInterfaceNames() {
/*  60 */     return Collections.EMPTY_SET;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
/*  65 */     paramIndentedWriter.print(CodegenUtils.getModifierString(this.ctor_modifiers));
/*  66 */     paramIndentedWriter.print(' ' + paramClassInfo.getClassName() + "( Map map )");
/*  67 */     paramIndentedWriter.println("{");
/*  68 */     paramIndentedWriter.upIndent();
/*     */     
/*  70 */     paramIndentedWriter.println("Object raw;"); byte b; int i;
/*  71 */     for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {
/*     */       
/*  73 */       Property property = paramArrayOfProperty[b];
/*  74 */       String str = property.getName();
/*  75 */       Class<boolean> clazz = paramArrayOfClass[b];
/*  76 */       paramIndentedWriter.println("raw = map.get( \"" + str + "\" );");
/*  77 */       paramIndentedWriter.println("if (raw != null)");
/*  78 */       paramIndentedWriter.println("{");
/*  79 */       paramIndentedWriter.upIndent();
/*     */       
/*  81 */       paramIndentedWriter.print("this." + str + " = ");
/*  82 */       if (clazz == boolean.class) {
/*  83 */         paramIndentedWriter.println("((Boolean) raw ).booleanValue();");
/*  84 */       } else if (clazz == byte.class) {
/*  85 */         paramIndentedWriter.println("((Byte) raw ).byteValue();");
/*  86 */       } else if (clazz == char.class) {
/*  87 */         paramIndentedWriter.println("((Character) raw ).charValue();");
/*  88 */       } else if (clazz == short.class) {
/*  89 */         paramIndentedWriter.println("((Short) raw ).shortValue();");
/*  90 */       } else if (clazz == int.class) {
/*  91 */         paramIndentedWriter.println("((Integer) raw ).intValue();");
/*  92 */       } else if (clazz == long.class) {
/*  93 */         paramIndentedWriter.println("((Long) raw ).longValue();");
/*  94 */       } else if (clazz == float.class) {
/*  95 */         paramIndentedWriter.println("((Float) raw ).floatValue();");
/*  96 */       } else if (clazz == double.class) {
/*  97 */         paramIndentedWriter.println("((Double) raw ).doubleValue();");
/*  98 */       }  paramIndentedWriter.println("raw = null;");
/*     */       
/* 100 */       paramIndentedWriter.downIndent();
/* 101 */       paramIndentedWriter.println("}");
/*     */     } 
/*     */     
/* 104 */     paramIndentedWriter.downIndent();
/* 105 */     paramIndentedWriter.println("}");
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/PropertyMapConstructorGeneratorExtension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
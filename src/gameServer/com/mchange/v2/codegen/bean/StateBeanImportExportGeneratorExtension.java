/*     */ package com.mchange.v2.codegen.bean;
/*     */ 
/*     */ import com.mchange.v2.codegen.CodegenUtils;
/*     */ import com.mchange.v2.codegen.IndentedWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
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
/*     */ public class StateBeanImportExportGeneratorExtension
/*     */   implements GeneratorExtension
/*     */ {
/*  46 */   int ctor_modifiers = 1;
/*     */   
/*     */   public Collection extraGeneralImports() {
/*  49 */     return Arrays.asList(new String[] { "com.mchange.v2.bean" });
/*     */   }
/*     */   public Collection extraSpecificImports() {
/*  52 */     return Collections.EMPTY_SET;
/*     */   }
/*     */   public Collection extraInterfaceNames() {
/*  55 */     return Arrays.asList(new String[] { "StateBeanExporter" });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
/*  61 */     String str = paramClassInfo.getClassName();
/*     */     
/*  63 */     int i = paramArrayOfProperty.length;
/*  64 */     Property[] arrayOfProperty = new Property[i]; byte b;
/*  65 */     for (b = 0; b < i; b++) {
/*  66 */       arrayOfProperty[b] = new SimplePropertyMask(paramArrayOfProperty[b]);
/*     */     }
/*  68 */     paramIndentedWriter.println("protected class MyStateBean implements StateBean");
/*  69 */     paramIndentedWriter.println("{");
/*  70 */     paramIndentedWriter.upIndent();
/*     */     
/*  72 */     for (b = 0; b < i; b++) {
/*     */       
/*  74 */       arrayOfProperty[b] = new SimplePropertyMask(paramArrayOfProperty[b]);
/*  75 */       BeangenUtils.writePropertyMember(arrayOfProperty[b], paramIndentedWriter);
/*  76 */       paramIndentedWriter.println();
/*  77 */       BeangenUtils.writePropertyGetter(arrayOfProperty[b], paramIndentedWriter);
/*  78 */       paramIndentedWriter.println();
/*  79 */       BeangenUtils.writePropertySetter(arrayOfProperty[b], paramIndentedWriter);
/*     */     } 
/*  81 */     paramIndentedWriter.println();
/*  82 */     paramIndentedWriter.downIndent();
/*  83 */     paramIndentedWriter.println("}");
/*  84 */     paramIndentedWriter.println();
/*  85 */     paramIndentedWriter.println("public StateBean exportStateBean()");
/*  86 */     paramIndentedWriter.println("{");
/*  87 */     paramIndentedWriter.upIndent();
/*  88 */     paramIndentedWriter.println("MyStateBean out = createEmptyStateBean();");
/*  89 */     for (b = 0; b < i; b++) {
/*     */       
/*  91 */       String str1 = BeangenUtils.capitalize(paramArrayOfProperty[b].getName());
/*  92 */       paramIndentedWriter.println("out.set" + str1 + "( this." + ((paramArrayOfClass[b] == boolean.class) ? "is" : "get") + str1 + "() );");
/*     */     } 
/*  94 */     paramIndentedWriter.println("return out;");
/*  95 */     paramIndentedWriter.downIndent();
/*  96 */     paramIndentedWriter.println("}");
/*  97 */     paramIndentedWriter.println();
/*     */ 
/*     */     
/* 100 */     paramIndentedWriter.println("public void importStateBean( StateBean bean )");
/* 101 */     paramIndentedWriter.println("{");
/* 102 */     paramIndentedWriter.upIndent();
/* 103 */     paramIndentedWriter.println("MyStateBean msb = (MyStateBean) bean;");
/* 104 */     for (b = 0; b < i; b++) {
/*     */       
/* 106 */       String str1 = BeangenUtils.capitalize(paramArrayOfProperty[b].getName());
/* 107 */       paramIndentedWriter.println("this.set" + str1 + "( msb." + ((paramArrayOfClass[b] == boolean.class) ? "is" : "get") + str1 + "() );");
/*     */     } 
/* 109 */     paramIndentedWriter.downIndent();
/* 110 */     paramIndentedWriter.println("}");
/* 111 */     paramIndentedWriter.println();
/*     */     
/* 113 */     paramIndentedWriter.print(CodegenUtils.getModifierString(this.ctor_modifiers));
/* 114 */     paramIndentedWriter.println(" " + str + "( StateBean bean )");
/* 115 */     paramIndentedWriter.println("{ importStateBean( bean ); }");
/*     */     
/* 117 */     paramIndentedWriter.println("protected MyStateBean createEmptyStateBean() throws StateBeanException");
/* 118 */     paramIndentedWriter.println("{ return new MyStateBean(); }");
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/StateBeanImportExportGeneratorExtension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
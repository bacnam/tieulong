/*     */ package com.mchange.v2.codegen.bean;
/*     */ 
/*     */ import com.mchange.v2.codegen.IndentedWriter;
/*     */ import com.mchange.v2.naming.JavaBeanObjectFactory;
/*     */ import com.mchange.v2.naming.JavaBeanReferenceMaker;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ public class PropertyReferenceableExtension
/*     */   implements GeneratorExtension
/*     */ {
/*     */   boolean explicit_reference_properties = false;
/*  49 */   String factoryClassName = JavaBeanObjectFactory.class.getName();
/*     */   
/*  51 */   String javaBeanReferenceMakerClassName = JavaBeanReferenceMaker.class.getName();
/*     */   
/*     */   public void setUseExplicitReferenceProperties(boolean paramBoolean) {
/*  54 */     this.explicit_reference_properties = paramBoolean;
/*     */   }
/*     */   public boolean getUseExplicitReferenceProperties() {
/*  57 */     return this.explicit_reference_properties;
/*     */   }
/*     */   public void setFactoryClassName(String paramString) {
/*  60 */     this.factoryClassName = paramString;
/*     */   }
/*     */   public String getFactoryClassName() {
/*  63 */     return this.factoryClassName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection extraGeneralImports() {
/*  73 */     return new HashSet();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection extraSpecificImports() {
/*  79 */     HashSet<String> hashSet = new HashSet();
/*  80 */     hashSet.add("javax.naming.Reference");
/*  81 */     hashSet.add("javax.naming.Referenceable");
/*  82 */     hashSet.add("javax.naming.NamingException");
/*  83 */     hashSet.add("com.mchange.v2.naming.JavaBeanObjectFactory");
/*  84 */     hashSet.add("com.mchange.v2.naming.JavaBeanReferenceMaker");
/*  85 */     hashSet.add("com.mchange.v2.naming.ReferenceMaker");
/*  86 */     return hashSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection extraInterfaceNames() {
/*  91 */     HashSet<String> hashSet = new HashSet();
/*  92 */     hashSet.add("Referenceable");
/*  93 */     return hashSet;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
/*  99 */     paramIndentedWriter.println("final static JavaBeanReferenceMaker referenceMaker = new " + this.javaBeanReferenceMakerClassName + "();");
/* 100 */     paramIndentedWriter.println();
/* 101 */     paramIndentedWriter.println("static");
/* 102 */     paramIndentedWriter.println("{");
/* 103 */     paramIndentedWriter.upIndent();
/*     */     
/* 105 */     paramIndentedWriter.println("referenceMaker.setFactoryClassName( \"" + this.factoryClassName + "\" );");
/* 106 */     if (this.explicit_reference_properties) {
/*     */       byte b; int i;
/* 108 */       for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {
/* 109 */         paramIndentedWriter.println("referenceMaker.addReferenceProperty(\"" + paramArrayOfProperty[b].getName() + "\");");
/*     */       }
/*     */     } 
/* 112 */     paramIndentedWriter.downIndent();
/* 113 */     paramIndentedWriter.println("}");
/* 114 */     paramIndentedWriter.println();
/* 115 */     paramIndentedWriter.println("public Reference getReference() throws NamingException");
/* 116 */     paramIndentedWriter.println("{");
/* 117 */     paramIndentedWriter.upIndent();
/*     */     
/* 119 */     paramIndentedWriter.println("return referenceMaker.createReference( this );");
/*     */     
/* 121 */     paramIndentedWriter.downIndent();
/* 122 */     paramIndentedWriter.println("}");
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/PropertyReferenceableExtension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
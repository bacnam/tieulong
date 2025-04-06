/*     */ package com.mchange.v2.codegen.bean;
/*     */ 
/*     */ import com.mchange.v2.codegen.IndentedWriter;
/*     */ import com.mchange.v2.ser.IndirectPolicy;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IndirectingSerializableExtension
/*     */   extends SerializableExtension
/*     */ {
/*     */   protected String findIndirectorExpr;
/*     */   protected String indirectorClassName;
/*     */   
/*     */   public IndirectingSerializableExtension(String paramString) {
/*  59 */     this.indirectorClassName = paramString;
/*  60 */     this.findIndirectorExpr = "new " + paramString + "()";
/*     */   }
/*     */ 
/*     */   
/*     */   protected IndirectingSerializableExtension() {}
/*     */ 
/*     */   
/*     */   public Collection extraSpecificImports() {
/*  68 */     Collection<String> collection = super.extraSpecificImports();
/*  69 */     collection.add(this.indirectorClassName);
/*  70 */     collection.add("com.mchange.v2.ser.IndirectlySerialized");
/*  71 */     collection.add("com.mchange.v2.ser.Indirector");
/*  72 */     collection.add("com.mchange.v2.ser.SerializableUtils");
/*  73 */     collection.add("java.io.NotSerializableException");
/*  74 */     return collection;
/*     */   }
/*     */ 
/*     */   
/*     */   protected IndirectPolicy indirectingPolicy(Property paramProperty, Class<?> paramClass) {
/*  79 */     if (Serializable.class.isAssignableFrom(paramClass)) {
/*  80 */       return IndirectPolicy.DEFINITELY_DIRECT;
/*     */     }
/*  82 */     return IndirectPolicy.INDIRECT_ON_EXCEPTION;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInitializeIndirector(Property paramProperty, Class paramClass, IndentedWriter paramIndentedWriter) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeExtraDeclarations(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
/* 100 */     super.generate(paramClassInfo, paramClass, paramArrayOfProperty, paramArrayOfClass, paramIndentedWriter);
/* 101 */     writeExtraDeclarations(paramClassInfo, paramClass, paramArrayOfProperty, paramArrayOfClass, paramIndentedWriter);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeStoreObject(Property paramProperty, Class paramClass, IndentedWriter paramIndentedWriter) throws IOException {
/* 106 */     IndirectPolicy indirectPolicy = indirectingPolicy(paramProperty, paramClass);
/* 107 */     if (indirectPolicy == IndirectPolicy.DEFINITELY_INDIRECT) {
/* 108 */       writeIndirectStoreObject(paramProperty, paramClass, paramIndentedWriter);
/* 109 */     } else if (indirectPolicy == IndirectPolicy.INDIRECT_ON_EXCEPTION) {
/*     */       
/* 111 */       paramIndentedWriter.println("try");
/* 112 */       paramIndentedWriter.println("{");
/* 113 */       paramIndentedWriter.upIndent();
/* 114 */       paramIndentedWriter.println("//test serialize");
/* 115 */       paramIndentedWriter.println("SerializableUtils.toByteArray(" + paramProperty.getName() + ");");
/* 116 */       super.writeStoreObject(paramProperty, paramClass, paramIndentedWriter);
/* 117 */       paramIndentedWriter.downIndent();
/* 118 */       paramIndentedWriter.println("}");
/* 119 */       paramIndentedWriter.println("catch (NotSerializableException nse)");
/* 120 */       paramIndentedWriter.println("{");
/* 121 */       paramIndentedWriter.upIndent();
/* 122 */       writeIndirectStoreObject(paramProperty, paramClass, paramIndentedWriter);
/* 123 */       paramIndentedWriter.downIndent();
/* 124 */       paramIndentedWriter.println("}");
/*     */     }
/* 126 */     else if (indirectPolicy == IndirectPolicy.DEFINITELY_DIRECT) {
/* 127 */       super.writeStoreObject(paramProperty, paramClass, paramIndentedWriter);
/*     */     } else {
/* 129 */       throw new InternalError("indirectingPolicy() overridden to return unknown policy: " + indirectPolicy);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void writeIndirectStoreObject(Property paramProperty, Class paramClass, IndentedWriter paramIndentedWriter) throws IOException {
/* 134 */     paramIndentedWriter.println("try");
/* 135 */     paramIndentedWriter.println("{");
/* 136 */     paramIndentedWriter.upIndent();
/*     */     
/* 138 */     paramIndentedWriter.println("Indirector indirector = " + this.findIndirectorExpr + ';');
/* 139 */     writeInitializeIndirector(paramProperty, paramClass, paramIndentedWriter);
/* 140 */     paramIndentedWriter.println("oos.writeObject( indirector.indirectForm( " + paramProperty.getName() + " ) );");
/*     */     
/* 142 */     paramIndentedWriter.downIndent();
/* 143 */     paramIndentedWriter.println("}");
/* 144 */     paramIndentedWriter.println("catch (IOException indirectionIOException)");
/* 145 */     paramIndentedWriter.println("{ throw indirectionIOException; }");
/* 146 */     paramIndentedWriter.println("catch (Exception indirectionOtherException)");
/* 147 */     paramIndentedWriter.println("{ throw new IOException(\"Problem indirectly serializing " + paramProperty.getName() + ": \" + indirectionOtherException.toString() ); }");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeUnstoreObject(Property paramProperty, Class paramClass, IndentedWriter paramIndentedWriter) throws IOException {
/* 152 */     IndirectPolicy indirectPolicy = indirectingPolicy(paramProperty, paramClass);
/* 153 */     if (indirectPolicy == IndirectPolicy.DEFINITELY_INDIRECT || indirectPolicy == IndirectPolicy.INDIRECT_ON_EXCEPTION) {
/*     */       
/* 155 */       paramIndentedWriter.println("// we create an artificial scope so that we can use the name o for all indirectly serialized objects.");
/* 156 */       paramIndentedWriter.println("{");
/* 157 */       paramIndentedWriter.upIndent();
/* 158 */       paramIndentedWriter.println("Object o = ois.readObject();");
/* 159 */       paramIndentedWriter.println("if (o instanceof IndirectlySerialized) o = ((IndirectlySerialized) o).getObject();");
/* 160 */       paramIndentedWriter.println("this." + paramProperty.getName() + " = (" + paramProperty.getSimpleTypeName() + ") o;");
/* 161 */       paramIndentedWriter.downIndent();
/* 162 */       paramIndentedWriter.println("}");
/*     */     }
/* 164 */     else if (indirectPolicy == IndirectPolicy.DEFINITELY_DIRECT) {
/* 165 */       super.writeUnstoreObject(paramProperty, paramClass, paramIndentedWriter);
/*     */     } else {
/* 167 */       throw new InternalError("indirectingPolicy() overridden to return unknown policy: " + indirectPolicy);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/IndirectingSerializableExtension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
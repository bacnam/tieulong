/*    */ package com.mchange.v2.codegen.bean;
/*    */ 
/*    */ import com.mchange.v2.codegen.IndentedWriter;
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
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
/*    */ public class PropsToStringGeneratorExtension
/*    */   implements GeneratorExtension
/*    */ {
/* 45 */   private Collection excludePropNames = null;
/*    */   
/*    */   public void setExcludePropertyNames(Collection paramCollection) {
/* 48 */     this.excludePropNames = paramCollection;
/*    */   }
/*    */   public Collection getExcludePropertyNames() {
/* 51 */     return this.excludePropNames;
/*    */   }
/*    */   public Collection extraGeneralImports() {
/* 54 */     return Collections.EMPTY_SET;
/*    */   }
/*    */   public Collection extraSpecificImports() {
/* 57 */     return Collections.EMPTY_SET;
/*    */   }
/*    */   public Collection extraInterfaceNames() {
/* 60 */     return Collections.EMPTY_SET;
/*    */   }
/*    */ 
/*    */   
/*    */   public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
/* 65 */     paramIndentedWriter.println("public String toString()");
/* 66 */     paramIndentedWriter.println("{");
/* 67 */     paramIndentedWriter.upIndent();
/*    */     
/* 69 */     paramIndentedWriter.println("StringBuffer sb = new StringBuffer();");
/* 70 */     paramIndentedWriter.println("sb.append( super.toString() );");
/* 71 */     paramIndentedWriter.println("sb.append(\" [ \");"); byte b;
/*    */     int i;
/* 73 */     for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {
/*    */       
/* 75 */       Property property = paramArrayOfProperty[b];
/*    */       
/* 77 */       if (this.excludePropNames == null || !this.excludePropNames.contains(property.getName())) {
/*    */ 
/*    */         
/* 80 */         paramIndentedWriter.println("sb.append( \"" + property.getName() + " -> \"" + " + " + property.getName() + " );");
/* 81 */         if (b != i - 1)
/* 82 */           paramIndentedWriter.println("sb.append( \", \");"); 
/*    */       } 
/*    */     } 
/* 85 */     paramIndentedWriter.println();
/* 86 */     paramIndentedWriter.println("String extraToStringInfo = this.extraToStringInfo();");
/* 87 */     paramIndentedWriter.println("if (extraToStringInfo != null)");
/* 88 */     paramIndentedWriter.upIndent();
/* 89 */     paramIndentedWriter.println("sb.append( extraToStringInfo );");
/* 90 */     paramIndentedWriter.downIndent();
/*    */ 
/*    */     
/* 93 */     paramIndentedWriter.println("sb.append(\" ]\");");
/* 94 */     paramIndentedWriter.println("return sb.toString();");
/* 95 */     paramIndentedWriter.downIndent();
/* 96 */     paramIndentedWriter.println("}");
/* 97 */     paramIndentedWriter.println();
/* 98 */     paramIndentedWriter.println("protected String extraToStringInfo()");
/* 99 */     paramIndentedWriter.println("{ return null; }");
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/PropsToStringGeneratorExtension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
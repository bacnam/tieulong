/*     */ package com.mchange.v2.codegen.bean;
/*     */ 
/*     */ import com.mchange.v2.codegen.IndentedWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
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
/*     */ public class CloneableExtension
/*     */   implements GeneratorExtension
/*     */ {
/*     */   boolean export_public;
/*     */   boolean exception_swallowing;
/*  47 */   String mLoggerName = null;
/*     */   
/*     */   public boolean isExportPublic() {
/*  50 */     return this.export_public;
/*     */   }
/*     */   public void setExportPublic(boolean paramBoolean) {
/*  53 */     this.export_public = paramBoolean;
/*     */   }
/*     */   public boolean isExceptionSwallowing() {
/*  56 */     return this.exception_swallowing;
/*     */   }
/*     */   public void setExceptionSwallowing(boolean paramBoolean) {
/*  59 */     this.exception_swallowing = paramBoolean;
/*     */   }
/*     */   public String getMLoggerName() {
/*  62 */     return this.mLoggerName;
/*     */   }
/*     */   public void setMLoggerName(String paramString) {
/*  65 */     this.mLoggerName = paramString;
/*     */   }
/*     */   
/*     */   public CloneableExtension(boolean paramBoolean1, boolean paramBoolean2) {
/*  69 */     this.export_public = paramBoolean1;
/*  70 */     this.exception_swallowing = paramBoolean2;
/*     */   }
/*     */   
/*     */   public CloneableExtension() {
/*  74 */     this(true, false);
/*     */   }
/*     */   public Collection extraGeneralImports() {
/*  77 */     return (this.mLoggerName == null) ? Collections.EMPTY_SET : Arrays.<String>asList(new String[] { "com.mchange.v2.log" });
/*     */   }
/*     */   public Collection extraSpecificImports() {
/*  80 */     return Collections.EMPTY_SET;
/*     */   }
/*     */   
/*     */   public Collection extraInterfaceNames() {
/*  84 */     HashSet<String> hashSet = new HashSet();
/*  85 */     hashSet.add("Cloneable");
/*  86 */     return hashSet;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
/*  92 */     if (this.export_public) {
/*     */       
/*  94 */       paramIndentedWriter.print("public Object clone()");
/*  95 */       if (!this.exception_swallowing) {
/*  96 */         paramIndentedWriter.println(" throws CloneNotSupportedException");
/*     */       } else {
/*  98 */         paramIndentedWriter.println();
/*  99 */       }  paramIndentedWriter.println("{");
/* 100 */       paramIndentedWriter.upIndent();
/* 101 */       if (this.exception_swallowing) {
/*     */         
/* 103 */         paramIndentedWriter.println("try");
/* 104 */         paramIndentedWriter.println("{");
/* 105 */         paramIndentedWriter.upIndent();
/*     */       } 
/* 107 */       paramIndentedWriter.println("return super.clone();");
/* 108 */       if (this.exception_swallowing) {
/*     */         
/* 110 */         paramIndentedWriter.downIndent();
/* 111 */         paramIndentedWriter.println("}");
/* 112 */         paramIndentedWriter.println("catch (CloneNotSupportedException e)");
/* 113 */         paramIndentedWriter.println("{");
/* 114 */         paramIndentedWriter.upIndent();
/* 115 */         if (this.mLoggerName == null) {
/* 116 */           paramIndentedWriter.println("e.printStackTrace();");
/*     */         } else {
/*     */           
/* 119 */           paramIndentedWriter.println("if ( " + this.mLoggerName + ".isLoggable( MLevel.FINE ) )");
/* 120 */           paramIndentedWriter.upIndent();
/* 121 */           paramIndentedWriter.println(this.mLoggerName + ".log( MLevel.FINE, \"Inconsistent clone() definitions between subclass and superclass! \", e );");
/* 122 */           paramIndentedWriter.downIndent();
/*     */         } 
/* 124 */         paramIndentedWriter.println("throw new RuntimeException(\"Inconsistent clone() definitions between subclass and superclass! \" + e);");
/* 125 */         paramIndentedWriter.downIndent();
/* 126 */         paramIndentedWriter.println("}");
/*     */       } 
/*     */       
/* 129 */       paramIndentedWriter.downIndent();
/* 130 */       paramIndentedWriter.println("}");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/CloneableExtension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
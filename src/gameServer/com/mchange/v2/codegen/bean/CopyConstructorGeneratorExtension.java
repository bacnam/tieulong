/*    */ package com.mchange.v2.codegen.bean;
/*    */ 
/*    */ import com.mchange.v2.codegen.CodegenUtils;
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
/*    */ public class CopyConstructorGeneratorExtension
/*    */   implements GeneratorExtension
/*    */ {
/* 46 */   int ctor_modifiers = 1;
/*    */   
/*    */   public Collection extraGeneralImports() {
/* 49 */     return Collections.EMPTY_SET;
/*    */   }
/*    */   public Collection extraSpecificImports() {
/* 52 */     return Collections.EMPTY_SET;
/*    */   }
/*    */   public Collection extraInterfaceNames() {
/* 55 */     return Collections.EMPTY_SET;
/*    */   }
/*    */ 
/*    */   
/*    */   public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
/* 60 */     paramIndentedWriter.print(CodegenUtils.getModifierString(this.ctor_modifiers));
/* 61 */     paramIndentedWriter.print(" " + paramClassInfo.getClassName() + "( ");
/* 62 */     paramIndentedWriter.print(paramClassInfo.getClassName() + " copyMe");
/* 63 */     paramIndentedWriter.println(" )");
/* 64 */     paramIndentedWriter.println("{");
/* 65 */     paramIndentedWriter.upIndent(); byte b;
/*    */     int i;
/* 67 */     for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {
/*    */       String str1;
/*    */       
/* 70 */       if (paramArrayOfClass[b] == boolean.class) {
/* 71 */         str1 = "is" + BeangenUtils.capitalize(paramArrayOfProperty[b].getName()) + "()";
/*    */       } else {
/* 73 */         str1 = "get" + BeangenUtils.capitalize(paramArrayOfProperty[b].getName()) + "()";
/* 74 */       }  paramIndentedWriter.println(paramArrayOfProperty[b].getSimpleTypeName() + ' ' + paramArrayOfProperty[b].getName() + " = copyMe." + str1 + ';');
/* 75 */       paramIndentedWriter.print("this." + paramArrayOfProperty[b].getName() + " = ");
/* 76 */       String str2 = paramArrayOfProperty[b].getDefensiveCopyExpression();
/* 77 */       if (str2 == null)
/* 78 */         str2 = paramArrayOfProperty[b].getName(); 
/* 79 */       paramIndentedWriter.println(str2 + ';');
/*    */     } 
/*    */     
/* 82 */     paramIndentedWriter.downIndent();
/* 83 */     paramIndentedWriter.println("}");
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/CopyConstructorGeneratorExtension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
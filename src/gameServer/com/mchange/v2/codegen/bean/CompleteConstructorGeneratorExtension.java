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
/*    */ public class CompleteConstructorGeneratorExtension
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
/* 61 */     paramIndentedWriter.print(paramClassInfo.getClassName() + "( ");
/* 62 */     BeangenUtils.writeArgList(paramArrayOfProperty, true, paramIndentedWriter);
/* 63 */     paramIndentedWriter.println(" )");
/* 64 */     paramIndentedWriter.println("{");
/* 65 */     paramIndentedWriter.upIndent(); byte b;
/*    */     int i;
/* 67 */     for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {
/*    */       
/* 69 */       paramIndentedWriter.print("this." + paramArrayOfProperty[b].getName() + " = ");
/* 70 */       String str = paramArrayOfProperty[b].getDefensiveCopyExpression();
/* 71 */       if (str == null)
/* 72 */         str = paramArrayOfProperty[b].getName(); 
/* 73 */       paramIndentedWriter.println(str + ';');
/*    */     } 
/*    */     
/* 76 */     paramIndentedWriter.downIndent();
/* 77 */     paramIndentedWriter.println("}");
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/CompleteConstructorGeneratorExtension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
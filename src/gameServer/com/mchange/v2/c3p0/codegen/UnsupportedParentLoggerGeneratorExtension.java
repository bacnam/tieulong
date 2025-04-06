/*    */ package com.mchange.v2.c3p0.codegen;
/*    */ 
/*    */ import com.mchange.v2.codegen.IndentedWriter;
/*    */ import com.mchange.v2.codegen.bean.ClassInfo;
/*    */ import com.mchange.v2.codegen.bean.GeneratorExtension;
/*    */ import com.mchange.v2.codegen.bean.Property;
/*    */ import java.io.IOException;
/*    */ import java.util.Arrays;
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
/*    */ public class UnsupportedParentLoggerGeneratorExtension
/*    */   implements GeneratorExtension
/*    */ {
/*    */   public Collection extraGeneralImports() {
/* 47 */     return Collections.EMPTY_SET;
/*    */   }
/*    */   public Collection extraSpecificImports() {
/* 50 */     return Arrays.asList(new String[] { "java.util.logging.Logger", "java.sql.SQLFeatureNotSupportedException" });
/*    */   }
/*    */   public Collection extraInterfaceNames() {
/* 53 */     return Collections.EMPTY_SET;
/*    */   }
/*    */ 
/*    */   
/*    */   public void generate(ClassInfo info, Class superclassType, Property[] props, Class[] propTypes, IndentedWriter iw) throws IOException {
/* 58 */     iw.println("// JDK7 add-on");
/* 59 */     iw.println("public Logger getParentLogger() throws SQLFeatureNotSupportedException");
/* 60 */     iw.println("{ throw new SQLFeatureNotSupportedException(\"javax.sql.DataSource.getParentLogger() is not currently supported by \" + this.getClass().getName());}");
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/codegen/UnsupportedParentLoggerGeneratorExtension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
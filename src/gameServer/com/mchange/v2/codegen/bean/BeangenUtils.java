/*     */ package com.mchange.v2.codegen.bean;
/*     */ 
/*     */ import com.mchange.v1.lang.ClassUtils;
/*     */ import com.mchange.v2.codegen.CodegenUtils;
/*     */ import com.mchange.v2.codegen.IndentedWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.Comparator;
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
/*     */ public final class BeangenUtils
/*     */ {
/*  47 */   public static final Comparator PROPERTY_COMPARATOR = new Comparator()
/*     */     {
/*     */       public int compare(Object param1Object1, Object param1Object2)
/*     */       {
/*  51 */         Property property1 = (Property)param1Object1;
/*  52 */         Property property2 = (Property)param1Object2;
/*     */         
/*  54 */         return String.CASE_INSENSITIVE_ORDER.compare(property1.getName(), property2.getName());
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public static String capitalize(String paramString) {
/*  60 */     char c = paramString.charAt(0);
/*  61 */     return Character.toUpperCase(c) + paramString.substring(1);
/*     */   }
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
/*     */   public static void writeExplicitDefaultConstructor(int paramInt, ClassInfo paramClassInfo, IndentedWriter paramIndentedWriter) throws IOException {
/*  87 */     paramIndentedWriter.print(CodegenUtils.getModifierString(paramInt));
/*  88 */     paramIndentedWriter.println(' ' + paramClassInfo.getClassName() + "()");
/*  89 */     paramIndentedWriter.println("{}");
/*     */   }
/*     */   
/*     */   public static void writeArgList(Property[] paramArrayOfProperty, boolean paramBoolean, IndentedWriter paramIndentedWriter) throws IOException {
/*     */     byte b;
/*     */     int i;
/*  95 */     for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {
/*     */       
/*  97 */       if (b != 0)
/*  98 */         paramIndentedWriter.print(", "); 
/*  99 */       if (paramBoolean)
/* 100 */         paramIndentedWriter.print(paramArrayOfProperty[b].getSimpleTypeName() + ' '); 
/* 101 */       paramIndentedWriter.print(paramArrayOfProperty[b].getName());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writePropertyMember(Property paramProperty, IndentedWriter paramIndentedWriter) throws IOException {
/* 109 */     writePropertyVariable(paramProperty, paramIndentedWriter);
/*     */   }
/*     */   public static void writePropertyVariable(Property paramProperty, IndentedWriter paramIndentedWriter) throws IOException {
/* 112 */     writePropertyVariable(paramProperty, paramProperty.getDefaultValueExpression(), paramIndentedWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writePropertyMember(Property paramProperty, String paramString, IndentedWriter paramIndentedWriter) throws IOException {
/* 118 */     writePropertyVariable(paramProperty, paramString, paramIndentedWriter);
/*     */   }
/*     */   
/*     */   public static void writePropertyVariable(Property paramProperty, String paramString, IndentedWriter paramIndentedWriter) throws IOException {
/* 122 */     paramIndentedWriter.print(CodegenUtils.getModifierString(paramProperty.getVariableModifiers()));
/* 123 */     paramIndentedWriter.print(' ' + paramProperty.getSimpleTypeName() + ' ' + paramProperty.getName());
/* 124 */     String str = paramString;
/* 125 */     if (str != null)
/* 126 */       paramIndentedWriter.print(" = " + str); 
/* 127 */     paramIndentedWriter.println(';');
/*     */   }
/*     */   
/*     */   public static void writePropertyGetter(Property paramProperty, IndentedWriter paramIndentedWriter) throws IOException {
/* 131 */     writePropertyGetter(paramProperty, paramProperty.getDefensiveCopyExpression(), paramIndentedWriter);
/*     */   }
/*     */   
/*     */   public static void writePropertyGetter(Property paramProperty, String paramString, IndentedWriter paramIndentedWriter) throws IOException {
/* 135 */     String str1 = "boolean".equals(paramProperty.getSimpleTypeName()) ? "is" : "get";
/* 136 */     paramIndentedWriter.print(CodegenUtils.getModifierString(paramProperty.getGetterModifiers()));
/* 137 */     paramIndentedWriter.println(' ' + paramProperty.getSimpleTypeName() + ' ' + str1 + capitalize(paramProperty.getName()) + "()");
/* 138 */     String str2 = paramString;
/* 139 */     if (str2 == null) str2 = paramProperty.getName(); 
/* 140 */     paramIndentedWriter.println("{ return " + str2 + "; }");
/*     */   }
/*     */ 
/*     */   
/*     */   public static void writePropertySetter(Property paramProperty, IndentedWriter paramIndentedWriter) throws IOException {
/* 145 */     writePropertySetter(paramProperty, paramProperty.getDefensiveCopyExpression(), paramIndentedWriter);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void writePropertySetter(Property paramProperty, String paramString, IndentedWriter paramIndentedWriter) throws IOException {
/* 150 */     String str1 = paramString;
/* 151 */     if (str1 == null) str1 = paramProperty.getName(); 
/* 152 */     String str2 = "this." + paramProperty.getName();
/* 153 */     String str3 = "this." + paramProperty.getName() + " = " + str1 + ';';
/* 154 */     writePropertySetterWithGetExpressionSetStatement(paramProperty, str2, str3, paramIndentedWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writePropertySetterWithGetExpressionSetStatement(Property paramProperty, String paramString1, String paramString2, IndentedWriter paramIndentedWriter) throws IOException {
/* 160 */     paramIndentedWriter.print(CodegenUtils.getModifierString(paramProperty.getSetterModifiers()));
/* 161 */     paramIndentedWriter.print(" void set" + capitalize(paramProperty.getName()) + "( " + paramProperty.getSimpleTypeName() + ' ' + paramProperty.getName() + " )");
/* 162 */     if (paramProperty.isConstrained()) {
/* 163 */       paramIndentedWriter.println(" throws PropertyVetoException");
/*     */     } else {
/* 165 */       paramIndentedWriter.println();
/* 166 */     }  paramIndentedWriter.println('{');
/* 167 */     paramIndentedWriter.upIndent();
/*     */     
/* 169 */     if (changeMarked(paramProperty)) {
/*     */       String str3;
/* 171 */       paramIndentedWriter.println(paramProperty.getSimpleTypeName() + " oldVal = " + paramString1 + ';');
/*     */       
/* 173 */       String str1 = "oldVal";
/* 174 */       String str2 = paramProperty.getName();
/*     */ 
/*     */       
/* 177 */       String str4 = paramProperty.getSimpleTypeName();
/* 178 */       if (ClassUtils.isPrimitive(str4)) {
/*     */         
/* 180 */         Class<byte> clazz = ClassUtils.classForPrimitive(str4);
/*     */ 
/*     */ 
/*     */         
/* 184 */         if (clazz == byte.class) {
/*     */           
/* 186 */           str1 = "new Byte( " + str1 + " )";
/* 187 */           str2 = "new Byte( " + str2 + " )";
/*     */         }
/* 189 */         else if (clazz == char.class) {
/*     */           
/* 191 */           str1 = "new Character( " + str1 + " )";
/* 192 */           str2 = "new Character( " + str2 + " )";
/*     */         }
/* 194 */         else if (clazz == short.class) {
/*     */           
/* 196 */           str1 = "new Short( " + str1 + " )";
/* 197 */           str2 = "new Short( " + str2 + " )";
/*     */         }
/* 199 */         else if (clazz == float.class) {
/*     */           
/* 201 */           str1 = "new Float( " + str1 + " )";
/* 202 */           str2 = "new Float( " + str2 + " )";
/*     */         }
/* 204 */         else if (clazz == double.class) {
/*     */           
/* 206 */           str1 = "new Double( " + str1 + " )";
/* 207 */           str2 = "new Double( " + str2 + " )";
/*     */         } 
/*     */         
/* 210 */         str3 = "oldVal != " + paramProperty.getName();
/*     */       } else {
/*     */         
/* 213 */         str3 = "! eqOrBothNull( oldVal, " + paramProperty.getName() + " )";
/*     */       } 
/* 215 */       if (paramProperty.isConstrained()) {
/*     */         
/* 217 */         paramIndentedWriter.println("if ( " + str3 + " )");
/* 218 */         paramIndentedWriter.upIndent();
/* 219 */         paramIndentedWriter.println("vcs.fireVetoableChange( \"" + paramProperty.getName() + "\", " + str1 + ", " + str2 + " );");
/* 220 */         paramIndentedWriter.downIndent();
/*     */       } 
/*     */       
/* 223 */       paramIndentedWriter.println(paramString2);
/*     */       
/* 225 */       if (paramProperty.isBound()) {
/*     */         
/* 227 */         paramIndentedWriter.println("if ( " + str3 + " )");
/* 228 */         paramIndentedWriter.upIndent();
/* 229 */         paramIndentedWriter.println("pcs.firePropertyChange( \"" + paramProperty.getName() + "\", " + str1 + ", " + str2 + " );");
/* 230 */         paramIndentedWriter.downIndent();
/*     */       } 
/*     */     } else {
/*     */       
/* 234 */       paramIndentedWriter.println(paramString2);
/*     */     } 
/* 236 */     paramIndentedWriter.downIndent();
/* 237 */     paramIndentedWriter.println('}');
/*     */   }
/*     */   public static boolean hasBoundProperties(Property[] paramArrayOfProperty) {
/*     */     byte b;
/*     */     int i;
/* 242 */     for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {
/* 243 */       if (paramArrayOfProperty[b].isBound()) return true; 
/* 244 */     }  return false;
/*     */   }
/*     */   public static boolean hasConstrainedProperties(Property[] paramArrayOfProperty) {
/*     */     byte b;
/*     */     int i;
/* 249 */     for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {
/* 250 */       if (paramArrayOfProperty[b].isConstrained()) return true; 
/* 251 */     }  return false;
/*     */   }
/*     */   
/*     */   private static boolean changeMarked(Property paramProperty) {
/* 255 */     return (paramProperty.isBound() || paramProperty.isConstrained());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/BeangenUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
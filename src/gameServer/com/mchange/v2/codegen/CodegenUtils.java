/*     */ package com.mchange.v2.codegen;
/*     */ 
/*     */ import com.mchange.v1.lang.ClassUtils;
/*     */ import java.io.File;
/*     */ import java.io.Writer;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
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
/*     */ public final class CodegenUtils
/*     */ {
/*     */   public static String getModifierString(int paramInt) {
/*  47 */     StringBuffer stringBuffer = new StringBuffer(32);
/*  48 */     if (Modifier.isPublic(paramInt))
/*  49 */       stringBuffer.append("public "); 
/*  50 */     if (Modifier.isProtected(paramInt))
/*  51 */       stringBuffer.append("protected "); 
/*  52 */     if (Modifier.isPrivate(paramInt))
/*  53 */       stringBuffer.append("private "); 
/*  54 */     if (Modifier.isAbstract(paramInt))
/*  55 */       stringBuffer.append("abstract "); 
/*  56 */     if (Modifier.isStatic(paramInt))
/*  57 */       stringBuffer.append("static "); 
/*  58 */     if (Modifier.isFinal(paramInt))
/*  59 */       stringBuffer.append("final "); 
/*  60 */     if (Modifier.isSynchronized(paramInt))
/*  61 */       stringBuffer.append("synchronized "); 
/*  62 */     if (Modifier.isTransient(paramInt))
/*  63 */       stringBuffer.append("transient "); 
/*  64 */     if (Modifier.isVolatile(paramInt))
/*  65 */       stringBuffer.append("volatile "); 
/*  66 */     if (Modifier.isStrict(paramInt))
/*  67 */       stringBuffer.append("strictfp "); 
/*  68 */     if (Modifier.isNative(paramInt))
/*  69 */       stringBuffer.append("native "); 
/*  70 */     if (Modifier.isInterface(paramInt))
/*  71 */       stringBuffer.append("interface "); 
/*  72 */     return stringBuffer.toString().trim();
/*     */   }
/*     */ 
/*     */   
/*     */   public static Class unarrayClass(Class<?> paramClass) {
/*  77 */     Class<?> clazz = paramClass;
/*  78 */     while (clazz.isArray())
/*  79 */       clazz = clazz.getComponentType(); 
/*  80 */     return clazz;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean inSamePackage(String paramString1, String paramString2) {
/*  85 */     int i = paramString1.lastIndexOf('.');
/*  86 */     int j = paramString2.lastIndexOf('.');
/*     */ 
/*     */     
/*  89 */     if (i < 0 || j < 0)
/*  90 */       return true; 
/*  91 */     if (paramString1.substring(0, i).equals(paramString1.substring(0, i))) {
/*     */       
/*  93 */       if (paramString2.indexOf('.') >= 0) {
/*  94 */         return false;
/*     */       }
/*  96 */       return true;
/*     */     } 
/*     */     
/*  99 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String fqcnLastElement(String paramString) {
/* 106 */     return ClassUtils.fqcnLastElement(paramString);
/*     */   }
/*     */   public static String methodSignature(Method paramMethod) {
/* 109 */     return methodSignature(paramMethod, null);
/*     */   }
/*     */   public static String methodSignature(Method paramMethod, String[] paramArrayOfString) {
/* 112 */     return methodSignature(1, paramMethod, paramArrayOfString);
/*     */   }
/*     */   
/*     */   public static String methodSignature(int paramInt, Method paramMethod, String[] paramArrayOfString) {
/* 116 */     StringBuffer stringBuffer = new StringBuffer(256);
/* 117 */     stringBuffer.append(getModifierString(paramInt));
/* 118 */     stringBuffer.append(' ');
/* 119 */     stringBuffer.append(ClassUtils.simpleClassName(paramMethod.getReturnType()));
/* 120 */     stringBuffer.append(' ');
/* 121 */     stringBuffer.append(paramMethod.getName());
/* 122 */     stringBuffer.append('(');
/* 123 */     Class[] arrayOfClass1 = paramMethod.getParameterTypes(); int i;
/* 124 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 126 */       if (b != 0)
/* 127 */         stringBuffer.append(", "); 
/* 128 */       stringBuffer.append(ClassUtils.simpleClassName(arrayOfClass1[b]));
/* 129 */       stringBuffer.append(' ');
/* 130 */       stringBuffer.append((paramArrayOfString == null) ? String.valueOf((char)(97 + b)) : paramArrayOfString[b]);
/*     */     } 
/* 132 */     stringBuffer.append(')');
/* 133 */     Class[] arrayOfClass2 = paramMethod.getExceptionTypes();
/* 134 */     if (arrayOfClass2.length > 0) {
/*     */       
/* 136 */       stringBuffer.append(" throws "); int j;
/* 137 */       for (i = 0, j = arrayOfClass2.length; i < j; i++) {
/*     */         
/* 139 */         if (i != 0)
/* 140 */           stringBuffer.append(", "); 
/* 141 */         stringBuffer.append(ClassUtils.simpleClassName(arrayOfClass2[i]));
/*     */       } 
/*     */     } 
/* 144 */     return stringBuffer.toString();
/*     */   }
/*     */   
/*     */   public static String methodCall(Method paramMethod) {
/* 148 */     return methodCall(paramMethod, null);
/*     */   }
/*     */   
/*     */   public static String methodCall(Method paramMethod, String[] paramArrayOfString) {
/* 152 */     StringBuffer stringBuffer = new StringBuffer(256);
/* 153 */     stringBuffer.append(paramMethod.getName());
/* 154 */     stringBuffer.append('(');
/* 155 */     Class[] arrayOfClass = paramMethod.getParameterTypes(); byte b; int i;
/* 156 */     for (b = 0, i = arrayOfClass.length; b < i; b++) {
/*     */       
/* 158 */       if (b != 0)
/* 159 */         stringBuffer.append(", "); 
/* 160 */       stringBuffer.append((paramArrayOfString == null) ? generatedArgumentName(b) : paramArrayOfString[b]);
/*     */     } 
/* 162 */     stringBuffer.append(')');
/* 163 */     return stringBuffer.toString();
/*     */   }
/*     */   
/*     */   public static String reflectiveMethodObjectArray(Method paramMethod) {
/* 167 */     return reflectiveMethodObjectArray(paramMethod, null);
/*     */   }
/*     */   
/*     */   public static String reflectiveMethodObjectArray(Method paramMethod, String[] paramArrayOfString) {
/* 171 */     StringBuffer stringBuffer = new StringBuffer(256);
/* 172 */     stringBuffer.append("new Object[] ");
/* 173 */     stringBuffer.append('{');
/* 174 */     Class[] arrayOfClass = paramMethod.getParameterTypes(); byte b; int i;
/* 175 */     for (b = 0, i = arrayOfClass.length; b < i; b++) {
/*     */       
/* 177 */       if (b != 0)
/* 178 */         stringBuffer.append(", "); 
/* 179 */       stringBuffer.append((paramArrayOfString == null) ? generatedArgumentName(b) : paramArrayOfString[b]);
/*     */     } 
/* 181 */     stringBuffer.append('}');
/* 182 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String reflectiveMethodParameterTypeArray(Method paramMethod) {
/* 187 */     StringBuffer stringBuffer = new StringBuffer(256);
/* 188 */     stringBuffer.append("new Class[] ");
/* 189 */     stringBuffer.append('{');
/* 190 */     Class[] arrayOfClass = paramMethod.getParameterTypes(); byte b; int i;
/* 191 */     for (b = 0, i = arrayOfClass.length; b < i; b++) {
/*     */       
/* 193 */       if (b != 0)
/* 194 */         stringBuffer.append(", "); 
/* 195 */       stringBuffer.append(ClassUtils.simpleClassName(arrayOfClass[b]));
/* 196 */       stringBuffer.append(".class");
/*     */     } 
/* 198 */     stringBuffer.append('}');
/* 199 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String generatedArgumentName(int paramInt) {
/* 204 */     return String.valueOf((char)(97 + paramInt));
/*     */   }
/*     */   public static String simpleClassName(Class paramClass) {
/* 207 */     return ClassUtils.simpleClassName(paramClass);
/*     */   }
/*     */   public static IndentedWriter toIndentedWriter(Writer paramWriter) {
/* 210 */     return (paramWriter instanceof IndentedWriter) ? (IndentedWriter)paramWriter : new IndentedWriter(paramWriter);
/*     */   }
/*     */   
/*     */   public static String packageNameToFileSystemDirPath(String paramString) {
/* 214 */     StringBuffer stringBuffer = new StringBuffer(paramString); byte b; int i;
/* 215 */     for (b = 0, i = stringBuffer.length(); b < i; b++) {
/* 216 */       if (stringBuffer.charAt(b) == '.')
/* 217 */         stringBuffer.setCharAt(b, File.separatorChar); 
/* 218 */     }  stringBuffer.append(File.separatorChar);
/* 219 */     return stringBuffer.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/CodegenUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
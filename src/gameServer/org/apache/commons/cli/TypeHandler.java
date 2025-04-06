/*     */ package org.apache.commons.cli;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Date;
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
/*     */ public class TypeHandler
/*     */ {
/*     */   public static Object createValue(String str, Object obj) throws ParseException {
/*  49 */     return createValue(str, (Class)obj);
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
/*     */   public static Object createValue(String str, Class clazz) throws ParseException {
/*  64 */     if (PatternOptionBuilder.STRING_VALUE == clazz)
/*     */     {
/*  66 */       return str;
/*     */     }
/*  68 */     if (PatternOptionBuilder.OBJECT_VALUE == clazz)
/*     */     {
/*  70 */       return createObject(str);
/*     */     }
/*  72 */     if (PatternOptionBuilder.NUMBER_VALUE == clazz)
/*     */     {
/*  74 */       return createNumber(str);
/*     */     }
/*  76 */     if (PatternOptionBuilder.DATE_VALUE == clazz)
/*     */     {
/*  78 */       return createDate(str);
/*     */     }
/*  80 */     if (PatternOptionBuilder.CLASS_VALUE == clazz)
/*     */     {
/*  82 */       return createClass(str);
/*     */     }
/*  84 */     if (PatternOptionBuilder.FILE_VALUE == clazz)
/*     */     {
/*  86 */       return createFile(str);
/*     */     }
/*  88 */     if (PatternOptionBuilder.EXISTING_FILE_VALUE == clazz)
/*     */     {
/*  90 */       return createFile(str);
/*     */     }
/*  92 */     if (PatternOptionBuilder.FILES_VALUE == clazz)
/*     */     {
/*  94 */       return createFiles(str);
/*     */     }
/*  96 */     if (PatternOptionBuilder.URL_VALUE == clazz)
/*     */     {
/*  98 */       return createURL(str);
/*     */     }
/*     */ 
/*     */     
/* 102 */     return null;
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
/*     */   public static Object createObject(String classname) throws ParseException {
/* 116 */     Class cl = null;
/*     */ 
/*     */     
/*     */     try {
/* 120 */       cl = Class.forName(classname);
/*     */     }
/* 122 */     catch (ClassNotFoundException cnfe) {
/*     */       
/* 124 */       throw new ParseException("Unable to find the class: " + classname);
/*     */     } 
/*     */     
/* 127 */     Object instance = null;
/*     */ 
/*     */     
/*     */     try {
/* 131 */       instance = cl.newInstance();
/*     */     }
/* 133 */     catch (Exception e) {
/*     */       
/* 135 */       throw new ParseException(e.getClass().getName() + "; Unable to create an instance of: " + classname);
/*     */     } 
/*     */     
/* 138 */     return instance;
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
/*     */   public static Number createNumber(String str) throws ParseException {
/*     */     try {
/* 154 */       if (str.indexOf('.') != -1)
/*     */       {
/* 156 */         return Double.valueOf(str);
/*     */       }
/*     */ 
/*     */       
/* 160 */       return Long.valueOf(str);
/*     */     
/*     */     }
/* 163 */     catch (NumberFormatException e) {
/*     */       
/* 165 */       throw new ParseException(e.getMessage());
/*     */     } 
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
/*     */   public static Class createClass(String classname) throws ParseException {
/*     */     try {
/* 180 */       return Class.forName(classname);
/*     */     }
/* 182 */     catch (ClassNotFoundException e) {
/*     */       
/* 184 */       throw new ParseException("Unable to find the class: " + classname);
/*     */     } 
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
/*     */   public static Date createDate(String str) throws ParseException {
/* 198 */     throw new UnsupportedOperationException("Not yet implemented");
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
/*     */   public static URL createURL(String str) throws ParseException {
/*     */     try {
/* 213 */       return new URL(str);
/*     */     }
/* 215 */     catch (MalformedURLException e) {
/*     */       
/* 217 */       throw new ParseException("Unable to parse the URL: " + str);
/*     */     } 
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
/*     */   public static File createFile(String str) throws ParseException {
/* 230 */     return new File(str);
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
/*     */   public static File[] createFiles(String str) throws ParseException {
/* 244 */     throw new UnsupportedOperationException("Not yet implemented");
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/commons/cli/TypeHandler.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */
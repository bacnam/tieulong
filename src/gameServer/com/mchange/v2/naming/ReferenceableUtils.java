/*     */ package com.mchange.v2.naming;
/*     */ 
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.StringRefAddr;
/*     */ import javax.naming.spi.ObjectFactory;
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
/*     */ public final class ReferenceableUtils
/*     */ {
/*  48 */   static final MLogger logger = MLog.getLogger(ReferenceableUtils.class);
/*     */ 
/*     */   
/*     */   static final String REFADDR_VERSION = "version";
/*     */   
/*     */   static final String REFADDR_CLASSNAME = "classname";
/*     */   
/*     */   static final String REFADDR_FACTORY = "factory";
/*     */   
/*     */   static final String REFADDR_FACTORY_CLASS_LOCATION = "factoryClassLocation";
/*     */   
/*     */   static final String REFADDR_SIZE = "size";
/*     */   
/*     */   static final int CURRENT_REF_VERSION = 1;
/*     */ 
/*     */   
/*     */   public static String literalNullToNull(String paramString) {
/*  65 */     if (paramString == null || "null".equals(paramString)) {
/*  66 */       return null;
/*     */     }
/*  68 */     return paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object referenceToObject(Reference paramReference, Name paramName, Context paramContext, Hashtable<?, ?> paramHashtable) throws NamingException {
/*     */     try {
/*     */       ClassLoader classLoader;
/*  76 */       String str1 = paramReference.getFactoryClassName();
/*  77 */       String str2 = paramReference.getFactoryClassLocation();
/*     */ 
/*     */       
/*  80 */       if (str2 == null) {
/*  81 */         classLoader = ClassLoader.getSystemClassLoader();
/*     */       } else {
/*     */         
/*  84 */         URL uRL = new URL(str2);
/*  85 */         classLoader = new URLClassLoader(new URL[] { uRL }, ClassLoader.getSystemClassLoader());
/*     */       } 
/*     */       
/*  88 */       Class<?> clazz = Class.forName(str1, true, classLoader);
/*  89 */       ObjectFactory objectFactory = (ObjectFactory)clazz.newInstance();
/*  90 */       return objectFactory.getObjectInstance(paramReference, paramName, paramContext, paramHashtable);
/*     */     }
/*  92 */     catch (Exception exception) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  97 */       if (logger.isLoggable(MLevel.FINE)) {
/*  98 */         logger.log(MLevel.FINE, "Could not resolve Reference to Object!", exception);
/*     */       }
/* 100 */       NamingException namingException = new NamingException("Could not resolve Reference to Object!");
/* 101 */       namingException.setRootCause(exception);
/* 102 */       throw namingException;
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
/*     */   public static void appendToReference(Reference paramReference1, Reference paramReference2) throws NamingException {
/* 114 */     int i = paramReference2.size();
/* 115 */     paramReference1.add(new StringRefAddr("version", String.valueOf(1)));
/* 116 */     paramReference1.add(new StringRefAddr("classname", paramReference2.getClassName()));
/* 117 */     paramReference1.add(new StringRefAddr("factory", paramReference2.getFactoryClassName()));
/* 118 */     paramReference1.add(new StringRefAddr("factoryClassLocation", paramReference2.getFactoryClassLocation()));
/*     */     
/* 120 */     paramReference1.add(new StringRefAddr("size", String.valueOf(i)));
/* 121 */     for (byte b = 0; b < i; b++) {
/* 122 */       paramReference1.add(paramReference2.get(b));
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
/*     */   public static ExtractRec extractNestedReference(Reference paramReference, int paramInt) throws NamingException {
/*     */     try {
/* 135 */       int i = Integer.parseInt((String)paramReference.get(paramInt++).getContent());
/* 136 */       if (i == 1) {
/*     */         
/* 138 */         String str1 = (String)paramReference.get(paramInt++).getContent();
/* 139 */         String str2 = (String)paramReference.get(paramInt++).getContent();
/* 140 */         String str3 = (String)paramReference.get(paramInt++).getContent();
/*     */         
/* 142 */         Reference reference = new Reference(str1, str2, str3);
/*     */ 
/*     */         
/* 145 */         int j = Integer.parseInt((String)paramReference.get(paramInt++).getContent());
/* 146 */         for (byte b = 0; b < j; b++)
/* 147 */           reference.add(paramReference.get(paramInt++)); 
/* 148 */         return new ExtractRec(reference, paramInt);
/*     */       } 
/*     */       
/* 151 */       throw new NamingException("Bad version of nested reference!!!");
/*     */     }
/* 153 */     catch (NumberFormatException numberFormatException) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 158 */       if (logger.isLoggable(MLevel.FINE)) {
/* 159 */         logger.log(MLevel.FINE, "Version or size nested reference was not a number!!!", numberFormatException);
/*     */       }
/* 161 */       throw new NamingException("Version or size nested reference was not a number!!!");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ExtractRec
/*     */   {
/*     */     public Reference ref;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int index;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ExtractRec(Reference param1Reference, int param1Int) {
/* 182 */       this.ref = param1Reference;
/* 183 */       this.index = param1Int;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/naming/ReferenceableUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
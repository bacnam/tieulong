/*     */ package com.mchange.v2.log;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ResourceBundle;
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
/*     */ public final class LogUtils
/*     */ {
/*     */   public static String createParamsList(Object[] paramArrayOfObject) {
/*  45 */     StringBuffer stringBuffer = new StringBuffer(511);
/*  46 */     appendParamsList(stringBuffer, paramArrayOfObject);
/*  47 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void appendParamsList(StringBuffer paramStringBuffer, Object[] paramArrayOfObject) {
/*  52 */     paramStringBuffer.append("[params: ");
/*  53 */     if (paramArrayOfObject != null) {
/*     */       byte b; int i;
/*  55 */       for (b = 0, i = paramArrayOfObject.length; b < i; b++) {
/*     */         
/*  57 */         if (b != 0) paramStringBuffer.append(", "); 
/*  58 */         paramStringBuffer.append(paramArrayOfObject[b]);
/*     */       } 
/*     */     } 
/*  61 */     paramStringBuffer.append(']');
/*     */   }
/*     */ 
/*     */   
/*     */   public static String createMessage(String paramString1, String paramString2, String paramString3) {
/*  66 */     StringBuffer stringBuffer = new StringBuffer(511);
/*  67 */     stringBuffer.append("[class: ");
/*  68 */     stringBuffer.append(paramString1);
/*  69 */     stringBuffer.append("; method: ");
/*  70 */     stringBuffer.append(paramString2);
/*  71 */     if (!paramString2.endsWith(")"))
/*  72 */       stringBuffer.append("()"); 
/*  73 */     stringBuffer.append("] ");
/*  74 */     stringBuffer.append(paramString3);
/*  75 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String createMessage(String paramString1, String paramString2) {
/*  80 */     StringBuffer stringBuffer = new StringBuffer(511);
/*  81 */     stringBuffer.append("[method: ");
/*  82 */     stringBuffer.append(paramString1);
/*  83 */     if (!paramString1.endsWith(")"))
/*  84 */       stringBuffer.append("()"); 
/*  85 */     stringBuffer.append("] ");
/*  86 */     stringBuffer.append(paramString2);
/*  87 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String formatMessage(String paramString1, String paramString2, Object[] paramArrayOfObject) {
/*  92 */     if (paramString2 == null) {
/*     */       
/*  94 */       if (paramArrayOfObject == null) {
/*  95 */         return "";
/*     */       }
/*  97 */       return createParamsList(paramArrayOfObject);
/*     */     } 
/*     */ 
/*     */     
/* 101 */     if (paramString1 != null) {
/*     */       
/* 103 */       ResourceBundle resourceBundle = ResourceBundle.getBundle(paramString1);
/* 104 */       if (resourceBundle != null) {
/*     */         
/* 106 */         String str = resourceBundle.getString(paramString2);
/* 107 */         if (str != null)
/* 108 */           paramString2 = str; 
/*     */       } 
/*     */     } 
/* 111 */     return (paramArrayOfObject == null) ? paramString2 : MessageFormat.format(paramString2, paramArrayOfObject);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/log/LogUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
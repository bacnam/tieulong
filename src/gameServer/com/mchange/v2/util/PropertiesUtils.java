/*    */ package com.mchange.v2.util;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.util.Properties;
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
/*    */ 
/*    */ 
/*    */ public final class PropertiesUtils
/*    */ {
/*    */   public static int getIntProperty(Properties paramProperties, String paramString, int paramInt) throws NumberFormatException {
/* 48 */     String str = paramProperties.getProperty(paramString);
/* 49 */     return (str != null) ? Integer.parseInt(str) : paramInt;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static Properties fromString(String paramString1, String paramString2) throws UnsupportedEncodingException {
/*    */     try {
/* 56 */       Properties properties = new Properties();
/* 57 */       if (paramString1 != null) {
/*    */         
/* 59 */         byte[] arrayOfByte = paramString1.getBytes(paramString2);
/* 60 */         properties.load(new ByteArrayInputStream(arrayOfByte));
/*    */       } 
/* 62 */       return properties;
/*    */     }
/* 64 */     catch (UnsupportedEncodingException unsupportedEncodingException) {
/* 65 */       throw unsupportedEncodingException;
/* 66 */     } catch (IOException iOException) {
/* 67 */       throw new Error("Huh? An IOException while working with byte array streams?!", iOException);
/*    */     } 
/*    */   }
/*    */   public static Properties fromString(String paramString) {
/*    */     try {
/* 72 */       return fromString(paramString, "ISO-8859-1");
/* 73 */     } catch (UnsupportedEncodingException unsupportedEncodingException) {
/* 74 */       throw new Error("Huh? An ISO-8859-1 is an unsupported encoding?!", unsupportedEncodingException);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static String toString(Properties paramProperties, String paramString1, String paramString2) throws UnsupportedEncodingException {
/*    */     try {
/* 81 */       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/* 82 */       paramProperties.store(byteArrayOutputStream, paramString1);
/* 83 */       byteArrayOutputStream.flush();
/* 84 */       return new String(byteArrayOutputStream.toByteArray(), paramString2);
/*    */     }
/* 86 */     catch (UnsupportedEncodingException unsupportedEncodingException) {
/* 87 */       throw unsupportedEncodingException;
/* 88 */     } catch (IOException iOException) {
/* 89 */       throw new Error("Huh? An IOException while working with byte array streams?!", iOException);
/*    */     } 
/*    */   }
/*    */   public static String toString(Properties paramProperties, String paramString) {
/*    */     try {
/* 94 */       return toString(paramProperties, paramString, "ISO-8859-1");
/* 95 */     } catch (UnsupportedEncodingException unsupportedEncodingException) {
/* 96 */       throw new Error("Huh? An ISO-8859-1 is an unsupported encoding?!", unsupportedEncodingException);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/util/PropertiesUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
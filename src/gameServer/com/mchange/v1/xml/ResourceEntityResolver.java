/*    */ package com.mchange.v1.xml;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.InputSource;
/*    */ import org.xml.sax.SAXException;
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
/*    */ public class ResourceEntityResolver
/*    */   implements EntityResolver
/*    */ {
/*    */   ClassLoader cl;
/*    */   String prefix;
/*    */   
/*    */   public ResourceEntityResolver(ClassLoader paramClassLoader, String paramString) {
/* 52 */     this.cl = paramClassLoader;
/* 53 */     this.prefix = paramString;
/*    */   }
/*    */   
/*    */   public ResourceEntityResolver(Class paramClass) {
/* 57 */     this(paramClass.getClassLoader(), classToPrefix(paramClass));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InputSource resolveEntity(String paramString1, String paramString2) throws SAXException, IOException {
/* 65 */     if (paramString2 == null) {
/* 66 */       return null;
/*    */     }
/* 68 */     int i = paramString2.lastIndexOf('/');
/* 69 */     String str = (i >= 0) ? paramString2.substring(i + 1) : paramString2;
/* 70 */     InputStream inputStream = this.cl.getResourceAsStream(this.prefix + str);
/* 71 */     return (inputStream == null) ? null : new InputSource(inputStream);
/*    */   }
/*    */ 
/*    */   
/*    */   private static String classToPrefix(Class paramClass) {
/* 76 */     String str1 = paramClass.getName();
/* 77 */     int i = str1.lastIndexOf('.');
/* 78 */     String str2 = (i > 0) ? str1.substring(0, i) : null;
/* 79 */     StringBuffer stringBuffer = new StringBuffer(256);
/*    */ 
/*    */     
/* 82 */     if (str2 != null) {
/*    */       
/* 84 */       stringBuffer.append(str2); byte b; int j;
/* 85 */       for (b = 0, j = stringBuffer.length(); b < j; b++) {
/* 86 */         if (stringBuffer.charAt(b) == '.') stringBuffer.setCharAt(b, '/'); 
/* 87 */       }  stringBuffer.append('/');
/*    */     } 
/* 89 */     return stringBuffer.toString();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/xml/ResourceEntityResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
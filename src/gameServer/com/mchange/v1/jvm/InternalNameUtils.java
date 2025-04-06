/*     */ package com.mchange.v1.jvm;
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
/*     */ public final class InternalNameUtils
/*     */ {
/*     */   public static String dottifySlashesAndDollarSigns(String paramString) {
/*  41 */     return _dottifySlashesAndDollarSigns(paramString).toString();
/*     */   }
/*     */   public static String decodeType(String paramString) throws TypeFormatException {
/*  44 */     return _decodeType(paramString).toString();
/*     */   }
/*     */   
/*     */   public static String decodeTypeList(String paramString) throws TypeFormatException {
/*  48 */     StringBuffer stringBuffer = new StringBuffer(64);
/*  49 */     _decodeTypeList(paramString, 0, stringBuffer);
/*  50 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isPrimitive(char paramChar) {
/*  55 */     return (paramChar == 'Z' || paramChar == 'B' || paramChar == 'C' || paramChar == 'S' || paramChar == 'I' || paramChar == 'J' || paramChar == 'F' || paramChar == 'D' || paramChar == 'V');
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
/*     */   private static void _decodeTypeList(String paramString, int paramInt, StringBuffer paramStringBuffer) throws TypeFormatException {
/*  68 */     if (paramStringBuffer.length() != 0) {
/*  69 */       paramStringBuffer.append(' ');
/*     */     }
/*  71 */     char c = paramString.charAt(paramInt);
/*  72 */     if (isPrimitive(c)) {
/*     */       
/*  74 */       paramStringBuffer.append(_decodeType(paramString.substring(paramInt, paramInt + 1)));
/*  75 */       paramInt++;
/*     */     } else {
/*     */       int i;
/*     */ 
/*     */ 
/*     */       
/*  81 */       if (c == '[') {
/*     */         
/*  83 */         int j = paramInt + 1;
/*  84 */         while (paramString.charAt(j) == '[')
/*  85 */           j++; 
/*  86 */         if (paramString.charAt(j) == 'L') {
/*     */           
/*  88 */           j++;
/*  89 */           while (paramString.charAt(j) != ';')
/*  90 */             j++; 
/*     */         } 
/*  92 */         i = j;
/*     */       }
/*     */       else {
/*     */         
/*  96 */         i = paramString.indexOf(';', paramInt);
/*  97 */         if (i < 0) {
/*  98 */           throw new TypeFormatException(paramString.substring(paramInt) + " is neither a primitive nor semicolon terminated!");
/*     */         }
/*     */       } 
/* 101 */       paramStringBuffer.append(_decodeType(paramString.substring(paramInt, paramInt = i + 1)));
/*     */     } 
/* 103 */     if (paramInt < paramString.length()) {
/*     */       
/* 105 */       paramStringBuffer.append(',');
/* 106 */       _decodeTypeList(paramString, paramInt, paramStringBuffer);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static StringBuffer _decodeType(String paramString) throws TypeFormatException {
/*     */     StringBuffer stringBuffer;
/* 114 */     byte b1 = 0;
/*     */ 
/*     */     
/* 117 */     char c = paramString.charAt(0);
/*     */     
/* 119 */     switch (c) {
/*     */       
/*     */       case 'Z':
/* 122 */         stringBuffer = new StringBuffer("boolean");
/*     */         break;
/*     */       case 'B':
/* 125 */         stringBuffer = new StringBuffer("byte");
/*     */         break;
/*     */       case 'C':
/* 128 */         stringBuffer = new StringBuffer("char");
/*     */         break;
/*     */       case 'S':
/* 131 */         stringBuffer = new StringBuffer("short");
/*     */         break;
/*     */       case 'I':
/* 134 */         stringBuffer = new StringBuffer("int");
/*     */         break;
/*     */       case 'J':
/* 137 */         stringBuffer = new StringBuffer("long");
/*     */         break;
/*     */       case 'F':
/* 140 */         stringBuffer = new StringBuffer("float");
/*     */         break;
/*     */       case 'D':
/* 143 */         stringBuffer = new StringBuffer("double");
/*     */         break;
/*     */       case 'V':
/* 146 */         stringBuffer = new StringBuffer("void");
/*     */         break;
/*     */       case '[':
/* 149 */         b1++;
/* 150 */         stringBuffer = _decodeType(paramString.substring(1));
/*     */         break;
/*     */       case 'L':
/* 153 */         stringBuffer = _decodeSimpleClassType(paramString);
/*     */         break;
/*     */       default:
/* 156 */         throw new TypeFormatException(paramString + " is not a valid inernal type name.");
/*     */     } 
/* 158 */     for (byte b2 = 0; b2 < b1; b2++)
/* 159 */       stringBuffer.append("[]"); 
/* 160 */     return stringBuffer;
/*     */   }
/*     */ 
/*     */   
/*     */   private static StringBuffer _decodeSimpleClassType(String paramString) throws TypeFormatException {
/* 165 */     int i = paramString.length();
/* 166 */     if (paramString.charAt(0) != 'L' || paramString.charAt(i - 1) != ';') {
/* 167 */       throw new TypeFormatException(paramString + " is not a valid representation of a simple class type.");
/*     */     }
/* 169 */     return _dottifySlashesAndDollarSigns(paramString.substring(1, i - 1));
/*     */   }
/*     */ 
/*     */   
/*     */   private static StringBuffer _dottifySlashesAndDollarSigns(String paramString) {
/* 174 */     StringBuffer stringBuffer = new StringBuffer(paramString); byte b; int i;
/* 175 */     for (b = 0, i = stringBuffer.length(); b < i; b++) {
/*     */       
/* 177 */       char c = stringBuffer.charAt(b);
/* 178 */       if (c == '/' || c == '$')
/* 179 */         stringBuffer.setCharAt(b, '.'); 
/*     */     } 
/* 181 */     return stringBuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/*     */     try {
/* 192 */       System.out.println(decodeTypeList(paramArrayOfString[0]));
/*     */     }
/* 194 */     catch (Exception exception) {
/* 195 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/jvm/InternalNameUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
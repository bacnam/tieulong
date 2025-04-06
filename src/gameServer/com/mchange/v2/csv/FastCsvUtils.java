/*     */ package com.mchange.v2.csv;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public final class FastCsvUtils
/*     */ {
/*     */   private static final int ESCAPE_BIT = 16777216;
/*     */   private static final int SHIFT_BIT = 33554432;
/*     */   private static final int SHIFT_OFFSET = 8;
/*     */   
/*     */   public static String csvReadLine(BufferedReader paramBufferedReader) throws IOException {
/*  53 */     String str2, str1 = paramBufferedReader.readLine();
/*     */ 
/*     */     
/*  56 */     if (str1 != null) {
/*     */       
/*  58 */       int i = countQuotes(str1);
/*  59 */       if (i % 2 != 0) {
/*     */         
/*  61 */         StringBuilder stringBuilder = new StringBuilder(str1);
/*     */         
/*     */         while (true) {
/*  64 */           str1 = paramBufferedReader.readLine();
/*  65 */           stringBuilder.append(str1);
/*  66 */           i += countQuotes(str1);
/*     */           
/*  68 */           if (i % 2 == 0)
/*  69 */             return stringBuilder.toString(); 
/*     */         } 
/*     */       } 
/*  72 */       str2 = str1;
/*     */     } else {
/*     */       
/*  75 */       str2 = null;
/*     */     } 
/*  77 */     return str2;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int countQuotes(String paramString) {
/*  82 */     char[] arrayOfChar = paramString.toCharArray();
/*  83 */     byte b1 = 0; byte b2; int i;
/*  84 */     for (b2 = 0, i = arrayOfChar.length; b2 < i; b2++) {
/*     */       
/*  86 */       if (arrayOfChar[b2] == '"') b1++; 
/*     */     } 
/*  88 */     return b1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String[] splitRecord(String paramString) throws MalformedCsvException {
/*  93 */     int[] arrayOfInt = upshiftQuoteString(paramString);
/*     */     
/*  95 */     List<int[]> list = splitShifted(arrayOfInt);
/*  96 */     int i = list.size();
/*  97 */     String[] arrayOfString = new String[i];
/*  98 */     for (byte b = 0; b < i; b++)
/*  99 */       arrayOfString[b] = downshift(list.get(b)); 
/* 100 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void debugPrint(int[] paramArrayOfint) {
/* 105 */     int i = paramArrayOfint.length;
/* 106 */     char[] arrayOfChar = new char[i];
/* 107 */     for (byte b = 0; b < i; b++)
/* 108 */       arrayOfChar[b] = isShifted(paramArrayOfint[b]) ? '_' : (char)paramArrayOfint[b]; 
/* 109 */     System.err.println(new String(arrayOfChar));
/*     */   }
/*     */ 
/*     */   
/*     */   private static List splitShifted(int[] paramArrayOfint) {
/* 114 */     ArrayList<int[]> arrayList = new ArrayList();
/*     */     
/* 116 */     int i = 0; byte b; int j;
/* 117 */     for (b = 0, j = paramArrayOfint.length; b <= j; b++) {
/*     */       
/* 119 */       if (b == j || paramArrayOfint[b] == 44) {
/*     */         
/* 121 */         int k = b - i;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 126 */         int n = -1; int m;
/* 127 */         for (m = i; m <= b; m++) {
/*     */           
/* 129 */           if (m == b) {
/*     */             
/* 131 */             n = 0;
/*     */             break;
/*     */           } 
/* 134 */           if (paramArrayOfint[m] != 32 && paramArrayOfint[m] != 9)
/*     */             break; 
/*     */         } 
/* 137 */         if (n < 0)
/*     */         {
/* 139 */           if (m == b - 1) {
/* 140 */             n = 1;
/*     */           } else {
/*     */             
/* 143 */             for (n = b - m; n > 0; n--) {
/*     */               
/* 145 */               int i1 = m + n - 1;
/* 146 */               if (paramArrayOfint[i1] != 32 && paramArrayOfint[i1] != 9) {
/*     */                 break;
/*     */               }
/*     */             } 
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 158 */         int[] arrayOfInt = new int[n];
/* 159 */         if (n > 0)
/* 160 */           System.arraycopy(paramArrayOfint, m, arrayOfInt, 0, n); 
/* 161 */         arrayList.add(arrayOfInt);
/* 162 */         i = b + 1;
/*     */       } 
/*     */     } 
/* 165 */     return arrayList;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String downshift(int[] paramArrayOfint) {
/* 170 */     int i = paramArrayOfint.length;
/* 171 */     char[] arrayOfChar = new char[i];
/* 172 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 174 */       int j = paramArrayOfint[b];
/* 175 */       arrayOfChar[b] = (char)(isShifted(j) ? (j >>> 8) : j);
/*     */     } 
/* 177 */     return new String(arrayOfChar);
/*     */   }
/*     */   
/*     */   private static boolean isShifted(int paramInt) {
/* 181 */     return ((paramInt & 0x2000000) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] upshiftQuoteString(String paramString) throws MalformedCsvException {
/* 187 */     char[] arrayOfChar = paramString.toCharArray();
/* 188 */     int[] arrayOfInt1 = new int[arrayOfChar.length];
/*     */     
/* 190 */     EscapedCharReader escapedCharReader = new EscapedCharReader(arrayOfChar);
/* 191 */     byte b = 0;
/* 192 */     boolean bool = false;
/*     */     int i;
/* 194 */     for (i = escapedCharReader.read(bool); i >= 0; i = escapedCharReader.read(bool)) {
/*     */ 
/*     */       
/* 197 */       if (i == 34) {
/* 198 */         bool = !bool ? true : false;
/*     */       } else {
/* 200 */         arrayOfInt1[b++] = findShiftyChar(i, bool);
/*     */       } 
/*     */     } 
/* 203 */     int[] arrayOfInt2 = new int[b];
/* 204 */     System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, b);
/* 205 */     return arrayOfInt2;
/*     */   }
/*     */   
/*     */   private static int findShiftyChar(int paramInt, boolean paramBoolean) {
/* 209 */     return paramBoolean ? (paramInt << 8 | 0x2000000) : paramInt;
/*     */   }
/*     */   private static int escape(int paramInt) {
/* 212 */     return paramInt | 0x1000000;
/*     */   }
/*     */   private static boolean isEscaped(int paramInt) {
/* 215 */     return ((paramInt & 0x1000000) != 0);
/*     */   }
/*     */   
/*     */   private static class EscapedCharReader
/*     */   {
/*     */     char[] chars;
/*     */     int finger;
/*     */     
/*     */     EscapedCharReader(char[] param1ArrayOfchar) {
/* 224 */       this.chars = param1ArrayOfchar;
/* 225 */       this.finger = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     int read(boolean param1Boolean) throws MalformedCsvException {
/* 230 */       if (this.finger < this.chars.length) {
/*     */         
/* 232 */         char c = this.chars[this.finger++];
/* 233 */         if (c == '"' && param1Boolean) {
/*     */           
/* 235 */           if (this.finger < this.chars.length) {
/*     */             
/* 237 */             char c1 = this.chars[this.finger];
/* 238 */             if (c1 == '"') {
/*     */               
/* 240 */               this.finger++;
/*     */               
/* 242 */               return FastCsvUtils.escape(c1);
/*     */             } 
/* 244 */             return c;
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 249 */           return c;
/*     */         } 
/*     */ 
/*     */         
/* 253 */         return c;
/*     */       } 
/*     */       
/* 256 */       return -1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/csv/FastCsvUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
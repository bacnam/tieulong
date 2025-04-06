/*     */ package com.mchange.v1.util;
/*     */ 
/*     */ import com.mchange.v2.lang.ObjectUtils;
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
/*     */ public final class ArrayUtils
/*     */ {
/*     */   public static int indexOf(Object[] paramArrayOfObject, Object paramObject) {
/*     */     byte b;
/*     */     int i;
/*  48 */     for (b = 0, i = paramArrayOfObject.length; b < i; b++) {
/*  49 */       if (paramObject.equals(paramArrayOfObject[b])) return b; 
/*  50 */     }  return -1;
/*     */   }
/*     */   public static int identityIndexOf(Object[] paramArrayOfObject, Object paramObject) {
/*     */     byte b;
/*     */     int i;
/*  55 */     for (b = 0, i = paramArrayOfObject.length; b < i; b++) {
/*  56 */       if (paramObject == paramArrayOfObject[b]) return b; 
/*  57 */     }  return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean startsWith(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/*  62 */     int i = paramArrayOfbyte1.length;
/*  63 */     int j = paramArrayOfbyte2.length;
/*  64 */     if (i < j)
/*  65 */       return false; 
/*  66 */     for (byte b = 0; b < j; b++) {
/*  67 */       if (paramArrayOfbyte1[b] != paramArrayOfbyte2[b])
/*  68 */         return false; 
/*  69 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int hashArray(Object[] paramArrayOfObject) {
/*  77 */     int i = paramArrayOfObject.length;
/*  78 */     int j = i;
/*  79 */     for (byte b = 0; b < i; b++) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  84 */       int k = ObjectUtils.hashOrZero(paramArrayOfObject[b]);
/*  85 */       int m = b % 32;
/*  86 */       int n = k >>> m;
/*  87 */       n |= k << 32 - m;
/*  88 */       j ^= n;
/*     */     } 
/*  90 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int hashArray(int[] paramArrayOfint) {
/*  98 */     int i = paramArrayOfint.length;
/*  99 */     int j = i;
/* 100 */     for (byte b = 0; b < i; b++) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 105 */       int k = paramArrayOfint[b];
/* 106 */       int m = b % 32;
/* 107 */       int n = k >>> m;
/* 108 */       n |= k << 32 - m;
/* 109 */       j ^= n;
/*     */     } 
/* 111 */     return j;
/*     */   }
/*     */   
/*     */   public static int hashOrZeroArray(Object[] paramArrayOfObject) {
/* 115 */     return (paramArrayOfObject == null) ? 0 : hashArray(paramArrayOfObject);
/*     */   }
/*     */   public static int hashOrZeroArray(int[] paramArrayOfint) {
/* 118 */     return (paramArrayOfint == null) ? 0 : hashArray(paramArrayOfint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String stringifyContents(Object[] paramArrayOfObject) {
/* 125 */     StringBuffer stringBuffer = new StringBuffer();
/* 126 */     stringBuffer.append("[ "); byte b; int i;
/* 127 */     for (b = 0, i = paramArrayOfObject.length; b < i; b++) {
/*     */       
/* 129 */       if (b != 0)
/* 130 */         stringBuffer.append(", "); 
/* 131 */       stringBuffer.append(paramArrayOfObject[b].toString());
/*     */     } 
/* 133 */     stringBuffer.append(" ]");
/* 134 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String toString(String[] paramArrayOfString, int paramInt) {
/* 141 */     StringBuffer stringBuffer = new StringBuffer(paramInt);
/* 142 */     boolean bool = true;
/* 143 */     stringBuffer.append('['); byte b; int i;
/* 144 */     for (b = 0, i = paramArrayOfString.length; b < i; b++) {
/*     */       
/* 146 */       if (bool) {
/* 147 */         bool = false;
/*     */       } else {
/* 149 */         stringBuffer.append(',');
/* 150 */       }  stringBuffer.append(paramArrayOfString[b]);
/*     */     } 
/* 152 */     stringBuffer.append(']');
/* 153 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String toString(boolean[] paramArrayOfboolean) {
/* 158 */     String[] arrayOfString = new String[paramArrayOfboolean.length];
/* 159 */     int i = 0; byte b; int j;
/* 160 */     for (b = 0, j = paramArrayOfboolean.length; b < j; b++) {
/*     */       
/* 162 */       String str = String.valueOf(paramArrayOfboolean[b]);
/* 163 */       i += str.length();
/* 164 */       arrayOfString[b] = str;
/*     */     } 
/* 166 */     return toString(arrayOfString, i + paramArrayOfboolean.length + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String toString(byte[] paramArrayOfbyte) {
/* 171 */     String[] arrayOfString = new String[paramArrayOfbyte.length];
/* 172 */     int i = 0; byte b; int j;
/* 173 */     for (b = 0, j = paramArrayOfbyte.length; b < j; b++) {
/*     */       
/* 175 */       String str = String.valueOf(paramArrayOfbyte[b]);
/* 176 */       i += str.length();
/* 177 */       arrayOfString[b] = str;
/*     */     } 
/* 179 */     return toString(arrayOfString, i + paramArrayOfbyte.length + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String toString(char[] paramArrayOfchar) {
/* 184 */     String[] arrayOfString = new String[paramArrayOfchar.length];
/* 185 */     int i = 0; byte b; int j;
/* 186 */     for (b = 0, j = paramArrayOfchar.length; b < j; b++) {
/*     */       
/* 188 */       String str = String.valueOf(paramArrayOfchar[b]);
/* 189 */       i += str.length();
/* 190 */       arrayOfString[b] = str;
/*     */     } 
/* 192 */     return toString(arrayOfString, i + paramArrayOfchar.length + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String toString(short[] paramArrayOfshort) {
/* 197 */     String[] arrayOfString = new String[paramArrayOfshort.length];
/* 198 */     int i = 0; byte b; int j;
/* 199 */     for (b = 0, j = paramArrayOfshort.length; b < j; b++) {
/*     */       
/* 201 */       String str = String.valueOf(paramArrayOfshort[b]);
/* 202 */       i += str.length();
/* 203 */       arrayOfString[b] = str;
/*     */     } 
/* 205 */     return toString(arrayOfString, i + paramArrayOfshort.length + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String toString(int[] paramArrayOfint) {
/* 210 */     String[] arrayOfString = new String[paramArrayOfint.length];
/* 211 */     int i = 0; byte b; int j;
/* 212 */     for (b = 0, j = paramArrayOfint.length; b < j; b++) {
/*     */       
/* 214 */       String str = String.valueOf(paramArrayOfint[b]);
/* 215 */       i += str.length();
/* 216 */       arrayOfString[b] = str;
/*     */     } 
/* 218 */     return toString(arrayOfString, i + paramArrayOfint.length + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String toString(long[] paramArrayOflong) {
/* 223 */     String[] arrayOfString = new String[paramArrayOflong.length];
/* 224 */     int i = 0; byte b; int j;
/* 225 */     for (b = 0, j = paramArrayOflong.length; b < j; b++) {
/*     */       
/* 227 */       String str = String.valueOf(paramArrayOflong[b]);
/* 228 */       i += str.length();
/* 229 */       arrayOfString[b] = str;
/*     */     } 
/* 231 */     return toString(arrayOfString, i + paramArrayOflong.length + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String toString(float[] paramArrayOffloat) {
/* 236 */     String[] arrayOfString = new String[paramArrayOffloat.length];
/* 237 */     int i = 0; byte b; int j;
/* 238 */     for (b = 0, j = paramArrayOffloat.length; b < j; b++) {
/*     */       
/* 240 */       String str = String.valueOf(paramArrayOffloat[b]);
/* 241 */       i += str.length();
/* 242 */       arrayOfString[b] = str;
/*     */     } 
/* 244 */     return toString(arrayOfString, i + paramArrayOffloat.length + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String toString(double[] paramArrayOfdouble) {
/* 249 */     String[] arrayOfString = new String[paramArrayOfdouble.length];
/* 250 */     int i = 0; byte b; int j;
/* 251 */     for (b = 0, j = paramArrayOfdouble.length; b < j; b++) {
/*     */       
/* 253 */       String str = String.valueOf(paramArrayOfdouble[b]);
/* 254 */       i += str.length();
/* 255 */       arrayOfString[b] = str;
/*     */     } 
/* 257 */     return toString(arrayOfString, i + paramArrayOfdouble.length + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String toString(Object[] paramArrayOfObject) {
/* 262 */     String[] arrayOfString = new String[paramArrayOfObject.length];
/* 263 */     int i = 0; byte b; int j;
/* 264 */     for (b = 0, j = paramArrayOfObject.length; b < j; b++) {
/*     */       String str;
/*     */       
/* 267 */       Object object = paramArrayOfObject[b];
/* 268 */       if (object instanceof Object[]) {
/* 269 */         str = toString((Object[])object);
/* 270 */       } else if (object instanceof double[]) {
/* 271 */         str = toString((double[])object);
/* 272 */       } else if (object instanceof float[]) {
/* 273 */         str = toString((float[])object);
/* 274 */       } else if (object instanceof long[]) {
/* 275 */         str = toString((long[])object);
/* 276 */       } else if (object instanceof int[]) {
/* 277 */         str = toString((int[])object);
/* 278 */       } else if (object instanceof short[]) {
/* 279 */         str = toString((short[])object);
/* 280 */       } else if (object instanceof char[]) {
/* 281 */         str = toString((char[])object);
/* 282 */       } else if (object instanceof byte[]) {
/* 283 */         str = toString((byte[])object);
/* 284 */       } else if (object instanceof boolean[]) {
/* 285 */         str = toString((boolean[])object);
/*     */       } else {
/* 287 */         str = String.valueOf(paramArrayOfObject[b]);
/* 288 */       }  i += str.length();
/* 289 */       arrayOfString[b] = str;
/*     */     } 
/* 291 */     return toString(arrayOfString, i + paramArrayOfObject.length + 1);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/ArrayUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */